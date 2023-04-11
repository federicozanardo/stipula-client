package models.dto.requests.contract.function;

import models.dto.requests.Message;
import models.dto.requests.contract.FunctionArgument;

import java.util.ArrayList;

public class FunctionCall extends Message {
    private final String contractInstanceId;
    private final String functionName;
    private final ArrayList<FunctionArgument> arguments;

    public FunctionCall(String contractInstanceId, String function, ArrayList<FunctionArgument> arguments) {
        super(FunctionCall.class.getSimpleName());
        this.contractInstanceId = contractInstanceId;
        this.functionName = function;
        this.arguments = arguments;
    }

    public String getContractInstanceId() {
        return contractInstanceId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public ArrayList<FunctionArgument> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "FunctionCall{" +
                "contractInstanceId='" + contractInstanceId + '\'' +
                ", functionName='" + functionName + '\'' +
                ", arguments=" + arguments +
                '}';
    }
}
