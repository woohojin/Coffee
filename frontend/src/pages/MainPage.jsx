import { useAuth } from "../store/authStore";

function MainPage() {
  const { member, loading } = useAuth();

  let content;
  if (loading) {
    content = null;
  } else if (member === null) {
    content = (
      <div className="denied-text">
        <p>로그인을 진행하시거나</p>
        <p>최초 회원가입 진행 후에 1566-0904로 연락 부탁드립니다.</p>
      </div>
    );
  } else if (member.memberTier === 0) {
    content = (
      <div className="denied-text">
        <p>가입 승인 대기중입니다.</p>
        <p>문의사항이 있으시면 1566-0904로 연락 부탁드립니다.</p>
      </div>
    );
  } else {
    content = (
      <div className="main_product">
        <h1>인기 제품</h1>
        <div className="main_product_list">
          <ul>
            <li>
              <img src="/image/1.jpg" alt="" />
              <div className="center">
                <a>변경예정</a>
              </div>
            </li>
            <li>
              <img src="/image/2.jpg" alt="" />
              <div className="center">
                <a>변경예정</a>
              </div>
            </li>
            <li>
              <img src="/image/3.jpg" alt="" />
              <div className="center">
                <a>변경예정</a>
              </div>
            </li>
            <li>
              <img src="/image/4.jpg" alt="" />
              <div className="center">
                <a>변경예정</a>
              </div>
            </li>
          </ul>
        </div>
      </div>
    );
  }

  return <main id="index_page">{content}</main>;
}

export default MainPage;
