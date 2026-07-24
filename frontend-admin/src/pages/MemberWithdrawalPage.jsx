import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

function MemberWithdrawalPage() {
  const navigate = useNavigate();
  const [members, setMembers] = useState([]);
  const [totalCount, setTotalCount] = useState(0);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);

  const fetchMembers = async (p = page) => {
    try {
      const res = await axiosInstance.get("/api/admin/members/withdrawal", {
        params: { page: p },
      });
      const data = res.data.data;
      setMembers(data.list);
      setTotalCount(data.totalCount);
      setTotalPages(data.totalPages);
    } catch (err) {
      console.error("목록 조회 실패:", err);
    }
  };

  useEffect(() => {
    fetchMembers(page);
  }, [page]);

  return (
    <main id="admin_page">
      <div className="admin_page_wrap">
        <div className="page_head">
          <a
            onClick={() => navigate("/admin/memberWithdrawalList")}
            style={{ cursor: "pointer" }}
          >
            <h1>탈퇴 회원 리스트</h1>
          </a>
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
                      <th className="member_id">
                        <div className="asc">
                          <span>아이디</span>
                        </div>
                      </th>
                      <th className="member_company_name">
                        <div className="asc">
                          <span>업체명</span>
                        </div>
                      </th>
                      <th className="member_tel">
                        <div className="asc">
                          <span>전화번호</span>
                        </div>
                      </th>
                      <th className="member_company_tel">
                        <div className="asc">
                          <span>회사번호</span>
                        </div>
                      </th>
                      <th className="member_withdrawal_date">
                        <div className="asc">
                          <span>탈퇴일자</span>
                        </div>
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {members.map((m, idx) => (
                      <tr key={idx}>
                        <td>
                          <p>{m.memberId}</p>
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
                          <p>{m.memberWithdrawalDate}</p>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </ul>
        </div>

        <div className="pagination_wrap center">
          <div className="pagination">
            {page >= 3 && (
              <a
                onClick={() => setPage(page - 3)}
                style={{ cursor: "pointer" }}
              >
                «
              </a>
            )}
            {Array.from({ length: totalPages }, (_, i) => i + 1)
              .filter((p) => Math.abs(p - page) <= 2)
              .map((p) => (
                <a
                  key={p}
                  onClick={() => setPage(p)}
                  style={{
                    cursor: "pointer",
                    fontWeight: p === page ? "bold" : "normal",
                  }}
                >
                  {p}
                </a>
              ))}
            {page < totalPages - 3 && (
              <a
                onClick={() => setPage(page + 3)}
                style={{ cursor: "pointer" }}
              >
                »
              </a>
            )}
          </div>
        </div>
      </div>
    </main>
  );
}

export default MemberWithdrawalPage;
