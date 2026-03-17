import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";
import { useAddressSearch } from "../hooks/useAddressSearch";
import { useEmailVerify } from "../hooks/useEmailVerify";
import { validatePassword } from "../utils/validation";

function MemberSignUpPage() {
  const navigate = useNavigate();
  const [memberAddress, setMemberAddress] = useState("");
  const [memberDetailAddress, setMemberDetailAddress] = useState("");
  const [memberDeliveryAddress, setMemberDeliveryAddress] = useState("");
  const [memberDetailDeliveryAddress, setMemberDetailDeliveryAddress] =
    useState("");

  const sameAddress = () => {
    setMemberDeliveryAddress(memberAddress);
    setMemberDetailDeliveryAddress(memberDetailAddress);
  };

  const { execAddress, execDeliveryAddress } = useAddressSearch(
    (address) => setMemberAddress(address),
    (address) => setMemberDeliveryAddress(address),
  );

  const {
    verifyCode,
    setVerifyCode,
    countdown,
    sendVerifyEmail,
    checkVerifyCode,
    verifiedEmail,
  } = useEmailVerify();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const form = e.target;

    if (
      !validatePassword(
        form.memberPassword.value,
        form.memberPasswordCheck.value,
      )
    )
      return;

    try {
      const formData = new FormData(form);
      formData.set("memberEmail", verifiedEmail);
      await axiosInstance.post("/member/memberSignUpPro", formData);
      alert("회원가입이 완료되었습니다.");
      navigate("/member/memberSignIn");
    } catch (err) {
      alert(err.response?.data?.message || "회원가입 중 오류가 발생했습니다.");
    }
  };

  return (
    <main id="member_signup_page">
      <div className="member_signup_form_wrap">
        <form onSubmit={handleSubmit} className="member_signup_form">
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
                    name="memberId"
                    id="member_id"
                    type="text"
                    placeholder="영문 소문자만 가능, 4자리 이상, 21자리 미만"
                    minLength="4"
                    maxLength="20"
                    spellCheck="false"
                    required
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
                    spellCheck="false"
                    required
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="member_password">비밀번호</label>
                  <div className="form_required">*</div>
                </th>
                <td>
                  <input
                    name="memberPassword"
                    id="member_password"
                    type="password"
                    placeholder="영문 / 특수문자 / 숫자 중 2가지 이상 조합, 8자 이상"
                    minLength="8"
                    spellCheck="false"
                    required
                  />
                </td>
              </tr>
              <tr>
                <th>
                  <label htmlFor="member_password_check">비밀번호 확인</label>
                  <div className="form_required">*</div>
                </th>
                <td>
                  <input
                    name="memberPasswordCheck"
                    id="member_password_check"
                    type="password"
                    placeholder="영문 / 특수문자 / 숫자 중 2가지 이상 조합, 8자 이상"
                    minLength="8"
                    spellCheck="false"
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
                        readOnly
                        required
                        value={memberAddress}
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
                        spellCheck="false"
                        required
                        value={memberDetailAddress}
                        onChange={(e) => setMemberDetailAddress(e.target.value)}
                      />
                    </li>
                  </ul>
                </td>
              </tr>
              <tr className="member_delivery_address_wrap">
                <th>
                  <label htmlFor="member_delivery_address">배송지</label>
                  <div className="form_required">*</div>
                </th>
                <td>
                  <ul>
                    <li>
                      <input
                        name="memberDeliveryAddress"
                        id="member_delivery_address"
                        type="text"
                        placeholder="주소 찾기 버튼을 이용해 주세요"
                        readOnly
                        spellCheck="false"
                        required
                        value={memberDeliveryAddress}
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
                        spellCheck="false"
                        required
                        value={memberDetailDeliveryAddress}
                        onChange={(e) =>
                          setMemberDetailDeliveryAddress(e.target.value)
                        }
                      />
                    </li>
                    <div className="member_delivery_address_button2">
                      <button
                        id="same-address-btn"
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
                  <div className="form_required">*</div>
                </th>
                <td>
                  <input
                    name="memberTel"
                    id="member_tel"
                    type="tel"
                    maxLength="11"
                    placeholder="'-'없이 입력"
                    spellCheck="false"
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
                    placeholder="사업자 회원의 경우 꼭 넣어주세요"
                    spellCheck="false"
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
                    spellCheck="false"
                  />
                </td>
              </tr>
              <tr className="member_email_wrap">
                <th>
                  <label htmlFor="member_email">이메일</label>
                  <div className="form_required">*</div>
                </th>
                <td>
                  <input
                    name="memberEmail"
                    id="member_email"
                    className="member_email"
                    type="email"
                    spellCheck="false"
                    required
                  />
                  <div className="member_email_verify_button">
                    <button
                      id="send-verify-btn"
                      className="input_btn"
                      type="button"
                      onClick={() =>
                        sendVerifyEmail(
                          document.getElementById("member_email").value,
                        )
                      }
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
                    spellCheck="false"
                    required
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
                  <label htmlFor="signUpFile">사업자등록증 사본</label>
                </th>
                <td>
                  <div className="file_input_wrap">
                    <input name="file" id="signUpFile" type="file" />
                  </div>
                </td>
              </tr>
            </tbody>
          </table>

          <div className="signup">
            <input
              id="sign-up-btn"
              type="submit"
              value="회원가입"
              className="submit_btn"
            />
          </div>
        </form>
      </div>
    </main>
  );
}

export default MemberSignUpPage;
