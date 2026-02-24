document.addEventListener('DOMContentLoaded', () => {
  const contextPathMeta = document.querySelector('meta[name="contextPath"]');
  const contextPath = contextPathMeta ? contextPathMeta.content : '';

  const widgetClientKeyMeta = document.querySelector('meta[name="widgetClientKey"]');
  const widgetClientKey = widgetClientKeyMeta ? widgetClientKeyMeta.content : 'test_ck_yZqmkKeP8gNW0G14QzkBrbQRxB9l';

  const customerKeyMeta = document.querySelector('meta[name="customerKey"]');
  const customerKey = customerKeyMeta ? customerKeyMeta.content : '';

  const totalPriceMeta = document.querySelector('meta[name="totalPrice"]');
  const totalPrice = totalPriceMeta ? Number(totalPriceMeta.content) : 0;

  const orderIdMeta = document.querySelector('meta[name="orderId"]');
  const orderId = orderIdMeta ? orderIdMeta.content : '';

  const orderNameMeta = document.querySelector('meta[name="orderName"]');
  const orderName = orderNameMeta ? orderNameMeta.content : '';

  const memberNameMeta = document.querySelector('meta[name="memberName"]');
  const memberName = memberNameMeta ? memberNameMeta.content : '';

  const memberTelMeta = document.querySelector('meta[name="memberTel"]');
  const memberTel = memberTelMeta ? memberTelMeta.content : '';

  const button = document.getElementById("payment-request-button");
  if (!button) return;

  const paymentWidget = PaymentWidget(widgetClientKey, customerKey);

  paymentWidget.renderPaymentMethods(
    "#payment-method",
    { value: totalPrice, currency: "KRW", country: "KR" },
    { variantKey: "DEFAULT" }
  );

  paymentWidget.renderAgreement(
    "#agreement",
    { variantKey: "AGREEMENT" }
  );

  button.addEventListener("click", () => {
    paymentWidget.requestPayment({
      orderId: orderId,
      orderName: orderName,
      successUrl: window.location.origin + contextPath + "/member/memberPaymentsSuccess",
      failUrl: window.location.origin + contextPath + "/member/memberPaymentsFailure",
      customerName: memberName,
      customerMobilePhone: memberTel
    });
  });
});