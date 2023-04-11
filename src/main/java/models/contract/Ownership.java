package models.contract;

import java.io.Serializable;

public class Ownership implements Serializable {
    private final String id;
    private final SingleUseSeal singleUseSeal;
    private String unlockScript;
    private String contractInstanceId;

    public Ownership(String id, SingleUseSeal singleUseSeal) {
        this.id = id;
        this.singleUseSeal = singleUseSeal;
        this.unlockScript = "";
        this.contractInstanceId = "";
    }

    public String getId() {
        return id;
    }

    public SingleUseSeal getSingleUseSeal() {
        return singleUseSeal;
    }

    public String getUnlockScript() {
        return unlockScript;
    }

    public String getContractInstanceId() {
        return contractInstanceId;
    }

    public void setUnlockScript(String unlockScript) {
        this.unlockScript = unlockScript;
    }

    public void setContractInstanceId(String contractInstanceId) {
        this.contractInstanceId = contractInstanceId;
    }

    @Override
    public String toString() {
        return "Ownership{" +
                "id='" + id + '\'' +
                ", singleUseSeal=" + singleUseSeal +
                ", unlockScript='" + unlockScript + '\'' +
                ", contractInstanceId='" + contractInstanceId + '\'' +
                '}';
    }
}
