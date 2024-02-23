<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
        "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<body>
  <main>
    <div id="payment-method">
      123
    </div>
    <div id="agreement">

    </div>
    <button id="payment-button">결제하기</button>
  </main>
    <script>
      const button = document.getElementById("payment-button");

      const widgetClientKey = "test_ck_yZqmkKeP8gNW0G14QzkBrbQRxB9l";
      const customerKey = "2J4d1bA78p43EeAC6";
      const paymentWidget = PaymentWidget(widgetClientKey, customerKey);

      const paymentMethodWidget = paymentWidget.renderPaymentMethods(
              "#payment-method",
              { value: 50000 },
              { variantKey: "DEFAULT" }
      );
    </script>
  <script src="https://js.tosspayments.com/v1/payment-widget"></script>
</body>

