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
        <thead>
        <tr>
          <th class="member_cart_image">이미지</th>
          <th class="member_cart_info">상품정보</th>
          <th>수량</th>
          <th class="member_cart_price">금액</th>
          <th>배송비</th>
        </tr>
        </thead>
        <tbody>
          <c:forEach var="c" items="${ list }">
            <tr>
              <td class="member_cart_image">
              
              </td>
              <td class="member_cart_info">
                <p>${ c.productName }</p>
              </td>
              <td>
                <input type="text" value="${ c.quantity }" required />
              </td>
              <td class="member_cart_price">
                <p>${ c.productPrice }원</p>
              </td>
              <td>
                <p>3000원</p>
              </td>
            </tr>
          </c:forEach>
        </tbody>
        <tfoot>
        </tfoot>
      </table>
    </div>
  </form>
</main>
</body>
