package com.fitcrew.FitCrewAppTrainers.security;


import com.fitcrew.FitCrewAppTrainers.service.trainer.signin.TrainerSignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final Environment environment;
    private final TrainerSignInService trainerSignInService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(Environment environment,
                       TrainerSignInService trainerSignInService) {
        this.environment = environment;
        this.trainerSignInService = trainerSignInService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/**")
                .permitAll()
                .and()
                .addFilter(getAuthenticationFilter());
        http
                .headers()
                .frameOptions()
                .disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(environment, authenticationManager(), trainerSignInService);
        authenticationFilter.setFilterProcessesUrl(Objects.requireNonNull(environment.getProperty("login.url.path")));
        return authenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(trainerSignInService).passwordEncoder(bCryptPasswordEncoder);
    }
}
