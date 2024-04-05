<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<body>
  <main id="product_detail_page">
    <div class="product_detail_wrap">
      <div class="product_detail">
        <div class="machine_image">
          <img src="${ pageContext.request.contextPath }/view/image/machine_detail.png" alt="" />
        </div>
      </div>
    </div>
  </main>
  <script src="${ pageContext.request.contextPath }/view/js/addToCart.js"></script>
  <script src="${ pageContext.request.contextPath }/view/js/scrollMove.js"></script>
</body>

