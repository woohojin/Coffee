import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";
import { useAdminAuth } from "../store/adminAuthStore";

function AdminLoginPage() {
  const navigate = useNavigate();
  const { setAdmin } = useAdminAuth();
  const [memberId, setMemberId] = useState("");
  const [memberPassword, setMemberPassword] = useState("");

  const handleSignIn = async (e) => {
    e.preventDefault();
    try {
      const params = new URLSearchParams();
      params.append("memberId", memberId);
      params.append("memberPassword", memberPassword);
      await axiosInstance.post("/member/memberSignInPro", params);

      const res = await axiosInstance.get("/api/admin/me");
      setAdmin(res.data.data);
      navigate("/admin/dashboard");
    } catch (err) {
      alert(
        err.response?.data?.message ||
          "로그인 실패 또는 관리자 권한이 없습니다.",
      );
    }
  };

  return (
    <main id="admin_login_page">
      <form onSubmit={handleSignIn}>
        <h1>관리자 로그인</h1>
        <div>
          <label>아이디</label>
          <input
            type="text"
            value={memberId}
            onChange={(e) => setMemberId(e.target.value)}
            required
          />
        </div>
        <div>
          <label>비밀번호</label>
          <input
            type="password"
            value={memberPassword}
            onChange={(e) => setMemberPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit">로그인</button>
      </form>
    </main>
  );
}

export default AdminLoginPage;
