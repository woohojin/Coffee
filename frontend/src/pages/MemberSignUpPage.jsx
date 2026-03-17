import { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

const passwordRegex = /^(?=(.*[a-zA-Z]))(?=.*\d|.*\W).{8,}$/;
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

function MemberSignUpPage() {
  const navigate = useNavigate();
  const [verifyCode, setVerifyCode] = useState("");
  const [isEmailSending, setIsEmailSending] = useState(false);
  const [countdown, setCountdown] = useState("");
  const countdownRef = useRef(null);
  const [memberAddress, setMemberAddress] = useState("");
  const [memberDetailAddress, setMemberDetailAddress] = useState("");
  const [memberDeliveryAddress, setMemberDeliveryAddress] = useState("");
  const [memberDetailDeliveryAddress, setMemberDetailDeliveryAddress] =
    useState("");

  const sameAddress = () => {
    setMemberDeliveryAddress(memberAddress);
    setMemberDetailDeliveryAddress(memberDetailAddress);
  };

  const sendVerifyEmail = async () => {
    const memberEmail = document.getElementById("member_email").value;
    if (!emailRegex.test(memberEmail)) {
      alert("이메일 형식이 올바르지 않습니다.");
      return;
    }
    if (isEmailSending) {
      alert("1분 뒤에 인증번호를 재전송 할 수 있습니다.");
      return;
    }
    try {
      await axiosInstance.post("/api/member/verifyEmail", { memberEmail });
      alert("인증번호가 전송되었습니다.");
      setIsEmailSending(true);

      let timeLeft = 60;
      setCountdown(`${timeLeft}초`);
      countdownRef.current = setInterval(() => {
        timeLeft--;
        setCountdown(`${timeLeft}초`);
        if (timeLeft <= 0) {
          clearInterval(countdownRef.current);
          setIsEmailSending(false);
          setCountdown("");
        }
      }, 1000);
    } catch (err) {
      alert(err.response?.data?.message || "인증번호 전송 실패");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const form = e.target;

    const memberPassword = form.memberPassword.value;
    const memberPasswordCheck = form.memberPasswordCheck.value;

    if (!passwordRegex.test(memberPassword)) {
      alert(
        "비밀번호는 영문/특수문자/숫자 중 2가지 이상 조합, 8자 이상이어야 합니다.",
      );
      return;
    }
    if (memberPassword !== memberPasswordCheck) {
      alert("비밀번호가 일치하지 않습니다.");
      return;
    }

    try {
      await axiosInstance.post("/api/member/verifyCode", { verifyCode });
    } catch (err) {
      alert(err.response?.data?.message || "이메일 인증을 완료해주세요.");
      return;
    }

    try {
      const formData = new FormData(form);
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
                        id="address-btn"
                        className="input_btn"
                        type="button"
                        onClick={() =>
                          alert("주소 찾기는 추후 구현 예정입니다.")
                        }
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
                        id="delivery-address-btn"
                        className="input_btn"
                        type="button"
                        onClick={() =>
                          alert("주소 찾기는 추후 구현 예정입니다.")
                        }
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
                      onClick={sendVerifyEmail}
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
                    name="verifyCode"
                    className="verify_code"
                    type="text"
                    spellCheck="false"
                    required
                    value={verifyCode}
                    onChange={(e) => setVerifyCode(e.target.value)}
                  />
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
