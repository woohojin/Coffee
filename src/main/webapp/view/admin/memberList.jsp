<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<body>
<main id="admin_page">
  <div class="admin_page_wrap">
    <div class="page_head">
      <a href="${ pageContext.request.contextPath }/admin/productList">
        <h1>멤버 리스트</h1>
      </a>
    </div>
    <div class="search_form_wrap center">
      <form class="search_form"
            action="${ pageContext.request.contextPath }/admin/memberSearch"
            method="post"
      >
        <div>
          <label for="productCode">제품 코드</label>
          <input type="text" id="productCode" name="productCode" value="${requestScope.productCode}">
          <label for="productName">제품 이름</label>
          <input type="text" id="productName" name="productName" value="${requestScope.productName}">
        </div>
        <div>
          <label for="productCode">제품 타입</label>
          <input type="text" id="productType" name="productType" value="${requestScope.productType}">
          <label for="productName">금액</label>
          <input type="text" id="productPrice" name="productPrice" value="${requestScope.productPrice}">
        </div>
        <div>
          <label for="productCode">용량</label>
          <input type="text" id="productUnit" name="productUnit" value="${requestScope.productUnit}">
          <label for="productName">원산지</label>
          <input type="text" id="productCountry" name="productCountry" value="${requestScope.productCountry}">
        </div>
        <div>
          <label for="productCode">품종</label>
          <input type="text" id="productSpecies" name="productSpecies" value="${requestScope.productSpecies}">
          <label for="productName">제조사</label>
          <input type="text" id="productCompany" name="productCompany" value="${requestScope.productCompany}">
        </div>
        <div>
          <label for="productCode">등급</label>
          <input type="text" id="productTier" name="productTier" value="${requestScope.productTier}">
        </div>
        <div class="submit">
          <input type="submit" class="submit_btn" value="검색">
        </div>
      </form>
    </div>
    <div class="list">
      <ul class="center">
        <c:choose>
          <c:when test="${ requestScope.memberTier != '9' }">
            <p>게시물을 열람 할 권한이 부족합니다.</p>
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
                <input id="product" type="hidden" name="product" value="${requestScope.product}"/>
                <input id="orderBy" type="hidden" name="orderBy" value="${requestScope.orderBy}"/>
              </form>
              <table class="list">
                <thead>
                <tr>
                  <th class="product_type" onclick="orderBy(this)">
                    <div class="asc">
                      <span>등급</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_price" onclick="orderBy(this)">
                    <div class="asc">
                      <span>가맹점코드</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_code" onclick="orderBy(this)">
                    <div class="asc">
                      <span>아이디</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_name" onclick="orderBy(this)">
                    <div class="asc">
                      <span>이름</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_unit" onclick="orderBy(this)">
                    <div class="asc">
                      <span>업체명</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_unit" onclick="orderBy(this)">
                    <div class="asc">
                      <span>전화번호</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_unit" onclick="orderBy(this)">
                    <div class="asc">
                      <span>업체번호</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_country" onclick="orderBy(this)">
                    <div class="asc">
                      <span>주소</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_species" onclick="orderBy(this)">
                    <div class="asc">
                      <span>배송지</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_company" onclick="orderBy(this)">
                    <div class="asc">
                      <span>파일</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_tier" onclick="orderBy(this)">
                    <div class="asc">
                      <span>이메일</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_tier" onclick="orderBy(this)">
                    <div class="asc">
                      <span>가입일</span>
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
                      <a>${ m.memberFile }</a>
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
            <c:when test="${requestScope.searchText != null}">
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
    const productInput = document.getElementById("product");
    const orderByInput = document.getElementById("orderBy");

    const productInputValue = productInput.value;
    const orderByInputValue = orderByInput.value;

    const productElement = document.querySelector("." + productInputValue).firstElementChild;
    const imgElement = productElement.lastElementChild;

    if(orderByInputValue === "asc") {
      productElement.className = "asc";
      imgElement.src = "${ pageContext.request.contextPath }/view/image/down-arrow.png";
    } else if(orderByInputValue === "desc") {
      productElement.className = "desc";
      imgElement.src = "${ pageContext.request.contextPath }/view/image/up-arrow.png";
    }
  }

  function orderBy(object) {
    const columnName = object.className;
    const currentOrderBy = object.firstElementChild.className;
    const orderBy = (currentOrderBy === "asc") ? "desc" : "asc";
    const form = document.getElementById("orderByForm");

    form.querySelector('input[name="product"]').value = columnName;
    form.querySelector('input[name="orderBy"]').value = orderBy;

    form.submit();
  }
</script>
</body>
