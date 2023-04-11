package models.dto.requests.ownership;

import models.dto.requests.Message;

public class GetOwnershipsByAddress extends Message {
    private final String address;

    public GetOwnershipsByAddress(String address) {
        super(GetOwnershipsByAddress.class.getSimpleName());
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "GetAssetsByAddress{" +
                "address='" + address + '\'' +
                '}';
    }
}
