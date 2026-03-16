import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "./store/authStore";
import MemberSignIn from "./pages/MemberSignIn";
import MainPage from "./pages/MainPage";
import Layout from "./components/Layout";

function ProtectedRoute({ member, loading, children }) {
  if (loading) return <div>로딩중...</div>;
  if (!member) return <Navigate to="/member/memberSignIn" />;
  return children;
}

function App() {
  const { member, loading, setMember } = useAuth();

  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/member/memberSignIn"
          element={<MemberSignIn setMember={setMember} />}
        />
        <Route element={<Layout />}>
          <Route path="/main" element={<MainPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
