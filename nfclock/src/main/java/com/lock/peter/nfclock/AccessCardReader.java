/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lock.peter.nfclock;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * Callback class, invoked when an NFC card is scanned while the device is running in reader mode.
 * <p/>
 * Reader mode can be invoked by calling NfcAdapter
 */
public class AccessCardReader implements NfcAdapter.ReaderCallback {
    public interface AccountCallback {
        public void onAccountReceived(String account);
    }

    private static final String TAG = "AccessCardReader";
    // AID for our loyalty card service.
    private static final String APPLICATION_AID = "F222222222";
    // ISO-DEP command HEADER for selecting an AID.
    // Format: [Class | Instruction | Parameter 1 | Parameter 2]
    private static final String SELECT_APDU_HEADER = "00A40400";
    private static final String GET_DATA_APDU_HEADER = "00CA0000";
    // "OK" status word sent in response to SELECT AID command (0x9000)
    private static final byte[] SELECT_OK_SW = {(byte) 0x90, (byte) 0x00};

    private String gotData = "", finalGotData = "";
    private Door door;
    long timeTaken = 0;

    // Weak reference to prevent retain loop. mAccountCallback is responsible for exiting
    // foreground mode before it becomes invalid (e.g. during onPause() or onStop()).
    private WeakReference<AccountCallback> mAccountCallback;


    //TODO Change to use EventBus instead of passing door
    public AccessCardReader(AccountCallback accountCallback, Door door) {
        mAccountCallback = new WeakReference<AccountCallback>(accountCallback);
        this.door = door;
    }

    /**
     * Callback when a new tag is discovered by the system.
     *
     * @param tag Discovered tag
     */
    @Override
    public void onTagDiscovered(Tag tag) {
        Log.i(TAG, "New tag discovered");
        gotData = "";
        IsoDep isoDep = IsoDep.get(tag);
        if (isoDep != null) {
            try {
                // Connect to the remote NFC device
                isoDep.connect();
                Log.i(TAG, "Timeout = " + isoDep.getTimeout());
                isoDep.setTimeout(3600);
                Log.i(TAG, "Timeout = " + isoDep.getTimeout());
                Log.i(TAG, "MaxTransceiveLength = " + isoDep.getMaxTransceiveLength());
                Log.i(TAG, "Requesting remote AID: " + APPLICATION_AID);
                byte[] selCommand = BuildSelectApdu(APPLICATION_AID);

                // Send command to remote device
                Log.i(TAG, "Sending: " + ByteArrayToHexString(selCommand));
                byte[] result = isoDep.transceive(selCommand);
                // If AID is successfully selected, 0x9000 is returned as the status word (last 2
                // bytes of the result) by convention. Everything before the status word is
                // optional payload, which is used here to hold the account number.
                int resultLength = result.length;
                byte[] statusWord = {result[resultLength - 2], result[resultLength - 1]};
                byte[] payload = Arrays.copyOf(result, resultLength - 2);
                if (Arrays.equals(SELECT_OK_SW, statusWord)) {
                    // The remote NFC device will immediately respond with its stored account number
                    String accountNumber = new String(payload, "UTF-8");
                    Log.i(TAG, "Received: " + accountNumber);
                    // Inform DoorActivity of received account number
                    timeTaken = System.currentTimeMillis();
                    while (!(gotData.contains("END")) && door.isRequiresPin()) {
                        byte[] getCommand = BuildGetDataApdu();
                        Log.i(TAG, "Sending: " + ByteArrayToHexString(getCommand));
                        isoDep.setTimeout(20000);
                        result = isoDep.transceive(getCommand);
                        resultLength = result.length;
                        byte[] statusWordNew = {result[resultLength - 2], result[resultLength - 1]};
                        payload = Arrays.copyOf(result, resultLength - 2);
                        //Will Be Used For Pin Feature
                        if (Arrays.equals(SELECT_OK_SW, statusWordNew)) {
                            gotData = new String(payload, "UTF-8");
                            Log.i(TAG, "Received: " + gotData);
                            finalGotData = finalGotData + gotData;
                            Log.i(TAG, "Data transferred : " + finalGotData.length());
                        }
                        Log.i(TAG, "Got call back");
                        mAccountCallback.get().onAccountReceived(accountNumber);

                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "Error communicating with card: " + e.toString());
            }
        }
    }

    /**
     * Build APDU for SELECT AID command. This command indicates which service a reader is
     * interested in communicating with. See ISO 7816-4.
     *
     * @param aid Application ID (AID) to select
     * @return APDU for SELECT AID command
     */
    public static byte[] BuildSelectApdu(String aid) {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return HexStringToByteArray(SELECT_APDU_HEADER + String.format("%02X", aid.length() / 2) + aid);
    }

    /**
     * Build APDU for GET_DATA command. See ISO 7816-4.
     *
     * @return APDU for SELECT AID command
     */
    public static byte[] BuildGetDataApdu() {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return HexStringToByteArray(GET_DATA_APDU_HEADER + "0FFF");
    }

    /**
     * Utility class to convert a byte array to a hexadecimal string.
     *
     * @param bytes Bytes to convert
     * @return String, containing hexadecimal representation.
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Utility class to convert a hexadecimal string to a byte string.
     * Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     */
    public static byte[] HexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}
