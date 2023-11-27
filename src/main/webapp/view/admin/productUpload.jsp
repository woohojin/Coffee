<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>

<body>
<main>
  <form
    action="${ pageContext.request.contextPath }/board/productUploadPro"
    name="f"
    method="post"
  >
    <div class="input_image_wrap center">
      <div class="input_image">
        <input type="hidden" multiple="multiple" name="productFile" id="files" value="" />
        <div class="image_wrap">
          <img
            src="${ pageContext.request.contextPath }/view/image/video_cover_pattern.png"
            alt="img"
            id="pic"
          />
          <div class="input_btn">
            <a href="javascript:void(window.open('../board/fileUploadForm', '_blank','width=500, height=150, left=50, top=150'))">사진 넣기</a>
          </div>
        </div>
      </div>
    </div>

    <div class="product_form center">
      <ul>
        <li>
          <label for="product_code">제품코드 : </label>
          <input
            name="productCode"
            id="product_code"
            class="productCode"
            type="text"
          />
        </li>
        <li>
          <label for="product_type">제품종류 : </label>
          <input
            name="productType"
            id="product_type"
            class="productType"
            type="text"
          />
        </li>
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
        <li>
          <div class="submit">
            <input type="submit" value="게시물 작성" class="submit_btn" />
          </div>
        </li>
      </ul>
    </div>
    
  </form>
</main>
</body>
