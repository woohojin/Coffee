<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<body>
<main id="admin_page">
  <div class="admin_page_wrap">
    <div class="page_head">
      <h1>제품 리스트</h1>
    </div>
    <div class="datepicker_form_wrap">
      <form
        action="${ pageContext.request.contextPath }/member/memberHistoryPro"
        class="datepicker_form center"
        method="post"
      >
        <input type="text" name="startDate" id="datepickerStart" class="datepicker" value="${ requestScope.startDate }" />
        <span>&nbsp~&nbsp</span>
        <input type="text" name="endDate" id="datepickerEnd" class="datepicker" value="${ requestScope.endDate }" />
        <input type="submit" value="조회" class="submit_btn" />
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
              <table class="list">
                <thead>
                <tr>
                  <th>제품타입</th>
                  <th>제품코드</th>
                  <th>제품이름</th>
                  <th>용량</th>
                  <th>금액</th>
                  <th>원산지</th>
                  <th>품종</th>
                  <th>제조사</th>
                  <th>등급</th>
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
<script src="${ pageContext.request.contextPath }/view/js/datepicker.js"></script>
</body>
