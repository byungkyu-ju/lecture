package me.bk.springbootbasic.sampleRunner;

import me.bk.springbootbasic.CorporationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SampleApplicationRunner implements ApplicationRunner {

  private Logger logger = LoggerFactory.getLogger(SampleApplicationRunner.class);
  /*
  @Value("${corporation.name}")
  private String corporationName; */

  @Autowired
  private CorporationProperties corporationProperties;

  @Autowired
  private String configurationProfile;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    System.out.println("corporationName : " + corporationProperties.getName());
    System.out.println("system environment : " + corporationProperties.getSystemEnvironment());
    System.out.println("show configuration profile : " + configurationProfile);
    logger.info("here");
  }

}
