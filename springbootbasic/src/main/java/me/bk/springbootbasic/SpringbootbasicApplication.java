package me.bk.springbootbasic;

import me.bk.springbootbasic.sampleRunner.SampleApplicationStartingListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CorporationProperties.class)
public class SpringbootbasicApplication {

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(SpringbootbasicApplication.class);
    application.addListeners(new SampleApplicationStartingListener());
    application.run(args);
  }

}
