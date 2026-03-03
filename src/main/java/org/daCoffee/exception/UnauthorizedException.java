package org.daCoffee.exception;

public class UnauthorizedException extends BusinessException {
  public UnauthorizedException(String message) {
    super(message, 401, "/member/memberSignIn");
  }

  public UnauthorizedException() {
    super("로그인이 필요합니다.", 401, "/member/memberSignIn");
  }
}