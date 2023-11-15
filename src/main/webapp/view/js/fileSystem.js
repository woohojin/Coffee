function fileUpload() {
    const op = "width=500, height=150, left=50, top=150";
    window.open("../board/fileUploadForm", "", op);
}

function fileDownload(productFile, productName) {
    if(confirm("파일을 다운로드 하시겠습니까?") === true) {
        $.ajax({
            type: "GET",
            url: "./fileDownload?fileName=" + productFile,
            xhrFields: {
                responseType: "blob"
            },
            success: function(data, status, jqXHR) {
                var blob = new Blob([data], {type: jqXHR.getResponseHeader("Content-Type")});
                var url = window.URL.createObjectURL(blob);

                var a = $("<a>")
                    .attr("href", url) //attribute
                    .attr("download", productName + ".jpg")
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