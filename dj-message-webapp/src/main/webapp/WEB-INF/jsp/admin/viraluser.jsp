<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../include/jsp.html"%>

<html>
<head>
    <%@include file="../../include/base.html"%>
    <script src="../../js/bootstrap-datetimepicker.js"></script>
    <link rel="stylesheet" type="text/css" href="../../css/bootstrap-datetimepicker.css">
    <title>邮件推广</title>

</head>
<body>
    <!-- nav -->
    <%@include file="../../include/operationNav.jsp"%>

    <div class="container">

        <br>
        <br>
        <ul class="nav nav-tabs">
            <li id="nav-viraljob"><a href="/admin/viral/getjobs">推荐职位</a>
            </li>
            <li id="nav-viraluser"><a href="/admin/viral/getusers">推荐人才</a>
            </li>
        </ul>

        <form class="form-inline" id="SearchForm">

            <div class="row">
                <div class="form-group has-info col-md-3">
                    <label class="control-label"></label>
                    <div class="input-group date" data-link-field="timeInput" id="timePicker">
                        <input class="form-control" type="text" id="time" name="time" value="">
                        <span class="input-group-addon">
                            <span class="glyphicon glyphicon-remove"></span>
                        </span>
                        <span class="input-group-addon">
                            <span class="glyphicon glyphicon-th"></span>
                        </span>
                    </div>
                    <input type="hidden" id="timeInput" value="" />
                    <br/>
                </div>

                <div class="form-group col-md-1 ">
                    <label class="control-label">&nbsp;&nbsp;&nbsp;</label>
                    <button type="submit" class=" form-control btn btn-default" id="vuSearchBtn">查询</button>
                </div>
                 <div class="form-group col-md-1 ">
                    <label class="control-label">&nbsp;&nbsp;&nbsp;</label>
                    <button type="button" class=" form-control btn btn-success" id="vuSendBtn">生成url</button>
                </div>

                <div class="form-group col-md-1 ">
                    <label class="control-label">&nbsp;&nbsp;&nbsp;</label>
                    <button type="button" class=" form-control btn btn-default" id="copyBtn">复制</button>
                </div>

                <div class="form-group col-md-4">
                    <label class="control-label">&nbsp;&nbsp;&nbsp;</label>
                    <div>
                        <input type="text" class="form-control" size="84" id="urlP" />
                    </div>
                </div>
            </div>
        </form>

        <div class="table-responsive">
            <table class="table table-striped table-bordered table-hover table-condensed">
                <thead>
                    <tr>
                        <th>用户id</th>
                        <th>姓名</th>
                        <th>公司或学校</th>
                        <th>职位名称</th>
                        <th>个人亮点</th>
                        <th>选择</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td>${user.simpleUserView.userId}</td>
                        <td>${user.simpleUserView.name}</td>
                        <td>${user.simpleUserView.corpName}</td>
                        <td>${user.simpleUserView.positionName}</td>
                        <td>${user.userLabelList[0].content}  ${user.userLabelList[1].content}  ${user.userLabelList[2].content}</td>
                        <td>
                            <input type="checkbox" name="checkbox" value=${user.simpleUserView.userId}>
                        </td>

                    </tr>

                </c:forEach>
                </tbody>
            </table>
        </div>

    </div>

    <script src="../../js/JsonUtil.js"></script>
    <script src="../../js/viraluser.js"></script>

</body>

</html>