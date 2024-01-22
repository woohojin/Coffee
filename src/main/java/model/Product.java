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

    //bean

    String beanCountry;
    String beanSpecies;
    String beanCompany;
    String beanManufacturingDate;
    String beanUseByDate;
    String beanRawMaterials;
    String beanRegisterName;
    String beanRegitsterDate;
    String beanModifierName;
    String beanModifierDate;

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

    public String getBeanManufacturingDate() {
        return beanManufacturingDate;
    }

    public void setBeanManufacturingDate(String beanManufacturingDate) {
        this.beanManufacturingDate = beanManufacturingDate;
    }

    public String getBeanUseByDate() {
        return beanUseByDate;
    }

    public void setBeanUseByDate(String beanUseByDate) {
        this.beanUseByDate = beanUseByDate;
    }

    public String getBeanRawMaterials() {
        return beanRawMaterials;
    }

    public void setBeanRawMaterials(String beanRawMaterials) {
        this.beanRawMaterials = beanRawMaterials;
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

    // mix

    String mixCompany;
    String mixUseByDate;
    String mixRegisterName;
    String mixRegisterDate;
    String mixModifierName;
    String mixModifierDate;

    public String getMixCompany() {
        return mixCompany;
    }

    public void setMixCompany(String mixCompany) {
        this.mixCompany = mixCompany;
    }

    public String getMixUseByDate() {
        return mixUseByDate;
    }

    public void setMixUseByDate(String mixUseByDate) {
        this.mixUseByDate = mixUseByDate;
    }

    public String getMixRegisterName() {
        return mixRegisterName;
    }

    public void setMixRegisterName(String mixRegisterName) {
        this.mixRegisterName = mixRegisterName;
    }

    public String getMixRegisterDate() {
        return mixRegisterDate;
    }

    public void setMixRegisterDate(String mixRegisterDate) {
        this.mixRegisterDate = mixRegisterDate;
    }

    public String getMixModifierName() {
        return mixModifierName;
    }

    public void setMixModifierName(String mixModifierName) {
        this.mixModifierName = mixModifierName;
    }

    public String getMixModifierDate() {
        return mixModifierDate;
    }

    public void setMixModifierDate(String mixModifierDate) {
        this.mixModifierDate = mixModifierDate;
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
                ", beanManufacturingDate='" + beanManufacturingDate + '\'' +
                ", beanUseByDate='" + beanUseByDate + '\'' +
                ", beanRawMaterials='" + beanRawMaterials + '\'' +
                ", beanRegisterName='" + beanRegisterName + '\'' +
                ", beanRegitsterDate='" + beanRegitsterDate + '\'' +
                ", beanModifierName='" + beanModifierName + '\'' +
                ", beanModifierDate='" + beanModifierDate + '\'' +
                ", mixCompany='" + mixCompany + '\'' +
                ", mixUseByDate='" + mixUseByDate + '\'' +
                ", mixRegisterName='" + mixRegisterName + '\'' +
                ", mixRegisterDate='" + mixRegisterDate + '\'' +
                ", mixModifierName='" + mixModifierName + '\'' +
                ", mixModifierDate='" + mixModifierDate + '\'' +
                '}';
    }
}
