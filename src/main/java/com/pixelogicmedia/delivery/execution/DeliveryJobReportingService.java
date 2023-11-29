package com.pixelogicmedia.delivery.execution;

import com.pixelogicmedia.delivery.Factories.ReporterFactory;
import com.pixelogicmedia.delivery.data.entities.Contact;
import com.pixelogicmedia.delivery.data.entities.DeliveryJob;
import com.pixelogicmedia.delivery.data.entities.EmailNotification;
import com.pixelogicmedia.delivery.data.repositories.DeliveryJobRepository;
import com.pixelogicmedia.delivery.data.repositories.EmailNotificationRepository;
import jakarta.transaction.Transactional;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class DeliveryJobReportingService {

    private final DeliveryJobRepository deliveryJobRepository;
    private final EmailNotificationRepository emailNotificationRepository;
    private final ReporterFactory reporterFactory;

    public DeliveryJobReportingService(final DeliveryJobRepository deliveryJobRepository, final EmailNotificationRepository emailNotificationRepository, final ReporterFactory reporterFactory) {
        this.deliveryJobRepository = deliveryJobRepository;
        this.emailNotificationRepository = emailNotificationRepository;
        this.reporterFactory = reporterFactory;
    }


    @Scheduled(fixedRate = 5000, timeUnit = TimeUnit.MILLISECONDS)
    @SchedulerLock(name = "scheduledDeliveryJobsReporting")
    @Transactional
    public void pollRunningJobs() {
        var runningJobs = this.deliveryJobRepository.findByStatus(DeliveryJob.Status.IN_PROGRESS);
        runningJobs.forEach(job -> {
            if (job.getLastReportedProgress() - job.getProgress() > 0.01) {
                return;
            }
            final var reporter = this.reporterFactory.getJobReporter(job);
            if (reporter == null) {
                return;
            }
            reporter.reportProgress(job);
            job.setLastReportedProgress(job.getProgress());
        });
    }

    public void reportCompletion(DeliveryJob job) {
        if (job.isAutoNotify() && !job.getDeliveryJobContacts().stream().filter(contact -> Objects.equals(contact.getNotifyOn(), Contact.NotifyOn.SUCCESS)).toList().isEmpty()) {
            final var emailNotification = new EmailNotification();
            emailNotification.setType(EmailNotification.Type.COMPLETION);
            emailNotification.setStatus(EmailNotification.Status.PENDING);
            emailNotification.setDeliveryJob(job);
            emailNotificationRepository.save(emailNotification);
        }
        final var reporter = this.reporterFactory.getJobReporter(job);
        if (reporter == null) {
            return;
        }
        reporter.reportCompletion(job);
    }

    public void reportFailure(DeliveryJob job) {
        if (job.isAutoNotify() && !job.getDeliveryJobContacts().stream().filter(contact -> Objects.equals(contact.getNotifyOn(), Contact.NotifyOn.FAILURE)).toList().isEmpty()) {
            final var emailNotification = new EmailNotification();
            emailNotification.setType(EmailNotification.Type.FAILURE);
            emailNotification.setStatus(EmailNotification.Status.PENDING);
            emailNotification.setDeliveryJob(job);
            emailNotificationRepository.save(emailNotification);
        }
        final var reporter = this.reporterFactory.getJobReporter(job);
        if (reporter == null) {
            return;
        }
        reporter.reportFailure(job);
    }
}
