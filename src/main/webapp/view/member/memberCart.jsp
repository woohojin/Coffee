<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>

<body>
<main id="member_cart_page">

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
          <c:forEach var="c" items="${ list }" varStatus="status">
            <tr>
              <td class="member_cart_image">
                <img src="${ pageContext.request.contextPath }/board/files/${ c.productFile }" alt=""/>
              </td>
              <td class="member_cart_info">
                <p>${ c.productName }</p>
              </td>
              <td class="member_cart_quantity">
                <form
                        action="${ pageContext.request.contextPath }/member/memberCartPro"
                        class="member_cart_form form${ status.index }"
                        method="post"
                >
                  <input type="hidden" name="productCode" value="${ c.productCode }"/>
                  <input type="hidden" name="status" class="status${ status.index }" value="" />
                  <input type="text" name="quantity" class="member_cart_quantity_input input${ status.index }" value="${ c.quantity }" required />
                </form>
                <div class="member_cart_quantity_btn">
                  <button type="button" class="up_btn" onclick="increaseCartQuantity(${ status.index })">+</button>
                  <button type="button" class="down_btn" onclick="decreaseCartQuantity(${ status.index })">-</button>
                </div>
              </td>
              <td class="member_cart_price">
                <p>${ c.productPrice * c.quantity }원</p>
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
</main>
</body>
