<!doctype html>
<%@ page language="java" import="java.util.*"  pageEncoding="UTF-8" %>
<html lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="x-ua-compatible" content="ie=7"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <!-- 全站公用css -->
    <!--#include virtual="/up/job-project-2013/demo/dajie.html"-->
    <!-- 本页需要的静态文件 -->
    <link rel="stylesheet" href="../css/reset.css">
    <link rel="stylesheet" href="../css/mail_verify.css">
    <title>邮箱验证成功</title>
    <script type="text/javascript">
        var UA = navigator.userAgent,
        IsAndroid = (/Android|HTC/i.test(UA) || !!(navigator['platform'] + '').match(/Linux/i)),
        IsIOS = !IsAndroid && /iPad|iPod|iPhone/i.test(UA),
        IsWeixin =  /MicroMessenger/i.test(UA),
        elHTML = document.getElementsByTagName('html')[0],
        IsMobile = IsAndroid || IsIOS;
        
        if (IsAndroid) {
            elHTML.className = 'm android';
        } else if (IsIOS) {
            elHTML.className = 'm ios';
        }
    </script>
</head>
<body class="">
    <div class="p-page-wrap">
        <div class="p-main-img">
            <h2>邮箱链接过期啦~</h2>
            <p>邮箱链接过期<br>请重新进行邮箱验证</p>
            <span></span>
        </div>
        <div class="p-btn-box">
            <!-- phone端的按钮 -->
            <a ontouchstart="" class="p-phone-btn" href="goudajob://goudajob">返回应用</a>
            <a ontouchstart="" class="p-phone-btn p-btn-nom" href="http://www.goudajob.com">去勾搭首页</a>
        </div>
    </div>
</body>
</html>