package exceptions.storage;

public class ContractNotFoundException extends Exception {
    public ContractNotFoundException() {
        super();
    }

    public ContractNotFoundException(String contractId) {
        super("The contract with id = " + contractId + " does not exist in the storage");
    }
}
