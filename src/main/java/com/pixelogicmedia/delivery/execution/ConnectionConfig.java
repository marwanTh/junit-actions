package com.pixelogicmedia.delivery.execution;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pixelogicmedia.delivery.execution.aspera.AsperaConnectionConfig;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "_type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AsperaConnectionConfig.class, name = "AsperaConnectionConfig")
})
public abstract class ConnectionConfig {
}
