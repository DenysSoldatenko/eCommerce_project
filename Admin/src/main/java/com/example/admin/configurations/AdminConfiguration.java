package com.example.admin.configurations;

import com.example.library.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configuration class for security settings.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AdminConfiguration {

  private final AdminRepository adminRepository;

  @Bean
  public UserDetailsService userDetailsService() {
    return new AdminServiceConfig(adminRepository);
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Creates a DaoAuthenticationProvider bean for authentication.
   *
   * @return The DaoAuthenticationProvider bean.
   */
  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService());
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  /**
   * Configures the security filter chain.
   *
   * @param http The HttpSecurity instance.
   * @return The configured SecurityFilterChain.
   * @throws Exception if an error occurs during configuration.
  */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authorize -> authorize
        .requestMatchers("/login", "/register-new", "/register", "/vendor/**", "/js/**", "/css/**").permitAll()
        .requestMatchers("/admin/**").hasAuthority("ADMIN")
        .anyRequest().authenticated())
        .formLogin(form -> form.loginPage("/login")
        .loginProcessingUrl("/do-login")
        .defaultSuccessUrl("/index")
        .permitAll())
        .logout(logout -> logout.invalidateHttpSession(true)
        .clearAuthentication(true)
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/login?logout")
        .permitAll());

    return http.build();
  }
}
