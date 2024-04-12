<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>

<body>
<main>
  <div class="admin_page_wrap">
    <div class="page_head">
      <a href="">
        <h1>주문 기록 수정</h1>
      </a>
    </div>
    <form
      action="${ pageContext.request.contextPath }/admin/orderHistoryUpdatePro"
      method="post"
    >
      <div class="product_form center">
        <ul>
          <li>
            <label>주문 번호 : </label>
            <input
              name="orderId"
              class="order_id"
              type="text"
              value="${ requestScope.history.orderId }"
              required
              readonly
              style="background-color: #F4F4F4; border: 1px solid black; border-radius: 2px;"
            />
          </li>
          <li>
            <label>제품 코드 : </label>
            <input
              name="productCode"
              class="product_code"
              type="text"
              value="${ requestScope.history.productCode }"
              required
              readonly
              style="background-color: #F4F4F4; border: 1px solid black; border-radius: 2px;"
            />
          </li>
          <li>
            <label>가맹점코드 : </label>
            <input
              name="memberFranCode"
              class="member_fran_code"
              type="text"
              value="${ requestScope.history.memberFranCode }"
              required
            />
          </li>
          <li>
            <label>주문자명 : </label>
            <input
              name="memberName"
              class="member_name"
              type="text"
              value="${ requestScope.history.memberName }"
              required
            />
          </li>
          <li>
            <label>업체명 : </label>
            <input
              name="memberCompanyName"
              class="member_company_name"
              type="text"
              value="${ requestScope.history.memberCompanyName }"
            />
          </li>
          <li>
            <label>배송지 : </label>
            <input
              name="deliveryAddress"
              class="delivery_address"
              type="text"
              value="${ requestScope.history.deliveryAddress }"
              required
            />
          </li>
          <li>
            <label>배송지 상세주소 : </label>
            <input
              name="detailDeliveryAddress"
              class="detailDelivery_address"
              type="text"
              value="${ requestScope.history.detailDeliveryAddress }"
              required
            />
          </li>
          <li>
            <label>송장 번호 : </label>
            <input
              name="deliveryCode"
              class="delivery_code"
              type="text"
              value="${ requestScope.history.deliveryCode }"
            />
          </li>
          <li>
            <label>수정자 : </label>
            <input
              name="historyModifierName"
              class="history_modifier_name"
              type="text"
              required
            />
          </li>
          <li>
            <div class="submit" style="justify-content: space-between">
              <div class="input_btn">
                <a href="${ pageContext.request.contextPath }/admin/orderHistoryDelete?orderId=${ requestScope.history.orderId }&&productCode=${ requestScope.history.productCode }">삭제하기</a>
              </div>
              <input type="submit" value="수정하기" class="submit_btn" />
            </div>
          </li>
        </ul>
      </div>

    </form>
  </div>
</main>
</body>
