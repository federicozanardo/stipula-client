package vm.types;

import models.party.Party;

public class PartyType extends Type {
    private final Party value;

    public PartyType() {
        this.value = null;
    }

    public PartyType(Party value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "party";
    }

    @Override
    public String getValue() {
        assert value != null;
        return value.getAddress() + " " + value.getPublicKey();
    }

    public String getAddress() {
        assert value != null;
        return value.getAddress();
    }

    public String getPublicKey() {
        assert value != null;
        return value.getPublicKey();
    }

    @Override
    public String toString() {
        return "PartyType{" +
                "value=" + value +
                '}';
    }
}
