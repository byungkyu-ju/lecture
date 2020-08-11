package me.bk.springbootbasic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Configuration
public class ProdConfiguration {
  @Bean
  public String showConfigurationProfile() {
    return "this is prod configuration";
  }
}
