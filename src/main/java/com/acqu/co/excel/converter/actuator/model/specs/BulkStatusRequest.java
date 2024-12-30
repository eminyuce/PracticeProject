package com.acqu.co.excel.converter.actuator.model.specs;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BulkStatusRequest {
    private List<Long> userIds;
    private String status;
}
