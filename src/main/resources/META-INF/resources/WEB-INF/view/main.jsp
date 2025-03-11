<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<body>
  <main id="index_page">
<%--    <div class="main_slider_wrap">--%>
<%--      <div class="main_slider">--%>
<%--        <ul class="slider">--%>
<%--          <li>--%>
<%--            <a href="">--%>
<%--              <img--%>
<%--                src="${ pageContext.request.contextPath }/image/sample1.png"--%>
<%--                alt=""--%>
<%--              />--%>
<%--            </a>--%>
<%--          </li>--%>
<%--          <li>--%>
<%--            <a href="">--%>
<%--              <img--%>
<%--                src="${ pageContext.request.contextPath }/image/sample2.png"--%>
<%--                alt=""--%>
<%--              />--%>
<%--            </a>--%>
<%--          </li>--%>
<%--          <li>--%>
<%--            <a href="">--%>
<%--              <img--%>
<%--                src="${ pageContext.request.contextPath }/image/sample3.png"--%>
<%--                alt=""--%>
<%--              />--%>
<%--            </a>--%>
<%--          </li>--%>
<%--          <li>--%>
<%--            <a href="">--%>
<%--              <img--%>
<%--                src="${ pageContext.request.contextPath }/image/sample4.png"--%>
<%--                alt=""--%>
<%--              />--%>
<%--            </a>--%>
<%--          </li>--%>
<%--        </ul>--%>
<%--        <div class="arrow">--%>
<%--          <a href="" class="prev">--%>
<%--            <img--%>
<%--              class="prev_button"--%>
<%--              src="${ pageContext.request.contextPath }/image/left-arrow.png"--%>
<%--              alt=""--%>
<%--            />--%>
<%--          </a>--%>
<%--          <a href="" class="next">--%>
<%--            <img--%>
<%--              class="next_button"--%>
<%--              src="${ pageContext.request.contextPath }/image/right-arrow.png"--%>
<%--              alt=""--%>
<%--            />--%>
<%--          </a>--%>
<%--        </div>--%>
<%--      </div>--%>
<%--    </div>--%>

  <c:choose>
    <c:when test="${ sessionScope.memberTier != 0 }">
      <div class="main_product">
        <h1>인기 제품</h1>
        <div class="main_product_list">
          <ul>
            <li>
              <a href="">
                <img
                  src="${ pageContext.request.contextPath }/image/1.jpg"
                  alt=""
                />
              </a>
              <div class="center">
                <a>변경예정</a>
              </div>
            </li>
            <li>
              <a href="">
                <img
                  src="${ pageContext.request.contextPath }/image/2.jpg"
                  alt=""
                />
              </a>
              <div class="center">
                <a>변경예정</a>
              </div>
            </li>
            <li>
              <a href="">
                <img
                  src="${ pageContext.request.contextPath }/image/3.jpg"
                  alt=""
                />
              </a>
              <div class="center">
                <a>변경예정</a>
              </div>
            </li>
            <li>
              <a href="">
                <img
                  src="${ pageContext.request.contextPath }/image/4.jpg"
                  alt=""
                />
              </a>
              <div class="center">
                <a>변경예정</a>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </c:when>

    <c:when test="${ sessionScope.memberId == null }">
      <div class="denied-text">
        <p>로그인을 진행하시거나</p>
        <br/>
        <p>최초 회원가입 진행 후에 1566-0904로 연락 부탁드립니다.</p>
      </div>
    </c:when>

    <c:when test="${ sessionScope.memberId != null && sessionScope.memberTier == 0 }">
      <div class="denied-text">
        <p>가입 승인 대기중입니다.</p>
        <br/>
        <p>문의사항이 있으시면 1566-0904로 연락 부탁드립니다.</p>
      </div>
    </c:when>
  </c:choose>

<%--  <div class="main_product">--%>
<%--    <h1>프리미엄 제품</h1>--%>
<%--    <div class="main_product_list">--%>
<%--      <ul>--%>
<%--        <li>--%>
<%--          <a href="">--%>
<%--            <img--%>
<%--              src="${ pageContext.request.contextPath }/image/1.jpg"--%>
<%--              alt=""--%>
<%--            />--%>
<%--          </a>--%>
<%--          <div class="center">--%>
<%--            <a>케냐AA</a>--%>
<%--          </div>--%>
<%--        </li>--%>
<%--        <li>--%>
<%--          <a href="">--%>
<%--            <img--%>
<%--              src="${ pageContext.request.contextPath }/image/2.jpg"--%>
<%--              alt=""--%>
<%--            />--%>
<%--          </a>--%>
<%--          <div class="center">--%>
<%--            <a>예가체프</a>--%>
<%--          </div>--%>
<%--        </li>--%>
<%--        <li>--%>
<%--          <a href="">--%>
<%--            <img--%>
<%--              src="${ pageContext.request.contextPath }/image/3.jpg"--%>
<%--              alt=""--%>
<%--            />--%>
<%--          </a>--%>
<%--          <div class="center">--%>
<%--            <a>탄자니아</a>--%>
<%--          </div>--%>
<%--        </li>--%>
<%--        <li>--%>
<%--          <a href="">--%>
<%--            <img--%>
<%--              src="${ pageContext.request.contextPath }/image/4.jpg"--%>
<%--              alt=""--%>
<%--            />--%>
<%--          </a>--%>
<%--          <div class="center">--%>
<%--            <a>아리차</a>--%>
<%--          </div>--%>
<%--        </li>--%>
<%--      </ul>--%>
<%--    </div>--%>
<%--  </div>--%>
  </main>
  <script src="${ pageContext.request.contextPath }/js/slider.js"></script>
  <script>
    document.addEventListener("DOMContentLoaded", function() {
      const memberId = "${ sessionScope.memberId }";
      const memberTier = ${ sessionScope.memberTier };

      if(memberId.length > 0 && memberTier === 0) {
        alert("현재 가입 승인 대기중입니다.");
      }
    });
  </script>
</body>
