import { useState } from "react";
import { Link } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";
import { useEmailVerify } from "../hooks/useEmailVerify";

function MemberFindAccountPage() {
  const [findType, setFindType] = useState("id");
  const [memberName, setMemberName] = useState("");
  const [memberId, setMemberId] = useState("");
  const [foundIds, setFoundIds] = useState(null);

  const {
    verifyCode,
    setVerifyCode,
    countdown,
    sendVerifyEmail,
    checkVerifyCode,
    verifiedEmail,
  } = useEmailVerify();

  const switchFindType = (type) => {
    setFindType(type);
    setFoundIds(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const body = {
      findType,
      memberEmail: verifiedEmail,
      ...(findType === "id" ? { memberName } : { memberId }),
    };

    try {
      const res = await axiosInstance.post("/api/member/findAccount", body);
      if (!res.data.success) {
        alert(res.data.message || "요청 처리에 실패했습니다.");
        return;
      }

      if (findType === "id") {
        setFoundIds(res.data.data);
      } else {
        alert(res.data.message || "임시 비밀번호가 이메일로 전송되었습니다.");
      }
    } catch (err) {
      alert(err.response?.data?.message || "요청 처리 중 오류가 발생했습니다.");
    }
  };

  if (foundIds) {
    return (
      <main>
        <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
          {foundIds.length > 0 ? (
            foundIds.map((id) => <p key={id}>회원님의 아이디는 {id} 입니다.</p>)
          ) : (
            <p>일치하는 아이디가 없습니다.</p>
          )}
          <div className="btn" style={{ width: "100px", height: "45px", marginTop: "25px" }}>
            <Link to="/member/memberSignIn">로그인창으로</Link>
          </div>
        </div>
      </main>
    );
  }

  return (
    <main>
      <div className="member_signin_form_wrap">
        <form onSubmit={handleSubmit} className="member_signin_form">
          <div>
            <input
              id="find_id"
              name="findType"
              type="radio"
              value="id"
              checked={findType === "id"}
              onChange={() => switchFindType("id")}
            />
            <label htmlFor="find_id">아이디 찾기</label>
            <input
              id="find_password"
              name="findType"
              type="radio"
              value="password"
              checked={findType === "password"}
              onChange={() => switchFindType("password")}
            />
            <label htmlFor="find_password">비밀번호 찾기</label>
          </div>
          <table>
            <tbody>
              {findType === "id" ? (
                <tr>
                  <th>
                    <label htmlFor="member_name">이름</label>
                  </th>
                  <td>
                    <input
                      name="memberName"
                      id="member_name"
                      type="text"
                      spellCheck="false"
                      required
                      value={memberName}
                      onChange={(e) => setMemberName(e.target.value)}
                    />
                  </td>
                </tr>
              ) : (
                <tr>
                  <th>
                    <label htmlFor="member_id">아이디</label>
                  </th>
                  <td>
                    <input
                      name="memberId"
                      id="member_id"
                      type="text"
                      spellCheck="false"
                      required
                      value={memberId}
                      onChange={(e) => setMemberId(e.target.value)}
                    />
                  </td>
                </tr>
              )}
              <tr style={{ position: "relative" }}>
                <th>
                  <label htmlFor="member_email">이메일</label>
                </th>
                <td>
                  <input
                    name="memberEmail"
                    id="member_email"
                    className="member_email"
                    type="text"
                    spellCheck="false"
                    required
                  />
                  <div className="member_email_verify_button">
                    <button
                      className="input_btn"
                      type="button"
                      onClick={() =>
                        sendVerifyEmail(
                          document.getElementById("member_email").value,
                        )
                      }
                    >
                      인증번호 발송
                    </button>
                  </div>
                </td>
              </tr>
              <tr style={{ position: "relative" }}>
                <th>
                  <label>인증번호</label>
                </th>
                <td>
                  <input
                    type="text"
                    className="verify_code"
                    spellCheck="false"
                    required
                    value={verifyCode}
                    onChange={(e) => setVerifyCode(e.target.value)}
                  />
                  <div className="member_email_verify_button">
                    <button
                      className="input_btn"
                      type="button"
                      onClick={() => checkVerifyCode()}
                    >
                      인증하기
                    </button>
                  </div>
                  <div className="remain_time">
                    <span className="countdown">{countdown}</span>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <div className="signin" style={{ justifyContent: "flex-end", marginTop: "15px" }}>
            <input
              type="submit"
              value="찾기"
              className="submit_btn"
            />
          </div>
        </form>
      </div>
    </main>
  );
}

export default MemberFindAccountPage;
