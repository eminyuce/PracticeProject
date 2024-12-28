package com.acqu.co.excel.converter.actuator;

import com.acqu.co.excel.converter.actuator.repo.AcquUserEntityRepository;
import com.acqu.co.excel.converter.actuator.service.impl.CsvProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;

import java.util.List;


@SpringBootApplication
@EntityScan(basePackages = "com.acqu.co.excel.converter.actuator.model")
public class PracticeProjectApp {

    @Autowired
    private CsvProcessor csvProcessor;

    public static void main(String[] args) throws Exception {

        var context = SpringApplication.run(PracticeProjectApp.class, args);
        //testGetPhoneModels(context);
    }

    public void run(String... args) {
        System.out.println("DONE");

        csvProcessor.printTest();

        System.out.println(csvProcessor.getStrAppend("THINK COFFEE"));

        csvProcessor.printNewColumns();

    }

    public static void testGetPhoneModels(ApplicationContext context) throws Exception {
        // Sorting by phoneModel
        Sort sort = Sort.by(Sort.Order.asc("phoneModel"));  // Change as per your requirement

        // Get the phone models
        List<String> phoneModels = context.getBean(AcquUserEntityRepository.class).getPhoneModels(sort);

        // Print the result
        System.out.println("Phone Models: " + phoneModels);
    }
}