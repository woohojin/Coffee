import { useRef } from "react";

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
      target.getBoundingClientRect().top + window.scrollY - HEADER_OFFSET;
    window.scrollTo({ top: y, behavior: "smooth" });
  };

  return { refs, scrollTo };
}
