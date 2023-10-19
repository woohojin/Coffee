<head>
  <link rel="stylesheet" href="board.css" />
  <link rel="stylesheet" href="font.css" />
  <link rel="stylesheet" href="variables.css" />
</head>
<html lang="en">
<body>
<main>
  <div class="product_detail_wrap">
    <div class="product_detail">
      <div class="product_detail_img">
        <img src="${ pageContext.request.contextPath }/view/image/1.jpg" alt="" />
      </div>
      <div class="product_info_wrap">
        <h1>레브 프리미엄 200g</h1>
        <table>
          <tbody>
          <tr>
            <th>
              <span>원산지</span>
            </th>
            <td>
              <span>인도</span>
            </td>
          </tr>
          <tr>
            <th>
              <span>품종</span>
            </th>
            <td>
              <span>아라비카</span>
            </td>
          </tr>
          <tr>
            <th>
              <span>제조사</span>
            </th>
            <td>
              <span>다올커피</span>
            </td>
          </tr>
          <tr>
            <th>
              <span>용량</span>
            </th>
            <td>
              <span>200g</span>
            </td>
          </tr>
          <tr>
            <th>
              <span>배송비</span>
            </th>
            <td>
              <span>3000원 (30000원 이상 구매시 무료)</span>
            </td>
          </tr>
          <tr>
            <th>
              <span>가격</span>
            </th>
            <td>
              <span>23100원</span>
            </td>
          </tr>
          </tbody>
        </table>
        <div>
          <form class="product_quantity_form">
            <p>레브 프리미엄 200g</p>
            <div class="product_quantity_input">
              <input type="text" id="product_quantity" value="1" />
              <div class="product_quantity_button">
                <button type="button" class="left_button">-</button>
                <button type="button" class="right_button">+</button>
              </div>
              <div class="product_quantity_price">
                <span>23100</span>
                <span>원</span>
                <img src="${ pageContext.request.contextPath }/view/image/close.png" alt="X" />
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</main>
</body>
</html>
