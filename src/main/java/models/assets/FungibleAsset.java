package models.assets;

public class FungibleAsset extends AssetConfig {
    public FungibleAsset(String assetName, String unitName, int supply, int decimals) {
        super(assetName, unitName, supply, decimals);
    }
}