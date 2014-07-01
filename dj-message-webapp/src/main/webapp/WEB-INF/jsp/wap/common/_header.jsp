<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="trial" uri="http://www.dajie.com/trial/core" %><!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>勾搭—基于地理位置的即时招聘</title>

		<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />
		<meta content="telephone=no" name="format-detection" />
		<meta http-equiv="x-ua-compatible" content="ie=edge" />

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
