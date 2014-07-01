<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../include/jsp.html"%>

<html>
<head>
    <title>Service Test</title>
    <%@include file="../../include/base.html"%>
</head>

<body>
    <!-- nav -->
    <%@include file="../../include/nav.html"%>

    <div class="container">
        <div class="page-header">
            <h1>Service Test</h1>
        </div>
        <form class="form-horizontal" id="invokeForm">
            <div class="form-group has-warning">
                <label class="control-label col-xs-1">Service</label>
                <div class="col-xs-10">
                    <select class="from-control p-select" name="service" id="serviceSelect">
                        <c:forEach var="service" items="${services}">
                            <option>${service}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group has-warning">
                <label class="control-label col-xs-1">Method</label>
                <div class="col-xs-1">
                    <select class="from-control p-select" name="method" id="methodSelect">
                    </select>
                </div>
            </div>
            <div class="form-group has-warning">
                <label class="control-label col-xs-1">ServiceHost</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control" name="serviceHost" id="serviceHostInput" placeholder="currentHost:8556"/>
                </div>
            </div>
            <div class="form-group has-warning">
                <label class="control-label col-xs-1">UserId</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control" name="userId" id="userIdInput"/>
                </div>
            </div>
            <div class="form-group has-warning">
                <label class="control-label col-xs-1">input</label>
                <div class="textarea col-xs-10">
                    <textarea type="text" rows="6" class="form-control" name="input" id="inputTextarea" placeholder="input param"></textarea>
                </div>
            </div>

            <div class="form-group has-warning">
                <label class="control-label col-xs-1">output</label>
                <div class="textarea col-xs-10">
                    <textarea type="text" rows="4" class="form-control" id="outputTextarea" placeholder="output"></textarea>
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-offset-0 col-xs-10">
                    <button type="submit" class="btn btn-success" id="invokeBtn">Invoke</button>
                </div>
            </div>
        </form>
    </div>
    <!-- foot -->
    <%@include file="../../include/foot.html"%>
    <script type="text/javascript" src="../../js/JsonUtil.js"></script>
    <script type="text/javascript" src="../../js/serviceTest.js"></script>
</body>

</html>