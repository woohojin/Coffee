package org.daCoffee.exception;

public class NotFoundException extends BusinessException {
  public NotFoundException(String message) {
    super(message, 404);
  }

  public NotFoundException() {
    super("요청한 리소스를 찾을 수 없습니다.", 404);
  }
}