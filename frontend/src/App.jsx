import { BrowserRouter, Routes, Route } from "react-router-dom";
import MemberSignIn from "./pages/memberSignIn";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/main" element={<div>메인페이지</div>} />
        <Route path="/member/memberSignIn" element={<MemberSignIn />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
