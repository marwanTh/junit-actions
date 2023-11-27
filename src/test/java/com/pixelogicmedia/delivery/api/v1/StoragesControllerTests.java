package com.pixelogicmedia.delivery.api.v1;

import com.pixelogicmedia.delivery.api.v1.models.StorageResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

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
}
