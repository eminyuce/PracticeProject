package com.acqu.co.excel.converter.actuator.model.specs;

import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AcquUserEntitySearchParams {
    private String search;
    private CustomPageable pageable;
}
