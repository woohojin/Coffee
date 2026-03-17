import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import { useCart } from "../store/cartStore";

export function useCartPreview() {
  const location = useLocation();
  const { cartPreview, setCartPreview } = useCart();
  const [isCartOpen, setIsCartOpen] = useState(false);

  // 페이지 이동 시 팝업 닫기
  useEffect(() => {
    setIsCartOpen(false);
    setCartPreview(null);
  }, [location.pathname]);

  // cartPreview 세팅되면 팝업 열고 4초 후 자동 닫기
  useEffect(() => {
    if (!cartPreview) return;
    setIsCartOpen(true);
    const timer = setTimeout(() => {
      setIsCartOpen(false);
      setTimeout(() => setCartPreview(null), 700);
    }, 4000);
    return () => clearTimeout(timer);
  }, [cartPreview]);

  const handleCloseCart = () => {
    setIsCartOpen(false);
    setTimeout(() => setCartPreview(null), 700);
  };

  const folder =
    cartPreview?.productType === 0
      ? "bean"
      : cartPreview?.productType === 1
        ? "mix"
        : "cafe";

  const detailUrl =
    cartPreview?.productType === 0
      ? "/products/beanDetail"
      : cartPreview?.productType === 1
        ? "/products/mixDetail"
        : "/products/cafeDetail";

  return { isCartOpen, handleCloseCart, cartPreview, folder, detailUrl };
}
