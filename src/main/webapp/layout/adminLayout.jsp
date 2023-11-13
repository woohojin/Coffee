<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>DaCOFFEE <sitemesh:write property='title'/></title>
  <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/reset.css" />
  <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/datepicker.css" />
  <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/font.css" />
  <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/common.css" />
  <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/admin.css" />
  <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
  <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
  <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
  <script src="${ pageContext.request.contextPath }/view/js/daumAddress.js"></script>
  <script src="${ pageContext.request.contextPath }/view/js/searchSubmit.js"></script>
  <script src="${ pageContext.request.contextPath }/view/js/fileSystem.js"></script>
  <script src="${ pageContext.request.contextPath }/view/js/signUpFormRequirement.js"></script>
  <script src="${ pageContext.request.contextPath }/view/js/changeCart.js"></script>
  <script src="${ pageContext.request.contextPath }/view/js/index.js"></script>
</head>
<body>
<%@ include file="/common/adminHead.jsp" %>
<sitemesh:write property="body" />
</body>
</html>
