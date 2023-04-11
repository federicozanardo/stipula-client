package models.assets;

import java.io.Serializable;

public class Asset implements Serializable {
    private final String id;
    private final AssetConfig asset;

    public Asset(String id, AssetConfig asset) {
        this.id = id;
        this.asset = asset;
    }

    public String getId() {
        return id;
    }

    public AssetConfig getAsset() {
        return asset;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "id='" + id + '\'' +
                ", asset=" + asset +
                '}';
    }
}
