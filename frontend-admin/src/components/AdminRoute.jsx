import { useEffect } from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAdminAuth } from "../store/adminAuthStore";

function AdminRoute({ children }) {
  const { admin, loading, checkAuth } = useAdminAuth();
  const location = useLocation();

  useEffect(() => {
    checkAuth();
  }, [location.pathname, checkAuth]);

  if (loading) return <div>로딩중...</div>;
  if (!admin) return <Navigate to="/admin/login" />;

  return children;
}

export default AdminRoute;
