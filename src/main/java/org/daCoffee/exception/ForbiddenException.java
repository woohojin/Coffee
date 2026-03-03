package org.daCoffee.exception;

public class ForbiddenException extends BusinessException {
  public ForbiddenException(String message) {
    super(message, 403);
  }

  public ForbiddenException() {
    super("권한이 부족합니다.", 403);
  }
}