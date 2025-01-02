package inha.dayoook_e.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import inha.dayoook_e.user.domain.repository.UserJpaRepository;
import inha.dayoook_e.utils.ApplicationAuditAware;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static inha.dayoook_e.common.BaseEntity.State.ACTIVE;


@Configuration
@RequiredArgsConstructor
public class AppConfig {

  private final UserJpaRepository userJpaRepository;

  @Bean
  public UserDetailsService userDetailsService() {
    return email -> userJpaRepository.findByEmailAndState(email, ACTIVE)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuditorAware<Integer> auditorAware() {
    return new ApplicationAuditAware();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  JPAQueryFactory jpaQueryFactory(EntityManager em) {
    return new JPAQueryFactory(em);
  }



}
