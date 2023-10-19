function searchSubmit() {
  if(document.getElementById("search_text").value="") {
    alert("검색어를 입력해주세요.");
    return false;
  }
document.getElementById("search_form").submit();
}
