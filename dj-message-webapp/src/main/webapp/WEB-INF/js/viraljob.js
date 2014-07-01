$("#navbar-viral").attr('class', 'active');
$("#nav-viraljob").attr('class', 'active');

var getDate = function(n) {
    var date = new Date(new Date-0+n*86400000);
    return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + (date.getDate());
}

var pickerOption = {
    format: "yyyy-mm-dd",
    startDate: getDate(-14),
    endDate: getDate(0),
    minView:2,
    maxView:2,
    autoclose: true,
    minuteStep:20

}

$("#timePicker").datetimepicker(pickerOption);

//$("#time").val(getDate(-7));

$("#vjSearchBtn").click(function() {
    var time = $("#time").val()+" 00:00";
//    var data = $("#vjSearchForm").serialize();

    window.location.href = "/admin/viral/getjobs?time" +time;
});

$("input[name='checkbox']").click(function() {
    if ($(this).attr("checked")) {
        $(this).attr("checked", false);
    } else {
        $(this).attr("checked", true);
    }
})

$("#vjSendBtn").click(function(event) {

    var str = "";
    var i=0;
    var ids="";
    $("input[name='checkbox'][checked]").each(function() {
        ids=ids+$(this).val()+",";
        i++;
    });

    if(i!=5){
        alert("请选择5条工作");
        return;
    }
    $.ajax({
        url: '/admin/viral/sendJobMail',
        type: 'post',
        data: {
            ids:ids.substring(0,ids.length-1)
        },
        success: function(result) {
            $("#urlP").val(result);
//            $("#urlModal").modal('show');
        }
    });
})

$('#copyBtn').zclip({
    path:'/js/ZeroClipboard.swf',
    copy: function(){
        return $("#urlP").val();
    }
});


