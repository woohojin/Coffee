<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<body>
<main id="admin_page">
  <div class="admin_page_wrap">
    <div class="page_head">
      <a href="${ pageContext.request.contextPath }/admin/productDelete">
        <h1>제품 삭제</h1>
      </a>
    </div>
    <div class="search_form_wrap center">
      <div class="inline_wrap">
        <form class="search_form"
              action="${ pageContext.request.contextPath }/admin/productDeletePro"
              method="post"
              style="max-height: 35px"
        >
          <div style="justify-content: center;">
            <label for="productCode" style="flex: none;">제품 번호</label>
            <input type="text" id="productCode" name="productCode" value="${ requestScope.productCode }">
          </div>
        </form>
      </div>
    </div>
    <div class="btn_wrap center">
      <div class="btn">
        <a onclick="submit()">삭제</a>
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
              href="${ pageContext.request.contextPath }/admin/productDelete?pageNum=${ pageNum - 3 }"
            >&laquo;</a
            >
          </c:when>
          <c:when test="${requestScope.searchText != null}">
            <a
              href="${ pageContext.request.contextPath }/admin/productDeleteSearch?pageNum=${ pageNum - 3 }&&searchText=${ requestScope.searchText }"
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
                href="${ pageContext.request.contextPath }/admin/productDelete?pageNum=${ p }"
              >${ p }</a
              >
            </c:when>
            <c:when test="${ requestScope.searchText != null }">
              <a
                href="${ pageContext.request.contextPath }/admin/productDeleteSearch?pageNum=${ p }&&searchText=${ requestScope.searchText }"
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
              href="${ pageContext.request.contextPath }/admin/productDelete?pageNum=${ pageNum + 3 }"
            >&raquo;</a
            >
          </c:when>
          <c:when test="${ requestScope.searchText != null}">
            <a
              href="${ pageContext.request.contextPath }/admin/productDeleteSearch?pageNum=${ pageNum + 3 }&&searchText=${ requestScope.searchText }"
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
    if(confirm("제품이 삭제됩니다. 진행하시겠습니까?") === true) {
      form.submit();
      return true;
    } else {
      return false;
    }
  }
</script>
</body>
