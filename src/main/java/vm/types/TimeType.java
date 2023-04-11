package vm.types;

public class TimeType extends Type {
    private final Integer value; // seconds

    public TimeType() {
        this.value = 0;
    }

    public TimeType(int seconds) {
        this.value = seconds;
    }

    @Override
    public String getType() {
        return "time";
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TimeType{" +
                "value=" + value +
                '}';
    }
}
