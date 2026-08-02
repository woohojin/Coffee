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

  const sortIcon = () => <img src="/image/down-arrow.png" alt="" />;

  return (
    <main id="admin_page">
      <div className="admin_page_wrap">
        <div className="page_head">
          <a
            onClick={() => navigate("/admin/memberDisableUpdate")}
            style={{ cursor: "pointer" }}
          >
            <h1>비활성화 회원 리스트</h1>
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
                          <a
                            onClick={() =>
                              navigate(
                                `/admin/memberDisableProcess?memberId=${m.memberId}`,
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
      </div>
    </main>
  );
}

export default MemberDisablePage;
