package vm.dfa;

import lib.datastructures.Triple;

import java.io.Serializable;
import java.util.ArrayList;

public class DeterministicFiniteAutomata implements Serializable {
    private DfaState currentState;
    private final DfaState initialState;
    private final FinalStates finalStates;
    private final ArrayList<Triple<DfaState, DfaState, TransitionData>> transitions;

    public DeterministicFiniteAutomata(
            DfaState initialState,
            FinalStates finalStates,
            ArrayList<Triple<DfaState, DfaState, TransitionData>> transitions
    ) {
        this.initialState = initialState;
        this.currentState = initialState;
        this.finalStates = finalStates;
        this.transitions = transitions;
    }

    /**
     * Check if the given state is the next state of the current state, given a party.
     *
     * @param party:              the party that made the request.
     * @param functionName:       the name of the function called.
     * @param candidateNextState: state to check if it is the next state of the current state.
     * @param argumentTypes:      the argument types of the function called.
     * @return true, if the given state is the next state of the current state; false, otherwise.
     */
    public boolean isNextState(String party, String functionName, DfaState candidateNextState, ArrayList<String> argumentTypes) {
        /**
         * To verify if candidateNextState is effectively the next state of the current state, there is the need to match:
         * - function name
         * - party that made the function call
         * - arguments of the function
         */

        int i = 0;
        boolean found = false;

        while (i < transitions.size() && !found) {
            Triple<DfaState, DfaState, TransitionData> transition = transitions.get(i);
            DfaState sourceState = transition.getFirst();
            DfaState destinationState = transition.getSecond();

            // Look only at ContractCallByParty objects
            if (transition.getThird() instanceof ContractCallByParty) {
                ContractCallByParty callByParty = (ContractCallByParty) transition.getThird();

                // Check if the party can call the function
                if (callByParty.getParty().equals(party)) {

                    // Check function name
                    if (callByParty.getFunctionName().equals(functionName)) {

                        // Check if the source state matches with the current state
                        if (currentState.getName().equals(sourceState.getName())) {

                            // Check if it is the correct destination state
                            if (destinationState.getName().equals(candidateNextState.getName())) {

                                // Check arguments
                                int j = 0;
                                boolean areAllArgumentsCorrect = true;

                                while (j < callByParty.getArguments().size() && areAllArgumentsCorrect) {
                                    String argument = callByParty.getArguments().get(j);
                                    if (!argumentTypes.get(j).equals(argument)) {
                                        areAllArgumentsCorrect = false;
                                    }
                                    j++;
                                }

                                if (areAllArgumentsCorrect) {
                                    found = true;
                                }
                            }
                        }
                    }
                }
            }
            i++;
        }

        return found;
    }

    /**
     * Check if the given state is the next state of the current state, given an obligation function name.
     *
     * @param obligationFunctionName: obligation to call.
     * @param candidateNextState:     state to check if it is the next state of the current state.
     * @return true, if the given state is the next state of the current state; false, otherwise.
     */
    public boolean isNextState(String obligationFunctionName, DfaState candidateNextState) {
        int i = 0;
        boolean found = false;

        while (i < transitions.size() && !found) {
            Triple<DfaState, DfaState, TransitionData> transition = transitions.get(i);
            DfaState sourceState = transition.getFirst();
            DfaState destinationState = transition.getSecond();

            // Look only at ContractCallByEvent objects
            if (transition.getThird() instanceof ContractCallByEvent) {
                ContractCallByEvent callByEvent = (ContractCallByEvent) transition.getThird();

                // Check if the request has been made by the correct obligation (check function name)
                if (callByEvent.getObligationFunctionName().equals(obligationFunctionName)) {

                    // Check if the source state matches with the current state
                    if (currentState.getName().equals(sourceState.getName())) {

                        // Check if it is the correct destination state
                        if (destinationState.getName().equals(candidateNextState.getName())) {
                            found = true;
                        }
                    }
                }
            }
            i++;
        }

        return found;
    }

    /**
     * Update the state machine.
     *
     * @param party:         the party that made the request.
     * @param functionName:  the name of the function called.
     * @param argumentTypes: the argument types of the function called.
     */
    public void nextState(String party, String functionName, ArrayList<String> argumentTypes) {
        int i = 0;
        boolean found = false;

        while (i < transitions.size() && !found) {
            Triple<DfaState, DfaState, TransitionData> transition = transitions.get(i);
            DfaState sourceState = transition.getFirst();
            DfaState destinationState = transition.getSecond();

            // Look only at ContractCallByParty objects
            if (transition.getThird() instanceof ContractCallByParty) {
                ContractCallByParty callByParty = (ContractCallByParty) transition.getThird();

                // Check if the party can call the function
                if (callByParty.getParty().equals(party)) {

                    // Check function name
                    if (callByParty.getFunctionName().equals(functionName)) {

                        // Find the source state
                        if (currentState.getName().equals(sourceState.getName())) {

                            // Check arguments
                            int j = 0;
                            boolean areAllArgumentsCorrect = true;

                            while (j < callByParty.getArguments().size() && areAllArgumentsCorrect) {
                                String argument = callByParty.getArguments().get(j);
                                if (!argumentTypes.get(j).equals(argument)) {
                                    areAllArgumentsCorrect = false;
                                }
                                j++;
                            }

                            // Update the current state with the destination state
                            if (areAllArgumentsCorrect) {
                                found = true;
                                currentState = destinationState;
                            }
                        }
                    }
                }
            }
            i++;
        }
    }

    /**
     * Update the state machine.
     *
     * @param obligationFunctionName: the name of the obligation function called.
     */
    public void nextState(String obligationFunctionName) {
        int i = 0;
        boolean found = false;

        while (i < transitions.size() && !found) {
            Triple<DfaState, DfaState, TransitionData> transition = transitions.get(i);
            DfaState sourceState = transition.getFirst();
            DfaState destinationState = transition.getSecond();

            // Look only at ContractCallByEvent objects
            if (transition.getThird() instanceof ContractCallByEvent) {
                ContractCallByEvent callByEvent = (ContractCallByEvent) transition.getThird();

                // Check if the request has been made by the correct obligation (check function name)
                if (callByEvent.getObligationFunctionName().equals(obligationFunctionName)) {

                    // Find the source state
                    if (currentState.getName().equals(sourceState.getName())) {

                        // Update the current state with the destination state
                        found = true;
                        currentState = destinationState;
                    }
                }
            }
            i++;
        }
    }

    /**
     * Return the current state of the state machine.
     *
     * @return the current state.
     */
    public DfaState getCurrentState() {
        return currentState;
    }

    /**
     * Return the initial state of the state machine.
     *
     * @return the initial state.
     */
    public DfaState getInitialState() {
        return initialState;
    }

    /**
     * Check if the current state belongs to the end-states set.
     *
     * @return true, if the current state belongs to the end-states set; false otherwise.
     */
    public boolean isCurrentStateEndState() {
        return finalStates.isStateInFinalStates(currentState);
    }

    /**
     * Check if the state, given in input, belongs to the end-states set.
     *
     * @param state: the state to check if it belongs to the end-states set.
     * @return true, if the given state belongs to the end-states set; false otherwise.
     */
    public boolean isEndState(DfaState state) {
        return finalStates.isStateInFinalStates(state);
    }

    @Override
    public String toString() {
        return "DeterministicFiniteAutomata{" +
                "currentState=" + currentState +
                ", initialState=" + initialState +
                ", finalStates=" + finalStates +
                ", transitions=" + transitions +
                '}';
    }
}
