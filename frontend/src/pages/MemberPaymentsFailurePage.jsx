import { useSearchParams, Link } from "react-router-dom";

function MemberPaymentsFailurePage() {
  const [searchParams] = useSearchParams();
  const message = searchParams.get("message");
  const code = searchParams.get("code");

  return (
    <main>
      <div className="wrapper w-100">
        <div className="flex-column align-center w-100 max-w-540">
          <img
            src="https://static.toss.im/lotties/error-spot-no-loop-space-apng.png"
            width="160"
            height="160"
          />
          <h2 className="title">결제를 실패했어요</h2>
          <div className="response-section w-100">
            <div className="flex justify-between">
              <span className="response-label">code</span>
              <span className="response-text">{code}</span>
            </div>
            <div className="flex justify-between">
              <span className="response-label">message</span>
              <span className="response-text">{message}</span>
            </div>
          </div>
          <div className="button-group">
            <Link className="payments_btn primary" to="/member/memberCart">
              장바구니로 이동하기
            </Link>
            <Link className="payments_btn primary" to="/main">
              메인화면으로 이동하기
            </Link>
          </div>
        </div>
      </div>
    </main>
  );
}

export default MemberPaymentsFailurePage;
