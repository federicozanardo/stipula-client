package exceptions.datastructures.queue;

public class QueueUnderflowException extends Exception {
    public QueueUnderflowException() {
        super();
    }

    public QueueUnderflowException(String message) {
        super(message);
    }
}
