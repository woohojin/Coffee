import { useState, useRef } from "react";
import axiosInstance from "../api/axiosInstance";
import { emailRegex } from "../utils/validation";

export function useEmailVerify() {
  const [verifyCode, setVerifyCode] = useState("");
  const [isVerified, setIsVerified] = useState(false);
  const [verifiedEmail, setVerifiedEmail] = useState("");
  const [isEmailSending, setIsEmailSending] = useState(false);
  const [countdown, setCountdown] = useState("");
  const countdownRef = useRef(null);

  // 인증번호 전송
  const sendVerifyEmail = async (memberEmail) => {
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
      const res = await axiosInstance.post("/api/member/verifyEmail", {
        memberEmail,
      });
      if (!res.data.success) {
        alert(res.data.message || "인증번호 전송 실패");
        return;
      }
      alert("인증번호가 전송되었습니다.");

      if (countdownRef.current) clearInterval(countdownRef.current);
      setCountdown("");
      setVerifiedEmail(memberEmail);
      setIsEmailSending(true);

      // 1분 후 재발송 가능
      setTimeout(() => {
        setIsEmailSending(false);
      }, 60000);

      // 3분 카운트다운
      const endTime = Date.now() + 180000;
      countdownRef.current = setInterval(() => {
        const distance = endTime - Date.now();
        if (distance <= 0) {
          clearInterval(countdownRef.current);
          setCountdown("0 : 0");
          return;
        }
        const m = Math.floor(distance / 60000);
        const s = Math.floor((distance % 60000) / 1000);
        setCountdown(`${m} : ${s}`);
      }, 1000);
    } catch (err) {
      alert(err.response?.data?.message || "인증번호 전송 실패");
    }
  };

  // 인증번호 확인
  const checkVerifyCode = async () => {
    if (!verifyCode.trim()) {
      alert("인증번호를 입력해주세요.");
      return;
    }
    try {
      await axiosInstance.post("/api/member/verifyCode", {
        verifyCode,
        memberEmail: verifiedEmail,
      });
      clearInterval(countdownRef.current);
      setCountdown("");
      setIsVerified(true);
      setIsEmailSending(false);
      alert("인증이 완료되었습니다.");
    } catch (err) {
      alert(err.response?.data?.message || "이메일 인증을 완료해주세요.");
    }
  };

  return {
    verifyCode,
    setVerifyCode,
    countdown,
    sendVerifyEmail,
    checkVerifyCode,
    verifiedEmail,
  };
}
