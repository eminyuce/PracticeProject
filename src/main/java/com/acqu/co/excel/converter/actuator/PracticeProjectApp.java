package com.acqu.co.excel.converter.actuator;

import com.acqu.co.excel.converter.actuator.service.impl.CsvProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan(basePackages = "com.acqu.co.excel.converter.actuator.model")
public class PracticeProjectApp {

    @Autowired
    private CsvProcessor csvProcessor;

    public static void main(String[] args) {
        SpringApplication.run(PracticeProjectApp.class, args);
    }

    public void run(String... args) {
        System.out.println("DONE");

        csvProcessor.printTest();

        System.out.println(csvProcessor.getStrAppend("THINK COFFEE"));

        csvProcessor.printNewColumns();

    }
}