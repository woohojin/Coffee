<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
</head>
<body>
  <div>
    <div class="error-box">
      <p><strong>상태 코드:</strong> <%= request.getAttribute("status") != null ? request.getAttribute("status") : "N/A" %></p>
      <p><strong>에러:</strong> <%= request.getAttribute("error") != null ? request.getAttribute("error") : "Unknown" %></p>
      <p><strong>메시지:</strong> <%= request.getAttribute("message") != null ? request.getAttribute("message") : "알 수 없는 에러" %></p>
      <p><strong>예외:</strong> <%= request.getAttribute("exception") != null ? request.getAttribute("exception") : "없음" %></p>
      <p><strong>CSRF 토큰:</strong> <%= request.getAttribute("csrfToken") != null ? request.getAttribute("csrfToken") : "전송되지 않음" %></p>
      <p><strong>요청 URL:</strong> <%= request.getRequestURI() %></p>
      <p><strong>요청 메서드:</strong> <%= request.getMethod() %></p>
    </div>

    <p>문제가 지속되면 관리자에게 문의하세요.</p>
  </div>
</body>
</html>
