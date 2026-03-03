import { apiPost } from "./api-utils.js";

let countdownInterval;
let isVerified = false;
let isEmailSending = false;
const emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

// 이메일 인증 버튼 이벤트
document.addEventListener('DOMContentLoaded', () => {
  const verifyBtn = document.getElementById('send-verify-btn');
  if (verifyBtn) {
    verifyBtn.addEventListener('click', async () => {
      await sendVerifyEmail();
    });
  }

  const signUpForm = document.getElementById('signUpForm');
  if (signUpForm) {
    signUpForm.addEventListener('submit', async (event) => {
      event.preventDefault();
      const verified = await checkVerify();
      if (verified) {
        signUpForm.submit();
      }
    });
  }
});

async function sendVerifyEmail() {
  const memberEmail = document.querySelector('.member_email').value;

  if (!emailRegex.test(memberEmail)) {
    alert("이메일 형식이 올바르지 않습니다.");
    return;
  }

  if (isVerified) {
    alert("이미 인증하셨습니다.");
    return;
  }

  if (isEmailSending) {
    alert("1분 뒤에 인증번호를 재전송 할 수 있습니다.");
    return;
  }

  try {
    await apiPost('/api/member/verifyEmail', { memberEmail });

    if (countdownInterval) clearInterval(countdownInterval);

    alert("인증번호가 전송되었습니다.");
    startCountdown();
    document.querySelector(".verify_code").focus();

    setTimeout(() => {
      isEmailSending = false;
    }, 60000);

  } catch (err) {
    console.error("이메일 전송 실패:", err);
    isEmailSending = false;
  }
}

async function checkVerify() {
  const verifyCode = document.querySelector(".verify_code").value;

  try {
    await apiPost('/api/member/verifyCode', { verifyCode });
    isVerified = true;
    return true;
  } catch (err) {
    console.error("인증 실패:", err);
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
    }
  }, 1000);
}

document.addEventListener('DOMContentLoaded', () => {
  const verifyCodeBtn = document.querySelector('#verify-btn');
  if (verifyCodeBtn) {
    verifyCodeBtn.addEventListener('click', async () => {
      const verified = await checkVerify();
      if (verified) {
        clearInterval(countdownInterval);
        document.querySelector('.countdown').textContent = "";
        alert("인증이 완료되었습니다.");
      }
    });
  }
});