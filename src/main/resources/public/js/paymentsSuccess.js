document.addEventListener('DOMContentLoaded', () => {
  // meta 태그에서 Thymeleaf 변수 읽어오기
  const contextPathMeta = document.querySelector('meta[name="contextPath"]');
  const contextPath = contextPathMeta ? contextPathMeta.content : '';

  const paymentKeyMeta = document.querySelector('meta[name="paymentKey"]');
  const paymentKey = paymentKeyMeta ? paymentKeyMeta.content : '';

  const orderIdMeta = document.querySelector('meta[name="orderId"]');
  const orderId = orderIdMeta ? orderIdMeta.content : '';

  const amountMeta = document.querySelector('meta[name="amount"]');
  const amount = amountMeta ? amountMeta.content : '';

  const orderIdElement = document.getElementById("orderId");
  const amountElement = document.getElementById("amount");
  const confirmLoadingSection = document.querySelector('.confirm-loading');
  const confirmSuccessSection = document.querySelector('.confirm-success');
  const confirmPaymentButton = document.getElementById('confirmPaymentButton');

  if (orderIdElement) orderIdElement.textContent = orderId;
  if (amountElement) amountElement.textContent = amount + '원';

  // 결제 확인 함수
  async function confirmPayment() {
    const requestData = {
      paymentKey: paymentKey,
      orderId: orderId,
      amount: amount
    };

    try {
      const response = await fetch(contextPath + "/api/member/payments/confirm", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(requestData)
      });

      if (response.ok) {
        confirmLoadingSection.style.display = 'none';
        confirmSuccessSection.style.display = 'flex';
      } else {
        const json = await response.json();
        console.log(json);
        window.location.href = contextPath + `/member/memberPaymentsFailure?message=${json.message}&code=${json.code}`;
      }
    } catch (err) {
      console.error("결제 확인 실패:", err);
    }
  }

  // 버튼 클릭 이벤트
  if (confirmPaymentButton) {
    confirmPaymentButton.addEventListener('click', confirmPayment);
  }
});