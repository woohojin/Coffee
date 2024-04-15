<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<body>
<main id="member_profile_page">
  <div class="page_head">
    <a href="">
      <h1>회원 정보 수정</h1>
    </a>
  </div>
  <div class="member_signup_form_wrap">
    <form
      action="${ pageContext.request.contextPath }/admin/memberUpdatePro"
      name="f"
      method="post"
      class="member_signup_form"
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
              oninput="idInputCheck(this)"
              spellcheck="false"
              value="${ member.memberId }"
              readonly
              required
              style="background-color: #F4F4F4"
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
              placeholder="한국어만 입력 가능합니다."
              oninput="nameInputCheck(this)"
              spellcheck="false"
              value="${ member.memberName }"
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
          </th>
          <td>
            <input
              name="memberTel"
              id="member_tel"
              type="tel"
              maxlength="11"
              placeholder="'-'없이 입력"
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
              placeholder="사업자 회원의 경우 꼭 넣어주세요"
              value="${ member.memberCompanyTel }"
              oninput="maxLengthCheck(this), numberInputCheck(this)"
              spellcheck="false"
            />
          </td>
        </tr>
        <tr class="member_email_wrap">
          <th>
            <label for="member_email">이메일</label>
          </th>
          <td>
            <input
              name="memberEmail"
              id="member_email"
              class="member_email"
              type="email"
              spellcheck="false"
              readonly
              required
              value="${ member.memberEmail }"
              style="background-color: #F4F4F4"
            />
          </td>
        </tr>
        <tr>
          <th>
            <label for="member_company_name">가맹점코드</label>
          </th>
          <td>
            <input
              name="memberFranCode"
              id="member_fran_code"
              type="text"
              value="${ member.memberFranCode }"
              spellcheck="false"
            />
          </td>
        </tr>
        <tr>
          <th>
            <label for="member_tier">회원 등급</label>
          </th>
          <td>
            <input
              name="memberTier"
              id="member_tier"
              type="text"
              value="${ member.memberTier }"
              spellcheck="false"
              required
            />
          </td>
        </tr>
        </tbody>
      </table>
      <div class="signup">
        <input
          type="submit"
          value="수정하기"
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
<script src="${ pageContext.request.contextPath }/view/js/signUpFormRequirement.js"></script>
</body>
