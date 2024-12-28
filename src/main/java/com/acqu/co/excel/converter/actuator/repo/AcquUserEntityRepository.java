package com.acqu.co.excel.converter.actuator.repo;

import com.acqu.co.excel.converter.actuator.model.AcquUserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface AcquUserEntityRepository extends JpaRepository<AcquUserEntity, String>, JpaSpecificationExecutor<AcquUserEntity> {

    List<AcquUserEntity> findAll(Sort sort);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM AcquUserEntity WHERE user_entity_id IN (:ids)", nativeQuery = true)
    void deleteByIds(List<Long> ids);

    @Query("SELECT DISTINCT a.phoneModel FROM AcquUserEntity a")
    List<String> getPhoneModels(Sort sorting);
}
