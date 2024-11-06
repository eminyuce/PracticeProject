package com.acqu.co.excel.converter.actuator.config;



import com.okta.spring.boot.oauth.Okta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()  // Allow H2 console access
                        .requestMatchers("/api/acqu-users/**").permitAll()
                        .anyRequest().authenticated())
                .headers(headers -> headers
                        .frameOptions(frameOptions -> Customizer.withDefaults()) // You can also use `withDefaults()` if you want to stick with the default behavior
                )
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