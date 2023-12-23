package model;

public class Image {
  String productCode;
  String fileName;
  String fileModifierName;
  String fileModifiedDate;

  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileModifierName() {
    return fileModifierName;
  }

  public void setFileModifierName(String fileModifierName) {
    this.fileModifierName = fileModifierName;
  }

  public String getFileModifiedDate() {
    return fileModifiedDate;
  }

  public void setFileModifiedDate(String fileModifiedDate) {
    this.fileModifiedDate = fileModifiedDate;
  }

  @Override
  public String toString() {
    return "Image{" +
      "productCode='" + productCode + '\'' +
      ", fileName='" + fileName + '\'' +
      ", fileModifierName='" + fileModifierName + '\'' +
      ", fileModifiedDate='" + fileModifiedDate + '\'' +
      '}';
  }
}
