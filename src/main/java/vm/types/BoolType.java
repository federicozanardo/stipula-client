package vm.types;

public class BoolType extends Type {
    private final Boolean value;

    public BoolType() {
        this.value = true;
    }

    public BoolType(Boolean value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "bool";
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "BoolType{" +
                "value=" + value +
                '}';
    }
}
