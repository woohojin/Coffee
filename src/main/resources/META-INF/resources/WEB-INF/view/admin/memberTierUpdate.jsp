<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<body>
<main id="admin_page">
  <div class="admin_page_wrap">
    <div class="page_head">
      <a href="${ pageContext.request.contextPath }/admin/memberTierUpdate">
        <h1>멤버 등급 수정</h1>
      </a>
    </div>
    <div class="search_form_wrap center">
      <div class="inline_wrap">
        <form action="${ pageContext.request.contextPath }/admin/memberTierUpdatePro"
              method="post"
        >
          <div class="list">
            <ul class="center">
              <c:choose>
                <c:when test="${ requestScope.memberTier != '9' }">
                  <p>권한이 부족합니다.</p>
                </c:when>
                <c:when test="${ requestScope.memberTier == '9' && requestScope.memberCount == 0 }">
                  <p>회원을 찾을 수 없습니다.</p>
                </c:when>
                <c:when test="${ requestScope.memberTier == '9' }">
                  <c:if test="${ requestScope.memberSearchCount != 0 || requestScope.memberCount != 0 }">
                    <table class="list">
                      <thead>
                      <tr>
                        <th class="member_tier" onclick="orderBy(this)">
                          <div class="asc">
                            <span>등급</span>
                            <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                          </div>
                        </th>
                        <th class="member_id" onclick="orderBy(this)">
                          <div class="asc">
                            <span>아이디</span>
                            <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                          </div>
                        </th>
                        <th class="member_name" onclick="orderBy(this)">
                          <div class="asc">
                            <span>이름</span>
                            <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                          </div>
                        </th>
                        <th class="member_company_name" onclick="orderBy(this)">
                          <div class="asc">
                            <span>업체명</span>
                            <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                          </div>
                        </th>
                        <th class="member_tel" onclick="orderBy(this)">
                          <div class="asc">
                            <span>전화번호</span>
                            <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                          </div>
                        </th>
                        <th class="member_company_tel" onclick="orderBy(this)">
                          <div class="asc">
                            <span>회사번호</span>
                            <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                          </div>
                        </th>
                        <th class="member_address" onclick="orderBy(this)">
                          <div class="asc">
                            <span>주소</span>
                            <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                          </div>
                        </th>
                        <th class="member_file" onclick="orderBy(this)">
                          <div class="asc">
                            <span>파일</span>
                            <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                          </div>
                        </th>
                        <th class="member_date" onclick="orderBy(this)">
                          <div class="asc">
                            <span>가입일</span>
                            <img src="${ pageContext.request.contextPath }/image/down-arrow.png" />
                          </div>
                        </th>
                        <th>
                          <div class="asc">
                            <span>등급 설정</span>
                          </div>
                        </th>
                        <th>
                          <div class="asc">
                            <span>업데이트</span>
                          </div>
                        </th>
                      </tr>
                      </thead>
                      <tbody>
                      <c:forEach var="m" items="${ list }" varStatus="status">
                        <tr>
                          <td>
                            <p>${ m.memberTier }</p>
                          </td>
                          <td>
                            <p>${ m.memberId }</p>
                          </td>
                          <td>
                            <p>${ m.memberName }</p>
                          </td>
                          <td>
                            <p>${ m.memberCompanyName }</p>
                          </td>
                          <td>
                            <p>${ m.memberTel }</p>
                          </td>
                          <td>
                            <p>${ m.memberCompanyTel }</p>
                          </td>
                          <td>
                            <p>${ m.memberAddress }</p>
                            <p>${ m.memberDetailAddress }</p>
                          </td>
                          <td>
                            <a style="color: var(--mainColor);" th:data-file="${m.memberFile}" th:data-filename="${m.memberId}"
                               onclick="fileDownload(this.getAttribute('data-file'), this.getAttribute('data-filename'))">
                                ${ m.memberFile }
                            </a>
                          </td>
                          <td>
                            <p>${ m.memberDate }</p>
                          </td>
                          <form action="${ pageContext.request.contextPath }/admin/memberTierUpdatePro"
                                method="post">
                            <td>
                                <select id="memberTier" name="memberTier">
                                  <option value="0">0 (권한없음)</option>
                                  <option value="1">1 (머신임대고객)</option>
                                  <option value="2">2 (머신비임대고객)</option>
                                </select>
                                <input
                                  name="memberId"
                                  type="hidden"
                                  value="${ m.memberId }"
                                />
                            </td>
                            <td>
                              <div class="signup">
                                <input
                                  type="submit"
                                  value="업데이트"
                                  class="btn"
                                />
                              </div>
                            </td>
                          </form>
                        </tr>
                      </c:forEach>
                      </tbody>
                    </table>
                  </c:if>
                </c:when>
              </c:choose>
            </ul>
          </div>
        </form>
      </div>
    </div>
  </div>
</main>
</body>
