import { useState } from "react";
import axiosInstance from "../api/axiosInstance";
import { useNavigate, Link } from "react-router-dom";

function MemberSignInPage({ setMember }) {
  const [memberId, setMemberId] = useState("");
  const [memberPassword, setMemberPassword] = useState("");
  const navigate = useNavigate();

  const handleSignIn = async (e) => {
    e.preventDefault();
    try {
      const formData = new FormData();
      formData.append("memberId", memberId);
      formData.append("memberPassword", memberPassword);

      await axiosInstance.post("/member/memberSignInPro", formData);
      const meRes = await axiosInstance.get("/api/member/me");
      setMember(meRes.data.data);
      navigate("/main");
    } catch (err) {
      alert(err.response?.data?.message || "로그인 실패");
    }
  };

  return (
    <main>
      <div className="member_signin_form_wrap">
        <form onSubmit={handleSignIn} className="member_signin_form">
          <table>
            <tbody>
              <tr>
                <th>
                  <label htmlFor="member_id">아이디</label>
                </th>
                <td>
                  <input
                    name="memberId"
                    id="member_id"
                    className="memberId"
                    type="text"
                    spellCheck="false"
                    autoFocus
                    required
                    value={memberId}
                    onChange={(e) => setMemberId(e.target.value)}
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
          <div className="autologin_checkbox">
            <label htmlFor="auto_login">자동 로그인</label>
            <input type="checkbox" id="auto_login" name="autoLogin" />
          </div>
          <div className="signin">
            <Link to="/member/memberFindAccount" target="_blank">
              아이디 / 비밀번호 찾기
            </Link>
            <input type="submit" value="로그인" className="submit_btn" />
          </div>
        </form>
      </div>
    </main>
  );
}

export default MemberSignInPage;
