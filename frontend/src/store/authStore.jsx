import { createContext, useContext, useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [member, setMember] = useState(null);
  const [loading, setLoading] = useState(true);
  const location = useLocation();

  // 페이지 이동마다 재확인 - 다른 요청이 백그라운드에서 토큰을 재발급받아도
  // 여기서 다시 확인해야 로그인 상태 UI가 최신 상태로 반영됨
  useEffect(() => {
    axiosInstance
      .get("/api/member/me")
      .then((res) => setMember(res.data.data))
      .catch(() => setMember(null))
      .finally(() => setLoading(false));
  }, [location.pathname]);

  return (
    <AuthContext.Provider value={{ member, loading, setMember }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
