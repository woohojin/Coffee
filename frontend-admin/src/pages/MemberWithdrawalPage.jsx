import { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

function MemberWithdrawalPage() {
  const navigate = useNavigate();
  const [members, setMembers] = useState([]);
  const [totalCount, setTotalCount] = useState(0);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [isSearching, setIsSearching] = useState(false);
  const [isExpanded, setIsExpanded] = useState(false);

  const [searchMemberId, setSearchMemberId] = useState("");
  const [searchMemberCompanyName, setSearchMemberCompanyName] = useState("");
  const [searchMemberTel, setSearchMemberTel] = useState("");
  const [searchMemberCompanyTel, setSearchMemberCompanyTel] = useState("");
  const [searchMemberEmail, setSearchMemberEmail] = useState("");
  const [searchWithdrawalMemo, setSearchWithdrawalMemo] = useState("");

  const searchParams = useRef({});

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

  const fetchSearch = async (p = page) => {
    try {
      const res = await axiosInstance.get("/api/admin/members/withdrawal/search", {
        params: { ...searchParams.current, page: p },
      });
      const data = res.data.data;
      setMembers(data.list);
      setTotalCount(data.totalCount);
      setTotalPages(data.totalPages);
    } catch (err) {
      console.error("검색 실패:", err);
    }
  };

  useEffect(() => {
    if (isSearching) {
      fetchSearch(page);
    } else {
      fetchMembers(page);
    }
  }, [page]);

  const handleSearch = () => {
    searchParams.current = {
      memberId: searchMemberId,
      memberCompanyName: searchMemberCompanyName,
      memberTel: searchMemberTel,
      memberCompanyTel: searchMemberCompanyTel,
      memberEmail: searchMemberEmail,
      withdrawalMemo: searchWithdrawalMemo,
    };
    setIsSearching(true);
    setPage(1);
    fetchSearch(1);
  };

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

        <div className="search_form_wrap center">
          <div className="inline_wrap">
            <div
              className="search_form"
              style={{
                maxHeight: isExpanded ? "200px" : "70px",
                overflowY: "hidden",
                transition: "all 0.6s ease",
                width: "480px",
              }}
            >
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  marginBottom: "10px",
                }}
              >
                <label htmlFor="memberId">아이디</label>
                <input
                  type="text"
                  id="memberId"
                  value={searchMemberId}
                  onChange={(e) => setSearchMemberId(e.target.value)}
                />
                <label htmlFor="memberCompanyName">업체명</label>
                <input
                  type="text"
                  id="memberCompanyName"
                  value={searchMemberCompanyName}
                  onChange={(e) => setSearchMemberCompanyName(e.target.value)}
                />
              </div>
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  marginBottom: "10px",
                }}
              >
                <label htmlFor="memberTel">전화번호</label>
                <input
                  type="text"
                  id="memberTel"
                  value={searchMemberTel}
                  onChange={(e) => setSearchMemberTel(e.target.value)}
                />
                <label htmlFor="memberCompanyTel">회사번호</label>
                <input
                  type="text"
                  id="memberCompanyTel"
                  value={searchMemberCompanyTel}
                  onChange={(e) => setSearchMemberCompanyTel(e.target.value)}
                />
              </div>
              <div
                className="last"
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  marginBottom: "10px",
                  width: "240px",
                }}
              >
                <label htmlFor="memberEmail">이메일</label>
                <input
                  type="text"
                  id="memberEmail"
                  value={searchMemberEmail}
                  onChange={(e) => setSearchMemberEmail(e.target.value)}
                />
              </div>
              <div
                className="last"
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  marginBottom: "10px",
                  width: "240px",
                }}
              >
                <label htmlFor="withdrawalMemo">메모</label>
                <input
                  type="text"
                  id="withdrawalMemo"
                  value={searchWithdrawalMemo}
                  onChange={(e) => setSearchWithdrawalMemo(e.target.value)}
                />
              </div>
            </div>
            <div
              className={isExpanded ? "expand" : "collapse"}
              onClick={() => setIsExpanded(!isExpanded)}
              style={{ cursor: "pointer" }}
            >
              <img
                src={
                  isExpanded ? "/image/up-arrow.png" : "/image/down-arrow.png"
                }
                alt=""
              />
            </div>
          </div>
        </div>

        <div className="btn_wrap center">
          <div className="btn">
            <a onClick={handleSearch} style={{ cursor: "pointer" }}>
              검색
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
                      <th className="member_email">
                        <div className="asc">
                          <span>이메일</span>
                        </div>
                      </th>
                      <th className="withdrawal_memo">
                        <div className="asc">
                          <span>메모</span>
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
                          <p>{m.memberEmail}</p>
                        </td>
                        <td>
                          <p>{m.withdrawalMemo}</p>
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
