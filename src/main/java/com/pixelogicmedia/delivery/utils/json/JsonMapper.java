package com.pixelogicmedia.delivery.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class JsonMapper {

    private static final ObjectMapper OBJECT_MAPPER_NO_CRYPTO = new ObjectMapper();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // Ignore decryption of the password sent from the frontend as it's not encrypted yet
        final var ignoreJsonDeserializeIntrospector = new JacksonAnnotationIntrospector() {
            @Override
            public Object findDeserializer(final Annotated a) {
                final var deserializer = super.findDeserializer(a);

                if (deserializer == EncryptedStringDeserializer.class) {
                    return null;
                }

                return deserializer;
            }

            @Override
            public Object findSerializer(final Annotated a) {
                final var serializer = super.findSerializer(a);

                if (serializer == EncryptedStringSerializer.class) {
                    return null;
                }

                return serializer;
            }
        };

        JsonMapper.OBJECT_MAPPER_NO_CRYPTO.setAnnotationIntrospector(ignoreJsonDeserializeIntrospector);
    }

    private JsonMapper() {
    }

    public static <T> T convertValueIgnoringEncryption(final Object fromValue, final Class<T> toValueType) {
        return OBJECT_MAPPER_NO_CRYPTO.convertValue(fromValue, toValueType);
    }

    public static <T> T convertValueIgnoringEncryption(final Object fromValue, final TypeReference<T> toValueTypeRef) {
        return OBJECT_MAPPER_NO_CRYPTO.convertValue(fromValue, toValueTypeRef);
    }

    public static <T> T convertValue(final Object fromValue, final Class<T> toValueType) {
        return OBJECT_MAPPER.convertValue(fromValue, toValueType);
    }

    public static <T> T convertValue(final Object fromValue, final TypeReference<T> toValueTypeRef) {
        return OBJECT_MAPPER.convertValue(fromValue, toValueTypeRef);
    }

    public static <T> T readValue(final String value, final Class<T> valueType) {
        try {
            return OBJECT_MAPPER.readValue(value, valueType);
        } catch (final JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T readValue(final InputStream is, final Class<T> valueType) {
        try {
            return OBJECT_MAPPER.readValue(is, valueType);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
