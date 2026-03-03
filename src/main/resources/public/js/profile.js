import { apiPost } from "./api-utils.js";

document.addEventListener('DOMContentLoaded', () => {
  const originalEmail = document.querySelector('meta[name="originalEmail"]')?.content || '';

  // 프로필 폼 제출
  const profileForm = document.getElementById('profileForm');
  if (profileForm) {
    profileForm.addEventListener('submit', async function(event) {
      event.preventDefault();

      if (!profilePasswordInputCheck(this)) return;

      const memberEmail = document.querySelector('.member_email')?.value || '';

      // 이메일이 변경된 경우에만 인증 체크
      if (memberEmail !== originalEmail) {
        const verifyCode = document.querySelector('.verify_code')?.value || '';
        try {
          await apiPost('/api/member/verifyCode', { verifyCode });
        } catch (err) {
          console.error("인증 실패:", err);
          return;
        }
      }

      this.submit();
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
});