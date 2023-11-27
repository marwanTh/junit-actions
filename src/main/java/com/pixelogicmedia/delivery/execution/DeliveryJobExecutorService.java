package com.pixelogicmedia.delivery.execution;

import com.pixelogicmedia.delivery.Factories.ExecutorFactory;
import com.pixelogicmedia.delivery.data.entities.DeliveryJob;
import com.pixelogicmedia.delivery.data.repositories.DeliveryJobRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.TimeUnit;

@Service
public class DeliveryJobExecutorService {

    private final DeliveryJobRepository deliveryJobRepository;

    private final TransactionTemplate transactionTemplate;

    private final ExecutorFactory executorFactory;

    public DeliveryJobExecutorService(final DeliveryJobRepository deliveryJobRepository, final TransactionTemplate transactionTemplate, final ExecutorFactory executorFactory) {
        this.deliveryJobRepository = deliveryJobRepository;
        this.transactionTemplate = transactionTemplate;
        this.executorFactory = executorFactory;
    }


    @Scheduled(fixedRate = 500, timeUnit = TimeUnit.MILLISECONDS)
    public void pollNextJob() {
        do {
            final var foundJobs = this.transactionTemplate.execute(status -> this.pollAndExecute());

            if (Boolean.FALSE.equals(foundJobs)) {
                break;
            }
        } while (true);
    }

    private boolean pollAndExecute() {
        final var job = this.deliveryJobRepository.getNextJob();

        if (job.isEmpty()) {
            return false;
        }

        this.execute(job.get());
        return true;
    }

    private void execute(final DeliveryJob job) {
        final var executor = this.executorFactory.getJobExecutor(job);

        if (executor == null) {
            // TODO add error message
            job.setStatus(DeliveryJob.Status.FAILED);
        } else {
            executor.execute(job);
        }
    }
}
