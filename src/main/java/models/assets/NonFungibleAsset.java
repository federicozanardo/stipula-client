package models.assets;

public class NonFungibleAsset extends AssetConfig {
    public NonFungibleAsset(String assetName, String unitName) {
        super(assetName, unitName, 1, 0);
    }
}