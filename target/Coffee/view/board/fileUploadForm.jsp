<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/form.css" />
</head>
<body>
    <div class="input_image_form_wrap">
      <form action="${ pageContext.request.contextPath }/board/fileUploadPro" method="post" enctype="multipart/form-data">
        <input type="file" name="file" value="" />
        <input class="submit_btn" type="submit" value="파일 등록" />
      </form>
    </div>
  </body>
</html>