<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
"http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
  <head> </head>
  <body>
    <main>
      <form
        action="${ pageContext.request.contextPath }/member/memberSignUpPro"
        name="f"
        method="post"
        onsubmit="return inputCheck(this)"
      >
        <div class="product_form">
          <ul>
            <li>
              <label for="member_id">아이디</label>
              <input
                name="memberId"
                id="member_id"
                type="text"
                placeholder="4자리 이상의 아이디를 입력해주세요."
                minlength="4"
                required
              />
            </li>
            <li>
              <label for="member_name">이름</label>
              <input name="memberName" id="member_name" type="text" required />
            </li>
            <li>
              <label for="member_company_name">회사명</label>
              <input
                name="memberCompanyName"
                id="member_company_name"
                type="text"
                required
              />
            </li>
            <li>
              <label for="member_password">비밀번호</label>
              <input
                name="memberPassword"
                id="member_password"
                type="password"
                placeholder="8자리 이상의 비밀번호를 입력해 주세요"
                minlength="8"
                required
              />
            </li>
            <li>
              <label for="member_password_check">비밀번호 확인</label>
              <input
                name="memberPasswordCheck"
                id="member_password_check"
                type="password"
                placeholder="8자리 이상의 비밀번호를 입력해 주세요"
                minlength="8"
                required
              />
            </li>
            <li>
              <label for="member_tel">연락처</label>
              <input
                name="memberTel"
                id="member_tel"
                type="tel"
                maxlength="11"
                placeholder="예) 01012345678"
                oninput="maxLengthCheck(this)"
                required
              />
            </li>
            <li>
              <label for="member_company_tel">회사 연락처</label>
              <input
                name="memberCompanyTel"
                id="member_company_tel"
                type="tel"
                maxlength="11"
                placeholder="예) 01012345678"
                oninput="maxLengthCheck(this)"
                required
              />
            </li>
            <li class="member_address_wrap">
              <label for="member_address">주소</label>
              <input
                name="memberAddress"
                id="member_address"
                type="text"
                placeholder="도로명주소"
                readonly
                required
              />
              <div class="member_address_button">
                <input
                  class="address_btn"
                  type="button"
                  value="주소 찾기"
                  onclick="execAddress()"
                />
              </div>
            </li>
            <li>
              <input
                name="memberDetailAddress"
                id="member_detail_address"
                type="text"
                placeholder="상세주소"
                required
              />
            </li>
            <li class="member_delivery_address_wrap">
              <label for="member_delivery_address">배송지</label>
              <input
                name="memberDeliveryAddress"
                id="member_delivery_address"
                type="text"
                placeholder="도로명주소"
                readonly
                required
              />
              <div class="member_delivery_address_button">
                <input
                  class="address_btn"
                  type="button"
                  value="주소 찾기"
                  onclick="execDeliveryAddress()"
                />
              </div>
            </li>
            <li>
              <input
                name="memberDetailDeliveryAddress"
                id="member_detail_delivery_address"
                type="text"
                placeholder="상세주소"
                required
              />
            </li>
            <li>
              <label for="member_email">이메일</label>
              <input
                name="memberEmail"
                id="member_email"
                type="email"
                required
              />
            </li>
            <li>
              <div class="file_input_wrap">
                <label for="file">사업자등록증 사본</label>
                <input
                  name="memberFile"
                  id="file"
                  type="text"
                  value=""
                  readonly
                  required
                />
                <div class="input_btn">
                  <a href="" onclick="imageUpload()">사진 넣기</a>
                </div>
              </div>
            </li>
            <li>
              <label for="member_fran_code">가맹점코드</label>
              <input
                name="memberFranCode"
                id="member_fran_code"
                type="text"
                required
              />
            </li>
          </ul>
        </div>
        <div class="submit">
          <input
            type="submit"
            value="회원가입"
            class="submit_btn"
            onclick="return inputCheck()"
          />
        </div>
      </form>
    </main>

    <script>
      function win_upload() {
        const op = "width=500, height=150, left=50, top=150";
        open("${pageContext.request.contextPath}/board/fileUploadForm", "", op);
      }
    </script>

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
  </body>
</html>
