<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="trial" uri="http://www.dajie.com/trial/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@include file="../../include/jsp.html"%>

<%@ include file="common/_header.jsp" %>

 <body class="job">

	<%@ include file="common/_top.jsp" %>

	<div class="main_wrap">
		<h2 class="head"></h2>
		<section class="personal-info">
			<dl>
				<dt>
					<!-- 头像 -->
					<img src="${job.hrAvatar}" alt="">
				</dt>
				<dd>
					<h3>${job.hrName}</h3>
					<p>${job.hrCorp}</p>
					<p>${job.hrTitle}</p>
					<p><fmt:formatDate value="${job.startDate}" pattern="MM-dd hh:mm"/></p>
				</dd>
			</dl>
		</section>
		<section >
			<dl class="position-desc">
				<dt><span>
				 <c:choose>
				  <c:when test="${job.salaryStart<1000}">面议</c:when>
				  <c:when test="${job.salaryStart>=1000&&job.salaryEnd<1000}">${job.salaryStart}以上</c:when>
				  <c:otherwise>${job.salaryStart}-${job.salaryEnd}</c:otherwise>
				 </c:choose>
				 </span>
				 ${job.positionName}
				</dt>
				<dd>
					<p>${job.corpName}</p>
					<p>
						<c:choose>
							<c:when test="${job.workExperience==0}"> 工作经验不限</c:when>
							<c:when test="${job.workExperience==10}"> 1年以下工作经验</c:when>
							<c:when test="${job.workExperience==20}"> 1-3年工作经验</c:when>
							<c:when test="${job.workExperience==30}"> 3-5年工作经验</c:when>
							<c:when test="${job.workExperience==40}"> 5-8年工作经验</c:when>
							<c:when test="${job.workExperience==50}"> 8年以上工作经验</c:when>
							<c:otherwise>工作经验不限</c:otherwise>
						</c:choose>
						 |
						<c:choose>
					      <c:when test="${job.educationLevel==10}"> 博士</c:when>
					      <c:when test="${job.educationLevel==12}"> 硕士</c:when>
					      <c:when test="${job.educationLevel==13}"> 本科</c:when>
					      <c:when test="${job.educationLevel==14}"> 大专</c:when>
					      <c:when test="${job.educationLevel==9}"> 其他</c:when>
					      <c:otherwise>其他</c:otherwise>
					    </c:choose>
					</p>
					<p>工作地点：${job.poiInfoObject[0].city}</p>
					<p>有效日期：<fmt:formatDate value="${job.startDate}" pattern="yyyy.MM"/> - <fmt:formatDate value="${job.endDate}" pattern="yyyy.MM"/></p>
				</dd>
			</dl>
			<h4>机会亮点</h4>
			<ul class="personal-hight">
				<c:forEach var="description" items="${job.jobDescriptionModels}">
						<li>
							<p>${description.description}</p>
						</li>
				</c:forEach>
			</ul>
		</section>
	</div>

	<%@ include file="common/_footer.jsp" %>

</body>
</html>