/**
 * Created by wills on 5/13/14.
 */

$("#navbar-pushjob").attr('class','active');


var getDate=function(){
    var date=new Date();
    return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+(date.getDate())+" "+date.getHours()+":"+date.getMinutes();
}

var pickerOption={
    format:"yyyy-mm-dd hh:ii",
    startDate: getDate(),
    endDate:"2100-5-1 10:00",
    autoclose: true,
    minuteStep:5
}

$("#triggerDatePicker").datetimepicker(pickerOption);

$(".opPrepareCancelBtn").click(function(){
    var tds=$(this).parent().parent().children();
    $("#idHidden").text(tds.eq(0).html());
    $("#cancelModal").modal('show');
});

$("#cancelBtn").click(function(){
    $("#cancelModal").modal('hide');
    var id=$("#idHidden").text();
    $.ajax({
        url:'/admin/pushjob/cancel',
        type:'post',
        data:{
            id:id
        },
        success:function(result){
            location.reload();
        }
    });
});


$("#preparePushJobBtn").click(function(){
    $("#pushjobModal").modal('show');
});

$("#confirmPushJobBtn").click(function(){
    var content=$("textarea[name='content']").val();
    var triggerDate=$("input[name='triggerDate']").val();
    var operator=$("input[name='operator']").val();

    if(content.length==0||triggerDate.length==0||operator.length==0){
        alert("必填项不能为空");
        return;
    }

    $("#pushjobModal").modal('hide');
    var data=$("#pushjobForm").serialize();

    var filterJob=$("select[name='filterJobType']").val();
    var filterIndustry=$("select[name='filterIndustry']").val();
    var filterJobTypeText = $("select[name='filterJobType'] option[value="+filterJob+"]").text();
    var filterIndustryText =$("select[name='filterIndustry'] option[value="+filterIndustry+"]").text();

    //"content=sss&filterIsVerified=-1&filterGender=0&triggerDate=2014-05-27+11%3A50&filterJobType=3&filterIndustry=26&filterCity=%E5%85%A8%E9%83%A8&operator=ss"

    $.ajax({
        url:'/admin/pushjob/add',
        type:'post',
        data:data+"&filterJobTypeText="+filterJobTypeText+"&filterIndustryText="+filterIndustryText,
        success:function(result){
            location.reload();
        }
    });

});