// main
document.addEventListener("DOMContentLoaded", function() {
  const memberId = /*[[${session.memberId}]]*/ '';
  const memberTier = /*[[${session.memberTier}]]*/ null;
  if (memberId.length > 0 && memberTier === 0) {
    alert("현재 가입 승인 대기중입니다.");
  }
});

// header
function checkLogout() {
  if (confirm("로그아웃 하시겠습니까?")) {
    return true;
  } else {
    return false;
  }
}

// productSearch
function searchSubmit() {
  document.getElementById("search_form").submit();
  return false; // 기본 링크 동작 방지
}

// memberTerms
function termsCheck() {
  document.addEventListener('DOMContentLoaded', () => {
    const checkAll = document.querySelector(".check-all");
    const firstCheckbox = document.querySelector(".first_checkbox");
    const secondCheckbox = document.querySelector(".second_checkbox");
    const termsSubmitBtn = document.querySelector("#terms-submit_btn");

    // 요소가 모두 존재할 때만 실행
    if (checkAll && firstCheckbox && secondCheckbox) {
      // 전체 동의 → 개별 체크박스 동기화
      checkAll.addEventListener('change', () => {
        const isChecked = checkAll.checked;
        firstCheckbox.checked = isChecked;
        secondCheckbox.checked = isChecked;
      });

      // 개별 체크박스 변경 → 전체 동의 상태 업데이트
      const updateCheckAll = () => {
        checkAll.checked = firstCheckbox.checked && secondCheckbox.checked;
      };

      firstCheckbox.addEventListener('change', updateCheckAll);
      secondCheckbox.addEventListener('change', updateCheckAll);

      // 페이지 로드 시 초기 상태 강제 동기화 (중요!)
      updateCheckAll();

      termsSubmitBtn.addEventListener('click', (e) => {
        e.preventDefault();
        if (firstCheckbox.checked && secondCheckbox.checked) {
          const signUpUrl = document.querySelector('meta[name="signUpUrl"]').content;
          if (signUpUrl) {
            window.location.href = signUpUrl;
          } else {
            alert("페이지 이동 URL을 찾을 수 없습니다.");
          }
        } else {
          alert("필수사항을 모두 동의해주세요.");
        }
        return false;
      });
    }
  });
}

termsCheck();
