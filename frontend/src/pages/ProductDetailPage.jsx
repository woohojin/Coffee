import { useState, useEffect, useRef } from "react";
import { useSearchParams } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";
import { useScrollTo } from "../hooks/useScrollTo";
import { useCart } from "../store/cartStore";

function formatPrice(price) {
  if (!price) return "0";
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function ProductDetailPage({ pageType }) {
  const [searchParams] = useSearchParams();
  const productCode = searchParams.get("productCode") || "";
  const [data, setData] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const { refs, scrollTo } = useScrollTo();
  const { setCartPreview, refreshCartCount } = useCart();

  useEffect(() => {
    axiosInstance
      .get(`/api/products/${productCode}?pageType=${pageType}`)
      .then((res) => setData(res.data.data))
      .catch((err) => console.error("상품 상세 로드 실패:", err));
  }, [productCode, pageType]);

  if (!data)
    return (
      <main id="product_detail_page">
        <p>로딩 중...</p>
      </main>
    );

  if (data.productCount === 0 || !data.product)
    return (
      <main id="product_detail_page">
        <p style={{ textAlign: "center" }}>제품을 찾을 수 없습니다.</p>
      </main>
    );

  const p = data.product;
  const b = data.bean;
  const m = data.mix;
  const basePath = `/files/${pageType}/${p.productCode}/`;
  const hasAdditional = pageType === "bean" || pageType === "mix";

  const handleCartSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    try {
      const res = await axiosInstance.post("/api/member/cart/items", formData);
      const cartDTO = res.data.data;
      await refreshCartCount();
      setCartPreview(cartDTO);
    } catch (err) {
      console.error("장바구니 추가 실패:", err);
    }
  };

  return (
    <main id="product_detail_page">
      <div>
        <div className="product_detail_wrap">
          <div className="product_detail">
            <div className="product_detail_img">
              <img src={`${basePath}${p.productFile}`} alt={p.productName} />
            </div>
            <div className="product_info">
              <h1>{p.productName}</h1>
              <table>
                <tbody>
                  {pageType === "bean" && (
                    <>
                      <tr>
                        <th>
                          <span>식품유형</span>
                        </th>
                        <td>
                          <span>원두커피 100%</span>
                        </td>
                      </tr>
                      <tr>
                        <th>
                          <span>원산지</span>
                        </th>
                        <td>
                          <span>{b?.beanCountry || ""}</span>
                        </td>
                      </tr>
                      <tr>
                        <th>
                          <span>품종</span>
                        </th>
                        <td>
                          <span>{b?.beanSpecies || ""}</span>
                        </td>
                      </tr>
                      <tr>
                        <th>
                          <span>제조사</span>
                        </th>
                        <td>
                          <span>{b?.beanCompany || ""}</span>
                        </td>
                      </tr>
                      <tr>
                        <th>
                          <span>소비기한</span>
                        </th>
                        <td>
                          <span>{b?.beanUseByDate || ""}</span>
                        </td>
                      </tr>
                      <tr>
                        <th>
                          <span>용량</span>
                        </th>
                        <td>
                          <span>{p.productUnit || ""}</span>
                        </td>
                      </tr>
                    </>
                  )}
                  {pageType === "mix" && (
                    <>
                      <tr>
                        <th>
                          <span>식품유형</span>
                        </th>
                        <td>
                          <span>커피믹스</span>
                        </td>
                      </tr>
                      <tr>
                        <th>
                          <span>제조사</span>
                        </th>
                        <td>
                          <span>{m?.mixCompany || ""}</span>
                        </td>
                      </tr>
                      <tr>
                        <th>
                          <span>소비기한</span>
                        </th>
                        <td>
                          <span>{m?.mixUseByDate || ""}</span>
                        </td>
                      </tr>
                      <tr>
                        <th>
                          <span>용량</span>
                        </th>
                        <td>
                          <span>{p.productUnit || ""}</span>
                        </td>
                      </tr>
                    </>
                  )}
                  {pageType === "cafe" && (
                    <>
                      <tr>
                        <th>
                          <span>용량</span>
                        </th>
                        <td>
                          <span>{p.productUnit || ""}</span>
                        </td>
                      </tr>
                    </>
                  )}
                  <tr>
                    <th>
                      <span>배송비</span>
                    </th>
                    <td>
                      <span>3,000 원 (원두 2kg 이상 구매시 무료)</span>
                    </td>
                  </tr>
                  <tr>
                    <th>
                      <span>가격</span>
                    </th>
                    <td>
                      <span className="product_default_price">
                        {formatPrice(p.productPrice)} 원
                      </span>
                    </td>
                  </tr>
                </tbody>
              </table>
              <div>
                <form
                  className="product_quantity_form"
                  onSubmit={handleCartSubmit}
                >
                  <input
                    type="hidden"
                    name="productCode"
                    value={p.productCode}
                  />
                  <p>
                    {p.productName} {p.productUnit}
                  </p>
                  <div className="product_quantity">
                    <input
                      type="text"
                      className="product_quantity_input"
                      value={quantity}
                      name="quantity"
                      readOnly
                    />
                    <div className="product_quantity_btn">
                      <button
                        type="button"
                        className="left_btn center decrease-btn"
                        onClick={() => setQuantity((q) => Math.max(1, q - 1))}
                      >
                        <img src="/image/minus.png" alt="" />
                      </button>
                      <button
                        type="button"
                        className="right_btn center increase-btn"
                        onClick={() => setQuantity((q) => q + 1)}
                      >
                        <img src="/image/plus.png" alt="" />
                      </button>
                    </div>
                    <div className="product_quantity_price">
                      {formatPrice(p.productPrice * quantity)} 원
                    </div>
                  </div>
                  {hasAdditional && (
                    <div className="product_additional">
                      <span>추가 상품 선택</span>
                      <select name="additionalProducts">
                        <option value="none">없음</option>
                        <option value="CA0001">
                          종이컵 6.5온스 1000개입 (+12,000원)
                        </option>
                        <option value="CA0003">
                          종이컵 8온스 1000개입 (+48,400원)
                        </option>
                        <option value="CA0010">
                          종이컵 뚜껑 8온스 1000개입 (+25,300원)
                        </option>
                        <option value="CA0101">
                          아이스컵 14온스 1000개입 (+69,300원)
                        </option>
                        <option value="CA0105">
                          아이스컵 뚜껑 14온스 1000개입 (+36,300원)
                        </option>
                        <option value="CA0210">
                          스트로우 자바라 400개입 (+3,000원)
                        </option>
                        <option value="CA0214">
                          스틱 15cm 검정 1000개입 (+2,800원)
                        </option>
                        <option value="CA0202">시럽 펌프 (+3,000원)</option>
                        <option value="CA0501">
                          카페 시럽 1.5L (+4,400원)
                        </option>
                        <option value="CA0520">
                          대한제당 스틱설탕 5g x 100개입 (+2,000원)
                        </option>
                      </select>
                    </div>
                  )}
                  {p.productSoldOut === 1 ? (
                    <div className="product_quantity_submit">
                      <br />
                      <p>Sold Out</p>
                    </div>
                  ) : (
                    <div className="product_quantity_submit">
                      <input
                        type="submit"
                        value="장바구니에 담기"
                        className="submit_btn"
                      />
                    </div>
                  )}
                </form>
              </div>
            </div>
          </div>
        </div>

        <ul className="product_detail_main">
          <li>
            <div className="product_detail_list scroll1" ref={refs.scroll1}>
              <ul>
                <li className="active">
                  <a className="scrollBtn1" onClick={() => scrollTo("scroll1")}>
                    상품상세정보
                  </a>
                </li>
                <li>
                  <a className="scrollBtn2" onClick={() => scrollTo("scroll2")}>
                    배송안내
                  </a>
                </li>
                <li>
                  <a className="scrollBtn3" onClick={() => scrollTo("scroll3")}>
                    교환 및 반품안내
                  </a>
                </li>
              </ul>
            </div>
            <div className="product_detail_content_wrap">
              <img
                src={`${basePath}${data.detailImageName}`}
                alt="상세이미지"
              />
            </div>
          </li>
          <li>
            <div className="product_detail_list scroll2" ref={refs.scroll2}>
              <ul>
                <li className="active">
                  <a className="scrollBtn1" onClick={() => scrollTo("scroll1")}>
                    상품상세정보
                  </a>
                </li>
                <li>
                  <a className="scrollBtn2" onClick={() => scrollTo("scroll2")}>
                    배송안내
                  </a>
                </li>
                <li>
                  <a className="scrollBtn3" onClick={() => scrollTo("scroll3")}>
                    교환 및 반품안내
                  </a>
                </li>
              </ul>
            </div>
            <div className="product_detail_content_wrap">
              <div className="product_detail_content">
                <ul>
                  <li>
                    <p>배송 방법 : 로젠 택배</p>
                  </li>
                  <li>
                    <p>배송 비용 : 3000원 (원두 2KG 이상 구매시 무료)</p>
                  </li>
                  <li>
                    <p>배송 기간 : 1일 - 3일</p>
                  </li>
                  <li>
                    <p>
                      당일 오후 2시 이전 주문 시 당일 발송 이후 주문시 익일 발송
                    </p>
                  </li>
                  <li>
                    <p>일부 상품은 구매 시 건당 배송 비용이 발생합니다.</p>
                  </li>
                </ul>
              </div>
            </div>
          </li>
          <li>
            <div className="product_detail_list scroll3" ref={refs.scroll3}>
              <ul>
                <li className="active">
                  <a className="scrollBtn1" onClick={() => scrollTo("scroll1")}>
                    상품상세정보
                  </a>
                </li>
                <li>
                  <a className="scrollBtn2" onClick={() => scrollTo("scroll2")}>
                    배송안내
                  </a>
                </li>
                <li>
                  <a className="scrollBtn3" onClick={() => scrollTo("scroll3")}>
                    교환 및 반품안내
                  </a>
                </li>
              </ul>
            </div>
            <div className="product_detail_content_wrap">
              <div className="product_detail_content">
                <ul>
                  <li>
                    <h1>교환 및 반품이 가능한 경우</h1>
                  </li>
                  <li>
                    <p>출고일로부터 7일이내 요청시 환불 가능합니다.</p>
                    <p>신청 방법 : 032-233-7400으로 연락</p>
                    <p>배송 비용 : 단순 변심 시 왕복 택배비 6,000원</p>
                    <p>
                      반품 주소 : 부천시 도약로 261 C동 902-1호
                      (부천대우테크노파크)
                    </p>
                  </li>
                  <li>
                    <p>
                      공급받으신 상품 및 용역의 내용이 표시.광고 내용과 다르거나
                      다르게 이행된 경우에는
                    </p>
                    <p>
                      공급받은 날로부터 3개월이내, 그사실을 알게 된 날로부터
                      30일이내 환불 가능합니다.
                    </p>
                  </li>
                </ul>
                <ul>
                  <li>
                    <h1>교환 및 반품이 불가능한 경우</h1>
                  </li>
                  <li>
                    <p>
                      포장을 개봉하였거나 포장이 훼손되어 상품가치가 상실된 경우
                    </p>
                  </li>
                  <li>
                    <p>
                      고객님의 사용 또는 일부 소비에 의하여 상품의 가치가 현저히
                      감소한 경우
                    </p>
                  </li>
                  <li>
                    <p>분쇄상태를 선택 시 환불이 불가합니다.</p>
                  </li>
                </ul>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </main>
  );
}

export default ProductDetailPage;
