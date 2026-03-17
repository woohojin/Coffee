import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./store/authStore";
import Layout from "./components/Layout";
import MainPage from "./pages/MainPage";
import ProductListPage from "./pages/ProductListPage";
import MemberSignInPage from "./pages/MemberSignInPage";
import ProductDetailPage from "./pages/ProductDetailPage";
import CartPage from "./pages/CartPage";
import MemberTermsPage from "./pages/MemberTermsPage";
import MemberSignInPage from "./pages/MemberSignInPage";
import MemberSignUpPage from "./pages/MemberSignUpPage";
import MemberMyPage from "./pages/MemberMyPage";
import MemberProfilePage from "./pages/MemberProfilePage";

function ProtectedRoute({ member, loading, children }) {
  if (loading) return <div>로딩중...</div>;
  if (!member) return <Navigate to="/member/memberSignIn" />;
  return children;
}

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route element={<Layout />}>
            <Route path="/main" element={<MainPage />} />
            <Route path="/products/productList" element={<ProductListPage />} />
            <Route
              path="/products/productSearch"
              element={<ProductListPage />}
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
            <Route path="/member/memberSignIn" element={<MemberSignInPage />} />
            <Route path="/member/memberSignUp" element={<MemberSignUpPage />} />
            <Route path="/member/memberCart" element={<CartPage />} />
            <Route path="/member/memberMyPage" element={<MemberMyPage />} />
            <Route
              path="/member/memberProfile"
              element={<MemberProfilePage />}
            />
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
