package models.dto.requests.contract.agreement;

import models.dto.requests.Message;
import models.dto.requests.contract.FunctionArgument;
import models.party.Party;

import java.util.ArrayList;
import java.util.HashMap;

public class AgreementCall extends Message {
    private final String contractId;
    private final ArrayList<FunctionArgument> arguments;
    private final HashMap<String, Party> parties;

    public AgreementCall(String contractId, ArrayList<FunctionArgument> arguments, HashMap<String, Party> parties) {
        super(AgreementCall.class.getSimpleName());
        this.contractId = contractId;
        this.arguments = arguments;
        this.parties = parties;
    }

    public String getContractId() {
        return contractId;
    }

    public ArrayList<FunctionArgument> getArguments() {
        return arguments;
    }

    public HashMap<String, Party> getParties() {
        return parties;
    }

    @Override
    public String toString() {
        return "AgreementCall{" +
                "contractId='" + contractId + '\'' +
                ", arguments=" + arguments +
                ", parties=" + parties +
                '}';
    }
}
