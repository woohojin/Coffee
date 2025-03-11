<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<body>
<main id="admin_page">
  <div class="admin_page_wrap">
    <div class="page_head">
      <a href="${ pageContext.request.contextPath }/admin/memberDisable">
        <h1>회원 비활성화 수정</h1>
      </a>
    </div>
    <div class="search_form_wrap center">
      <div class="inline_wrap">
        <form class="search_form"
              action="${ pageContext.request.contextPath }/admin/memberDisableUpdatePro"
              method="post"
              style="max-height: 70px;"
        >
          <div style="justify-content: center;">
            <label for="memberId" style="flex: none;">아이디</label>
            <input type="text" id="memberId" name="memberId" value="">
          </div>
        </form>
      </div>
    </div>
    <div class="btn_wrap center">
      <div class="btn">
        <a onclick="submit()">업데이트</a>
      </div>
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
              <table class="list">
                <thead>
                <tr>
                  <th class="member_tier" onclick="orderBy(this)">
                    <div class="asc">
                      <span>등급</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_fran_code" onclick="orderBy(this)">
                    <div class="asc">
                      <span>가맹점코드</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_id" onclick="orderBy(this)">
                    <div class="asc">
                      <span>아이디</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_name" onclick="orderBy(this)">
                    <div class="asc">
                      <span>이름</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_company_name" onclick="orderBy(this)">
                    <div class="asc">
                      <span>업체명</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_tel" onclick="orderBy(this)">
                    <div class="asc">
                      <span>전화번호</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_company_tel" onclick="orderBy(this)">
                    <div class="asc">
                      <span>회사번호</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_address" onclick="orderBy(this)">
                    <div class="asc">
                      <span>주소</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_delivery_address" onclick="orderBy(this)">
                    <div class="asc">
                      <span>배송지</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_file" onclick="orderBy(this)">
                    <div class="asc">
                      <span>파일</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_email" onclick="orderBy(this)">
                    <div class="asc">
                      <span>이메일</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_date" onclick="orderBy(this)">
                    <div class="asc">
                      <span>가입일</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="m" items="${ list }" varStatus="status">
                  <tr>
                    <td>
                      <p>${ m.memberTier }</p>
                    </td>
                    <td>
                      <p>${ m.memberFranCode }</p>
                    </td>
                    <td>
                      <p>${ m.memberId }</p>
                    </td>
                    <td>
                      <p>${ m.memberName }</p>
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
                    <td>
                      <p>${ m.memberAddress }</p>
                      <p>${ m.memberDetailAddress }</p>
                    </td>
                    <td>
                      <p>${ m.memberDeliveryAddress }</p>
                      <p>${ m.memberDetailDeliveryAddress }</p>
                    </td>
                    <td>
                      <a style="color: var(--mainColor);" onclick="fileDownload('${ m.memberFile }','${ m.memberId }')">${ m.memberFile }</a>
                    </td>
                    <td>
                      <p>${ m.memberEmail }</p>
                    </td>
                    <td>
                      <p>${ m.memberDate }</p>
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
</main>
<script>
  function submit() {
    const form = document.querySelector(".search_form");
    if(confirm("멤버 비활성화 상태가 변경됩니다. 진행하시겠습니까?") === true) {
      form.submit();
      return true;
    } else {
      return false;
    }
  }
</script>
</body>
