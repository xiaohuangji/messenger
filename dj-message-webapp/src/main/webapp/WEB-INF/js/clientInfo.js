/**
 * Created by wills on 4/21/14.
 */

$("#navbar-client").attr('class','active');

$('.removeBtn').click(function(event) {
    var tds=$(this).parent().parent().children();
    $("#modal-appId").text(tds.eq(0).html());
    $("#modal-userId").text(tds.eq(1).html());
    $("#modal-clientId").text(tds.eq(2).html());
    $("#myModal").modal('show');
});

$('.sendPrepareBtn').click(function(event) {
    var tds=$(this).parent().parent().children();
    var appId=tds.eq(0).html();
    var userId=tds.eq(1).html();
    location.href="/admin/push/sendPush?appId="+appId+"&userId="+userId;
});

//
//$('.table tr').dblclick(function(event) {
//    var tds=$(this).children();
//    var tdsLen=tds.size();
//    for(var i=0;i<tdsLen;i++){
//        var content=tds.eq(i).html();
//       // alert(content);
//    }
//    $("#modal-pinfo").text(tds.eq(1).html());
//    $("#myModal").modal('show');
//});

$("#confirmRemoveBtn").click(function(){

    $("#myModal").modal('hide');
    var appId= $("#modal-appId").html();
    var userId=$("#modal-userId").html();
    var clientId=$("#modal-clientId").html();
    $.ajax({
        url:'/admin/push/removeClientInfo',
        type:'post',
        data:{
            appId:appId,
            userId:userId,
            clientId:clientId
        },
        success:function(result){
            location.reload();
        }
    });
});
