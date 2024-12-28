package com.acqu.co.excel.converter.actuator.model.specs;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private String field;    // The field to be searched
    private String operator; // The operator (e.g., '=', '>', '<', etc.)
    private String value;    // The value to search for
}