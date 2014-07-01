<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="trial" uri="http://www.dajie.com/trial/core" %>
<%@include file="../../include/jsp.html"%>

<%@ include file="common/_header.jsp" %>

<body class="profile recommand">

	<%@ include file="common/_top.jsp" %>

	<div class="main_wrap">
		<h2 class="head"></h2>

        <c:forEach var="profile" items="${profileList}" varStatus="item">
           <section class="personal-info">
			<dl>
				<dt>
					<a href="${profileUrlMap[profile.userId]}">
						<img src="${profile.avatar}" alt="">
					</a>
				</dt>
				<dd>
					<h3><a href="${profileUrlMap[profile.userId]}">${profile.name}</a>
					    <c:if test="${status==2}">
					    <span>
					    <c:choose>
					      <c:when test="${profile.verification==1}"> 认 证</c:when>
					      <c:otherwise>未认证</c:otherwise>
					    </c:choose>
					    </span>
					    </c:if>
					</h3>
					<c:if test="${statusMap[profile.userId]==2}">
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
					<c:if test="${statusMap[profile.userId]==1}">
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
        </c:forEach>

	</div>

	<%@ include file="common/_footer.jsp" %>
	
</body>
</html>