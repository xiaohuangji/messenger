

$("#navbar-feedback").attr('class','active');

var pickerOption={
    format:"yyyy-mm-dd hh:ii",
    startDate: "2014-5-1 10:00",
    endDate:"2100-5-1 10:00",
    autoclose: true,
    minuteStep:60
}

$("#startTimePicker").datetimepicker(pickerOption);
$("#endTimePicker").datetimepicker(pickerOption);


$("#fbSearchBtn").click(function(){
    //check 时间
    var startTime=$("#idHidden").text();
    var endTime=$("#idHidden").text();


    var data=$("#fbSearchForm").serialize();
    window.location.href="/admin/feedback/get?"+data;
});

$("#fbClearBtn").click(function(){
    $("input").val("");
    $("select").val("0");

});

$("#fbRefreshBtn").click(function(){
    var data=$("#fbSearchForm").serialize();
//    $.post("/admin/feedback/get",data,function(result){
//        //alert(result);
//        document.write(result);
//    });

    window.location.href="/admin/feedback/get?"+data;
});

$(".opHandleBtn").click(function(){
    //获取当时选中行
    var tds=$(this).parent().parent().children();
    var id=tds.eq(0).html();
    $.ajax({
        url:'/admin/feedback/updateStatus',
        type:'post',
        data:{
            status:2,
            id:id
        },
        success:function(result){
            location.reload();
        }
    });
});

$(".opPrepareReplyBtn").click(function(){
    var tds=$(this).parent().parent().children();
    $("#idHidden").text(tds.eq(0).html());
    $("#userIdHidden").text(tds.eq(1).html());
    $("#replyModal").modal('show');
});


$("#sendReplyBtn").click(function(){
    $("#replyModal").modal('hide');
    var userId=$("#userIdHidden").text();
    var id=$("#idHidden").text();
    var reply=$("#replyInput").val();
    $.ajax({
        url:'/admin/feedback/reply',
        type:'post',
        data:{
            userId:userId,
            reply:reply,
            id:id
        },
        success:function(result){
            location.reload();
        }
    });
});

$("#deleteBtn").click(function(){
    $("#deleteModal").modal('hide');
    var id=$("#idHidden").text();
    $.ajax({
        url:'/admin/feedback/updateStatus',
        type:'post',
        data:{
            status:9,
            id:id
        },
        success:function(result){
            location.reload();
        }
    });
});

$(".opPrepareDeleteBtn").click(function(){
    var tds=$(this).parent().parent().children();
    $("#idHidden").text(tds.eq(0).html());
    $("#deleteModal").modal('show');

});

