package com.pixelogicmedia.delivery.data.entities;

import jakarta.persistence.*;

@Entity
public class ProfileContact extends AuditedEntity {
    @ManyToOne
    private Profile profile;

    @ManyToOne
    private Contact contact;

    @Enumerated(EnumType.STRING)
    private Contact.SendType sendType;

    @Enumerated(EnumType.STRING)
    private Contact.NotifyOn notifyOn;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Contact.SendType getSendType() {
        return sendType;
    }

    public void setSendType(Contact.SendType sendType) {
        this.sendType = sendType;
    }

    public Contact.NotifyOn getNotifyOn() {
        return notifyOn;
    }

    public void setNotifyOn(Contact.NotifyOn notifyOn) {
        this.notifyOn = notifyOn;
    }

}


