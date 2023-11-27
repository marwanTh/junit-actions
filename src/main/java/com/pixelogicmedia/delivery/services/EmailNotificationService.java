package com.pixelogicmedia.delivery.services;

import com.pixelogicmedia.delivery.Factories.ExecutorFactory;
import com.pixelogicmedia.delivery.data.dto.mh.MessageRequestData;
import com.pixelogicmedia.delivery.data.entities.Connection;
import com.pixelogicmedia.delivery.data.entities.Contact;
import com.pixelogicmedia.delivery.data.entities.EmailNotification;
import com.pixelogicmedia.delivery.data.repositories.DeliveryJobContactRepository;
import com.pixelogicmedia.delivery.data.repositories.EmailNotificationRepository;
import com.pixelogicmedia.delivery.execution.AbstractTransferJobExecutor;
import com.pixelogicmedia.delivery.execution.aspera.AsperaNodeTransferInfo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class EmailNotificationService {

    private final MessageHandlerClient messageHandlerClient;

    private final TransactionTemplate transactionTemplate;

    private final EmailNotificationRepository emailNotificationRepository;

    private final DeliveryJobContactRepository deliveryJobContactRepository;

    private final ExecutorFactory executorFactory;

    public EmailNotificationService(final MessageHandlerClient messageHandlerClient, final TransactionTemplate transactionTemplate, EmailNotificationRepository emailNotificationRepository, DeliveryJobContactRepository deliveryJobContactRepository, final ExecutorFactory executorFactory) {
        this.messageHandlerClient = messageHandlerClient;
        this.transactionTemplate = transactionTemplate;
        this.emailNotificationRepository = emailNotificationRepository;
        this.deliveryJobContactRepository = deliveryJobContactRepository;
        this.executorFactory = executorFactory;
    }

    @Scheduled(fixedRate = 500, timeUnit = TimeUnit.MILLISECONDS)
    public void pollNextEmail() {
        do {
            final var foundEmails = this.transactionTemplate.execute(status -> this.pollAndExecute());

            if (Boolean.FALSE.equals(foundEmails)) {
                break;
            }
        } while (true);
    }

    private boolean pollAndExecute() {
        final var email = this.emailNotificationRepository.getNextEmail();

        if (email.isEmpty()) {
            return false;
        }

        if (Objects.equals(EmailNotification.Status.PENDING,email.get().getStatus())) {
            this.send(email.get());
        } else {
            this.updateEmailStatus(email.get());
        }

        return true;
    }


    private void updateEmailStatus(EmailNotification emailNotification) {
        if (emailNotification.getMessageHandlerRequestId() == null) {
            emailNotification.setStatus(EmailNotification.Status.FAILED);
            return;
        }
        var status = messageHandlerClient.getEmailStatus(emailNotification.getMessageHandlerRequestId());

        switch (status) {
            case("delivered") -> emailNotification.setStatus(EmailNotification.Status.DELIVERED);
            case("partially delivered") -> emailNotification.setStatus(EmailNotification.Status.PARTIALLY_DELIVERED);
            case("bounced") -> emailNotification.setStatus(EmailNotification.Status.BOUNCED);
            case("dropped") -> emailNotification.setStatus(EmailNotification.Status.DROPPED);
            case("deferred") -> emailNotification.setStatus(EmailNotification.Status.DEFERRED);
            case("failed") -> emailNotification.setStatus(EmailNotification.Status.FAILED);
        }
    }

    private void send(EmailNotification emailNotification) {
        final var job = emailNotification.getDeliveryJob();
        final var executor = this.executorFactory.getJobExecutor(job);

        if (executor == null) {
            // todo log error
            emailNotification.setStatus(EmailNotification.Status.FAILED);
        } else {
            this.sendTransferEmail(emailNotification, executor);
        }
    }


    private void sendTransferEmail(EmailNotification emailNotification, AbstractTransferJobExecutor executor) {
        final var job = emailNotification.getDeliveryJob();
        if (Objects.equals(emailNotification.getType(), EmailNotification.Type.Completion)) {

            Map<String, Object> emailBodyData = executor.getEmailContext(job);
            List<MessageRequestData.Recipient> recipients = new ArrayList<>();

            final var onCompletionContacts = deliveryJobContactRepository.findDeliveryJobContactsByDeliveryJobIdAndNotifyOn(job.getId(), Contact.NotifyOn.SUCCESS);
            for (var contact: onCompletionContacts) {
                MessageRequestData.Recipient.RecipientType recipientType = MessageRequestData.Recipient.RecipientType.To;
                switch (contact.getSendType()) {
                    case CC -> recipientType = MessageRequestData.Recipient.RecipientType.CC;
                    case BCC -> recipientType = MessageRequestData.Recipient.RecipientType.BCC;
                }
                //Todo change the recipient with the email
                recipients.add(new MessageRequestData.Recipient("Pa","individual", contact.getContact().getEmail(), recipientType));
            }

            //todo store the template key in an environment variable
            var requestId = messageHandlerClient.sendEmail(new MessageRequestData("delivery_test",
                    new MessageRequestData.Payload(recipients, job.getEmailSubject(), emailBodyData)));

            emailNotification.setMessageHandlerRequestId(requestId);
            emailNotification.setStatus(EmailNotification.Status.SENDING);
        }
    }

}
