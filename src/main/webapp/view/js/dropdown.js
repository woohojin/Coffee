const hoverObject = document.querySelector(".hd_lnb_dropdown_wrap");
const dropdown = document.querySelector(".hd_lnb_dropdown");

hoverObject.addEventListener("mouseover", () => {
  dropdown.style.height = "150px";
  dropdown.style.borderTop = "1px solid var(--grayLine)";
  dropdown.style.borderBottom = "1px solid var(--grayLine)";
});
hoverObject.addEventListener("mouseout", () => {
  dropdown.style.height = "0";
  dropdown.style.borderTop = "none";
  dropdown.style.borderBottom = "none";
});