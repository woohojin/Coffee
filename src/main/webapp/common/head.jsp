<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<!DOCTYPE html>
<html lang="ko">
  <body>
    <header>
          <div id="hd_wrap">
            <div class="hd_gnb">
              <div class="hd_logo_wrap">
                <figure>
                  <a href="${ pageContext.request.contextPath }/board/main">
                    <img src="${ pageContext.request.contextPath }/image/logo.png" />
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
                  <img src="${ pageContext.request.contextPath }/image/search.png" alt="" />
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
                        <a href="${ pageContext.request.contextPath }/member/memberTerms">회원가입</a>
                      </li>
                    </c:when>

                    <c:when test="${ sessionScope.memberId != null && sessionScope.memberTier != 9 }">
                      <li>
                        <a href="${ pageContext.request.contextPath }/member/memberLogout" id="logout_button" onClick="return checkLogout()">로그아웃</a>
                      </li>
                      <li>
                        <a href="${ pageContext.request.contextPath }/member/memberMyPage">마이페이지</a>
                      </li>
                    </c:when>

                    <c:when test="${ sessionScope.memberId != null && sessionScope.memberTier == 9 }">
                      <li>
                        <a href="${ pageContext.request.contextPath }/member/memberLogout" id="logout_button" onClick="return checkLogout()">로그아웃</a>
                      </li>
                      <li>
                        <a href="${ pageContext.request.contextPath }/admin/dashboard" target="_blank">관리자페이지</a>
                      </li>
                    </c:when>

                  </c:choose>
                  <li>
                    <span> | </span>
                  </li>
                  <li class="hd_gnb_member_cart_wrap">
                    <a href="${ pageContext.request.contextPath }/member/memberCart" class="cart">
                      <div>
                        <span class="cart_count">
                          <c:if test="${ sessionScope.cartCount == null }">
                            0
                          </c:if>
                          <c:if test="${ sessionScope.cartCount != null }">
                            ${ sessionScope.cartCount }
                          </c:if>
                        </span>
                      </div>
                      <img src="${ pageContext.request.contextPath }/image/cart.png" alt="" />
                    </a>
                    <div class="background-fadeout" style="display: none; background-color: inherit;"></div>
                    <div class="hd_gnb_member_cart" style="max-height: 0;">
                      <div class="hd_gnb_member_cart_status">
                        <div>
                          <p>장바구니에 추가 완료</p>
                        </div>
                        <div>
                          <a class="cart_close_btn">
                            <img src="${ pageContext.request.contextPath }/image/close.png"  alt=""/>
                          </a>
                        </div>
                      </div>
                      <div class="hd_gnb_member_cart_info">
                        <c:choose>
                            <c:when test="${ product.productType == 0 }">
                                <a href="${ pageContext.request.contextPath }/board/beanDetail?productCode=${ product.productCode }">
                                    <img src="${ pageContext.request.contextPath }/files/bean/${ product.productCode }/${ product.productFile }"  alt=""/>
                                </a>
                            </c:when>

                            <c:when test="${ product.productType == 1 }">
                                <a href="${ pageContext.request.contextPath }/board/mixDetail?productCode=${ product.productCode }">
                                    <img src="${ pageContext.request.contextPath }/files/mix/${ product.productCode }/${ product.productFile }" alt="" />
                                </a>
                            </c:when>

                            <c:when test="${ product.productType == 2 }">
                                <a href="${ pageContext.request.contextPath }/board/cafeDetail?productCode=${ product.productCode }">
                                    <img src="${ pageContext.request.contextPath }/files/cafe/${ product.productCode }/${ product.productFile }" alt="" />
                                </a>
                            </c:when>
                        </c:choose>
                        <div class="hd_gnb_member_cart_text">
                          <c:choose>
                            <c:when test="${ product.productType == 0 }">
                              <a href="${ pageContext.request.contextPath }/board/beanDetail?productCode=${ product.productCode }">
                                <p class="cart_product_name">Empty</p>
                              </a>
                            </c:when>

                            <c:when test="${ product.productType == 1 }">
                              <a href="${ pageContext.request.contextPath }/board/mixDetail?productCode=${ product.productCode }">
                                <p class="cart_product_name">Empty</p>
                              </a>
                            </c:when>

                            <c:when test="${ product.productType == 2 }">
                              <a href="${ pageContext.request.contextPath }/board/cafeDetail?productCode=${ product.productCode }">
                                <p class="cart_product_name">Empty</p>
                              </a>
                            </c:when>
                          </c:choose>
                          <p class="cart_product_unit">Empty</p>
                          <p class="cart_quantity">Empty</p>
                          <p class="cart_product_price">Empty</p>
                        </div>
                      </div>
                      <div class="hd_gnb_member_cart_btn">
                        <div class="btn">
                          <a href="${ pageContext.request.contextPath }/member/memberCart">장바구니 (${ sessionScope.cartCount + 1})</a>
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
<%--              <div class="hd_lnb_menu">--%>
<%--                <div class="hd_lnb_dropdown_wrap">--%>
<%--                  <a class="hd_lnb_dropdown_icon">--%>
<%--                    <img src="${ pageContext.request.contextPath }/image/menu.png" alt="" />--%>
<%--                  </a>--%>
<%--                  <div class="hd_lnb_dropdown" style="height: 0;">--%>
<%--                    <ul>--%>
<%--                      <li>--%>
<%--                        <a href="">Menu Name</a>--%>
<%--                      </li>--%>
<%--                    </ul>--%>
<%--                    <ul>--%>
<%--                      <li>--%>
<%--                        <a href="">Menu Name</a>--%>
<%--                      </li>--%>
<%--                    </ul>--%>
<%--                    <ul>--%>
<%--                      <li>--%>
<%--                        <a href="">Menu Name</a>--%>
<%--                      </li>--%>
<%--                    </ul>--%>
<%--                    <ul>--%>
<%--                      <li>--%>
<%--                        <a href="">Menu Name</a>--%>
<%--                      </li>--%>
<%--                      <li>--%>
<%--                        <a href="">Menu 1</a>--%>
<%--                      </li>--%>
<%--                      <li>--%>
<%--                        <a href="">Menu 2</a>--%>
<%--                      </li>--%>
<%--                    </ul>--%>
<%--                  </div>--%>
<%--                </div>--%>
<%--              </div>--%>
              <div class="hd_lnb_list">
                <ul>
                  <li>
                    <a href="${ pageContext.request.contextPath }/board/machineDetail"
                    >임대머신</a
                    >
                  </li>
                  <li>
                    <a href="${ pageContext.request.contextPath }/board/product?pageType=bean"
                      >원두</a
                    >
                  </li>
                  <li>
                    <a href="${ pageContext.request.contextPath }/board/product?pageType=mix"
                    >커피믹스</a
                    >
                  </li>
                  <li>
                    <a href="${ pageContext.request.contextPath }/board/product?pageType=cafe"
                    >카페용품</a
                    >
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </header>
  <script>
    function checkLogout() {
      if(confirm("로그아웃 하시겠습니까?") === true) {
        return true;
      } else {
        return false;
      }
    }
  </script>
<%--  <script>--%>
<%--    const hoverObject = document.querySelector(".hd_lnb_dropdown_wrap");--%>
<%--    const dropdown = document.querySelector(".hd_lnb_dropdown");--%>

<%--    hoverObject.addEventListener("mouseover", () => {--%>
<%--      dropdown.style.height = "150px";--%>
<%--      dropdown.style.borderTop = "1px solid var(--grayLine)";--%>
<%--      dropdown.style.borderBottom = "1px solid var(--grayLine)";--%>
<%--    });--%>
<%--    hoverObject.addEventListener("mouseout", () => {--%>
<%--      dropdown.style.height = "0";--%>
<%--      dropdown.style.borderTop = "none";--%>
<%--      dropdown.style.borderBottom = "none";--%>
<%--    });--%>
<%--  </script>--%>
  </body>
</html>
