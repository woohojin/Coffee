import { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

function formatPrice(price) {
  if (!price) return "0";
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

const PRODUCT_TYPE = { 0: "원두", 1: "믹스", 2: "카페용품" };
const PRODUCT_TIER = { 0: "비활성화", 1: "임대", 2: "미임대", 3: "카페고객" };

function ProductListPage() {
  const navigate = useNavigate();
  const [products, setProducts] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [totalCount, setTotalCount] = useState(0);
  const [columnName, setColumnName] = useState("product_code");
  const [orderBy, setOrderBy] = useState("asc");
  const [isSearching, setIsSearching] = useState(false);
  const [isExpanded, setIsExpanded] = useState(false);

  const [searchProductCode, setSearchProductCode] = useState("");
  const [searchProductName, setSearchProductName] = useState("");
  const [searchProductType, setSearchProductType] = useState("");
  const [searchProductPrice, setSearchProductPrice] = useState("");
  const [searchProductUnit, setSearchProductUnit] = useState("");
  const [searchProductTier, setSearchProductTier] = useState("");
  const [searchProductSoldOut, setSearchProductSoldOut] = useState("");

  const searchParams = useRef({});

  const fetchProducts = async (p = page, col = columnName, ord = orderBy) => {
    try {
      const res = await axiosInstance.get("/api/admin/products", {
        params: { page: p, columnName: col, orderBy: ord },
      });
      const data = res.data.data;
      setProducts(data.list);
      setTotalPages(data.totalPages);
      setTotalCount(data.totalCount);
    } catch (err) {
      console.error("목록 조회 실패:", err);
    }
  };

  const fetchSearch = async (p = page, col = columnName, ord = orderBy) => {
    try {
      const params = {
        ...searchParams.current,
        page: p,
        columnName: col,
        orderBy: ord,
      };
      const res = await axiosInstance.get("/api/admin/products/search", {
        params,
      });
      const data = res.data.data;
      setProducts(data.list);
      setTotalPages(data.totalPages);
      setTotalCount(data.totalCount);
    } catch (err) {
      console.error("검색 실패:", err);
    }
  };

  useEffect(() => {
    if (isSearching) {
      fetchSearch(page, columnName, orderBy);
    } else {
      fetchProducts(page, columnName, orderBy);
    }
  }, [page, columnName, orderBy]);

  const handleSort = (col) => {
    const newOrder =
      columnName === col ? (orderBy === "asc" ? "desc" : "asc") : "asc";
    setColumnName(col);
    setOrderBy(newOrder);
    setPage(1);
  };

  const handleSearch = () => {
    searchParams.current = {
      productCode: searchProductCode,
      productName: searchProductName,
      productType: searchProductType,
      productPrice: searchProductPrice,
      productUnit: searchProductUnit,
      productTier: searchProductTier,
      productSoldOut: searchProductSoldOut,
    };
    setIsSearching(true);
    setPage(1);
    fetchSearch(1, columnName, orderBy);
  };

  const sortIcon = (col) => {
    if (columnName !== col) return <img src="/image/down-arrow.png" alt="" />;
    return orderBy === "asc" ? (
      <img src="/image/down-arrow.png" alt="" />
    ) : (
      <img src="/image/up-arrow.png" alt="" />
    );
  };

  const sortClass = (col) => (columnName === col ? orderBy : "asc");

  return (
    <main id="admin_page">
      <div className="admin_page_wrap">
        <div className="page_head">
          <a
            onClick={() => navigate("/admin/productList")}
            style={{ cursor: "pointer" }}
          >
            <h1>제품 리스트</h1>
          </a>
        </div>

        <div className="excel_download">
          <a href="/api/admin/excel/products">제품 엑셀 다운로드</a>
        </div>

        <div className="search_form_wrap center">
          <div className="inline_wrap">
            <div
              className="search_form"
              style={{
                maxHeight: isExpanded ? "200px" : "35px",
                overflowY: "hidden",
                transition: "all 0.6s ease",
                width: "480px",
              }}
            >
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  marginBottom: "10px",
                }}
              >
                <label htmlFor="productCode">제품 번호</label>
                <input
                  type="text"
                  id="productCode"
                  value={searchProductCode}
                  onChange={(e) => setSearchProductCode(e.target.value)}
                />
                <label htmlFor="productName">제품 이름</label>
                <input
                  type="text"
                  id="productName"
                  value={searchProductName}
                  onChange={(e) => setSearchProductName(e.target.value)}
                />
              </div>
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  marginBottom: "10px",
                }}
              >
                <label htmlFor="productType">제품 타입</label>
                <input
                  type="text"
                  id="productType"
                  value={searchProductType}
                  onChange={(e) => setSearchProductType(e.target.value)}
                />
                <label htmlFor="productPrice">금액</label>
                <input
                  type="text"
                  id="productPrice"
                  value={searchProductPrice}
                  onChange={(e) => setSearchProductPrice(e.target.value)}
                />
              </div>
              <div
                className="last"
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  marginBottom: "10px",
                }}
              >
                <label htmlFor="productUnit">용량</label>
                <input
                  type="text"
                  id="productUnit"
                  value={searchProductUnit}
                  onChange={(e) => setSearchProductUnit(e.target.value)}
                />
              </div>
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  marginBottom: "10px",
                }}
              >
                <label htmlFor="productTier">등급</label>
                <input
                  type="text"
                  id="productTier"
                  value={searchProductTier}
                  onChange={(e) => setSearchProductTier(e.target.value)}
                />
                <label htmlFor="productSoldOut">품절</label>
                <input
                  type="text"
                  id="productSoldOut"
                  value={searchProductSoldOut}
                  onChange={(e) => setSearchProductSoldOut(e.target.value)}
                />
              </div>
            </div>
            <div
              className={isExpanded ? "expand" : "collapse"}
              onClick={() => setIsExpanded(!isExpanded)}
              style={{ cursor: "pointer" }}
            >
              <img
                src={
                  isExpanded ? "/image/up-arrow.png" : "/image/down-arrow.png"
                }
                alt=""
              />
            </div>
          </div>
        </div>

        <div className="btn_wrap center">
          <div className="btn">
            <a onClick={handleSearch} style={{ cursor: "pointer" }}>
              검색
            </a>
          </div>
        </div>

        <div className="list">
          <ul className="center">
            {totalCount === 0 ? (
              <li>
                <p>제품을 찾을 수 없습니다.</p>
              </li>
            ) : (
              <li>
                <table className="list">
                  <thead>
                    <tr>
                      <th
                        className="product_type"
                        onClick={() => handleSort("product_type")}
                      >
                        <div className={sortClass("product_type")}>
                          <span>제품타입</span>
                          {sortIcon("product_type")}
                        </div>
                      </th>
                      <th
                        className="product_code"
                        onClick={() => handleSort("product_code")}
                      >
                        <div className={sortClass("product_code")}>
                          <span>제품코드</span>
                          {sortIcon("product_code")}
                        </div>
                      </th>
                      <th
                        className="product_name"
                        onClick={() => handleSort("product_name")}
                      >
                        <div className={sortClass("product_name")}>
                          <span>제품이름</span>
                          {sortIcon("product_name")}
                        </div>
                      </th>
                      <th
                        className="product_unit"
                        onClick={() => handleSort("product_unit")}
                      >
                        <div className={sortClass("product_unit")}>
                          <span>용량</span>
                          {sortIcon("product_unit")}
                        </div>
                      </th>
                      <th
                        className="product_price"
                        onClick={() => handleSort("product_price")}
                      >
                        <div className={sortClass("product_price")}>
                          <span>금액</span>
                          {sortIcon("product_price")}
                        </div>
                      </th>
                      <th
                        className="product_tier"
                        onClick={() => handleSort("product_tier")}
                      >
                        <div className={sortClass("product_tier")}>
                          <span>등급</span>
                          {sortIcon("product_tier")}
                        </div>
                      </th>
                      <th
                        className="product_sold_out"
                        onClick={() => handleSort("product_sold_out")}
                      >
                        <div className={sortClass("product_sold_out")}>
                          <span>품절</span>
                          {sortIcon("product_sold_out")}
                        </div>
                      </th>
                      <th>파일</th>
                      <th>등록자</th>
                      <th>등록일</th>
                      <th>수정자</th>
                      <th>수정일</th>
                    </tr>
                  </thead>
                  <tbody>
                    {products.map((p) => (
                      <tr key={p.productCode}>
                        <td>{PRODUCT_TYPE[p.productType] ?? p.productType}</td>
                        <td>
                          <a
                            onClick={() =>
                              navigate(
                                `/admin/productUpdate?productCode=${p.productCode}`,
                              )
                            }
                          >
                            {p.productCode}
                          </a>
                        </td>
                        <td>{p.productName}</td>
                        <td>{p.productUnit}</td>
                        <td>{formatPrice(p.productPrice)} 원</td>
                        <td>{PRODUCT_TIER[p.productTier] ?? p.productTier}</td>
                        <td>{p.productSoldOut ? "품절" : "-"}</td>
                        <td>{p.productFile}</td>
                        <td>{p.productRegisterName}</td>
                        <td>{p.productRegisterDate}</td>
                        <td>{p.productModifierName}</td>
                        <td>{p.productModifierDate}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </li>
            )}
          </ul>
        </div>

        <div className="pagination_wrap center">
          <div className="pagination">
            {page >= 3 && (
              <a
                onClick={() => setPage(page - 3)}
                style={{ cursor: "pointer" }}
              >
                «
              </a>
            )}
            {Array.from({ length: totalPages }, (_, i) => i + 1)
              .filter((p) => Math.abs(p - page) <= 2)
              .map((p) => (
                <a
                  key={p}
                  onClick={() => setPage(p)}
                  style={{
                    cursor: "pointer",
                    fontWeight: p === page ? "bold" : "normal",
                  }}
                >
                  {p}
                </a>
              ))}
            {page < totalPages - 3 && (
              <a
                onClick={() => setPage(page + 3)}
                style={{ cursor: "pointer" }}
              >
                »
              </a>
            )}
          </div>
        </div>
      </div>
    </main>
  );
}

export default ProductListPage;
