package com.acqu.co.excel.converter.actuator.repo;

import com.acqu.co.excel.converter.actuator.model.AcquUserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface AcquUserEntityRepository extends JpaRepository<AcquUserEntity, Long>, JpaSpecificationExecutor<AcquUserEntity> {

    List<AcquUserEntity> findAll(Sort sort);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM AcquUserEntity WHERE user_entity_id IN (:ids)", nativeQuery = true)
    void deleteByIds(List<Long> ids);

    @Query("SELECT DISTINCT a.phoneModel FROM AcquUserEntity a")
    List<String> getPhoneModels(Sort sorting);

    @Modifying
    @Transactional
    @Query("UPDATE AcquUserEntity u SET u.status = :status WHERE u.userEntityId IN :userIds")
    void updateStatusByIds(@Param("userIds") List<Long> userIds, @Param("status") String status);

    @Query("SELECT CONCAT('User ID: ', u.userEntityId, ', Status: ', u.status, ', Updated: ', u.updatedDate) FROM AcquUserEntity u WHERE u.userEntityId = :id")
    List<String> getAuditTrailByUserId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM AcquUserEntity u WHERE u.status = :status")
    void deleteByStatus(@Param("status") String status);
}
