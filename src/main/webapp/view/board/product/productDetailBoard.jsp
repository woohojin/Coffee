
  <body>
  <main id="product_detail_page">
    <div class="product_detail_wrap">
      <div class="product_detail">
        <div class="product_detail_img">
          <img src="${ pageContext.request.contextPath }/view/image/1.jpg" alt="" />
        </div>
        <div class="product_info">
          <h1>${ product.productName } ${ product.productUnit } </h1>
          <table>
            <tbody>
            <tr>
              <th>
                <span>원산지</span>
              </th>
              <td>
                <span>${ product.productCountry }</span>
              </td>
            </tr>
            <tr>
              <th>
                <span>품종</span>
              </th>
              <td>
                <span>${ product.productSpecies}</span>
              </td>
            </tr>
            <tr>
              <th>
                <span>제조사</span>
              </th>
              <td>
                <span>${ product.productCompany }</span>
              </td>
            </tr>
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
                <span>3000원 (30000원 이상 구매시 무료)</span>
              </td>
            </tr>
            <tr>
              <th>
                <span>가격</span>
              </th>
              <td>
                <span>${ product.productPrice } 원</span>
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
              <div class="product_quantity_input">
                <input type="text" id="product_quantity" value="1" name="quantity" />
                <div class="product_quantity_btn">
                  <button type="button" class="left_btn">-</button>
                  <button type="button" class="right_btn">+</button>
                </div>
                <div class="product_quantity_price">
                  <span>${ product.productPrice }</span>
                  <span>원</span>
                </div>
              </div>
              <div class="product_quantity_submit">
                <input type="hidden" value="${ sessionScope.memberId }" name="memberId">
                <input type="hidden" value="${ product.productCode }" name="productCode">
                <input type="hidden" value="${ product.productName }" name="productName">
                <input type="hidden" value="${ product.productPrice }" name="productPrice">
                <input type="hidden" value="${ product.productFile }" name="productFile">
                <input type="submit" value="장바구니에 담기" class="submit_btn" />
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
    <ul class="product_detail_main">
      <li>
        <div class="product_detail_list">
          <ul>
            <li class="active">
              <a href="">상품상세정보</a>
            </li>
            <li>
              <a href="">배송안내</a>
            </li>
            <li>
              <a href="">교환 및 반품안내</a>
            </li>
            <li>
              <a href="">상품문의</a>
            </li>
          </ul>
        </div>
        <div class="product_detail_content_wrap">
          <img src="${ pageContext.request.contextPath }/view/image/coffeeDetail.jpg" alt="" />
        </div>
      </li>
      <li>
        <div class="product_detail_list">
          <ul>
            <li>
              <a href="">상품상세정보</a>
            </li>
            <li class="active">
              <a href="">배송안내</a>
            </li>
            <li>
              <a href="">교환 및 반품안내</a>
            </li>
            <li>
              <a href="">상품문의</a>
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
                  상품을 공급 받으신 날로부터 7일이내 단, 가전제품의 경우
                  포장을 개봉하였거나<br />포장이 훼손되어 상품가치가 상실된
                  경우에는 교환/반품이 불가능합니다.
                </p>
              </li>
              <li>
                <p>
                  공급받으신 상품 및 용역의 내용이 표시.광고 내용과 다르거나
                  다르게 이행된 경우에는<br />
                  공급받은 날로부터 3개월이내, 그사실을 알게 된 날로부터
                  30일이내
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
      <li>
        <div class="product_detail_list">
          <ul>
            <li>
              <a href="">상품상세정보</a>
            </li>
            <li>
              <a href="">배송안내</a>
            </li>
            <li class="active">
              <a href="">교환 및 반품안내</a>
            </li>
            <li>
              <a href="">상품문의</a>
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
                  상품을 공급 받으신 날로부터 7일이내 단, 가전제품의 경우
                  포장을 개봉하였거나<br />포장이 훼손되어 상품가치가 상실된
                  경우에는 교환/반품이 불가능합니다.
                </p>
              </li>
              <li>
                <p>
                  공급받으신 상품 및 용역의 내용이 표시.광고 내용과 다르거나
                  다르게 이행된 경우에는<br />
                  공급받은 날로부터 3개월이내, 그사실을 알게 된 날로부터
                  30일이내
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
      <li>
        <div class="product_detail_list">
          <ul>
            <li>
              <a href="">상품상세정보</a>
            </li>
            <li>
              <a href="">배송안내</a>
            </li>
            <li>
              <a href="">교환 및 반품안내</a>
            </li>
            <li class="active">
              <a href="">상품문의</a>
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
                  상품을 공급 받으신 날로부터 7일이내 단, 가전제품의 경우
                  포장을 개봉하였거나<br />포장이 훼손되어 상품가치가 상실된
                  경우에는 교환/반품이 불가능합니다.
                </p>
              </li>
              <li>
                <p>
                  공급받으신 상품 및 용역의 내용이 표시.광고 내용과 다르거나
                  다르게 이행된 경우에는<br />
                  공급받은 날로부터 3개월이내, 그사실을 알게 된 날로부터
                  30일이내
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
  <script>
    $(document).ready(function() {
      $(".product_quantity_form").submit(function(event) {
        event.preventDefault();

        var formData = new FormData($(this)[0]);

        $.ajax({
          type: "POST",
          url: $(this).attr("action"),
          data: formData,
          async: true,
          cache: false,
          processData: false,
          contentType: false,
          success: function(data) {
            // 서버로부터 받은 응답 처리
            var msg = data.msg;
            var msgElement = $("<div>").text(msg);
            $(".product_quantity_form").append(msgElement);
          },
          error: function(xhr, status, error) {
            console.error("오류: " + error);
          }
        });
      });
    });
  </script>
  </body>

