package com.pixelogicmedia.delivery.data.repositories;

import com.pixelogicmedia.delivery.data.entities.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssetRepository extends JpaRepository<Asset, Long>, JpaSpecificationExecutor<Asset> {

    @Query("""
                SELECT CASE WHEN count(a) > 0 THEN true ELSE false END
                FROM Asset a JOIN a.deliveryJob j
                WHERE a.phelixAssetId = :assetId AND a.phelixAssetVersion = :assetVersion
                    AND j.status NOT IN (CANCELED, FAILED)
            """)
    boolean isAssetDuplicate(@Param("assetId") Long assetId, @Param("assetVersion") Long assetVersion);
}
