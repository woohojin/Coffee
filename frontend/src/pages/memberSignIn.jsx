import { useState } from "react";
import axiosInstance from "../api/axiosInstance";
import { useNavigate } from "react-router-dom";

function MemberSignIn() {
  const [memberId, setMemberId] = useState("");
  const [memberPassword, setMemberPassword] = useState("");
  const navigate = useNavigate();

  const handleSignIn = async (e) => {
    e.preventDefault();
    try {
      const res = await axiosInstance.post("/member/memberSignInPro", formData);
      const meRes = await axiosInstance.get("/api/member/me");
      setMember(meRes.data.data);
      navigate("/main");
    } catch (err) {
      alert(err.response?.data?.message || "로그인 실패");
    }
  };

  return (
    <form onSubmit={handleSignIn}>
      <input
        type="text"
        placeholder="아이디"
        value={memberId}
        onChange={(e) => setMemberId(e.target.value)}
      />
      <input
        type="password"
        placeholder="비밀번호"
        value={memberPassword}
        onChange={(e) => setMemberPassword(e.target.value)}
      />
      <button type="submit">로그인</button>
    </form>
  );
}

export default MemberSignIn;
