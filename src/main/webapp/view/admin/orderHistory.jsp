<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<body>
<main id="admin_page">
  <div class="admin_page_wrap">
    <div class="page_head">
      <a href="${ pageContext.request.contextPath }/admin/orderHistory">
        <h1>주문 기록</h1>
      </a>
    </div>
    <div class="excel_download">
      <a href="${ pageContext.request.contextPath }/admin/excelHistoryDownload">주문기록 엑셀 다운로드</a>
    </div>
    <div class="search_form_wrap center">
      <div class="inline_wrap">
        <form class="search_form"
              action="${ pageContext.request.contextPath }/admin/historySearch"
              method="post"
              style="max-height: 70px;"
        >
          <div>
            <label for="datepickerStart">시작일</label>
            <input type="text" name="startDate" id="datepickerStart" class="datepicker" value="${ requestScope.startDate }" />
            <label for="datepickerEnd">종료일</label>
            <input type="text" name="endDate" id="datepickerEnd" class="datepicker" value="${ requestScope.endDate }" />
          </div>
          <div>
            <label for="orderId">주문번호</label>
            <input type="text" id="orderId" name="orderId" value="${ requestScope.orderId }">
            <label for="memberId">아이디</label>
            <input type="text" id="memberId" name="memberId" value="${ requestScope.memberId }">
          </div>
        </form>
        <div class="collapse" onclick="expand(this)">
          <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
        </div>
      </div>
    </div>
    <div class="btn_wrap center">
      <div class="btn">
        <a onclick="submit()">검색</a>
      </div>
    </div>
    <div class="list">
      <ul class="center">
        <c:choose>
          <c:when test="${ requestScope.memberTier != '9' }">
            <p>권한이 부족합니다.</p>
          </c:when>
          <c:when test="${ requestScope.memberTier == '9' && requestScope.historyCount == 0 }">
            <p>기록을 찾을 수 없습니다.</p>
          </c:when>
          <c:when test="${ requestScope.memberTier == '9' }">
            <c:if test="${ requestScope.historySearchCount != 0 || requestScope.historyCount != 0 }">
              <form
                action="${ pageContext.request.contextPath }/admin/orderHistoryPro"
                method="post"
                id="orderByForm"
              >
                <input id="columnName" type="hidden" name="columnName" value="${ requestScope.columnName }"/>
                <input id="orderBy" type="hidden" name="orderBy" value="${ requestScope.orderBy }"/>
              </form>
              <table class="list">
                <thead>
                <tr>
                    <th class="history_code" onclick="orderBy(this)">
                    <div class="asc">
                      <span>주문번호</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="member_id" onclick="orderBy(this)">
                    <div class="asc">
                      <span>아이디</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_code" onclick="orderBy(this)">
                    <div class="asc">
                      <span>제품코드</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_name" onclick="orderBy(this)">
                    <div class="asc">
                      <span>제품명</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="product_price" onclick="orderBy(this)">
                    <div class="asc">
                      <span>제품가격</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="quantity" onclick="orderBy(this)">
                    <div class="asc">
                      <span>수량</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
<%--                  <th class="product_grinding" onclick="orderBy(this)">--%>
<%--                    <div class="asc">--%>
<%--                      <span>분쇄상태</span>--%>
<%--                    </div>--%>
<%--                  </th>--%>
                  <th class="product_price" onclick="orderBy(this)">
                    <div class="asc">
                      <span>제품합가격</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="delivery_address" onclick="orderBy(this)">
                    <div class="asc">
                      <span>배송지</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="order_date" onclick="orderBy(this)">
                    <div class="asc">
                      <span>주문일</span>
                      <img src="${ pageContext.request.contextPath }/view/image/down-arrow.png" />
                    </div>
                  </th>
                  <th class="total_price">
                    <div class="asc">
                      <span>총합계</span>
                    </div>
                  </th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="h" items="${ list }" varStatus="status">
                  <tr>
                    <td>
                      <p>${ h.orderId }</p>
                    </td>
                    <td>
                      <p>${ h.memberId }</p>
                    </td>
                    <td>
                      <p>${ h.productCode }</p>
                    </td>
                    <td>
                      <p>${ h.productName }</p>
                    </td>
                    <td>
                      <p><fmt:formatNumber value="${ h.productPrice }" pattern="#,###" /></p>
                    </td>
                    <td>
                      <p>${ h.quantity }</p>
                    </td>
