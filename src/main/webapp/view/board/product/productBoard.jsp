<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
  <body>
    <main>
      <div class="product_wrap">
        <h1>블렌드 커피</h1>
        <p>${ productCount }</p>
        <div class="product">
          <ul>
            <c:choose>
              <c:when test="${requestScope.memberTier == '0'}">
                <p>게시물을 열람 할 권한이 부족합니다.</p>
                <p>회원가입을 하시거나 회원가입 이후 문제가 발생했다면</p>
                <p>xxx로 연락주십시오.</p>
              </c:when>
              <c:when test="${requestScope.memberTier == '1' || requestScope.memberTier == '2'}">
                <c:forEach var="p" items="${ list }" varStatus="status">
                  <li>
                    <a href="${ pageContext.request.contextPath }/board/product?productCode=${ p.productCode }">
                      <img src="${ pageContext.request.contextPath }/view/image/1.jpg" alt="" />
                    </a>
                    <div>
                      <p>${ p.productName }</p>
                      <p>${ p.productPrice }원</p>
                    </div>
                  </li>
                </c:forEach>
              </c:when>
            </c:choose>
          </ul>
        </div>
      </div>

      <div class="">
          <div class="">
            <a <c:if test="${ start >= 3}" >href="${ pageContext.request.contextPath }/board/product?pageNum=${start-3}"</c:if>>&laquo;</a>
            <c:forEach var="p" begin="${ start }" end="${ end }">
            <a class="active" href="${ pageContext.request.contextPath }/board/product?pageNum=${p}">${ p }</a>
            </c:forEach>
            <a <c:if test="${ end < maxPage }">href="${ pageContext.request.contextPath }/board/product?pageNum=${end + 3}"</c:if>>&raquo;</a>
          </div>
      </div>

    </main>
  </body>
</html>
