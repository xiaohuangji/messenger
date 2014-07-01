$("#navbar-viral").attr('class', 'active');
$("#nav-viraluser").attr('class', 'active');

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
    autoclose: true
}

$("#timePicker").datetimepicker(pickerOption);

//$("#time").val(getDate(-7));

$("#vuSearchBtn").click(function() {
    var time = $("#time").val()+" 00:00";
//    var data = $("#vjSearchForm").serialize();

    window.location.href = "/admin/viral/getusers?time" +time;
});

$("input[name='checkbox']").click(function() {
    if ($(this).attr("checked")) {
        $(this).attr("checked", false);
    } else {
        $(this).attr("checked", true);
    }
})

$("#vuSendBtn").click(function() {
    var str = "";
    var i=0;
    var ids="";
    $("input[name='checkbox'][checked]").each(function() {
        ids=ids+$(this).val()+",";
        i++;
    });

    if(i!=5){
        alert("请选择5个用户");
        return;
    }
    $.ajax({
        url: '/admin/viral/sendUserMail',
        type: 'post',
        data: {
            ids:ids.substring(0,ids.length-1)
        },
        success: function(result) {
            $("#urlP").val(result);
//            $("#urlModel").modal('show');
        }
    });
})

$('#copyBtn').zclip({
    path:'/js/ZeroClipboard.swf',
    copy: function(){
        return $("#urlP").val();
    }
});

