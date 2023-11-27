package com.pixelogicmedia.delivery.execution;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pixelogicmedia.delivery.data.entities.DeliveryJob;
import com.pixelogicmedia.delivery.data.om.OmQueueMessage;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class OmDeliveryJobReporter extends AbstractDeliveryJobReporter {

    @Value("${om.queue_name}")
    private String queueName;

    private final SqsTemplate sqsTemplate;

    public OmDeliveryJobReporter(final SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    @Override
    public void reportProgress(DeliveryJob job) {
        double progressPercentage = job.getProgress() * 100;
        report(new OmQueueMessage(job.getExternalId(), 200, null, (int) progressPercentage));
    }
    @Override
    public void reportCompletion(DeliveryJob job) {
        report(new OmQueueMessage(job.getExternalId(), 0, null, 100));

    }
    @Override
    public void reportFailure(DeliveryJob job) {
        report(new OmQueueMessage(job.getExternalId(), -1, null, null));
    }

    private void report(OmQueueMessage omQueueMessage) {
        String jsonMessage;
        try {
            jsonMessage = new ObjectMapper().writeValueAsString(omQueueMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        sqsTemplate.send(queueName, MessageBuilder.withPayload(jsonMessage).build());
    }
}
