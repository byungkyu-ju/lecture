package me.bk.springbootbasic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TestConfiguration {
  @Bean
  public String showConfigurationProfile() {
    return "this is test configuration";
  }
}
