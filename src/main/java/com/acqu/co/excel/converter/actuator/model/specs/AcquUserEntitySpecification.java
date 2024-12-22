package com.acqu.co.excel.converter.actuator.model.specs;

import com.acqu.co.excel.converter.actuator.model.AcquUserEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AcquUserEntitySpecification implements Specification<AcquUserEntity> {

    private final AcquUserEntitySearchParams acquUserEntitySearchParams;

    public AcquUserEntitySpecification(AcquUserEntitySearchParams acquUserEntitySearchParams) {
        this.acquUserEntitySearchParams = acquUserEntitySearchParams;
    }

    @Override
    public Predicate toPredicate(Root<AcquUserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.hasText(acquUserEntitySearchParams.getSearch())) {
            predicates.add(cb.or(
                    cb.like(root.get("userName"), "%" + acquUserEntitySearchParams.getSearch() + "%")
            ));
        }
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}