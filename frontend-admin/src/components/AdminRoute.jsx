import { Navigate } from "react-router-dom";
import { useAdminAuth } from "../store/adminAuthStore";

function AdminRoute({ children }) {
  const { admin, loading } = useAdminAuth();

  if (loading) return <div>로딩중...</div>;
  if (!admin) return <Navigate to="/admin/login" />;

  return children;
}

export default AdminRoute;
