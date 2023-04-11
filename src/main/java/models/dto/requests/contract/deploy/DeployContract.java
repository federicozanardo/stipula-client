package models.dto.requests.contract.deploy;

import models.dto.requests.Message;

public class DeployContract extends Message {
    private final String sourceCode;

    public DeployContract(String sourceCode) {
        super(DeployContract.class.getSimpleName());
        this.sourceCode = sourceCode;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    @Override
    public String toString() {
        return "DeployContract{" +
                "sourceCode='" + sourceCode + '\'' +
                '}';
    }
}

