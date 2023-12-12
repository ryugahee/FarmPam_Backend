package com.fp.backend.security.config;

import com.fp.backend.account.service.UserDetailService;
import com.fp.backend.oauth2.handler.OAuth2LoginFailureHandler;
import com.fp.backend.oauth2.handler.OAuth2LoginSuccessHandler;
import com.fp.backend.oauth2.service.PrincipalOauth2UserService;
import com.fp.backend.system.config.redis.RedisService;
import com.fp.backend.system.jwt.JwtAccessDeniedHandler;
import com.fp.backend.system.jwt.JwtFilter;
import com.fp.backend.system.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final UserDetailService userDetailService;

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    private final PrincipalOauth2UserService principalOauth2UserService;

    private final TokenProvider tokenProvider;

    private final RedisService redisUserService;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//        JwtFilter customFilter = new JwtFilter(tokenProvider);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(smt ->
                        smt.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않겠다. jwt bearer 방식으로 하기 위해
                )
//                .logout(logout -> logout
//                        .logoutUrl("/api/userLogout")
//                        .logoutSuccessUrl("http://localhost:3000/")
//                        .deleteCookies("accessToken", "refreshToken", "username", "roles")
//                        )

                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers(
                                        new AntPathRequestMatcher("/api/user/signup"),
                                        new AntPathRequestMatcher("/api/login"),
                                        new AntPathRequestMatcher("/api/checkPhoneNumber"),
                                        new AntPathRequestMatcher("/api/userLogout"),
                                        new AntPathRequestMatcher("/favicon.ico")

                                ).permitAll()
                                .anyRequest().authenticated()
//                                .anyRequest().permitAll()
                )

                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(principalOauth2UserService))  // customUserService 설정
                )

                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource()))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                )


                .apply(new MyCustomDsl())
        ;


        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailService);

        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return daoAuthenticationProvider;
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilterBefore(new JwtFilter(tokenProvider, redisUserService), UsernamePasswordAuthenticationFilter.class)
            ;

        }
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:8081");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}