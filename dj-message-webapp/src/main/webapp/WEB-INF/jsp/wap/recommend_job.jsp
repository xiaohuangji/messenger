<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="trial" uri="http://www.dajie.com/trial/core" %>
<%@include file="../../include/jsp.html"%><!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>勾搭—基于地理位置的即时招聘</title>

		<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />
		<meta content="telephone=no" name="format-detection" />
		<meta http-equiv="x-ua-compatible" content="ie=edge" />

		<!-- 微信分享的图片和标题、描述 -->
		<meta property="og:image" content="http://assets.dajieimg.com/up/dj-mobile/hook_up/i/app_logo.png" />
		<meta property="og:title" content="${share_title}" />
		<meta property="og:description" content="${share_desc}" />
		<meta property="og:url" content="" />

		<script type="text/javascript">
			var UA = navigator.userAgent,
			IsAndroid = (/Android|HTC/i.test(UA) || !!(navigator['platform'] + '').match(/Linux/i)),
			IsIOS = !IsAndroid && /iPad|iPod|iPhone/i.test(UA),
			IsWeixin =  /MicroMessenger/i.test(UA),
			elHTML = document.getElementsByTagName('html')[0];

			if (IsAndroid) {
				var className = 'm android';

				var sysVersion;
				if ((sysVersion = UA.match(/Android[\s\/]([0-9\._]+)/i))) {
					sysVersion = parseFloat(sysVersion[1].replace(/_/g, '.').replace(/^([0-9]+\.[0-9]+)[0-9\.]*/, '$1') || 0);
					if (sysVersion < 2.4) {
						className += ' fixed_nav';
					}
				}
				elHTML.className = className;

			} else if (IsIOS) {
				elHTML.className = 'm ios';
			}
		</script>

		<trial:assertcss files="/up/dj-mobile/hook_up/css/reset.css" />
        <trial:assertcss files="/up/dj-mobile/hook_up/css/share.css" />
	</head>


<body class="job recommand">

	
	<div class="header">
		<h1>勾搭</h1>
		<p>人才机会，勾到手边</p>
		<div class="download_wrap">
			
	<a class="app_dowaload download_iphone" type="ios" id="download_iphone"  ios6="itms-services://?action=download-manifest&url=http://p0.goudajob.com:8080/GGD_house.plist" href="itms-services://?action=download-manifest&url=https://p0.goudajob.com:8443/GGD_house.plist">iPhone下载</a>
	<a  class="app_dowaload download_android disabled" type="android" id="download_android">敬请期待</a>
		</div>
	</div>



	<div class="main_wrap">
		<h2 class="head"></h2>
		
		<c:forEach var="jobInfo" items="${simpleJobInfos}" varStatus="item">
		  <section class="job-info">
		    <a href="${jobUrlMap[jobInfo.jobId]}">
			<dl class="position-desc">
				<dt><span>
				<c:choose>
				  <c:when test="${jobInfo.salaryStart<1000}">面议</c:when>
				  <c:when test="${jobInfo.salaryStart>=1000&&jobInfo.salaryEnd<1000}">${jobInfo.salaryStart}以上</c:when>
				  <c:otherwise>${jobInfo.salaryStart}-${jobInfo.salaryEnd}</c:otherwise>
				</c:choose>
				</span>${jobInfo.positionName}</dt>
				<dd>
					<p>${jobInfo.corpName}</p>
					<p><span>${jobInfo.hrName}发布</span>
					<c:choose>
							<c:when test="${jobInfo.workExperience==0}"> 工作经验不限</c:when>
							<c:when test="${jobInfo.workExperience==10}"> 1年以下工作经验</c:when>
							<c:when test="${jobInfo.workExperience==20}"> 1-3年工作经验</c:when>
							<c:when test="${jobInfo.workExperience==30}"> 3-5年工作经验</c:when>
							<c:when test="${jobInfo.workExperience==40}"> 5-8年工作经验</c:when>
							<c:when test="${jobInfo.workExperience==50}"> 8年以上工作经验</c:when>
							<c:otherwise>工作经验不限</c:otherwise>
						</c:choose>
					 | 
					     <c:choose>
					      <c:when test="${jobInfo.educationLevel==10}">博士</c:when>
					      <c:when test="${jobInfo.educationLevel==12}">硕士</c:when>
					      <c:when test="${jobInfo.educationLevel==13}">本科</c:when>
					      <c:when test="${jobInfo.educationLevel==14}">大专</c:when>
					      <c:otherwise>其他</c:otherwise>
					    </c:choose></p>
				</dd>
			</dl>
			</a>
	      </section>
		</c:forEach>
		
	</div>

	
    <trial:assertjs files="/up/dj-mobile/hook_up/js/weixin.js" />

</body>
</html>