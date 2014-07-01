
$("#navbar-jobreport").attr('class','active');

var pickerOption={
    format:"yyyy-mm-dd hh:ii",
    startDate: "2014-5-1 10:00",
    endDate:"2100-5-1 10:00",
    autoclose: true,
    minuteStep:60
}

$("#startTimePicker").datetimepicker(pickerOption);
$("#endTimePicker").datetimepicker(pickerOption);


$("#jrSearchBtn").click(function(){

    var data=$("#jrSearchForm").serialize();
    window.location.href="/admin/jobreport/get?"+data;
});

$("#jrClearBtn").click(function(){
    $("input").val("");
//    $("select").val("0");

});


//获取详情
$(".opGetDetailBtn").click(function(){
    var tds=$(this).parent().parent().children();
    var id=tds.eq(3).html();

    //$("#idHidden").text(tds.eq(0).html());
    $.ajax({
        url:'/admin/jobreport/getDetail',
        type:'post',
        data:{
            id:id
        },
        success:function(result){
            //将result 显示到detail中
            //alert(result['hrName']);

            $("#userIdP").html(result['userId']);
            $("#jobNameP").html(result['positionName']);
            $("#jobTypeP").html(result['jobType']);
            $("#corpNameP").html(result['corpName']);
            $("#corpNameP").html(result['corpName']);
            $("#salaryP").html(result['salaryStart']+"-"+result['salaryEnd']);
            try{
                $("#descriptionsP").html(result['jobDescriptionModels'][0]['description']);
            }catch(err){
                $("#descriptionsP").html();
            }
            try{
                $("#poisP").html(result['poiInfoObject'][0]['address']);
            }catch(err){
                $("#poisP").html();
            }
            $("#endTimeP").html(new Date(result['endDate']));
            $("#jobDetailModal").modal('show');
        }
    });

});

//下线
$(".opDeleteBtn").click(function(){
    //获取当时选中行
    var tds=$(this).parent().parent().children();
    var id=tds.eq(0).html();
    var jobId=tds.eq(3).html();

    $.ajax({
        url:'/admin/jobreport/updateStatus',
        type:'post',
        data:{
            status:1,
            id:id,
            jobId:jobId
        },
        success:function(result){
            location.reload();
        }
    });
});


//忽略
$(".opIgnoreBtn").click(function(){
    var tds=$(this).parent().parent().children();
    var id=tds.eq(0).html();
    var jobId=tds.eq(3).html();

    $.ajax({
        url:'/admin/jobreport/updateStatus',
        type:'post',
        data:{
            status:2,
            jobId:jobId,
            id:id
        },
        success:function(result){
            location.reload();
        }
    });
});


