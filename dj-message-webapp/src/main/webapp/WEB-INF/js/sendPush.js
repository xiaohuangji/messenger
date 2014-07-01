/**
 * Created by wills on 4/22/14.
 */

$("#navbar-send").attr('class','active');

var checkSendBtn=function(){
    var userId=$("#userIdInput").val();

    if(userId.length!=0&&!isNaN(userId)){
        $("#sendBtn").removeAttr("disabled");
    }else{
        $("#sendBtn").attr("disabled","disabled");
    }
}

$("#sendForm").submit(function(event){
    var data=$(this).serialize();
    $.ajax({
        url:'/admin/push/send',
        type:'post',
        data:data,
        success:function(result){
            if(result==0){
                $("#sendSucc").show();
                setTimeout(function(){
                    $("#sendSucc").hide();
                },1000);
            }else{
                $("#sendFail").show();
                setTimeout(function(){
                    $("#sendFail").hide();
                },2000);
            }
        }
    });

    $("#userIdInput").val("");
    $("#sendBtn").attr("disabled","disabled");
    return false;
});


$("#userIdInput").bind('input propertychange', checkSendBtn);

var template={
    11:{
        text:"input text"
    },
    12:{
        soundUrl:"input soundurl",
        duration:10

    },
    13:{
        imgUrl:"input image url",
        width:100,
        height:200

    }
}

$("#msgTypeSelect").change(function () {
    var msgType=$("#msgTypeSelect").val();
    $("#contentInput").val(JsonUti.convertToString(template[msgType]));

});

$("#contentInput").val(JsonUti.convertToString(template[11]));


