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

                let detailUrl = '/products/beanDetail';
                if (productType === 1) detailUrl = '/products/mixDetail';
                else if (productType === 2) detailUrl = '/products/cafeDetail';

                const imgHtml = `
                    <a href="${detailUrl}?productCode=${productCode}">
                        <img src="/files/${folder}/${productCode}/${productFile}" alt="${productName}" />
                    </a>
                `;

                // hd_gnb_member_cart_text 앞에 이미지 삽입
                info.prepend(imgHtml);

                text.html(`
                    <p class="cart_product_name">${productName}</p>
                    <p class="cart_product_unit">${productUnit}</p>
                    <p class="cart_quantity">${quantity} 개</p>
                    <p class="cart_product_price">${Number(productPrice).toLocaleString("ko-KR")} 원</p>
                `);

                // 장바구니 개수 올려주기
                let num = Number(cartCount.text());
                num += 1;

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