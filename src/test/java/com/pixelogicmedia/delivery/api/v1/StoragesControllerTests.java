package com.pixelogicmedia.delivery.api.v1;

import com.pixelogicmedia.delivery.api.v1.models.ContactResource;
import com.pixelogicmedia.delivery.api.v1.models.StorageResource;
import com.pixelogicmedia.delivery.exceptions.BusinessException;
import com.pixelogicmedia.delivery.utils.crypto.StringCrypto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class StoragesControllerTests extends AbstractControllerTest {

    @Test
    void canCreateStorage() {
        final var storageRequest = new StorageResource()
                .name("Test Storage")
                .storageId("VX-707")
                .location("Cairo")
                .attributes(Map.of("asperaNodeHost", "aspera-cairo.pixelogicmedia.com"));

        final var storage = this.post("storages", storageRequest, StorageResource.class);

        Assertions.assertNotNull(storage.getId());
        Assertions.assertEquals("aspera-cairo.pixelogicmedia.com", storage.getAttributes().get("asperaNodeHost"));
    }
    @Test
    void createStorageReqID() {
        final var storageRequest = new StorageResource()
                .name("Test Storage")
                .location("Cairo")
                .attributes(Map.of("asperaNodeHost", "aspera-cairo.pixelogicmedia.com"));

        assertThrows(BusinessException.class,
                () -> {
                    this.post("storages", storageRequest, StorageResource.class);
                });
    }
    @Disabled
    @Test
    void createStorageReqLocation() {
        final var storageRequest = new StorageResource()
                .name("Test Storage")
                .storageId("VX-707")
                .attributes(Map.of("asperaNodeHost", "aspera-cairo.pixelogicmedia.com"));

        assertThrows(BusinessException.class,
                () -> {
                    this.post("storages", storageRequest, StorageResource.class);
                });
    }
    @Disabled
    @Test
    void createStorageReqName() {
        final var storageRequest = new StorageResource()
                .storageId("VX-707")
                .location("Cairo")
                .attributes(Map.of("asperaNodeHost", "aspera-cairo.pixelogicmedia.com"));

        assertThrows(BusinessException.class,
                () -> {
                    this.post("storages", storageRequest, StorageResource.class);
                });
    }
    @Disabled
    @Test
    void createStorageReqAttributes() {
        final var storageRequest = new StorageResource()
                .storageId("VX-707")
                .name("Test Storage")
                .location("Cairo");

        assertThrows(BusinessException.class,
                () -> {
                    this.post("storages", storageRequest, StorageResource.class);
                });
    }
    @Test
    void updateStorages() {
        final var storageRequest = new StorageResource()
                .name("Test Storage")
                .storageId("VX-707")
                .location("Cairo");


        final var storageCreateResponse = this.post("storages", storageRequest, StorageResource.class);

        final var storageUpdateRequest = new StorageResource()
                .name("Updated_Test Storage")
                .storageId("VX-707")
                .location("Cairo")
                .attributes(Map.of("asperaNodeHost", "aspera-cairo.pixelogicmedia.com"
                ,"asperaNodeUsername","aspera_user"
                ,"asperaNodePassword","aspera_dummy_password"
                ,"asperaNodeMountPrefix","aspera_pref"));

        final var storageUpdateResponse = this.put("storages/%s".formatted(storageCreateResponse.getId()),
                storageUpdateRequest, StorageResource.class);

        Assertions.assertEquals(storageUpdateRequest.getName(),
                storageUpdateResponse.getName());
        Assertions.assertEquals(storageUpdateRequest.getStorageId(),
                storageUpdateResponse.getStorageId());
        Assertions.assertEquals(storageUpdateRequest.getAttributes().get("asperaNodeHost"),
                storageUpdateResponse.getAttributes().get("asperaNodeHost"));
        Assertions.assertEquals(storageUpdateRequest.getAttributes().get("asperaNodeUsername"),
                storageUpdateResponse.getAttributes().get("asperaNodeUsername"));
        Assertions.assertEquals(storageUpdateRequest.getAttributes().get("asperaNodeMountPrefix"),
                storageUpdateResponse.getAttributes().get("asperaNodeMountPrefix"));
        Assertions.assertEquals(storageUpdateRequest.getAttributes().get("asperaNodePassword"),
                StringCrypto.decrypt(storageUpdateResponse.getAttributes().get("asperaNodePassword").toString()));
    }
}
