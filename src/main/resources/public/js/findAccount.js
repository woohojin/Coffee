import { apiPost } from "./api-utils.js";

document.addEventListener('DOMContentLoaded', () => {
  // meta 태그에서 값 읽어오기
  const findType = document.querySelector('meta[name="findType"]')?.content || 'id';
  const findAccountUrl = document.querySelector('meta[name="findAccountUrl"]')?.content || '';

  let countdownInterval;
  let isEmailSending = false;
  const emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

  window.toggleFindType = function(object) {
    window.location.href = findAccountUrl + "?findType=" + object.value;
  };

  window.sendVerifyEmail = async function() {
    const memberEmail = document.querySelector('.member_email')?.value || '';

    if (!emailRegex.test(memberEmail)) {
      alert("이메일 형식이 올바르지 않습니다.");
      return;
    }

    if (findType === 'id') {
      const memberName = document.querySelector('.member_name')?.value || '';
      if (memberName.trim() === '' || memberEmail.trim() === '') {
        alert('이름과 이메일을 입력하세요.');
        return;
      }
    }

    if (findType === 'password') {
      const memberId = document.querySelector('.member_id')?.value || '';
      if (memberId.trim() === '' || memberEmail.trim() === '') {
        alert('아이디와 이메일을 입력하세요.');
        return;
      }
    }

    if (isEmailSending) {
      alert("1분 뒤에 인증번호를 재전송 할 수 있습니다.");
      return;
    }

    isEmailSending = true;

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
  };

  // 폼 submit 이벤트
  const form = document.querySelector('.member_signin_form');
  if (form) {
    form.addEventListener('submit', async function(e) {
      e.preventDefault();

      const verifyCode = document.querySelector('.verify_code')?.value || '';

      try {
        const data = await apiPost('/api/member/findAccount', {
          findType,
          memberName: document.querySelector('.member_name')?.value || '',
          memberEmail: document.querySelector('.member_email')?.value || '',
          memberId: document.querySelector('.member_id')?.value || '',
          verifyCode
        });

        if (findType === 'id') {
          const resultDiv = document.getElementById('find-result');
          const idList = document.getElementById('id-list');

          if (resultDiv) resultDiv.style.display = 'block';
          if (idList && data.ids) {
            idList.innerHTML = data.ids.map(id => `<p>회원님의 아이디는 ${id} 입니다.</p>`).join('');
          }
        } else {
          alert("임시 비밀번호가 이메일로 전송되었습니다.");
          location.href = '/member/memberSignIn';
        }

      } catch (err) {
        console.error("계정 찾기 실패:", err);
      }
    });
  }

  // 인증번호 발송 버튼 이벤트
  const sendVerifyBtn = document.getElementById('send-verify-btn');
  if (sendVerifyBtn) {
    sendVerifyBtn.addEventListener('click', () => {
      window.sendVerifyEmail();
    });
  }

  function startCountdown() {
    const countdown = document.querySelector('.countdown');
    const endTime = new Date().getTime() + 180 * 1000;

    countdownInterval = setInterval(() => {
      const distance = endTime - new Date().getTime();
      const minutes = Math.floor((distance / 1000) / 60);
      const seconds = Math.floor((distance / 1000) % 60);

      countdown.textContent = minutes + ' : ' + seconds;

      if (distance <= 0) {
        clearInterval(countdownInterval);
        countdown.textContent = '0 : 0';
      }
    }, 1000);
  }
});