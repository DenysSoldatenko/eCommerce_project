package com.example.customer.configurations;

import com.example.library.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CustomerConfiguration {
  private final CustomerRepository customerRepository;

  @Autowired
  public CustomerConfiguration(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new CustomerServiceConfig(customerRepository);
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
        .authorizeRequests(authorize -> authorize.requestMatchers("/*").permitAll()
        .requestMatchers("/customer/*").hasAuthority("CUSTOMER"))
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