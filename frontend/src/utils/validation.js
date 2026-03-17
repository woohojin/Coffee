export const idRegex = /[^A-Za-z0-9]/g;
export const koreanRegex = /[^가-힣ㄱ-ㅎㅏ-ㅣ]/g;
export const numberRegex = /[^0-9]/g;

export const passwordRegex = /^(?=(.*[a-zA-Z]))(?=.*\d|.*\W).{8,}$/;
export const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

// 비밀번호 검증 (회원가입용)
export const validatePassword = (password, passwordCheck) => {
  if (password !== passwordCheck) {
    alert("비밀번호와 재입력 비밀번호가 일치하지 않습니다.");
    return false;
  }
  if (!passwordRegex.test(password)) {
    alert("비밀번호 조건이 부합하지 않습니다.");
    return false;
  }
  return true;
};

// 비밀번호 검증 (프로필 수정용 - 비밀번호 미입력 허용)
export const validateProfilePassword = (password, passwordCheck) => {
  if (password !== passwordCheck) {
    alert("비밀번호와 재입력 비밀번호가 일치하지 않습니다.");
    return false;
  }
  if (password && !passwordRegex.test(password)) {
    alert("비밀번호 조건이 부합하지 않습니다.");
    return false;
  }
  return true;
};
