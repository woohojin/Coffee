const productCode = document.querySelector('meta[name="product-code"]')?.content || '';
const memberTier  = Number(document.querySelector('meta[name="member-tier"]')?.content || 0);
const pageType    = document.querySelector('meta[name="page-type"]')?.content || 'cafe';
const csrfHeader  = document.querySelector('meta[name="csrf-name"]')?.content || '';
const csrfToken   = document.querySelector('meta[name="csrf-token"]')?.content || '';

function formatPrice(price) {
  if (!price) return '0';
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

let currentQuantity = 1;
let currentUnitPrice = 0;

function updateTotalPrice() {
  const total = currentUnitPrice * currentQuantity;
  const priceEl = document.querySelector('.product_quantity_price');
  if (priceEl) {
    priceEl.textContent = formatPrice(total) + ' 원';
  }
}

function increaseProductQuantity() {
  currentQuantity++;
  document.querySelector('.product_quantity_input').value = currentQuantity;
  updateTotalPrice();
}

function decreaseProductQuantity() {
  if (currentQuantity > 1) {
    currentQuantity--;
    document.querySelector('.product_quantity_input').value = currentQuantity;
    updateTotalPrice();
  }
}

document.addEventListener('DOMContentLoaded', () => {
  loadProductDetail();

  document.addEventListener('click', function(e) {
    if (e.target.closest('.increase-btn')) {
      increaseProductQuantity();
    } else if (e.target.closest('.decrease-btn')) {
      decreaseProductQuantity();
    }
  });
});

function loadProductDetail() {
  if (!productCode) {
    document.getElementById('detail-container').innerHTML = '<p>상품 코드가 없습니다.</p>';
    return;
  }

  fetch(`/api/products/${productCode}?pageType=${pageType}`, { credentials: 'include' })
    .then(response => {
      if (!response.ok) {
        if (response.status === 404) throw new Error('상품을 찾을 수 없습니다.');
        if (response.status === 403) throw new Error('로그인이 필요합니다.');
        throw new Error(`서버 오류가 발생했습니다 (${response.status})`);
      }
      return response.json();
    })
    .then(data => renderDetail(data))
    .catch(err => {
      document.getElementById('detail-container').innerHTML =
        `<p style="text-align:center; color:red;">${err.message}</p>`;
    });
}

function renderDetail(data) {
  const container = document.getElementById('detail-container');

  if (memberTier === 0) {
    container.innerHTML = `
      <div class="denied-text">
        <p>로그인을 진행하시거나</p>
        <br/>
        <p>최초 회원가입 진행 후에 1566-0904로 연락 부탁드립니다.</p>
      </div>`;
    return;
  }

  if (data.productCount === 0 || !data.product) {
    container.innerHTML = '<p style="text-align:center;">제품을 찾을 수 없습니다.</p>';
    return;
  }

  const p = data.product;
  const detailImage = data.detailImageName;
  currentUnitPrice = p.productPrice;

  const basePath = `/files/${pageType}/${p.productCode}/`;

  // 페이지별 테이블 행
  let tableRows = '';
  if (pageType === 'bean') {
    tableRows = `
      <tr><th><span>식품유형</span></th><td><span>원두커피 100%</span></td></tr>
      <tr><th><span>원산지</span></th><td><span>${p.beanCountry || ''}</span></td></tr>
      <tr><th><span>품종</span></th><td><span>${p.beanSpecies || ''}</span></td></tr>
      <tr><th><span>제조사</span></th><td><span>${p.beanCompany || ''}</span></td></tr>
      <tr><th><span>소비기한</span></th><td><span>${p.beanUseByDate || ''}</span></td></tr>
      <tr><th><span>용량</span></th><td><span>${p.productUnit || ''}</span></td></tr>
    `;
  } else if (pageType === 'mix') {
    tableRows = `
      <tr><th><span>식품유형</span></th><td><span>커피믹스</span></td></tr>
      <tr><th><span>제조사</span></th><td><span>${p.mixCompany || ''}</span></td></tr>
      <tr><th><span>소비기한</span></th><td><span>${p.mixUseByDate || ''}</span></td></tr>
      <tr><th><span>용량</span></th><td><span>${p.productUnit || ''}</span></td></tr>
    `;
  } else { // cafe 또는 기본
    tableRows = `
      <tr><th><span>용량</span></th><td><span>${p.productUnit || ''}</span></td></tr>
    `;
  }

  tableRows += `
    <tr><th><span>배송비</span></th><td><span>3,000 원 (원두 2kg 이상 구매시 무료)</span></td></tr>
    <tr><th><span>가격</span></th><td><span class="product_default_price">${formatPrice(p.productPrice)} 원</span></td></tr>
  `;

  const hasAdditional = (pageType === 'bean' || pageType === 'mix');
  const additionalHtml = hasAdditional ? `
    <div class="product_additional">
      <span>추가 상품 선택</span>
      <select name="additionalProducts">
        <option value="none">없음</option>
        <option value="CA0001">종이컵 6.5온스 1000개입 (+12,000원)</option>
        <option value="CA0003">종이컵 8온스 1000개입 (+48,400원)</option>
        <option value="CA0010">종이컵 뚜껑 8온스 1000개입 (+25,300원)</option>
        <option value="CA0101">아이스컵 14온스 1000개입 (+69,300원)</option>
        <option value="CA0105">아이스컵 뚜껑 14온스 1000개입 (+36,300원)</option>
        <option value="CA0210">스트로우 자바라 400개입 (+3,000원)</option>
        <option value="CA0214">스틱 15cm 검정 1000개입 (+2,800원)</option>
        <option value="CA0202">시럽 펌프 (+3,000원)</option>
        <option value="CA0501">카페 시럽 1.5L (+4,400원)</option>
        <option value="CA0520">대한제당 스틱설탕 5g x 100개입 (+2,000원)</option>
      </select>
    </div>
  ` : '';

  container.innerHTML = `
    <div class="product_detail_wrap">
      <div class="product_detail">
        <div class="product_detail_img">
          <img src="${basePath}${p.productFile}" alt="${p.productName}" />
        </div>
        <div class="product_info">
          <h1>${p.productName}</h1>
          <table>
            <tbody>${tableRows}</tbody>
          </table>
          <div>
            <form class="product_quantity_form" action="/products/productDetailPro" method="post">
              <input type="hidden" name="productCode" value="${p.productCode}" />
              <p>${p.productName} ${p.productUnit}</p>
              <div class="product_quantity">
                <input type="text" class="product_quantity_input" value="1" name="quantity" readonly />
                <div class="product_quantity_btn">
                  <button type="button" class="left_btn center decrease-btn">
                    <img src="/image/minus.png" alt="" />
                  </button>
                  <button type="button" class="right_btn center increase-btn">
                    <img src="/image/plus.png" alt="" />
                  </button>
                </div>
                <div class="product_quantity_price">${formatPrice(p.productPrice)} 원</div>
              </div>
              ${additionalHtml}
              ${p.productSoldOut == 1 ?
    '<br/><p>Sold Out</p>' :
    `<div class="product_quantity_submit">
                  <input type="submit" value="장바구니에 담기" class="submit_btn" />
                </div>`
  }
            </form>
          </div>
        </div>
      </div>
    </div>

    <ul class="product_detail_main">
      <li>
        <div class="product_detail_list scroll1">
          <ul>
            <li class="active"><a class="scrollBtn1" href="">상품상세정보</a></li>
            <li><a class="scrollBtn2" href="">배송안내</a></li>
            <li><a class="scrollBtn3" href="">교환 및 반품안내</a></li>
          </ul>
        </div>
        <div class="product_detail_content_wrap">
          <img src="${basePath}${detailImage}" alt="상세이미지" />
        </div>
      </li>
      <li>
        <div class="product_detail_list scroll2">
          <ul>
            <li><a class="scrollBtn1" href="">상품상세정보</a></li>
            <li class="active"><a class="scrollBtn2" href="">배송안내</a></li>
            <li><a class="scrollBtn3" href="">교환 및 반품안내</a></li>
          </ul>
        </div>
        <div class="product_detail_content_wrap">
          <div class="product_detail_content">
            <ul>
              <li><p>배송 방법 : 로젠 택배</p></li>
              <li><p>배송 비용 : 3000원 (원두 2KG 이상 구매시 무료)</p></li>
              <li><p>배송 기간 : 1일 - 3일</p></li>
              <li><p>당일 오후 2시 이전 주문 시 당일 발송 이후 주문시 익일 발송</p></li>
              <li><p>일부 상품은 구매 시 건당 배송 비용이 발생합니다.</p></li>
            </ul>
          </div>
        </div>
      </li>
      <li>
        <div class="product_detail_list scroll3">
          <ul>
            <li><a class="scrollBtn1" href="">상품상세정보</a></li>
            <li><a class="scrollBtn2" href="">배송안내</a></li>
            <li class="active"><a class="scrollBtn3" href="">교환 및 반품안내</a></li>
          </ul>
        </div>
        <div class="product_detail_content_wrap">
          <div class="product_detail_content">
            <ul>
              <li><h1>교환 및 반품이 가능한 경우</h1></li>
              <li>
                <p>출고일로부터 7일이내 요청시 환불 가능합니다.</p>
                <p>신청 방법 : 032-233-7400으로 연락</p>
                <p>배송 비용 : 단순 변심 시 왕복 택배비 6,000원</p>
                <p>반품 주소 : 부천시 도약로 261 C동 902-1호 (부천대우테크노파크)</p>
              </li>
              <li>
                <p>공급받으신 상품 및 용역의 내용이 표시.광고 내용과 다르거나 다르게 이행된 경우에는</p>
                <p>공급받은 날로부터 3개월이내, 그사실을 알게 된 날로부터 30일이내 환불 가능합니다.</p>
              </li>
            </ul>
            <ul>
              <li><h1>교환 및 반품이 불가능한 경우</h1></li>
              <li><p>포장을 개봉하였거나 포장이 훼손되어 상품가치가 상실된 경우</p></li>
              <li><p>고객님의 사용 또는 일부 소비에 의하여 상품의 가치가 현저히 감소한 경우</p></li>
              <li><p>분쇄상태를 선택 시 환불이 불가합니다.</p></li>
            </ul>
          </div>
        </div>
      </li>
    </ul>
  `;

  updateTotalPrice();
}

document.addEventListener('DOMContentLoaded', () => {
  loadProductDetail();
});