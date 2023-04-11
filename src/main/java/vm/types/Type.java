package vm.types;

import java.io.Serializable;

public abstract class Type implements Serializable {
    public abstract String getType();

    public abstract Object getValue();
}
