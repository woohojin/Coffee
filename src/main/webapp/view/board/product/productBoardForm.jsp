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
          <input type="hidden" name="productImg" value="" />
          <img src="${ pageContext.request.contextPath }/view/image/video_cover_pattern.png" alt="img" id="pic" />
          <a href="javascript:win_upload()" class="btn">사진넣기</a>
        </div>

        <div class="product_form">
          <ul>
            <li>
              <span class="name">제품명 : </span>
              <input name="petName" class="productName" type="text">
            </li>
            <li>
              <span class="name">제품 가격 : </span>
              <input name="petName" class="productPrice" type="text">
            </li>
            <li>
              <span class="name">제품 단위: </span>
              <input name="petName" class="productUnit" type="text">
            </li>
            <li>
              <span class="name">제품 원산지: </span>
              <input name="petName" class="productCountry" type="text">
            </li>
            <li>
              <span class="name">제품 품종: </span>
              <input name="petName" class="productSpecies" type="text">
            </li>
            <li>
              <span class="name">제품 제조사: </span>
              <input name="petName" class="productCompany" type="text">
            </li>
          </ul>

          <div class="center submit">
            <input type="submit" value="게시물 작성" class="btn"> <div class="btn list">목록</div>
          </div>
        </div>
      </form>
    </main>
  </body>
</html>

