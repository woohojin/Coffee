import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

const MEMBER_TIER = {
  0: "미승인",
  1: "임대",
  2: "미임대",
  3: "카페고객",
  9: "관리자",
};

function MemberDisablePage() {
  const navigate = useNavigate();
  const [memberId, setMemberId] = useState("");
  const [members, setMembers] = useState([]);
  const [totalCount, setTotalCount] = useState(0);

  const fetchMembers = async () => {
    try {
      const res = await axiosInstance.get("/api/admin/members/disabled");
      const data = res.data.data;
      setMembers(data.list);
      setTotalCount(data.totalCount);
    } catch (err) {
      console.error("목록 조회 실패:", err);
    }
  };

  useEffect(() => {
    fetchMembers();
  }, []);

  const handleUpdate = async () => {
    if (!memberId.trim()) {
      alert("아이디를 입력해주세요.");
      return;
    }
    if (!confirm("멤버 비활성화 상태가 변경됩니다. 진행하시겠습니까?")) return;

    try {
      const res = await axiosInstance.patch(
        `/api/admin/members/${memberId}/disable`,
      );
      if (res.data.success) {
        alert("멤버 비활성화 상태 수정 성공");
        setMemberId("");
        fetchMembers();
      } else {
        alert(res.data.message || "수정에 실패했습니다.");
      }
    } catch (err) {
      alert(err.response?.data?.message || "수정 중 오류가 발생했습니다.");
    }
  };

  const sortIcon = () => <img src="/image/down-arrow.png" alt="" />;

  return (
    <main id="admin_page">
      <div className="admin_page_wrap">
        <div className="page_head">
          <a
            onClick={() => navigate("/admin/memberDisableUpdate")}
            style={{ cursor: "pointer" }}
          >
            <h1>회원 비활성화 수정</h1>
          </a>
        </div>

        <div className="search_form_wrap center">
          <div className="inline_wrap">
            <div
              className="search_form"
              style={{ maxHeight: "70px", overflowY: "hidden", width: "480px" }}
            >
              <div style={{ justifyContent: "center" }}>
                <label htmlFor="memberId" style={{ flex: "none" }}>
                  아이디
                </label>
                <input
                  type="text"
                  id="memberId"
                  value={memberId}
                  onChange={(e) => setMemberId(e.target.value)}
                />
              </div>
            </div>
          </div>
        </div>

        <div className="btn_wrap center">
          <div className="btn">
            <a onClick={handleUpdate} style={{ cursor: "pointer" }}>
              업데이트
            </a>
          </div>
        </div>

        <div className="list">
          <ul className="center">
            {totalCount === 0 ? (
              <div>
                <p>회원을 찾을 수 없습니다.</p>
              </div>
            ) : (
              <div>
                <table className="list">
                  <thead>
                    <tr>
                      <th className="member_tier">
                        <div className="asc">
                          <span>등급</span>
                          {sortIcon()}
                        </div>
                      </th>
                      <th className="member_fran_code">
                        <div className="asc">
                          <span>가맹점코드</span>
                          {sortIcon()}
                        </div>
                      </th>
                      <th className="member_id">
                        <div className="asc">
                          <span>아이디</span>
                          {sortIcon()}
                        </div>
                      </th>
                      <th className="member_name">
                        <div className="asc">
                          <span>이름</span>
                          {sortIcon()}
                        </div>
                      </th>
                      <th className="member_company_name">
                        <div className="asc">
                          <span>업체명</span>
                          {sortIcon()}
                        </div>
                      </th>
                      <th className="member_tel">
                        <div className="asc">
                          <span>전화번호</span>
                          {sortIcon()}
                        </div>
                      </th>
                      <th className="member_company_tel">
                        <div className="asc">
                          <span>회사번호</span>
                          {sortIcon()}
                        </div>
                      </th>
                      <th className="member_address">
                        <div className="asc">
                          <span>주소</span>
                          {sortIcon()}
                        </div>
                      </th>
                      <th className="member_delivery_address">
                        <div className="asc">
                          <span>배송지</span>
                          {sortIcon()}
                        </div>
                      </th>
                      <th className="member_file">
                        <div className="asc">
                          <span>파일</span>
                          {sortIcon()}
                        </div>
                      </th>
                      <th className="member_email">
                        <div className="asc">
                          <span>이메일</span>
                          {sortIcon()}
                        </div>
                      </th>
                      <th className="member_date">
                        <div className="asc">
                          <span>가입일</span>
                          {sortIcon()}
                        </div>
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {members.map((m) => (
                      <tr key={m.memberId}>
                        <td>
                          <p>{MEMBER_TIER[m.memberTier] ?? m.memberTier}</p>
                        </td>
                        <td>
                          <p>{m.memberFranCode}</p>
                        </td>
                        <td>
                          <p>{m.memberId}</p>
                        </td>
                        <td>
                          <p>{m.memberName}</p>
                        </td>
                        <td>
                          <p>{m.memberCompanyName}</p>
                        </td>
                        <td>
                          <p>{m.memberTel}</p>
                        </td>
                        <td>
                          <p>{m.memberCompanyTel}</p>
                        </td>
                        <td>
                          <p>{m.memberAddress}</p>
                          <p>{m.memberDetailAddress}</p>
                        </td>
                        <td>
                          <p>{m.memberDeliveryAddress}</p>
                          <p>{m.memberDetailDeliveryAddress}</p>
                        </td>
                        <td>
                          <p>{m.memberFile}</p>
                        </td>
                        <td>
                          <p>{m.memberEmail}</p>
                        </td>
                        <td>
                          <p>{m.memberDate}</p>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </ul>
        </div>
      </div>
    </main>
  );
}

export default MemberDisablePage;
