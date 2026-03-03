import { apiGet } from "./api-utils.js";

document.addEventListener('DOMContentLoaded', () => {
  const pageType = document.querySelector('meta[name="pageType"]')?.content || 'bean';

  let currentPage = 1;

  function formatPrice(price) {
    if (!price) return '0';
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  }

  async function loadProducts(page = 1) {
    currentPage = page;

    try {
      const data = await apiGet(`/api/products?pageType=${pageType}&page=${page}`);
      renderProductList(data.list, data.pageType);
      renderPagination(data.start, data.end, data.pageInt, data.productCount);
    } catch (err) {
      console.error("상품 목록 로드 실패:", err);
    }
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