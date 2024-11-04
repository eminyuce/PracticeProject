package com.acqu.co.excel.converter.actuator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fedex.fiis.util.GeneralProperties;
import com.okta.spring.boot.oauth.Okta;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	@Autowired
	private GeneralProperties generalProperties;

	@Autowired
	JwtRequestFilter jwtRequestFilter;

	@Bean
	static GrantedAuthorityDefaults grantedAuthorityDefaults() {
		return new GrantedAuthorityDefaults("");
	}

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		String adminGroup = generalProperties.getAdminGroup();

		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests((authorize) -> authorize
					 
			 
						.requestMatchers("/api/acqu-users/**").permitAll() 
			 
				 

						.anyRequest().authenticated())
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
		// .exceptionHandling((exception)-> exception
		// .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
		// )
		;
		Okta.configureResourceServer401ResponseBody(http);

		return http.build();

	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		// return (web) -> web.ignoring().requestMatchers("/**");
		return (web) -> web.ignoring().requestMatchers("/v2/api-docs", "/v3/api-docs/**", "/swagger-ui.html",
				"/configuration/ui", "/swagger-resources/**", "/swagger-ui/**", "/configuration/**", "/webjars/**",
				"/actuator/**", "/favicon.ico");
	}

}