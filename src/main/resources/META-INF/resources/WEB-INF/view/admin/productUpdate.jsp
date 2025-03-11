<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>

<body>
<main>
  <div class="admin_page_wrap">
    <div class="page_head">
      <a href="">
        <h1>제품 수정</h1>
      </a>
    </div>
    <form
      action="${ pageContext.request.contextPath }/admin/productUpdatePro"
      method="post"
      enctype="multipart/form-data"
    >
      <input name="existProductCode" type="text" value="${ requestScope.product.existProductCode }" hidden>
      <div class="product_form center">
        <ul>
          <li>
            <label>제품 종류 : </label>
            <select name="productType" class="product_type" required>
              <option value="0" ${ requestScope.product.productType == 0 ? 'selected' : '' }>원두</option>
              <option value="1" ${ requestScope.product.productType == 1 ? 'selected' : '' }>커피믹스</option>
              <option value="2" ${ requestScope.product.productType == 2 ? 'selected' : '' }>카페용품</option>
            </select>
          </li>
          <li>
            <label>제품 코드 : </label>
            <input
              name="productCode"
              class="product_code"
              type="text"
              value="${ requestScope.product.productCode }"
              required
            />
          </li>
          <li>
            <label>제품명 : </label>
            <input
              name="productName"
              class="product_name"
              type="text"
              value="${ requestScope.product.productName }"
              required
            />
          </li>
          <li>
            <label>제품 가격 : </label>
            <input
              name="productPrice"
              class="product_price"
              type="text"
              value="${ requestScope.product.productPrice }"
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
              value="${ requestScope.product.productUnit }"
              required
              placeholder="예) 1box / 800g * 12개입"
            />
          </li>
          <li>
            <label>제품 등급 : </label>
            <select name="productTier" class="product_tier" required>
              <option value="0" ${ requestScope.product.productTier == 0 ? 'selected' : '' }>0 - 비활성화</option>
              <option value="1" ${ requestScope.product.productTier == 1 ? 'selected' : '' }>1 - 임대고객(카페용품은 전부 등급 1)</option>
              <option value="2" ${ requestScope.product.productTier == 2 ? 'selected' : '' }>2 - 비임대고객</option>
              <option value="3" ${ requestScope.product.productTier == 3 ? 'selected' : '' }>3 - 카페고객</option>
            </select>
          </li>
          <li>
            <label>제품 품절 여부 : </label>
            <select name="productSoldOut" class="product_sold_out" required style="width: 193px">
              <option value="0" ${ requestScope.product.productSoldOut == 0 ? 'selected' : '' }>0 - 품절 X</option>
              <option value="1" ${ requestScope.product.productSoldOut == 1 ? 'selected' : '' }>1 - 품절 O</option>
            </select>
          </li>
          <div id="additional_fields"></div>
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
              <input type="submit" value="수정하기" class="submit_btn" />
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
      let productType = document.querySelector(".product_type");
      let additionalFields = document.getElementById('additional_fields');

      // Reset the form fields
      additionalFields.innerHTML = '';
      if(productType.value === '0') { // 원두
        let additionalField = document.createElement('ul');
        additionalField.innerHTML = '<li>' +
          '<label>품종 : </label>' +
          '<input type="text" name="beanSpecies" value="${ requestScope.bean.beanSpecies }" required />' +
          '</li>' +
          '<li>' +
          '<label>제조사 : </label>' +
          '<input type="text" name="beanCompany" value="${ requestScope.bean.beanCompany }" required />' +
          '</li>' +
          '<li>' +
          '<label>소비기한 : </label>' +
          '<input type="text" name="beanUseByDate" value="${ requestScope.bean.beanUseByDate }" required />' +
          '</li>' +
          '<li>' +
          '<label>원산지 : </label>' +
          '<input type="text" name="beanRawMaterials" value="${ requestScope.bean.beanCountry }" required />' +
          '</li>';

        additionalFields.appendChild(additionalField);
      }
      else if(productType.value === '1') { // 믹스커피
        let additionalField = document.createElement('ul');
        additionalField.innerHTML = '<li>' +
          '<label>제조사 : </label>' +
          '<input type="text" name="mixCompany" value="${ requestScope.mix.mixCompany }" required />' +
          '</li>' +
          '<li>' +
          '<label>소비기한 : </label>' +
          '<input type="text" name="mixUseByDate" value="${ requestScope.mix.mixUseByDate }" required />' +
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
