<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
  <body>
    <main id="index_page">
      <div class="main_slider_wrap">
        <div class="main_slider">
          <ul class="slider">
            <li>
              <a href="">
                <img
                  src="${ pageContext.request.contextPath }/view/image/sample1.png"
                  alt=""
                />
              </a>
            </li>
            <li>
              <a href="">
                <img
                  src="${ pageContext.request.contextPath }/view/image/sample2.png"
                  alt=""
                />
              </a>
            </li>
            <li>
              <a href="">
                <img
                  src="${ pageContext.request.contextPath }/view/image/sample3.png"
                  alt=""
                />
              </a>
            </li>
            <li>
              <a href="">
                <img
                  src="${ pageContext.request.contextPath }/view/image/sample4.png"
                  alt=""
                />
              </a>
            </li>
          </ul>
          <div class="arrow">
            <a href="" class="prev">
              <img
                class="prev_button"
                src="${ pageContext.request.contextPath }/view/image/left-arrow.png"
                alt=""
              />
            </a>
            <a href="" class="next">
              <img
                class="next_button"
                src="${ pageContext.request.contextPath }/view/image/right-arrow.png"
                alt=""
              />
            </a>
          </div>
        </div>
      </div>
      
      <div class="main_product">
        <h1>프리미엄 제품</h1>
        <div class="main_premium_product">
          <ul>
            <li>
              <a href="">
                <img
                  src="${ pageContext.request.contextPath }/view/image/1.jpg"
                  alt=""
                />
              </a>
              <div>
                <p>케냐AA</p>
                <p>10500원</p>
              </div>
            </li>
            <li>
              <a href="">
                <img
                  src="${ pageContext.request.contextPath }/view/image/2.jpg"
                  alt=""
                />
              </a>
              <div>
                <p>예가체프</p>
                <p>10500원</p>
              </div>
            </li>
            <li>
              <a href="">
                <img
                  src="${ pageContext.request.contextPath }/view/image/3.jpg"
                  alt=""
                />
              </a>
              <div>
                <p>탄자니아</p>
                <p>10500원</p>
              </div>
            </li>
            <li>
              <a href="">
                <img
                  src="${ pageContext.request.contextPath }/view/image/4.jpg"
                  alt=""
                />
              </a>
              <div>
                <p>아리차</p>
                <p>10500원</p>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </main>
  </body>
</html>
