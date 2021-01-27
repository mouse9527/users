package com.mouse.users;

import java.util.Locale;

public interface MessageTranslator {
    Message translate(Throwable throwable, Locale local);

    class Message {
        private final String message;
        private final int status;

        public Message(String message, int status) {
            this.message = message;
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public int getStatus() {
            return status;
        }
    }
}
