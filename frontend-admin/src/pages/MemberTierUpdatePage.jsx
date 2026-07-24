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

function MemberTierUpdatePage() {
  const navigate = useNavigate();
  const [members, setMembers] = useState([]);
  const [totalCount, setTotalCount] = useState(0);
  const [selectedTiers, setSelectedTiers] = useState({});

  const fetchMembers = async () => {
    try {
      const res = await axiosInstance.get("/api/admin/members/pending");
      const data = res.data.data;
      setMembers(data.list);
      setTotalCount(data.totalCount);

      const initialTiers = {};
      data.list.forEach((m) => {
        initialTiers[m.memberId] = "1";
      });
      setSelectedTiers(initialTiers);
    } catch (err) {
      console.error("목록 조회 실패:", err);
    }
  };

  useEffect(() => {
    fetchMembers();
  }, []);

  const handleTierChange = (memberId, value) => {
    setSelectedTiers((prev) => ({ ...prev, [memberId]: value }));
  };

  const handleUpdate = async (memberId) => {
    const memberTier = parseInt(selectedTiers[memberId]);
    try {
      const res = await axiosInstance.patch(
        `/api/admin/members/${memberId}/tier`,
        null,
        { params: { memberTier } },
      );
      if (res.data.success) {
        alert("멤버 등급 수정 성공");
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
            onClick={() => navigate("/admin/memberTierUpdate")}
            style={{ cursor: "pointer" }}
          >
            <h1>멤버 등급 수정</h1>
          </a>
        </div>

        <div className="search_form_wrap center">
          <div className="inline_wrap">
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
                          <th className="member_file">
                            <div className="asc">
                              <span>파일</span>
                              {sortIcon()}
                            </div>
                          </th>
                          <th className="member_date">
                            <div className="asc">
                              <span>가입일</span>
                              {sortIcon()}
                            </div>
                          </th>
                          <th>
                            <div className="asc">
                              <span>등급 설정</span>
                            </div>
                          </th>
                          <th>
                            <div className="asc">
                              <span>업데이트</span>
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
                              <p>{m.memberFile}</p>
                            </td>
                            <td>
                              <p>{m.memberDate}</p>
                            </td>
                            <td>
                              <select
                                value={selectedTiers[m.memberId] || "1"}
                                onChange={(e) =>
                                  handleTierChange(m.memberId, e.target.value)
                                }
                              >
                                <option value="1">임대</option>
                                <option value="2">미임대</option>
                              </select>
                            </td>
                            <td>
                              <div className="signup">
                                <input
                                  type="button"
                                  value="업데이트"
                                  className="btn"
                                  onClick={() => handleUpdate(m.memberId)}
                                  style={{ cursor: "pointer" }}
                                />
                              </div>
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
        </div>
      </div>
    </main>
  );
}

export default MemberTierUpdatePage;
