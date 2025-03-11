<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<body>
<div class="wrapper w-100">
  <div class="flex-column align-center w-100 max-w-540">
    <img src="https://static.toss.im/lotties/error-spot-no-loop-space-apng.png" width="160" height="160" />
    <h2 class="title">결제를 실패했어요</h2>
    <div class="response-section w-100">
      <div class="flex justify-between">
        <span class="response-label">code</span>
        <span id="error-code" class="response-text">${ requestScope.errorCode }</span>
      </div>
      <div class="flex justify-between">
        <span class="response-label">message</span>
        <span id="error-message" class="response-text">${ requestScope.errorMessage }</span>
      </div>
    </div>
    <div class="button-group">
      <a class="payments_btn primary" href="${ pageContext.request.contextPath }/member/memberCart">장바구니로 이동하기</a>
      <a class="payments_btn primary" href="${ pageContext.request.contextPath }/board/main">메인화면으로 이동하기</a>
    </div>
  </div>
</div>
</body>

<script>
    const urlParams = new URLSearchParams(window.location.search);
    const errorCode = urlParams.get("code");
    const errorMessage = urlParams.get("message");

    const errorCodeElement = document.getElementById("error-code");
    const errorMessageElement = document.getElementById("error-message");

    if(errorCode != null && errorMessage != null) {
        errorCodeElement.textContent = errorCode;
        errorMessageElement.textContent = errorMessage;
    }
</script>