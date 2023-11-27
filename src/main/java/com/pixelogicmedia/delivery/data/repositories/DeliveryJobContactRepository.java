package com.pixelogicmedia.delivery.data.repositories;
import com.pixelogicmedia.delivery.data.entities.Contact;
import com.pixelogicmedia.delivery.data.entities.DeliveryJobContact;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryJobContactRepository extends JpaRepository<DeliveryJobContact, Long>, JpaSpecificationExecutor<DeliveryJobContact> {

    @EntityGraph(value = "DeliveryJobContact.contact", type = EntityGraph.EntityGraphType.LOAD)
    List<DeliveryJobContact> findDeliveryJobContactsByDeliveryJobIdAndNotifyOn(Long deliveryJobId, Contact.NotifyOn notifyOn);
}
