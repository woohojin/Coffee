<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>
<body>
	<div class="w3-container w3-blue">
		<h2>업로드</h2>
	</div>
	<form class="w3-container" action="${ pageContext.request.contextPath }/board/imageInputPro" method="post" enctype="multipart/form-data">
		<input class="w3-input" type="file" name="picture">
		<input class="w3-input" type="submit" value="사진등록" style="cursor:pointer;">
	</form>
</body>
</html>