package com.buixuantruong.shopapp.Configuration;

import com.buixuantruong.shopapp.model.Role;
import com.buixuantruong.shopapp.security.jwt.JWTTokenFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class WebSecurityConfig {

    JWTTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers("/api/v1/users/register").permitAll()
                            .requestMatchers("/api/v1/users/login").permitAll()
                            //category
                            .requestMatchers(GET, "/api/v1/categories?**").hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(POST, "/api/v1/categories/**").hasRole(Role.ADMIN)
                            .requestMatchers(PUT, "/api/v1/categories/**").hasRole(Role.ADMIN)
                            .requestMatchers(DELETE, "/api/v1/categories/**").hasRole(Role.ADMIN)
                            //product
                            .requestMatchers(GET, "/api/v1/products?**").hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(POST, "/api/v1/products/**").hasRole(Role.ADMIN)
                            .requestMatchers(PUT, "/api/v1/products/**").hasRole(Role.ADMIN)
                            .requestMatchers(DELETE, "/api/v1/products/**").hasRole(Role.ADMIN)
                            //order
                            .requestMatchers(PUT, "/api/v1/orders/**").hasRole(Role.ADMIN)
                            .requestMatchers(GET, "/api/v1/orders/**").hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(DELETE, "/api/v1/orders/**").hasRole(Role.ADMIN)
                            .requestMatchers(POST, "/api/v1/orders").hasRole(Role.USER)
                            //orderDetail
                            .requestMatchers(PUT, "/api/v1/order_details").hasRole(Role.ADMIN)
                            .requestMatchers(GET, "/api/v1/order_details/**").hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(DELETE, "/api/v1/order_details/**").hasRole(Role.ADMIN)
                            .requestMatchers(POST, "/api/v1/order_details").hasRole(Role.USER)
                            .anyRequest().authenticated();
                });

        return security.build();
    }
}
