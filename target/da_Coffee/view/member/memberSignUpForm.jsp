<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <script type="text/javascript">
        function win_upload() {
            const op = "width=500, height=150, left=50, top=150";
            open("${pageContext.request.contextPath}/board/fileUploadForm", "", op);
        }
    </script>
</head>
<body>
    <main>
        <form action="${ pageContext.request.contextPath }/member/memberSignUpPro" name="f" method="post">
            <div class="input_image_wrap">
                <div class="input_image">
                    <input type="hidden" name="productImg" value="" />
                    <div class="image_btn">
                        <a href="javascript:win_upload()">사진 넣기</a>
                    </div>
                </div>
            </div>

            <div class="product_form">
                <ul>
                    <li>
                        <label for="member_id">아이디</label>
                        <input
                                name="memberId"
                                id="member_id"
                                class="memberId"
                                type="text"
                        />
                    </li>
                    <li>
                        <label for="member_name">이름</label>
                        <input
                                name="memberName"
                                id="member_name"
                                class="memberName"
                                type="text"
                        />
                    </li>
                    <li>
                        <label for="member_company_name">회사명</label>
                        <input
                                name="memberCompanyName"
                                id="member_company_name"
                                class="memberCompanyName"
                                type="text"
                        />
                    </li>
                    <li>
                        <label for="member_password">비밀번호</label>
                        <input
                                name="memberPassword"
                                id="member_password"
                                class="memberPassword"
                                type="password"
                        />
                    </li>
                    <li>
                        <label for="member_password_check">비밀번호 확인</label>
                        <input
                                name="memberPasswordCheck"
                                id="member_password_check"
                                class="memberPasswordCheck"
                                type="password"
                        />
                    </li>
                    <li>
                        <label for="member_tel">연락처</label>
                        <input
                                name="memberTel"
                                id="member_tel"
                                class="memberTel"
                                type="text"
                        />
                    </li>
                    <li>
                        <label for="member_address">주소</label>
                        <input
                                name="memberAddress"
                                id="member_address"
                                class="memberAddress"
                                type="text"
                        />
                    </li>
                    <li>
                        <label for="member_delivery_address">배송지</label>
                        <input
                                name="memberDeliveryAddress"
                                id="member_delivery_address"
                                class="memberDeliveryAddress"
                                type="text"
                        />
                    </li>
                    <li>
                        <label for="member_email">이메일</label>
                        <input
                                name="memberEmail"
                                id="member_email"
                                class="memberEmail"
                                type="text"
                        />
                    </li>
                    <li>
                        <label for="member_file">파일</label>
                        <input
                                name="memberFile"
                                id="member_file"
                                class="memberFile"
                                type="file"
                        />
                    </li>
                    <li>
                        <label for="member_fran_code">가맹점코드</label>
                        <input
                                name="memberFranCode"
                                id="member_fran_code"
                                class="memberFranCode"
                                type="text"
                        />
                    </li>
                </ul>
            </div>
            <div class="submit">
                <input type="submit" value="로그인" class="submit_btn" />
            </div>
        </form>
    </main>
</body>
</html>

