<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/style.css?after" />
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/header.css?after" />
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/board.css?after" />
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/view/css/reset.css" />
  </head>
  <body>
    <header>
      <div id="hd_wrap">
        <div class="hd_top">
          <div class="hd_gnb">
            <ul>
              <li>
                <a href="${ pageContext.request.contextPath }/board/main">로그인</a>
              </li>
              <li>
                <a href="">회원가입</a>
              </li>
              <li>
                <a href="">마이페이지</a>
              </li>
              <li>
                <a href="">장바구니</a>
              </li>
            </ul>
          </div>
        </div>
        <div class="hd_center">
          <figure>
            <a href="${ pageContext.request.contextPath }">
              <img src="${ pageContext.request.contextPath }/view/image/logo.png" />
            </a>
          </figure>
          <div class="hd_search">
            <form action="../">
              <input type="text" />
            </form>
            <a href="" class="search_icon">
              <img src="${ pageContext.request.contextPath }/view/image/search.png" alt="" />
            </a>
          </div>
        </div>
        <div class="hd_lnb">
          <div class="hd_lnb_menu">
            <a href="">메뉴</a>
          </div>
          <div class="hd_lnb_list">
            <ul>
              <li>
                <a href="">신상품</a>
              </li>
              <li>
                <a href="">베스트</a>
              </li>
              <li>
                <a href="">할인품</a>
              </li>
              <li>
                <a href="">이벤트</a>
              </li>
              <li>
                <a href="${ pageContext.request.contextPath }/board/blend">블렌드</a>
              </li>
              <li>
                <a href="">스페셜티</a>
              </li>
              <li>
                <a href="">스트레이트</a>
              </li>
              <li>
                <a href="">신선한 생두</a>
              </li>
              <li>
                <a href="">드립용품</a>
              </li>
              <li>
                <a href="">카페몰</a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </header>

  </body>
</html>
