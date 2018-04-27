package org.pcfapps.postgres.broker.config;

import org.pcfapps.postgres.broker.security.SecurityAuthorities;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/v2/**").hasAuthority(SecurityAuthorities.ADMIN)
                .requestMatchers(EndpointRequest.to("info", "health")).permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasAuthority(SecurityAuthorities.ADMIN)
                .and()
                .httpBasic();
        // @formatter:on
    }
}
