<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<body>
<main id="admin_page">
  <div class="admin_page_wrap">
    <div class="page_head">
      <a href="">
        <h1>제품 리스트</h1>
      </a>
    </div>
    <div class="excel_download">
      <a href="${ pageContext.request.contextPath }/admin/excelProductDownload">제품 엑셀 다운로드</a>
    </div>
    <div class="search_form_wrap center">
      <div class="inline_wrap">
        <form class="search_form"
              action="${ pageContext.request.contextPath }/admin/productSearch"
              method="post"
              style="max-height: 35px"
        >
          <div>
            <label for="productCode">제품 코드</label>
            <input type="text" id="productCode" name="productCode" value="${ requestScope.productCode }">
            <label for="productName">제품 이름</label>
            <input type="text" id="productName" name="productName" value="${ requestScope.productName }">
          </div>
          <div>
            <label for="productType">제품 타입</label>
            <input type="text" id="productType" name="productType" value="${ requestScope.productType }">
            <label for="productPrice">금액</label>
            <input type="text" id="productPrice" name="productPrice" value="${ requestScope.productPrice }">
          </div>
          <div>
            <label for="productUnit">용량</label>
            <input type="text" id="productUnit" name="productUnit" value="${ requestScope.productUnit }">
            <label for="productCountry">원산지</label>
            <input type="text" id="productCountry" name="productCountry" value="${ requestScope.productCountry }">
          </div>
          <div>
            <label for="productSpecies">품종</label>
            <input type="text" id="productSpecies" name="productSpecies" value="${ requestScope.productSpecies }">
            <label for="productCompany">제조사</label>
            <input type="text" id="productCompany" name="productCompany" value="${ requestScope.productCompany }">
          </div>
          <div class="last">
            <label for="productTier">등급</label>
            <input type="text" id="productTier" name="productTier" value="${ requestScope.productTier }">
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
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_code" onclick="orderBy(this)">
                    <div class="asc">
                      <span>제품번호</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_name" onclick="orderBy(this)">
                    <div class="asc">
                      <span>제품이름</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_unit" onclick="orderBy(this)">
                    <div class="asc">
                      <span>용량</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_price" onclick="orderBy(this)">
                    <div class="asc">
                      <span>금액</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_country" onclick="orderBy(this)">
                    <div class="asc">
                      <span>원산지</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_species" onclick="orderBy(this)">
                    <div class="asc">
                      <span>품종</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_company" onclick="orderBy(this)">
                    <div class="asc">
                      <span>제조사</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_tier" onclick="orderBy(this)">
                    <div class="asc">
                      <span>등급</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
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
                      <p>${ p.productCountry }</p>
                    </td>
                    <td>
                      <p>${ p.productSpecies }</p>
                    </td>
                    <td>
                      <p>${ p.productCompany }</p>
                    </td>
                    <td>
                      <p>${ p.productTier }</p>
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
      form.style.maxHeight = "35px";
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
