<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<body>
<main id="member_cart_page">
  <div class="member_cart_page_wrap">
    <div class="page_head">
      <h1>멤버 업데이트</h1>
    </div>
    <div class="member_cart_wrap center">
      <form
        action="${ pageContext.request.contextPath }/admin/memberTierUpdatePro"
        class="member_cart_form"
        method="post"
      >
        <input type="text" name="memberId" class="member_cart_quantity_input" value="${ member.memberId }" required />
        <input type="text" name="memberTier" class="member_cart_quantity_input" value="${ member.memberTier }" required />
        <input type="submit" />
        <div class="member_cart_quantity_btn">
          <button type="button" class="up_btn" onclick="increaseCartQuantity()">+</button>
          <button type="button" class="down_btn" onclick="decreaseCartQuantity()">-</button>
        </div>
      </form>
    </div>
  </div>
</main>
</body>
