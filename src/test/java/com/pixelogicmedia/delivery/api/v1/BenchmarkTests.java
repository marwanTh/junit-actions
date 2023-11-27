package com.pixelogicmedia.delivery.api.v1;

import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import com.pixelogicmedia.delivery.api.v1.models.ContactResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.ParameterizedTypeReference;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static java.lang.System.getProperty;

@ExtendWith(JUnitPerfInterceptor.class)
public class BenchmarkTests extends AbstractControllerTest {

    private static final String REPORT_PATH = String.join(
            File.separator,
            getProperty("user.dir"),
            "target",
            "reports",
            "junitperf_report.html"
    );
    @JUnitPerfTestActiveConfig
    private final static JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(new HtmlReportGenerator(REPORT_PATH))
            .build();

    @Test
    @JUnitPerfTest(threads = 500, durationMs = 180_000, warmUpMs = 10_000)
    void createAndListContacts() {
        final var contactRequest = new ContactResource()
                .email("%s@pixelogicmedia.com".formatted(UUID.randomUUID()))
                .firstName("Mad")
                .lastName("Max");

        final var contactResponse = this.post("contacts", contactRequest, ContactResource.class);

        Assertions.assertNotNull(contactResponse);
        Assertions.assertNotNull(contactResponse.getId());
        Assertions.assertEquals(contactRequest.getEmail(), contactResponse.getEmail());

        final var contacts = this.get("contacts", new ParameterizedTypeReference<List<ContactResource>>() {
        });

        Assertions.assertFalse(contacts.isEmpty());
    }
}
