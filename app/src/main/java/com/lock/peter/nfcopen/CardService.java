package com.lock.peter.nfcopen;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

import de.greenrobot.event.EventBus;

public class CardService extends HostApduService {
    private static final String TAG = "CardService";
    // AID for our card service.
    private static final String AID = "F222222222";
    // "OK" status word sent in response to SELECT AID command (0x9000)
    private static final byte[] SELECT_OK_SW = hexStringToByteArray("9000");
    // "UNKNOWN" status word sent in response to invalid APDU command (0x0000)
    private static final byte[] UNKNOWN_CMD_SW = hexStringToByteArray("0000");
    private static final byte[] SELECT_APDU = buildSelectApdu(AID);
    private static final byte[] GET_PIN_APDU = buildGetPinApdu();

    private EventBus bus = EventBus.getDefault();

    //Build APDU for SELECT AID command. This command indicates which service a reader is
    //interested in communicating with. See ISO 7816-4.
    private static byte[] buildSelectApdu(String aid) {
        // ISO-DEP command HEADER for selecting an AID.
        // Format: [Class | Instruction | Parameter 1 | Parameter 2]
        final String SELECT_APDU_HEADER = "00A40400";
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return hexStringToByteArray(SELECT_APDU_HEADER + String.format("%02X",
                aid.length() / 2) + aid);
    }


    // Build APDU for GET_DATA command. See ISO 7816-4
    private static byte[] buildGetPinApdu() {
        // Format: [Class | Instruction | Parameter 1 | Parameter 2]
        final String GET_PIN_APDU_HEADER = "00CA0000";
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return hexStringToByteArray(GET_PIN_APDU_HEADER + "0FFF");
    }

    // Utility method to concatenate two byte arrays.
    private static byte[] ConcatArrays(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

     // Utility method to convert a hexadecimal string to a byte string.#
     // Behavior with input strings containing non-hexadecimal characters is undefined
    private static byte[] hexStringToByteArray(String s) throws IllegalArgumentException {
        int len = s.length();
        if (len % 2 == 1) {
            throw new IllegalArgumentException("Hex string must have even number of characters");
        }
        byte[] data = new byte[len / 2]; // Allocate 1 byte per 2 hex characters
        for (int i = 0; i < len; i += 2) {
            // Convert each character into a integer (base-16), then bit-shift into place
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    //Called when service is terminated
    @Override
    public void onDeactivated(int reason) {
    }

    /**
     * This method will be called when a command APDU has been received from a remote device. A
     * response APDU can be provided directly by returning a byte-array in this method. In general
     * response APDUs must be sent as quickly as possible, given the fact that the user is likely
     * holding his device over an NFC reader when this method is called.
     */
    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        Log.i(TAG, "processCommandApdu");
        String username = User.getCurrentUser();
        String userSessionToken = User.getSessionToken();
        //If Received APDU contained select command
        if (Arrays.equals(SELECT_APDU, commandApdu)) {
            String accessRequest = DoorOptions.prepareNdefPayload(username, userSessionToken);
            byte[] accessRequestBytes = accessRequest.getBytes();
            Log.i(TAG, "Responding to APDU with " + accessRequest);
            return ConcatArrays(accessRequestBytes, SELECT_OK_SW);
        }//If Following APDU contains Get data request door requires pin
        else if (Arrays.equals(GET_PIN_APDU, commandApdu)) {
            return requiresPin();
        } else {
            return UNKNOWN_CMD_SW;
        }
    }

    private byte[] requiresPin(){
        byte[] nullArray = {};
        //If the user has already entered door pin transmit pin
        if (DoorOptions.isPinSet()) {
            int pin = DoorOptions.getPin();
            Log.i(TAG, "Sending Pin: " + pin);
            return ConcatArrays(nullArray, SELECT_OK_SW);
        }
        //If the user has not previously entered a door pin present them with a pin dialogue
        else {
            Log.i(TAG, "Requesting Door Pin Entry");
            Events.PinRequest pr = new Events.PinRequest();
            bus.post(pr);
            return nullArray;
        }
    }
}
