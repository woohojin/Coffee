<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>

<body>
<main id="member_signup_page">
    <div class="member_signup_form_wrap">
        <form
                action="${ pageContext.request.contextPath }/member/memberSignUpPro"
                name="f"
                method="post"
                enctype="multipart/form-data"
                class="member_signup_form"
                onsubmit="return passwordInputCheck(this)"
        >
            <p>회원 정보</p>
            <table class="member_signup_form_info">
                <tbody>
                    <tr>
                        <th>
                          <label for="member_id">아이디</label>
                            <div class="form_required">*</div>
                        </th>
                        <td>
                            <input
                              name="memberId"
                              id="member_id"
                              type="text"
                              placeholder="영문 소문자만 가능, 4자리 이상"
                              minlength="4"
                              oninput="idInputCheck(this)"
                              spellcheck="false"
                              required
                            />
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="member_name">이름</label>
                            <div class="form_required">*</div>
                        </th>
                        <td>
                            <input
                              name="memberName"
                              id="member_name"
                              type="text"
                              placeholder="한국어만 입력 가능합니다."
                              oninput="nameInputCheck(this)"
                              spellcheck="false"
                              required
                            />
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="member_password">비밀번호</label>
                            <div class="form_required">*</div>
                        </th>
                        <td>
                            <input
                              name="memberPassword"
                              id="member_password"
                              type="password"
                              placeholder="영문 / 특수문자 / 숫자 중 2가지 이상 조합, 8자 이상"
                              minlength="8"
                              spellcheck="false"
                              required
                            />
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="member_password_check">비밀번호 확인</label>
                            <div class="form_required">*</div>
                        </th>
                        <td>
                            <input
                              name="memberPasswordCheck"
                              id="member_password_check"
                              type="password"
                              placeholder="영문 / 특수문자 / 숫자 중 2가지 이상 조합, 8자 이상"
                              minlength="8"
                              spellcheck="false"
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
                        <div class="form_required">*</div>
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
                                  spellcheck="false"
                                  required
                                />
                            </li>
                            <div class="member_address_button">
                                <button class="input_btn" type="button" onclick="execAddress()">
                                    주소 찾기
                                </button>
                            </div>
                            <li>
                                <input
                                  name="memberDetailAddress"
                                  id="member_detail_address"
                                  type="text"
                                  placeholder="상세주소를 입력해주세요"
                                  spellcheck="false"
                                  required
                                />
                            </li>
                        </ul>
                    </td>
                </tr>
                <tr class="member_delivery_address_wrap">
                    <th>
                        <label for="member_delivery_address">배송지</label>
                        <div class="form_required">*</div>
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
                                  spellcheck="false"
                                  required
                                />
                            </li>
                            <div class="member_delivery_address_button">
                                <button class="input_btn" type="button" onclick="execDeliveryAddress()">
                                    주소 찾기
                                </button>
                                
                            </div>
                            <li>
                                <input
                                  name="memberDetailDeliveryAddress"
                                  id="member_detail_delivery_address"
                                  type="text"
                                  placeholder="상세주소를 입력해주세요"
                                  spellcheck="false"
                                  required
                                />
                            </li>
                            <div class="member_delivery_address_button2">
                                <button class="input_btn" type="button" onclick="sameAddress()">
                                    주소와<br/>동일하게
                                </button>
                            </div>
                        </ul>
                    </td>
                </tr>
                    <tr>
                        <th>
                            <label for="member_tel">개인 연락처</label>
                            <div class="form_required">*</div>
                        </th>
                        <td>
                            <input
                              name="memberTel"
                              id="member_tel"
                              type="tel"
                              maxlength="11"
                              placeholder="예) 01012345678"
                              oninput="maxLengthCheck(this), numberInputCheck(this)"
                              spellcheck="false"
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
                              placeholder="사업자 회원의 경우 꼭 넣어주세요"
                              spellcheck="false"
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
                              placeholder="사업자 회원의 경우 꼭 넣어주세요"
                              oninput="maxLengthCheck(this), numberInputCheck(this)"
                              spellcheck="false"
                            />
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="member_email">이메일</label>
                            <div class="form_required">*</div>
                        </th>
                        <td>
                            <input
                              name="memberEmail"
                              id="member_email"
                              type="email"
                              spellcheck="false"
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
                                  name="file"
                                  id="file"
                                  type="file"
                                  value=""
                                  placeholder="사업자 회원의 경우 꼭 넣어주세요"
                                  spellcheck="false"
                                  onchange="inputFileName()"
                                />
                                <input type="hidden" id="memberFile" name="memberFile" value="">
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="signup">
                <input
                        type="submit"
                        value="회원가입"
                        class="submit_btn"
                />
            </div>
        </form>
    </div>
</main>
<script>
    function sameAddress() {
        const memberAddress = document.getElementById("member_address");
        const memberDetailAddress = document.getElementById("member_detail_address");
        const memberDeliveryAddress = document.getElementById("member_delivery_address");
        const memberDetailDeliveryAddress = document.getElementById("member_detail_delivery_address");
        
        memberDeliveryAddress.value = memberAddress.value;
        memberDetailDeliveryAddress.value = memberDetailAddress.value;
    }
    function inputFileName() {
        const fileInput = document.getElementById("file");
        const file = fileInput.files[0];
        
        if(file) {
            const fileName = file.name;
            const input = document.getElementById("memberFile");
            input.value = fileName;
        }
    }
</script>
</body>
