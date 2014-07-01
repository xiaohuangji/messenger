<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../include/jsp.html"%>

<html>
<head>
    <%@include file="../../include/base.html"%>
    <script src="../../js/bootstrap-datetimepicker.js"></script>
    <link rel="stylesheet" type="text/css" href="../../css/bootstrap-datetimepicker.css">
    <title>职位举报</title>
</head>

<body>
    <!-- nav -->
    <%@include file="../../include/operationNav.jsp"%>

    <br>
    <div class="container">
        <form class="form-inline" id="jrSearchForm">

            <div class="form-group has-info col-md-2">
                <label class="control-label">用户id</label>
                <div>
                    <input type="text" class="form-control input-sm" name="userId" value="${userId}"/>
                </div>
            </div>
            <div class="form-group has-info col-md-2">
                <label class="control-label">职位名</label>
                <div>
                    <input type="text" class="form-control input-sm" name="jobName" value="${jobName}" />
                </div>
            </div>

            <div class="form-group has-info col-md-3">
                <label class="control-label">提交时间</label>
                <div class="input-group date" data-date="1979-09-16T05:25:07Z"  data-link-field="startTimeInput" id="startTimePicker">
                    <input class="form-control" type="text" name="startTime" value="${startTime}">
                    <%--<span class="input-group-addon">--%>
                        <%--<span class="glyphicon glyphicon-remove"></span>--%>
                    <%--</span>--%>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-th"></span>
                    </span>
                </div>
                <input type="hidden" id="startTimeInput" value="" />
                <br/>
            </div>

            <div class="form-group has-info col-md-3">
                <label class="control-label">结束时间</label>
                <div class="input-group date" data-date="1979-09-16T05:25:07Z"  data-link-field="endTimeInput" id="endTimePicker">
                    <input class="form-control" type="text" name="endTime" value="${endTime}">
                    <%--<span class="input-group-addon">--%>
                        <%--<span class="glyphicon glyphicon-remove"></span>--%>
                    <%--</span>--%>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-th"></span>
                    </span>
                </div>
                <input type="hidden" id="endTimeInput" value="" />
                <br/>
            </div>
            <div class="form-group col-md-1">
                <label class="control-label">&nbsp;&nbsp;&nbsp;</label>
                <a type="submit" class="btn btn-default" id="jrSearchBtn">查询</a>
            </div>
            <div class="form-group col-md-1">
                <label class="control-label">&nbsp;&nbsp;&nbsp;</label>
                <a class="btn btn-default" id="jrClearBtn">清空</a>
            </div>
        </form>

        <div class="table-responsive">
            <table class="table table-striped table-bordered table-hover table-condensed">
                <thead>
                    <tr>
                        <th>id</th>
                        <th>用户id</th>
                        <th>提交时间</th>
                        <th>职位id</th>
                        <th>职位名称</th>
                        <th>举报原因</th>
                        <th>详情</th>
                        <th>状态</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="job" items="${jobs}">
                    <tr>
                        <td>${job.id}</td>
                        <td>${job.informUserId}</td>
                        <td style="width:130px">
                            <fmt:formatDate value="${job.createTime}" pattern="yyyy-MM-dd HH:mm"/>
                        </td>
                        <td>${job.jobId}</td>
                        <td>${job.positionName}</td>
                        <td style="width:550px">${job.description}</td>

                        <td><button type="button" class="btn btn-xs btn-success opGetDetailBtn">查看</button></td>

                        <c:if test="${job.status==1}">
                            <td>已下线</td>
                        </c:if>
                        <c:if test="${job.status==2}">
                            <td>已忽略</td>
                        </c:if>
                        <c:if test="${job.status==0}">
                            <td>
                                <button type="button" class="btn btn-xs btn-success opDeleteBtn">下线</button>
                                <button type="button" class="btn btn-xs opIgnoreBtn">忽略</button>
                            </td>
                        </c:if>

                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <%@include file="../../include/page.html"%>
    </div>

    <input type="hidden" id="jobIdHidden" value="" />

    <div id="jobDetailModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title"></h4>
                </div>
                <div class="modal-body">
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group has-info">
                                <label class="control-label col-xs-2">发布者Id</label>
                                <p class="form-control-static" id="userIdP"></p>
                            </div>
                            <div class="form-group has-info">
                                <label class="control-label col-xs-2">职位名称</label>
                                <p class="form-control-static" id="jobNameP"></p>
                            </div>
                            <div class="form-group has-info">
                                <label class="control-label col-xs-2">职位类别</label>
                                <p class="form-control-static" id="jobTypeP"></p>
                            </div>
                            <div class="form-group has-info">
                                <label class="control-label col-xs-2">公司名称</label>
                                <p class="form-control-static" id="corpNameP"></p>
                            </div>
                            <%--<div class="form-group has-warning">--%>
                                <%--<label class="control-label col-xs-2">行业</label>--%>
                                <%--<p class="form-control-static" id="industryP"></p>--%>
                            <%--</div>--%>
                            <div class="form-group has-info">
                                <label class="control-label col-xs-2">薪资范围</label>
                                <p class="form-control-static" id="salaryP"></p>
                            </div>
                            <%--<div class="form-group has-info">--%>
                                <%--<label class="control-label col-xs-2">学历</label>--%>
                                <%--<p class="form-control-static" id="educationP"></p>--%>
                            <%--</div>--%>
                            <div class="form-group has-info">
                                <label class="control-label col-xs-2">亮点</label>
                                <p class="form-control-static" id="descriptionsP"></p>
                            </div>
                            <div class="form-group has-info">
                                <label class="control-label col-xs-2">地点</label>
                                <p class="form-control-static" id="poisP"></p>
                            </div>
                            <div class="form-group has-info">
                                <label class="control-label col-xs-2">有效期</label>
                                <p class="form-control-static" id="endTimeP"></p>
                            </div>
                        </form>
                    </div>
                </div>

                <%--<div class="modal-footer">--%>
                    <%--<button type="button" class="btn btn-default btn-xs deleteBtn">下线</button>--%>
                    <%--<button type="button" class="btn btn-primary btn-xs ignoreBtn">忽略</button>--%>
                <%--</div>--%>
            </div>
        </div>
    </div>

    <!-- foot -->
    <%@include file="../../include/foot.html"%>
    <script  src="../../js/jobReport.js"></script>

</body>

</html>