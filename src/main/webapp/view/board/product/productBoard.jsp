<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<body>
<main id="product_page">
  <div class="product_wrap">
    <div class="page_head">
      <h1>블렌드 커피</h1>
    </div>
    <div class="product">
      <ul class="center">
        <c:choose>
          <c:when test="${requestScope.memberTier == '0'}">
            <p>게시물을 열람 할 권한이 부족합니다.</p>
            <p>회원가입을 하시거나 회원가입 이후 문제가 발생했다면</p>
            <p>xxx로 연락주십시오.</p>
          </c:when>
          <c:when test="${requestScope.memberTier != 0}">
            <c:if test="${requestScope.productSearchCount != 0}">
              <c:forEach var="p" items="${ list }">
                <li>
                  <a
                    href="${ pageContext.request.contextPath }/board/productDetail?productCode=${ p.productCode }"
                  >
                    <img
                      src="${ pageContext.request.contextPath }/view/board/files/${ p.productFile }"
                      alt=""
                    />
                  </a>
                  <div>
                    <a onclick="return fileSystem('${ p.productFile }', '${ p.productName }')">${ p.productName }</a>
                    <p><fmt:formatNumber value="${ p.productPrice }" pattern="#,###" /> 원</p>
                  </div>
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
      <c:if test="${ productSearchCount != 0 }">
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

