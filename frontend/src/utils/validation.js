export const idRegex = /^[a-z0-9]{4,20}$/;
export const koreanOnlyRegex = /^[가-힣]+$/;
export const numberOnlyRegex = /^[0-9]+$/;
export const passwordRegex = /^(?=(.*[a-zA-Z]))(?=.*\d|.*\W).{8,}$/;
export const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

export const handleValidationFail = (field, message) => {
  alert(message);
  const el = document.getElementById(field);
  if (el) {
    el.focus();
    const y = el.getBoundingClientRect().top + window.scrollY - 160;
    window.scrollTo({ top: y, behavior: "smooth" });
  }
};

export const validateSignUp = (form) => {
  if (!idRegex.test(form.memberId)) {
    handleValidationFail(
      "member_id",
      "아이디는 영문 소문자와 숫자만 가능합니다. (4~20자)",
    );
    return false;
  }

  if (!koreanOnlyRegex.test(form.memberName)) {
    handleValidationFail("member_name", "이름은 한글만 입력 가능합니다.");
    return false;
  }

  if (!passwordRegex.test(form.memberPassword)) {
    handleValidationFail(
      "member_password",
      "비밀번호 조건이 부합하지 않습니다.",
    );
    return false;
  }

  if (form.memberPassword !== form.memberPasswordCheck) {
    handleValidationFail(
      "member_password_check",
      "비밀번호가 일치하지 않습니다.",
    );
    return false;
  }

  if (form.memberTel && !numberOnlyRegex.test(form.memberTel)) {
    handleValidationFail("member_tel", "전화번호는 숫자만 입력 가능합니다.");
    return false;
  }
  return true;
};

export const validateProfile = (form, originalEmail, verifiedEmail) => {
  if (!koreanOnlyRegex.test(form.memberName)) {
    handleValidationFail("member_name", "이름은 한글만 입력 가능합니다.");
    return false;
  }
  if (!form.memberExistingPassword) {
    handleValidationFail(
      "member_existing_password",
      "기존 비밀번호를 입력해주세요.",
    );
    return false;
  }
  if (form.memberPassword && !passwordRegex.test(form.memberPassword)) {
    handleValidationFail(
      "member_password",
      "비밀번호 조건이 부합하지 않습니다.",
    );
    return false;
  }
  if (form.memberPassword !== form.memberPasswordCheck) {
    handleValidationFail(
      "member_password_check",
      "비밀번호가 일치하지 않습니다.",
    );
    return false;
  }
  if (form.memberTel && !numberOnlyRegex.test(form.memberTel)) {
    handleValidationFail("member_tel", "전화번호는 숫자만 입력 가능합니다.");
    return false;
  }
  if (form.memberCompanyTel && !numberOnlyRegex.test(form.memberCompanyTel)) {
    handleValidationFail(
      "member_company_tel",
      "업체 연락처는 숫자만 입력 가능합니다.",
    );
    return false;
  }
  if (form.memberEmail !== originalEmail && !verifiedEmail) {
    handleValidationFail("member_email", "이메일 변경 시 인증이 필요합니다.");
    return false;
  }
  return true;
};
