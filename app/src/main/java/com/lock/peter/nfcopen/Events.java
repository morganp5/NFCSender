package com.lock.peter.nfcopen;

/**
 * Created by Peter on 26/02/2015.
 */
public class Events {

    public static class PinRequest {
    }

    public static class buttonPressed {
        String text;
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
