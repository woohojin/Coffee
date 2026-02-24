document.addEventListener('DOMContentLoaded', () => {
  // meta 태그에서 값 읽어오기
  const findType = document.querySelector('meta[name="findType"]')?.content || 'id';
  const verifyEmailUrl = document.querySelector('meta[name="verifyEmailUrl"]')?.content || '';
  const findAccountUrl = document.querySelector('meta[name="findAccountUrl"]')?.content || '';
  const apiFindAccountUrl = document.querySelector('meta[name="apiFindAccountUrl"]')?.content || '';

  let code;
  let countdownInterval;
  const emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

  window.toggleFindType = function(object) {
    window.location.href = findAccountUrl + "?findType=" + object.value;
  };

  window.verifyEmail = function() {
    let memberEmail = document.querySelector('.member_email')?.value || '';
    if (!emailRegex.test(memberEmail)) {
      alert("이메일 형식이 올바르지 않습니다.");
      return;
    }

    if (findType === 'id') {
      let memberName = document.querySelector('.member_name')?.value || '';
      if (memberName.trim() === '' || memberEmail.trim() === '') {
        alert('이름과 이메일을 입력하세요.');
        return;
      }
    }

    if (findType === 'password') {
      let memberId = document.querySelector('.member_id')?.value || '';
      if (memberId.trim() === '' || memberEmail.trim() === '') {
        alert('아이디와 이메일을 입력하세요.');
        return;
      }
    }

    let xhr = new XMLHttpRequest();
    xhr.open('POST', verifyEmailUrl, true);
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
  };

  window.checkVerify = function() {
    let verifyCode = document.querySelector(".verify_code")?.value || '';
    if (code === "timeout") {
      alert("인증시간이 초과되었습니다.");
      return false;
    }
    if (code === verifyCode && code !== "timeout") {
      return true;
    } else {
      alert("인증번호가 일치하지 않습니다.");
      return false;
    }
  };

  window.stopCountdown = function() {
    let verifyCode = document.querySelector(".verify_code")?.value || '';
    let countdown = document.querySelector('.countdown');
    if (code != null && code !== verifyCode) {
      alert("인증에 실패했습니다.");
      return;
    }
    clearInterval(countdownInterval);
    countdown.textContent = "";
    alert("인증이 완료되었습니다.");
  };

  // 폼 submit 이벤트
  const form = document.querySelector('.member_signin_form');
  if (form) {
    form.addEventListener('submit', async function(e) {
      e.preventDefault();

      if (!window.checkVerify()) return;

      const params = new URLSearchParams();
      params.append('findType', document.querySelector('input[name="findType"]:checked')?.value || '');
      params.append('memberName', document.querySelector('.member_name')?.value || '');
      params.append('memberEmail', document.querySelector('.member_email')?.value || '');
      params.append('memberId', document.querySelector('.member_id')?.value || '');
      params.append('verifyCode', document.querySelector('.verify_code')?.value || '');

      const csrfToken = document.getElementById('csrf-token');
      if (csrfToken) {
        params.append(csrfToken.name, csrfToken.value);
      }

      try {
        const response = await fetch(apiFindAccountUrl, {
          method: 'POST',
          headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
          body: params,
          credentials: 'include'
        });

        const data = await response.json();

        const resultDiv = document.getElementById('find-result');
        const messageP = document.getElementById('result-message');
        const idList = document.getElementById('id-list');

        if (messageP) messageP.textContent = data.message;
        if (resultDiv) resultDiv.style.display = 'block';

        if (data.success && data.ids && idList) {
          idList.innerHTML = data.ids.map(id => `<p>회원님의 아이디는 ${id} 입니다.</p>`).join('');
        } else if (data.success) {
          alert(data.message);
          location.href = '/member/memberSignIn';
        } else if (idList) {
          idList.innerHTML = '';
        }
      } catch (err) {
        alert('처리 중 오류 발생');
      }
    });
  }

  // 인증번호 발송 버튼 이벤트
  const sendVerifyBtn = document.getElementById('send-verify-btn');
  if (sendVerifyBtn) {
    sendVerifyBtn.addEventListener('click', () => {
      verifyEmail();
    });
  }
});