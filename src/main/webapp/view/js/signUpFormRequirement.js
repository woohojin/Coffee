const englishRegex = /[^A-Za-z]/g;
const koreanRegex = /[^가-힣ㄱ-ㅎㅏ-ㅣ]/g;
const numberRegex = /[^0-9]/g;
const passwordRegex = /^(?=(.*[A-Za-z]){0,}(.*\d){0,}(.*[!@#$%^&*()_+{}\[\]:;<>,.?~\\/-]){0,}).{8,}$/;

function passwordInputCheck(object) {
  const memberPassword = object.memberPassword.value;
  const memberPasswordCheck = object.memberPasswordCheck.value;
  if (memberPassword != memberPasswordCheck) {
    alert("비밀번호와 재입력 비밀번호가 일치하지 않습니다.");
    object.memberPasswordCheck.value = "";
    object.memberPasswordCheck.focus();
    return false;
  }
  if(passwordRegex.test(memberPassword) == true) {
    return true;
  } else {
    alert("비밀번호 조건이 부합하지 않습니다.");
    return false;
  }

}

function maxLengthCheck(object) {
  if (object.value.length > object.maxLength) {
    object.value = object.value.slice(0, object.maxLength);
  }
}

function numberInputCheck(object) {
  const inputText = object.value;
  const numberInputCheck = inputText.replace(numberRegex, "");
  object.value = numberInputCheck;
}

function nameInputCheck(object) {
  const inputText = object.value;
  const nameInputCheck = inputText.replace(koreanRegex, "");
  object.value = nameInputCheck;
  if (inputText !== nameInputCheck) {
    alert("한국어로 입력해주세요.");
  }
}

function idInputCheck(object) {
  const inputText = object.value;
  const idInputCheck = inputText.replace(englishRegex, "");
  object.value = idInputCheck.toLowerCase();
  if (inputText !== idInputCheck) {
    alert("영어로 입력해주세요.");
  }
}

