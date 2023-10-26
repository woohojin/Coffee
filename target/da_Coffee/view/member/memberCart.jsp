<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>

<body>
<main id="member_cart_page">
  <form
    action="${ pageContext.request.contextPath }/board/productUploadPro"
    name="f"
    method="post"
  >
    <div class="product_form">
      <c:forEach var="c" items="${ list }">
        <ul>
          <li>
            <p>${ c.productName }</p>
            <p>${ c.productPrice } 원</p>
            <p>${ c.quantity } 개</p>
          </li>
        </ul>
      </c:forEach>
    </div>
  
  </form>
</main>
</body>
