<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="trial" uri="http://www.dajie.com/trial/core"%><!DOCTYPE html>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>勾搭—基于地理位置的即时招聘</title>

<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />
<meta content="telephone=no" name="format-detection" />
<meta http-equiv="x-ua-compatible" content="ie=edge" />

<meta property="og:image"
	content="http://assets.dajieimg.com/up/dj-mobile/hook_up/i/app_logo.png" />
<meta property="og:title" content="勾搭—基于地理位置的即时招聘" />
<meta property="og:description" content="人才机会，勾到手边！" />
<meta property="og:url" content="" />

<script type="text/javascript">
	var UA = navigator.userAgent, IsAndroid = (/Android|HTC/i.test(UA) || !!(navigator['platform'] + '')
			.match(/Linux/i)), IsIOS = !IsAndroid
			&& /iPad|iPod|iPhone/i.test(UA), IsWeixin = /MicroMessenger/i
			.test(UA), elHTML = document.getElementsByTagName('html')[0];

	if (IsAndroid) {
		elHTML.className = 'm android';
	} else if (IsIOS) {
		elHTML.className = 'm ios';
	}
</script>
<trial:assertcss files="/up/dj-mobile/hook_up/css/reset.css" />
<trial:assertcss files="/up/dj-mobile/hook_up/css/home.css" />
<trial:assertcss files="/up/dj-mobile/hook_up/css/home_wap.css" />
</head>

<body>

	<div class="head" id="head">
		<div class="share">
			<a target="_blank" class="renren" title="分享到人人">分享到人人</a> <a
				target="_blank" class="weibo" title="分享到新浪微博">分享到新浪微博</a> <a
				target="_blank" class="douban" title="分享到豆瓣">分享到豆瓣</a> <a
				target="_blank" class="tencent" title="分享到腾讯微博">分享到腾讯微博</a>
		</div>
		<h1>勾搭</h1>
		<p>基于地理位置的即时招聘</p>
		<span id="hand">勾搭招聘</span>
	</div>

	<div class="download_wrap">
		<div class="ani_wrap">
			<b class="cloud cloud_1"></b><b class="cloud cloud_2"></b><b
			class="cloud cloud_3"></b><b class="cloud cloud_4"></b><b
			class="plane plane_1"></b><b class="plane plane_2"></b><b
			class="plane plane_3"></b>
		</div>
		<p>人才机会，勾到手边</p>
		<span> <%@ include file="common/_download_link.jsp"%>
		</span>

	</div>

	<div class="section_wrap map_wrap">
		<b class="mask"></b>
		<div class="section">
			<div class="phone">
				<b class="p p_1"></b>
				<div class="p p_2"></div>
			</div>
			<div class="text">
				<h2>人才与机会第一次面对面</h2>
				<p>基于真实地理位置的即时招聘，让你突破传统招聘的重重障碍，清晰定位人才与机会。</p>
			</div>
		</div>

	</div>

	<div class="section_wrap section_1">
		<div class="section" id="section_1">
			<div class="cat"></div>
			<div class="phone">
				<b class="p">
					<div>
						<em class="avatar" id="avatar"> <i class="a_1"></i> <i
							class="a_2"></i> <i class="a_3"></i> <i class="a_4"></i>
						</em> <b><i>MR.Z</i><i class="i">认证</i></b> <span>新咚咚英语培训</span> <span>英语教师</span>
						<span>5年经验 | 本科</span>
					</div>
				</b>
			</div>
			<div class="text">
				<h2>史上最贴心的功能－隐私名片</h2>
				<p>
					<span>求职原来可以匿名，你会化名Mr. A-Z，头像覆盖猫面具，</span><span>分享到朋友圈超级放心 。</span>
				</p>
			</div>
			<div id="lightning">
				<b class="l_1"></b> <b class="l_2"></b> <b class="l_3"></b> <b
					class="l_4"></b>
			</div>
		</div>
	</div>

	<div class="section_wrap section_2">
		<div class="section">
			<div class="cat"></div>
			<div class="phone phone_text" id="phone_text">
				<b class="p"> <b class="t1"></b> <b class="t2"></b>
				</b> <span class="s_1 p_1"><i>126</i>33岁去过33个国家</span> <span
					class="s_1 p_2"><i>88</i>和乔帮主一天生日</span> <span class="s_1 p_3"><i>320</i>业余说相声</span>
				<span class="s_1 p_4"><i>130</i>共教过三千人英语</span> <span
					class="s_2 p_5">处女座优先录取</span> <span class="s_2 p_6">不用打卡超弹工作制</span>
				<span class="s_2 p_7">机战宅欢迎勾搭</span>
			</div>
			<div class="text">
				<h2>求职秀亮点，招聘抛诱惑</h2>
				<p>
					<span>添加个人亮点，快速秀出关键技能；列出机会诱惑，</span><span>瞬间勾搭无数人才－求职与招聘从此告别繁复。</span>
				</p>
			</div>
		</div>
	</div>

	<div class="section_wrap section_3">
		<div class="section" id="section_3">
			<div class="cat"></div>
			<div class="phone">
				<b class="p"> <b class="chat"> <span class="c_1"></span> <span
						class="c_2"></span> <span class="c_3"></span> <span class="c_4"></span>
				</b> <b class="input"></b>
				</b>
			</div>
			<div class="text">
				<h2>即时聊天，随时拿Offer</h2>
				<p>
					<span>发名片发语音发位置发高清大图，使用各种勾搭技巧，</span><span>绝赞机遇、靠谱人才——聊聊就有So
						easy！</span>
				</p>
			</div>
		</div>
	</div>

	<div class="footer">&copy; 2014 大街网 Dajie.com</div>

	<trial:assertjs files="/up/dj-mobile/hook_up/js/weixin.js" />
	<trial:assertjs files="/up/dj-mobile/hook_up/js/home.js" />

</body>
</html>
