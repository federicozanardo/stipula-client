package vm.types;

public class AssetType extends Type {
    private final String assetId;
    private final RealType amount;

    public AssetType(String assetId, RealType amount) {
        this.assetId = assetId;
        this.amount = amount;
    }

    @Override
    public String getType() {
        return "asset";
    }

    @Override
    public RealType getValue() {
        return this.amount;
    }

    public String getAssetId() {
        return this.assetId;
    }

    @Override
    public String toString() {
        return "AssetType{" +
                "assetId='" + assetId + '\'' +
                ", amount=" + amount +
                '}';
    }
}