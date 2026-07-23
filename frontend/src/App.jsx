import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./store/authStore";
import { CartProvider } from "./store/cartStore";
import Layout from "./components/Layout";
import MainPage from "./pages/MainPage";
import PrivacyPage from "./pages/PrivacyPage";
import ProductListPage from "./pages/ProductListPage";
import ProductDetailPage from "./pages/ProductDetailPage";
import MachineDetailPage from "./pages/MachineDetailPage";
import CartPage from "./pages/CartPage";
import MemberTermsPage from "./pages/MemberTermsPage";
import MemberSignInPage from "./pages/MemberSignInPage";
import MemberSignUpPage from "./pages/MemberSignUpPage";
import MemberFindAccountPage from "./pages/MemberFindAccountPage";
import MemberMyPage from "./pages/MemberMyPage";
import MemberProfilePage from "./pages/MemberProfilePage";
import MemberHistoryPage from "./pages/MemberHistory";
import MemberPaymentsPage from "./pages/MemberPaymentsPage";
import MemberPaymentsSuccessPage from "./pages/MemberPaymentsSuccessPage";
import MemberPaymentsFailurePage from "./pages/MemberPaymentsFailurePage";

function ProtectedRoute({ member, loading, children }) {
  if (loading) return <div>로딩중...</div>;
  if (!member) return <Navigate to="/member/memberSignIn" />;
  return children;
}

function App() {
  return (
    <AuthProvider>
      <CartProvider>
        <BrowserRouter>
          <Routes>
            <Route element={<Layout />}>
              <Route path="/main" element={<MainPage />} />
              <Route path="/privacy" element={<PrivacyPage />} />
              <Route
                path="/products/productList"
                element={<ProductListPage />}
              />
              <Route
                path="/products/productSearch"
                element={<ProductListPage />}
              />
              <Route
                path="/products/machineDetail"
                element={<MachineDetailPage />}
              />
              <Route
                path="/products/beanDetail"
                element={<ProductDetailPage pageType="bean" />}
              />
              <Route
                path="/products/mixDetail"
                element={<ProductDetailPage pageType="mix" />}
              />
              <Route
                path="/products/cafeDetail"
                element={<ProductDetailPage pageType="cafe" />}
              />

              <Route path="/member/memberTerms" element={<MemberTermsPage />} />
              <Route
                path="/member/memberSignIn"
                element={<MemberSignInPage />}
              />
              <Route
                path="/member/memberSignUp"
                element={<MemberSignUpPage />}
              />
              <Route
                path="/member/memberFindAccount"
                element={<MemberFindAccountPage />}
              />

              <Route path="/member/memberCart" element={<CartPage />} />
              <Route
                path="/member/memberPayments"
                element={<MemberPaymentsPage />}
              />
              <Route
                path="/member/memberPaymentsSuccess"
                element={<MemberPaymentsSuccessPage />}
              />
              <Route
                path="/member/memberPaymentsFailure"
                element={<MemberPaymentsFailurePage />}
              />

              <Route path="/member/memberMyPage" element={<MemberMyPage />} />
              <Route
                path="/member/memberProfile"
                element={<MemberProfilePage />}
              />
              <Route
                path="/member/memberHistory"
                element={<MemberHistoryPage />}
              />
            </Route>
          </Routes>
        </BrowserRouter>
      </CartProvider>
    </AuthProvider>
  );
}

export default App;
