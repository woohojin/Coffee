<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
  <body>
    <main>
      <div class="product_detail_wrap">
        <div class="product_detail">
            <c:choose>
              <c:when test="${requestScope.memberTier == '0'}">
                <p>제품을 열람 할 권한이 부족합니다.</p>
                <p>회원가입을 하시거나 회원가입 이후 문제가 발생했다면</p>
                <p>xxx로 연락주십시오.</p>
              </c:when>
              <c:when test="${requestScope.memberTier != 0}">
                <div>
                    <img src="${ pageContext.request.contextPath }/view/image/1.jpg" alt="" />
                </div>
                <h1>${ product.productName }</h1>
                <ul>
                  <li>
                    <p>${ product.productPrice }원</p>
                  </li>
                  <li>
                    <p>${ product.productCountry }</p>
                  </li>
                  <li>
                    <p>${ product.productSpecies }</p>
                  </li>
                  <li>
                    <p>${ product.productCompany }</p>
                  </li>
                </ul>
              </c:when>
            </c:choose>
        </div>
      </div>
    </main>
  </body>
</html>
