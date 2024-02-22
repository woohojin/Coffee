function fileDownload(file, fileName) {
    if(confirm("파일을 다운로드 하시겠습니까?") === true) {
        $.ajax({
            type: "GET",
            url: "../board/fileDownload?fileName=" + file,
            xhrFields: {
                responseType: "blob"
            },
            success: function(data, status, jqXHR) {
                var blob = new Blob([data], {type: jqXHR.getResponseHeader("Content-Type")});
                var url = window.URL.createObjectURL(blob);
                var a = $("<a>")
                    .attr("href", url) //attribute
                    .attr("download", fileName + ".jpg")
                    .appendTo("body") //body에 담기
                a[0].click();
                setTimeout(function() {
                    window.URL.revokeObjectURL(url);
                }, 10000);
            },
            error: function() {
                alert("파일을 다운로드하는 중 문제가 발생했습니다.");
            }
        });
    } else {
        return false;
    }
}