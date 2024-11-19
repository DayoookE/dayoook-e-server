package inha.dayoook_e.common.config;

import inha.dayoook_e.utils.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static inha.dayoook_e.user.domain.enums.Permission.*;
import static inha.dayoook_e.user.domain.enums.Role.ADMIN;
import static inha.dayoook_e.user.domain.enums.Role.TUTOR;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private static final String VERSION = "/api/v1";

    // 관리자 전용 경로
    private static final String ADMIN_URL = VERSION + "/admin/**";

    // 튜터 전용 경로
    private static final String TUTOR_URL = VERSION + "/tutor/**";

    private static final String[] GET_ONLY_WHITE_LIST_URL = {
            VERSION + "/languages",
            VERSION + "/countries",
            VERSION + "/ageGroups",
            VERSION + "/days",
            VERSION + "/timeSlots",
            VERSION + "/tutors/**"
    };
    private static final String[] WHITE_LIST_URL = {
            "/",
            VERSION + "/auth/**",
            VERSION + "/test/**",
            VERSION + "/users/tutee",
            VERSION + "/users/tutor",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/index.html",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/error/**"
    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .requiresChannel(channel -> channel
                .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
                .requiresSecure())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                        .xssProtection(xss -> xss.disable())
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; " +
                                        "script-src 'self' 'unsafe-inline'; " +
                                        "style-src 'self' 'unsafe-inline'; " +
                                        "img-src 'self' data:; " +
                                        "font-src 'self' data:; " +
                                        "object-src 'none'"))
                )
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL).permitAll().
                                requestMatchers(GET, GET_ONLY_WHITE_LIST_URL).permitAll()  // GET 요청만 허용
                                // 관리자 전용 접근 설정
                                .requestMatchers(ADMIN_URL).hasRole(ADMIN.name())
                                .requestMatchers(GET, ADMIN_URL).hasAuthority(ADMIN_READ.name())
                                .requestMatchers(POST, ADMIN_URL).hasAuthority(ADMIN_CREATE.name())
                                .requestMatchers(PUT, ADMIN_URL).hasAuthority(ADMIN_UPDATE.name())
                                .requestMatchers(DELETE, ADMIN_URL).hasAuthority(ADMIN_DELETE.name())
                                //튜터 전용 접근 설정
                                .requestMatchers(TUTOR_URL).hasAnyRole(TUTOR.name(), ADMIN.name())
                                .requestMatchers(GET, TUTOR_URL).hasAnyAuthority(TUTOR_READ.name(), ADMIN_READ.name())
                                .requestMatchers(POST, TUTOR_URL).hasAnyAuthority(TUTOR_CREATE.name(), ADMIN_CREATE.name())
                                .requestMatchers(PUT, TUTOR_URL).hasAnyAuthority(TUTOR_UPDATE.name(), ADMIN_UPDATE.name())
                                .requestMatchers(DELETE, TUTOR_URL).hasAnyAuthority(TUTOR_DELETE.name(), ADMIN_DELETE.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .logout(logout ->
                        logout.logoutUrl(VERSION + "/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())

                );
        return http.build();

    }
}
