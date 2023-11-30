<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<body>
<main id="admin_page">
  <div class="admin_page_wrap">
    <div class="page_head">
      <a href="">
        <h1>회원 리스트</h1>
      </a>
    </div>
    <div class="excel_download">
      <a href="${ pageContext.request.contextPath }/admin/excelMemberDownload">멤버 엑셀 다운로드</a>
    </div>
    <div class="search_form_wrap center">
      <div class="inline_wrap">
        <form class="search_form"
              action="${ pageContext.request.contextPath }/admin/memberSearch"
              method="post"
              style="max-height: 70px;"
        >
          <div>
            <label for="memberCompanyName">업체명</label>
            <input type="text" id="memberCompanyName" name="memberCompanyName" value="${ requestScope.memberCompanyName }">
            <label for="memberFranCode">가맹점코드</label>
            <input type="text" id="memberFranCode" name="memberFranCode" value="${ requestScope.memberFranCode }">
          </div>
          <div>
            <label for="memberId">아이디</label>
            <input type="text" id="memberId" name="memberId" value="${ requestScope.memberId }">
            <label for="memberName">이름</label>
            <input type="text" id="memberName" name="memberName" value="${ requestScope.memberName }">
          </div>
          <div>
            <label for="memberTel">전화번호</label>
            <input type="text" id="memberTel" name="memberTel" value="${ requestScope.memberTel }">
            <label for="memberCompanyTel">회사번호</label>
            <input type="text" id="memberCompanyTel" name="memberCompanyTel" value="${ requestScope.memberCompanyTel }">
          </div>
          <div class="last">
            <label for="memberTier">등급</label>
            <input type="text" id="memberTier" name="memberTier" value="">
          </div>
        </form>
        <div class="collapse" onclick="expand(this)">
          <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
        </div>
      </div>
    </div>
    <div class="btn_wrap center">
      <div class="btn">
        <a onclick="submit()">검색</a>
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
                  <th class="member_tier" onclick="orderBy(this)">
                    <div class="asc">
                      <span>등급</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_fran_code" onclick="orderBy(this)">
                    <div class="asc">
                      <span>가맹점코드</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_id" onclick="orderBy(this)">
                    <div class="asc">
                      <span>아이디</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_name" onclick="orderBy(this)">
                    <div class="asc">
                      <span>이름</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_company_name" onclick="orderBy(this)">
                    <div class="asc">
                      <span>업체명</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_tel" onclick="orderBy(this)">
                    <div class="asc">
                      <span>전화번호</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_company_tel" onclick="orderBy(this)">
                    <div class="asc">
                      <span>회사번호</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_address" onclick="orderBy(this)">
                    <div class="asc">
                      <span>주소</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_delivery_address" onclick="orderBy(this)">
                    <div class="asc">
                      <span>배송지</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_file" onclick="orderBy(this)">
                    <div class="asc">
                      <span>파일</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_email" onclick="orderBy(this)">
                    <div class="asc">
                      <span>이메일</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_date" onclick="orderBy(this)">
                    <div class="asc">
                      <span>가입일</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_disable" onclick="orderBy(this)">
                    <div class="asc">
                      <span>비활성화</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
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
                    <td>
                      <p>${ m.memberDisable }</p>
                    </td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </c:if>
            <c:if test="${ requestScope.memberSearchCount == 0 }">
              <p>검색결과를 찾을 수 없습니다.</p>
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
      <c:if test="${ memberSearchCount != 0 }">
        <c:forEach var="p" begin="${ start }" end="${ end }">
          <c:choose>
            <c:when test="${requestScope.searchText == null}">
              <a
                href="${ pageContext.request.contextPath }/admin/memberList?pageNum=${ p }"
              >${ p }</a
              >
            </c:when>
            <c:when test="${requestScope.searchText != null}">
              <a
                href="${ pageContext.request.contextPath }/admin/memberListSearch?pageNum=${ p }&&searchText=${ requestScope.searchText }"
              >${ p }</a
              >
            </c:when>
          </c:choose>
        </c:forEach>
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
  window.onload = function() {
    const columnNameInput = document.getElementById("columnName");
    const orderByInput = document.getElementById("orderBy");

    const columnNameInputValue = columnNameInput.value;
    const orderByInputValue = orderByInput.value;

    const columnNameElement = document.querySelector("." + columnNameInputValue).firstElementChild;
    const imgElement = columnNameElement.lastElementChild;

    if(orderByInputValue === "asc") {
      columnNameElement.className = "asc";
      imgElement.src = "${ pageContext.request.contextPath }/view/image/down-arrow.png";
    } else if(orderByInputValue === "desc") {
      columnNameElement.className = "desc";
      imgElement.src = "${ pageContext.request.contextPath }/view/image/up-arrow.png";
    }
  }

  function orderBy(object) {
    const columnName = object.className;
    const currentOrderBy = object.firstElementChild.className;
    const orderBy = (currentOrderBy === "asc") ? "desc" : "asc";
    const form = document.getElementById("orderByForm");

    form.querySelector('input[name="columnName"]').value = columnName;
    form.querySelector('input[name="orderBy"]').value = orderBy;

    form.submit();
  }
</script>
<script>
  function expand(object) {
    const form = document.querySelector(".search_form");
    const img = object.firstElementChild;
    const state = object.className;

    if(state === "collapse") {
      form.style.maxHeight = "200px";
      object.className = "expand";
      img.src = "${ pageContext.request.contextPath }/view/image/up-arrow.png";
    } else if(state === "expand") {
      form.style.maxHeight = "70px";
      object.className = "collapse";
      img.src = "${ pageContext.request.contextPath }/view/image/down-arrow.png";
    }
  }
  function submit() {
    const form = document.querySelector(".search_form");
    form.submit();
  }
</script>
</body>
