package com.pixelogicmedia.delivery.execution;

import com.pixelogicmedia.delivery.data.dto.am.AssetGeneralData;
import com.pixelogicmedia.delivery.data.entities.DeliveryJob;
import com.pixelogicmedia.delivery.exceptions.BusinessException;
import com.pixelogicmedia.delivery.services.AssetManagerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PathResolverService {

    private final AssetManagerClient assetManagerClient;

    @Autowired
    public PathResolverService(final AssetManagerClient assetManagerClient) {
        this.assetManagerClient = assetManagerClient;
    }

    public void resolvePaths(final DeliveryJob job) {
        if (job.getAssets() == null || job.getAssets().isEmpty()) {
            throw BusinessException.badRequest("Submitted Job doesn't have any assets.");
        }

        job.getAssets().forEach(asset -> {
            final AssetGeneralData data;
            if (asset.getPhelixAssetVersion() != null) {
                data = this.assetManagerClient.getAssetGeneralDataByOmAssetVersion(asset.getPhelixAssetVersion());
            } else if (asset.getPhelixAssetId() != null) {
                data = this.assetManagerClient.getAssetGeneralDataByOmAssetId(asset.getPhelixAssetId());
            } else {
                throw BusinessException.badRequest("Either assetId or assetVersionId should be provided for the Asset");
            }

            final var location = Optional.ofNullable(data.getLocations()).stream()
                    .flatMap(Collection::stream)
                    .filter(it -> it.getStorageType().equalsIgnoreCase("ON_PREM"))
                    .findFirst();

            if (location.isEmpty()) {
                throw BusinessException.of(HttpStatus.PRECONDITION_FAILED, "Can't find asset location on local storages");
            }

            asset.setPhelixAssetId(data.getOmAssetId());
            asset.setPhelixAssetVersion(data.getOmAssetVersionId());

            asset.setStorageId(location.get().getStorageId());
            asset.setPath(location.get().getPath());
            asset.setTitleName(data.getTitle().getName());
            asset.setAlphaName(data.getAlpha().getName());
        });
    }
}
