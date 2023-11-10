<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<head>
  <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/reset.css" />
  <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/font.css" />
  <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/common.css" />
  <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/header.css" />
</head>
<body>
  <header>
  <div id="hd_wrap">
    <div class="hd_lnb">
      <div class="hd_lnb_list">
        <ul>
          <li>
            <a href="${ pageContext.request.contextPath }/board/product"
            >원두</a
            >
          </li>
          <li>
            <a href="${ pageContext.request.contextPath }/board/productUploadForm">제품 업로드</a>
          </li>
          <li>
            <a href="${ pageContext.request.contextPath }/admin/memberTierUpdate">권한 주입</a>
          </li>
        </ul>
      </div>
    </div>
  </div>
</header>
  <main id="admin_page">
    <div class="admin_page_wrap">
      <div class="dashboard_wrap">
        <div class="dashboard">
        
        </div>
      </div>
    </div>
  </main>
</body>

