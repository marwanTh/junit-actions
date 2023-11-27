package com.pixelogicmedia.delivery.api.v1;

import com.pixelogicmedia.delivery.api.v1.models.*;
import com.pixelogicmedia.delivery.api.v1.models.DeliveryJobResource.StatusEnum;
import com.pixelogicmedia.delivery.data.entities.Connection;
import com.pixelogicmedia.delivery.exceptions.BusinessException;
import com.pixelogicmedia.delivery.execution.aspera.AsperaConnectionConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DeliveryJobsControllerTests extends AbstractControllerTest {

    @Test
    void shouldNotAcceptDuplicateAssets() {
        final var asperaConfig = new AsperaConnectionConfig();
        final var connectionRequest = this.createConnection("Aspera Test",
                Connection.Type.ASPERA.toString(),
                asperaConfig);

        final var connection = this.post("connections", connectionRequest, ConnectionResource.class);

        final var profile = this.post("profiles",
                new ProfileResource().connection(new ConnectionResource().id(connection.getId())).name("Aspera Profile"),
                ProfileResource.class);

        final var asset = new AssetResource().phelixAssetId(903521L);
        final var job = new DeliveryJobResource()
                .profile(profile)
                .assets(List.of(asset));

        final var savedJob = this.post("delivery-jobs", job, DeliveryJobResource.class);

        assertNotNull(savedJob);
        assertNotNull(savedJob.getId());

        final var exception = Assertions.assertThrows(BusinessException.class, () -> {
            this.post("delivery-jobs", job, DeliveryJobResource.class);
        });

        Assertions.assertTrue(exception.getMessage().contains("already submitted before"));
    }

    @Test
    void canExecuteTransferJob() {
        final var asperaConfig = new AsperaConnectionConfig();

        asperaConfig.setRemoteAddress("192.168.134.190");
        asperaConfig.setUsername("khaled");
        asperaConfig.setPassword(System.getenv("ASPERA_PASSWORD"));

        final var connectionRequest = this.createConnection("Aspera Staging 190",
                Connection.Type.ASPERA.toString(),
                asperaConfig);

        final var connection = this.post("connections", connectionRequest, ConnectionResource.class);

        assertNotNull(connection.getId());

        final var profile = this.post("profiles",
                new ProfileResource().connection(new ConnectionResource().id(connection.getId())).name("Aspera Staging Profile"),
                ProfileResource.class);

        assertNotNull(profile.getId());

        final var storageRequest = new StorageResource()
                .name("Test Storage")
                .storageId("VX-76")
                .location("Cairo")
                .attributes(Map.of("asperaNodeHost", "https://aspera-staging.pixelogicmedia.us",
                        "asperaNodeUsername", "bahaa",
                        "asperaNodePassword", System.getenv("ASPERA_PASSWORD"),
                        "asperaNodeMountPrefix", "/WailuaVidispineTesting/mam_team_storage"));

        final var storage = this.post("storages", storageRequest, StorageResource.class);

        assertNotNull(storage.getId());

        final var asset = new AssetResource().phelixAssetId(903521L);
        final var job = new DeliveryJobResource()
                .profile(profile)
                .assets(List.of(asset));

        final var savedJob = this.post("delivery-jobs", job, DeliveryJobResource.class);
        double epsilon = 0.0001d;
        assertEquals(StatusEnum.CREATED, savedJob.getStatus());
        assertEquals(Math.abs(savedJob.getProgress() - 0.0) < epsilon, true);

        Awaitility.await().atMost(Duration.ofSeconds(10)).until(() ->
                this.get("delivery-jobs/%d".formatted(savedJob.getId()), DeliveryJobResource.class).getStatus() ==
                        StatusEnum.IN_PROGRESS);

        Awaitility.await().atMost(Duration.ofSeconds(10)).until(() -> {
            var deliveryJobResource = this.get("delivery-jobs/%d".formatted(savedJob.getId()), DeliveryJobResource.class);
            return deliveryJobResource.getStatus() == StatusEnum.COMPLETED && Math.abs(deliveryJobResource.getProgress() - 1.0) < epsilon;
        });
    }
}
