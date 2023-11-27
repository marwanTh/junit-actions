package com.pixelogicmedia.delivery.data.repositories;

import com.pixelogicmedia.delivery.data.entities.ProfileContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProfileContactRepository extends JpaRepository<ProfileContact, Long>, JpaSpecificationExecutor<ProfileContact>  {
    List<ProfileContact> findByProfileId(Long profileId);
}
