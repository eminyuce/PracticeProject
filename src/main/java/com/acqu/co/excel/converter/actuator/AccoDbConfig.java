package com.acqu.co.excel.converter.actuator;

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

//import javax.persistence.EntityManagerFactory;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(basePackages = "com.acqu.co.excel", entityManagerFactoryRef = "acquEntityManagerFactory", transactionManagerRef = "acquTransactionManager")
public class AccoDbConfig {

	@Bean(name = "acquDataSource")
	@ConfigurationProperties(prefix = "datasource.acqu")
	@Primary
	public DataSource acquDataSource() {

		return DataSourceBuilder.create().type(HikariDataSource.class).build(); // type is not necessary, it assumes
																				// this from classpath
	}

	@Primary
	@Bean(name = "acquEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("acquDataSource") DataSource dataSource) {

		return builder.dataSource(dataSource).packages("com.acqu.model").persistenceUnit("acqu").build();
	}

	@Primary
	@Bean(name = "acquTransactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("acquEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

}