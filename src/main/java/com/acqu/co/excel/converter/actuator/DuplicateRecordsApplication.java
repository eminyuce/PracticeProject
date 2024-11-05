package com.acqu.co.excel.converter.actuator;

import com.acqu.co.excel.converter.actuator.service.impl.CsvProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EntityScan(basePackages = "com.acqu.co.excel.converter.actuator.model")
public class DuplicateRecordsApplication   {

    @Autowired
    private CsvProcessor csvProcessor;

    public static void main(String[] args) {
        SpringApplication.run(DuplicateRecordsApplication.class, args);
    }

    //test
    public void run(String... args) {
        String inputFilePath = "C:\\Users\\mehme\\Desktop\\TestFolder\\faire-products.csv"; // path to the input CSV
        String outputFilePath = "C:\\Users\\mehme\\Desktop\\TestFolder\\faire-products-output_v4.csv"; // path to the output CSV
        //csvProcessor.processCsv(inputFilePath, outputFilePath);
        System.out.println("DONE");

        csvProcessor.printTest();

        System.out.println(csvProcessor.getStrAppend("THINK COFFEE"));

        csvProcessor.printNewColumns();

    }
}