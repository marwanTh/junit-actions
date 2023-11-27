package com.pixelogicmedia.delivery.api.v1;

import com.pixelogicmedia.delivery.api.v1.models.ContactResource;
import com.pixelogicmedia.delivery.exceptions.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class ContactsControllerTests extends AbstractControllerTest {

    @Test
    void createContacts() {
        final var contactRequest = new ContactResource()
                .email("%s@pixelogicmedia.com".formatted(UUID.randomUUID()))
                .firstName("testFirstName")
                .lastName("testLastName");

        final var contactResponse = this.post("contacts", contactRequest, ContactResource.class);
        Assertions.assertNotNull(contactResponse);
        Assertions.assertNotNull(contactResponse.getId());
        Assertions.assertEquals(contactRequest.getEmail(), contactResponse.getEmail());
        Assertions.assertEquals(contactRequest.getFirstName(), contactResponse.getFirstName());
        Assertions.assertEquals(contactRequest.getLastName(), contactResponse.getLastName());
    }

    @Test
    void createContactsDuplicateEmail() {
        final var contactRequest = new ContactResource()
                .email("%s@pixelogicmedia.com".formatted(UUID.randomUUID()))
                .firstName("testFirstName")
                .lastName("testLastName");
        this.post("contacts", contactRequest, ContactResource.class);
        assertThrows(BusinessException.class,
                () -> {
                    this.post("contacts", contactRequest, ContactResource.class);
                });
    }

    @Test
    void createContactsReqEmail() {
        final var contactRequest = new ContactResource()
                .firstName("testFirstName")
                .lastName("testLastName");
        assertThrows(BusinessException.class,
                () -> {
                    this.post("contacts", contactRequest, ContactResource.class);
                });
    }

    @Test
    void createContactsReqFirstName() {
        final var contactRequest = new ContactResource()
                .email("%s@pixelogicmedia.com".formatted(UUID.randomUUID()))
                .lastName("testLastName");
        assertThrows(BusinessException.class,
                () -> {
                    this.post("contacts", contactRequest, ContactResource.class);
                });
    }

    @Test
    void createContactsReqLastName() {
        final var contactRequest = new ContactResource()
                .email("%s@pixelogicmedia.com".formatted(UUID.randomUUID()))
                .firstName("testFirstName");
        assertThrows(BusinessException.class,
                () -> {
                    this.post("contacts", contactRequest, ContactResource.class);
                });
    }

    @Test
    void updateContacts() {
        final var contactCreateRequest = new ContactResource()
                .email("%s@pixelogicmedia.com".formatted(UUID.randomUUID()))
                .firstName("testFirstName")
                .lastName("testLastName");
        final var contactCreateResponse = this.post("contacts", contactCreateRequest, ContactResource.class);
        Assertions.assertNotNull(contactCreateResponse);
        Assertions.assertNotNull(contactCreateResponse.getId());

        final var contactUpdateRequest = new ContactResource()
                .email("updated%s@pixelogicmedia.com".formatted(UUID.randomUUID()))
                .firstName("updatedTestFirstName")
                .lastName("updatedTestLastName");

        final var contactResponse = this.put("contacts/%s".formatted(contactCreateResponse.getId()), contactUpdateRequest, ContactResource.class);

        Assertions.assertEquals(contactUpdateRequest.getEmail(), contactResponse.getEmail());
        Assertions.assertEquals(contactUpdateRequest.getFirstName(), contactResponse.getFirstName());
        Assertions.assertEquals(contactUpdateRequest.getLastName(), contactResponse.getLastName());
    }

    @Test
    void getContacts() {
        final var initialContacts = this.get("contacts", new ParameterizedTypeReference<List<ContactResource>>() {
        });
        final var contactCreateRequest = new ContactResource()
                .email("%s@pixelogicmedia.com".formatted(UUID.randomUUID()))
                .firstName("testFirstName")
                .lastName("testLastName");

        this.post("contacts", contactCreateRequest, ContactResource.class);
        final var finalContacts = this.get("contacts", new ParameterizedTypeReference<List<ContactResource>>() {
        });
        Assertions.assertEquals(finalContacts.size(),initialContacts.size()+1);
    }
}
