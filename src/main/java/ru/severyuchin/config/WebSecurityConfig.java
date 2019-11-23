package ru.severyuchin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.severyuchin.security.AuthSuccesHandler;
import ru.severyuchin.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;

    @Autowired
    AuthSuccesHandler authSuccesHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .csrf().disable();
        http
                .authorizeRequests()
                    .antMatchers("/","/reg**").permitAll()
                    .antMatchers("/bootstrap/css/**").permitAll()
                    .antMatchers("/popper.js/**").permitAll()
                    .antMatchers("/jquery/**").permitAll()
                    .antMatchers("/static/js/**").permitAll()
                    .antMatchers("/api/getUserByName/**").permitAll()
                    .antMatchers("/admin/**").hasAuthority("ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .successHandler(authSuccesHandler)
                    .permitAll()
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .invalidateHttpSession(true)
                    .logoutSuccessUrl("/")
                    .permitAll();
    }


    @Override
    protected UserDetailsService userDetailsService() {
        return userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("{noop}admin").roles("ADMIN")
                .and()
                .withUser("user").password("{noop}user").roles("USER");
        //auth.authenticationProvider(daoAuthenticationProvider());
    }

//    @Bean
//    DaoAuthenticationProvider daoAuthenticationProvider(){
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(encoder);
//        provider.setUserDetailsService(userService);
//        return provider;
//    }



}
