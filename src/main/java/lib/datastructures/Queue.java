package lib.datastructures;

import exceptions.datastructures.queue.QueueOverflowException;
import exceptions.datastructures.queue.QueueUnderflowException;

import java.util.ArrayList;

public class Queue<T> {
    /**
     * Data structure used to represent the queue.
     */
    ArrayList<T> Q;

    /**
     * Maximum dimension of the queue.
     */
    int length;

    /**
     * Pointer to the first element of the queue.
     */
    int head = 0;

    /**
     * Pointer to the last element of the queue.
     */
    int tail = 0;

    public Queue(int length) {
        this.length = length;
        this.Q = new ArrayList<T>(length);
    }

    /**
     * Enqueue a value in the queue.
     *
     * @param value: value to be enqueued.
     */
    public void enqueue(T value) throws QueueOverflowException {
        if (this.isFull()) {
            throw new QueueOverflowException();
        } else {
            Q.add(tail, value);
            if (tail == length) {
                tail = 1;
            } else {
                tail++;
            }
        }
    }

    /**
     * Dequque a value from the queue.
     *
     * @return the value dequeued from the queue.
     */
    public T dequeue() throws QueueUnderflowException {
        if (this.isEmpty()) {
            throw new QueueUnderflowException();
        } else {
            T value = Q.get(head);
            if (head == length) {
                head = 1;
            } else {
                head++;
            }
            return value;
        }
    }

    // Other methods

    /**
     * Check if the queue is full or not.
     *
     * @return true, if the queue is full; otherwise, false;
     */
    public boolean isFull() {
        return head == (tail + 1) || (head == 1 && tail == length);
    }

    /**
     * Check if the queue is empty or not.
     *
     * @return true, if the queue is empty; otherwise, false;
     */
    public boolean isEmpty() {
        return head == tail;
    }

    @Override
    public String toString() {
        return "Queue{" +
                "Q=" + Q +
                ", length=" + length +
                ", head=" + head +
                ", tail=" + tail +
                '}';
    }
}