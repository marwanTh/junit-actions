package com.pixelogicmedia.delivery.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Asset extends AuditedEntity {

    private Long phelixAssetId;

    private Long phelixAssetVersion;

    private String storageId;

    private String path;

    private String localPath;

    private String titleName;

    private String alphaName;

    @ManyToOne(optional = false)
    @JoinColumn
    private DeliveryJob deliveryJob;

    public DeliveryJob getTransferJob() {
        return this.deliveryJob;
    }

    public void setTransferJob(final DeliveryJob deliveryJob) {
        this.deliveryJob = deliveryJob;
    }

    public Long getPhelixAssetId() {
        return this.phelixAssetId;
    }

    public void setPhelixAssetId(final Long phelixAssetId) {
        this.phelixAssetId = phelixAssetId;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public Long getPhelixAssetVersion() {
        return this.phelixAssetVersion;
    }

    public void setPhelixAssetVersion(final Long phelixAssetVersion) {
        this.phelixAssetVersion = phelixAssetVersion;
    }

    public DeliveryJob getDeliveryJob() {
        return this.deliveryJob;
    }

    public void setDeliveryJob(final DeliveryJob deliveryJob) {
        this.deliveryJob = deliveryJob;
    }

    public String getStorageId() {
        return this.storageId;
    }

    public void setStorageId(final String storageId) {
        this.storageId = storageId;
    }

    public String getLocalPath() {
        return this.localPath;
    }

    public void setLocalPath(final String localPath) {
        this.localPath = localPath;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getAlphaName() {
        return alphaName;
    }

    public void setAlphaName(String alphaName) {
        this.alphaName = alphaName;
    }
}
