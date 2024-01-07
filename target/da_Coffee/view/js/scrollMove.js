$(document).ready(function() {
  $(".scrollBtn1").on("click", function(event) {
    event.preventDefault();
    let offset = $(".scroll1").offset();
    offset.top -= 160;
    $("html, body").animate({ scrollTop:offset.top }, 800);
  });
  $(".scrollBtn2").on("click", function(event) {
    event.preventDefault();
    let offset = $(".scroll2").offset();
    offset.top -= 160;
    $("html, body").animate({ scrollTop:offset.top }, 800);
  });
  $(".scrollBtn3").on("click", function(event) {
    event.preventDefault();
    let offset = $(".scroll3").offset();
    offset.top -= 160;
    $("html, body").animate({ scrollTop:offset.top }, 800);
  });
  $(".scrollBtn4").on("click", function(event) {
    event.preventDefault();
    let offset = $(".scroll4").offset();
    offset.top -= 160;
    $("html, body").animate({ scrollTop:offset.top }, 800);
  });
});