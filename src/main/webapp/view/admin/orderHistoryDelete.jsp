<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>

<body>
<main>
  <div class="admin_page_wrap">
    <div class="page_head">
      <a href="">
        <h1>주문 기록 삭제</h1>
      </a>
    </div>
    <form
      action="${ pageContext.request.contextPath }/admin/orderHistoryDeletePro"
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
              value="${ requestScope.orderId }"
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
              value="${ requestScope.productCode }"
              required
              readonly
              style="background-color: #F4F4F4; border: 1px solid black; border-radius: 2px;"
            />
          </li>
          <li>
            <label>삭제 확인</label>
            <input
              name="confirmDelete"
              class="confirm_delete"
              type="text"
              required
            >
          </li>
          <li>
            <input type="submit" value="삭제하기" class="submit_btn" />
          </li>
        </ul>
      </div>
    </form>
  </div>
</main>
</body>
