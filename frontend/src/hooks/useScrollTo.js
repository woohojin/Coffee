import { useRef } from "react";

// 헤더 높이만큼 스크롤 위치 보정
const HEADER_OFFSET = 160;

export function useScrollTo() {
  const refs = {
    scroll1: useRef(null),
    scroll2: useRef(null),
    scroll3: useRef(null),
  };

  const scrollTo = (key) => {
    const target = refs[key]?.current;
    if (!target) return;
    const y =
      target.getBoundingClientRect().top + window.scrollY - HEADER_OFFSET; // 요소 위치 + 현재 스크롤 - 헤더 높이
    window.scrollTo({ top: y, behavior: "smooth" });
  };

  return { refs, scrollTo };
}
