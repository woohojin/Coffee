package org.daCoffee.dto;

import lombok.Data;

@Data
public class ProductDTO {
    String productCode;
    String existProductCode; // admin productUpdate
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
    String beanUseByDate;
    String beanRegisterName;
    String beanRegisterDate;
    String beanModifierName;
    String beanModifierDate;

    // mix

    String mixCompany;
    String mixUseByDate;
    String mixRegisterName;
    String mixRegisterDate;
    String mixModifierName;
    String mixModifierDate;

    // cafe

//    String cafeCompany;
//    String cafeRegisterName;
//    String cafeRegisterDate;
//    String cafeModifierName;
//    String cafeModifierDate;
//
//    public String getCafeCompany() {
//        return cafeCompany;
//    }
//
//    public void setCafeCompany(String cafeCompany) {
//        this.cafeCompany = cafeCompany;
//    }
//
//    public String getCafeRegisterName() {
//        return cafeRegisterName;
//    }
//
//    public void setCafeRegisterName(String cafeRegisterName) {
//        this.cafeRegisterName = cafeRegisterName;
//    }
//
//    public String getCafeRegisterDate() {
//        return cafeRegisterDate;
//    }
//
//    public void setCafeRegisterDate(String cafeRegisterDate) {
//        this.cafeRegisterDate = cafeRegisterDate;
//    }
//
//    public String getCafeModifierName() {
//        return cafeModifierName;
//    }
//
//    public void setCafeModifierName(String cafeModifierName) {
//        this.cafeModifierName = cafeModifierName;
//    }
//
//    public String getCafeModifierDate() {
//        return cafeModifierDate;
//    }
//
//    public void setCafeModifierDate(String cafeModifierDate) {
//        this.cafeModifierDate = cafeModifierDate;
//    }

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
                ", beanUseByDate='" + beanUseByDate + '\'' +
                ", beanRegisterName='" + beanRegisterName + '\'' +
                ", beanRegisterDate='" + beanRegisterDate + '\'' +
                ", beanModifierName='" + beanModifierName + '\'' +
                ", beanModifierDate='" + beanModifierDate + '\'' +
                ", mixCompany='" + mixCompany + '\'' +
                ", mixUseByDate='" + mixUseByDate + '\'' +
                ", mixRegisterName='" + mixRegisterName + '\'' +
                ", mixRegisterDate='" + mixRegisterDate + '\'' +
                ", mixModifierName='" + mixModifierName + '\'' +
                ", mixModifierDate='" + mixModifierDate + '\'' +
//                ", cafeCompany='" + cafeCompany + '\'' +
//                ", cafeRegisterName='" + cafeRegisterName + '\'' +
//                ", cafeRegisterDate='" + cafeRegisterDate + '\'' +
//                ", cafeModifierName='" + cafeModifierName + '\'' +
//                ", cafeModifierDate='" + cafeModifierDate + '\'' +
                '}';
    }
}
