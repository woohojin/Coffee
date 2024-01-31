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
            <label>제품 종류 : </label>
            <select name="productType" class="product_type" required>
              <option value="0">원두</option>
              <option value="1">커피믹스</option>
              <option value="2">카페용품</option>
              <option value="3">임대머신</option>
            </select>
          </li>
          <li>
            <label>제품 코드 : </label>
            <input
              name="productCode"
              class="product_code"
              type="text"
              required
            />
          </li>
          <li>
            <label>제품명 : </label>
            <input
              name="productName"
              class="product_name"
              type="text"
              required
            />
          </li>
          <li>
            <label>제품 가격 : </label>
            <input
              name="productPrice"
              class="product_price"
              type="text"
              required
              placeholder="숫자만 입력"
              oninput="numberInputCheck(this)"
            />
          </li>
          <li>
            <label>제품 단위 : </label>
            <input
              name="productUnit"
              class="product_unit"
              type="text"
              required
              placeholder="예) 1box / 800g * 12개입"
            />
          </li>
          <li>
            <label>제품 등급 : </label>
            <select name="productTier" class="product_tier" required>
              <option value="0">0 - 비활성화</option>
              <option value="1">1 - 임대고객(카페용품, 임대머신은 전부 등급 1)</option>
              <option value="2">2 - 비임대고객</option>
              <option value="3">3 - 카페고객</option>
            </select>
          </li>
          <li>
            <label>등록자 : </label>
            <input
              name="productRegisterName"
              class="product_register_name"
              type="text"
              required
            />
          </li>
          <li>
            <label>제품 파일 : </label>
            <input
              name="files"
              class="file"
              type="file"
              multiple="multiple"
              required
            />
          </li>
          <div id="additional_fields"></div>
          <li>
            <div class="submit">
              <input type="submit" value="게시물 작성" class="submit_btn" />
            </div>
          </li>
        </ul>
      </div>

    </form>
  </div>
</main>
<script>
  window.onload = function() {
    function showHideForm() {
      let productType = document.querySelector(".product_type")
      let additionalFields = document.getElementById('additional_fields');

      // Reset the form fields
      additionalFields.innerHTML = '';
      if(productType.value === '0') { // 원두
        let additionalField = document.createElement('ul');
        additionalField.innerHTML = '<li>' +
                                      '<label>품종 : </label>' +
                                      '<input type="text" name="beanSpecies" required />' +
                                    '</li>' +
                                    '<li>' +
                                      '<label>제조사 : </label>' +
                                      '<input type="text" name="beanCompany" required />' +
                                    '</li>' +
                                    '<li>' +
                                      '<label>제조연월일 : </label>' +
                                      '<input type="text" name="beanManufacturingDate" required />' +
                                    '</li>' +
                                    '<li>' +
                                      '<label>소비기한 : </label>' +
                                      '<input type="text" name="beanUseByDate" required />' +
                                    '</li>' +
                                    '<li>' +
                                      '<label>원재료명 :  </label>' +
                                      '<input type="text" name="beanRawMaterials" required />' +
                                    '</li>';

        additionalFields.appendChild(additionalField);
      }
      else if(productType.value === '1') { // 믹스커피
        let additionalField = document.createElement('ul');
        additionalField.innerHTML = '<li>' +
                                      '<label>제조사 : </label>' +
                                      '<input type="text" name="mixCompany" required />' +
                                    '</li>' +
                                    '<li>' +
                                      '<label>소비기한 : </label>' +
                                      '<input type="text" name="mixUseByDate" required />' +
                                    '</li>';

        additionalFields.appendChild(additionalField);
      }
    }

    document.querySelector(".product_type").addEventListener("change", showHideForm);
    showHideForm();
  }
</script>
<script>
  const numberRegex = /[^0-9]/g;

  function numberInputCheck(object) {
    const inputText = object.value;
    const numberInputCheck = inputText.replace(numberRegex, "");
    object.value = numberInputCheck;
  }
</script>
</body>
