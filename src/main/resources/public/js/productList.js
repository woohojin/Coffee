document.addEventListener('DOMContentLoaded', () => {
  const pageType = document.querySelector('meta[name="pageType"]')?.content || 'bean';
  const memberTier = Number(document.querySelector('meta[name="memberTier"]')?.content || 0);

  let currentPage = 1;

  function formatPrice(price) {
    if (!price) return '0';
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  }

  function loadProducts(page = 1) {
    currentPage = page;

    fetch(`/api/products?pageType=${pageType}&page=${page}`, {
      credentials: 'include'
    })
      .then(response => {
        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        return response.json();
      })
      .then(data => {
        const container = document.getElementById('product-container');
        const pagination = document.querySelector('.pagination');

        if (data.memberTier === 0) {
          container.innerHTML = `
            <div class="denied-text">
              <p>로그인을 진행하시거나</p>
              <br/>
              <p>최초 회원가입 진행 후에 1566-0904로 연락 부탁드립니다.</p>
            </div>
          `;
          pagination.innerHTML = '';
          return;
        }

        renderProductList(data.list, data.pageType);
        renderPagination(data.start, data.end, data.pageInt, data.productCount);
      })
      .catch(err => {
        console.error(err);
        document.getElementById('product-container').innerHTML = '<p>데이터를 불러오는 데 실패했습니다.</p>';
      });
  }

  function renderProductList(list, pageType) {
    const container = document.getElementById('product-container');

    if (list.length === 0) {
      container.innerHTML = `<p>제품을 찾을 수 없습니다.</p>`;
      return;
    }

    let html = "";

    list.forEach(p => {
      let detailPath = '/products/beanDetail';
      let imgFolder = 'bean';
      if (pageType === 'mix') {
        detailPath = '/products/mixDetail';
        imgFolder = 'mix';
      } else if (pageType === 'cafe' || pageType === 'machine') {
        detailPath = '/products/cafeDetail';
        imgFolder = 'cafe';
      }

      const detailUrl = `${detailPath}?productCode=${p.productCode}`;
      const imgSrc = `/files/${imgFolder}/${p.productCode}/${p.productFile}`;

      html += `
        <li>
          ${p.productSoldOut == 1 ? '<div class="sold_out">Sold Out</div>' : ''}
          <a href="${detailUrl}">
            <img src="${imgSrc}" alt="${p.productName || '상품 이미지'}" />
          </a>
          <div>
            <a href="${detailUrl}">${p.productName}</a>
            <p>${formatPrice(p.productPrice)} 원</p>
          </div>
        </li>
      `;
    });

    container.innerHTML = html;
  }

  function renderPagination(start, end, current, totalCount) {
    const pagination = document.querySelector('.pagination');
    pagination.innerHTML = '';

    if (totalCount === 0) return;

    // 이전 (3페이지 이동)
    if (current >= 4) {
      const prev = document.createElement('a');
      prev.href = 'javascript:void(0)';
      prev.textContent = '«';
      prev.onclick = () => loadProducts(current - 3);
      pagination.appendChild(prev);
    }

    // 숫자 버튼
    for (let i = start; i <= end; i++) {
      const a = document.createElement('a');
      a.href = 'javascript:void(0)';
      a.textContent = i;
      if (i === current) a.classList.add('active');
      a.onclick = () => loadProducts(i);
      pagination.appendChild(a);
    }

    // 다음 (3페이지 이동)
    if (current < end - 3) {
      const next = document.createElement('a');
      next.href = 'javascript:void(0)';
      next.textContent = '»';
      next.onclick = () => loadProducts(current + 3);
      pagination.appendChild(next);
    }
  }

  // 페이지 로드 시 자동 실행
  loadProducts(1);
});