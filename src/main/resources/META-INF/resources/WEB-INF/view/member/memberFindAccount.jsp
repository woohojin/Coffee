<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri =
  "http://java.sun.com/jsp/jstl/core" %>

<body>
<main>
  <div class="member_signin_form_wrap">
    <c:if test="${ isFind == 0 }">
      <form
        action="${ pageContext.request.contextPath }/member/memberFindAccountPro"
        method="post"
        class="member_signin_form"
      >
        <div>
          <input id="find_id" name="findType" onclick="toggleFindType(this)" type="radio" value="id" ${ findType == "id" ? "checked" : "" }>
          <label for="find_id">아이디 찾기</label>
          <input id="find_password" name="findType" onclick="toggleFindType(this)" type="radio" value="password" ${ findType == "password" ? "checked" : "" }>
          <label for="find_password">비밀번호 찾기</label>
        </div>
        <table>
          <tbody>
          <c:if test="${ findType == 'id'}">
            <tr>
              <th>
                <label>이름</label>
              </th>
              <td>
                <input
                  name="memberName"
                  class="member_name"
                  type="text"
                  spellcheck="false"
                  required
                />
              </td>
            </tr>
            <tr style="position: relative;">
              <th>
                <label>이메일</label>
              </th>
              <td>
                <input
                  name="memberEmail"
                  class="member_email"
                  type="text"
                  spellcheck="false"
                  required
                />
                <div class="member_email_verify_button">
                  <button class="input_btn" type="button" onclick="verifyEmail()">
                    인증번호 발송
                  </button>
                </div>
              </td>
            </tr>
            <tr style="position: relative;">
              <th>
                <label>인증번호</label>
              </th>
              <td>
                <input
                  name="verifyCode"
                  class="verify_code"
                  type="text"
                  spellcheck="false"
                  required
                />
                <div class="member_email_verify_button">
                  <button class="input_btn" type="button" onclick="stopCountdown()">
                    인증하기
                  </button>
                </div>
                <div class="remain_time">
                  <span class="countdown"></span>
                </div>
              </td>
            </tr>
          </c:if>
          <c:if test="${ findType == 'password'}">
            <tr>
              <th>
                <label>아이디</label>
              </th>
              <td>
                <input
                  name="memberId"
                  class="member_id"
                  type="text"
                  spellcheck="false"
                  required
                />
              </td>
            </tr>
            <tr style="position: relative;">
              <th>
                <label>이메일</label>
              </th>
              <td>
                <input
                  name="memberEmail"
                  class="member_email"
                  type="text"
                  spellcheck="false"
                  required
                />
                <div class="member_email_verify_button">
                  <button class="input_btn" type="button" onclick="verifyEmail()">
                    인증번호 발송
                  </button>
                </div>
              </td>
            </tr>
            <tr style="position: relative;">
              <th>
                <label>인증번호</label>
              </th>
              <td>
                <input
                  name="verifyCode"
                  class="verify_code"
                  type="text"
                  spellcheck="false"
                  required
                />
                <div class="member_email_verify_button">
                  <button class="input_btn" type="button" onclick="stopCountdown()">
                    인증하기
                  </button>
                </div>
                <div class="remain_time">
                  <span class="countdown"></span>
                </div>
              </td>
            </tr>
          </c:if>
          </tbody>
        </table>
        <div class="signin" style="justify-content: flex-end; margin-top: 15px;">
          <input type="submit" value="찾기" class="submit_btn" onclick="return checkVerify()"/>
        </div>
      </form>
    </c:if>
  </div>
  <c:if test="${ isFind == 1 }">
    <div style="display: flex; flex-direction: column; align-items: center">
      <c:forEach var="m" items="${ list }">
        <p>회원님의 아이디는 ${ m.memberId } 입니다.</p>
      </c:forEach>
      <div class="btn" style="width: 100px; height: 45px; margin-top: 25px;">
        <a href="${ pageContext.request.contextPath }/member/memberSignIn">로그인창으로</a>
      </div>
    </div>
  </c:if>
</main>
<script>
  function toggleFindType(object) {
    window.location.href = "./memberFindAccount?findType=" + object.value;
  }
</script>
<script>
  let code;
  let countdownInterval;
  const emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  
  function verifyEmail() {
    let memberEmail = document.querySelector('.member_email').value;
    

    if(emailRegex.test(memberEmail) === false) {
      alert("이메일 형식이 올바르지 않습니다.");
      return;
    }

    if(${ findType == 'id'}) {
      let memberName = document.querySelector('.member_name').value;

      if (memberName.trim() === '' || memberEmail.trim() === '') {
        alert('이름과 이메일을 입력하세요.');
        return;
      }
    }
    
    if(${ findType == 'password'}) {
      let memberId = document.querySelector('.member_id').value;
      
      if(memberId.trim() === '' || memberEmail.trim() === '') {
        alert('아이디와 이메일을 입력하세요.');
        return;
      }
    }

    let xhr = new XMLHttpRequest();
    xhr.open('POST', '${ pageContext.request.contextPath }/member/verifyEmail', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onreadystatechange = function() {
      if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status === 200) {
          let response = JSON.parse(xhr.responseText);
          code = response.code;
          startCountdown();
          alert("인증번호가 전송되었습니다.");
        } else {
          console.error('요청 실패');
        }
      }
    };

    let params = 'memberEmail=' + encodeURIComponent(memberEmail);
    xhr.send(params);
  }
  
  function checkVerify() {
    let verifyCode = document.querySelector(".verify_code").value;

    if(code === "timeout") {
      alert("인증시간이 초과되었습니다.");
      return false;
    }
    
    if(code === verifyCode && !(code === "timeout")) {
      return true;
    } else {
      alert("인증번호가 일치하지 않습니다.");
      return false;
    }
  }

  function startCountdown() {
    let timeLimit = 180; // 3분 (초 단위)

    let countdown = document.querySelector('.countdown');
    let endTime = new Date().getTime() + timeLimit * 1000; // 현재 시간 + 3분

    countdownInterval = setInterval(function () {
      let now = new Date().getTime(); // 현재시간
      let distance = endTime - now; // 현재와 종료시간의 차이 (남은시간을 분과 초로 바꾸기 위해)

      let minutes = Math.floor((distance / 1000) / 60);
      let seconds = Math.floor((distance / 1000) % 60);

      countdown.textContent = minutes + ' : ' + seconds;

      if (distance <= 0) {
        clearInterval(countdownInterval);
        countdown.textContent = '0 : 0';
        code = "timeout";
      }
    }, 1000);
  }

  function stopCountdown() {
    let verifyCode = document.querySelector(".verify_code").value;
    let countdown = document.querySelector('.countdown');
    
    if(code != null && code !== verifyCode) {
      alert("인증에 실패했습니다.");
      return;
    }
    
    clearInterval(countdownInterval);
    countdown.textContent = "";
    alert("인증이 완료되었습니다.");
  }
</script>
</body>
