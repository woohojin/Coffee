import { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

function formatPrice(price) {
  if (!price) return "0";
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function OrderHistoryPage() {
  const navigate = useNavigate();
  const [orders, setOrders] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [totalCount, setTotalCount] = useState(0);
  const [columnName, setColumnName] = useState("order_num");
  const [orderBy, setOrderBy] = useState("desc");
  const [isSearching, setIsSearching] = useState(false);
  const [isExpanded, setIsExpanded] = useState(false);

  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [searchOrderId, setSearchOrderId] = useState("");
  const [searchMemberId, setSearchMemberId] = useState("");

  const searchParams = useRef({});

  const fetchOrders = async (p = page, col = columnName, ord = orderBy) => {
    try {
      const res = await axiosInstance.get("/api/admin/orders", {
        params: { page: p, columnName: col, orderBy: ord },
      });
      const data = res.data.data;
      setOrders(data.list);
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
      const res = await axiosInstance.get("/api/admin/orders/search", {
        params,
      });
      const data = res.data.data;
      setOrders(data.list);
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
      fetchOrders(page, columnName, orderBy);
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
    if ((startDate && !endDate) || (!startDate && endDate)) {
      alert("날짜를 한쪽에만 입력 할 수 없습니다.");
      return;
    }
    if (startDate && endDate && new Date(startDate) > new Date(endDate)) {
      alert("시작날짜가 종료날짜보다 앞설 수 없습니다.");
      return;
    }
    searchParams.current = {
      orderId: searchOrderId,
      memberId: searchMemberId,
      startDate,
      endDate,
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
            onClick={() => navigate("/admin/orderHistory")}
            style={{ cursor: "pointer" }}
          >
            <h1>주문 기록</h1>
          </a>
        </div>

        <div className="excel_download">
          <a href="/api/admin/excel/orders">주문기록 엑셀 다운로드</a>
        </div>

        <div className="search_form_wrap center">
          <div className="inline_wrap">
            <div
              className="search_form"
              style={{
                maxHeight: isExpanded ? "200px" : "70px",
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
                  position: "relative",
                }}
              >
                <label htmlFor="datepickerStart">시작일</label>
                <input
                  type="date"
                  id="datepickerStart"
                  className="datepicker"
                  value={startDate}
                  onChange={(e) => setStartDate(e.target.value)}
                />
                <label htmlFor="datepickerEnd">종료일</label>
                <input
                  type="date"
                  id="datepickerEnd"
                  className="datepicker"
                  value={endDate}
                  onChange={(e) => setEndDate(e.target.value)}
                />
              </div>
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  marginBottom: "10px",
                }}
              >
                <label htmlFor="orderId">주문번호</label>
                <input
                  type="text"
                  id="orderId"
                  value={searchOrderId}
                  onChange={(e) => setSearchOrderId(e.target.value)}
                />
                <label htmlFor="memberId">아이디</label>
                <input
                  type="text"
                  id="memberId"
                  value={searchMemberId}
                  onChange={(e) => setSearchMemberId(e.target.value)}
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
              <div>
                <p>기록을 찾을 수 없습니다.</p>
              </div>
            ) : (
              <div>
                <table className="list">
                  <thead>
                    <tr>
                      <th
                        className="history_code"
                        onClick={() => handleSort("order_id")}
                      >
                        <div className={sortClass("order_id")}>
                          <span>주문번호</span>
                          {sortIcon("order_id")}
                        </div>
                      </th>
                      <th className="member_fran_code">
                        <div className="asc">
                          <span>가맹점코드</span>
                        </div>
                      </th>
                      <th className="member_name">
                        <div className="asc">
                          <span>이름</span>
                        </div>
                      </th>
                      <th className="member_company_name">
                        <div className="asc">
                          <span>업체명</span>
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
                          <span>제품명</span>
                          {sortIcon("product_name")}
                        </div>
                      </th>
                      <th
                        className="product_price"
                        onClick={() => handleSort("product_price")}
                      >
                        <div className={sortClass("product_price")}>
                          <span>제품가격</span>
                          {sortIcon("product_price")}
                        </div>
                      </th>
                      <th
                        className="quantity"
                        onClick={() => handleSort("quantity")}
                      >
                        <div className={sortClass("quantity")}>
                          <span>수량</span>
                          {sortIcon("quantity")}
                        </div>
                      </th>
                      <th className="product_price">
                        <div className="asc">
                          <span>제품합가격</span>
                        </div>
                      </th>
                      <th
                        className="delivery_address"
                        onClick={() => handleSort("delivery_address")}
                      >
                        <div className={sortClass("delivery_address")}>
                          <span>배송지</span>
                          {sortIcon("delivery_address")}
                        </div>
                      </th>
                      <th
                        className="order_date"
                        onClick={() => handleSort("order_date")}
                      >
                        <div className={sortClass("order_date")}>
                          <span>주문일</span>
                          {sortIcon("order_date")}
                        </div>
                      </th>
                      <th className="total_price">
                        <div className="asc">
                          <span>총합계</span>
                        </div>
                      </th>
                      <th className="history_modifier_name">
                        <div className="asc">
                          <span>수정자</span>
                        </div>
                      </th>
                      <th className="delivery_code">
                        <div className="asc">
                          <span>송장번호</span>
                        </div>
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {orders.map((h) => (
                      <tr key={`${h.orderId}-${h.productCode}`}>
                        <td>
                          <a
                            onClick={() =>
                              navigate(
                                `/admin/orderHistoryUpdate?orderId=${h.orderId}&productCode=${h.productCode}`,
                              )
                            }
                            style={{ cursor: "pointer" }}
                          >
                            {h.orderId}
                          </a>
                        </td>
                        <td>
                          <p>{h.memberFranCode}</p>
                        </td>
                        <td>
                          <p>{h.memberName}</p>
                        </td>
                        <td>
                          <p>{h.memberCompanyName}</p>
                        </td>
                        <td>
                          <p>{h.productCode}</p>
                        </td>
                        <td>
                          <p>{h.productName}</p>
                        </td>
                        <td>
                          <p>{formatPrice(h.productPrice)}</p>
                        </td>
                        <td>
                          <p>{h.quantity}</p>
                        </td>
                        <td>
                          <p>{formatPrice(h.productPrice * h.quantity)}</p>
                        </td>
                        <td>
                          <p>{h.deliveryAddress}</p>
                          <p>{h.detailDeliveryAddress}</p>
                        </td>
                        <td>
                          <p>{h.orderDate}</p>
                        </td>
                        <td>
                          <p>{formatPrice(h.totalPrice)}</p>
                        </td>
                        <td>
                          <p>{h.historyModifierName}</p>
                        </td>
                        <td>
                          <p>{h.deliveryCode}</p>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
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

export default OrderHistoryPage;
