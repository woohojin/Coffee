<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
"http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
  <body>
    <main>
      <div class="product_detail_wrap">
        <div class="product_detail">
          <c:choose>
            <c:when test="${requestScope.memberTier == '0'}">
              <p>��ǰ�� ���� �� ������ �����մϴ�.</p>
              <p>ȸ�������� �Ͻðų� ȸ������ ���� ������ �߻��ߴٸ�</p>
              <p>xxx�� �����ֽʽÿ�.</p>
            </c:when>
            <c:when test="${requestScope.memberTier != 0}">
              <div>
                <img
                  src="${ pageContext.request.contextPath }/view/image/1.jpg"
                  alt=""
                />
              </div>
              <div class="product_info_wrap">
                <h1>${ product.productName }</h1>
                <table>
                  <tbody>
                    <tr>
                      <th>
                        <span>������ </span>
                      </th>
                      <td>${ product.productCountry }</td>
                    </tr>
                    <tr>
                      <th>ǰ��</th>
                      <td>${ product.productSpecies }</td>
                    </tr>
                    <tr>
                      <th>������</th>
                      <td>${ product.productCompany }</td>
                    </tr>
                    <tr>
                      <th>�뷮</th>
                      <td>${ product.productUnit }</td>
                    </tr>
                    <tr>
                      <th>����</th>
                      <td>${ product.productPrice }��</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </c:when>
          </c:choose>
        </div>
      </div>
    </main>
  </body>
</html>
