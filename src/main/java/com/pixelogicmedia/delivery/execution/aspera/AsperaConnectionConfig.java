package com.pixelogicmedia.delivery.execution.aspera;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pixelogicmedia.delivery.execution.ConnectionConfig;
import com.pixelogicmedia.delivery.utils.json.EncryptedStringDeserializer;
import com.pixelogicmedia.delivery.utils.json.EncryptedStringSerializer;

public class AsperaConnectionConfig extends ConnectionConfig {

    private String username;

    @JsonSerialize(using = EncryptedStringSerializer.class)
    @JsonDeserialize(using = EncryptedStringDeserializer.class)
    private String password;

    private String remoteAddress;

    private Long targetRate;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getRemoteAddress() {
        return this.remoteAddress;
    }

    public void setRemoteAddress(final String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public Long getTargetRate() { return targetRate; }

    public void setTargetRate(Long targetRate) { this.targetRate = targetRate; }
}
