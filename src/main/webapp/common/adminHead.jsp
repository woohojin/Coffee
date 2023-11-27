<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<body>
  <header>
    <div id="hd_wrap" class="center">
      <div class="hd_lnb">
        <div class="hd_lnb_list">
          <ul>
            <li>
              <a href="${ pageContext.request.contextPath }/admin/productList">제품 리스트</a>
            </li>
            <li>
              <a href="${ pageContext.request.contextPath }/admin/memberList">멤버 리스트</a>
            </li>
            <li>
              <a href="${ pageContext.request.contextPath }/admin/orderHistory">주문 기록</a>
            </li>
            <li>
              <a href="${ pageContext.request.contextPath }/admin/productUpload">제품 업로드</a>
            </li>
            <li>
              <a href="${ pageContext.request.contextPath }/admin/memberTierUpdate">등급 수정</a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </header>
</body>

