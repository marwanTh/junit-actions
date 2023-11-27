package com.pixelogicmedia.delivery.data.entities;

import jakarta.persistence.*;

@Entity
public class DeliveryJobContact extends AuditedEntity {

    @ManyToOne
    private DeliveryJob deliveryJob;

    @ManyToOne
    private Contact contact;

    @Enumerated(EnumType.STRING)
    private Contact.SendType sendType;

    @Enumerated(EnumType.STRING)
    private Contact.NotifyOn notifyOn;

    public DeliveryJob getDeliveryJob() {
        return deliveryJob;
    }

    public void setDeliveryJob(DeliveryJob deliveryJob) {
        this.deliveryJob = deliveryJob;
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
