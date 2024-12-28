package com.acqu.co.excel.converter.actuator.model.specs;

import com.acqu.co.excel.converter.actuator.model.AcquUserEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

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

        // Apply search criteria if present
        if (acquUserEntitySearchParams.getCriteriaList() != null) {
            for (SearchCriteria criteria : acquUserEntitySearchParams.getCriteriaList()) {
                String operation = criteria.getOperator().toLowerCase();
                String field = criteria.getField();
                String value = criteria.getValue();

                switch (operation) {
                    case "=":
                        predicates.add(cb.equal(root.get(field), value));
                        break;
                    case "like":
                        predicates.add(cb.like(root.get(field), "%" + value + "%"));
                        break;
                    case ">":
                        predicates.add(cb.greaterThan(root.get(field), value));
                        break;
                    case "<":
                        predicates.add(cb.lessThan(root.get(field), value));
                        break;
                    case ">=":
                        predicates.add(cb.greaterThanOrEqualTo(root.get(field), value));
                        break;
                    case "<=":
                        predicates.add(cb.lessThanOrEqualTo(root.get(field), value));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid operator: " + operation);
                }
            }
        }

        // Apply date range filters
        if (acquUserEntitySearchParams.getCreatedFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), acquUserEntitySearchParams.getCreatedFrom()));
        }
        if (acquUserEntitySearchParams.getCreatedTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdDate"), acquUserEntitySearchParams.getCreatedTo()));
        }
        if (acquUserEntitySearchParams.getUpdatedFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("updatedDate"), acquUserEntitySearchParams.getUpdatedFrom()));
        }
        if (acquUserEntitySearchParams.getUpdatedTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("updatedDate"), acquUserEntitySearchParams.getUpdatedTo()));
        }

        // Combine all predicates with AND
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
