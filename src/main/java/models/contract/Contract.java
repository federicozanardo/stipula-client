package models.contract;

import lib.datastructures.Triple;
import vm.dfa.DfaState;
import vm.dfa.FinalStates;
import vm.dfa.TransitionData;

import java.io.Serializable;
import java.util.ArrayList;

public class Contract implements Serializable {
    private final String sourceCode;
    private final String bytecode;
    private final DfaState initialState;
    private final FinalStates finalStates;
    private final ArrayList<Triple<DfaState, DfaState, TransitionData>> transitions;

    public Contract(String sourceCode,
                    String bytecode,
                    DfaState initialState,
                    FinalStates finalStates,
                    ArrayList<Triple<DfaState, DfaState, TransitionData>> transitions
    ) {
        this.sourceCode = sourceCode;
        this.bytecode = bytecode;
        this.initialState = initialState;
        this.finalStates = finalStates;
        this.transitions = transitions;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public String getBytecode() {
        return bytecode;
    }

    public DfaState getInitialState() {
        return initialState;
    }

    public FinalStates getFinalStates() {
        return finalStates;
    }

    public ArrayList<Triple<DfaState, DfaState, TransitionData>> getTransitions() {
        return transitions;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "sourceCode='" + sourceCode + '\'' +
                ", bytecode='" + bytecode + '\'' +
                ", initialState=" + initialState +
                ", finalStates=" + finalStates +
                ", transitions=" + transitions +
                '}';
    }
}
