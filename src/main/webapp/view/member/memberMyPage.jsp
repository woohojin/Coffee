<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<body>
  <main id="member_mypage_page">
    <div class="member_mypage_page_wrap">
      <div class="page_head">
        <h1>마이페이지</h1>
      </div>
      <table class="member_mypage_wrap center">
        <tbody>
          <tr>
            <td>
                <a href="${ pageContext.request.contextPath }/member/memberProfile">
                  회원정보 수정
                  <p>
                    고객님의 회원정보를
                    <br/>
                    수정 할 수 있습니다.
                  </p>
                </a>
            </td>
            <td>
                <a>
                  배송 조회
                  <p>
                    고객님의 상품 배송정보를
                    <br/>
                    확인 할 수 있습니다.
                  </p>
                </a>
            </td>
            <td>
              <a href="${ pageContext.request.contextPath }/member/memberHistory">
                주문기록 조회
                <p>
                  고객님께서 주문했던 상품 정보를
                  <br/>
                  확인 할 수 있습니다.
                </p>
              </a>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </main>
</body>
