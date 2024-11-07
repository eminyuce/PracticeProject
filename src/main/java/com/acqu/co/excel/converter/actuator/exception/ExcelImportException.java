package com.acqu.co.excel.converter.actuator.exception;

public class ExcelImportException extends RuntimeException {

    // Constructor with message
    public ExcelImportException(String message) {
        super(message); // Pass the message to the parent RuntimeException class
    }

    // Constructor with message and cause
    public ExcelImportException(String message, Throwable cause) {
        super(message, cause); // Pass the message and cause to the parent RuntimeException class
    }

    // Constructor with cause only
    public ExcelImportException(Throwable cause) {
        super(cause); // Pass the cause to the parent RuntimeException class
    }
}