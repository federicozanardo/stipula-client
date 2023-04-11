package models.assets;

import java.io.Serializable;

public abstract class AssetConfig implements Serializable {
    private final String assetName;
    private final String unitName;
    private final int supply;
    private final int decimals;

    public AssetConfig(String assetName, String unitName, int supply, int decimals) {
        this.assetName = assetName;
        this.unitName = unitName;
        this.supply = supply;
        this.decimals = decimals;
    }

    public String getAssetName() {
        return assetName;
    }

    public String getUnitName() {
        return unitName;
    }

    public int getSupply() {
        return supply;
    }

    public int getDecimals() {
        return decimals;
    }

    @Override
    public String toString() {
        return "AssetConfig{" +
                "assetName='" + assetName + '\'' +
                ", unitName='" + unitName + '\'' +
                ", supply=" + supply +
                ", decimals=" + decimals +
                '}';
    }
}
