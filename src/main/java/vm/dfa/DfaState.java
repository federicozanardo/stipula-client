package vm.dfa;

import java.io.Serializable;

public class DfaState implements Serializable {
    private final String name;

    public DfaState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "DfaState{" +
                "name='" + name + '\'' +
                '}';
    }
}
