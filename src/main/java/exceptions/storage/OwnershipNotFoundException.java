package exceptions.storage;

public class OwnershipNotFoundException extends Exception {
    public OwnershipNotFoundException() {
        super();
    }

    public OwnershipNotFoundException(String address, String ownershipId) {
        super("The ownership with id = " + ownershipId +
                ", associated to the address = " + address +
                ", does not exist in the storage");
    }
}
