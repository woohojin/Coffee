<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head> </head>
<body>
<main>
    <div class="member_signup_form_wrap">
        <form
                action="${ pageContext.request.contextPath }/member/memberSignUpPro"
                name="f"
                method="post"
                class="member_signup_form"
                onsubmit="return inputCheck(this)"
        >
            <p>회원 정보</p>
            <table class="member_signup_form_info">
                <tbody>
                    <tr>
                        <th>
                          <label for="member_id">아이디</label>
                        </th>
                        <td>
                            <input
                              name="memberId"
                              id="member_id"
                              type="text"
                              placeholder="영문 소문자만 가능, 4자리 이상"
                              minlength="4"
                              required
                            />
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="member_name">이름</label>
                        </th>
                        <td>
                            <input
                              name="memberName"
                              id="member_name"
                              type="text"
                              placeholder="한국어만"
                              required
                            />
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="member_password">비밀번호</label>
                        </th>
                        <td>
                            <input
                              name="memberPassword"
                              id="member_password"
                              type="password"
                              placeholder="영문 / 특수문자 / 숫자 중 2가지 이상 조합, 8자 이상"
                              minlength="8"
                              required
                            />
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="member_password_check">비밀번호 확인</label>
                        </th>
                        <td>
                            <input
                              name="memberPasswordCheck"
                              id="member_password_check"
                              type="password"
                              placeholder="영문 / 특수문자 / 숫자 중 2가지 이상 조합, 8자 이상"
                              minlength="8"
                              required
                            />
                        </td>
                    </tr>
                </tbody>
            </table>
            <p>기타 정보</p>
            <table class="member_signup_form_personal_info">
                <tbody>
                <tr class="member_address_wrap">
                    <th>
                        <label for="member_address">주소</label>
                    </th>
                    <td>
                        <ul>
                            <li>
                                <input
                                  name="memberAddress"
                                  id="member_address"
                                  type="text"
                                  placeholder="주소 찾기 버튼을 이용해 주세요"
                                  readonly
                                  required
                                />
                            </li>
                            <div class="member_address_button">
                                <button class="address_btn" type="button" onclick="execAddress()">
                                    주소 찾기
                                </button>
                            </div>
                            <li>
                                <input
                                  name="memberDetailAddress"
                                  id="member_detail_address"
                                  type="text"
                                  placeholder="상세주소를 입력해주세요"
                                  required
                                />
                            </li>
                        </ul>
                    </td>
                </tr>
                <tr class="member_delivery_address_wrap">
                    <th>
                        <label for="member_delivery_address">배송지</label>
                    </th>
                    <td>
                        <ul>
                            <li>
                                <input
                                  name="memberDeliveryAddress"
                                  id="member_delivery_address"
                                  type="text"
                                  placeholder="주소 찾기 버튼을 이용해 주세요"
                                  readonly
                                  required
                                />
                            </li>
                            <div class="member_delivery_address_button">
                                <button class="address_btn" type="button" onclick="execDeliveryAddress()">
                                    주소 찾기
                                </button>
                                
                            </div>
                            <li>
                                <input
                                  name="memberDetailDeliveryAddress"
                                  id="member_detail_delivery_address"
                                  type="text"
                                  placeholder="상세주소를 입력해주세요"
                                  required
                                />
                            </li>
                        </ul>
                    </td>
                </tr>
                    <tr>
                        <th>
                            <label for="member_tel">개인 연락처</label>
                        </th>
                        <td>
                            <input
                              name="memberTel"
                              id="member_tel"
                              type="tel"
                              maxlength="11"
                              placeholder="예) 01012345678"
                              oninput="maxLengthCheck(this)"
                              required
                            />
                        </td>
                    </tr>
                    
                    <tr>
                        <th>
                            <label for="member_company_name">회사명</label>
                        </th>
                        <td>
                            <input
                              name="memberCompanyName"
                              id="member_company_name"
                              type="text"
                              required
                            />
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="member_company_tel">회사 연락처</label>
                        </th>
                        <td>
                            <input
                              name="memberCompanyTel"
                              id="member_company_tel"
                              type="tel"
                              maxlength="11"
                              placeholder="예) 01012345678"
                              oninput="maxLengthCheck(this)"
                              required
                            />
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="member_email">이메일</label>
                        </th>
                        <td>
                            <input
                              name="memberEmail"
                              id="member_email"
                              type="email"
                              required
                            />
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="file">사업자등록증 사본</label>
                        </th>
                        <td>
                            <div class="file_input_wrap">
                                <input
                                  name="memberFile"
                                  id="file"
                                  type="text"
                                  value=""
                                  placeholder="사진 넣기 버튼을 이용해주세요"
                                  readonly
                                  required
                                />
                                <div class="input_btn">
                                    <a href="" onclick="imageUpload()">사진 넣기</a>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="member_fran_code">가맹점코드</label>
                        </th>
                        <td>
                            <input
                              name="memberFranCode"
                              id="member_fran_code"
                              type="text"
                              required
                            />
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="signup">
                <input
                        type="submit"
                        value="회원가입"
                        class="submit_btn"
                        onclick="return checkPassword()"
                />
            </div>
        </form>
    </div>
</main>

<script>
    const checkPassword = (form) => {
        if (form.memberPassword.value != form.memberPasswordCheck.value) {
            alert("비밀번호와 재입력 비밀번호가 일치하지 않습니다.");
            form.memberPasswordCheck.value = "";
            form.memberPasswordCheck.focus();
            return false;
        }
        return true;
    };
    
    function maxLengthCheck(object) {
        if (object.value.length > object.maxLength) {
            object.value = object.value.slice(0, object.maxLength);
        }
    }
</script>

<script>
    const memberIdInput = document.getElementById("member_id");
    const memberNameInput = document.getElementById("member_name");
    const memberTelInput = document.getElementById("member_tel");
    const memberCompanyTelInput = document.getElementById("member_company_tel");
    
    memberIdInput.addEventListener("input", function () {
        const inputText = this.value;
        const idInputCheck = inputText.replace(/[^a-zA-Z0-9]/g, "");
        this.value = idInputCheck;
        if (inputText !== idInputCheck) {
            alert("영어로 입력해주세요.");
        }
    });
    
    memberNameInput.addEventListener("input", function () {
        const inputText = this.value;
        const nameInputCheck = inputText.replace(/[^가-힣ㄱ-ㅎㅏ-ㅣ]/g, "");
        this.value = nameInputCheck;
        if (inputText !== nameInputCheck) {
            alert("한국어로 입력해주세요.");
        }
    });
    
    memberTelInput.addEventListener("input", function () {
        const inputText = this.value;
        const numberInputCheck = inputText.replace(/[^0-9]/g, "");
        this.value = numberInputCheck;
    });
    memberCompanyTelInput.addEventListener("input", function () {
        const inputText = this.value;
        const numberInputCheck = inputText.replace(/[^0-9]/g, "");
        this.value = numberInputCheck;
    });
</script>
</body>
</html>
