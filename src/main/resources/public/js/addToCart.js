document.addEventListener("DOMContentLoaded", function () {
    // 이벤트 위임으로 submit 처리
  const csrfInput = document.getElementById('csrf-token');
  const csrfToken = csrfInput ? csrfInput.value : '';
  const csrfHeader = 'X-CSRF-TOKEN';

  document.addEventListener("submit", function (event) {
      if (!event.target.matches(".product_quantity_form")) return;

      event.preventDefault();

      const form = event.target;
      const formData = new FormData(form);

      fetch(form.action, {
          method: "POST",
          headers: {
            [csrfHeader]: csrfToken,
          },
          body: formData,
      })
        .then((response) => response.json())
        .then((data) => {
            const cart = document.querySelector(".hd_gnb_member_cart");
            const background = document.querySelector(".background-fadeout");
            const cartCount = document.querySelector(".cart_count");

            const productName = data.productName;
            const productUnit = data.productUnit;
            const quantity = data.quantity;
            const productPrice = data.productPrice;
            const productFile = data.productFile;
            const productCode = data.productCode;
            const productType = data.productType;

            // 기존 이미지 & 텍스트 제거
            const info = document.querySelector(".hd_gnb_member_cart_info");
            const text = document.querySelector(".hd_gnb_member_cart_text");

            info.querySelectorAll("a").forEach((el) => el.remove());
            text.querySelectorAll("a").forEach((el) => el.remove());

            // 폴더 결정
            let folder = "bean";
            if (productType === 1) folder = "mix";
            else if (productType === 2) folder = "cafe";

            // 상세 페이지 URL 결정
            let detailUrl = "/products/beanDetail";
            if (productType === 1) detailUrl = "/products/mixDetail";
            else if (productType === 2) detailUrl = "/products/cafeDetail";

            // 새로운 이미지 HTML 생성 및 삽입
            const imgHtml = `
        <a href="${detailUrl}?productCode=${productCode}">
          <img src="/files/${folder}/${productCode}/${productFile}" alt="${productName}" />
        </a>
      `;
            info.insertAdjacentHTML("afterbegin", imgHtml);

            // 텍스트 업데이트
            text.innerHTML = `
        <p class="cart_product_name">${productName}</p>
        <p class="cart_product_unit">${productUnit}</p>
        <p class="cart_quantity">${quantity} 개</p>
        <p class="cart_product_price">${Number(productPrice).toLocaleString("ko-KR")} 원</p>
      `;

            // 장바구니 개수 업데이트
            let num = Number(cartCount.textContent);
            cartCount.textContent = num + 1;

            // 열기
            cart.classList.add("open");
            background.classList.add("visible");

            // 자동 닫기
            setTimeout(() => {
                cart.classList.remove("open");
                setTimeout(() => {
                    background.classList.remove("visible");
                }, 1000); // transition 시간과 맞춤
            }, 4000);

            // 수동 닫기 이벤트 (한 번만 등록되도록 조정)
            const closeHandler = () => {
                cart.classList.remove("open");
                setTimeout(() => {
                    background.classList.remove("visible");
                }, 1000);
            };

            document.querySelector(".cart_close_btn").addEventListener("click", closeHandler);
            background.addEventListener("click", closeHandler);
        })
        .catch((error) => {
            console.error("ERROR:", error);
        });
  });
});