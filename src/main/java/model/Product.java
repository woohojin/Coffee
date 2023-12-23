package model;

public class Product {
    String productCode;
    int productType;
    String productName;
    int productPrice;
    String productUnit;
    String productCountry;
    String productSpecies;
    String productCompany;
    int productTier;
    String productFile;
    int productSoldOut;
    String productDate;
    String productModifierName;
    String productModifiedDate;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductCountry() {
        return productCountry;
    }

    public void setProductCountry(String productCountry) {
        this.productCountry = productCountry;
    }

    public String getProductSpecies() {
        return productSpecies;
    }

    public void setProductSpecies(String productSpecies) {
        this.productSpecies = productSpecies;
    }

    public String getProductCompany() {
        return productCompany;
    }

    public void setProductCompany(String productCompany) {
        this.productCompany = productCompany;
    }

    public int getProductTier() {
        return productTier;
    }

    public void setProductTier(int productTier) {
        this.productTier = productTier;
    }

    public String getProductFile() {
        return productFile;
    }

    public void setProductFile(String productFile) {
        this.productFile = productFile;
    }

    public int getProductSoldOut() {
        return productSoldOut;
    }

    public void setProductSoldOut(int productSoldOut) {
        this.productSoldOut = productSoldOut;
    }

    public String getProductDate() {
        return productDate;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
    }

    public String getProductModifierName() {
        return productModifierName;
    }

    public void setProductModifierName(String productModifierName) {
        this.productModifierName = productModifierName;
    }

    public String getProductModifiedDate() {
        return productModifiedDate;
    }

    public void setProductModifiedDate(String productModifiedDate) {
        this.productModifiedDate = productModifiedDate;
    }

    @Override
    public String toString() {
        return "Product{" +
          "productCode='" + productCode + '\'' +
          ", productType=" + productType +
          ", productName='" + productName + '\'' +
          ", productPrice=" + productPrice +
          ", productUnit='" + productUnit + '\'' +
          ", productCountry='" + productCountry + '\'' +
          ", productSpecies='" + productSpecies + '\'' +
          ", productCompany='" + productCompany + '\'' +
          ", productTier=" + productTier +
          ", productFile='" + productFile + '\'' +
          ", productSoldOut=" + productSoldOut +
          ", productDate='" + productDate + '\'' +
          ", productModifierName='" + productModifierName + '\'' +
          ", productModifiedDate='" + productModifiedDate + '\'' +
          '}';
    }
}
