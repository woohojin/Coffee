import { apiGet, apiPostForm} from './api-utils.js'

function formatPrice(price) {
  if (!price) return '0';
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

export async function fetchCart() {
  return apiGet('/api/member/cart');
}

function renderCart(data) {
  const container = document.getElementById('cart-container');

  if (data.cartCount < 1) {
    container.innerHTML = '<div class="member_cart_no_product">장바구니에 담은 상품이 없습니다.</div>';
    document.getElementById('order-summary').style.display = 'none';
    return;
  }

  let html = `
    <div class="member_cart_wrap">
      <table class="member_cart">
        <colgroup>
          <col class="cart-col-image" />
          <col class="cart-col-info" />
          <col class="cart-col-unit" />
          <col class="cart-col-qty" />
          <col class="cart-col-price" />
          <col class="cart-col-delete" />
        </colgroup>
        <thead>
          <tr>
            <th class="member_cart_image">이미지</th>
            <th class="member_cart_info">상품정보</th>
            <th>용량</th>
            <th>수량</th>
            <th class="member_cart_price">금액</th>
          </tr>
        </thead>
        <tbody>
  `;

  data.list.forEach((c, index) => {
    let folder = 'bean';
    if (c.productType === 1) folder = 'mix';
    else if (c.productType === 2) folder = 'cafe';

    const csrfHtml = window.csrf?.value
      ? `<input type="hidden" name="${window.csrf.name}" value="${window.csrf.value}" />`
      : '';

    html += `
      <tr>
        <td class="member_cart_image">
          <img src="/files/${folder}/${c.productCode}/${c.productFile}" alt="${c.productName}" />
        </td>
        <td class="member_cart_info">
          <p>${c.productName}</p>
        </td>
        <td class="member_cart_unit">
          <p>${c.productUnit}</p>
        </td>
        <td class="member_cart_quantity">
          <form class="member_cart_form form${index}" method="post">
            <input type="hidden" name="productCode" value="${c.productCode}" />
            <input type="hidden" class="status${index}" name="status" value="" />
            ${csrfHtml}
            <input type="text" class="member_cart_quantity_input input${index}" name="quantity" value="${c.quantity}" required readonly />
            <div class="member_cart_quantity_btn">
              <button type="button" class="up_btn" data-index="${index}">
                <img src="/image/triangle-up.png" alt="수량 증가" />
              </button>
              <button type="button" class="down_btn" data-index="${index}">
                <img src="/image/triangle-down.png" alt="수량 감소" />
              </button>
            </div>
          </form>
        </td>
        <td class="member_cart_price">
          ${c.productSoldOut == 1 ? '<p>Sold Out</p>' : `<p>${formatPrice(c.productPrice * c.quantity)} 원</p>`}
        </td>
        <td>
          <a class="member_cart_delete" data-index="${index}">
            <img src="/image/close.png" alt="삭제" />
          </a>
        </td>
      </tr>
    `;
  });

  html += `
        </tbody>
        <tfoot></tfoot>
      </table>
    </div>
  `;

  container.innerHTML = html;

  // 주문 요약 업데이트
  document.getElementById('sum-price').textContent = formatPrice(data.sumPrice) + " 원";
  document.getElementById('delivery-fee').textContent = formatPrice(data.deliveryFee) + " 원";
  document.getElementById('total-price').textContent = formatPrice(data.totalPrice) + " 원";
}

// 페이지 로드 시 장바구니 불러오기
(async () => {
  try {
    const data = await fetchCart();
    renderCart(data);
  } catch (err) {
    console.error('장바구니 초기 로드 실패:', err);
    document.getElementById('cart-container').innerHTML =
      '<p>장바구니를 불러올 수 없습니다.</p>';
  }
})();

// 수량 증가/감소/삭제 처리
document.addEventListener('click', async (e) => {
  const target = e.target.closest('button, a');
  if (!target) return;

  const index = target.dataset.index;
  if (!index) return;

  const form = document.querySelector(`.form${index}`);
  if (!form) return;

  const quantityInput = document.querySelector(`.input${index}`);

  let status = '';
  if (target.classList.contains('up_btn')) {
    status = 'increase';
  } else if (target.classList.contains('down_btn')) {
    status = 'decrease';
  } else if (target.classList.contains('member_cart_delete')) {
    if (!confirm('장바구니에서 삭제하시겠습니까?')) return;
    status = 'delete';
  }

  if (!status) return;

  const formData = new FormData(form);
  // 에러 방지를 위한 formData 덮어쓰기
  formData.set('status', status);

  try {
    const data = await apiPostForm('/api/member/cart/update', formData);

    // 주문 요약 업데이트
    document.getElementById('sum-price').textContent = formatPrice(data.sumPrice) + " 원";
    document.getElementById('delivery-fee').textContent = formatPrice(data.deliveryFee) + " 원";
    document.getElementById('total-price').textContent = formatPrice(data.totalPrice) + " 원";

    if (status === 'increase' || status === 'decrease') {
      const updatedItem = data.list.find(item => item.productCode === form.querySelector('[name="productCode"]').value);
      if (updatedItem) {
        quantityInput.value = updatedItem.quantity;

        const priceCell = form.closest('tr').querySelector('.member_cart_price p');
        if (priceCell) {
          priceCell.innerHTML = updatedItem.productSoldOut === 1
            ? 'Sold Out'
            : `${formatPrice(updatedItem.productPrice * updatedItem.quantity)} 원`;
        }
      }
    }

    if (status === 'delete') {
      form.closest('tr').remove();
    }

  } catch (err) {
    console.error("장바구니 업데이트 오류:", err);
    alert("서버와의 연결에 문제가 발생했습니다.");
  }
});