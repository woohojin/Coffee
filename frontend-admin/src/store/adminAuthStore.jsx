import { createContext, useContext, useState, useEffect } from "react";
import axiosInstance from "../api/axiosInstance";

const AdminAuthContext = createContext(null);

export function AdminAuthProvider({ children }) {
  const [admin, setAdmin] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axiosInstance
      .get("/api/admin/me")
      .then((res) => setAdmin(res.data.data))
      .catch(() => setAdmin(null))
      .finally(() => setLoading(false));
  }, []);

  return (
    <AdminAuthContext.Provider value={{ admin, setAdmin, loading }}>
      {children}
    </AdminAuthContext.Provider>
  );
}

export function useAdminAuth() {
  return useContext(AdminAuthContext);
}
