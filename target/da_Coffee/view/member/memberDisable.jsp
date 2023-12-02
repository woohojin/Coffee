<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>

<body>
<main>
  <div class="member_signin_form_wrap">
    <form
      action="${ pageContext.request.contextPath }/member/memberDisablePro"
      name="f"
      method="post"
      class="member_signin_form"
      onsubmit="checkDisable()"
    >
      <div class="page_head">
        <a href="">
          <h1>회원탈퇴</h1>
        </a>
      </div>
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
              value="${ sessionScope.memberId }"
              readonly
              style="border: 1px solid var(--grayLine);"
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
      <div class="signin">
        <input type="submit" value="회원탈퇴" class="submit_btn" style="margin-top: 15px;" />
      </div>
    </form>
  </div>
</main>
<script>
  function checkDisable() {
    if(confirm("정말 회원을 탈퇴하시겠습니까?") === true) {
      return true;
    } else {
      return false;
    }
  }
</script>
</body>
