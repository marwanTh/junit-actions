package com.pixelogicmedia.delivery.Factories;

import com.pixelogicmedia.delivery.data.entities.DeliveryJob;
import com.pixelogicmedia.delivery.execution.AbstractTransferJobExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExecutorFactory {
    private final List<AbstractTransferJobExecutor> executors;

    public ExecutorFactory(final List<AbstractTransferJobExecutor> executors) {
        this.executors = executors;
    }

    public AbstractTransferJobExecutor getJobExecutor(DeliveryJob deliveryJob) {
        return this.executors.stream().filter(e -> deliveryJob.getProfile().getConnection().getType().getExecutor().isInstance(e)).findFirst().orElse(null);
    }
}
