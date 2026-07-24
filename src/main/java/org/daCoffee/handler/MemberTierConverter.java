package org.daCoffee.handler;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.daCoffee.entity.MemberTier;

@Converter
public class MemberTierConverter implements AttributeConverter<MemberTier, Integer> {

  @Override
  public Integer convertToDatabaseColumn(MemberTier attribute) {
    return attribute == null ? null : attribute.getCode();
  }

  @Override
  public MemberTier convertToEntityAttribute(Integer dbData) {
    return dbData == null ? null : MemberTier.fromCode(dbData);
  }
}
