import { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";
import { useAddressSearch } from "../hooks/useAddressSearch";

const MEMBER_TIER = {
  0: "미승인",
  1: "임대",
  2: "미임대",
  3: "카페고객",
  9: "관리자",
};

function MemberUpdatePage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const memberId = searchParams.get("memberId");

  const [memberName, setMemberName] = useState("");
  const [memberTel, setMemberTel] = useState("");
  const [memberCompanyName, setMemberCompanyName] = useState("");
  const [memberCompanyTel, setMemberCompanyTel] = useState("");
  const [memberEmail, setMemberEmail] = useState("");
  const [memberFranCode, setMemberFranCode] = useState("");
  const [memberTier, setMemberTier] = useState("");
  const [memberAddress, setMemberAddress] = useState("");
  const [memberDetailAddress, setMemberDetailAddress] = useState("");
  const [memberDeliveryAddress, setMemberDeliveryAddress] = useState("");
  const [memberDetailDeliveryAddress, setMemberDetailDeliveryAddress] =
    useState("");

  const { execAddress, execDeliveryAddress } = useAddressSearch(
    (address) => setMemberAddress(address),
    (address) => setMemberDeliveryAddress(address),
  );

  const handleSameAddress = () => {
    setMemberDeliveryAddress(memberAddress);
    setMemberDetailDeliveryAddress(memberDetailAddress);
  };

  useEffect(() => {
    if (!memberId) return;

    axiosInstance
      .get(`/api/admin/members/${memberId}`)
      .then((res) => {
        const m = res.data.data;
        setMemberName(m.memberName || "");
        setMemberTel(m.memberTel || "");
        setMemberCompanyName(m.memberCompanyName || "");
        setMemberCompanyTel(m.memberCompanyTel || "");
        setMemberEmail(m.memberEmail || "");
        setMemberFranCode(m.memberFranCode || "");
        setMemberTier(String(m.memberTier));
        setMemberAddress(m.memberAddress || "");
        setMemberDetailAddress(m.memberDetailAddress || "");
        setMemberDeliveryAddress(m.memberDeliveryAddress || "");
        setMemberDetailDeliveryAddress(m.memberDetailDeliveryAddress || "");
      })
      .catch((err) => console.error("회원 조회 실패:", err));
  }, [memberId]);

  const handleDisable = async () => {
    if (!confirm("멤버 비활성화 상태가 변경됩니다. 진행하시겠습니까?")) return;

    try {
      const res = await axiosInstance.patch(
        `/api/admin/members/${memberId}/disable`,
      );
      if (res.data.success) {
        alert("멤버 비활성화 상태 수정 성공");
        navigate("/admin/memberList");
      } else {
        alert(res.data.message || "수정에 실패했습니다.");
      }
    } catch (err) {
      alert(err.response?.data?.message || "수정 중 오류가 발생했습니다.");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await axiosInstance.put(`/api/admin/members/${memberId}`, {
        memberId,
        memberName,
        memberCompanyName,
        memberTel,
        memberCompanyTel,
        memberAddress,
        memberDetailAddress,
        memberDeliveryAddress,
        memberDetailDeliveryAddress,
        memberEmail,
        memberFranCode,
        memberTier: parseInt(memberTier),
      });

      if (res.data.success) {
        alert("회원 정보가 수정되었습니다.");
        navigate("/admin/memberList");
      } else {
        alert(res.data.message || "수정에 실패했습니다.");
      }
    } catch (err) {
      alert(err.response?.data?.message || "수정 중 오류가 발생했습니다.");
    }
  };

  return (
    <main id="member_profile_page">
      <div className="page_head">
        <a
          onClick={() => navigate(`/admin/memberUpdate?memberId=${memberId}`)}
          style={{ cursor: "pointer" }}
        >
          <h1>회원 정보 수정</h1>
        </a>
      </div>

      <div className="member_signup_form_wrap">
        <form onSubmit={handleSubmit} className="member_signup_form">
          <p>회원 정보</p>
          <table className="member_signup_form_info">
            <tbody>
              <tr>
                <th>
                  <label htmlFor="member_id">아이디</label>
                </th>
                <td>
                  <input
                    id="member_id"
                    type="text"
                    value={memberId || ""}
                    readOnly
                    style={{ backgroundColor: "#F4F4F4" }}
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="member_name">이름</label>
                </th>
                <td>
                  <input
                    id="member_name"
                    type="text"
                    placeholder="한국어만 입력 가능합니다."
                    value={memberName}
                    onChange={(e) => setMemberName(e.target.value)}
                    required
                  />
                </td>
              </tr>
            </tbody>
          </table>

          <p>기타 정보</p>
          <table className="member_signup_form_personal_info">
            <tbody>
              <tr className="member_address_wrap">
                <th>
                  <label htmlFor="member_address">주소</label>
                </th>
                <td>
                  <ul>
                    <li>
                      <input
                        id="member_address"
                        type="text"
                        placeholder="주소 찾기 버튼을 이용해 주세요"
                        value={memberAddress}
                        readOnly
                        required
                      />
                    </li>
                    <div className="member_address_button">
                      <button
                        id="address-btn"
                        className="input_btn"
                        type="button"
                        onClick={execAddress}
                      >
                        주소 찾기
                      </button>
                    </div>
                    <li>
                      <input
                        id="member_detail_address"
                        type="text"
                        placeholder="상세주소를 입력해주세요"
                        value={memberDetailAddress}
                        onChange={(e) => setMemberDetailAddress(e.target.value)}
                        required
                      />
                    </li>
                  </ul>
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="member_tel">개인 연락처</label>
                </th>
                <td>
                  <input
                    id="member_tel"
                    type="tel"
                    maxLength="11"
                    placeholder="'-'없이 입력"
                    value={memberTel}
                    onChange={(e) => setMemberTel(e.target.value)}
                    required
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="member_company_name">회사명</label>
                </th>
                <td>
                  <input
                    id="member_company_name"
                    type="text"
                    value={memberCompanyName}
                    onChange={(e) => setMemberCompanyName(e.target.value)}
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="member_company_tel">회사 연락처</label>
                </th>
                <td>
                  <input
                    id="member_company_tel"
                    type="tel"
                    maxLength="11"
                    placeholder="사업자 회원의 경우 꼭 넣어주세요"
                    value={memberCompanyTel}
                    onChange={(e) => setMemberCompanyTel(e.target.value)}
                  />
                </td>
              </tr>
              <tr className="member_email_wrap">
                <th>
                  <label htmlFor="member_email">이메일</label>
                </th>
                <td>
                  <input
                    id="member_email"
                    type="email"
                    value={memberEmail}
                    readOnly
                    style={{ backgroundColor: "#F4F4F4" }}
                    required
                  />
                </td>
              </tr>
              <tr className="member_delivery_address_wrap">
                <th>
                  <label htmlFor="member_delivery_address">배송지</label>
                </th>
                <td>
                  <ul>
                    <li>
                      <input
                        id="member_delivery_address"
                        type="text"
                        placeholder="주소 찾기 버튼을 이용해 주세요"
                        value={memberDeliveryAddress}
                        readOnly
                        required
                      />
                    </li>
                    <div className="member_delivery_address_button">
                      <button
                        id="delivery-address-btn"
                        className="input_btn"
                        type="button"
                        onClick={execDeliveryAddress}
                      >
                        주소 찾기
                      </button>
                    </div>
                    <li>
                      <input
                        id="member_detail_delivery_address"
                        type="text"
                        placeholder="상세주소를 입력해주세요"
                        value={memberDetailDeliveryAddress}
                        onChange={(e) =>
                          setMemberDetailDeliveryAddress(e.target.value)
                        }
                        required
                      />
                    </li>
                    <div className="member_delivery_address_button2">
                      <button
                        id="same-address-btn"
                        className="input_btn"
                        type="button"
                        onClick={handleSameAddress}
                      >
                        주소와
                        <br />
                        동일하게
                      </button>
                    </div>
                  </ul>
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="member_fran_code">가맹점코드</label>
                </th>
                <td>
                  <input
                    id="member_fran_code"
                    type="text"
                    value={memberFranCode}
                    onChange={(e) => setMemberFranCode(e.target.value)}
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="member_tier">회원 등급</label>
                </th>
                <td>
                  <select
                    id="member_tier"
                    value={memberTier}
                    onChange={(e) => setMemberTier(e.target.value)}
                    required
                  >
                    {Object.entries(MEMBER_TIER).map(([code, label]) => (
                      <option key={code} value={code}>
                        {label}
                      </option>
                    ))}
                  </select>
                </td>
              </tr>
            </tbody>
          </table>

          <div className="signup" style={{ justifyContent: "space-between" }}>
            <button
              type="button"
              className="submit_btn"
              onClick={handleDisable}
            >
              비활성화
            </button>
            <input type="submit" value="수정하기" className="submit_btn" />
          </div>
        </form>
      </div>
    </main>
  );
}

export default MemberUpdatePage;
