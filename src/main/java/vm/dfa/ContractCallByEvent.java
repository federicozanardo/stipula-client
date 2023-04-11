package vm.dfa;

public class ContractCallByEvent extends TransitionData {
    private final String obligationFunctionName;

    public ContractCallByEvent(String obligationFunctionName) {
        this.obligationFunctionName = obligationFunctionName;
    }

    public String getObligationFunctionName() {
        return obligationFunctionName;
    }

    @Override
    public String toString() {
        return "ContractCallByEvent{" +
                "obligationFunctionName='" + obligationFunctionName + '\'' +
                '}';
    }
}
