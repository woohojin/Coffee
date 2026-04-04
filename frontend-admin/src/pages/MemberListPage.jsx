import { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

const MEMBER_TIER = {
  0: "미승인",
  1: "임대",
  2: "미임대",
  3: "카페고객",
  9: "관리자",
};

function MemberListPage() {
  const navigate = useNavigate();
  const [members, setMembers] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [totalCount, setTotalCount] = useState(0);
  const [columnName, setColumnName] = useState("member_tier");
  const [orderBy, setOrderBy] = useState("asc");
  const [isSearching, setIsSearching] = useState(false);
  const [isExpanded, setIsExpanded] = useState(false);

  const [searchMemberCompanyName, setSearchMemberCompanyName] = useState("");
  const [searchMemberFranCode, setSearchMemberFranCode] = useState("");
  const [searchMemberId, setSearchMemberId] = useState("");
  const [searchMemberName, setSearchMemberName] = useState("");
  const [searchMemberTel, setSearchMemberTel] = useState("");
  const [searchMemberCompanyTel, setSearchMemberCompanyTel] = useState("");
  const [searchMemberTier, setSearchMemberTier] = useState("");

  const searchParams = useRef({});

  const fetchMembers = async (p = page, col = columnName, ord = orderBy) => {
    try {
      const res = await axiosInstance.get("/api/admin/members", {
        params: { page: p, columnName: col, orderBy: ord },
      });
      const data = res.data.data;
      setMembers(data.list);
      setTotalPages(data.totalPages);
      setTotalCount(data.totalCount);
    } catch (err) {
      console.error("목록 조회 실패:", err);
    }
  };

  const fetchSearch = async (p = page, col = columnName, ord = orderBy) => {
    try {
      const params = {
        ...searchParams.current,
        page: p,
        columnName: col,
        orderBy: ord,
      };
      const res = await axiosInstance.get("/api/admin/members/search", {
        params,
      });
      const data = res.data.data;
      setMembers(data.list);
      setTotalPages(data.totalPages);
      setTotalCount(data.totalCount);
    } catch (err) {
      console.error("검색 실패:", err);
    }
  };

  useEffect(() => {
    if (isSearching) {
      fetchSearch(page, columnName, orderBy);
    } else {
      fetchMembers(page, columnName, orderBy);
    }
  }, [page, columnName, orderBy]);

  const handleSort = (col) => {
    const newOrder =
      columnName === col ? (orderBy === "asc" ? "desc" : "asc") : "asc";
    setColumnName(col);
    setOrderBy(newOrder);
    setPage(1);
  };

  const handleSearch = () => {
    searchParams.current = {
      memberCompanyName: searchMemberCompanyName,
      memberFranCode: searchMemberFranCode,
      memberId: searchMemberId,
      memberName: searchMemberName,
      memberTel: searchMemberTel,
      memberCompanyTel: searchMemberCompanyTel,
      memberTier: searchMemberTier,
    };
    setIsSearching(true);
    setPage(1);
    fetchSearch(1, columnName, orderBy);
  };

  const sortIcon = (col) => {
    if (columnName !== col) return <img src="/image/down-arrow.png" alt="" />;
    return orderBy === "asc" ? (
      <img src="/image/down-arrow.png" alt="" />
    ) : (
      <img src="/image/up-arrow.png" alt="" />
    );
  };

  const sortClass = (col) => (columnName === col ? orderBy : "asc");

  return (
    <main id="admin_page">
      <div className="admin_page_wrap">
        <div className="page_head">
          <a
            onClick={() => navigate("/admin/memberList")}
            style={{ cursor: "pointer" }}
          >
            <h1>회원 리스트</h1>
          </a>
        </div>

        <div className="excel_download">
          <a href="/api/admin/excel/members">회원 엑셀 다운로드</a>
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
                <label htmlFor="memberCompanyName">업체명</label>
                <input
                  type="text"
                  id="memberCompanyName"
                  value={searchMemberCompanyName}
                  onChange={(e) => setSearchMemberCompanyName(e.target.value)}
                />
                <label htmlFor="memberFranCode">가맹점코드</label>
                <input
                  type="text"
                  id="memberFranCode"
                  value={searchMemberFranCode}
                  onChange={(e) => setSearchMemberFranCode(e.target.value)}
                />
              </div>
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
                <label htmlFor="memberName">이름</label>
                <input
                  type="text"
                  id="memberName"
                  value={searchMemberName}
                  onChange={(e) => setSearchMemberName(e.target.value)}
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
                <label htmlFor="memberCompanyTel">업체번호</label>
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
                <label htmlFor="memberTier">등급</label>
                <input
                  type="text"
                  id="memberTier"
                  value={searchMemberTier}
                  onChange={(e) => setSearchMemberTier(e.target.value)}
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
                      <th
                        className="member_tier"
                        onClick={() => handleSort("member_tier")}
                      >
                        <div className={sortClass("member_tier")}>
                          <span>등급</span>
                          {sortIcon("member_tier")}
                        </div>
                      </th>
                      <th
                        className="member_fran_code"
                        onClick={() => handleSort("member_fran_code")}
                      >
                        <div className={sortClass("member_fran_code")}>
                          <span>가맹점코드</span>
                          {sortIcon("member_fran_code")}
                        </div>
                      </th>
                      <th
                        className="member_id"
                        onClick={() => handleSort("member_id")}
                      >
                        <div className={sortClass("member_id")}>
                          <span>아이디</span>
                          {sortIcon("member_id")}
                        </div>
                      </th>
                      <th
                        className="member_name"
                        onClick={() => handleSort("member_name")}
                      >
                        <div className={sortClass("member_name")}>
                          <span>이름</span>
                          {sortIcon("member_name")}
                        </div>
                      </th>
                      <th
                        className="member_company_name"
                        onClick={() => handleSort("member_company_name")}
                      >
                        <div className={sortClass("member_company_name")}>
                          <span>업체명</span>
                          {sortIcon("member_company_name")}
                        </div>
                      </th>
                      <th
                        className="member_tel"
                        onClick={() => handleSort("member_tel")}
                      >
                        <div className={sortClass("member_tel")}>
                          <span>전화번호</span>
                          {sortIcon("member_tel")}
                        </div>
                      </th>
                      <th
                        className="member_company_tel"
                        onClick={() => handleSort("member_company_tel")}
                      >
                        <div className={sortClass("member_company_tel")}>
                          <span>업체번호</span>
                          {sortIcon("member_company_tel")}
                        </div>
                      </th>
                      <th
                        className="member_address"
                        onClick={() => handleSort("member_address")}
                      >
                        <div className={sortClass("member_address")}>
                          <span>주소</span>
                          {sortIcon("member_address")}
                        </div>
                      </th>
                      <th
                        className="member_delivery_address"
                        onClick={() => handleSort("member_delivery_address")}
                      >
                        <div className={sortClass("member_delivery_address")}>
                          <span>배송지</span>
                          {sortIcon("member_delivery_address")}
                        </div>
                      </th>
                      <th
                        className="member_file"
                        onClick={() => handleSort("member_file")}
                      >
                        <div className={sortClass("member_file")}>
                          <span>파일</span>
                          {sortIcon("member_file")}
                        </div>
                      </th>
                      <th
                        className="member_email"
                        onClick={() => handleSort("member_email")}
                      >
                        <div className={sortClass("member_email")}>
                          <span>이메일</span>
                          {sortIcon("member_email")}
                        </div>
                      </th>
                      <th
                        className="member_date"
                        onClick={() => handleSort("member_date")}
                      >
                        <div className={sortClass("member_date")}>
                          <span>가입일</span>
                          {sortIcon("member_date")}
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
                          <a
                            onClick={() =>
                              navigate(
                                `/admin/memberUpdate?memberId=${m.memberId}`,
                              )
                            }
                            style={{ cursor: "pointer" }}
                          >
                            {m.memberId}
                          </a>
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

export default MemberListPage;
