<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<body>
  <main>
    <div class="wrapper w-100">
      <div class="flex-column align-center confirm-loading w-100 max-w-540">
        <div class="flex-column align-center">
          <img src="https://static.toss.im/lotties/loading-spot-apng.png" width="120" height="120"></img>
          <h2 class="title text-center">결제 요청까지 성공했어요.</h2>
          <h4 class="text-center description">결제 승인하고 완료해보세요.</h4>
        </div>
        <div class="w-100">
          <button id="confirmPaymentButton" class="btn primary w-100">결제 승인하기</button>
        </div>
      </div>
      <div class="flex-column align-center confirm-success w-100 max-w-540">
        <img src="https://static.toss.im/illusts/check-blue-spot-ending-frame.png" width="120" height="120"></img>
        <h2 class="title">결제를 완료했어요</h2>
        <div class="response-section w-100">
          <div class="flex justify-between">
            <span class="response-label">결제 금액</span>
            <span id="amount" class="response-text"></span>
          </div>
          <div class="flex justify-between">
            <span class="response-label">주문번호</span>
            <span id="orderId" class="response-text"></span>
          </div>
          <div class="flex justify-between">
            <span class="response-label">paymentKey</span>
            <span id="paymentKey" class="response-text"></span>
          </div>
        </div>
      </div>
    </div>
  </main>
  <script>
      const contextPath = "${ pageContext.request.contextPath }";
      // 쿼리 파라미터 값이 결제 요청할 때 보낸 데이터와 동일한지 반드시 확인하세요.
      // 클라이언트에서 결제 금액을 조작하는 행위를 방지할 수 있습니다.
      const urlParams = new URLSearchParams(window.location.search);
      const paymentKey = urlParams.get("paymentKey");
      const orderId = urlParams.get("orderId");
      const amount = urlParams.get("amount");

      const confirmLoadingSection = document.querySelector('.confirm-loading');
      const confirmSuccessSection = document.querySelector('.confirm-success');

      async function confirmPayment() {
          const requestData = {
              paymentKey: paymentKey,
              orderId: orderId,
              amount: amount,
          };

          const response = await fetch(contextPath + "/member/memberPaymentsConfirm", {
              method: "POST",
              headers: {
                  "Content-Type": "application/json",
              },
              body: JSON.stringify(requestData),
          });
          // 결제 성공
          if(response.ok) {
              confirmLoadingSection.style.display = 'none';
              confirmSuccessSection.style.display = 'flex';
          }

          // 결제 실패
          if (!response.ok) {
              const json = await response.json();
              console.log(json);
              window.location.href = contextPath + `/member/memberPaymentsFailure?message=${json.message}&code=${json.code}`;
          }

          const json = await response.json();
          console.log(json);
      }

      const confirmPaymentButton = document.getElementById('confirmPaymentButton');
      confirmPaymentButton.addEventListener('click', confirmPayment);

      const paymentKeyElement = document.getElementById("paymentKey");
      const orderIdElement = document.getElementById("orderId");
      const amountElement = document.getElementById("amount");

      orderIdElement.textContent = "주문번호: " + orderId;
      amountElement.textContent = "결제 금액: " + amount;
      paymentKeyElement.textContent = "paymentKey: " + paymentKey;
  </script>
</body>
