import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AdminAuthProvider } from "./store/adminAuthStore";
import AdminRoute from "./components/AdminRoute";
import AdminLayout from "./components/AdminLayout";
import AdminLoginPage from "./pages/AdminLoginPage";
import DashboardPage from "./pages/DashboardPage";
import OrderHistoryPage from "./pages/OrderHistoryPage";
import MemberListPage from "./pages/MemberListPage";
import MemberTierUpdatePage from "./pages/MemberTierUpdatePage";
import MemberDisablePage from "./pages/MemberDisablePage";
import MemberWithdrawalPage from "./pages/MemberWithdrawalPage";
import ProductListPage from "./pages/ProductListPage";
import ProductUploadPage from "./pages/ProductUploadPage";
import ProductDeletePage from "./pages/ProductDeletePage";

function App() {
  return (
    <AdminAuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/admin/login" element={<AdminLoginPage />} />
          <Route
            element={
              <AdminRoute>
                <AdminLayout />
              </AdminRoute>
            }
          >
            <Route path="/admin/dashboard" element={<DashboardPage />} />
            <Route path="/admin/orderHistory" element={<OrderHistoryPage />} />
            <Route path="/admin/memberList" element={<MemberListPage />} />
            <Route
              path="/admin/memberTierUpdate"
              element={<MemberTierUpdatePage />}
            />
            <Route
              path="/admin/memberDisableUpdate"
              element={<MemberDisablePage />}
            />
            <Route
              path="/admin/memberWithdrawalList"
              element={<MemberWithdrawalPage />}
            />
            <Route path="/admin/productList" element={<ProductListPage />} />
            <Route
              path="/admin/productUpload"
              element={<ProductUploadPage />}
            />
            <Route
              path="/admin/productDelete"
              element={<ProductDeletePage />}
            />
          </Route>
          <Route path="*" element={<Navigate to="/admin/dashboard" />} />
        </Routes>
      </BrowserRouter>
    </AdminAuthProvider>
  );
}

export default App;
