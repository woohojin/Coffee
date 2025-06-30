package org.daCoffee.dto;

public class CartDTO {
  String memberId;
  String productCode;
  String productName;
  String productFile;
  String productUnit;
  int productPrice;
  int productSoldOut;
  int quantity;
  int productGrinding;
  int productType;

  public String getMemberId() {
    return memberId;
  }

  public void setMemberId(String memberId) {
    this.memberId = memberId;
  }

  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductFile() {
    return productFile;
  }

  public void setProductFile(String productFile) {
    this.productFile = productFile;
  }

  public String getProductUnit() {
    return productUnit;
  }

  public void setProductUnit(String productUnit) {
    this.productUnit = productUnit;
  }

  public int getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(int productPrice) {
    this.productPrice = productPrice;
  }

  public int getProductSoldOut() {
    return productSoldOut;
  }

  public void setProductSoldOut(int productSoldOut) {
    this.productSoldOut = productSoldOut;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public int getProductGrinding() {
    return productGrinding;
  }

  public void setProductGrinding(int productGrinding) {
    this.productGrinding = productGrinding;
  }

  public int getProductType() {
    return productType;
  }

  public void setProductType(int productType) {
    this.productType = productType;
  }
}
