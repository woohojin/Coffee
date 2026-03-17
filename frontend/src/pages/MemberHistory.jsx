import { useState, useEffect } from "react";
import axiosInstance from "../api/axiosInstance";

function formatPrice(price) {
  if (!price) return "0";
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function getDefaultDates() {
  const now = new Date();
  const end = now.toISOString().split("T")[0];
  const start = new Date(now.setMonth(now.getMonth() - 3))
    .toISOString()
    .split("T")[0];
  return { start, end };
}

function MemberHistoryPage() {
  const { start, end } = getDefaultDates();
  const [startDate, setStartDate] = useState(start);
  const [endDate, setEndDate] = useState(end);
  const [list, setList] = useState([]);

  const loadHistory = async (sd, ed) => {
    try {
      const res = await axiosInstance.get(
        `/api/member/history?startDate=${sd}&endDate=${ed}`,
      );
      setList(res.data.data);
    } catch (err) {
      console.error("주문기록 로드 실패:", err);
    }
  };

  useEffect(() => {
    loadHistory(startDate, endDate);
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    loadHistory(startDate, endDate);
  };

  return (
    <main id="member_history_page">
      <div className="member_history_page_wrap">
        <div className="page_head">
          <h1>주문기록</h1>
        </div>
        <div className="datepicker_form_wrap">
          <form onSubmit={handleSearch} className="datepicker_form center">
            <input
              type="date"
              className="datepicker"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
            />
            <span>&nbsp;~&nbsp;</span>
            <input
              type="date"
              className="datepicker"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
            />
            <input type="submit" value="조회" className="submit_btn" />
          </form>
        </div>
        <div className="member_cart_wrap">
          {list.length < 1 ? (
            <p>주문기록이 존재하지 않습니다.</p>
          ) : (
            <table className="member_cart">
              <thead>
                <tr>
                  <th>주문번호</th>
                  <th>이미지</th>
                  <th>상품정보</th>
                  <th>수량</th>
                  <th>금액</th>
                  <th>배송지</th>
                  <th>주문 날짜</th>
                </tr>
              </thead>
              <tbody>
                {list.map((h, idx) => (
                  <tr key={idx}>
                    <td>
                      <p>{h.orderId}</p>
                    </td>
                    <td className="member_cart_image">
                      <img
                        src={`/files/${h.productCode}/${h.productFile}`}
                        alt=""
                      />
                    </td>
                    <td>
                      <p>{h.productName}</p>
                    </td>
                    <td>
                      <p>{h.quantity}</p>
                    </td>
                    <td>
                      <p>{formatPrice(h.productPrice * h.quantity)} 원</p>
                    </td>
                    <td>
                      <p>{h.deliveryAddress}</p>
                      <p>{h.detailDeliveryAddress}</p>
                    </td>
                    <td>
                      <p>{h.orderDate}</p>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </main>
  );
}

export default MemberHistoryPage;
