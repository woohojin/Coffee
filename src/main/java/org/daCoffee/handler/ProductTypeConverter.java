package org.daCoffee.handler;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.daCoffee.entity.ProductType;

@Converter
public class ProductTypeConverter implements AttributeConverter<ProductType, Integer> {

  @Override
  public Integer convertToDatabaseColumn(ProductType attribute) {
    return attribute == null ? null : attribute.getCode();
  }

  @Override
  public ProductType convertToEntityAttribute(Integer dbData) {
    return dbData == null ? null : ProductType.fromCode(dbData);
  }
}
