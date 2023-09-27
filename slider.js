window.onload = function () {
  const sliderWrap = document.querySelector(".main_slider_wrap");
  const slider = sliderWrap.querySelector(".slider");
  const sliderList = slider.querySelectorAll("li");
  const moveButton = sliderWrap.querySelector(".arrow");

  /*리스너 설치 */
  let currentIdx = sliderList.length - 1;
  moveButton.addEventListener("click", moveSlide);
  function moveSlide(event) {
    event.preventDefault();
    if (event.target.className === "next") {
      if (currentIdx <= 0) {
        sliderList[0].classList.add("fade_out");
        sliderList[0].classList.remove("fade_in");
        sliderList[1].classList.add("fade_out");
        sliderList[2].classList.add("fade_out");
        sliderList[3].classList.add("fade_in");
        sliderList[3].classList.remove("fade_out");
        currentIdx = sliderList.length - 1;
      } else if (currentIdx !== currentIdx - 1) {
        sliderList[currentIdx - 1].classList.add("fade_in");
        sliderList[currentIdx - 1].classList.remove("fade_out");
        sliderList[currentIdx].classList.add("fade_out");
        sliderList[currentIdx].classList.remove("fade_in");
        currentIdx -= 1;
      }
    } else if (event.target.className === "prev") {
      if (currentIdx + 2 > sliderList.length) {
        sliderList[0].classList.add("fade_in");
        sliderList[0].classList.remove("fade_out");
        sliderList[1].classList.add("fade_out");
        sliderList[2].classList.add("fade_out");
        sliderList[3].classList.add("fade_out");
        sliderList[3].classList.remove("fade_in");
        currentIdx = 0;
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
