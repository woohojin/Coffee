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
              <label for="member_id">���̵�</label>
              <input
                name="memberId"
                id="member_id"
                type="text"
                placeholder="4�ڸ� �̻��� ���̵� �Է����ּ���."
                minlength="4"
                required
              />
            </li>
            <li>
              <label for="member_name">�̸�</label>
              <input name="memberName" id="member_name" type="text" required />
            </li>
            <li>
              <label for="member_company_name">ȸ���</label>
              <input
                name="memberCompanyName"
                id="member_company_name"
                type="text"
                required
              />
            </li>
            <li>
              <label for="member_password">��й�ȣ</label>
              <input
                name="memberPassword"
                id="member_password"
                type="password"
                placeholder="8�ڸ� �̻��� ��й�ȣ�� �Է��� �ּ���"
                minlength="8"
                required
              />
            </li>
            <li>
              <label for="member_password_check">��й�ȣ Ȯ��</label>
              <input
                name="memberPasswordCheck"
                id="member_password_check"
                type="password"
                placeholder="8�ڸ� �̻��� ��й�ȣ�� �Է��� �ּ���"
                minlength="8"
                required
              />
            </li>
            <li>
              <label for="member_tel">����ó</label>
              <input
                name="memberTel"
                id="member_tel"
                type="tel"
                maxlength="11"
                placeholder="��) 01012345678"
                oninput="maxLengthCheck(this)"
                required
              />
            </li>
            <li>
              <label for="member_company_tel">ȸ�� ����ó</label>
              <input
                name="memberCompanyTel"
                id="member_company_tel"
                type="tel"
                maxlength="11"
                placeholder="��) 01012345678"
                oninput="maxLengthCheck(this)"
                required
              />
            </li>
            <li class="member_address_wrap">
              <label for="member_address">�ּ�</label>
              <input
                name="memberAddress"
                id="member_address"
                type="text"
                placeholder="���θ��ּ�"
                readonly
                required
              />
              <div class="member_address_button">
                <input
                  class="address_btn"
                  type="button"
                  value="�ּ� ã��"
                  onclick="execAddress()"
                />
              </div>
            </li>
            <li>
              <input
                name="memberDetailAddress"
                id="member_detail_address"
                type="text"
                placeholder="���ּ�"
                required
              />
            </li>
            <li class="member_delivery_address_wrap">
              <label for="member_delivery_address">�����</label>
              <input
                name="memberDeliveryAddress"
                id="member_delivery_address"
                type="text"
                placeholder="���θ��ּ�"
                readonly
                required
              />
              <div class="member_delivery_address_button">
                <input
                  class="address_btn"
                  type="button"
                  value="�ּ� ã��"
                  onclick="execDeliveryAddress()"
                />
              </div>
            </li>
            <li>
              <input
                name="memberDetailDeliveryAddress"
                id="member_detail_delivery_address"
                type="text"
                placeholder="���ּ�"
                required
              />
            </li>
            <li>
              <label for="member_email">�̸���</label>
              <input
                name="memberEmail"
                id="member_email"
                type="email"
                required
              />
            </li>
            <li>
              <div class="file_input_wrap">
                <label for="file">����ڵ���� �纻</label>
                <input
                  name="memberFile"
                  id="file"
                  type="text"
                  value=""
                  readonly
                  required
                />
                <div class="input_btn">
                  <a href="javascript:win_upload()">���� �ֱ�</a>
                </div>
              </div>
            </li>
            <li>
              <label for="member_fran_code">�������ڵ�</label>
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
          <input type="submit" value="ȸ������" class="submit_btn" />
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
          alert("��й�ȣ�� �Է��ϼ���.");
          form.memberPassword.focus();
          return false;
        } else if (
          form.memberPassword.value != form.memberPasswordCheck.value
        ) {
          alert("��й�ȣ �� ���Է� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
          form.memberPasswordCheck.value = "";
          form.memberPasswordCheck.focus();
          return false;
        } else if (
          form.memberPassword.value.length < 8 ||
          form.memberId.value.length < 4
        ) {
          alert("���̵� �Ǵ� ��й�ȣ�� �ڸ����� Ȯ�����ּ���.");
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
          alert("����� �Է����ּ���.");
        }
      });

      memberNameInput.addEventListener("input", function () {
        const inputText = this.value;
        const nameInputCheck = inputText.replace(/[^��-�R��-����-��]/g, "");
        this.value = nameInputCheck;
        if (inputText !== nameInputCheck) {
          alert("�ѱ���� �Է����ּ���.");
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
      //�� ���������� ���θ� �ּ� ǥ�� ��Ŀ� ���� ���ɿ� ����, �������� �����͸� �����Ͽ� �ùٸ� �ּҸ� �����ϴ� ����� �����մϴ�.
      function execAddress() {
        new daum.Postcode({
          oncomplete: function (data) {
            // �˾����� �˻���� �׸��� Ŭ�������� ������ �ڵ带 �ۼ��ϴ� �κ�.

            // ���θ� �ּ��� ���� ��Ģ�� ���� �ּҸ� ǥ���Ѵ�.
            // �������� ������ ���� ���� ��쿣 ����('')���� �����Ƿ�, �̸� �����Ͽ� �б� �Ѵ�.
            var roadAddr = data.roadAddress; // ���θ� �ּ� ����

            // �����ȣ�� �ּ� ������ �ش� �ʵ忡 �ִ´�.
            document.getElementById("member_address").value = roadAddr;
          },
        }).open();
      }
      function execDeliveryAddress() {
        new daum.Postcode({
          oncomplete: function (data) {
            // �˾����� �˻���� �׸��� Ŭ�������� ������ �ڵ带 �ۼ��ϴ� �κ�.

            // ���θ� �ּ��� ���� ��Ģ�� ���� �ּҸ� ǥ���Ѵ�.
            // �������� ������ ���� ���� ��쿣 ����('')���� �����Ƿ�, �̸� �����Ͽ� �б� �Ѵ�.
            var roadAddr = data.roadAddress; // ���θ� �ּ� ����

            // �����ȣ�� �ּ� ������ �ش� �ʵ忡 �ִ´�.
            document.getElementById("member_delivery_address").value = roadAddr;
          },
        }).open();
      }
    </script>
  </body>
</html>
