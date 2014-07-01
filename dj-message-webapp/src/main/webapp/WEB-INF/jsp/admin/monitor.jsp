<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../include/jsp.html"%>

<html>
<head>
    <%@include file="../../include/base.html"%>
    <link rel="stylesheet" type="text/css" href="../../css/monitor.css">
    <title>Broker Monitor</title>

</head>

<body>
    <!-- nav -->
    <%@include file="../../include/nav.html"%>

    <div class="container">
        <div class="page-header">
            <h1>Broker Monitor</h1>
        </div>

        <div class="table-responsive">
            <table class="table table-striped table-bordered table-hover table-condensed p-monitor-table">
                <thead>
                    <tr>
                        <th rowspan="2" id="serverNameTh">ServerName</th>
                        <th colspan="3" id="connTh">conn</th>
                        <th colspan="3" id="upStreamTh">upStream</th>
                        <th colspan="2" id="downStreamTh">downStream</th>
                        <th rowspan="2" id="updateTimeTh">updateTime</th>
                    </tr>
                    <tr>
                        <th>iOS</th>
                        <th>Android</th>
                        <th>Server</th>
                        <th>iOS</th>
                        <th>Android</th>
                        <th>Server</th>
                        <th>iOS</th>
                        <th>Android</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="info" items="${infos}">
                    <tr>
                        <td>${info.serverName}</td>
                        <td>${info.iosConn}</td>
                        <td>${info.androidConn}</td>
                        <td>${info.serverConn}</td>
                        <td>${info.fromIos}</td>
                        <td>${info.fromAndroid}</td>
                        <td>${info.fromServer}</td>
                        <td>${info.toIos}</td>
                        <td>${info.toAndroid}</td>
                        <td>
                            <fmt:formatDate value="${info.updateTime}" pattern="yyyy-MM-dd hh:mm"/>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <!-- foot -->
    <%@include file="../../include/foot.html"%>
    <script type="application/javascript" src="../../js/monitor.js"></script>
</body>

</html>