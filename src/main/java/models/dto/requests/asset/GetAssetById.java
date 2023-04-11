package models.dto.requests.asset;

import models.dto.requests.Message;

public class GetAssetById extends Message {
    private final String assetId;

    public GetAssetById(String assetId) {
        super(GetAssetById.class.getSimpleName());
        this.assetId = assetId;
    }

    public String getAssetId() {
        return assetId;
    }

    @Override
    public String toString() {
        return "GetAssetById{" +
                "assetId='" + assetId + '\'' +
                '}';
    }
}
