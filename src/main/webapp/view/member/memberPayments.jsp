<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
        "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<body>
  <main>
    <div class="wrapper w-100">
      <div class="max-w-540 w-100">
        <div id="payment-method" class="w-100"></div>
        <div id="agreement" class="w-100"></div>
        <div class="payments_btn-wrapper w-100">
          <button id="payment-request-button" class="payments_btn primary w-100">결제하기</button>
        </div>
      </div>
    </div>
  </main>
    <script>
      const contextPath = "${ pageContext.request.contextPath }";

      const button = document.getElementById("payment-request-button");

      const widgetClientKey = "test_ck_yZqmkKeP8gNW0G14QzkBrbQRxB9l";
      const customerKey = "${ requestScope.customerKey }";
      const paymentWidget = PaymentWidget(widgetClientKey, customerKey);

      const paymentMethodWidget = paymentWidget.renderPaymentMethods(
              "#payment-method",
              {
                value: ${ requestScope.totalPrice },
                currency : "KRW",
                country : "KR",
              },
              { variantKey: "DEFAULT" }
      );

      paymentWidget.renderAgreement(
              "#agreement",
              { variantKey: "AGREEMENT" }
      );

      button.addEventListener("click", function () {
        // 결제를 요청하기 전에 orderId, amount를 서버에 저장하세요.
        // 결제 과정에서 악의적으로 결제 금액이 바뀌는 것을 확인하는 용도입니다.
        paymentWidget.requestPayment({
          orderId: "${ requestScope.orderId }",
          orderName: "${ requestScope.orderName }",
          successUrl: window.location.origin + "${ pageContext.request.contextPath }" +"/member/memberPaymentsSuccess",
          failUrl: window.location.origin + "${ pageContext.request.contextPath }" + "/member/memberPaymentsFailure",
          customerName: "${ requestScope.memberName }",
          customerMobilePhone: "${ requestScope.memberTel }",
        });
      });
    </script>
</body>

