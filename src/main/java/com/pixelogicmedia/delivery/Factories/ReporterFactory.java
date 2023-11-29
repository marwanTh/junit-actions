package com.pixelogicmedia.delivery.Factories;

import com.pixelogicmedia.delivery.data.entities.DeliveryJob;
import com.pixelogicmedia.delivery.data.entities.EmailNotification;
import com.pixelogicmedia.delivery.execution.AbstractDeliveryJobReporter;
import com.pixelogicmedia.delivery.execution.AbstractEmailNotificationReporter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReporterFactory {

    private final List<AbstractDeliveryJobReporter> jobReporters;

    private final List<AbstractEmailNotificationReporter> emailReporters;

    public ReporterFactory(final List<AbstractDeliveryJobReporter> reporters, final List<AbstractEmailNotificationReporter> emailReporters) {
        this.jobReporters = reporters;
        this.emailReporters = emailReporters;
    }

    public AbstractDeliveryJobReporter getJobReporter(DeliveryJob deliveryJob) {
        if (deliveryJob.getInitiator() == null) {
            return null;
        }
        return this.jobReporters.stream().filter(r -> deliveryJob.getInitiator().getReporter().isInstance(r)).findFirst().orElse(null);
    }

    public AbstractEmailNotificationReporter getEmailReporter(EmailNotification emailNotification) {
        if (emailNotification.getInitiator() == null) {
            return null;
        }
        return this.emailReporters.stream().filter(r -> emailNotification.getInitiator().getReporter().isInstance(r)).findFirst().orElse(null);
    }
}
