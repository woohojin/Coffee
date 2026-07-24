package org.daCoffee.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MemberTier {
  UNAPPROVED(0),
  RENTAL(1),
  NON_RENTAL(2),
  CAFE_CUSTOMER(3),
  ADMIN(9);

  private final int code;

  MemberTier(int code) {
    this.code = code;
  }

  @JsonValue
  public int getCode() {
    return code;
  }

  public static MemberTier fromCode(int code) {
    for (MemberTier tier : values()) {
      if (tier.code == code) return tier;
    }
    throw new IllegalArgumentException("잘못된 memberTier 값: " + code);
  }
}
