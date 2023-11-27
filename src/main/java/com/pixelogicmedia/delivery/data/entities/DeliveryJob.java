package com.pixelogicmedia.delivery.data.entities;

import com.pixelogicmedia.delivery.execution.AbstractDeliveryJobReporter;
import com.pixelogicmedia.delivery.execution.AbstractTransferJobExecutor;
import com.pixelogicmedia.delivery.execution.OmDeliveryJobReporter;
import com.pixelogicmedia.delivery.execution.TransferInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DefaultValue;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class DeliveryJob extends AuditedEntity {

    @NotNull
    @ManyToOne
    @JoinColumn
    private Profile profile;

    @OneToMany(mappedBy = "deliveryJob", orphanRemoval = true, fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    private List<Asset> assets = new ArrayList<>();

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Status status;

    @DefaultValue("0")
    private Double progress;

    @DefaultValue("0")
    private Double lastReportedProgress;

    private String externalId;

    @Enumerated(EnumType.STRING)
    private JobInitiator initiator;

    @JdbcTypeCode(SqlTypes.JSON)
    private TransferInfo transferInfo;

    @OneToMany(mappedBy = "deliveryJob", orphanRemoval=true)
    @Cascade(CascadeType.ALL)
    private Set<DeliveryJobContact> contacts;

    private String emailSubject;

    public Profile getProfile() {
        return this.profile;
    }

    public void setProfile(final Profile profile) {
        this.profile = profile;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public Double getProgress() {
        return this.progress;
    }

    public void setProgress(final Double progress) {
        this.progress = progress;
    }

    public List<Asset> getAssets() {
        return this.assets;
    }

    public TransferInfo getTransferInfo() {
        return this.transferInfo;
    }

    public void setTransferInfo(final TransferInfo transferInfo) {
        this.transferInfo = transferInfo;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public Double getLastReportedProgress() {
        return lastReportedProgress;
    }

    public void setLastReportedProgress(Double lastReportedProgress) {
        this.lastReportedProgress = lastReportedProgress;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public JobInitiator getInitiator() {
        return initiator;
    }

    public void setInitiator(JobInitiator initiator) {
        this.initiator = initiator;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public Set<DeliveryJobContact> getDeliveryJobContacts() {
        return contacts;
    }

    public void setDeliveryJobContacts(Set<DeliveryJobContact> deliveryJobContacts) {
        this.contacts = deliveryJobContacts;
    }



    public enum Status {
        CREATED,
        COMPLETED,
        FAILED,
        CANCELED,
        IN_PROGRESS,
    }

    public enum JobInitiator {
        OM(OmDeliveryJobReporter.class);

        private final Class<? extends AbstractDeliveryJobReporter> reporter;

        private JobInitiator(Class<? extends AbstractDeliveryJobReporter> reporter) {
            this.reporter = reporter;
        }

        public Class<? extends AbstractDeliveryJobReporter> getReporter() {
            return reporter;
        }
    }
}
