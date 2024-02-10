<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<body>
  <main id="product_detail_page">
    <div class="product_detail_wrap">
      <div class="product_detail">
        <div class="machine_image">
          <img src="${ pageContext.request.contextPath }/view/image/machine_detail.png" alt="" />
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
          <img src="${ pageContext.request.contextPath }/view/files/bean/${ product.productCode }/${ requestScope.detailmageName }" alt="" />
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
                  배송 방법 : 택배
                </p>
              </li>
              <li>
                <p>
                  배송 비용 : 3000원
                </p>
              </li>
              <li>
                <p>
                  배송 기간 : 1일 - 3일
                </p>
              </li>
              <li>
                <p>
                  당일 3시 이전 주문 시 당일 발송 이후 주문시 익일 발송
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
                </p>
              </li>
              <li>
                <p>
                  공급받으신 상품 및 용역의 내용이 표시.광고 내용과 다르거나
                  다르게 이행된 경우에는<br />
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
                  포장을 개봉하였거나 포장이 훼손되어 상품가치가 상실된
                  경우<br />
                  (예 : 가전제품, 식품, 음반 등, 단 액정화면이 부착된 노트북,
                  LCD모니터, 디지털 카메라 등의<br />
                  불량화소에 따른 반품/교환은 제조사 기준에 따릅니다.)
                </p>
              </li>
              <li>
                <p>
                  고객님의 사용 또는 일부 소비에 의하여 상품의 가치가 현저히
                  감소한 경우<br />
                  단, 화장품등의 경우 시용제품을 제공한 경우에 한 합니다.
                </p>
              </li>
            </ul>
          </div>
        </div>
      </li>
    </ul>
  </main>
  <script src="${ pageContext.request.contextPath }/view/js/addToCart.js"></script>
  <script src="${ pageContext.request.contextPath }/view/js/scrollMove.js"></script>
</body>

