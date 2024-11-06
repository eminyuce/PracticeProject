package com.acqu.co.excel.converter.actuator.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import com.fedex.fiis.exception.Status;

@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "error")
public class ErrorProperties {

	private Status serviceUnavailable;
	private Status noErrors;
	private Status notAuthorized;
	private Status notFound;

	public Status getServiceUnavailable() {
		return serviceUnavailable;
	}

	public void setServiceUnavailable(Status serviceUnavailable) {
		this.serviceUnavailable = serviceUnavailable;
	}

	public Status getNoErrors() {
		return noErrors;
	}

	public void setNoErrors(Status noErrors) {
		this.noErrors = noErrors;
	}

	public Status getNotAuthorized() {
		return notAuthorized;
	}

	public void setNotAuthorized(Status notAuthorized) {
		this.notAuthorized = notAuthorized;
	}

	public Status getNotFound() {
		return notFound;
	}

	public void setNotFound(Status notFound) {
		this.notFound = notFound;
	}

}
