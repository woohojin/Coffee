$.datepicker.setDefaults({
    dateFormat: 'yy-mm-dd',
    prevText: '이전',
    nextText: '다음',
    monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
    monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
    dayNames: ['일', '월', '화', '수', '목', '금', '토'],
    dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
    dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
    showMonthAfterYear: true,
    yearSuffix: '년'
});

$(function(){
    $('#datepickerStart, #datepickerEnd').datepicker();
});

$(function() {
    let $datepickerStart = $('#datepickerStart');
    let $datepickerEnd = $('#datepickerEnd');
    let $datepickerStartButton = $('.datepickerStartButton');
    let $datepickerEndButton = $('.datepickerEndButton');

    $('.member_history_form').on("submit", function() {
        let startDate = $datepickerStart.val();
        let startDateArr = startDate.split('-');
        let endDate = $datepickerEnd.val();
        let endDateArr = endDate.split('-');

        let startDateCompare = new Date(startDateArr[0], parseInt(startDateArr[1])-1, startDateArr[2]);
        let endDateCompare = new Date(endDateArr[0], parseInt(endDateArr[1])-1, endDateArr[2]);

        if(startDateCompare.getTime() > endDateCompare.getTime()) {
            alert("시작날짜가 종료날짜보다 앞설 수 없습니다.");
            return false;
        } else {
            return true;
        }
    });

    $datepickerStartButton.click(function() { // 달력 아이콘 클릭 시 input을 클릭하는 것과 똑같은 동작 하도록
        $datepickerStart.focus().trigger("click");
    })
    $datepickerEndButton.click(function() { // 달력 아이콘 클릭 시 input을 클릭하는 것과 똑같은 동작 하도록
        $datepickerEnd.focus().trigger("click");
    })
});

$(function() {

})