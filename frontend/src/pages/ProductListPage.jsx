import { useState, useEffect } from "react";
import axiosInstance from "../api/axiosInstance";
import { useSearchParams } from "react-router-dom";

function formatPrice(price) {
  if (!price) return "0";
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function ProductList() {
  const [products, setProducts] = useState([]);
  const [pageInt, setPageInt] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [searchParams] = useSearchParams();
  const [loading, setLoading] = useState(true);
  const pageType = searchParams.get("pageType") || "bean";
  const searchText = searchParams.get("searchText") || "";

  useEffect(() => {
    setLoading(true);
    const url = searchText
      ? `/api/products/search?searchText=${searchText}&page=${pageInt}`
      : `/api/products?pageType=${pageType}&page=${pageInt}`;

    axiosInstance
      .get(url)
      .then((res) => {
        setProducts(res.data.data.list ?? []);
        setTotalPages(res.data.data.totalPages ?? 0);
      })
      .catch((err) => console.error("로드 실패:", err))
      .finally(() => setLoading(false));
  }, [pageInt, searchText ? searchText : pageType]);

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [pageType, searchText]);

  const getDetailPath = (p) => {
    if (p.productType === 1)
      return `/products/mixDetail?productCode=${p.productCode}`;
    if (p.productType === 2)
      return `/products/cafeDetail?productCode=${p.productCode}`;
    return `/products/beanDetail?productCode=${p.productCode}`;
  };

  const getImgFolder = (p) => {
    if (p.productType === 1) return "mix";
    if (p.productType === 2) return "cafe";
    return "bean";
  };

  const getPageTitle = () => {
    if (pageType === "mix") return "믹스 커피";
    if (pageType === "cafe") return "카페 용품";
    if (pageType === "machine") return "임대 머신";
    return "원두 커피";
  };

  if (loading)
    return (
      <main id="product_page">
        <p>로딩 중...</p>
      </main>
    );

  return (
    <main id="product_page">
      <div className="product_wrap">
        <div className="page_head">
          <a>
            <h1>{getPageTitle()}</h1>
          </a>
        </div>

        <div className="product">
          <ul className="center">
            <div id="product-container">
              {products.length === 0 ? (
                <p>제품을 찾을 수 없습니다.</p>
              ) : (
                products.map((p) => (
                  <li key={p.productCode}>
                    {p.productSoldOut === 1 && (
                      <div className="sold_out">Sold Out</div>
                    )}
                    <a href={getDetailPath(p)}>
                      <img
                        src={`/files/${getImgFolder(p)}/${p.productCode}/${p.productFile}`}
                        alt={p.productName || "상품 이미지"}
                      />
                    </a>
                    <div>
                      <a href={getDetailPath(p)}>{p.productName}</a>
                      <p>{formatPrice(p.productPrice)} 원</p>
                    </div>
                  </li>
                ))
              )}
            </div>
          </ul>
        </div>
      </div>

      <div className="pagination_wrap center">
        <div className="pagination">
          {pageInt >= 4 && (
            <button onClick={() => setPageInt(pageInt - 3)}>«</button>
          )}
          {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
            <button
              key={page}
              className={page === pageInt ? "active" : ""}
              onClick={() => setPageInt(page)}
            >
              {page}
            </button>
          ))}
          {pageInt < totalPages - 3 && (
            <button onClick={() => setPageInt(pageInt + 3)}>»</button>
          )}
        </div>
      </div>
    </main>
  );
}

export default ProductList;
