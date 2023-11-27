package com.pixelogicmedia.delivery.data.entities;

import com.pixelogicmedia.delivery.execution.AbstractTransferJobExecutor;
import com.pixelogicmedia.delivery.execution.ConnectionConfig;
import com.pixelogicmedia.delivery.execution.aspera.AsperaServerTransferJobExecutor;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
public class Connection extends AuditedEntity {

    @NotBlank
    @NotNull
    private String name;

    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Type type;

    @JdbcTypeCode(SqlTypes.JSON)
    private ConnectionConfig config;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public ConnectionConfig getConfig() {
        return this.config;
    }

    public void setConfig(final ConnectionConfig config) {
        this.config = config;
    }

    public enum Type {
        ASPERA(AsperaServerTransferJobExecutor.class, "AsperaNode");

        private final Class<? extends AbstractTransferJobExecutor> executor;

        private final String friendlyName;

        private Type(Class<? extends AbstractTransferJobExecutor> executor, final String friendlyName) {
            this.executor = executor;
            this.friendlyName = friendlyName;
        }

        public Class<? extends AbstractTransferJobExecutor> getExecutor() {
            return executor;
        }

        public String getFriendlyName() {
            return friendlyName;
        }
    }
}
