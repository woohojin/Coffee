package org.daCoffee.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ImageDTO {
  String productCode;
  String existProductCode; // admin productUpdate
  String fileName;
  String fileRegisterName;
  String fileRegisterDate;
  String fileModifierName;
  String fileModifierDate;
}
