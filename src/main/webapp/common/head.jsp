<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
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
                <form action="${ pageContext.request.contextPath }/board/productSearch" id="search_form" method="post">
                  <input
                          name="searchText"
                          id="search_text"
                          type="text"
                  />
                </form>
                <a href="" class="search_icon" onclick="return searchSubmit()">
                  <img src="${ pageContext.request.contextPath }/view/image/search.png" alt="" />
                </a>
              </div>
              <div class="hd_gnb_menu">
                <ul>
                  <c:choose>
                    <c:when test="${ sessionScope.memberId == null }" >
                      <li>
                        <a href="${ pageContext.request.contextPath }/member/memberSignIn">로그인</a>
                      </li>
                      <li>
                        <a href="${ pageContext.request.contextPath }/member/memberSignUp">회원가입</a>
                      </li>
                    </c:when>

                    <c:when test="${ sessionScope.memberId != null }">
                      <li>
                        <a href="${ pageContext.request.contextPath }/member/memberLogout" id="logout_button" onClick="return checkLogout()">로그아웃</a>
                      </li>
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
                  <li class="hd_gnb_member_cart_wrap">
                    <a href="${ pageContext.request.contextPath }/member/memberCart" class="cart">
                      <div>
                        <span class="cart_count">${sessionScope.cartCount}</span>
                      </div>
                      <img src="${ pageContext.request.contextPath }/view/image/cart.png" alt="" />
                    </a>
                    <div class="hd_gnb_member_cart" style="max-height: 0; border: none;">
                      <div class="hd_gnb_member_cart_status">
                        <div>
                          <p>장바구니에 추가 완료</p>
                        </div>
                        <div>
                          <a>
                            <img src="${ pageContext.request.contextPath }/view/image/close.png"  alt=""/>
                          </a>
                        </div>
                      </div>
                      <div class="hd_gnb_member_cart_info">
                        <a href="${ pageContext.request.contextPath }/board/productDetail?productCode=${ cart.productCode }">
                          <img src="${ pageContext.request.contextPath }/view/image/1.jpg"  alt=""/>
                        </a>
                        <div class="hd_gnb_member_cart_text">
                          <a>
                            <p class="cart_product_name">레브 로얄</p>
                          </a>
                          <p class="cart_product_unit">200g</p>
                          <p class="cart_quantity">1개</p>
                          <p class="cart_product_price">10,000 원</p>
                        </div>
                      </div>
                      <div class="hd_gnb_member_cart_btn">
                        <div class="btn">
                          <a href="${ pageContext.request.contextPath }/member/memberCart">장바구니 (2)</a>
                        </div>
                        <div class="btn">
                          <a>결제하기</a>
                        </div>
                      </div>
                    </div>
                  </li>
                </ul>
              </div>
            </div>

            <div class="hd_lnb">
              <div class="hd_lnb_menu">
                <div class="hd_lnb_dropdown_wrap">
                  <a class="hd_lnb_dropdown_icon">
                    <img src="${ pageContext.request.contextPath }/view/image/menu.png" alt="" />
                  </a>
                  <div class="hd_lnb_dropdown" style="height: 0;">
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
                    <a href="${ pageContext.request.contextPath }/board/productUploadForm">제품 업로드</a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </header>
  <script>
    const logoutButton = document.getElementById("logout_button");

    function checkLogout() {
      if(confirm("로그아웃 하시겠습니까?") === true) {
        return true;
      } else {
        return false;
      }
    }
  </script>
  <script>
    const hoverObject = document.querySelector(".hd_lnb_dropdown_wrap");
    const dropdown = document.querySelector(".hd_lnb_dropdown");

    hoverObject.addEventListener("mouseover", () => {
      dropdown.style.height = "150px";
      dropdown.style.borderTop = "1px solid rgba(200, 200, 200, 1)";
    });
    hoverObject.addEventListener("mouseout", () => {
      dropdown.style.height = "0";
      dropdown.style.borderTop = "inherit";
    });
  </script>
  </body>
</html>
