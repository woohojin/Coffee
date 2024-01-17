package model;

public class Product {
    String productCode;
    int productType;
    String productName;
    int productPrice;
    String productUnit;
    int productTier;
    String productFile;
    int productSoldOut;
    String productRegisterName;
    String productRegisterDate;
    String productModifierName;
    String productModifierDate;

    //bean

    String beanCountry;
    String beanSpecies;
    String beanCompany;
    String beanRegisterName;
    String beanRegitsterDate;
    String beanModifierName;
    String beanModifierDate;

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

    public String getProductRegisterName() {
        return productRegisterName;
    }

    public void setProductRegisterName(String productRegisterName) {
        this.productRegisterName = productRegisterName;
    }

    public String getProductRegisterDate() {
        return productRegisterDate;
    }

    public void setProductRegisterDate(String productRegisterDate) {
        this.productRegisterDate = productRegisterDate;
    }

    public String getProductModifierName() {
        return productModifierName;
    }

    public void setProductModifierName(String productModifierName) {
        this.productModifierName = productModifierName;
    }

    public String getProductModifierDate() {
        return productModifierDate;
    }

    public void setProductModifierDate(String productModifierDate) {
        this.productModifierDate = productModifierDate;
    }

    public String getBeanCountry() {
        return beanCountry;
    }

    public void setBeanCountry(String beanCountry) {
        this.beanCountry = beanCountry;
    }

    public String getBeanSpecies() {
        return beanSpecies;
    }

    public void setBeanSpecies(String beanSpecies) {
        this.beanSpecies = beanSpecies;
    }

    public String getBeanCompany() {
        return beanCompany;
    }

    public void setBeanCompany(String beanCompany) {
        this.beanCompany = beanCompany;
    }

    public String getBeanRegisterName() {
        return beanRegisterName;
    }

    public void setBeanRegisterName(String beanRegisterName) {
        this.beanRegisterName = beanRegisterName;
    }

    public String getBeanRegitsterDate() {
        return beanRegitsterDate;
    }

    public void setBeanRegitsterDate(String beanRegitsterDate) {
        this.beanRegitsterDate = beanRegitsterDate;
    }

    public String getBeanModifierName() {
        return beanModifierName;
    }

    public void setBeanModifierName(String beanModifierName) {
        this.beanModifierName = beanModifierName;
    }

    public String getBeanModifierDate() {
        return beanModifierDate;
    }

    public void setBeanModifierDate(String beanModifierDate) {
        this.beanModifierDate = beanModifierDate;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productCode='" + productCode + '\'' +
                ", productType=" + productType +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productUnit='" + productUnit + '\'' +
                ", productTier=" + productTier +
                ", productFile='" + productFile + '\'' +
                ", productSoldOut=" + productSoldOut +
                ", productRegisterName='" + productRegisterName + '\'' +
                ", productRegisterDate='" + productRegisterDate + '\'' +
                ", productModifierName='" + productModifierName + '\'' +
                ", productModifierDate='" + productModifierDate + '\'' +
                ", beanCountry='" + beanCountry + '\'' +
                ", beanSpecies='" + beanSpecies + '\'' +
                ", beanCompany='" + beanCompany + '\'' +
                ", beanRegisterName='" + beanRegisterName + '\'' +
                ", beanRegitsterDate='" + beanRegitsterDate + '\'' +
                ", beanModifierName='" + beanModifierName + '\'' +
                ", beanModifierDate='" + beanModifierDate + '\'' +
                '}';
    }
}
