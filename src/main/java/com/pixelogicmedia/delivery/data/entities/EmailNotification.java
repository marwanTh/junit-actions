package com.pixelogicmedia.delivery.data.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class EmailNotification extends AuditedEntity {

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String messageHandlerRequestId;

    @NotNull
    @ManyToOne
    @JoinColumn
    private DeliveryJob deliveryJob;

    public enum Type {
        Completion,
        Failure
    }

    public enum Status {
        PENDING,
        SENDING,
        DELIVERED,
        PARTIALLY_DELIVERED,
        FAILED,
        BOUNCED,
        DROPPED,
        DEFERRED,
    }
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessageHandlerRequestId() {
        return messageHandlerRequestId;
    }

    public void setMessageHandlerRequestId(String messageHandlerRequestId) {
        this.messageHandlerRequestId = messageHandlerRequestId;
    }

    public DeliveryJob getDeliveryJob() {
        return deliveryJob;
    }

    public void setDeliveryJob(DeliveryJob deliveryJob) {
        this.deliveryJob = deliveryJob;
    }
}
