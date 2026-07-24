import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

function formatPrice(price) {
  if (!price) return "0";
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

const PRODUCT_TYPE = { 0: "원두", 1: "믹스", 2: "카페용품" };
const PRODUCT_TIER = { 0: "비활성화", 1: "임대", 2: "미임대", 3: "카페고객" };

function ProductDeletePage() {
  const navigate = useNavigate();
  const [productCode, setProductCode] = useState("");
  const [products, setProducts] = useState([]);
  const [totalCount, setTotalCount] = useState(0);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [columnName, setColumnName] = useState("product_code");
  const [orderBy, setOrderBy] = useState("asc");

  const fetchProducts = async (p = page, col = columnName, ord = orderBy) => {
    try {
      const res = await axiosInstance.get("/api/admin/products", {
        params: { page: p, columnName: col, orderBy: ord },
      });
      const data = res.data.data;
      setProducts(data.list);
      setTotalCount(data.totalCount);
      setTotalPages(data.totalPages);
    } catch (err) {
      console.error("목록 조회 실패:", err);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, [page, columnName, orderBy]);

  const handleSort = (col) => {
    const newOrder =
      columnName === col ? (orderBy === "asc" ? "desc" : "asc") : "asc";
    setColumnName(col);
    setOrderBy(newOrder);
    setPage(1);
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

  const handleDelete = async () => {
    if (!productCode.trim()) {
      alert("제품 번호를 입력해주세요.");
      return;
    }
    if (!confirm("제품이 삭제됩니다. 진행하시겠습니까?")) return;

    try {
      const res = await axiosInstance.delete(
        `/api/admin/products/${productCode}`,
      );
      if (res.data.success) {
        alert("제품이 삭제되었습니다.");
        setProductCode("");
        setPage(1);
        fetchProducts(1, columnName, orderBy);
      } else {
        alert(res.data.message || "삭제에 실패했습니다.");
      }
    } catch (err) {
      alert(err.response?.data?.message || "삭제 중 오류가 발생했습니다.");
    }
  };

  return (
    <main id="admin_page">
      <div className="admin_page_wrap">
        <div className="page_head">
          <a
            onClick={() => navigate("/admin/productDelete")}
            style={{ cursor: "pointer" }}
          >
            <h1>제품 삭제</h1>
          </a>
        </div>

        <div className="search_form_wrap center">
          <div className="inline_wrap">
            <div
              className="search_form"
              style={{ maxHeight: "35px", overflowY: "hidden", width: "480px" }}
            >
              <div style={{ justifyContent: "center" }}>
                <label htmlFor="productCode" style={{ flex: "none" }}>
                  제품 번호
                </label>
                <input
                  type="text"
                  id="productCode"
                  value={productCode}
                  onChange={(e) => setProductCode(e.target.value)}
                />
              </div>
            </div>
          </div>
        </div>

        <div className="btn_wrap center">
          <div className="btn">
            <a onClick={handleDelete} style={{ cursor: "pointer" }}>
              삭제
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
                          <span>제품번호</span>
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
                      <th className="product_file">
                        <div className="asc">
                          <span>파일명</span>
                        </div>
                      </th>
                      <th className="product_register_name">
                        <div className="asc">
                          <span>등록자</span>
                        </div>
                      </th>
                      <th className="product_register_date">
                        <div className="asc">
                          <span>등록일</span>
                        </div>
                      </th>
                      <th className="product_modifier_name">
                        <div className="asc">
                          <span>수정자</span>
                        </div>
                      </th>
                      <th className="product_modifier_date">
                        <div className="asc">
                          <span>수정일</span>
                        </div>
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {products.map((p) => (
                      <tr key={p.productCode}>
                        <td>
                          <p>{PRODUCT_TYPE[p.productType] ?? p.productType}</p>
                        </td>
                        <td>
                          <p>{p.productCode}</p>
                        </td>
                        <td>
                          <p>{p.productName}</p>
                        </td>
                        <td>
                          <p>{p.productUnit}</p>
                        </td>
                        <td>
                          <p>{formatPrice(p.productPrice)} 원</p>
                        </td>
                        <td>
                          <p>{PRODUCT_TIER[p.productTier] ?? p.productTier}</p>
                        </td>
                        <td>
                          <p>{p.productSoldOut ? "품절" : "-"}</p>
                        </td>
                        <td>
                          <p>{p.productFile}</p>
                        </td>
                        <td>
                          <p>{p.productRegisterName}</p>
                        </td>
                        <td>
                          <p>{p.productRegisterDate}</p>
                        </td>
                        <td>
                          <p>{p.productModifierName}</p>
                        </td>
                        <td>
                          <p>{p.productModifierDate}</p>
                        </td>
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

export default ProductDeletePage;
