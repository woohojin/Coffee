import { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

function OrderHistoryUpdatePage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const orderId = searchParams.get("orderId");
  const productCode = searchParams.get("productCode");

  const [memberFranCode, setMemberFranCode] = useState("");
  const [memberName, setMemberName] = useState("");
  const [memberCompanyName, setMemberCompanyName] = useState("");
  const [deliveryAddress, setDeliveryAddress] = useState("");
  const [detailDeliveryAddress, setDetailDeliveryAddress] = useState("");
  const [deliveryCode, setDeliveryCode] = useState("");

  useEffect(() => {
    if (!orderId || !productCode) return;

    axiosInstance
      .get(`/api/admin/orders/${orderId}`, { params: { productCode } })
      .then((res) => {
        const h = res.data.data;
        setMemberFranCode(h.memberFranCode || "");
        setMemberName(h.memberName || "");
        setMemberCompanyName(h.memberCompanyName || "");
        setDeliveryAddress(h.deliveryAddress || "");
        setDetailDeliveryAddress(h.detailDeliveryAddress || "");
        setDeliveryCode(h.deliveryCode || "");
      })
      .catch((err) => console.error("조회 실패:", err));
  }, [orderId, productCode]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await axiosInstance.put(`/api/admin/orders/${orderId}`, {
        orderId,
        productCode,
        memberFranCode,
        memberName,
        memberCompanyName,
        deliveryAddress,
        detailDeliveryAddress,
        deliveryCode,
      });

      if (res.data.success) {
        alert("주문 기록이 수정되었습니다.");
        navigate("/admin/orderHistory");
      } else {
        alert(res.data.message || "수정에 실패했습니다.");
      }
    } catch (err) {
      alert(err.response?.data?.message || "수정 중 오류가 발생했습니다.");
    }
  };

  const handleDelete = async () => {
    const confirmDelete = prompt("삭제하려면 주문번호를 입력하세요:");
    if (!confirmDelete) return;

    try {
      const res = await axiosInstance.delete(`/api/admin/orders/${orderId}`, {
        params: { productCode, confirmDelete },
      });

      if (res.data.success) {
        alert("주문 기록이 삭제되었습니다.");
        navigate("/admin/orderHistory");
      } else {
        alert(res.data.message || "삭제에 실패했습니다.");
      }
    } catch (err) {
      alert(err.response?.data?.message || "삭제 중 오류가 발생했습니다.");
    }
  };

  return (
    <main>
      <div className="admin_page_wrap">
        <div className="page_head">
          <a
            onClick={() =>
              navigate(
                `/admin/orderHistoryUpdate?orderId=${orderId}&productCode=${productCode}`,
              )
            }
            style={{ cursor: "pointer" }}
          >
            <h1>주문 기록 수정</h1>
          </a>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="product_form center">
            <ul>
              <li>
                <label>주문 번호 : </label>
                <input
                  className="order_id"
                  type="text"
                  value={orderId || ""}
                  readOnly
                  style={{
                    backgroundColor: "#F4F4F4",
                    border: "1px solid black",
                    borderRadius: "2px",
                  }}
                />
              </li>
              <li>
                <label>제품 코드 : </label>
                <input
                  className="product_code"
                  type="text"
                  value={productCode || ""}
                  readOnly
                  style={{
                    backgroundColor: "#F4F4F4",
                    border: "1px solid black",
                    borderRadius: "2px",
                  }}
                />
              </li>
              <li>
                <label>가맹점코드 : </label>
                <input
                  className="member_fran_code"
                  type="text"
                  value={memberFranCode}
                  onChange={(e) => setMemberFranCode(e.target.value)}
                  required
                />
              </li>
              <li>
                <label>주문자명 : </label>
                <input
                  className="member_name"
                  type="text"
                  value={memberName}
                  onChange={(e) => setMemberName(e.target.value)}
                  required
                />
              </li>
              <li>
                <label>업체명 : </label>
                <input
                  className="member_company_name"
                  type="text"
                  value={memberCompanyName}
                  onChange={(e) => setMemberCompanyName(e.target.value)}
                />
              </li>
              <li>
                <label>배송지 : </label>
                <input
                  className="delivery_address"
                  type="text"
                  value={deliveryAddress}
                  onChange={(e) => setDeliveryAddress(e.target.value)}
                  required
                />
              </li>
              <li>
                <label>배송지 상세주소 : </label>
                <input
                  className="detailDelivery_address"
                  type="text"
                  value={detailDeliveryAddress}
                  onChange={(e) => setDetailDeliveryAddress(e.target.value)}
                  required
                />
              </li>
              <li>
                <label>송장 번호 : </label>
                <input
                  className="delivery_code"
                  type="text"
                  value={deliveryCode}
                  onChange={(e) => setDeliveryCode(e.target.value)}
                />
              </li>
              <li>
                <div
                  className="submit"
                  style={{ justifyContent: "space-between" }}
                >
                  <div className="input_btn">
                    <a onClick={handleDelete} style={{ cursor: "pointer" }}>
                      삭제하기
                    </a>
                  </div>
                  <input
                    type="submit"
                    value="수정하기"
                    className="submit_btn"
                  />
                </div>
              </li>
            </ul>
          </div>
        </form>
      </div>
    </main>
  );
}

export default OrderHistoryUpdatePage;
