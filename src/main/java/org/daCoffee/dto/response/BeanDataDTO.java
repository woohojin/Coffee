package org.daCoffee.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BeanDataDTO {
  private String beanSpecies;
  private String beanCompany;
  private String beanUseByDate;
  private String beanCountry;
}