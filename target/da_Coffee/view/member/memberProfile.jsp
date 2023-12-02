<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>

<body>
<main id="member_profile_page">
  <div class="page_head">
    <a href="">
      <h1>회원정보 수정</h1>
    </a>
  </div>
  <div class="member_signup_form_wrap">
    <form
      action="${ pageContext.request.contextPath }/member/memberProfilePro"
      name="f"
      method="post"
      class="member_signup_form"
      onsubmit="return profilePasswordInputCheck(this)"
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
              value="${ member.memberId }"
              readonly
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
              value="${ member.memberName }"
              required
            />
          </td>
        </tr>
        <tr>
          <th>
            <label for="member_existing_password">기존비밀번호</label>
            <div class="form_required">*</div>
          </th>
          <td>
            <input
              name="memberExistingPassword"
              id="member_existing_password"
              type="password"
              minlength="8"
              spellcheck="false"
              required
            />
          </td>
        </tr>
        <tr>
          <th>
            <label for="member_password">새 비밀번호</label>
          </th>
          <td>
            <input
              name="memberPassword"
              id="member_password"
              type="password"
              placeholder="영문 / 특수문자 / 숫자 중 2가지 이상 조합, 8자 이상"
              minlength="8"
              spellcheck="false"
            />
          </td>
        </tr>
        <tr>
          <th>
            <label for="member_password_check">새 비밀번호 확인</label>
          </th>
          <td>
            <input
              name="memberPasswordCheck"
              id="member_password_check"
              type="password"
              placeholder="영문 / 특수문자 / 숫자 중 2가지 이상 조합, 8자 이상"
              minlength="8"
              spellcheck="false"
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
                    value="${ member.memberAddress }"
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
                    value="${ member.memberDetailAddress }"
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
                    value="${ member.memberDeliveryAddress }"
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
                    value="${ member.memberDetailDeliveryAddress }"
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
                value="${ member.memberTel }"
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
                value="${ member.memberCompanyName }"
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
                placeholder="예) 01012345678"
                value="${ member.memberCompanyTel }"
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
                value="${ member.memberEmail }"
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
                  name="memberFile"
                  id="file"
                  type="text"
                  value=""
                  placeholder="사진 넣기 버튼을 이용해주세요"
                  value=" ${ member.memberFile }"
                  readonly
                  spellcheck="false"
                />
                <div class="input_btn">
                  <a href="" onclick="fileUpload()">사진 넣기</a>
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="signup" style="justify-content: space-between">
        <div class="input_btn">
          <a href="${ pageContext.request.contextPath }/member/memberDisable">회원탈퇴</a>
        </div>
        <input
          type="submit"
          value="수정"
          class="submit_btn"
        />
      </div>
    </form>
  </div>
</main>
</body>
