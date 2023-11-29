package com.pixelogicmedia.delivery.api.v1;

import com.pixelogicmedia.delivery.api.v1.models.*;
import com.pixelogicmedia.delivery.api.v1.models.DeliveryJobResource.StatusEnum;
import com.pixelogicmedia.delivery.data.entities.Connection;
import com.pixelogicmedia.delivery.exceptions.BusinessException;
import com.pixelogicmedia.delivery.execution.aspera.AsperaConnectionConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
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

        asperaConfig.setRemoteAddress("192.168.134.190");
        asperaConfig.setUsername("khaled");
        asperaConfig.setPassword(System.getenv("ASPERA_PASSWORD"));

        ProfileResource profileResource = new ProfileResource();
        final var connectionRequest = this.createConnection("Aspera Test",
                Connection.Type.ASPERA.toString(),
                asperaConfig);
        final var connection = this.post("connections", connectionRequest, ConnectionResource.class);
        assertNotNull(connection.getId());

        profileResource.connection(connection.config(connectionRequest.get("config")));
        profileResource.name("ASPERA_test_Profile");


        final var profile = this.post("profiles",
                profileResource,
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
                .profile(profileResource.id(profile.getId()))
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

        ProfileResource profileResource = new ProfileResource();
        final var connectionRequest = this.createConnection("Aspera Test",
                Connection.Type.ASPERA.toString(),
                asperaConfig);
        final var connection = this.post("connections", connectionRequest, ConnectionResource.class);
        assertNotNull(connection.getId());

        profileResource.connection(connection.config(connectionRequest.get("config")));
        profileResource.name("ASPERA_test_Profile");


        final var profile = this.post("profiles",
                profileResource,
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

        final var asset = new AssetResource().phelixAssetId(995350L);
        final var job = new DeliveryJobResource()
                .profile(profileResource.id(profile.getId()))
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

    @Test
    void jobRetrievalById() {
        final var asperaConfig = new AsperaConnectionConfig();

        asperaConfig.setRemoteAddress("192.168.134.190");
        asperaConfig.setUsername("khaled");
        asperaConfig.setPassword(System.getenv("ASPERA_PASSWORD"));

        ProfileResource profileResource = new ProfileResource();
        final var connectionRequest = this.createConnection("Aspera Test",
                Connection.Type.ASPERA.toString(),
                asperaConfig);
        final var connection = this.post("connections", connectionRequest, ConnectionResource.class);
        assertNotNull(connection.getId());

        profileResource.connection(connection.config(connectionRequest.get("config")));
        profileResource.name("ASPERA_test_Profile");


        final var profile = this.post("profiles",
                profileResource,
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

        final var asset = new AssetResource().phelixAssetId(995339L);
        final var job = new DeliveryJobResource()
                .profile(profileResource.id(profile.getId()))
                .assets(List.of(asset));


        final var savedJob = this.post("delivery-jobs", job, DeliveryJobResource.class);
        assertNotNull(savedJob);
        assertNotNull(savedJob.getId());
        final var retrievedJob = this.get("delivery-jobs/%s".formatted(savedJob.getId()), DeliveryJobResource.class);
        Assertions.assertEquals(savedJob.getAssets(), retrievedJob.getAssets());
    }

    @Test
    void listJobs() {
        final var initialJobs = this.get("delivery-jobs", new ParameterizedTypeReference<List<DeliveryJobResource>>() {
        });
        final var asperaConfig = new AsperaConnectionConfig();

        asperaConfig.setRemoteAddress("192.168.134.190");
        asperaConfig.setUsername("khaled");
        asperaConfig.setPassword(System.getenv("ASPERA_PASSWORD"));

        ProfileResource profileResource = new ProfileResource();
        final var connectionRequest = this.createConnection("Aspera Test",
                Connection.Type.ASPERA.toString(),
                asperaConfig);
        final var connection = this.post("connections", connectionRequest, ConnectionResource.class);
        assertNotNull(connection.getId());

        profileResource.connection(connection.config(connectionRequest.get("config")));
        profileResource.name("ASPERA_test_Profile");


        final var profile = this.post("profiles",
                profileResource,
                ProfileResource.class);

        assertNotNull(profile.getId());
        final var asset = new AssetResource().phelixAssetId(995332L);
        final var job = new DeliveryJobResource()
                .profile(profileResource.id(profile.getId()))
                .assets(List.of(asset));

        final var savedJob = this.post("delivery-jobs", job, DeliveryJobResource.class);
        assertNotNull(savedJob);
        assertNotNull(savedJob.getId());
        final var finalJobs = this.get("delivery-jobs", new ParameterizedTypeReference<List<DeliveryJobResource>>() {
        });
        Assertions.assertEquals(finalJobs.size(), initialJobs.size() + 1);

    }
}
