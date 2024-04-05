<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<body>
  <main id="product_detail_page">
    <c:choose>
      <c:when test="${ requestScope.memberTier == '0' }">
        <div class="denied-text">
          <p>로그인을 진행하시거나</p>
          <br/>
          <p>최초 회원가입 진행 후에 1566-0904로 연락 부탁드립니다.</p>
        </div>
      </c:when>

      <c:when test="${ requestScope.memberTier != 0 }">
        <div class="product_detail_wrap">
          <div class="product_detail">
            <div class="product_detail_img">
              <img src="${ pageContext.request.contextPath }/view/files/cafe/${ product.productCode }/${ product.productFile }" alt="" />
            </div>
            <div class="product_info">
              <h1>${ product.productName }</h1>
              <table>
                <tbody>
                <tr>
                  <th>
                    <span>용량</span>
                  </th>
                  <td>
                    <span>${ product.productUnit }</span>
                  </td>
                </tr>
                <tr>
                  <th>
                    <span>배송비</span>
                  </th>
                  <td>
                    <span>3,000 원 (원두 2kg 이상 구매시 무료)</span>
                  </td>
                </tr>
                <tr>
                  <th>
                    <span>가격</span>
                  </th>
                  <td>
                    <span class="product_default_price"><fmt:formatNumber value="${ product.productPrice }" pattern="#,###" /> 원</span>
                  </td>
                </tr>
                </tbody>
              </table>
              <div>
                <form class="product_quantity_form"
                      action="${ pageContext.request.contextPath }/board/productDetailPro"
                      method="post"
                      accept-charset = "UTF-8"
                >
                  <p>${ product.productName } ${ product.productUnit }</p>
                  <div class="product_quantity">
                    <input type="text" class="product_quantity_input" value="1" name="quantity" readonly />
                    <div class="product_quantity_btn">
                      <button type="button" class="left_btn center" onclick="decreaseProductQuantity()">
                        <img src="${ pageContext.request.contextPath }/view/image/minus.png" alt="" />
                      </button>
                      <button type="button" class="right_btn center" onclick="increaseProductQuantity()">
                        <img src="${ pageContext.request.contextPath }/view/image/plus.png" alt="" />
                      </button>
                    </div>
                    <div class="product_quantity_price center">
                      <fmt:formatNumber value="${ product.productPrice }" pattern="#,###" /> 원
                    </div>
                  </div>
<%--                    <div class="product_additional">--%>
<%--                      <span>추가 상품 선택</span>--%>
<%--                      <label>--%>
<%--                        <select name="additionalProducts">--%>
<%--                          <option value="none">없음</option>--%>
<%--                          <option value="CA0001">종이컵6.5온스</option>--%>
<%--                          <option value="CA0003">종이컵8온스</option>--%>
<%--                        </select>--%>
<%--                      </label>--%>
<%--                    </div>--%>
                  <c:if test="${ product.productSoldOut == 1 }">
                    <br/>
                    Sold Out
                  </c:if>
                  <c:if test="${ product.productSoldOut != 1 }">
                    <div class="product_quantity_submit">
                      <input type="hidden" value="${ product.productCode }" name="productCode">
                      <input type="submit" value="장바구니에 담기" class="submit_btn" />
                    </div>
                  </c:if>
                </form>
              </div>
            </div>
          </div>
        </div>
        <ul class="product_detail_main">
          <li>
            <div class="product_detail_list scroll1">
              <ul>
                <li class="active">
                  <a class="scrollBtn1" href="">상품상세정보</a>
                </li>
                <li>
                  <a class="scrollBtn2" href="">배송안내</a>
                </li>
                <li>
                  <a class="scrollBtn3" href="">교환 및 반품안내</a>
                </li>
              </ul>
            </div>
            <div class="product_detail_content_wrap">
              <img src="${ pageContext.request.contextPath }/view/files/cafe/${ product.productCode }/${ requestScope.detailmageName }" alt="" />
            </div>
          </li>
          <li>
            <div class="product_detail_list scroll2">
              <ul>
                <li>
                  <a class="scrollBtn1" href="">상품상세정보</a>
                </li>
                <li class="active">
                  <a class="scrollBtn2" href="">배송안내</a>
                </li>
                <li>
                  <a class="scrollBtn3" href="">교환 및 반품안내</a>
                </li>
              </ul>
            </div>
            <div class="product_detail_content_wrap">
              <div class="product_detail_content">
                <ul>
                  <li>
                    <p>
                      배송 방법 : 로젠 택배
                    </p>
                  </li>
                  <li>
                    <p>
                      배송 비용 : 3000원 (원두 2KG 이상 구매시 무료)
                    </p>
                  </li>
                  <li>
                    <p>
                      배송 기간 : 1일 - 3일
                    </p>
                  </li>
                  <li>
                    <p>
                      당일 오후 2시 이전 주문 시 당일 발송 이후 주문시 익일 발송
                    </p>
                  </li>
                  <li>
                    <p>
                      일부 상품은 구매 시 건당 배송 비용이 발생합니다.
                    </p>
                  </li>
                </ul>
              </div>
            </div>
          </li>
          <li>
            <div class="product_detail_list scroll3">
              <ul>
                <li>
                  <a class="scrollBtn1" href="">상품상세정보</a>
                </li>
                <li>
                  <a class="scrollBtn2" href="">배송안내</a>
                </li>
                <li class="active">
                  <a class="scrollBtn3" href="">교환 및 반품안내</a>
                </li>
              </ul>
            </div>
            <div class="product_detail_content_wrap">
              <div class="product_detail_content">
                <ul>
                  <li>
                    <h1>교환 및 반품이 가능한 경우</h1>
                  </li>
                  <li>
                    <p>
                      출고일로부터 7일이내 요청시 환불 가능합니다.

                      신청 방법 : 032-233-7400으로 연락
                      배송 비용 : 단순 변심 시 왕복 택배비 6,000원
                      반품 주소 : 부천시 도약로 261 C동 902-1호 (부천대우테크노파크)
                    </p>
                  </li>
                  <li>
                    <p>
                      공급받으신 상품 및 용역의 내용이 표시.광고 내용과 다르거나
                      다르게 이행된 경우에는
                    </p>
                    <p>
                      공급받은 날로부터 3개월이내, 그사실을 알게 된 날로부터
                      30일이내 환불 가능합니다.
                    </p>
                  </li>
                </ul>
                <ul>
                  <li>
                    <h1>교환 및 반품이 불가능한 경우</h1>
                  </li>
                  <li>
                    <p>
                      포장을 개봉하였거나 포장이 훼손되어 상품가치가 상실된 경우
                    </p>
                  </li>
                  <li>
                    <p>
                      고객님의 사용 또는 일부 소비에 의하여 상품의 가치가 현저히 감소한 경우
                    </p>
                  </li>
                  <li>
                    <p>
                      분쇄상태를 선택 시 환불이 불가합니다.
                    </p>
                  </li>
                </ul>
              </div>
            </div>
          </li>
        </ul>
      </c:when>
    </c:choose>
  </main>
  <script src="${ pageContext.request.contextPath }/view/js/addToCart.js"></script>
  <script src="${ pageContext.request.contextPath }/view/js/scrollMove.js"></script>
</body>

