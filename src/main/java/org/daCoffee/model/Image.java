package org.daCoffee.model;

public class Image {
  String productCode;
  String existProductCode; // admin productUpdate
  String fileName;
  String fileRegisterName;
  String fileRegisterDate;
  String fileModifierName;
  String fileModifierDate;

  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public String getExistProductCode() {
    return existProductCode;
  }

  public void setExistProductCode(String existProductCode) {
    this.existProductCode = existProductCode;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileRegisterName() {
    return fileRegisterName;
  }

  public void setFileRegisterName(String fileRegisterName) {
    this.fileRegisterName = fileRegisterName;
  }

  public String getFileRegisterDate() {
    return fileRegisterDate;
  }

  public void setFileRegisterDate(String fileRegisterDate) {
    this.fileRegisterDate = fileRegisterDate;
  }

  public String getFileModifierName() {
    return fileModifierName;
  }

  public void setFileModifierName(String fileModifierName) {
    this.fileModifierName = fileModifierName;
  }

  public String getFileModifierDate() {
    return fileModifierDate;
  }

  public void setFileModifierDate(String fileModifierDate) {
    this.fileModifierDate = fileModifierDate;
  }

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
