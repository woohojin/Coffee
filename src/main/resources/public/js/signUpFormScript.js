const idRegex = /[^A-Za-z0-9]/g;
const koreanRegex = /[^가-힣ㄱ-ㅎㅏ-ㅣ]/g;
const numberRegex = /[^0-9]/g;
const passwordRegex = /^(?=(.*[a-zA-Z]))(?=.*\d|.*\W).{8,}$/;

function passwordInputCheck(object) {
  const memberPassword = object.memberPassword.value;
  const memberPasswordCheck = object.memberPasswordCheck.value;
  if (memberPassword !== memberPasswordCheck) {
    alert("비밀번호와 재입력 비밀번호가 일치하지 않습니다.");
    object.memberPasswordCheck.value = "";
    object.memberPasswordCheck.focus();
    return false;
  } else {
    if(passwordRegex.test(memberPassword) === true) {
      return true;
    } else {
      alert("비밀번호 조건이 부합하지 않습니다.");
      return false;
    }
  }
}

function profilePasswordInputCheck(object) {
  const memberPassword = object.memberPassword.value;
  const memberPasswordCheck = object.memberPasswordCheck.value;
  if (memberPassword !== memberPasswordCheck) {
    alert("비밀번호와 재입력 비밀번호가 일치하지 않습니다.");
    object.memberPasswordCheck.value = "";
    object.memberPasswordCheck.focus();
    return false;
  } else {
    if(memberPassword == null || memberPassword === "") {
      return true;
    } else {
      if(passwordRegex.test(memberPassword) === true) {
        return true;
      } else {
        alert("비밀번호 조건이 부합하지 않습니다.");
        return false;
      }
    }
  }
}

// 전화번호 숫자 및 길이 필터링
function numberInputCheck() {
  document.addEventListener('DOMContentLoaded', () => {
    const telInputs = [
      document.getElementById('member_tel'),
      document.getElementById('member_company_tel')
    ].filter(input => input !== null);

    telInputs.forEach(input => {
      const maxLen = parseInt(input.getAttribute('maxlength') || '11', 10);

      input.addEventListener('input', () => {
        let value = input.value;
        // 숫자 정규화
        value = value.replace(numberRegex, '');

        // 길이 필터링
        if (value.length > maxLen) {
          value = value.slice(0, maxLen);
        }

        input.value = value;
      });
    });
  });
}

function nameInputCheck() {
  document.addEventListener('DOMContentLoaded', () => {
    const nameInput = document.getElementById('member_name');
    if (!nameInput) return;

    nameInput.addEventListener('input', () => {
      const original = nameInput.value;
      // 이름 정규화
      let cleaned = original.replace(koreanRegex, '');

      if (original !== cleaned) {
        alert("한국어로 입력해주세요.");
      }

      nameInput.value = cleaned;
    });
  });
}

function idInputCheck() {
  document.addEventListener('DOMContentLoaded', () => {
    const idInput = document.getElementById('member_id');
    if (!idInput) return;

    idInput.addEventListener('input', () => {
      const original = idInput.value;
      // id 정규화
      let cleaned = original.replace(idRegex, '');

      // id 전체 소문자로 변경
      cleaned = cleaned.toLowerCase();

      if (original !== cleaned) {
        alert("영어 및 숫자로 입력해주세요.");
      }

      idInput.value = cleaned;
    });
  });
}

const signUpForm = document.getElementById('signUpForm');
signUpForm.addEventListener('submit', function(event) {
  if (!passwordInputCheck(this)) {
    event.preventDefault();
  }
});

// 주소와 동일하게 버튼 클릭 이벤트
document.addEventListener('DOMContentLoaded', () => {
  const sameAddressBtn = document.getElementById('same-address-btn');
  if (sameAddressBtn) {
    sameAddressBtn.addEventListener('click', () => {
      sameAddress();
    });
  }
});

function sameAddress() {
  const memberAddress = document.getElementById("member_address");
  const memberDetailAddress = document.getElementById("member_detail_address");
  const memberDeliveryAddress = document.getElementById("member_delivery_address");
  const memberDetailDeliveryAddress = document.getElementById("member_detail_delivery_address");

  memberDeliveryAddress.value = memberAddress.value;
  memberDetailDeliveryAddress.value = memberDetailAddress.value;
}

// 파일 삽입 시 파일 이름 저장
function inputFileName() {
  const fileInput = document.getElementById("signUpFile");
  const file = fileInput.files[0];

  fileInput.addEventListener("change", (e) => {
    if (file) {
      const fileName = file.name;
      const input = document.getElementById("memberFile");
      input.value = fileName;
    }
  });
}

document.getElementById("member_email").addEventListener("keypress", function(event) {
  if (event.key === "Enter") {
    event.preventDefault();
    verifyEmail();
    document.querySelector(".verify_code").focus();
  }
});

document.querySelector(".verify_code").addEventListener("keypress", function(event) {
  if (event.key === "Enter") {
    event.preventDefault();
    stopCountdown();
  }
});

inputFileName();
numberInputCheck();
nameInputCheck();
idInputCheck();
