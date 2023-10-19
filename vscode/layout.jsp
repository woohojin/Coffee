<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>DaCOFFEE <sitemesh:write property='title'/></title>
    <link
      rel="stylesheet"
      href="${ pageContext.request.contextPath }/view/css/style.css"
    />
    <link
      rel="stylesheet"
      href="${ pageContext.request.contextPath }/view/css/board.css"
    />
    <link
      rel="stylesheet"
      href="${ pageContext.request.contextPath }/view/css/header.css"
    />
    <link
      rel="stylesheet"
      href="${ pageContext.request.contextPath }/view/css/form.css"
    />
    <link
      rel="stylesheet"
      href="${ pageContext.request.contextPath }/view/css/reset.css"
    />
    <%@ include file="/common/head.jsp" %>
    <sitemesh:write property="head" />
  </head>
  <body>
    <sitemesh:write property="body" />

    <%@ include file="/common/footer.jsp" %>
    <sitemesh:write property="footer" />
  </body>
</html>
