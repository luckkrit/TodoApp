package com.k9.todoapp.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class SecurityConfiguration : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests()?.antMatchers("/**")?.permitAll()?.and()?.authorizeRequests()
            ?.antMatchers("/h2-console/**")?.permitAll()
        http?.csrf()?.disable()
        http?.headers()?.frameOptions()?.disable()
        super.configure(http)
    }
}