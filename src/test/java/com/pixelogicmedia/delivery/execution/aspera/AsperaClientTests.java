package com.pixelogicmedia.delivery.execution.aspera;

import com.ibm.aspera.models.FilesBrowseRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AsperaClientTests {

    private final AsperaClient asperaClient = AsperaClient.of("https://aspera-staging.pixelogicmedia.us", "bahaa", System.getenv("ASPERA_PASSWORD"));

    @Test
    void canGetInfo() {
        final var application = this.asperaClient.info().getApplication();

        assertEquals("node", application);
    }

    @Test
    void canBrowseFiles() {
        final var response = this.asperaClient.browse(new FilesBrowseRequest().path("/"));

        assertEquals("directory", response.getSelf().getType());
    }

    @Test
    void canListTransfers() {
        final var transfers = this.asperaClient.transfers();

        assertFalse(transfers.isEmpty());
    }
}
