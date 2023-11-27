package com.pixelogicmedia.delivery.services;

import com.pixelogicmedia.delivery.data.dto.am.AssetGeneralData;
import kong.unirest.Unirest;
import org.keycloak.admin.client.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AssetManagerClient {

    private TokenManager tokenManager;

    private String amBaseUri;

    @Autowired
    public AssetManagerClient(final TokenManager tokenManager, @Value("${am.base-uri}") final String amBaseUri) {
        this.tokenManager = tokenManager;
        this.amBaseUri = amBaseUri;
    }

    public AssetGeneralData getAssetGeneralDataByOmAssetVersion(final Long assetVersion) {
        return Unirest.get("%s/item/{id}/general-data".formatted(this.amBaseUri))
                .routeParam("id", "omAssetVersionId_%d".formatted(assetVersion))
                .header("Authorization", "Bearer %s".formatted(this.tokenManager.getAccessTokenString()))
                .asObject(AssetGeneralData.class)
                .getBody();
    }

    public AssetGeneralData getAssetGeneralDataByOmAssetId(final Long assetId) {
        return Unirest.get("%s/item/{id}/general-data".formatted(this.amBaseUri))
                .routeParam("id", "omAssetId_%d".formatted(assetId))
                .header("Authorization", "Bearer %s".formatted(this.tokenManager.getAccessTokenString()))
                .asObject(AssetGeneralData.class)
                .getBody();
    }

    public AssetGeneralData getAssetGeneralDataByAmAssetId(final String assetId) {
        return Unirest.get("%s/item/{id}/general-data".formatted(this.amBaseUri))
                .routeParam("id", assetId)
                .header("Authorization", "Bearer %s".formatted(this.tokenManager.getAccessTokenString()))
                .asObject(AssetGeneralData.class)
                .getBody();
    }
}
