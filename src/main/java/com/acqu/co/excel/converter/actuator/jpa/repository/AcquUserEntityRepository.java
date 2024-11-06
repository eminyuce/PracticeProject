

package com.acqu.co.excel.converter.actuator.jpa.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.acqu.co.excel.converter.actuator.jpa.entity.AcquUserEntity;


@Repository
public interface AcquUserEntityRepository extends JpaRepository<TmdUsers, String> {

	List<AcquUserEntity> findAll(Sort sort);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM AcquUserEntity WHERE user_entity_id IN (:ids)", nativeQuery = true)
	void deleteByIds(List<String> ids);

}
