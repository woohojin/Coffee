<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
        "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<body>
<main id="admin_page">
  <div class="admin_page_wrap">
    <div class="page_head">
      <a href="${ pageContext.request.contextPath }/admin/memberWithdrawalList">
        <h1>탈퇴 회원 리스트</h1>
      </a>
    </div>
    <div class="list">
      <ul class="center">
        <c:choose>
          <c:when test="${ requestScope.memberTier != '9' }">
            <p>권한이 부족합니다.</p>
          </c:when>
          <c:when test="${ requestScope.memberTier == '9' && requestScope.memberCount == 0 }">
            <p>회원을 찾을 수 없습니다.</p>
          </c:when>
          <c:when test="${ requestScope.memberTier == '9' }">
            <c:if test="${ requestScope.memberSearchCount != 0 || requestScope.memberCount != 0 }">
              <form
                      action="${ pageContext.request.contextPath }/admin/memberListPro"
                      method="post"
                      id="orderByForm"
              >
                <input id="columnName" type="hidden" name="columnName" value="${ requestScope.columnName }"/>
                <input id="orderBy" type="hidden" name="orderBy" value="${ requestScope.orderBy }"/>
              </form>
              <table class="list">
                <thead>
                <tr>
                  <th class="member_id">
                    <div class="asc">
                      <span>아이디</span>
                    </div>
                  </th>
                  <th class="member_company_name">
                    <div class="asc">
                      <span>업체명</span>
                    </div>
                  </th>
                  <th class="member_tel">
                    <div class="asc">
                      <span>전화번호</span>
                    </div>
                  </th>
                  <th class="member_company_tel">
                    <div class="asc">
                      <span>회사번호</span>
                    </div>
                  </th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="m" items="${ list }" varStatus="status">
                  <tr>
                    <td>
                      <p>${ m.memberId }</p>
                    </td>
                    <td>
                      <p>${ m.memberCompanyName }</p>
                    </td>
                    <td>
                      <p>${ m.memberTel }</p>
                    </td>
                    <td>
                      <p>${ m.memberCompanyTel }</p>
                    </td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </c:if>
          </c:when>
        </c:choose>
      </ul>
    </div>
  </div>

  <div class="pagination_wrap center">
    <div class="pagination">
      <c:if test="${ pageNum >= 3}">
        <c:choose>
          <c:when test="${ requestScope.searchText == null }">
            <a
                    href="${ pageContext.request.contextPath }/admin/memberList?pageNum=${ pageNum - 3 }"
            >&laquo;</a
            >
          </c:when>
          <c:when test="${requestScope.searchText != null}">
            <a
                    href="${ pageContext.request.contextPath }/admin/memberListSearch?pageNum=${ pageNum - 3 }&&searchText=${ requestScope.searchText }"
            >&laquo;</a
            >
          </c:when>
        </c:choose>
      </c:if>
      <c:if test="${ pageNum < end - 3 }">
        <c:choose>
          <c:when test="${ requestScope.searchText == null}">
            <a
                    href="${ pageContext.request.contextPath }/admin/memberList?pageNum=${ pageNum + 3 }"
            >&raquo;</a
            >
          </c:when>
          <c:when test="${ requestScope.searchText != null}">
            <a
                href="${ pageContext.request.contextPath }/admin/memberListSearch?pageNum=${ pageNum + 3 }&&searchText=${ requestScope.searchText }"
            >&raquo;</a
            >
          </c:when>
        </c:choose>
      </c:if>
    </div>
  </div>
</main>
<script>
  function submit() {
    const form = document.querySelector(".search_form");
    form.submit();
  }
</script>
</body>
