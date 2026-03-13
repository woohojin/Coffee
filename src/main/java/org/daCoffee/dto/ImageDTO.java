package org.daCoffee.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageDTO {
  String productCode;
  String existProductCode; // admin productUpdate
  String fileName;
  String fileRegisterName;
  String fileRegisterDate;
  String fileModifierName;
  String fileModifierDate;
}
