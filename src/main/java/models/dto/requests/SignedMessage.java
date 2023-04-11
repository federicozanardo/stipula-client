package models.dto.requests;

import java.util.HashMap;

public class SignedMessage {
    private final Message message;
    private final HashMap<String, String> signatures;

    public SignedMessage(Message message, HashMap<String, String> signatures) {
        this.message = message;
        this.signatures = signatures;
    }

    public Message getMessage() {
        return message;
    }

    public HashMap<String, String> getSignatures() {
        return signatures;
    }

    @Override
    public String toString() {
        return "SignedMessage{" +
                "message=" + message +
                ", signatures=" + signatures +
                '}';
    }
}
