import { createContext, useContext, useState, useEffect } from "react";
import axiosInstance from "../api/axiosInstance";
import { useAuth } from "./authStore";

const CartContext = createContext(null);

export function CartProvider({ children }) {
  const { member } = useAuth();
  const [cartCount, setCartCount] = useState(0);
  const [cartPreview, setCartPreview] = useState(null);

  useEffect(() => {
    if (!member) {
      setCartCount(0);
      return;
    }
    axiosInstance
      .get("/api/member/cart")
      .then((res) => setCartCount(res.data.data.cartCount))
      .catch(() => setCartCount(0));
  }, [member]);

  const refreshCartCount = async () => {
    try {
      const res = await axiosInstance.get("/api/member/cart");
      setCartCount(res.data.data.cartCount);
    } catch {
      setCartCount(0);
    }
  };

  return (
    <CartContext.Provider
      value={{
        cartCount,
        setCartCount,
        refreshCartCount,
        cartPreview,
        setCartPreview,
      }}
    >
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  return useContext(CartContext);
}
