document.addEventListener('DOMContentLoaded', () => {
  // 프로필 폼 제출 시 비밀번호 체크
  const profileForm = document.getElementById('profileForm');
  if (profileForm) {
    profileForm.addEventListener('submit', function(event) {
      if (!profilePasswordInputCheck(this)) {
        event.preventDefault();
      }
    });
  }

  // 파일 선택 시 파일명 표시
  const fileInput = document.getElementById("file");
  if (fileInput) {
    fileInput.addEventListener('change', () => {
      const file = fileInput.files[0];
      if (file) {
        const input = document.getElementById("memberFile");
        if (input) input.value = file.name;
      }
    });
  }

  // 이메일 변경 시 인증 체크
  const originalEmailMeta = document.querySelector('meta[name="originalEmail"]');
  const originalEmail = originalEmailMeta ? originalEmailMeta.content : '';

  window.checkVerifyInProfile = function() {
    const verifyCodeInput = document.querySelector(".verify_code");
    const memberEmailInput = document.querySelector(".member_email");

    if (!verifyCodeInput || !memberEmailInput) return true;

    const verifyCode = verifyCodeInput.value;
    const memberEmailValue = memberEmailInput.value;

    if (memberEmailValue !== originalEmail) {
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
    }
    return true;
  };

  // 프로필 수정 버튼 클릭 시 인증 체크
  const profileBtn = document.getElementById('profile-btn');
  if (profileBtn) {
    profileBtn.addEventListener('click', (e) => {
      // form submit 허용하면서도 인증 체크
      if (!checkVerifyInProfile()) {
        e.preventDefault();  // 인증 실패 시 제출 막기
      }
    });
  }
});