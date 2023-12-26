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
          <label>제품코드 : </label>
          <input
            name="productCode"
            class="productCode"
            type="text"
          />
        </li>
        <li>
          <label>제품종류 : </label>
          <input
            name="productType"
            class="productType"
            type="text"
          />
        </li>
        <li>
          <label>제품명 : </label>
          <input
            name="productName"
            class="productName"
            type="text"
          />
        </li>
        <li>
          <label>제품 가격 : </label>
          <input
            name="productPrice"
            class="productPrice"
            type="text"
          />
        </li>
        <li>
          <label>제품 단위 : </label>
          <input
            name="productUnit"
            class="productUnit"
            type="text"
          />
        </li>
        <li>
          <label>제품 원산지 : </label>
          <input
            name="productCountry"
            class="productCountry"
            type="text"
          />
        </li>
        <li>
          <label>제품 품종 : </label>
          <input
            name="productSpecies"
            class="productSpecies"
            type="text"
          />
        </li>
        <li>
          <label>제품 제조사 : </label>
          <input
            name="productCompany"
            class="productCompany"
            type="text"
          />
        </li>
        <li>
          <label>제품 등급 : </label>
          <input
            name="productTier"
            class="productTier"
            type="text"
          />
        </li>
        <li>
          <label>등록자 : </label>
          <input
            name="productRegisterName"
            class="productRegisterName"
            type="text"
          />
        </li>
        <li>
          <label>제품 파일 : </label>
          <input
            name="files"
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
