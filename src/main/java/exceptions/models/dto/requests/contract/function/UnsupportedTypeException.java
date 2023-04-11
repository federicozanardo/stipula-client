package exceptions.models.dto.requests.contract.function;

public class UnsupportedTypeException extends Exception {
    public UnsupportedTypeException() {
        super();
    }

    public UnsupportedTypeException(String message) {
        super(message);
    }
}
