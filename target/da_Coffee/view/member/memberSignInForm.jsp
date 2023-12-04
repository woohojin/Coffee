<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>

<body>
<main>
    <div class="member_signin_form_wrap">
        <form
          action="${ pageContext.request.contextPath }/member/memberSignInPro"
          method="post"
          class="member_signin_form"
        >
            <table>
                <tbody>
                    <tr>
                        <th>
                            <label for="member_id">아이디</label>
                        </th>
                        <td>
                            <input
                              name="memberId"
                              id="member_id"
                              class="memberId"
                              type="text"
                              spellcheck="false"
                              required
                            />
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="member_password">비밀번호</label>
                        </th>
                        <td>
                            <input
                              name="memberPassword"
                              id="member_password"
                              class="memberPassword"
                              type="password"
                              spellcheck="false"
                              required
                            />
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="autologin_checkbox">
                <label for="auto_login">자동 로그인</label>
                <input
                  type="checkbox"
                  id="auto_login"
                  name="autoLogin"
                  value="0"
                />
            </div>
            <div class="signin">
                <a href="${ pageContext.request.contextPath }/member/memberFindAccount" target="_blank">아이디 / 비밀번호 찾기</a>
                <input type="submit" value="로그인" class="submit_btn" />
            </div>
        </form>
    </div>
</main>
</body>
