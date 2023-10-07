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
        <div class="input_image_wrap">
          <div class="input_image">
            <input type="hidden" name="productImg" value="" />
            <div class="input_image_wrap">
                <img src="${ pageContext.request.contextPath }/view/image/video_cover_pattern.png" alt="img" id="pic" />
            </div>
            <div class="image_btn">
              <a href="javascript:win_upload()">사진 넣기</a>
            </div>
          </div>
        </div>

        <div class="product_form">
          <ul>
            <li>
              <label for="product_name">제품명 : </label>
              <input
                      name="productName"
                      id="product_name"
                      class="productName"
                      type="text"
              />
            </li>
            <li>
              <label for="product_price">제품 가격 : </label>
              <input
                      name="productPrice"
                      id="product_price"
                      class="productPrice"
                      type="text"
              />
            </li>
            <li>
              <label for="product_unit">제품 단위 : </label>
              <input
                      name="productUnit"
                      id="product_unit"
                      class="productUnit"
                      type="text"
              />
            </li>
            <li>
              <label for="product_country">제품 원산지 : </label>
              <input
                      name="productCountry"
                      id="product_country"
                      class="productCountry"
                      type="text"
              />
            </li>
            <li>
              <label for="product_species">제품 품종 : </label>
              <input
                      name="productSpecies"
                      id="product_species"
                      class="productSpecies"
                      type="text"
              />
            </li>
            <li>
              <label for="product_company">제품 제조사 : </label>
              <input
                      name="productCompany"
                      id="product_company"
                      class="productCompany"
                      type="text"
              />
            </li>
          </ul>
        </div>
        <div class="submit">
          <input type="submit" value="게시물 작성" class="submit_btn" />
          <div class="btn">
            <a href="">목록</a>
          </div>
        </div>
      </form>
    </main>
  </body>
</html>

