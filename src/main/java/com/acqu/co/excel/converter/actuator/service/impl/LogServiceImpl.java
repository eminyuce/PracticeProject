package com.acqu.co.excel.converter.actuator.service.impl;

import com.acqu.co.excel.converter.actuator.model.AcquUserEntity;
import com.acqu.co.excel.converter.actuator.model.Log;
import com.acqu.co.excel.converter.actuator.model.specs.AcquUserEntitySearchParams;
import com.acqu.co.excel.converter.actuator.model.specs.AcquUserEntitySpecification;
import com.acqu.co.excel.converter.actuator.model.specs.LogEntitySpecification;
import com.acqu.co.excel.converter.actuator.model.specs.LogSearchParams;
import com.acqu.co.excel.converter.actuator.repo.LogRepository;
import com.acqu.co.excel.converter.actuator.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public List<Log> getLogsByApplicationName(String applicationName) {
        return logRepository.findByApplicationName(applicationName);
    }
    @Override
    public List<Log> getLogsByLogLevel(String logLevel) {
        return logRepository.findByLogLevel(logLevel);
    }
    @Override
    public List<Log> getLogsByTimestampRange(LocalDateTime start, LocalDateTime end) {
        return logRepository.findByTimestampBetween(start, end);
    }

    @Override
    public Page<Log> findAll(LogSearchParams logSearchParams) {
        return logRepository.findAll(
                new LogEntitySpecification(logSearchParams),
                logSearchParams.getPageable().toPageRequest());
    }
}