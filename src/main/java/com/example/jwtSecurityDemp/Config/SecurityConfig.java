package com.example.jwtSecurityDemp.Config;


import com.example.jwtSecurityDemp.expection.authExpection;
import com.example.jwtSecurityDemp.filters.jwttokenfilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

@Configuration
public class SecurityConfig {
    @Autowired
    jwttokenfilter jwtfilter;

    @Autowired
    authExpection authexpection;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors->cors.disable())
                .authorizeHttpRequests(request-> request.requestMatchers("/loginuser","/addadmin","/adduser","/adminregistration","/userregistration","/login"
                ,"/GlobalResource","/adminResource","/home").permitAll().requestMatchers("/display").hasRole("ADMIN").requestMatchers("/display_any").hasAnyRole("USER","ADMIN"))
                .csrf(csrf -> csrf.disable()).addFilterBefore(jwtfilter, BasicAuthenticationFilter.class).exceptionHandling(cus->cus.accessDeniedHandler(authexpection));


        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}
