import { Outlet, Link, useNavigate } from "react-router-dom";
import { useAdminAuth } from "../store/adminAuthStore";
import axiosInstance from "../api/axiosInstance";

function AdminLayout() {
  const { setAdmin } = useAdminAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    if (!confirm("로그아웃 하시겠습니까?")) return;
    await axiosInstance.post("/member/memberLogout");
    setAdmin(null);
    navigate("/admin/login");
  };

  return (
    <div id="admin_wrap">
      <header>
        <div id="hd_wrap" className="center">
          <div className="hd_lnb">
            <div className="hd_lnb_list">
              <ul>
                <li>
                  <Link to="/admin/orderHistory">주문 기록</Link>
                </li>
                <li>
                  <Link to="/admin/memberList">회원 리스트</Link>
                </li>
                <li>
                  <Link to="/admin/memberTierUpdate">회원 승인 요청</Link>
                </li>
                <li>
                  <Link to="/admin/productList">제품 리스트</Link>
                </li>
                <li>
                  <Link to="/admin/productUpload">제품 등록</Link>
                </li>
                <li>
                  <Link to="/admin/productDelete">제품 삭제</Link>
                </li>
                <li>
                  <Link to="/admin/memberDisableUpdate">
                    비활성화 회원 리스트
                  </Link>
                </li>
                <li>
                  <Link to="/admin/memberWithdrawalList">탈퇴 회원 리스트</Link>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </header>
      <main>
        <Outlet />
      </main>
    </div>
  );
}

export default AdminLayout;
