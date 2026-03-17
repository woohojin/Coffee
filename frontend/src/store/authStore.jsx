import { createContext, useContext, useState, useEffect } from "react";
import axiosInstance from "../api/axiosInstance";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [member, setMember] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axiosInstance
      .get("/api/member/me")
      .then((res) => setMember(res.data.data))
      .catch(() => setMember(null))
      .finally(() => setLoading(false));
  }, []);

  return (
    <AuthContext.Provider value={{ member, loading, setMember }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
