package me.bk.springbootbasic;

import java.util.Objects;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("corporation")
public class CorporationProperties {
  private String name;
  private String businessNumber;
  private String systemEnvironment;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBusinessNumber() {
    return businessNumber;
  }

  public void setBusinessNumber(String businessNumber) {
    this.businessNumber = businessNumber;
  }

  public String getSystemEnvironment() {
    return systemEnvironment;
  }

  public void setSystemEnvironment(String systemEnvironment) {
    this.systemEnvironment = systemEnvironment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CorporationProperties that = (CorporationProperties) o;
    return Objects.equals(name, that.name) &&
        Objects.equals(businessNumber, that.businessNumber) &&
        Objects.equals(systemEnvironment, that.systemEnvironment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, businessNumber, systemEnvironment);
  }
}
