import {apiPostForm} from "./api-utils";

function increaseCartQuantity(num) {
    updateCart(num, 1);
}

function decreaseCartQuantity(num) {
    const input = document.querySelector(".input" + num);
    if (Number(input.value) > 1) {
        updateCart(num, -1);
    } else {
        alert("상품 수량이 1보다 작을 수 없습니다.");
    }
}

function deleteCart(num) {
    if (confirm("장바구니에서 상품을 삭제하시겠습니까?")) {
        updateCart(num, 0);  // status = 0 (삭제)
    }
}

// num = 장바구니 내 상품 위치 change = status와 비슷하지만 1과 -1로 증감을 구분 0으로 삭제
function updateCart(num, change) {
    const form = document.querySelector(".form" + num);
    if (!form) return;

    const productCode = form.querySelector('input[name="productCode"]').value;
    let quantity = Number(form.querySelector(".input" + num).value);

    // 1 = 수량 변경 0 = 삭제
    let status = 1;
    if (change === 0) {
        status = 0;
        quantity = 0;
    } else {
        quantity += change;
    }

    const formData = new FormData();
    formData.append('status', status);
    formData.append('quantity', quantity);
    formData.append('productCode', productCode);

    const headers = {};
    if (window.csrf?.name && window.csrf?.value) {
        headers[window.csrf.name] = window.csrf.value;
    }

    apiPostForm('/api/member/cart/update', formData, headers)
      .then(data => {
          // 여기서 data.success 체크 필요 없음 → 성공 시에만 여기로 옴
          renderCart(data);
      })
      .catch(err => {
          console.error("장바구니 업데이트 실패:", err);
          alert('오류가 발생했습니다. 다시 시도 해주세요.');
      });
}
