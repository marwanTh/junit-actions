package com.pixelogicmedia.delivery.api.v1;

import com.pixelogicmedia.delivery.api.v1.models.ConnectionResource;
import com.pixelogicmedia.delivery.api.v1.models.ContactResource;
import com.pixelogicmedia.delivery.api.v1.models.ProfileContactResource;
import com.pixelogicmedia.delivery.api.v1.models.ProfileResource;
import com.pixelogicmedia.delivery.data.entities.Connection;
import com.pixelogicmedia.delivery.execution.aspera.AsperaConnectionConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Random;
import java.util.UUID;


public class ProfileControllerTests extends AbstractControllerTest {

    @Test
    void createProfile() {
        ProfileResource profileResource = new ProfileResource();
        final var connectionRequest = this.createConnection("Aspera Test",
                Connection.Type.ASPERA.toString(),
                new AsperaConnectionConfig());

        final var connection = this.post("connections", connectionRequest, ConnectionResource.class);
        profileResource.connection(connection.config(connectionRequest.get("config")));
        profileResource.name("ASPERA_test_Profile");
        profileResource = addRandomContactsToProfile(profileResource);
        final var profileResponse = this.post("profiles", profileResource,
                ProfileResource.class);
        Assertions.assertNotNull(profileResponse.getId());
        Assertions.assertTrue(profileResponse.getContacts().size() <= 5);
    }

    private ProfileResource addRandomContactsToProfile(ProfileResource profileResource) {
        int numberOfContacts = new Random().nextInt(5);
        for (int i = 0; i <= numberOfContacts; i++) {
            final var contactRequest = new ContactResource()
                    .email("%s@pixelogicmedia.com".formatted(UUID.randomUUID()))
                    .firstName("testFirstName")
                    .lastName("testLastName");
            profileResource.addContactsItem(new ProfileContactResource()
                    .notifyOn(ProfileContactResource.NotifyOnEnum.SUCCESS)
                    .sendType(ProfileContactResource.SendTypeEnum.TO)
                    .contact(this.post("contacts", contactRequest, ContactResource.class)));
        }
        return profileResource;
    }

    @Test
    void updateProfile() {
        ProfileResource profileResource = new ProfileResource();
        final var connectionRequest = this.createConnection("Aspera Test",
                Connection.Type.ASPERA.toString(),
                new AsperaConnectionConfig());

        final var connection = this.post("connections", connectionRequest, ConnectionResource.class);
        profileResource.connection(connection.config(connectionRequest.get("config")));
        profileResource.name("ASPERA_test_Profile");
        profileResource = addRandomContactsToProfile(profileResource);

        final var profileResponse = this.post("profiles", profileResource,
                ProfileResource.class);

        final var profileUpdateRequest = profileResource.name("Updated_ASPERA_test_Profile");
        final var profileUpdateResponse = this.put("profiles/%s".formatted(profileResponse.getId()),
                profileUpdateRequest, ProfileResource.class);
        Assertions.assertEquals(profileUpdateResponse.getName(), "Updated_ASPERA_test_Profile");
    }

    @Test
    void getProfile() {
        final var initialProfiles = this.get("profiles", new ParameterizedTypeReference<List<ProfileResource>>() {
        });
        createProfile();
        final var finalProfiles = this.get("profiles", new ParameterizedTypeReference<List<ProfileResource>>() {
        });
        Assertions.assertEquals(finalProfiles.size(), initialProfiles.size() + 1);
    }
}
