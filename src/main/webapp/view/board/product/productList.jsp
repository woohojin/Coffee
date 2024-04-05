<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<body>
<main id="product_page">
  <div class="product_wrap">
    <div class="page_head">
      <a>
        <c:if test="${ requestScope.pageType == 'bean' }">
          <h1>원두 커피</h1>
        </c:if>
        <c:if test="${ requestScope.pageType == 'mix' }">
          <h1>믹스 커피</h1>
        </c:if>
        <c:if test="${ requestScope.pageType == 'cafe' }">
          <h1>카페 용품</h1>
        </c:if>
        <c:if test="${ requestScope.pageType == 'machine' }">
          <h1>임대 머신</h1>
        </c:if>
      </a>
    </div>
    <div class="product">
      <ul class="center">
        <c:choose>
          <c:when test="${ requestScope.memberTier == '0' }">
            <div class="denied-text">
              <p>로그인을 진행하시거나</p>
              <br/>
              <p>초기 회원가입 진행 후에 1566-0904 으로 연락 부탁드립니다.</p>
            </div>
          </c:when>
          <c:when test="${ requestScope.memberTier == '0' && requestScope.productCount == 0 }">
            <p>제품을 찾을 수 없습니다.</p>
          </c:when>
          <c:when test="${ requestScope.memberTier != 0 }">
            <c:if test="${ requestScope.productSearchCount > 0 || requestScope.productCount > 0 }">
              <c:forEach var="p" items="${ list }">
                <li>
                  <c:if test="${ p.productSoldOut == 1 }">
                    <div class="sold_out">Sold Out</div>
                  </c:if>

                    <c:choose>
                      <c:when test="${ p.productType == 0 }">
                        <a href="${ pageContext.request.contextPath }/board/beanDetail?productCode=${ p.productCode }">
                          <img src="${ pageContext.request.contextPath }/view/files/bean/${ p.productCode }/${ p.productFile }" alt="" />
                        </a>
                        <div>
                          <a href="${ pageContext.request.contextPath }/board/beanDetail?productCode=${ p.productCode }">${ p.productName }</a>
                          <p><fmt:formatNumber value="${ p.productPrice }" pattern="#,###" /> 원</p>
                        </div>
                      </c:when>

                      <c:when test="${ p.productType == 1 }">
                        <a href="${ pageContext.request.contextPath }/board/mixDetail?productCode=${ p.productCode }">
                          <img src="${ pageContext.request.contextPath }/view/files/mix/${ p.productCode }/${ p.productFile }" alt="" />
                        </a>
                        <div>
                          <a href="${ pageContext.request.contextPath }/board/mixDetail?productCode=${ p.productCode }">${ p.productName }</a>
                          <p><fmt:formatNumber value="${ p.productPrice }" pattern="#,###" /> 원</p>
                        </div>
                      </c:when>

                      <c:when test="${ p.productType == 2 }">
                        <a href="${ pageContext.request.contextPath }/board/cafeDetail?productCode=${ p.productCode }">
                          <img src="${ pageContext.request.contextPath }/view/files/cafe/${ p.productCode }/${ p.productFile }" alt="" />
                        </a>
                        <div>
                          <a href="${ pageContext.request.contextPath }/board/cafeDetail?productCode=${ p.productCode }">${ p.productName }</a>
                          <p><fmt:formatNumber value="${ p.productPrice }" pattern="#,###" /> 원</p>
                        </div>
                      </c:when>
                    </c:choose>
                </li>
              </c:forEach>
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
          <c:when test="${requestScope.searchText == null}">
            <a
              href="${ pageContext.request.contextPath }/board/product?pageNum=${pageNum - 3}"
            >&laquo;</a
            >
          </c:when>
          <c:when test="${requestScope.searchText != null}">
            <a
              href="${ pageContext.request.contextPath }/board/productSearch?pageNum=${pageNum - 3}&&searchText=${requestScope.searchText}"
            >&laquo;</a
            >
          </c:when>
        </c:choose>
      </c:if>
      <c:if test="${ productSearchCount > 0 }">
        <c:forEach var="p" begin="${ start }" end="${ end }">
          <c:choose>
            <c:when test="${requestScope.searchText == null}">
              <a
                href="${ pageContext.request.contextPath }/board/product?pageNum=${p}"
              >${p}</a
              >
            </c:when>
            <c:when test="${requestScope.searchText != null}">
              <a
                href="${ pageContext.request.contextPath }/board/productSearch?pageNum=${p}&&searchText=${requestScope.searchText}"
              >${p}</a
              >
            </c:when>
          </c:choose>
        </c:forEach>
      </c:if>

      <c:if test="${ pageNum < end - 3 }">
        <c:choose>
          <c:when test="${requestScope.searchText == null}">
            <a
              href="${ pageContext.request.contextPath }/board/product?pageNum=${pageNum + 3}"
            >&raquo;</a
            >
          </c:when>
          <c:when test="${requestScope.searchText != null}">
            <a
              href="${ pageContext.request.contextPath }/board/productSearch?pageNum=${pageNum + 3}&&searchText=${requestScope.searchText}"
            >&raquo;</a
            >
          </c:when>
        </c:choose>
      </c:if>
    </div>
  </div>
</main>
</body>

