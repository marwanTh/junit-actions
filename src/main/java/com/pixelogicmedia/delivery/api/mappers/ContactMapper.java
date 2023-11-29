package com.pixelogicmedia.delivery.api.mappers;

import com.pixelogicmedia.delivery.api.v1.models.ContactResource;
import com.pixelogicmedia.delivery.api.v1.models.CreateContactResource;
import com.pixelogicmedia.delivery.data.entities.Contact;
import com.pixelogicmedia.delivery.exceptions.EnumMappingException;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", unexpectedValueMappingException = EnumMappingException.class)
public interface ContactMapper {

    Contact map(CreateContactResource contactResource);

    ContactResource map(Contact contact);

    List<ContactResource> map(List<Contact> contacts);
}
