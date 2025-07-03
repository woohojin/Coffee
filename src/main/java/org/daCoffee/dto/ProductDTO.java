package org.daCoffee.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
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
}
