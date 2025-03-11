<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<body>
<main id="admin_page">
  <div class="admin_page_wrap">
    <div class="page_head">
      <a href="${ pageContext.request.contextPath }/admin/productList">
        <h1>제품 품절 수정</h1>
      </a>
    </div>
    <div class="search_form_wrap center">
      <div class="inline_wrap">
        <form class="search_form"
              action="${ pageContext.request.contextPath }/admin/productSoldOutUpdatePro"
              method="post"
              style="max-height: 35px"
        >
          <div style="justify-content: center">
            <label for="productCode" style="flex: none;">제품 번호</label>
            <input type="text" id="productCode" name="productCode" value="${ requestScope.productCode }">
            <label for="productSoldOut" style="flex: none;">품절</label>
            <select id="productSoldOut" name="productSoldOut">
              <option value="0">0 (품절 X)</option>
              <option value="1">1 (품절 O)</option>
            </select>
          </div>
        </form>
      </div>
    </div>
    <div class="btn_wrap center">
      <div class="btn">
        <a onclick="submit()">수정</a>
      </div>
    </div>
    <div class="list">
      <ul class="center">
        <c:choose>
          <c:when test="${ requestScope.memberTier != '9' }">
            <p>권한이 부족합니다.</p>
          </c:when>
          <c:when test="${ requestScope.memberTier == '9' && requestScope.productCount == 0 }">
            <p>제품을 찾을 수 없습니다.</p>
          </c:when>
          <c:when test="${ requestScope.memberTier == '9' }">
            <c:if test="${ requestScope.productSearchCount != 0 || requestScope.productCount != 0 }">
              <form
                action="${ pageContext.request.contextPath }/admin/productListPro"
                method="post"
                id="orderByForm"
              >
                <input id="columnName" type="hidden" name="columnName" value="${ requestScope.columnName }"/>
                <input id="orderBy" type="hidden" name="orderBy" value="${ requestScope.orderBy }"/>
              </form>
              <table class="list">
                <thead>
                <tr>
                  <th class="product_type" onclick="orderBy(this)">
                    <div class="asc">
                      <span>제품타입</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_code" onclick="orderBy(this)">
                    <div class="asc">
                      <span>제품번호</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_name" onclick="orderBy(this)">
                    <div class="asc">
                      <span>제품이름</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_unit" onclick="orderBy(this)">
                    <div class="asc">
                      <span>용량</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_price" onclick="orderBy(this)">
                    <div class="asc">
                      <span>금액</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_tier" onclick="orderBy(this)">
                    <div class="asc">
                      <span>등급</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_sold_out" onclick="orderBy(this)">
                    <div class="asc">
                      <span>품절</span>
                      <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_file">
                    <div class="asc">
                      <span>파일명</span>
                    </div>
                  </th>
                  <th class="product_register_name">
                    <div class="asc">
                      <span>등록자</span>
                    </div>
                  </th>
                  <th class="product_register_date">
                    <div class="asc">
                      <span>등록일</span>
                    </div>
                  </th>
                  <th class="product_modifier_name">
                    <div class="asc">
                      <span>수정자</span>
                    </div>
                  </th>
                  <th class="product_modifier_date">
                    <div class="asc">
                      <span>수정일</span>
                    </div>
                  </th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="p" items="${ list }" varStatus="status">
                  <tr>
                    <td>
                      <p>${ p.productType }</p>
                    </td>
                    <td>
                      <p>${ p.productCode }</p>
                    </td>
                    <td>
                      <p>${ p.productName }</p>
                    </td>
                    <td>
                      <p>${ p.productUnit }</p>
                    </td>
                    <td>
                      <p><fmt:formatNumber value="${ p.productPrice }" pattern="#,###" /> 원</p>
                    </td>
                    <td>
                      <p>${ p.productTier }</p>
                    </td>
                    <td>
                      <p>${ p.productSoldOut }</p>
                    </td>
                    <td>
                      <p>${ p.productFile }</p>
                    </td>
                    <td>
                      <p>${ p.productRegisterName }</p>
                    </td>
                    <td>
                      <p>${ p.productRegisterDate }</p>
                    </td>
                    <td>
                      <p>${ p.productModifierName }</p>
                    </td>
                    <td>
                      <p>${ p.productModifierDate }</p>
                    </td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </c:if>
            <c:if test="${ requestScope.productSearchCount == 0 }">
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
              href="${ pageContext.request.contextPath }/admin/productList?pageNum=${ pageNum - 3 }"
            >&laquo;</a
            >
          </c:when>
          <c:when test="${requestScope.searchText != null}">
            <a
              href="${ pageContext.request.contextPath }/admin/productListSearch?pageNum=${ pageNum - 3 }&&searchText=${ requestScope.searchText }"
            >&laquo;</a
            >
          </c:when>
        </c:choose>
      </c:if>
      <c:if test="${ productSearchCount != 0 }">
        <c:forEach var="p" begin="${ start }" end="${ end }">
          <c:choose>
            <c:when test="${requestScope.searchText == null}">
              <a
                href="${ pageContext.request.contextPath }/admin/productList?pageNum=${ p }"
              >${ p }</a
              >
            </c:when>
            <c:when test="${ requestScope.searchText != null }">
              <a
                href="${ pageContext.request.contextPath }/admin/productListSearch?pageNum=${ p }&&searchText=${ requestScope.searchText }"
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
              href="${ pageContext.request.contextPath }/admin/productList?pageNum=${ pageNum + 3 }"
            >&raquo;</a
            >
          </c:when>
          <c:when test="${ requestScope.searchText != null}">
            <a
              href="${ pageContext.request.contextPath }/admin/productListSearch?pageNum=${ pageNum + 3 }&&searchText=${ requestScope.searchText }"
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
      imgElement.src = "${ pageContext.request.contextPath }/image/down-arrow.png";
    } else if(orderByInputValue === "desc") {
      columnNameElement.className = "desc";
      imgElement.src = "${ pageContext.request.contextPath }/image/up-arrow.png";
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
      img.src = "${ pageContext.request.contextPath }/image/up-arrow.png";
    } else if(state === "expand") {
      form.style.maxHeight = "35px";
      object.className = "collapse";
      img.src = "${ pageContext.request.contextPath }/image/down-arrow.png";
    }
  }
  function submit() {
    const form = document.querySelector(".search_form");
    form.submit();
  }
</script>
</body>
