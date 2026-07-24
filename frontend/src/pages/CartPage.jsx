import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

function formatPrice(price) {
  if (!price) return "0";
  return Number(price).toLocaleString("ko-KR");
}

function CartPage() {
  const [cartData, setCartData] = useState(null);
  const navigate = useNavigate();

  const handlePayments = () => {
    navigate("/member/memberPayments");
  };

  const getDetailPath = (c) => {
    if (c.productType === 1)
      return `/products/mixDetail?productCode=${c.productCode}`;
    if (c.productType === 2)
      return `/products/cafeDetail?productCode=${c.productCode}`;
    return `/products/beanDetail?productCode=${c.productCode}`;
  };

  const updateCart = async (productCode, delta) => {
    try {
      const res = await axiosInstance.patch(
        `/api/member/cart/items/${productCode}`,
        null,
        { params: { delta } },
      );
      setCartData(res.data.data);
    } catch (err) {
      alert("서버와의 연결에 문제가 발생했습니다.");
    }
  };

  const handleDelete = async (productCode) => {
    if (!confirm("장바구니에서 삭제하시겠습니까?")) return;
    try {
      const res = await axiosInstance.delete(
        `/api/member/cart/items/${productCode}`,
      );
      setCartData(res.data.data);
    } catch (err) {
      alert("서버와의 연결에 문제가 발생했습니다.");
    }
  };

  useEffect(() => {
    axiosInstance
      .get("/api/member/cart")
      .then((res) => setCartData(res.data.data))
      .catch((err) => console.error("장바구니 로드 실패:", err));
  }, []);

  if (!cartData)
    return (
      <main id="member_cart_page">
        <p>로딩 중...</p>
      </main>
    );

  return (
    <main id="member_cart_page">
      <div className="member_cart_page_wrap">
        <div className="page_head">
          <a href="">
            <h1>장바구니</h1>
          </a>
        </div>

        <div id="cart-container">
          {cartData.cartCount < 1 ? (
            <div className="member_cart_no_product">
              장바구니에 담은 상품이 없습니다.
            </div>
          ) : (
            <div className="member_cart_wrap">
              <table className="member_cart">
                <colgroup>
                  <col className="cart-col-image" />
                  <col className="cart-col-info" />
                  <col className="cart-col-unit" />
                  <col className="cart-col-qty" />
                  <col className="cart-col-price" />
                  <col className="cart-col-delete" />
                </colgroup>
                <thead>
                  <tr>
                    <th className="member_cart_image">이미지</th>
                    <th className="member_cart_info">상품정보</th>
                    <th>용량</th>
                    <th>수량</th>
                    <th className="member_cart_price">금액</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  {cartData.list.map((c) => {
                    const folder =
                      c.productType === 1
                        ? "mix"
                        : c.productType === 2
                          ? "cafe"
                          : "bean";
                    return (
                      <tr key={c.productCode}>
                        <td className="member_cart_image">
                          <a
                            onClick={() => navigate(getDetailPath(c))}
                            style={{ cursor: "pointer" }}
                          >
                            <img
                              src={`/files/${folder}/${c.productCode}/${c.productFile}`}
                              alt={c.productName}
                            />
                          </a>
                        </td>
                        <td className="member_cart_info">
                          <p>{c.productName}</p>
                        </td>
                        <td className="member_cart_unit">
                          <p>{c.productUnit}</p>
                        </td>
                        <td className="member_cart_quantity">
                          <div className="member_cart_form">
                            <input
                              type="text"
                              className="member_cart_quantity_input"
                              value={c.quantity}
                              readOnly
                            />
                            <div className="member_cart_quantity_btn">
                              <button
                                type="button"
                                className="up_btn"
                                onClick={() => updateCart(c.productCode, 1)}
                              >
                                <img
                                  src="/image/triangle-up.png"
                                  alt="수량 증가"
                                />
                              </button>
                              <button
                                type="button"
                                className="down_btn"
                                onClick={() => updateCart(c.productCode, -1)}
                              >
                                <img
                                  src="/image/triangle-down.png"
                                  alt="수량 감소"
                                />
                              </button>
                            </div>
                          </div>
                        </td>
                        <td className="member_cart_price">
                          {c.productSoldOut === 1 ? (
                            <p>Sold Out</p>
                          ) : (
                            <p>{formatPrice(c.productPrice * c.quantity)} 원</p>
                          )}
                        </td>
                        <td>
                          <a
                            className="member_cart_delete"
                            onClick={() => handleDelete(c.productCode)}
                          >
                            <img src="/image/close.png" alt="삭제" />
                          </a>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
                <tfoot></tfoot>
              </table>
            </div>
          )}
        </div>
      </div>

      <div className="member_cart_order_wrap center">
        <div className="member_cart_order">
          <h1>주문 요약</h1>
          <div>
            <span>상품 금액 :</span>
            <span>{formatPrice(cartData.sumPrice)} 원</span>
          </div>
          <div>
            <span>배송비 :</span>
            <span>{formatPrice(cartData.deliveryFee)} 원</span>
          </div>
          <div className="member_cart_order_total">
            <span>합계 :</span>
            <span>{formatPrice(cartData.totalPrice)} 원</span>
          </div>
          <div className="btn_wrap">
            <div className="btn">
              <a onClick={handlePayments}>결제하기</a>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}

export default CartPage;
