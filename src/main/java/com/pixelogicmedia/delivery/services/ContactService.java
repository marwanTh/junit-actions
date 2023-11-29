package com.pixelogicmedia.delivery.services;

import com.pixelogicmedia.delivery.data.entities.Contact;
import com.pixelogicmedia.delivery.data.repositories.ContactRepository;
import com.pixelogicmedia.delivery.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(final ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Transactional
    public Contact createContact(final Contact contact) {
        return this.contactRepository.save(contact);
    }

    @Transactional(readOnly = true)
    public Page<Contact> listContacts(final Pageable pageable, final Specification<Contact> spec) {
        return this.contactRepository.findAll(spec, pageable);
    }

    @Transactional
    public Contact updateContact(final Long id, final Contact contact) {
        contact.setId(id);
        if (Objects.isNull(contact.getId())) {
            throw BusinessException.badRequest("ID is required when updating objects");
        }

        return this.contactRepository.save(contact);
    }
}
