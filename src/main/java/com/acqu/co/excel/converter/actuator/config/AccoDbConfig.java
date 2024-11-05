package com.acqu.co.excel.converter.actuator.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(basePackages = "com.acqu.co.excel.converter.actuator.repo", // Set the correct repository package
        entityManagerFactoryRef = "acquEntityManagerFactory",
        transactionManagerRef = "acquTransactionManager")
public class AccoDbConfig {

    // Define the main DataSource
    @Primary
    @Bean(name = "acquDataSource")
    @ConfigurationProperties(prefix = "datasource.acqu")
    public DataSource acquDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    // Configure the EntityManagerFactory
    @Primary
    @Bean(name = "acquEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("acquDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.acqu.co.excel.converter.actuator.model") // Ensure this matches your entity package
                .persistenceUnit("acqu")
                .build();
    }

    // Configure the Transaction Manager
    @Primary
    @Bean(name = "acquTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("acquEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
