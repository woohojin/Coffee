<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>

<body>
<main>
  <div class="member_signin_form_wrap">
    <c:if test="${ isFind == 0 }">
      <form
        action="${ pageContext.request.contextPath }/member/memberFindAccountPro"
        method="post"
        class="member_signin_form"
      >
        <div>
          <input id="find_id" name="findType" onclick="toggleFindType(this)" type="radio" value="id" ${ findType == "id" ? "checked" : "" }>
          <label for="find_id">아이디 찾기</label>
          <input id="find_password" name="findType" onclick="toggleFindType(this)" type="radio" value="password" ${ findType == "password" ? "checked" : "" }>
          <label for="find_password">비밀번호 찾기</label>
        </div>
        <table>
          <tbody>
          <c:if test="${ findType == 'id'}">
            <tr>
              <th>
                <label for="member_name">이름</label>
              </th>
              <td>
                <input
                  name="memberName"
                  id="member_name"
                  class="memberName"
                  type="text"
                  spellcheck="false"
                  required
                />
              </td>
            </tr>
            <tr>
              <th>
                <label for="member_email">이메일</label>
              </th>
              <td>
                <input
                  name="memberEmail"
                  id="member_email"
                  class="memberEmail"
                  type="text"
                  spellcheck="false"
                  required
                />
              </td>
            </tr>
          </c:if>
          <c:if test="${ findType == 'password'}">
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
                <label for="member_email">이메일</label>
              </th>
              <td>
                <input
                  name="memberEmail"
                  id="member_email"
                  class="memberEmail"
                  type="text"
                  spellcheck="false"
                  required
                />
              </td>
            </tr>
          </c:if>
          </tbody>
        </table>
        <div class="signin" style="justify-content: flex-end; margin-top: 15px;">
          <input type="submit" value="찾기" class="submit_btn" />
        </div>
      </form>
    </c:if>
  </div>
  <c:if test="${ isFind == 1 }">
    <div style="display: flex; flex-direction: column; align-items: center">
      <c:forEach var="m" items="${ list }">
        <p>회원님의 아이디는 ${ m.memberId } 입니다.</p>
      </c:forEach>
      <div class="btn" style="width: 100px; height: 45px; margin-top: 25px;">
        <a href="${ pageContext.request.contextPath }/member/memberSignIn">로그인창으로</a>
      </div>
    </div>
  </c:if>
</main>
<script>
  function toggleFindType(object) {
    window.location.href = "./memberFindAccount?findType=" + object.value;
  }
</script>
</body>
