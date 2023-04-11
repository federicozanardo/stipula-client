package vm.types;

public class TraceChange {
    private final Type value;
    private final boolean isChanged;

    public TraceChange(Type value) {
        this.value = value;
        this.isChanged = false;
    }

    public TraceChange(Type value, boolean isChanged) {
        this.value = value;
        this.isChanged = isChanged;
    }

    public Type getValue() {
        return value;
    }

    /**
     * @return true, if the value is changed,; false, otherwise.
     */
    public boolean isChanged() {
        return isChanged;
    }

    @Override
    public String toString() {
        return "TraceChange{" +
                "value=" + value +
                ", isChanged=" + isChanged +
                '}';
    }
}
