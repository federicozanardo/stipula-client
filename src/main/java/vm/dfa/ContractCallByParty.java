package vm.dfa;

import java.util.ArrayList;

public class ContractCallByParty extends TransitionData {
    private final String functionName;
    private final String party;
    private final ArrayList<String> arguments;

    public ContractCallByParty(String functionName, String party, ArrayList<String> arguments) {
        super();
        this.functionName = functionName;
        this.party = party;
        this.arguments = arguments;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getParty() {
        return party;
    }

    public ArrayList<String> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "ContractCallByParty{" +
                "functionName='" + functionName + '\'' +
                ", party=" + party +
                ", arguments=" + arguments +
                '}';
    }
}
