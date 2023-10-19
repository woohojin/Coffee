<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<body>
<main>
  <div class="product_wrap">
    <h1>블렌드 커피</h1>
    <div class="product">
      <ul>
        <c:choose>
          <c:when test="${requestScope.memberTier == '0'}">
            <p>게시물을 열람 할 권한이 부족합니다.</p>
            <p>회원가입을 하시거나 회원가입 이후 문제가 발생했다면</p>
            <p>xxx로 연락주십시오.</p>
          </c:when>
          <c:when test="${requestScope.memberTier != 0}">
            <c:if test="${requestScope.productSearchCount != 0}">
              <c:forEach var="p" items="${ list }">
                <li>
                  <a
                    href="${ pageContext.request.contextPath }/board/productDetail?productCode=${ p.productCode }"
                  >
                    <img
                      src="${ pageContext.request.contextPath }/view/image/1.jpg"
                      alt=""
                    />
                  </a>
                  <div>
                    <a
                      onclick="fileDownload('${p.productFile}', '${p.productName}')"
                    >${p.productName}</a
                    >
                    <p>${ p.productPrice }원</p>
                  </div>
                </li>
              </c:forEach>
            </c:if>

            <c:if test="${ requestScope.productSearchCount == 0 }">
              <p>검색결과를 찾을 수 없습니다.</p>
            </c:if>
          </c:when>
        </c:choose>
      </ul>
    </div>
  </div>

  <div class="">
    <div class="">
      <c:if test="${ pageNum >= 3}">
        <c:choose>
          <c:when test="${requestScope.searchText == null}">
            <a
              href="${ pageContext.request.contextPath }/board/product?pageNum=${pageNum - 3}"
            >&laquo;</a
            >
          </c:when>
          <c:when test="${requestScope.searchText != null}">
            <a
              href="${ pageContext.request.contextPath }/board/productSearch?pageNum=${pageNum - 3}&&searchText=${requestScope.searchText}"
            >&laquo;</a
            >
          </c:when>
        </c:choose>
      </c:if>
      <c:if test="${ productSearchCount != 0 }">
        <c:forEach var="p" begin="${ start }" end="${ end }">
          <c:choose>
            <c:when test="${requestScope.searchText == null}">
              <a
                href="${ pageContext.request.contextPath }/board/product?pageNum=${p}"
              >${p}</a
              >
            </c:when>
            <c:when test="${requestScope.searchText != null}">
              <a
                href="${ pageContext.request.contextPath }/board/productSearch?pageNum=${p}&&searchText=${requestScope.searchText}"
              >${p}</a
              >
            </c:when>
          </c:choose>
        </c:forEach>
      </c:if>

      <c:if test="${ pageNum < end - 3 }">
        <c:choose>
          <c:when test="${requestScope.searchText == null}">
            <a
              href="${ pageContext.request.contextPath }/board/product?pageNum=${pageNum + 3}"
            >&raquo;</a
            >
          </c:when>
          <c:when test="${requestScope.searchText != null}">
            <a
              href="${ pageContext.request.contextPath }/board/productSearch?pageNum=${pageNum + 3}&&searchText=${requestScope.searchText}"
            >&raquo;</a
            >
          </c:when>
        </c:choose>
      </c:if>
    </div>
  </div>
</main>
<script type="text/javascript">
  function fileDownload(productFile, productName) {
    var xhr = new XMLHttpRequest();
    xhr.open(
      "GET",
      "${pageContext.request.contextPath}/board/fileDownload?fileName=" +
      productFile,
      true
    );
    xhr.responseType = "blob";

    xhr.onload = function () {
      if (xhr.status === 200) {
        var blob = new Blob([xhr.response], {
          type: xhr.getResponseHeader("Content-Type"),
        });
        var url = window.URL.createObjectURL(blob);

        var a = document.createElement("a");
        a.href = url;
        a.download = productName + ".jpg"; // 다운로드될 파일명
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
      } else {
        alert("파일을 다운로드하는 중에 오류가 발생했습니다.");
      }
    };

    xhr.send();
  }
</script>
</body>
</html>
