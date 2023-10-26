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
    <div class="member_cart_wrap">
      <table class="member_cart">
        <c:forEach var="c" items="${ list }">
          <thead>
          <tr>
            <th>이미지</th>
            <th>상품정보</th>
            <th>수량</th>
            <th>금액</th>
            <th>배송비</th>
          </tr>
          </thead>
          <tbody>
            <tr>
              <td>
              
              </td>
              <td>
                <p>${ c.productName }</p>
              </td>
              <td>
                <p>${ c.quantity } 개</p>
              </td>
              <td>
                <p>${ c.productPrice }원</p>
              </td>
              <td>
                <p>배송비 : 3000원</p>
              </td>
            </tr>
          </tbody>
          <tfoot>
          
          </tfoot>
        </c:forEach>
      </table>
    </div>
  </form>
</main>
</body>
