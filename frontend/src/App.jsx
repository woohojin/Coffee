import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "./store/authStore";
import MemberSignIn from "./pages/MemberSignIn";

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
        <Route
          path="/main"
          element={
            <ProtectedRoute member={member} loading={loading}>
              <div>메인페이지 - {member?.memberName}</div>
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
