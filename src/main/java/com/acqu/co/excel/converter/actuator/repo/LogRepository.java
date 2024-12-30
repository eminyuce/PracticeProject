package com.acqu.co.excel.converter.actuator.repo;

import com.acqu.co.excel.converter.actuator.model.AcquUserEntity;
import com.acqu.co.excel.converter.actuator.model.Log;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long>, JpaSpecificationExecutor<Log> {
    List<Log> findByApplicationName(String applicationName);
    List<Log> findByLogLevel(String logLevel);
    List<Log> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}