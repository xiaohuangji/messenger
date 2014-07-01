/**
 * Created by wills on 4/22/14.
 */
$("#navbar-service").attr('class','active');


$("#invokeForm").submit(function(event){
    var data=$(this).serialize();
    $.ajax({
        url:'/admin/service/invoke',
        type:'post',
        data:data,
        success:function(result){
            $("#outputTextarea").val(result);
        }
    });
    return false;
});

//$("#userIdInput").bind('input propertychange', function() {
//    var userId=$("#userIdInput").val();
//    var content=$("#contentInput").val();
//    if(userId.length!=0&&content.length!=0){
//        $("#invokeBtn").removeAttr("disabled");
//    }else{
//        $("#invokeBtn").attr("disabled","disabled");
//    }
//});

var serviceChange=function () {
    var serviceName=$("#serviceSelect").val();
    $.ajax({
        url:'/admin/service/getMethod',
        type:'post',
        data:{
            serviceName:serviceName
        },
        success:function(result){
            $("#methodSelect").empty();
            for(var i=0;i<result.length;i++){
                $("#methodSelect").append("<option>"+result[i]+"</option>");
            }
            methodChange();
        }
    });
 };

var methodChange=function(){
    var serviceName=$("#serviceSelect").val();
    var methodName=$("#methodSelect").val();

    $.ajax({
        url:'/admin/service/getParam',
        type:'post',
        data:{
            serviceName:serviceName,
            methodName:methodName
        },
        success:function(result){
            $("#inputTextarea").val(JsonUti.convertToString(result));
        }
    });

    $("#outputTextarea").val("");
}

$("#serviceSelect").change(serviceChange);
$("#methodSelect").change(methodChange);

serviceChange();


