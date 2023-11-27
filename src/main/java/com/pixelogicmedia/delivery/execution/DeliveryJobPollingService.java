package com.pixelogicmedia.delivery.execution;

import com.pixelogicmedia.delivery.Factories.ExecutorFactory;
import com.pixelogicmedia.delivery.data.entities.Connection;
import com.pixelogicmedia.delivery.data.entities.DeliveryJob;
import com.pixelogicmedia.delivery.data.repositories.DeliveryJobRepository;
import jakarta.transaction.Transactional;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DeliveryJobPollingService {
    private final DeliveryJobRepository deliveryJobRepository;

    private final ExecutorFactory executorFactory;

    public DeliveryJobPollingService(final DeliveryJobRepository deliveryJobRepository, final ExecutorFactory executorFactory) {
        this.deliveryJobRepository = deliveryJobRepository;
        this.executorFactory = executorFactory;
    }

    @Scheduled(fixedRate = 5000, timeUnit = TimeUnit.MILLISECONDS)
    @SchedulerLock(name = "scheduledDeliveryJobsPolling")
    @Transactional
    public void pollRunningJobs() {
        var runningJobs = this.deliveryJobRepository.findByStatus(DeliveryJob.Status.IN_PROGRESS);
        Map<AbstractTransferJobExecutor, List<DeliveryJob>> executorListMap = new HashMap<>();
        for (final var job : runningJobs) {
            final var executor = executorFactory.getJobExecutor(job);
            if (executor == null) {
                continue;
            }
            if (executorListMap.get(executor) == null) {
                executorListMap.put(executor, new ArrayList<>(List.of(job)));
            } else {
                executorListMap.get(executor).add(job);
            }
        }
        for (final var entry: executorListMap.entrySet()) {
            entry.getKey().poll(entry.getValue());
        }
    }
}
