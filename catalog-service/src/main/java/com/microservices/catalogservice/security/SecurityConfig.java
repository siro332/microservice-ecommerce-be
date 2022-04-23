package com.microservices.catalogservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity security) throws Exception {

        security.csrf().and().cors().disable();
        security.authorizeRequests().antMatchers("/api/catalog/categories/**"
                ,"/api/catalog/products/**").permitAll();
        security.authorizeRequests().anyRequest().authenticated();
        security.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

    }
}
