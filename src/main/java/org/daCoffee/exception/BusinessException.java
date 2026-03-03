package org.daCoffee.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
  private final int statusCode;
  private final String redirectUrl;

  public BusinessException(String message) {
    super(message);
    this.statusCode = 400;
    this.redirectUrl = null;
  }

  public BusinessException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
    this.redirectUrl = null;
  }

  public BusinessException(String message, int statusCode, String redirectUrl) {
    super(message);
    this.statusCode = statusCode;
    this.redirectUrl = redirectUrl;
  }
}