function increaseProductQuantity() {
    const productQuantity = document.querySelector(".product_quantity_input");
    const productDefaultPrice = document.querySelector(".product_default_price");
    const productPrice = document.querySelector(".product_quantity_price");
    const numberRegex = /[^0-9]/g;

    let quantity = Number(productQuantity.value);
    const defaultPrice = Number(productDefaultPrice.innerText.replace(numberRegex, ""));

    quantity += 1;

    productQuantity.value = quantity;
    productPrice.innerText = defaultPrice * quantity + " 원";
}

function decreaseProductQuantity() {
    const productQuantity = document.querySelector(".product_quantity_input");
    const productDefaultPrice = document.querySelector(".product_default_price");
    const productPrice = document.querySelector(".product_quantity_price");
    const numberRegex = /[^0-9]/g;

    let quantity = Number(productQuantity.value);
    const defaultPrice = Number(productDefaultPrice.innerText.replace(numberRegex, ""));

    if(quantity !== 1) {
        quantity -= 1;
    } else {
        alert("상품 수량이 1보다 작을 수 없습니다.");
    }

    productQuantity.value = quantity;
    productPrice.innerText = defaultPrice * quantity + " 원";
}

function increaseCartQuantity(num) {
    const form = document.querySelector(".form" + num);
    const cartQuantity = document.querySelector(".input" + num);
    const status = document.querySelector(".status" + num);
    let quantity = Number(cartQuantity.value);

    quantity += 1;

    cartQuantity.value = quantity;
    status.value = 1;

    form.submit();
}

function decreaseCartQuantity(num) {
    const form = document.querySelector(".form" + num);
    const cartQuantity = document.querySelector(".input" + num);
    const status = document.querySelector(".status" + num);
    let quantity = Number(cartQuantity.value);

    if(quantity > 1) {
        quantity -= 1;
    } else {
        alert("상품 수량이 1보다 작을 수 없습니다.");
    }

    cartQuantity.value = quantity;
    status.value = 1;

    form.submit();
}

function deleteProduct(num) {
    const form = document.querySelector(".form" + num);
    const status = document.querySelector(".status" + num);
    const button = document.querySelector(".member_cart_delete");

    if(confirm("장바구니에서 상품을 삭제하시겠습니까?") === true) {
        status.value = 0;
        form.submit();
    } else {
        return false;
    }
}