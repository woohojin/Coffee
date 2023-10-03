<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

  <body>
    <main>
      <div class="main_slider_wrap">
        <div class="main_slider">
          <ul class="slider">
            <li>
              <a href="">
                <img src="${ pageContext.request.contextPath }/view/image/sample1.png" alt="" />
              </a>
            </li>
            <li>
              <a href="">
                <img src="${ pageContext.request.contextPath }/view/image/sample2.png" alt="" />
              </a>
            </li>
            <li>
              <a href="">
                <img src="${ pageContext.request.contextPath }/view/image/sample3.png" alt="" />
              </a>
            </li>
            <li>
              <a href="">
                <img src="${ pageContext.request.contextPath }/view/image/sample4.png" alt="" />
              </a>
            </li>
          </ul>
          <div class="arrow">
            <a href="" class="prev"><</a>
            <a href="" class="next">></a>
          </div>
        </div>
      </div>

      <div class="main_product">
        <h1>Premium Product</h1>
        <div class="main_premium_product">
          <ul>
            <li>
              <div class="product_wrap">
                <a href="">
                  <img src="${ pageContext.request.contextPath }/view/image/1.jpg" alt="" />
                </a>
                <div>
                  <span>케냐AA</span>
                  <span>10500원</span>
                </div>
              </div>
            </li>
            <li>
              <a href="">
                <img src="${ pageContext.request.contextPath }/view/image/2.jpg" alt="" />
              </a>
              <div>
                <span>예가체프</span>
                <span>10500원</span>
              </div>
            </li>
            <li>
              <a href="">
                <img src="${ pageContext.request.contextPath }/view/image/3.jpg" alt="" />
              </a>
              <div>
                <span>탄자니아</span>
                <span>10500원</span>
              </div>
            </li>
            <li>
              <a href="">
                <img src="${ pageContext.request.contextPath }/view/image/4.jpg" alt="" />
              </a>
              <div>
                <span>아리차</span>
                <span>10500원</span>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </main>

    <footer>
      <div class="dc_footer">
        <ul>
          <li>
            <p>사업자 등록번호 : 111-111111</p>
          </li>
          <li>
            <p>
              회사 : 경기도 부천시 도약로 261 (부천대우테크노파크) C동 901호
              (주)다올커피
            </p>
          </li>
          <li>
            <p>이메일 : xxx@gmail.com</p>
          </li>
          <li>
            <p>전화번호 : xxx-xxxx</p>
          </li>
          <li>
            <p></p>
          </li>
        </ul>
      </div>
    </footer>

    <script src="${ pageContext.request.contextPath }/view/js/slider.js"></script>
  </body>
</html>
