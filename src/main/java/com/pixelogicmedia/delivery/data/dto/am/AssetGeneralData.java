package com.pixelogicmedia.delivery.data.dto.am;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetGeneralData {

    private String id;
    private String name;

    private String owner;

    private LocalDate creationDate;

    private String mediaType;

    private Long originalFileSize;

    private Double durationSeconds;

    private Long omAssetId;

    private Long omAssetVersionId;

    private List<Location> locations;

    private Title title;

    private Alpha alpha;

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getOwner() {
        return this.owner;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public String getMediaType() {
        return this.mediaType;
    }

    public Long getOriginalFileSize() {
        return this.originalFileSize;
    }

    public Double getDurationSeconds() {
        return this.durationSeconds;
    }

    public Long getOmAssetId() {
        return this.omAssetId;
    }

    public Long getOmAssetVersionId() {
        return this.omAssetVersionId;
    }

    public List<Location> getLocations() {
        return this.locations;
    }

    public Title getTitle() {
        return this.title;
    }

    public Alpha getAlpha() {
        return this.alpha;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        private String storageName;
        private String storageId;

        private String storageExternalId;

        private String storageLocation;

        private String storageType;

        private String path;

        public String getStorageName() {
            return this.storageName;
        }

        public String getStorageId() {
            return this.storageId;
        }

        public String getStorageExternalId() {
            return this.storageExternalId;
        }

        public String getStorageLocation() {
            return this.storageLocation;
        }

        public String getStorageType() {
            return this.storageType;
        }

        public String getPath() {
            return this.path;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Title {
        private String id;
        private String name;

        public String getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Alpha {
        private String id;
        private String name;

        public String getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }
    }
}
