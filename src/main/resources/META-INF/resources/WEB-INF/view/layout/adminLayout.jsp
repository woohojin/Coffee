<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <link rel="icon" href="${ pageContext.request.contextPath }/image/favicon.ico" type="image/x-icon">
  <title>다올커피 <sitemesh:write property='title'/></title>
  <link rel="stylesheet" href="${ pageContext.request.contextPath }/css/reset.css?v=1.0" />
  <link rel="stylesheet" href="${ pageContext.request.contextPath }/css/datepicker.css?v=1.0" />
  <link rel="stylesheet" href="${ pageContext.request.contextPath }/css/font.css?v=1.0" />
  <link rel="stylesheet" href="${ pageContext.request.contextPath }/css/common.css?v=1.1" />
  <link rel="stylesheet" href="${ pageContext.request.contextPath }/css/form.css?v=1.0" />
  <link rel="stylesheet" href="${ pageContext.request.contextPath }/css/admin.css?v=1.0" />

  <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
  <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
  <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
  <script src="${ pageContext.request.contextPath }/js/daumAddress.js"></script>
  <script src="${ pageContext.request.contextPath }/js/searchSubmit.js"></script>
  <script src="${ pageContext.request.contextPath }/js/fileSystem.js"></script>
  <script src="${ pageContext.request.contextPath }/js/index.js"></script>
</head>
<body>
<%@ include file="/common/adminHead.jsp" %>
<sitemesh:write property="body" />
</body>
</html>
