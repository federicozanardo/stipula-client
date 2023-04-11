package vm.types;

public class IntType extends Type {
    private final Integer value;

    public IntType() {
        this.value = 0;
    }

    public IntType(Integer value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "int";
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "IntType{" +
                "value=" + value +
                '}';
    }
}
