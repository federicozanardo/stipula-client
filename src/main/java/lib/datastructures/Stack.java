package lib.datastructures;

import exceptions.datastructures.stack.StackOverflowException;
import exceptions.datastructures.stack.StackUnderflowException;

import java.util.ArrayList;

public class Stack<T> {

    /**
     * Data structure used to represent the stack.
     */
    ArrayList<T> S;

    /**
     * Represents the most recently inserted element.
     */
    int top = -1;

    /**
     * Maximum dimension of the stack.
     */
    int size;

    public Stack(int size) {
        this.size = size;
        this.S = new ArrayList<T>(size);
    }

    /**
     * Push an element into the stack.
     *
     * @param value: value to be pushed in the stack.
     * @throws StackOverflowException: throws when the current value exceed the stack space limit.
     */
    public void push(T value) throws StackOverflowException {
        // Check if the stack is full
        if (this.isFull()) {
            throw new StackOverflowException();
        } else {
            // Increment top to go to the next position
            top++;

            // Over-writing existing element
            if (S.size() > top) {
                S.set(top, value);
            } else {
                // Creating new element
                S.add(value);
            }
        }
    }

    /**
     * Return the last element and remove it from the stack.
     *
     * @return the last element from the stack.
     * @throws StackUnderflowException: throws when someone tries to pop a value from an empty stack.
     */
    public T pop() throws StackUnderflowException {
        // Check if the stack is empty
        if (this.isEmpty()) {
            throw new StackUnderflowException();
        } else {
            T value = S.get(top);

            // Delete the last element by decrementing the `top` value
            top--;
            return value;
        }
    }

    /**
     * Check if stack is empty or not.
     *
     * @return true, if the stack is empty; false, otherwise.
     */
    public boolean isEmpty() {
        return top == -1;
    }

    // Other methods

    /**
     * Check if stack is full or not.
     *
     * @return true, if the stack is full; false, otherwise.
     */
    public boolean isFull() {
        return top == size - 1;
    }

    @Override
    public String toString() {
        return "Stack{" +
                "S=" + S +
                ", top=" + top +
                ", size=" + size +
                '}';
    }
}