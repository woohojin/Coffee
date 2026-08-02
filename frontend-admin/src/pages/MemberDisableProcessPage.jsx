import { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

const MEMBER_TIER = {
  0: "미승인",
  1: "임대",
  2: "미임대",
  3: "카페고객",
  9: "관리자",
};

function MemberDisableProcessPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const memberId = searchParams.get("memberId");

  const [member, setMember] = useState(null);

  useEffect(() => {
    if (!memberId) return;

    axiosInstance
      .get(`/api/admin/members/${memberId}`)
      .then((res) => setMember(res.data.data))
      .catch((err) => console.error("회원 조회 실패:", err));
  }, [memberId]);

  const handleEnable = async () => {
    if (!confirm("멤버 비활성화 상태가 변경됩니다. 진행하시겠습니까?")) return;

    try {
      const res = await axiosInstance.patch(
        `/api/admin/members/${memberId}/disable`,
      );
      if (res.data.success) {
        alert("멤버 비활성화 상태 수정 성공");
        navigate("/admin/memberDisableUpdate");
      } else {
        alert(res.data.message || "수정에 실패했습니다.");
      }
    } catch (err) {
      alert(err.response?.data?.message || "수정 중 오류가 발생했습니다.");
    }
  };

  const handleWithdraw = async () => {
    const memo = prompt(
      "탈퇴 처리 사유를 입력해주세요 (선택사항, 비워두면 생략됩니다):",
    );
    if (memo === null) return;

    if (
      !confirm(
        "멤버를 탈퇴 처리합니다. 이 작업은 되돌릴 수 없습니다. 진행하시겠습니까?",
      )
    )
      return;

    try {
      const res = await axiosInstance.delete(
        `/api/admin/members/${memberId}`,
        { params: { memo } },
      );
      if (res.data.success) {
        alert("멤버 탈퇴 처리 성공");
        navigate("/admin/memberDisableUpdate");
      } else {
        alert(res.data.message || "탈퇴 처리에 실패했습니다.");
      }
    } catch (err) {
      alert(err.response?.data?.message || "탈퇴 처리 중 오류가 발생했습니다.");
    }
  };

  if (!member) {
    return (
      <main id="member_profile_page">
        <div className="page_head">
          <h1>비활성화 회원 처리</h1>
        </div>
      </main>
    );
  }

  return (
    <main id="member_profile_page">
      <div className="page_head">
        <a
          onClick={() =>
            navigate(`/admin/memberDisableProcess?memberId=${memberId}`)
          }
          style={{ cursor: "pointer" }}
        >
          <h1>비활성화 회원 처리</h1>
        </a>
      </div>

      <div className="member_signup_form_wrap">
        <div className="member_signup_form">
          <p>회원 정보</p>
          <table className="member_signup_form_info">
            <tbody>
              <tr>
                <th>
                  <label>아이디</label>
                </th>
                <td>
                  <input
                    type="text"
                    value={member.memberId || ""}
                    readOnly
                    style={{ backgroundColor: "#F4F4F4" }}
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label>이름</label>
                </th>
                <td>
                  <input
                    type="text"
                    value={member.memberName || ""}
                    readOnly
                    style={{ backgroundColor: "#F4F4F4" }}
                  />
                </td>
              </tr>
            </tbody>
          </table>

          <p>기타 정보</p>
          <table className="member_signup_form_personal_info">
            <tbody>
              <tr className="member_address_wrap">
                <th>
                  <label>주소</label>
                </th>
                <td>
                  <ul>
                    <li>
                      <input
                        type="text"
                        value={member.memberAddress || ""}
                        readOnly
                        style={{ backgroundColor: "#F4F4F4" }}
                      />
                    </li>
                    <li>
                      <input
                        type="text"
                        value={member.memberDetailAddress || ""}
                        readOnly
                        style={{ backgroundColor: "#F4F4F4" }}
                      />
                    </li>
                  </ul>
                </td>
              </tr>
              <tr className="member_delivery_address_wrap">
                <th>
                  <label>배송지</label>
                </th>
                <td>
                  <ul>
                    <li>
                      <input
                        type="text"
                        value={member.memberDeliveryAddress || ""}
                        readOnly
                        style={{ backgroundColor: "#F4F4F4" }}
                      />
                    </li>
                    <li>
                      <input
                        type="text"
                        value={member.memberDetailDeliveryAddress || ""}
                        readOnly
                        style={{ backgroundColor: "#F4F4F4" }}
                      />
                    </li>
                  </ul>
                </td>
              </tr>
              <tr>
                <th>
                  <label>개인 연락처</label>
                </th>
                <td>
                  <input
                    type="text"
                    value={member.memberTel || ""}
                    readOnly
                    style={{ backgroundColor: "#F4F4F4" }}
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label>회사명</label>
                </th>
                <td>
                  <input
                    type="text"
                    value={member.memberCompanyName || ""}
                    readOnly
                    style={{ backgroundColor: "#F4F4F4" }}
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label>회사 연락처</label>
                </th>
                <td>
                  <input
                    type="text"
                    value={member.memberCompanyTel || ""}
                    readOnly
                    style={{ backgroundColor: "#F4F4F4" }}
                  />
                </td>
              </tr>
              <tr className="member_email_wrap">
                <th>
                  <label>이메일</label>
                </th>
                <td>
                  <input
                    type="text"
                    value={member.memberEmail || ""}
                    readOnly
                    style={{ backgroundColor: "#F4F4F4" }}
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label>등급</label>
                </th>
                <td>
                  <input
                    type="text"
                    value={MEMBER_TIER[member.memberTier] ?? member.memberTier}
                    readOnly
                    style={{ backgroundColor: "#F4F4F4" }}
                  />
                </td>
              </tr>
            </tbody>
          </table>

          <div className="signup" style={{ justifyContent: "space-between" }}>
            <button
              type="button"
              className="submit_btn"
              onClick={handleWithdraw}
            >
              탈퇴처리
            </button>
            <button type="button" className="submit_btn" onClick={handleEnable}>
              비활성화 해제
            </button>
          </div>
        </div>
      </div>
    </main>
  );
}

export default MemberDisableProcessPage;
