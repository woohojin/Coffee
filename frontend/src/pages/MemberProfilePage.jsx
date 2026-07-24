import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";
import { useAddressSearch } from "../hooks/useAddressSearch";
import { useEmailVerify } from "../hooks/useEmailVerify";
import { validateProfile } from "../utils/validation";

function MemberProfilePage() {
  const navigate = useNavigate();
  const [member, setMember] = useState(null);
  const [form, setForm] = useState({
    memberName: "",
    memberExistingPassword: "",
    memberPassword: "",
    memberPasswordCheck: "",
    memberAddress: "",
    memberDetailAddress: "",
    memberDeliveryAddress: "",
    memberDetailDeliveryAddress: "",
    memberTel: "",
    memberCompanyName: "",
    memberCompanyTel: "",
    memberEmail: "",
    memberFile: "",
  });
  const [file, setFile] = useState(null);
  const {
    verifyCode,
    setVerifyCode,
    countdown,
    sendVerifyEmail,
    checkVerifyCode,
    verifiedEmail,
  } = useEmailVerify();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e) => {
    const selected = e.target.files[0];
    if (selected) {
      setFile(selected);
      setForm((prev) => ({ ...prev, memberFile: selected.name }));
    }
  };

  const sameAddress = () => {
    setForm((prev) => ({
      ...prev,
      memberDeliveryAddress: prev.memberAddress,
      memberDetailDeliveryAddress: prev.memberDetailAddress,
    }));
  };

  const { execAddress, execDeliveryAddress } = useAddressSearch(
    (address) => setForm((prev) => ({ ...prev, memberAddress: address })),
    (address) =>
      setForm((prev) => ({ ...prev, memberDeliveryAddress: address })),
  );

  // 회원 정보 조회
  useEffect(() => {
    axiosInstance.get("/api/member/profile").then((res) => {
      const m = res.data.data;
      setMember(m);
      setForm((prev) => ({
        ...prev,
        memberName: m.memberName || "",
        memberAddress: m.memberAddress || "",
        memberDetailAddress: m.memberDetailAddress || "",
        memberDeliveryAddress: m.memberDeliveryAddress || "",
        memberDetailDeliveryAddress: m.memberDetailDeliveryAddress || "",
        memberTel: m.memberTel || "",
        memberCompanyName: m.memberCompanyName || "",
        memberCompanyTel: m.memberCompanyTel || "",
        memberEmail: m.memberEmail || "",
        memberFile: m.memberFile || "",
      }));
    });
  }, []);

  // 폼 제출
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateProfile(form, member.memberEmail, verifiedEmail)) return;

    // multipart/form-data 전송
    const formData = new FormData();
    Object.entries(form).forEach(([key, value]) => {
      formData.append(key, value ?? "");
    });
    formData.set("memberEmail", verifiedEmail || form.memberEmail);
    if (file) formData.append("file", file);

    try {
      await axiosInstance.put("/api/member/profile", formData);
      alert("회원 정보가 수정되었습니다.");
      navigate("/member/memberMyPage");
    } catch (err) {
      alert(err.response?.data?.message || "수정 실패");
    }
  };

  if (!member) return <div>로딩중...</div>;

  return (
    <main id="member_profile_page">
      <div className="page_head">
        <h1>회원정보 수정</h1>
      </div>
      <div className="member_signup_form_wrap">
        <form
          onSubmit={handleSubmit}
          className="member_signup_form"
          id="profileForm"
        >
          <p>회원 정보</p>
          <table className="member_signup_form_info">
            <tbody>
              <tr>
                <th>
                  <label htmlFor="member_id">아이디</label>
                  <div className="form_required">*</div>
                </th>
                <td>
                  <input
                    id="member_id"
                    type="text"
                    value={member.memberId}
                    readOnly
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="member_name">이름</label>
                  <div className="form_required">*</div>
                </th>
                <td>
                  <input
                    name="memberName"
                    id="member_name"
                    type="text"
                    placeholder="한국어만 입력 가능합니다."
                    value={form.memberName}
                    onChange={handleChange}
                    required
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="member_existing_password">기존비밀번호</label>
                  <div className="form_required">*</div>{" "}
                </th>
                <td>
                  <input
                    name="memberExistingPassword"
                    id="member_existing_password"
                    type="password"
                    minLength="8"
                    value={form.memberExistingPassword}
                    onChange={handleChange}
                    required
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="member_password">새 비밀번호</label>
                </th>
                <td>
                  <input
                    name="memberPassword"
                    id="member_password"
                    type="password"
                    minLength="8"
                    placeholder="영문 / 특수문자 / 숫자 중 2가지 이상 조합, 8자 이상"
                    value={form.memberPassword}
                    onChange={handleChange}
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="member_password_check">
                    새 비밀번호 확인
                  </label>
                </th>
                <td>
                  <input
                    name="memberPasswordCheck"
                    id="member_password_check"
                    type="password"
                    minLength="8"
                    placeholder="영문 / 특수문자 / 숫자 중 2가지 이상 조합, 8자 이상"
                    value={form.memberPasswordCheck}
                    onChange={handleChange}
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
                  <div className="form_required">*</div>
                </th>
                <td>
                  <ul>
                    <li>
                      <input
                        name="memberAddress"
                        id="member_address"
                        type="text"
                        placeholder="주소 찾기 버튼을 이용해 주세요"
                        value={form.memberAddress}
                        readOnly
                        required
                      />
                    </li>

                    <div className="member_address_button">
                      <button
                        type="button"
                        className="input_btn"
                        onClick={execAddress}
                      >
                        주소 찾기
                      </button>
                    </div>

                    <li>
                      <input
                        name="memberDetailAddress"
                        id="member_detail_address"
                        type="text"
                        placeholder="상세주소를 입력해주세요"
                        value={form.memberDetailAddress}
                        onChange={handleChange}
                        required
                      />
                    </li>
                  </ul>
                </td>
              </tr>
              <tr className="member_delivery_address_wrap">
                <th>
                  <label htmlFor="member_delivery_address">배송지</label>
                  <div className="form_required">*</div>{" "}
                </th>
                <td>
                  <ul>
                    <li>
                      <input
                        name="memberDeliveryAddress"
                        id="member_delivery_address"
                        type="text"
                        placeholder="주소 찾기 버튼을 이용해 주세요"
                        value={form.memberDeliveryAddress}
                        readOnly
                        required
                      />
                    </li>

                    <div className="member_delivery_address_button">
                      <button
                        type="button"
                        className="input_btn"
                        onClick={execDeliveryAddress}
                      >
                        주소 찾기
                      </button>
                    </div>

                    <li>
                      <input
                        name="memberDetailDeliveryAddress"
                        id="member_detail_delivery_address"
                        type="text"
                        placeholder="상세주소를 입력해주세요"
                        value={form.memberDetailDeliveryAddress}
                        onChange={handleChange}
                        required
                      />
                    </li>

                    <div className="member_delivery_address_button2">
                      <button
                        className="input_btn"
                        type="button"
                        onClick={sameAddress}
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
                  <label htmlFor="member_tel">개인 연락처</label>
                  <div className="form_required">*</div>{" "}
                </th>
                <td>
                  <input
                    name="memberTel"
                    id="member_tel"
                    type="tel"
                    maxLength="11"
                    placeholder="'-'없이 입력"
                    value={form.memberTel}
                    onChange={handleChange}
                    required
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="member_company_name">업체명</label>
                </th>
                <td>
                  <input
                    name="memberCompanyName"
                    id="member_company_name"
                    type="text"
                    value={form.memberCompanyName}
                    onChange={handleChange}
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="member_company_tel">업체 연락처</label>
                </th>
                <td>
                  <input
                    name="memberCompanyTel"
                    id="member_company_tel"
                    type="tel"
                    maxLength="11"
                    placeholder="사업자 회원의 경우 꼭 넣어주세요"
                    value={form.memberCompanyTel}
                    onChange={handleChange}
                  />
                </td>
              </tr>
              <tr className="member_email_wrap">
                <th>
                  <label htmlFor="member_email">이메일</label>
                  <div className="form_required">*</div>{" "}
                </th>
                <td>
                  <input
                    name="memberEmail"
                    id="member_email"
                    className="member_email"
                    type="email"
                    value={form.memberEmail}
                    onChange={handleChange}
                    required
                  />
                  <div className="member_email_verify_button">
                    <button
                      id="send-verify-btn"
                      className="input_btn"
                      type="button"
                      onClick={() => sendVerifyEmail(form.memberEmail)}
                    >
                      인증번호 발송
                    </button>
                  </div>
                </td>
              </tr>
              <tr>
                <th>
                  <label>인증번호</label>
                </th>
                <td>
                  <input
                    type="text"
                    className="verify_code"
                    placeholder="이메일 변경 시 인증바랍니다."
                    value={verifyCode}
                    onChange={(e) => setVerifyCode(e.target.value)}
                  />
                  <div className="member_email_verify_button">
                    <button
                      id="verify-btn"
                      className="input_btn"
                      type="button"
                      onClick={() => checkVerifyCode()}
                    >
                      인증하기
                    </button>
                  </div>
                  <div className="remain_time">
                    <span className="countdown">{countdown}</span>
                  </div>
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="file">사업자등록증 사본</label>
                </th>
                <td>
                  <div className="file_input_wrap">
                    <input id="file" type="file" onChange={handleFileChange} />
                  </div>
                </td>
              </tr>
            </tbody>
          </table>

          <div className="signup" style={{ justifyContent: "space-between" }}>
            <div className="input_btn">
              <Link to="/member/memberWithdrawal">회원탈퇴</Link>
            </div>
            <input type="submit" value="수정" className="submit_btn" />
          </div>
        </form>
      </div>
    </main>
  );
}

export default MemberProfilePage;
