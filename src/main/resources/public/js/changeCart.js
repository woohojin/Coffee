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

    const csrfToken = document.getElementById('csrf-token');
    if (csrfToken) {
        formData.append(csrfToken.name, csrfToken.value);
    }

    fetch('/api/member/cart/update', {
        method: 'POST',
        body: formData,
        credentials: 'include'
    })
      .then(response => {
          if (!response.ok) {
              // 403이나 401이면 로그인 페이지 반환될 수 있음
              if (response.status === 403 || response.status === 401) {
                  alert('로그인 세션이 만료되었습니다. 다시 로그인해주세요.');
                  location.href = '/member/memberSignIn';
                  return;
              }
              throw new Error('장바구니 처리 실패');
          }
          return response.json();
      })
      .then(data => {
          if (data.success) {
              renderCart(data);  // 장바구니 다시 렌더링
          } else {
              alert(data.message || '처리 중 오류 발생. 다시 시도 해주세요.');
          }
      })
      .catch(err => {
          console.error(err);
          alert('오류가 발생했습니다. 다시 시도 해주세요.');
      });


// function changeGrinding(num) {
//     const form = document.querySelector(".grinding_form" + num);
//     const status = document.querySelector(".grinding_status" + num);
//     const productGrinding = form.querySelector('select[name="productGrinding"]');
//
//     const formElements = form.elements;
//     console.log(formElements);
//
//     // productGrinding이 선택되었는지 확인
//     console.log(productGrinding);
//
//     status.value = 2;
//
//     const formData = new FormData(form);
//
//     for(const pair of formData.entries()) {
//         console.log(pair[0] + ', ' + pair[1]);
//     }
}