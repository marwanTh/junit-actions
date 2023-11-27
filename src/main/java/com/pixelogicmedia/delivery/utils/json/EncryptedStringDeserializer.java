package com.pixelogicmedia.delivery.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.pixelogicmedia.delivery.utils.crypto.StringCrypto;

import java.io.IOException;

public class EncryptedStringDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException {
        return StringCrypto.decrypt(jsonParser.getText());
    }
}
