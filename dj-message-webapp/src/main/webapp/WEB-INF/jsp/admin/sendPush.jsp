<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../include/jsp.html"%>

<html>
<head>
    <%@include file="../../include/base.html"%>

    <title>Send Push</title>
</head>
<body>
    <!-- nav -->
    <%@include file="../../include/nav.html"%>

    <!-- Begin page content -->
    <div class="container">

        <table class="table">
            <tbody>
                <tr class="page-header">
                    <td class="p-td-head">
                        <div>
                            <h1>Send Push</h1>
                        </div>
                    </td>
                    <td class="p-td-alert">
                        <div id="sendSucc" class="alert alert-success p-alert">
                            <%--<a href="#" class="close" data-dismiss="alert">&times;</a>--%>
                            <strong>Success!</strong> There was a problem with your network connection.
                        </div>
                        <div id="sendFail" class="alert alert-danger p-alert">
                                <%--<a href="#" class="close" data-dismiss="alert">&times;</a>--%>
                            <strong>Success!</strong> There was a problem with your network connection.
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>

        <form class="form-horizontal" id="sendForm">
            <%--<div class="form-group has-warning">--%>
                <%--<label class="control-label col-xs-1">AppId</label>--%>
                <%--<div class="col-xs-10">--%>
                    <%--<c:if refection="${curAppId==null}">--%>
                    <%--<select class="from-control p-select" name="appId">--%>
                    <%--</c:if>--%>
                    <%--<c:if refection="${curAppId!=null}">--%>
                    <%--<select class="from-control p-select" disabled="disabled" name="appId">--%>
                    <%--</c:if>--%>
                        <%--<c:forEach var="appId" items="${appIds}">--%>
                            <%--<c:if refection="${curAppId==appId}">--%>
                                <%--<option selected="selected">${appId}</option>--%>
                            <%--</c:if>--%>
                            <%--<c:if refection="${curAppId!=appId}">--%>
                                <%--<option>${appId}</option>--%>
                            <%--</c:if>--%>
                        <%--</c:forEach>--%>
                    <%--</select>--%>
                <%--</div>--%>
            <%--</div>--%>
            <div class="form-group has-warning">
                <label class="control-label col-xs-1">msgType</label>
                <div class="col-xs-1">
                    <select class="from-control p-select" name="msgType" id="msgTypeSelect">
                        <option selected value="11">Text</option>
                        <option value="12">Sound</option>
                        <option value="13">Image</option>
                    </select>
                </div>
            </div>
            <div class="form-group has-warning">
                <label class="control-label col-xs-1">UserId</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control" name="userId" id="userIdInput" value="${curUserId}" placeholder="userId"/>
                </div>
            </div>
            <div class="form-group has-warning">
                <label class="control-label col-xs-1">Content</label>
                <div class="textarea col-xs-10">
                    <textarea type="text" rows="8" class="form-control" name="content" id="contentInput"></textarea>
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-offset-0 col-xs-10">
                    <button type="submit" class="btn btn-success" id="sendBtn">Send</button>
                </div>
            </div>
        </form>
    </div>

    <!-- foot -->
    <%@include file="../../include/foot.html"%>
    <script type="application/javascript" src="../../js/JsonUtil.js"></script>
    <script type="application/javascript" src="../../js/sendPush.js"></script>

</body>
</html>

