package com.pixelogicmedia.delivery.api.v1;

import com.pixelogicmedia.delivery.api.BaseController;
import com.pixelogicmedia.delivery.api.mappers.ContactMapper;
import com.pixelogicmedia.delivery.api.v1.models.ContactResource;
import com.pixelogicmedia.delivery.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContactsController extends BaseController implements ContactsApi {

    private ContactService contactService;

    private ContactMapper contactMapper;

    @Autowired
    public ContactsController(final ContactService contactService, final ContactMapper contactMapper) {
        this.contactService = contactService;
        this.contactMapper = contactMapper;
    }

    @Override
    public ResponseEntity<List<ContactResource>> listContacts(final Integer offset, final Integer limit, final String filter, final String sort) {
        final var page = this.contactService.listContacts(this.pageOf(offset, limit), this.specificationOf(filter, sort));

        return ResponseEntity.ok()
                .header(TOTAL_COUNT_HEADER, String.valueOf(page.getTotalElements()))
                .body(this.contactMapper.map(page.getContent()));
    }

    @Override
    public ResponseEntity<ContactResource> createContact(final ContactResource contactResource) {
        final var contact = this.contactService.createContact(this.contactMapper.map(contactResource));

        return ResponseEntity.ok(this.contactMapper.map(contact));
    }

    @Override
    public ResponseEntity<ContactResource> updateContact(final Long id, final ContactResource contactResource) {
        final var contact = this.contactService.updateContact(this.contactMapper.map(contactResource.id(id)));

        return ResponseEntity.ok(this.contactMapper.map(contact));
    }
}
