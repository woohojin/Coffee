window.onload = function () {
  const sliderWrap = document.querySelector(".main_slider_wrap");
  const slider = sliderWrap.querySelector(".slider");
  const sliderList = slider.querySelectorAll("li");
  const moveButton = sliderWrap.querySelector(".arrow");

  // fade_in = 이미지 나타남
  // fade_out = 이미지 사라짐
  //맨 마지막 이미지부터 보이기 때문에 currentIdx가 next는 - prev는 +로 진행됨

  let currentIdx = sliderList.length - 1;
  moveButton.addEventListener("click", moveSlide);
  function moveSlide(event) {
    event.preventDefault();
    if (event.target.className === "next") {
      if (currentIdx <= 0) {
        for (var i = currentIdx; i <= sliderList.length - 1; i++) {
          sliderList[i].classList.add("fade_out");
          if (i === 0) {
            sliderList[i].classList.remove("fade_in");
          } else if (i === sliderList.length - 1) {
            sliderList[i].classList.remove("fade_out");
            sliderList[i].classList.add("fade_in");
            currentIdx = sliderList.length - 1;
          }
        }
      } else if (currentIdx !== currentIdx - 1) {
        sliderList[currentIdx - 1].classList.add("fade_in");
        sliderList[currentIdx - 1].classList.remove("fade_out");
        sliderList[currentIdx].classList.add("fade_out");
        sliderList[currentIdx].classList.remove("fade_in");
        currentIdx -= 1;
      }
      console.log(currentIdx);
    } else if (event.target.className === "prev") {
      if (currentIdx >= sliderList.length - 1) {
        for (var i = currentIdx; i >= 0; i--) {
          sliderList[i].classList.add("fade_out");
          if (i === 0) {
            sliderList[i].classList.add("fade_in");
            sliderList[i].classList.remove("fade_out");
          } else if (i === sliderList.length - 1) {
            sliderList[i].classList.remove("fade_in");
            currentIdx = 0;
          }
        }
      } else if (currentIdx !== currentIdx + 1) {
        sliderList[currentIdx].classList.add("fade_out");
        sliderList[currentIdx].classList.remove("fade_in");
        sliderList[currentIdx + 1].classList.add("fade_in");
        sliderList[currentIdx + 1].classList.remove("fade_out");
        currentIdx += 1;
      }
    }
  }
};
