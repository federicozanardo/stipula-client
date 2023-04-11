package vm.types;

public class StrType extends Type {
    private final String value;

    public StrType() {
        this.value = "";
    }

    public StrType(String value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "str";
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "StrType{" +
                "value='" + value + '\'' +
                '}';
    }
}
