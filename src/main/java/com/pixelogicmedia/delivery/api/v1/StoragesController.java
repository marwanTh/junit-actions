package com.pixelogicmedia.delivery.api.v1;

import com.pixelogicmedia.delivery.api.BaseController;
import com.pixelogicmedia.delivery.api.mappers.StorageMapper;
import com.pixelogicmedia.delivery.api.v1.models.StorageResource;
import com.pixelogicmedia.delivery.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StoragesController extends BaseController implements StoragesApi {

    private final StorageService storageService;

    private final StorageMapper storageMapper;

    @Autowired
    public StoragesController(final StorageService storageService, final StorageMapper storageMapper) {
        this.storageService = storageService;
        this.storageMapper = storageMapper;
    }

    @Override
    public ResponseEntity<StorageResource> createStorage(final StorageResource storageResource) {
        final var storage = this.storageService.createStorage(this.storageMapper.map(storageResource));

        return ResponseEntity.ok(this.storageMapper.map(storage));
    }

    @Override
    public ResponseEntity<List<StorageResource>> listStorages(final Integer offset, final Integer limit, final String filter, final String sort) {
        final var page = this.storageService.listStorages(this.pageOf(offset, limit), this.specificationOf(filter, sort));

        return ResponseEntity.ok()
                .header(TOTAL_COUNT_HEADER, String.valueOf(page.getTotalElements()))
                .body(this.storageMapper.map(page.getContent()));
    }

    @Override
    public ResponseEntity<StorageResource> updateStorage(final Long id, final StorageResource storageResource) {
        final var storage = this.storageService.updateStorage(this.storageMapper.map(storageResource.id(id)));

        return ResponseEntity.ok(this.storageMapper.map(storage));
    }
}
