import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";
import { useAuth } from "../store/authStore";

function MemberWithdrawalPage() {
  const navigate = useNavigate();
  const { member, setMember } = useAuth();
  const [memberPassword, setMemberPassword] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!confirm("정말로 탈퇴하시겠습니까? 탈퇴 후에는 되돌릴 수 없습니다.")) return;

    try {
      await axiosInstance.delete("/api/member/profile", {
        data: { memberPassword },
      });

      alert("회원탈퇴가 완료되었습니다.");
      setMember(null);
      navigate("/main");
    } catch (err) {
      alert(err.response?.data?.message || "회원탈퇴 중 오류가 발생했습니다.");
    }
  };

  return (
    <main>
      <div className="member_signin_form_wrap">
        <form onSubmit={handleSubmit} className="member_signin_form">
          <div className="page_head">
            <h1>회원탈퇴</h1>
          </div>
          <table>
            <tbody>
              <tr>
                <th>
                  <label htmlFor="member_id">아이디</label>
                </th>
                <td>
                  <input
                    id="member_id"
                    className="memberId"
                    type="text"
                    value={member?.memberId || ""}
                    readOnly
                    style={{ border: "1px solid var(--grayLine)" }}
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="member_password">비밀번호</label>
                </th>
                <td>
                  <input
                    name="memberPassword"
                    id="member_password"
                    className="memberPassword"
                    type="password"
                    spellCheck="false"
                    required
                    value={memberPassword}
                    onChange={(e) => setMemberPassword(e.target.value)}
                  />
                </td>
              </tr>
            </tbody>
          </table>
          <div className="signin">
            <input
              type="submit"
              value="회원탈퇴"
              className="submit_btn"
              style={{ marginTop: "15px" }}
            />
          </div>
        </form>
      </div>
    </main>
  );
}

export default MemberWithdrawalPage;
