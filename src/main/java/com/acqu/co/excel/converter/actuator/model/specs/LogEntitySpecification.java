package com.acqu.co.excel.converter.actuator.model.specs;

import com.acqu.co.excel.converter.actuator.model.AcquUserEntity;
import com.acqu.co.excel.converter.actuator.model.Log;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class LogEntitySpecification implements Specification<Log> {

    LogSearchParams logSearchParams;

    public LogEntitySpecification(LogSearchParams logSearchParams) {
        this.logSearchParams=logSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
