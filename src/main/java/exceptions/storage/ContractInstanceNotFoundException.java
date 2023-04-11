package exceptions.storage;

public class ContractInstanceNotFoundException extends Exception {
    public ContractInstanceNotFoundException() {
        super();
    }

    public ContractInstanceNotFoundException(String contractInstanceId) {
        super("The contract instance with id = " + contractInstanceId + " does not exist in the storage");
    }

    /*public ContractInstanceNotFoundException(String contractId, String contractInstanceId) {
        super("The is contract instance with id = " + contractInstanceId +
                " for the contract with id = " + contractId +
                " does not exist in the storage");
    }*/
}
