package com.pixelogicmedia.delivery.data.entities;

import com.pixelogicmedia.delivery.execution.AbstractEmailNotificationReporter;
import com.pixelogicmedia.delivery.execution.OmEmailNotificationReporter;
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

    private String externalId;

    @Enumerated(EnumType.STRING)
    private EmailNotificationInitiator initiator;

    public enum Type {
        COMPLETION,
        FAILURE
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

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public EmailNotificationInitiator getInitiator() {
        return initiator;
    }

    public void setInitiator(EmailNotificationInitiator initiator) {
        this.initiator = initiator;
    }

    public enum EmailNotificationInitiator {
        OM(OmEmailNotificationReporter.class);

        private final Class<? extends AbstractEmailNotificationReporter> reporter;

        private EmailNotificationInitiator(Class<? extends AbstractEmailNotificationReporter> reporter) {
            this.reporter = reporter;
        }

        public Class<? extends AbstractEmailNotificationReporter> getReporter() {
            return reporter;
        }
    }
}
