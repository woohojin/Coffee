import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

function MemberPaymentsPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const [paymentsData, setPaymentsData] = useState(null);

  // totalPrice 없으면 장바구니로 돌려보냄
  useEffect(() => {
    axiosInstance
      .post("/api/member/payments")
      .then((res) => setPaymentsData(res.data.data))
      .catch(() => {
        alert("결제 정보를 불러올 수 없습니다.");
        navigate("/member/memberCart");
      });
  }, []);

  // 토스 위젯 렌더링
  useEffect(() => {
    if (!paymentsData) return;

    const script = document.createElement("script");
    script.src = "https://js.tosspayments.com/v1/payment-widget";
    script.onload = () => {
      const { orderId, customerKey, orderName, totalPrice, member } =
        paymentsData;

      const paymentWidget = window.PaymentWidget(
        "test_ck_yZqmkKeP8gNW0G14QzkBrbQRxB9l",
        customerKey,
      );

      paymentWidget.renderPaymentMethods(
        "#payment-method",
        { value: totalPrice, currency: "KRW", country: "KR" },
        { variantKey: "DEFAULT" },
      );

      paymentWidget.renderAgreement("#agreement", { variantKey: "AGREEMENT" });

      const button = document.getElementById("payment-request-button");
      if (button) {
        button.addEventListener("click", () => {
          paymentWidget.requestPayment({
            orderId,
            orderName,
            successUrl:
              window.location.origin + "/member/memberPaymentsSuccess",
            failUrl: window.location.origin + "/member/memberPaymentsFailure",
            customerName: member.memberName,
            customerMobilePhone: member.memberTel,
          });
        });
      }
    };
    document.body.appendChild(script);

    return () => document.body.removeChild(script);
  }, [paymentsData]);

  if (!paymentsData)
    return (
      <main>
        <p>로딩 중...</p>
      </main>
    );

  return (
    <main>
      <div className="wrapper w-100">
        <div className="max-w-540 w-100">
          <div id="payment-method" className="w-100"></div>
          <div id="agreement" className="w-100"></div>
          <div className="payments_btn-wrapper w-100">
            <button
              id="payment-request-button"
              className="payments_btn primary w-100"
            >
              결제하기
            </button>
          </div>
        </div>
      </div>
    </main>
  );
}

export default MemberPaymentsPage;
