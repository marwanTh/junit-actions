package com.pixelogicmedia.delivery.data.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pixelogicmedia.delivery.utils.json.EncryptedStringDeserializer;
import com.pixelogicmedia.delivery.utils.json.EncryptedStringSerializer;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
public class Storage extends AuditedEntity {

    private String name;
    private String storageId;
    private String location;

    @JdbcTypeCode(SqlTypes.JSON)
    private Attributes attributes = new Attributes();

    @Enumerated(EnumType.STRING)
    private Type type;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getStorageId() {
        return this.storageId;
    }

    public void setStorageId(final String storageId) {
        this.storageId = storageId;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public Attributes getAttributes() {
        return this.attributes;
    }

    public void setAttributes(final Attributes attributes) {
        this.attributes = attributes;
    }

    public enum Type {
        ON_PREM,
        S3
    }

    public static class Attributes {
        // Aspera
        private String asperaNodeHost;

        private String asperaNodeUsername;

        @JsonSerialize(using = EncryptedStringSerializer.class)
        @JsonDeserialize(using = EncryptedStringDeserializer.class)
        private String asperaNodePassword;

        private String asperaNodeMountPrefix;

        public String getAsperaNodeHost() {
            return this.asperaNodeHost;
        }

        public void setAsperaNodeHost(final String asperaNodeHost) {
            this.asperaNodeHost = asperaNodeHost;
        }


        public String getAsperaNodeUsername() {
            return this.asperaNodeUsername;
        }

        public void setAsperaNodeUsername(final String asperaNodeUsername) {
            this.asperaNodeUsername = asperaNodeUsername;
        }

        public String getAsperaNodePassword() {
            return this.asperaNodePassword;
        }

        public void setAsperaNodePassword(final String asperaNodePassword) {
            this.asperaNodePassword = asperaNodePassword;
        }

        public String getAsperaNodeMountPrefix() {
            return this.asperaNodeMountPrefix;
        }

        public void setAsperaNodeMountPrefix(final String asperaNodeMountPrefix) {
            this.asperaNodeMountPrefix = asperaNodeMountPrefix;
        }
    }
}
