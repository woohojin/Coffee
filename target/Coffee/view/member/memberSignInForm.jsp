<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <script type="text/javascript">
        function win_upload() {
            const op = "width=500, height=150, left=50, top=150";
            open("${pageContext.request.contextPath}/board/imageInputForm", "", op);
        }
    </script>
</head>
<body>
<main>
    <form action="${ pageContext.request.contextPath }/member/memberSignInPro" name="f" method="post">
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
                    <label for="member_password">비밀번호</label>
                    <input
                            name="memberPassword"
                            id="member_password"
                            class="memberPassword"
                            type="password"
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