<%--                    <td>--%>
<%--                      <p>--%>
<%--                        ${ h.productGrinding }--%>
<%--                        <c:if test="${ h.productGrinding == 0 }">(원두)</c:if>--%>
<%--                          <c:if test="${ h.productGrinding == 1 }">(핸드드립)</c:if>--%>
<%--                          <c:if test="${ h.productGrinding == 2 }">(에스프레소)</c:if>--%>
<%--                      </p>--%>
<%--                    </td>--%>
                    <td>
                      <p><fmt:formatNumber value="${ h.productPrice * h.quantity }" pattern="#,###" /></p>
                    </td>
                    <td>
                      <p>${ h.deliveryAddress }</p>
                      <p>${ h.detailDeliveryAddress }</p>
                    </td>
                    <td>
                      <p>${ h.orderDate }</p>
                    </td>
                    <td>
                      <p><fmt:formatNumber value="${ h.totalPrice }" pattern="#,###" /></p>
                    </td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </c:if>
            <c:if test="${ requestScope.historySearchCount == 0 }">
              <p>검색결과를 찾을 수 없습니다.</p>
            </c:if>
          </c:when>
        </c:choose>
      </ul>
    </div>
  </div>
  
  <div class="pagination_wrap center">
    <div class="pagination">
      <c:if test="${ pageNum >= 3}">
        <c:choose>
          <c:when test="${ requestScope.searchText == null }">
            <a
              href="${ pageContext.request.contextPath }/admin/historyList?pageNum=${ pageNum - 3 }"
            >&laquo;</a
            >
          </c:when>
          <c:when test="${requestScope.searchText != null}">
            <a
              href="${ pageContext.request.contextPath }/admin/historyListSearch?pageNum=${ pageNum - 3 }&&searchText=${ requestScope.searchText }"
            >&laquo;</a
            >
          </c:when>
        </c:choose>
      </c:if>
      <c:if test="${ historySearchCount != 0 }">
        <c:forEach var="p" begin="${ start }" end="${ end }">
          <c:choose>
            <c:when test="${requestScope.searchText == null}">
              <a
                href="${ pageContext.request.contextPath }/admin/historyList?pageNum=${ p }"
              >${ p }</a
              >
            </c:when>
            <c:when test="${requestScope.searchText != null}">
              <a
                href="${ pageContext.request.contextPath }/admin/historyListSearch?pageNum=${ p }&&searchText=${ requestScope.searchText }"
              >${ p }</a
              >
            </c:when>
          </c:choose>
        </c:forEach>
      </c:if>
      
      <c:if test="${ pageNum < end - 3 }">
        <c:choose>
          <c:when test="${ requestScope.searchText == null}">
            <a
              href="${ pageContext.request.contextPath }/admin/historyList?pageNum=${ pageNum + 3 }"
            >&raquo;</a
            >
          </c:when>
          <c:when test="${ requestScope.searchText != null}">
            <a
              href="${ pageContext.request.contextPath }/admin/historyListSearch?pageNum=${ pageNum + 3 }&&searchText=${ requestScope.searchText }"
            >&raquo;</a
            >
          </c:when>
        </c:choose>
      </c:if>
    </div>
  </div>
</main>
<script>
  window.onload = function() {
    const columnNameInput = document.getElementById("columnName");
    const orderByInput = document.getElementById("orderBy");

    const columnNameInputValue = columnNameInput.value;
    const orderByInputValue = orderByInput.value;

    const columnNameElement = document.querySelector("." + columnNameInputValue).firstElementChild;
    const imgElement = columnNameElement.lastElementChild;

    if(orderByInputValue === "asc") {
      columnNameElement.className = "asc";
      imgElement.src = "${ pageContext.request.contextPath }/view/image/down-arrow.png";
    } else if(orderByInputValue === "desc") {
      columnNameElement.className = "desc";
      imgElement.src = "${ pageContext.request.contextPath }/view/image/up-arrow.png";
    }
  }

  function orderBy(object) {
    const columnName = object.className;
    const currentOrderBy = object.firstElementChild.className;
    const orderBy = (currentOrderBy === "asc") ? "desc" : "asc";
    const form = document.getElementById("orderByForm");

    form.querySelector('input[name="columnName"]').value = columnName;
    form.querySelector('input[name="orderBy"]').value = orderBy;

    form.submit();
  }
</script>
<script>
  function expand(object) {
    const form = document.querySelector(".search_form");
    const img = object.firstElementChild;
    const state = object.className;

    if(state === "collapse") {
      form.style.maxHeight = "200px";
      object.className = "expand";
      img.src = "${ pageContext.request.contextPath }/view/image/up-arrow.png";
    } else if(state === "expand") {
      form.style.maxHeight = "70px";
      object.className = "collapse";
      img.src = "${ pageContext.request.contextPath }/view/image/down-arrow.png";
    }
  }
  function submit() {
    const form = document.querySelector(".search_form");
    let $datepickerStart = $('#datepickerStart');
    let $datepickerEnd = $('#datepickerEnd');

    let startDate = $datepickerStart.val();
    let endDate = $datepickerEnd.val();

    if((startDate !== "" && endDate === "") || (endDate !== "" && startDate === "")) {
      alert("날짜를 한쪽에만 입력 할 수 없습니다.");
      return false;
    } else if(startDate !== "" && endDate !== "") {
      let startDateArr = startDate.split('-');
      let endDateArr = endDate.split('-');

      let startDateCompare = new Date(startDateArr[0], parseInt(startDateArr[1])-1, startDateArr[2]);
      let endDateCompare = new Date(endDateArr[0], parseInt(endDateArr[1])-1, endDateArr[2]);
      
      if(startDateCompare.getTime() > endDateCompare.getTime()) {
        alert("시작날짜가 종료날짜보다 앞설 수 없습니다.");
        return false;
      } else {
        form.submit();
        return true;
      }
    }
      form.submit();
      return true;
  }
</script>
<script src="${ pageContext.request.contextPath }/view/js/datepicker.js"></script>
</body>
