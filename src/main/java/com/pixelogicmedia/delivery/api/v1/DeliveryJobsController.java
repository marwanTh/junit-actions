package com.pixelogicmedia.delivery.api.v1;

import com.pixelogicmedia.delivery.api.BaseController;
import com.pixelogicmedia.delivery.api.mappers.DeliveryJobMapper;
import com.pixelogicmedia.delivery.api.v1.models.DeliveryJobResource;
import com.pixelogicmedia.delivery.services.DeliveryJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DeliveryJobsController extends BaseController implements DeliveryJobsApi {

    private DeliveryJobMapper deliveryJobMapper;

    private DeliveryJobService deliveryJobService;

    @Autowired
    public DeliveryJobsController(final DeliveryJobMapper deliveryJobMapper, final DeliveryJobService deliveryJobService) {
        this.deliveryJobMapper = deliveryJobMapper;
        this.deliveryJobService = deliveryJobService;
    }

    @Override
    public ResponseEntity<DeliveryJobResource> createJob(final Boolean allowDuplication, final DeliveryJobResource deliveryJobResource) {
        final var job = this.deliveryJobService.createJob(this.deliveryJobMapper.map(deliveryJobResource), allowDuplication);
        return ResponseEntity.ok(this.deliveryJobMapper.map(job));
    }

    @Override
    public ResponseEntity<DeliveryJobResource> getJob(final Long id) {
        return ResponseEntity.ok(this.deliveryJobMapper.map(this.deliveryJobService.getJob(id)));
    }

    @Override
    public ResponseEntity<List<DeliveryJobResource>> listJobs(final Integer offset, final Integer limit, final String filter, final String sort) {
        final var page = this.deliveryJobService.listJobs(this.pageOf(offset, limit), this.specificationOf(filter, sort));

        return ResponseEntity.ok()
                .header(TOTAL_COUNT_HEADER, String.valueOf(page.getTotalElements()))
                .body(this.deliveryJobMapper.map(page.getContent()));
    }
}
