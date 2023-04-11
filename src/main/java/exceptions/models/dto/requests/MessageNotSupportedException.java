package exceptions.models.dto.requests;

public class MessageNotSupportedException extends Exception {
    public MessageNotSupportedException() {
        super();
    }

    public MessageNotSupportedException(String message) {
        super(message);
    }
}
