package org.daCoffee.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode // 복합키는 객체 주소값으로 비교해서 hashCode와 값 비교가 필요
public class CartId implements Serializable {
  private String memberId;
  private String productCode;
}
