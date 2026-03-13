package org.daCoffee.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDTO {
    private String productCode;
    private String existProductCode; // admin productUpdate
    private int productType;
    private String productName;
    private int productPrice;
    private String productUnit;
    private int productTier;
    private String productFile;
    private int productSoldOut;
    private String productRegisterName;
    private String productRegisterDate;
    private String productModifierName;
    private String productModifierDate;

    //bean

    private String beanCountry;
    private String beanSpecies;
    private String beanCompany;
    private String beanUseByDate;
    private String beanRegisterName;
    private String beanRegisterDate;
    private String beanModifierName;
    private String beanModifierDate;

    // mix

    private String mixCompany;
    private String mixUseByDate;
    private String mixRegisterName;
    private String mixRegisterDate;
    private String mixModifierName;
    private String mixModifierDate;
}
