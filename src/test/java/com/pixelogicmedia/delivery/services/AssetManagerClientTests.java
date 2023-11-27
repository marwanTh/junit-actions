package com.pixelogicmedia.delivery.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AssetManagerClientTests {

    @Autowired
    private AssetManagerClient assetManagerClient;


    @Test
    void canGetAssetGeneralDataByOmAssetVersion() {
        final var data = this.assetManagerClient.getAssetGeneralDataByOmAssetVersion(903237L);

        Assertions.assertEquals("image", data.getMediaType());
    }

    @Test
    void canGetAssetGeneralDataByOmAssetId() {
        final var data = this.assetManagerClient.getAssetGeneralDataByOmAssetId(903521L);

        Assertions.assertEquals("image", data.getMediaType());
    }
}
