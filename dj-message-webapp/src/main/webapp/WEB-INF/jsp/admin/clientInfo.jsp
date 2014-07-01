<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../include/jsp.html"%>

<html>
<head>
    <%@include file="../../include/base.html"%>
    <title>client info</title>

</head>
<body>
    <!-- nav -->
    <%@include file="../../include/nav.html"%>

    <!-- Begin page content -->
    <div class="container">

        <div class="page-header">
            <h1>ClientInfo in ${curAppId}</h1>
        </div>

        <div class="table-responsive">
            <table class="table table-striped table-bordered table-hover table-condensed">
                <thead>
                    <tr>
                        <th>AppId</th>
                        <th>UserId</th>
                        <th>ClientId</th>
                        <th>Tag</th>
                        <th>UpdateTime</th>
                        <th>Operation</th>
                    </tr>
                </thead>
                <tbody>

                <c:forEach var="clientInfo" items="${clientInfoList}">
                    <tr>
                        <td>${clientInfo.appId}</td>
                        <td>${clientInfo.userId}</td>
                        <td>${clientInfo.clientId}</td>
                        <td>${clientInfo.tagId}</td>
                        <td>
                             <fmt:formatDate value="${clientInfo.updateTime}" pattern="yyyy-MM-dd HH:mm"/>
                        </td>
                        <td>
                            <button type="button" class="btn btn-xs removeBtn">Remove</button>
                            <button type="button" class="btn btn-xs btn-success sendPrepareBtn">SendPush</button>
                        </td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
        </div>
        <%@include file="../../include/page.html"%>
    </div>

    <div id="myModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">Confirmation</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="removeForm">
                        <div class="form-group has-info">
                            <label class="control-label col-xs-2">AppId</label>
                            <p class="form-control-static" id="modal-appId"></p>
                        </div>
                        <div class="form-group has-warning">
                            <label class="control-label col-xs-2">UserId</label>
                            <p class="form-control-static" id="modal-userId"></p>
                        </div>
                        <div class="form-group has-warning">
                            <label class="control-label col-xs-2">ClientId</label>
                            <p class="form-control-static" id="modal-clientId"></p>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default btn-xs" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary btn-xs" id="confirmRemoveBtn">Remove</button>
                </div>
            </div>
        </div>
    </div>

    <!-- foot -->
    <%@include file="../../include/foot.html"%>
    <script type="application/javascript" src="../../js/clientInfo.js"></script>

</body>

</html>