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
              <label for="member_id">¾ÆÀÌµð</label>
              <input
                name="memberId"
                id="member_id"
                type="text"
                placeholder="4ÀÚ¸® ÀÌ»óÀÇ ¾ÆÀÌµð¸¦ ÀÔ·ÂÇØÁÖ¼¼¿ä."
                minlength="4"
                required
              />
            </li>
            <li>
              <label for="member_name">ÀÌ¸§</label>
              <input name="memberName" id="member_name" type="text" required />
            </li>
            <li>
              <label for="member_company_name">È¸»ç¸í</label>
              <input
                name="memberCompanyName"
                id="member_company_name"
                type="text"
                required
              />
            </li>
            <li>
              <label for="member_password">ºñ¹Ð¹øÈ£</label>
              <input
                name="memberPassword"
                id="member_password"
                type="password"
                placeholder="8ÀÚ¸® ÀÌ»óÀÇ ºñ¹Ð¹øÈ£¸¦ ÀÔ·ÂÇØ ÁÖ¼¼¿ä"
                minlength="8"
                required
              />
            </li>
            <li>
              <label for="member_password_check">ºñ¹Ð¹øÈ£ È®ÀÎ</label>
              <input
                name="memberPasswordCheck"
                id="member_password_check"
                type="password"
                placeholder="8ÀÚ¸® ÀÌ»óÀÇ ºñ¹Ð¹øÈ£¸¦ ÀÔ·ÂÇØ ÁÖ¼¼¿ä"
                minlength="8"
                required
              />
            </li>
            <li>
              <label for="member_tel">¿¬¶ôÃ³</label>
              <input
                name="memberTel"
                id="member_tel"
                type="tel"
                maxlength="11"
                placeholder="¿¹) 01012345678"
                oninput="maxLengthCheck(this)"
                required
              />
            </li>
            <li>
              <label for="member_company_tel">È¸»ç ¿¬¶ôÃ³</label>
              <input
                name="memberCompanyTel"
                id="member_company_tel"
                type="tel"
                maxlength="11"
                placeholder="¿¹) 01012345678"
                oninput="maxLengthCheck(this)"
                required
              />
            </li>
            <li class="member_address_wrap">
              <label for="member_address">ÁÖ¼Ò</label>
              <input
                name="memberAddress"
                id="member_address"
                type="text"
                placeholder="µµ·Î¸íÁÖ¼Ò"
                readonly
                required
              />
              <div class="member_address_button">
                <input
                  class="address_btn"
                  type="button"
                  value="ÁÖ¼Ò Ã£±â"
                  onclick="execAddress()"
                />
              </div>
            </li>
            <li>
              <input
                name="memberDetailAddress"
                id="member_detail_address"
                type="text"
                placeholder="»ó¼¼ÁÖ¼Ò"
                required
              />
            </li>
            <li class="member_delivery_address_wrap">
              <label for="member_delivery_address">¹è¼ÛÁö</label>
              <input
                name="memberDeliveryAddress"
                id="member_delivery_address"
                type="text"
                placeholder="µµ·Î¸íÁÖ¼Ò"
                readonly
                required
              />
              <div class="member_delivery_address_button">
                <input
                  class="address_btn"
                  type="button"
                  value="ÁÖ¼Ò Ã£±â"
                  onclick="execDeliveryAddress()"
                />
              </div>
            </li>
            <li>
              <input
                name="memberDetailDeliveryAddress"
                id="member_detail_delivery_address"
                type="text"
                placeholder="»ó¼¼ÁÖ¼Ò"
                required
              />
            </li>
            <li>
              <label for="member_email">ÀÌ¸ÞÀÏ</label>
              <input
                name="memberEmail"
                id="member_email"
                type="email"
                required
              />
            </li>
            <li>
              <div class="file_input_wrap">
                <label for="file">»ç¾÷ÀÚµî·ÏÁõ »çº»</label>
                <input
                  name="memberFile"
                  id="file"
                  type="text"
                  value=""
                  readonly
                  required
                />
                <div class="input_btn">
                  <a href="javascript:win_upload()">»çÁø ³Ö±â</a>
                </div>
              </div>
            </li>
            <li>
              <label for="member_fran_code">°¡¸ÍÁ¡ÄÚµå</label>
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
          <input type="submit" value="È¸¿ø°¡ÀÔ" class="submit_btn" />
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
      const inputCheck = (form) => {
        if (form.memberPassword.value == "") {
          alert("ºñ¹Ð¹øÈ£¸¦ ÀÔ·ÂÇÏ¼¼¿ä.");
          form.memberPassword.focus();
          return false;
        } else if (
          form.memberPassword.value != form.memberPasswordCheck.value
        ) {
          alert("ºñ¹Ð¹øÈ£ ¿Í ÀçÀÔ·Â ºñ¹Ð¹øÈ£°¡ ÀÏÄ¡ÇÏÁö ¾Ê½À´Ï´Ù.");
          form.memberPasswordCheck.value = "";
          form.memberPasswordCheck.focus();
          return false;
        } else if (
          form.memberPassword.value.length < 8 ||
          form.memberId.value.length < 4
        ) {
          alert("¾ÆÀÌµð ¶Ç´Â ºñ¹Ð¹øÈ£ÀÇ ÀÚ¸®¼ö¸¦ È®ÀÎÇØÁÖ¼¼¿ä.");
          form.memberId.focus();
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
      const memberCompanyTelInput =
        document.getElementById("member_company_tel");

      memberIdInput.addEventListener("input", function () {
        const inputText = this.value;
        const idInputCheck = inputText.replace(/[^a-zA-Z0-9]/g, "");
        this.value = idInputCheck;
        if (inputText !== idInputCheck) {
          alert("¿µ¾î·Î ÀÔ·ÂÇØÁÖ¼¼¿ä.");
        }
      });

      memberNameInput.addEventListener("input", function () {
        const inputText = this.value;
        const nameInputCheck = inputText.replace(/[^°¡-ÆR¤¡-¤¾¤¿-¤Ó]/g, "");
        this.value = nameInputCheck;
        if (inputText !== nameInputCheck) {
          alert("ÇÑ±¹¾î·Î ÀÔ·ÂÇØÁÖ¼¼¿ä.");
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

    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script>
      //º» ¿¹Á¦¿¡¼­´Â µµ·Î¸í ÁÖ¼Ò Ç¥±â ¹æ½Ä¿¡ ´ëÇÑ ¹ý·É¿¡ µû¶ó, ³»·Á¿À´Â µ¥ÀÌÅÍ¸¦ Á¶ÇÕÇÏ¿© ¿Ã¹Ù¸¥ ÁÖ¼Ò¸¦ ±¸¼ºÇÏ´Â ¹æ¹ýÀ» ¼³¸íÇÕ´Ï´Ù.
      function execAddress() {
        new daum.Postcode({
          oncomplete: function (data) {
            // ÆË¾÷¿¡¼­ °Ë»ö°á°ú Ç×¸ñÀ» Å¬¸¯ÇßÀ»¶§ ½ÇÇàÇÒ ÄÚµå¸¦ ÀÛ¼ºÇÏ´Â ºÎºÐ.

            // µµ·Î¸í ÁÖ¼ÒÀÇ ³ëÃâ ±ÔÄ¢¿¡ µû¶ó ÁÖ¼Ò¸¦ Ç¥½ÃÇÑ´Ù.
            // ³»·Á¿À´Â º¯¼ö°¡ °ªÀÌ ¾ø´Â °æ¿ì¿£ °ø¹é('')°ªÀ» °¡Áö¹Ç·Î, ÀÌ¸¦ Âü°íÇÏ¿© ºÐ±â ÇÑ´Ù.
            var roadAddr = data.roadAddress; // µµ·Î¸í ÁÖ¼Ò º¯¼ö

            // ¿ìÆí¹øÈ£¿Í ÁÖ¼Ò Á¤º¸¸¦ ÇØ´ç ÇÊµå¿¡ ³Ö´Â´Ù.
            document.getElementById("member_address").value = roadAddr;
          },
        }).open();
      }
      function execDeliveryAddress() {
        new daum.Postcode({
          oncomplete: function (data) {
            // ÆË¾÷¿¡¼­ °Ë»ö°á°ú Ç×¸ñÀ» Å¬¸¯ÇßÀ»¶§ ½ÇÇàÇÒ ÄÚµå¸¦ ÀÛ¼ºÇÏ´Â ºÎºÐ.

            // µµ·Î¸í ÁÖ¼ÒÀÇ ³ëÃâ ±ÔÄ¢¿¡ µû¶ó ÁÖ¼Ò¸¦ Ç¥½ÃÇÑ´Ù.
            // ³»·Á¿À´Â º¯¼ö°¡ °ªÀÌ ¾ø´Â °æ¿ì¿£ °ø¹é('')°ªÀ» °¡Áö¹Ç·Î, ÀÌ¸¦ Âü°íÇÏ¿© ºÐ±â ÇÑ´Ù.
            var roadAddr = data.roadAddress; // µµ·Î¸í ÁÖ¼Ò º¯¼ö

            // ¿ìÆí¹øÈ£¿Í ÁÖ¼Ò Á¤º¸¸¦ ÇØ´ç ÇÊµå¿¡ ³Ö´Â´Ù.
            document.getElementById("member_delivery_address").value = roadAddr;
          },
        }).open();
      }
    </script>
  </body>
</html>
