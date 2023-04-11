package models.contract;

import java.io.Serializable;

public class PayToContract implements Serializable {
    private final String ownershipId;
    private final String address;
    private final String unlockScript;

    public PayToContract(String ownershipId, String address, String unlockScript) {
        this.ownershipId = ownershipId;
        this.address = address;
        this.unlockScript = unlockScript;
    }

    public String getOwnershipId() {
        return ownershipId;
    }

    public String getAddress() {
        return address;
    }

    public String getUnlockScript() {
        return unlockScript;
    }

    @Override
    public String toString() {
        return "PayToContract{" +
                "ownershipId='" + ownershipId + '\'' +
                ", address='" + address + '\'' +
                ", unlockScript='" + unlockScript + '\'' +
                '}';
    }
}
