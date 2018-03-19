package ru.innopolis.config;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Manages the App Security Configurations.
 *
 * @author      Ilya Borovik
 * @version     1.0
 */
@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
    static {
        PropertyConfigurator.configure(AppSecurityConfig.class.getClassLoader().getResource("log4j.properties"));
    }

    private static final Logger LOGGER = LogManager.getLogger(AppSecurityConfig.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOGGER.info("Configuring HttpSecurity");

        http
            .authorizeRequests()
                .antMatchers("/", "/login", "/sign-up").permitAll()
                .antMatchers("/order-taxi", "/history").authenticated()
                .and()
            .exceptionHandling()
                .accessDeniedPage("/403")
                .and()
            .logout()
                .logoutSuccessUrl("/");
    }
}