package com.acqu.co.excel.converter.actuator.exception;

import lombok.*;
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ServiceStatus {
    private String message;
    private int statusCode;
    private String errorDescription;
    private String timestamp  = java.time.LocalDateTime.now().toString();;

}
