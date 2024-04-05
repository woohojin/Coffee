<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>DaCOFFEE <sitemesh:write property='title'/></title>
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/reset.css?v=1.0" />
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/datepicker.css?v=1.0" />
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/font.css?v=1.0" />
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/main.css?v=1.0" />
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/common.css?v=1.1" />
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/header.css?v=1.0" />
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/board.css?v=1.1" />
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/form.css?v=1.0" />
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/footer.css?v=1.0" />
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/payments.css?v=1.0" />

    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
    <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script src="${ pageContext.request.contextPath }/view/js/daumAddress.js"></script>
    <script src="https://js.tosspayments.com/v1/payment-widget"></script>

    <script src="${ pageContext.request.contextPath }/view/js/searchSubmit.js"></script>
    <script src="${ pageContext.request.contextPath }/view/js/fileSystem.js"></script>
    <script src="${ pageContext.request.contextPath }/view/js/changeCart.js"></script>
    <script src="${ pageContext.request.contextPath }/view/js/index.js"></script>
</head>
<body>
<%@ include file="/common/head.jsp" %>
<sitemesh:write property="body" />

<%@ include file="/common/footer.jsp" %>
<sitemesh:write property="footer" />
</body>
</html>
