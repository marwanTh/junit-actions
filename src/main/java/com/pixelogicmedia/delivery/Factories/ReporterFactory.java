package com.pixelogicmedia.delivery.Factories;

import com.pixelogicmedia.delivery.data.entities.DeliveryJob;
import com.pixelogicmedia.delivery.execution.AbstractDeliveryJobReporter;
import com.pixelogicmedia.delivery.execution.AbstractTransferJobExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReporterFactory {

    private final List<AbstractDeliveryJobReporter> reporters;

    public ReporterFactory(final List<AbstractDeliveryJobReporter> reporters) {
        this.reporters = reporters;
    }

    public AbstractDeliveryJobReporter getJobReporter(DeliveryJob deliveryJob) {
        if (deliveryJob.getInitiator() == null) {
            return null;
        }
        return this.reporters.stream().filter(r -> deliveryJob.getInitiator().getReporter().isInstance(r)).findFirst().orElse(null);
    }
}
