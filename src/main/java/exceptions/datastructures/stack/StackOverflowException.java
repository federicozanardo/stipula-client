package exceptions.datastructures.stack;

public class StackOverflowException extends Exception {
    public StackOverflowException() {
        super();
    }

    public StackOverflowException(String message) {
        super(message);
    }
}
