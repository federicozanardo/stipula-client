package vm.dfa;

import java.io.Serializable;
import java.util.HashSet;

public class FinalStates implements Serializable {
    private final HashSet<DfaState> acceptanceStates;
    private final HashSet<DfaState> failingStates;

    public FinalStates(HashSet<DfaState> acceptanceStates, HashSet<DfaState> failingStates) {
        this.acceptanceStates = acceptanceStates;
        this.failingStates = failingStates;
    }

    public HashSet<DfaState> getAcceptanceStates() {
        return acceptanceStates;
    }

    public HashSet<DfaState> getFailingStates() {
        return failingStates;
    }

    public boolean isStateInFinalStates(DfaState state) {
        return (acceptanceStates.contains(state) || failingStates.contains(state));
    }

    public boolean isStateInAcceptanceStates(DfaState state) {
        return acceptanceStates.contains(state);
    }

    public boolean isStateInFailingStates(DfaState state) {
        return failingStates.contains(state);
    }

    @Override
    public String toString() {
        return "FinalStates{" +
                "acceptanceStates=" + acceptanceStates +
                ", failingStates=" + failingStates +
                '}';
    }
}
