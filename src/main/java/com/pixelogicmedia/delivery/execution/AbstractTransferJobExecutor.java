package com.pixelogicmedia.delivery.execution;

import com.pixelogicmedia.delivery.data.entities.DeliveryJob;

import java.util.List;
import java.util.Map;

public abstract class AbstractTransferJobExecutor {

    public abstract void execute(DeliveryJob job);

    public abstract void poll(List<DeliveryJob> jobs);

    public abstract Map<String,Object> getEmailContext(DeliveryJob job);
}
