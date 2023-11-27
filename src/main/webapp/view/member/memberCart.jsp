<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<body>
<main id="member_cart_page">
  <div class="member_cart_page_wrap">
    <div class="page_head">
      <a>
        <h1>장바구니</h1>
      </a>
    </div>
    <div class="member_cart_wrap">
      <c:if test="${ cartCount < 1 }">
        장바구니에 담은 상품이 없습니다.
      </c:if>
      <c:if test="${ cartCount >= 1}">
      <table class="member_cart">
        <thead>
        <tr>
          <th class="member_cart_image">이미지</th>
          <th class="member_cart_info">상품정보</th>
          <th>수량</th>
          <th class="member_cart_price">금액</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="c" items="${ list }" varStatus="status">
          <tr>
            <td class="member_cart_image">
              <img src="${ pageContext.request.contextPath }/view/board/files/${ c.productFile }" alt=""/>
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
                <input type="text" name="quantity" class="member_cart_quantity_input input${ status.index }" value="${ c.quantity }" required readonly />
                <div class="member_cart_quantity_btn">
                  <button type="button" class="up_btn" onclick="increaseCartQuantity(${ status.index })">
                    <img src="${ pageContext.request.contextPath }/view/image/triangle-up.png" alt="" />
                  </button>
                  <button type="button" class="down_btn" onclick="decreaseCartQuantity(${ status.index })">
                    <img src="${ pageContext.request.contextPath }/view/image/triangle-down.png" alt="" />
                  </button>
                </div>
              </form>
            </td>
            <td class="member_cart_price">
              <p><fmt:formatNumber value="${ c.productPrice * c.quantity }" pattern="#,###" /> 원</p>
            </td>
            <td>
              <a class="member_cart_delete" onclick="deleteCart(${ status.index })">
                <img src="${ pageContext.request.contextPath }/view/image/close.png" alt="" />
              </a>
            </td>
          </tr>
        </c:forEach>
        </tbody>
        <tfoot>
        </tfoot>
      </table>
      </c:if>
    </div>
  </div>
  <div class="member_cart_order_wrap center">
    <div class="member_cart_order">
      <h1>주문 요약</h1>
      <div>
        <span>상품 금액 :</span>
        <span><fmt:formatNumber value="${ requestScope.sumPrice }" pattern="#,###" /> 원</span>
        <input type="hidden" name="sumPrice" value="${ requestScope.sumPrice }" />
      </div>
      <div>
        <span>배송비 :</span>
        <span><fmt:formatNumber value="${ requestScope.deliveryFee }" pattern="#,###" /> 원</span>
        <input type="hidden" name="sumPrice" value="${ requestScope.deliveryFee }" />
      </div>
      <div class="member_cart_order_total">
        <span>합계 :</span>
        <span><fmt:formatNumber value="${ requestScope.totalPrice }" pattern="#,###" /> 원</span>
        <input type="hidden" name="sumPrice" value="${ requestScope.totalPrice }" />
      </div>
      <div class="btn_wrap">
        <div class="btn">
          <a>결제하기</a>
        </div>
      </div>
    </div>
  </div>
</main>
</body>
