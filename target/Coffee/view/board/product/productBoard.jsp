<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
  <body>
    <main>
      <div class="product_wrap">
        <h1>블렌드 커피</h1>
        <div class="product">
          <ul>
            <c:if test="${ productCount == 0 }">
                <h2 class="center">아직 게시물이 작성되지 않았습니다.</h2>
            </c:if>
            <c:if test="${ productCount > 0 }">
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
            </c:if>
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
