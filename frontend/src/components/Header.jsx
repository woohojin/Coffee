import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../store/authStore";
import axiosInstance from "../api/axiosInstance";
import { useState, useEffect } from "react";

function Header() {
  const navigate = useNavigate();
  const { member, setMember } = useAuth();
  const [searchText, setSearchText] = useState("");
  const [cartCount, setCartCount] = useState(0);

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

  useEffect(() => {
    if (!member) return;
    axiosInstance
      .get("/api/member/cart")
      .then((res) => setCartCount(res.data.data.cartCount))
      .catch(() => setCartCount(0));
  }, [member]);

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
              {!member && (
                <>
                  <li>
                    <Link to="/member/memberSignIn">로그인</Link>
                  </li>
                  <li>
                    <Link to="/member/memberTerms">회원가입</Link>
                  </li>
                </>
              )}
              {member && member.memberTier !== 9 && (
                <>
                  <li>
                    <button onClick={handleLogout}>로그아웃</button>
                  </li>
                  <li>
                    <Link to="/member/memberMyPage">마이페이지</Link>
                  </li>
                </>
              )}
              {member && member.memberTier === 9 && (
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
              </li>
            </ul>
          </div>
        </div>
        <div className="hd_lnb">
          <div className="hd_lnb_list">
            <ul>
              <li>
                <Link to="/products/productList?pageType=bean">원두</Link>
              </li>
              <li>
                <Link to="/products/productList?pageType=mix">커피믹스</Link>
              </li>
              <li>
                <Link to="/products/productList?pageType=cafe">카페용품</Link>
              </li>
              <li>
                <Link to="/products/machineDetail">임대머신</Link>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </header>
  );
}

export default Header;
