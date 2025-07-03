package org.daCoffee.dto;

import lombok.Data;

@Data
public class ImageDTO {
  String productCode;
  String existProductCode; // admin productUpdate
  String fileName;
  String fileRegisterName;
  String fileRegisterDate;
  String fileModifierName;
  String fileModifierDate;

  @Override
  public String toString() {
    return "Image{" +
            "productCode='" + productCode + '\'' +
            ", fileName='" + fileName + '\'' +
            ", fileRegisterName='" + fileRegisterName + '\'' +
            ", fileRegisterDate='" + fileRegisterDate + '\'' +
            ", fileModifierName='" + fileModifierName + '\'' +
            ", fileModifierDate='" + fileModifierDate + '\'' +
            '}';
  }
}
