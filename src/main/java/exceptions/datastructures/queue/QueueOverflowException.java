package exceptions.datastructures.queue;

public class QueueOverflowException extends Exception {
    public QueueOverflowException() {
        super();
    }

    public QueueOverflowException(String message) {
        super(message);
    }
}
