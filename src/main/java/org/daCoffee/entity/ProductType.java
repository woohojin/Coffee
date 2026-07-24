package org.daCoffee.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductType {
  BEAN(0),
  MIX(1),
  CAFE(2);

  private final int code;

  ProductType(int code) {
    this.code = code;
  }

  @JsonValue
  public int getCode() {
    return code;
  }

  public static ProductType fromCode(int code) {
    for (ProductType type : values()) {
      if (type.code == code) return type;
    }
    throw new IllegalArgumentException("잘못된 productType 값: " + code);
  }
}
