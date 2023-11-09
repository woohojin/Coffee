package model;

public class History {
  String historyCode;
  String memberId;
  String productCode;
  String productName;
  String productPrice;
  String productUnit;
  String productFile;
  String quantity;
  String orderDate;
  String deliveryAddress;
  String detailDeliveryAddress;

  public String getHistoryCode() {
    return historyCode;
  }

  public void setHistoryCode(String historyCode) {
    this.historyCode = historyCode;
  }

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

  public String getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(String productPrice) {
    this.productPrice = productPrice;
  }

  public String getProductUnit() {
    return productUnit;
  }

  public void setProductUnit(String productUnit) {
    this.productUnit = productUnit;
  }

  public String getProductFile() {
    return productFile;
  }

  public void setProductFile(String productFile) {
    this.productFile = productFile;
  }

  public String getQuantity() {
    return quantity;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }

  public String getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(String orderDate) {
    this.orderDate = orderDate;
  }

  public String getDeliveryAddress() {
    return deliveryAddress;
  }

  public void setDeliveryAddress(String deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }

  public String getDetailDeliveryAddress() {
    return detailDeliveryAddress;
  }

  public void setDetailDeliveryAddress(String detailDeliveryAddress) {
    this.detailDeliveryAddress = detailDeliveryAddress;
  }

  @Override
  public String toString() {
    return "History{" +
      "historyCode='" + historyCode + '\'' +
      ", memberId='" + memberId + '\'' +
      ", productCode='" + productCode + '\'' +
      ", productName='" + productName + '\'' +
      ", productPrice='" + productPrice + '\'' +
      ", productUnit='" + productUnit + '\'' +
      ", productFile='" + productFile + '\'' +
      ", quantity='" + quantity + '\'' +
      ", orderDate='" + orderDate + '\'' +
      ", deliveryAddress='" + deliveryAddress + '\'' +
      ", detailDeliveryAddress='" + detailDeliveryAddress + '\'' +
      '}';
  }
}
