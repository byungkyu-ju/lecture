package me.bk.springbootbasic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("local")
@Configuration
public class BaseConfiguration {
  @Bean
  public String showConfigurationProfile() {
    return "this is local configuration";
  }
}
