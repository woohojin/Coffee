import { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

function ProductUpdatePage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const productCode = searchParams.get("productCode");

  const [productType, setProductType] = useState("0");
  const [existProductCode, setExistProductCode] = useState("");
  const [newProductCode, setNewProductCode] = useState("");
  const [productName, setProductName] = useState("");
  const [productPrice, setProductPrice] = useState("");
  const [productUnit, setProductUnit] = useState("");
  const [productTier, setProductTier] = useState("1");
  const [productSoldOut, setProductSoldOut] = useState("0");
  const [files, setFiles] = useState(null);

  // 원두 추가 필드
  const [beanSpecies, setBeanSpecies] = useState("");
  const [beanCompany, setBeanCompany] = useState("");
  const [beanUseByDate, setBeanUseByDate] = useState("");
  const [beanCountry, setBeanCountry] = useState("");

  // 믹스 추가 필드
  const [mixCompany, setMixCompany] = useState("");
  const [mixUseByDate, setMixUseByDate] = useState("");

  useEffect(() => {
    if (!productCode) return;

    axiosInstance
      .get(`/api/admin/products/${productCode}`)
      .then((res) => {
        const p = res.data.data;
        setExistProductCode(p.productCode);
        setNewProductCode(p.productCode);
        setProductType(String(p.productType));
        setProductName(p.productName);
        setProductPrice(String(p.productPrice));
        setProductUnit(p.productUnit);
        setProductTier(String(p.productTier));
        setProductSoldOut(p.productSoldOut ? "1" : "0");

        if (p.bean) {
          setBeanSpecies(p.bean.beanSpecies || "");
          setBeanCompany(p.bean.beanCompany || "");
          setBeanUseByDate(p.bean.beanUseByDate || "");
          setBeanCountry(p.bean.beanCountry || "");
        }
        if (p.mix) {
          setMixCompany(p.mix.mixCompany || "");
          setMixUseByDate(p.mix.mixUseByDate || "");
        }
      })
      .catch((err) => console.error("제품 조회 실패:", err));
  }, [productCode]);

  const handlePriceInput = (e) => {
    setProductPrice(e.target.value.replace(/[^0-9]/g, ""));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append("existProductCode", existProductCode);
    formData.append("productCode", newProductCode);
    formData.append("productType", productType);
    formData.append("productName", productName);
    formData.append("productPrice", productPrice);
    formData.append("productUnit", productUnit);
    formData.append("productTier", productTier);
    formData.append("productSoldOut", productSoldOut);

    if (productType === "0") {
      formData.append("beanSpecies", beanSpecies);
      formData.append("beanCompany", beanCompany);
      formData.append("beanUseByDate", beanUseByDate);
      formData.append("beanCountry", beanCountry);
    } else if (productType === "1") {
      formData.append("mixCompany", mixCompany);
      formData.append("mixUseByDate", mixUseByDate);
    }

    if (files) {
      for (let i = 0; i < files.length; i++) {
        formData.append("files", files[i]);
      }
    }

    try {
      const res = await axiosInstance.put(
        `/api/admin/products/${existProductCode}`,
        formData,
      );
      if (res.data.success) {
        alert("제품 수정에 성공하였습니다.");
        navigate("/admin/productList");
      } else {
        alert(res.data.message || "제품 수정에 실패하였습니다.");
      }
    } catch (err) {
      alert(err.response?.data?.message || "제품 수정 중 오류가 발생했습니다.");
    }
  };

  return (
    <main>
      <div className="admin_page_wrap">
        <div className="page_head">
          <a
            onClick={() => navigate("/admin/productList")}
            style={{ cursor: "pointer" }}
          >
            <h1>제품 수정</h1>
          </a>
        </div>

        <form onSubmit={handleSubmit} encType="multipart/form-data">
          <input type="hidden" value={existProductCode} readOnly />
          <div className="product_form center">
            <ul>
              <li>
                <label>제품 종류 : </label>
                <select
                  className="product_type"
                  value={productType}
                  onChange={(e) => setProductType(e.target.value)}
                  required
                >
                  <option value="0">원두</option>
                  <option value="1">커피믹스</option>
                  <option value="2">카페용품</option>
                </select>
              </li>
              <li>
                <label>제품 코드 : </label>
                <input
                  className="product_code"
                  type="text"
                  value={newProductCode}
                  onChange={(e) => setNewProductCode(e.target.value)}
                  required
                />
              </li>
              <li>
                <label>제품명 : </label>
                <input
                  className="product_name"
                  type="text"
                  value={productName}
                  onChange={(e) => setProductName(e.target.value)}
                  required
                />
              </li>
              <li>
                <label>제품 가격 : </label>
                <input
                  className="product_price"
                  type="text"
                  value={productPrice}
                  onChange={handlePriceInput}
                  placeholder="숫자만 입력"
                  required
                />
              </li>
              <li>
                <label>제품 단위 : </label>
                <input
                  className="product_unit"
                  type="text"
                  value={productUnit}
                  onChange={(e) => setProductUnit(e.target.value)}
                  placeholder="예) 1box / 800g * 12개입"
                  required
                />
              </li>
              <li>
                <label>제품 등급 : </label>
                <select
                  className="product_tier"
                  value={productTier}
                  onChange={(e) => setProductTier(e.target.value)}
                  required
                >
                  <option value="0">0 - 비활성화</option>
                  <option value="1">1 - 임대(카페용품은 전부 등급 1)</option>
                  <option value="2">2 - 미임대</option>
                  <option value="3">3 - 카페고객</option>
                </select>
              </li>
              <li>
                <label>제품 품절 여부 : </label>
                <select
                  className="product_sold_out"
                  value={productSoldOut}
                  onChange={(e) => setProductSoldOut(e.target.value)}
                  required
                  style={{ width: "193px" }}
                >
                  <option value="0">0 - 품절 X</option>
                  <option value="1">1 - 품절 O</option>
                </select>
              </li>

              {/* 원두 추가 필드 */}
              {productType === "0" && (
                <ul>
                  <li>
                    <label>품종 : </label>
                    <input
                      type="text"
                      value={beanSpecies}
                      onChange={(e) => setBeanSpecies(e.target.value)}
                      required
                    />
                  </li>
                  <li>
                    <label>제조사 : </label>
                    <input
                      type="text"
                      value={beanCompany}
                      onChange={(e) => setBeanCompany(e.target.value)}
                      required
                    />
                  </li>
                  <li>
                    <label>소비기한 : </label>
                    <input
                      type="text"
                      value={beanUseByDate}
                      onChange={(e) => setBeanUseByDate(e.target.value)}
                      required
                    />
                  </li>
                  <li>
                    <label>원산지 : </label>
                    <input
                      type="text"
                      value={beanCountry}
                      onChange={(e) => setBeanCountry(e.target.value)}
                      required
                    />
                  </li>
                </ul>
              )}

              {/* 믹스 추가 필드 */}
              {productType === "1" && (
                <ul>
                  <li>
                    <label>제조사 : </label>
                    <input
                      type="text"
                      value={mixCompany}
                      onChange={(e) => setMixCompany(e.target.value)}
                      required
                    />
                  </li>
                  <li>
                    <label>소비기한 : </label>
                    <input
                      type="text"
                      value={mixUseByDate}
                      onChange={(e) => setMixUseByDate(e.target.value)}
                      required
                    />
                  </li>
                </ul>
              )}

              <li>
                <label>제품 파일 : </label>
                <input
                  className="file"
                  type="file"
                  multiple
                  onChange={(e) => setFiles(e.target.files)}
                />
              </li>
              <li>
                <div className="submit">
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

export default ProductUpdatePage;
