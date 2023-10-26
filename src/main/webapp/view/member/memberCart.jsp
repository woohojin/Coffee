<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>

<body>
<main id="member_cart_page">
  <form
    action="${ pageContext.request.contextPath }/board/productUploadPro"
    name="f"
    method="post"
  >
    <div class="product_form">
      <ul>
        <li>
          
          <input
            name="productCode"
            id="product_code"
            class="productCode"
            type="text"
          />
        </li>
        <li>
          
          <input
            name="productType"
            id="product_type"
            class="productType"
            type="text"
          />
        </li>
        <li>
          <div class="submit">
            <input type="submit" value="게시물 작성" class="submit_btn" />
            <div class="btn">
              <a href="">목록</a>
            </div>
          </div>
        </li>
      </ul>
    </div>
  
  </form>
</main>
</body>
