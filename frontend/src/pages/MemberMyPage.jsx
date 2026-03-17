import { Link } from "react-router-dom";

function MemberMyPage() {
  return (
    <main id="member_mypage_page">
      <div className="member_mypage_page_wrap">
        <div className="page_head">
          <h1>마이페이지</h1>
        </div>
        <table className="member_mypage_wrap center">
          <tbody>
            <tr>
              <td>
                <Link to="/member/memberProfile">
                  회원정보 수정
                  <p>
                    고객님의 회원정보를
                    <br />
                    수정 할 수 있습니다.
                  </p>
                </Link>
              </td>
              <td>
                <a>
                  배송 조회
                  <p>준비중입니다.</p>
                </a>
              </td>
              <td>
                <Link to="/member/memberHistory">
                  주문기록 조회
                  <p>
                    고객님께서 주문했던 상품 정보를
                    <br />
                    확인 할 수 있습니다.
                  </p>
                </Link>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </main>
  );
}

export default MemberMyPage;
