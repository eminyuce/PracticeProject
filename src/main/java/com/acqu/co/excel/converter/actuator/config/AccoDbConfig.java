package com.acqu.co.excel.converter.actuator.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.acqu.co.excel.converter.actuator.repo", // Set the correct repository package
        entityManagerFactoryRef = "acquEntityManagerFactory",
        transactionManagerRef = "acquTransactionManager")
public class AccoDbConfig {

    // Inject values from application.yml using @Value annotation
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.jpa.database-platform}")
    private String dialect;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Value("${spring.jpa.show-sql}")
    private boolean showSql;

    // Define the main DataSource
    @Primary
    @Bean(name = "acquDataSource")
    public DataSource acquDataSource() {
        return DataSourceBuilder.create()
                .url(url)  // Injected via @Value
                .username(username)  // Injected via @Value
                .password(password)  // Injected via @Value
                .driverClassName(driverClassName)  // Injected via @Value
                .type(HikariDataSource.class)  // Using HikariCP as the connection pool
                .build();  // Builds the DataSource
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
