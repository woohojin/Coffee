<%@page import="java.io.IOException"%> <%@ page language="java"
																								contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8" />
	<title>Insert title here</title>
</head>
<body>
<script>
	img = opener.document.getElementById("pic");
	files = opener.document.getElementById("files");
	if (img != null) {
		img.src =
			"${ pageContext.request.contextPath }/view/board/files/${filename}"; // 업로드 된 이미지 회원가입 화면에 출력
	}
	if (files != null) {
		files.value = "${filename}";
	}
	const WinClose = (() => {
		window.open("", "_self").close();
	})();
</script>
</body>
</html>
