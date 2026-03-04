import {apiPostForm} from "./api-utils.js";

document.addEventListener("DOMContentLoaded", function () {
  function updateCartPreview(data) {
    const cart = document.querySelector(".hd_gnb_member_cart");
    const background = document.querySelector(".background-fadeout");
    const cartCount = document.querySelector(".cart_count");

    const {
      productName = "",
      productUnit = "",
      quantity = 0,
      productPrice = 0,
      productFile = "",
      productCode = "",
      productType = 0
    } = data;

    // 상품 타입별 폴더 및 URL 결정
    let folder = "bean";
    let detailUrl = "/products/beanDetail";

    if (productType === 1) {
      folder = "mix";
      detailUrl = "/products/mixDetail";
    } else if (productType === 2) {
      folder = "cafe";
      detailUrl = "/products/cafeDetail";
    }

    // 기존 내역 삭제
    const info = document.querySelector(".hd_gnb_member_cart_info");
    const text = document.querySelector(".hd_gnb_member_cart_text");

    info?.querySelectorAll("a").forEach((el) => el.remove());
    text?.querySelectorAll("a").forEach((el) => el.remove());

    // 이미지 추가
    const imgHtml = `
      <a href="${detailUrl}?productCode=${productCode}">
        <img src="/files/${folder}/${productCode}/${productFile}" alt="${productName}" />
      </a>
    `;
    info?.insertAdjacentHTML("afterbegin", imgHtml);

    // 텍스트 업데이트
    text.innerHTML = `
      <p class="cart_product_name">${productName}</p>
      <p class="cart_product_unit">${productUnit}</p>
      <p class="cart_quantity">${quantity} 개</p>
      <p class="cart_product_price">${Number(productPrice).toLocaleString("ko-KR")} 원</p>
    `;

    let count = Number(cartCount?.textContent) || 0;
    cartCount.textContent = count + 1;

    // 숨겨둔 장바구니 활성화
    cart?.classList.add("open");
    background?.classList.add("visible");

    // 4초 제한
    setTimeout(() => {
      cart?.classList.remove("open");
      setTimeout(() => {
        background?.classList.remove("visible");
      }, 1000);
    }, 4000);

    // 클릭 이벤트 수동 닫기
    const closeHandler = () => {
      cart?.classList.remove("open");
      setTimeout(() => {
        background?.classList.remove("visible");
      }, 1000);
    };

    document.querySelector(".cart_close_btn")?.addEventListener("click", closeHandler, { once: true });
    background?.addEventListener("click", closeHandler, { once: true });
  }
  // submit 이벤트 리스너
  document.addEventListener("submit", async (e) => {
    if (!e.target.matches(".product_quantity_form")) return;

    e.preventDefault();

    const form = e.target;
    const formData = new FormData(form);

    try {
      const data = await apiPostForm(
        form.action,
        formData
      );

      updateCartPreview(data);

    } catch (err) {
      console.error("장바구니 추가 실패:", err);
    }
  });
});