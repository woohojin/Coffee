<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
"http://java.sun.com/jsp/jstl/core" %>
<html>
  <body>
    <main>
      <form
        action="${ pageContext.request.contextPath }/member/memberSignInPro"
        name="f"
        method="post"
      >
        <div class="product_form">
          <ul>
            <li>
              <label for="member_id">���̵�</label>
              <input
                name="memberId"
                id="member_id"
                class="memberId"
                type="text"
                required
              />
            </li>
            <li>
              <label for="member_password">��й�ȣ</label>
              <input
                name="memberPassword"
                id="member_password"
                class="memberPassword"
                type="password"
                required
              />
            </li>
            <li>
              <label for="auto_login">�ڵ� �α���</label>
              <input
                type="checkbox"
                id="auto_login"
                name="autoLogin"
                value="0"
              />
            </li>
          </ul>
        </div>
        <div class="submit">
          <input type="submit" value="�α���" class="submit_btn" />
        </div>
      </form>
    </main>
  </body>
</html>
