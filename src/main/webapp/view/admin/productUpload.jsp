<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>

<body>
<main>
  <div class="admin_page_wrap">
    <div class="page_head">
      <a href="">
        <h1>제품 등록</h1>
      </a>
    </div>
  <form
    action="${ pageContext.request.contextPath }/admin/productUploadPro"
    method="post"
    enctype="multipart/form-data"
  >
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
          <label for="product_company">제품 파일 : </label>
          <input
            name="files"
            id="file"
            class="file"
            type="file"
            multiple="multiple"
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
