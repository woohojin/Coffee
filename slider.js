window.onload = function () {
  const sliderWrap = document.querySelector(".main_slider_wrap");
  const slider = sliderWrap.querySelector(".slider");
  const sliderList = slider.querySelectorAll("li");
  const moveButton = sliderWrap.querySelector(".arrow");

  /* ul 넓이 계산 */
  const liWidth = sliderList[0].clientWidth;
  const sliderWidth = liWidth * sliderList.length;
  slider.style.width = `${sliderWidth}px`;

  /*리스너 설치 */
  let currentIdx = 0;
  let translate = 0;
  moveButton.addEventListener("click", moveSlide);

  function moveSlide(event) {
    event.preventDefault();
    if (event.target.className === "next") {
      if (currentIdx !== sliderList.length - 1) {
        translate -= liWidth;
        slider.style.transform = `translateX(${translate}px)`;
        currentIdx += 1;
      }
    } else if (event.target.className === "prev") {
      if (currentIdx !== 0) {
        translate += liWidth;
        slider.style.transform = `translateX(${translate}px)`;
        currentIdx -= 1;
      }
    }
  }
};
