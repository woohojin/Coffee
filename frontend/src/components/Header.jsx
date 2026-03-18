import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../store/authStore";
import { useCart } from "../store/cartStore";
import axiosInstance from "../api/axiosInstance";
import { useState } from "react";
import { useCartPreview } from "../hooks/useCartPreview";

function Header() {
  const navigate = useNavigate();
  const { member, setMember, loading } = useAuth();
  const { isCartOpen, handleCloseCart, cartPreview, folder, detailUrl } =
    useCartPreview();
  const [searchText, setSearchText] = useState("");
  const { cartCount, refreshCartCount } = useCart();

  const handleLogout = async () => {
    if (!confirm("로그아웃 하시겠습니까?")) return;
    await axiosInstance.post("/member/memberLogout");
    setMember(null);
    navigate("/main");
  };

  const handleSearch = (e) => {
    e.preventDefault();
    if (!searchText.trim()) return;
    navigate(`/products/productSearch?searchText=${searchText}`);
  };

  return (
    <header>
      <div id="hd_wrap">
        <div className="hd_gnb">
          <div className="hd_logo_wrap">
            <figure>
              <Link to="/main">
                <img src="/image/logo.png" alt="Daall Coffee" />
              </Link>
            </figure>
          </div>
          <div className="hd_search">
            <form onSubmit={handleSearch}>
              <input
                name="searchText"
                id="search_text"
                type="text"
                value={searchText}
                onChange={(e) => setSearchText(e.target.value)}
              />
            </form>
            <a href="#" className="search_icon" onClick={handleSearch}>
              <img src="/image/search.png" alt="Search" />
            </a>
          </div>
          <div className="hd_gnb_menu">
            <ul>
              {!loading && !member && (
                <>
                  <li>
                    <Link to="/member/memberSignIn">로그인</Link>
                  </li>
                  <li>
                    <Link to="/member/memberTerms">회원가입</Link>
                  </li>
                </>
              )}
              {!loading && member && member.memberTier !== 9 && (
                <>
                  <li>
                    <button onClick={handleLogout}>로그아웃</button>
                  </li>
                  <li>
                    <Link to="/member/memberMyPage">마이페이지</Link>
                  </li>
                </>
              )}
              {!loading && member && member.memberTier === 9 && (
                <>
                  <li>
                    <button onClick={handleLogout}>로그아웃</button>
                  </li>
                  <li>
                    <a href="/admin/dashboard" target="_blank">
                      관리자페이지
                    </a>
                  </li>
                </>
              )}
              <li>
                <span> | </span>
              </li>
              <li className="hd_gnb_member_cart_wrap">
                <Link to="/member/memberCart" className="cart">
                  <div>
                    <span className="cart_count">{cartCount}</span>
                  </div>
                  <img src="/image/cart.png" alt="" />
                </Link>

                {/* 배경 오버레이 */}
                <div
                  className={`background-fadeout${isCartOpen ? " visible" : ""}`}
                  onClick={handleCloseCart}
                />

                {/* 장바구니 팝업 */}
                <div
                  className={`hd_gnb_member_cart${isCartOpen ? " open" : ""}`}
                >
                  <div className="hd_gnb_member_cart_status">
                    <div>
                      <p>장바구니에 추가 완료</p>
                    </div>
                    <div>
                      <a className="cart_close_btn" onClick={handleCloseCart}>
                        <img src="/image/close.png" alt="Close" />
                      </a>
                    </div>
                  </div>
                  {cartPreview && (
                    <div className="hd_gnb_member_cart_info">
                      <Link
                        to={`${detailUrl}?productCode=${cartPreview.productCode}`}
                      >
                        <img
                          src={`/files/${folder}/${cartPreview.productCode}/${cartPreview.productFile}`}
                          alt={cartPreview.productName}
                        />
                      </Link>
                      <div className="hd_gnb_member_cart_text">
                        <p className="cart_product_name">
                          {cartPreview.productName}
                        </p>
                        <p className="cart_product_unit">
                          {cartPreview.productUnit}
                        </p>
                        <p className="cart_quantity">
                          {cartPreview.quantity} 개
                        </p>
                        <p className="cart_product_price">
                          {Number(cartPreview.productPrice).toLocaleString(
                            "ko-KR",
                          )}{" "}
                          원
                        </p>
                      </div>
                    </div>
                  )}
                  <div className="hd_gnb_member_cart_btn">
                    <div className="btn">
                      <Link to="/member/memberCart">
                        장바구니 ({cartCount})
                      </Link>
                    </div>
                    <div className="btn">
                      <Link to="/member/memberPayments">결제하기</Link>
                    </div>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
        <div className="hd_lnb">
          <div className="hd_lnb_list">
            <ul>
              <li>
                <Link to="/products/machineDetail">임대머신</Link>
              </li>
              <li>
                <Link to="/products/productList?pageType=bean">원두</Link>
              </li>
              <li>
                <Link to="/products/productList?pageType=mix">커피믹스</Link>
              </li>
              <li>
                <Link to="/products/productList?pageType=cafe">카페용품</Link>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </header>
  );
}

export default Header;
