import { useState, useEffect, useRef } from "react";
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

    const closeTimer = setTimeout(() => {
      setIsCartOpen(false);
      const clearTimer = setTimeout(() => setCartPreview(null), 700);
      return () => clearTimeout(clearTimer);
    }, 4000);

    return () => clearTimeout(closeTimer); // 새 cartPreview 들어오면 기존 타이머 취소
  }, [cartPreview]);

  // 수동 클릭으로 팝업 닫을 때 타이머도 같이 정리
  const handleCloseCart = () => {
    setIsCartOpen(false);
    if (clearTimerRef.current) clearTimeout(clearTimerRef.current);
    clearTimerRef.current = setTimeout(() => setCartPreview(null), 700);
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
