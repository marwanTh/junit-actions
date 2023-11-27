package com.pixelogicmedia.delivery.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pixelogicmedia.delivery.utils.crypto.StringCrypto;

import java.io.IOException;

public class EncryptedStringSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(final String str, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(StringCrypto.encrypt(str));
    }
}
