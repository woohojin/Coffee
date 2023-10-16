<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>

</head>
<body>
    <main>
        <form action="${ pageContext.request.contextPath }/member/memberSignUpPro" name="f" method="post" onsubmit="return inputCheck(this)">
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
                        <input
                                name="memberName"
                                id="member_name"
                                type="text"
                                required
                        />
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
                                <a href="javascript:win_upload()">사진 넣기</a>
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
                <input type="submit" value="회원가입" class="submit_btn" />
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
            if(form.memberPassword.value == "") {
                alert("비밀번호를 입력하세요.");
                form.memberPassword.focus();
                return false;
            } else if (form.memberPassword.value != form.memberPasswordCheck.value) {
                alert("비밀번호 와 재입력 비밀번호가 일치하지 않습니다.");
                form.memberPasswordCheck.value = "";
                form.memberPasswordCheck.focus();
                return false;
            } else if (form.memberPassword.value.length < 8 || form.memberId.value.length < 4) {
                alert("아이디 또는 비밀번호의 자리수를 확인해주세요.");
                form.memberId.focus();
                return false;
            }
            return true;
        }

        function maxLengthCheck(object){
            if (object.value.length > object.maxLength){
                object.value = object.value.slice(0, object.maxLength);
            }
        }
    </script>

    <script>
        const memberIdInput = document.getElementById('member_id');
        const memberNameInput = document.getElementById('member_name');
        const memberTelInput = document.getElementById('member_tel');
        const memberCompanyTelInput = document.getElementById('member_company_tel');

        memberIdInput.addEventListener('input', function() {
            const inputText = this.value;
            const idInputCheck = inputText.replace(/[^a-zA-Z0-9]/g, '');
            this.value = idInputCheck;
            if (inputText !== idInputCheck) {
                alert('영어로 입력해주세요.');
            }
        });

        memberNameInput.addEventListener('input', function() {
            const inputText = this.value;
            const nameInputCheck = inputText.replace(/[^가-힣ㄱ-ㅎㅏ-ㅣ]/g, '');
            this.value = nameInputCheck;
            if (inputText !== nameInputCheck) {
                alert('한국어로 입력해주세요.');
            }
        });

        memberTelInput.addEventListener('input', function() {
            const inputText = this.value;
            const numberInputCheck = inputText.replace(/[^0-9]/g, '');
            this.value = numberInputCheck;
        });
        memberCompanyTelInput.addEventListener('input', function() {
            const inputText = this.value;
            const numberInputCheck = inputText.replace(/[^0-9]/g, '');
            this.value = numberInputCheck;
        })
    </script>

    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script>
        //본 예제에서는 도로명 주소 표기 방식에 대한 법령에 따라, 내려오는 데이터를 조합하여 올바른 주소를 구성하는 방법을 설명합니다.
        function execAddress() {
            new daum.Postcode({
                oncomplete: function(data) {
                    // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                    // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
                    // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                    var roadAddr = data.roadAddress; // 도로명 주소 변수

                    // 우편번호와 주소 정보를 해당 필드에 넣는다.
                    document.getElementById("member_address").value = roadAddr;
                }
            }).open();
        }
        function execDeliveryAddress() {
            new daum.Postcode({
                oncomplete: function(data) {
                    // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                    // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
                    // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                    var roadAddr = data.roadAddress; // 도로명 주소 변수

                    // 우편번호와 주소 정보를 해당 필드에 넣는다.
                    document.getElementById("member_delivery_address").value = roadAddr;
                }
            }).open();
        }
    </script>

</body>
</html>

