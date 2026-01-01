$(document).ready(function() {
    $(document).on("submit", ".product_quantity_form", function(event) {
        event.preventDefault();

        let formData = new FormData($(this)[0]);

        $.ajax({
            type: "POST",
            url: $(this).attr("action"),
            data: formData,
            processData: false,
            contentType: false,
            success: function(data) {
                const cart = $(".hd_gnb_member_cart");
                const text = $(".hd_gnb_member_cart_text");
                const info = $(".hd_gnb_member_cart_info");
                const cartCount = $(".cart_count");
                const background = $(".background-fadeout");
                const close = $(".cart_close_btn");

                const productName = data.productName;
                const productUnit = data.productUnit;
                const quantity = data.quantity;
                const productPrice = data.productPrice;
                const productFile = data.productFile;
                const productCode = data.productCode;
                const productType = data.productType;

                // 기존 이미지 링크(a + img) 제거 (th:if로 안 생겼을 테니)
                info.find("a").remove();
                text.find('a').remove();

                // 새로운 a + img 동적으로 생성
                let folder = 'bean';
                if (productType === 1) folder = 'mix';
                else if (productType === 2) folder = 'cafe';

                let detailUrl = '/board/beanDetail';
                if (productType === 1) detailUrl = '/board/mixDetail';
                else if (productType === 2) detailUrl = '/board/cafeDetail';

                const imgHtml = `
                    <a href="${detailUrl}?productCode=${productCode}">
                        <img src="/files/${folder}/${productCode}/${productFile}" alt="${productName}" />
                    </a>
                `;

                // hd_gnb_member_cart_text 앞에 이미지 삽입
                info.prepend(imgHtml);

                // 상품명 링크도 동적으로 (기존 a 태그 재사용 또는 새로 만들기)
                const nameHtml = `<a href="${detailUrl}?productCode=${productCode}">
                                          <p class="cart_product_name">${productName}</p>
                                        </a>`;
                text.prepend(nameHtml);


                // 장바구니 개수 올려주기
                let num = Number(cartCount.text());
                num += 1;

                // 장바구니에 담긴 상품 정보 표시
                // info.find("a img").src("");
                text.find(".cart_product_unit").text(productUnit);
                text.find(".cart_quantity").text(quantity + " 개");

                let priceText = '0 원';
                if (productPrice !== undefined && productPrice !== null) {
                    priceText = Number(productPrice).toLocaleString("ko-KR") + " 원";
                }
                text.find(".cart_product_price").text(priceText);

                // 장바구니에 focus 되도록 해줌
                cart.css("max-height", "1000px");
                background.css("display", "block").css("background-color", "rgba(0, 0, 0, 0.4)");
                cartCount.text(num);

                function closeCart() {
                    cart.css("max-height", "0");
                    setTimeout(() => {
                        background.css("display", "none").css("background-color", "inherit");
                    }, 1000);
                }

                setTimeout(() => {
                    closeCart();
                }, 4000);

                close.on("click", closeCart);
                background.on("click", closeCart);
            },
            error: function(xhr, status, error) {
                console.error("ERROR: " + error);
            }
        });
    });
});