package org.daCoffee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {
  private boolean success;
  private String message;
  private T data;
  private String redirectUrl;
  private int statusCode;

  public static <T> ApiResponseDTO<T> success(T data) {
    return ApiResponseDTO.<T>builder()
      .success(true)
      .data(data)
      .statusCode(200)
      .build();
  }

  public static <T> ApiResponseDTO<T> success(String message, T data) {
    return ApiResponseDTO.<T>builder()
      .success(true)
      .message(message)
      .data(data)
      .statusCode(200)
      .build();
  }

  public static <T> ApiResponseDTO<T> error(String message) {
    return ApiResponseDTO.<T>builder()
      .success(false)
      .message(message)
      .statusCode(400)
      .build();
  }

  public static <T> ApiResponseDTO<T> error(String message, int statusCode) {
    return ApiResponseDTO.<T>builder()
      .success(false)
      .message(message)
      .statusCode(statusCode)
      .build();
  }

  public static <T> ApiResponseDTO<T> error(String message, String redirectUrl, int statusCode) {
    return ApiResponseDTO.<T>builder()
      .success(false)
      .message(message)
      .redirectUrl(redirectUrl)
      .statusCode(statusCode)
      .build();
  }
}