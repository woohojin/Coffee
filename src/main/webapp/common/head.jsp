<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
  <body>
    <header>
          <div id="hd_wrap">
            <div class="hd_gnb">
              <div class="hd_logo_wrap">
                <figure>
                  <a href="${ pageContext.request.contextPath }/board/main">
                    <img src="${ pageContext.request.contextPath }/view/image/logo.png" />
                  </a>
                </figure>
              </div>

              <div class="hd_search">
                <form action="../">
                  <input type="text" />
                </form>
                <a href="" class="search_icon">
                  <img src="${ pageContext.request.contextPath }/view/image/search.png" alt="" />
                </a>
              </div>
              <div class="hd_gnb_menu">
                <ul>
                  <c:choose>
                    <c:when test="${ sessionScope.memberId == null }" >
                      <li>
                        <a href="${ pageContext.request.contextPath }/member/memberSignIn"
                        >로그인</a
                        >
                      </li>
                      <li>
                        <a href="${ pageContext.request.contextPath }/member/memberSignUp">회원가입</a>
                      </li>
                    </c:when>

                    <c:when test="${ sessionScope.memberId != null }">
                      <li>
                        <a href="${ pageContext.request.contextPath }/member/memberProfile">마이페이지</a>
                      </li>
                    </c:when>

                    <c:when test="${ sessionScope.memberId == 'admin' }">
                      <li>
                        <a href="${ pageContext.request.contextPath }/member/Admin">관리자페이지</a>
                      </li>
                    </c:when>

                  </c:choose>
                  <li>
                    <span> | </span>
                  </li>
                  <li>
                    <a href="" class="cart">
                      <div>
                        <span>1</span>
                      </div>
                      <img src="${ pageContext.request.contextPath }/view/image/cart.png" alt="" />
                    </a>
                  </li>
                </ul>
              </div>
            </div>

            <div class="hd_lnb">
              <div class="hd_lnb_menu">
                <div class="hd_lnb_dropdown_wrap">
                  <div class="hd_lnb_dropdown_icon">
                    <img src="${ pageContext.request.contextPath }/view/image/menu.png" alt="" />
                  </div>
                  <div class="hd_lnb_dropdown">
                    <ul>
                      <li>
                        <a href="">Menu Name</a>
                      </li>
                      <li>
                        <a href="">Menu 1</a>
                      </li>
                      <li>
                        <a href="">Menu 2</a>
                      </li>
                    </ul>
                    <ul>
                      <li>
                        <a href="">Menu Name</a>
                      </li>
                      <li>
                        <a href="">Menu 1</a>
                      </li>
                      <li>
                        <a href="">Menu 2</a>
                      </li>
                    </ul>
                    <ul>
                      <li>
                        <a href="">Menu Name</a>
                      </li>
                      <li>
                        <a href="">Menu 1</a>
                      </li>
                      <li>
                        <a href="">Menu 2</a>
                      </li>
                    </ul>
                    <ul>
                      <li>
                        <a href="">Menu Name</a>
                      </li>
                      <li>
                        <a href="">Menu 1</a>
                      </li>
                      <li>
                        <a href="">Menu 2</a>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
              <div class="hd_lnb_list">
                <ul>
                  <li>
                    <a href="${ pageContext.request.contextPath }/board/product"
                      >원두</a
                    >
                  </li>
                  <li>
                    <a href="${ pageContext.request.contextPath }/board/productBoardForm">이미지 테스트</a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </header>

  </body>
</html>
