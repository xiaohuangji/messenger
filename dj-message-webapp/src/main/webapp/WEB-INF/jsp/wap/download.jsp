<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="trial" uri="http://www.dajie.com/trial/core" %>
<%@include file="../../include/jsp.html"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>勾搭客户端</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />
	<style>
		body{
			font-family:arial;
			color:#777;
			padding:1em;
			font-size:.9em;
			background:#eee;
		}
	</style>
<body>
	下载勾搭客户端...
	<p>如果下载失败，请选择在浏览器中打开此链接</p>
	<script>
		var type= '';
		if ((/Android|HTC/i.test(navigator.userAgent) || !!(navigator.platform + '').match(/Linux/i))) {
			downloadLink = 'http://www.goudajob.com/';
			//type = 'android';
		} else {
			if (navigator.userAgent.match(/OS [456]/)) {
				downloadLink = 'itms-services://?action=download-manifest&url=http://p0.goudajob.com:8080/GGD_house.plist';
			} else {
				downloadLink = 'itms-services://?action=download-manifest&url=https://p0.goudajob.com:8443/GGD_house.plist';
			}
			type = 'ios';
		}

		(new Image()).src = '/download/clicklog?platform=' + type;
		setTimeout(function() {
			location.href = downloadLink;
		}, 50);
	</script>
</body>
</html>