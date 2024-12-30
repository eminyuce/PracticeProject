package com.acqu.co.excel.converter.actuator.service;

import com.acqu.co.excel.converter.actuator.model.Log;
import com.acqu.co.excel.converter.actuator.model.specs.LogSearchParams;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface LogService {
    List<Log> getLogsByApplicationName(String applicationName);

    List<Log> getLogsByLogLevel(String logLevel);

    List<Log> getLogsByTimestampRange(LocalDateTime start, LocalDateTime end);
    Page<Log> findAll(LogSearchParams logSearchParams);
}
