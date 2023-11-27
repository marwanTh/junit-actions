package com.pixelogicmedia.delivery.execution;

import com.pixelogicmedia.delivery.data.entities.DeliveryJob;

public abstract class AbstractDeliveryJobReporter {
    public abstract void reportProgress(DeliveryJob job);
    public abstract void reportCompletion(DeliveryJob job);
    public abstract void reportFailure(DeliveryJob job);
}
