import { useEffect, useState } from "react";
import { useSearchParams, Link } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

function MemberPaymentsSuccessPage() {
  const [searchParams] = useSearchParams();
  const paymentKey = searchParams.get("paymentKey");
  const orderId = searchParams.get("orderId");
  const amount = searchParams.get("amount");
  const [isSuccess, setIsSuccess] = useState(false);

  // 페이지 진입 시 서버에 결제 성공 알림
  useEffect(() => {
    axiosInstance
      .post("/api/member/payments/success", null, {
        params: { orderId, amount },
      })
      .catch(() => {
        window.location.href = `/member/memberPaymentsFailure?message=결제검증실패&code=VERIFY_FAIL`;
      });
  }, []);

  const handleConfirm = async () => {
    try {
      await axiosInstance.post("/api/member/payments/confirm", {
        paymentKey,
        orderId,
        amount,
      });
      setIsSuccess(true);
    } catch (err) {
      const { message, code } = err.response?.data || {};
      window.location.href = `/member/memberPaymentsFailure?message=${message}&code=${code}`;
    }
  };

  return (
    <main>
      <div className="wrapper w-100">
        {!isSuccess ? (
          <div className="flex-column align-center confirm-loading w-100 max-w-540">
            <div className="flex-column align-center">
              <img
                src="https://static.toss.im/lotties/loading-spot-apng.png"
                width="120"
                height="120"
              />
              <h2 className="title text-center">결제 요청까지 성공했어요.</h2>
              <h4 className="text-center description">
                결제를 승인하고 완료해보세요.
              </h4>
            </div>
            <div className="w-100">
              <button
                onClick={handleConfirm}
                className="payments_btn primary w-100"
              >
                결제 승인하기
              </button>
            </div>
          </div>
        ) : (
          <div className="flex-column align-center confirm-success w-100 max-w-540">
            <img
              src="https://static.toss.im/illusts/check-blue-spot-ending-frame.png"
              width="120"
              height="120"
            />
            <h2 className="title">결제를 완료했어요</h2>
            <div className="response-section w-100">
              <div className="flex justify-between">
                <span className="response-label">결제 금액</span>
                <span className="response-text">
                  {Number(amount).toLocaleString("ko-KR")} 원
                </span>
              </div>
              <div className="flex justify-between">
                <span className="response-label">주문번호</span>
                <span className="response-text">{orderId}</span>
              </div>
              <div className="flex-column align-center w-100 max-w-540">
                <div className="button-group">
                  <Link className="payments_btn primary" to="/main">
                    메인화면으로 이동
                  </Link>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </main>
  );
}

export default MemberPaymentsSuccessPage;
