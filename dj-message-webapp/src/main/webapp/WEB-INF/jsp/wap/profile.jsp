<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="trial" uri="http://www.dajie.com/trial/core" %>
<%@include file="../../include/jsp.html"%>

<%@ include file="common/_header.jsp" %>

<body class="profile">

    <%@ include file="common/_top.jsp" %>

	<div class="main_wrap">
		<h2 class="head"></h2>
		<section class="personal-info">
			<dl>
				<dt>
					<img src="${avatarShow}" alt="">
				</dt>
				<dd>
					<h3>${profile.name}
					    <c:if test="${status==2}">
					    <span>
					    <c:choose>
					      <c:when test="${profile.verification==1}"> 认 证</c:when>
					      <c:otherwise>未认证</c:otherwise>
					    </c:choose>
					    </span>
					    </c:if>
					</h3>
					<c:if test="${status==2}">
					  <p>${profile.corpName}</p>
					  <p>${profile.positionName }</p>
					  <p>${profile.experience}年经验 | 
					    <c:choose>
					      <c:when test="${profile.education==10}">博士</c:when>
					      <c:when test="${profile.education==12}">硕士</c:when>
					      <c:when test="${profile.education==13}">本科</c:when>
					      <c:when test="${profile.education==14}">大专</c:when>
					      <c:otherwise>其他</c:otherwise>
					    </c:choose>
				  	  </p>
					</c:if>
					<c:if test="${status==1}">
					  <p>${profile.school}</p>
					  <p>${profile.major }</p>
					  <p>
					    <c:choose>
					      <c:when test="${profile.degree==10}">博士</c:when>
					      <c:when test="${profile.degree==12}">硕士</c:when>
					      <c:when test="${profile.degree==13}">本科</c:when>
					      <c:when test="${profile.degree==14}">大专</c:when>
					      <c:otherwise>其他</c:otherwise>
					    </c:choose>
				  	  </p>
					</c:if>
				</dd>
			</dl>
		</section>
		<section>
			<h4>个人亮点</h2>
			<ul class="personal-hight">
			    <c:forEach var="label" items="${profile.labels}">
			      <li>
			         <p>${label.content}</p>
			         <span>${label.likeCounts}</span>
			      </li>
			    </c:forEach>
			</ul>
		</section>
	</div>
    
    <%@ include file="common/_footer.jsp" %>
	
</body>
</html>