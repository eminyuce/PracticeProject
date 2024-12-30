package com.acqu.co.excel.converter.actuator.model.specs;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LogSearchParams {
    private List<SearchCriteria> criteriaList; // A list of criteria for search
    private LocalDate createdFrom;            // Start date for creation range
    private LocalDate createdTo;              // End date for creation range

    // Custom Pageable field as per provided snippet
    private CustomPageable pageable = new CustomPageable();
}
