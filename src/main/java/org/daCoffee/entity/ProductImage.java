package org.daCoffee.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "product_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "file_id")
  private Integer fileId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_code", nullable = false)
  private Product product;

  @Column(name = "file_name", nullable = false, length = 50)
  private String fileName;

  @Column(name = "file_register_name", nullable = false, length = 5)
  private String fileRegisterName;

  @Column(name = "file_register_date", nullable = false)
  private LocalDate fileRegisterDate;

  @Column(name = "file_modifier_name", length = 5)
  private String fileModifierName;

  @Column(name = "file_modifier_date")
  private LocalDate fileModifierDate;
  
  // 수정자 업데이트
  public void updateModifier(String fileName, String modifierName) {
    this.fileName = fileName;
    this.fileModifierName = modifierName;
    this.fileModifierDate = LocalDate.now();
  }
}