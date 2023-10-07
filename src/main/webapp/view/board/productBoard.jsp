<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
  <body>
    <main class="board_main">
      <div class="product_wrap">
        <h1>블렌드 커피</h1>
        <div class="product">
          <ul>
            <li>
              <a href="">
                <img src="${ pageContext.request.contextPath }/view/image/1.jpg" alt="" />
              </a>
              <div>
                <p>케냐AA</p>
                <p>10500원</p>
              </div>
            </li>
            <li>
              <a href="">
                <img src="${ pageContext.request.contextPath }/view/image/2.jpg" alt="" />
              </a>
              <div>
                <p>예가체프</p>
                <p>10500원</p>
              </div>
            </li>
            <li>
              <a href="">
                <img src="${ pageContext.request.contextPath }/view/image/3.jpg" alt="" />
              </a>
              <div>
                <p>탄자니아</p>
                <p>10500원</p>
              </div>
            </li>
            <li>
              <a href="">
                <img src="${ pageContext.request.contextPath }/view/image/4.jpg" alt="" />
              </a>
              <div>
                <p>아리차</p>
                <p>10500원</p>
              </div>
            </li>
          </ul>
        </div>
      </div>

      <div class="modPage center">
          <div class="inner center">
            <a <c:if test="${ start >= 3}" >href="${ pageContext.request.contextPath }/board/blend?pageNum=${start-3}" class="active"</c:if>>&laquo;</a>
            <c:forEach var="p" begin="${ start }" end="${ end }">
            <a class="active" href="${ pageContext.request.contextPath }/board/blend?pageNum=${p}">${ p }</a>
            </c:forEach>
            <a <c:if test="${ end < maxPage }">href="${ pageContext.request.contextPath }/board/blend?pageNum=${end + 3}" class="active"</c:if>>&raquo;</a>
          </div>
      </div>

    </main>
  </body>
</html>
