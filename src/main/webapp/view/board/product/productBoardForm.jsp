<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

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
      <form action="${ pageContext.request.contextPath }/board/productBoardPro" name="f" method="post">
        <div class="contentImg">
          <input type="hidden" name="petImg" value="" />
          <img src="${ pageContext.request.contextPath }/view/image/video_cover_pattern.png" alt="img" id="pic" />
          <a href="javascript:win_upload()" class="btn">사진넣기</a>
        </div>
      </form>
    </main>
  </body>
</html>

