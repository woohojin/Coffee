<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
        "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<body>
<main id="member_history_page">
  <div class="member_history_page_wrap">
    <div class="page_head">
      <a href="">
        <h1>주문기록</h1>
      </a>
    </div>
    <div class="datepicker_form_wrap">
      <form
              action="${ pageContext.request.contextPath }/member/memberHistoryPro"
              class="datepicker_form center"
              method="post"
      >
        <input type="text" name="startDate" id="datepickerStart" class="datepicker" value="${ requestScope.startDate }" />
        <span>&nbsp~&nbsp</span>
        <input type="text" name="endDate" id="datepickerEnd" class="datepicker" value="${ requestScope.endDate }" />
        <input type="submit" value="조회" class="submit_btn" />
      </form>
    </div>
    <div class="member_cart_wrap">
      <c:if test="${ historyCount < 1 }">
        주문기록이 존재하지 않습니다.
      </c:if>
      <c:if test="${ historyCount >= 1}">
        <table class="member_cart">
          <thead>
          <tr>
            <th>주문번호</th>
            <th>이미지</th>
            <th>상품정보</th>
            <th>수량</th>
            <th>금액</th>
            <th>배송지</th>
            <th>주문 날짜</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="c" items="${ list }" varStatus="status">
            <tr>
              <td>
                <p>${ c.historyCode }</p>
              </td>
              <td class="member_cart_image">
                <img src="${ pageContext.request.contextPath }/view/board/files/${ c.productFile }" alt=""/>
              </td>
              <td>
                <p>${ c.productName } ${ c.productUnit }</p>
              </td>
              <td>
                <p>${ c.quantity }</p>
              </td>
              <td>
                <p><fmt:formatNumber value="${ c.productPrice * c.quantity }" pattern="#,###" /> 원</p>
              </td>
              <td>
                <p>${ c.deliveryAddress }</p>
                <p>${ c.detailDeliveryAddress }</p>
              </td>
              <td>
                <p>${ c.orderDate }</p>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </c:if>
    </div>
  </div>
</main>
<script src="${ pageContext.request.contextPath }/view/js/datepicker.js"></script>
</body>
