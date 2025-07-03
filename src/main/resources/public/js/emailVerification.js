let code;
let countdownInterval;
let isVerified = false;
let isEmailSending = false;
const emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

async function verifyEmail() {
  let memberEmail = document.querySelector('.member_email').value;

  if (emailRegex.test(memberEmail) === false) {
    alert("이메일 형식이 올바르지 않습니다.");
    return;
  }

  if (!isEmailSending && !isVerified) {
    isEmailSending = true;

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    const params = new URLSearchParams();
    params.append("memberEmail", memberEmail);

    try {
      const response = await fetch("../member/verifyEmail", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
          [csrfHeader]: csrfToken
        },
        body: params
      });

      if (response.ok) {
        const responseData = await response.json();
        code = responseData.code;

        if (countdownInterval) {
          clearInterval(countdownInterval);
        }

        alert("인증번호가 전송되었습니다.");
        startCountdown();
        document.querySelector(".verify_code").focus();

        setTimeout(() => {
          isEmailSending = false;
        }, 60000);
      } else {
        console.error("요청 실패 : ", response.status);
        alert("인증번호 발송에 실패했습니다.");
      }
    } catch (err) {
      console.error("에러 : ", err);
      alert("인증번호 발송에 실패했습니다.");
    }
  } else if (isVerified) {
    alert("이미 인증하셨습니다.");
  } else {
    alert("1분 뒤에 인증번호를 재전송 할 수 있습니다.");
  }
}

  //   let xhr = new XMLHttpRequest();
  //   xhr.open('POST', '../member/verifyEmail', true);
  //   xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
  //
  //
  //   xhr.setRequestHeader(csrfHeader, csrfToken);
  //
  //   xhr.onreadystatechange = function() {
  //     if (xhr.readyState === XMLHttpRequest.DONE) {
  //       if (xhr.status === 200) {
  //         let response = JSON.parse(xhr.responseText);
  //         code = response.code;
  //
  //         if (countdownInterval) {
  //           clearInterval(countdownInterval);
  //         }
  //
  //         alert("인증번호가 전송되었습니다.");
  //         startCountdown();
  //
  //         setTimeout(() => {
  //           isEmailSending = false;
  //         }, 60000);
  //
  //       } else {
  //         console.error('요청 실패 :', xhr.status);
  //       }
  //     }
  //   };
  //   let params = 'memberEmail=' + encodeURIComponent(memberEmail);
  //   xhr.send(params);
  // } else if(isVerified){
  //   alert("이미 인증하셨습니다.");
  // } else {
  //   alert("1분뒤에 인증번호를 재전송 할 수 있습니다.");
  // }

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

  if(code === null || code !== verifyCode || code === "") {
    alert("인증에 실패했습니다.");
    return;
  }

  clearInterval(countdownInterval);
  countdown.textContent = "";
  isVerified = true;
  alert("인증이 완료되었습니다.");
}