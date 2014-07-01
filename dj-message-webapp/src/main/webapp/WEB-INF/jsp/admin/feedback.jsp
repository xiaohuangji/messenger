<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../include/jsp.html"%>

<html>
<head>
    <%@include file="../../include/base.html"%>
    <script src="../../js/bootstrap-datetimepicker.js"></script>
    <link rel="stylesheet" type="text/css" href="../../css/bootstrap-datetimepicker.css">
    <title>用户反馈</title>
</head>

<body>
    <!-- nav -->
    <%@include file="../../include/operationNav.jsp"%>

    <br>
    <div class="container">
        <form class="form-inline" id="fbSearchForm">

            <div class="form-group has-info">
                <label class="control-label">用户id</label>
                <div>
                    <input type="text" class="form-control input-sm" name="userId" value="${feedbackOption.userId}"/>
                </div>
            </div>
            <div class="form-group has-info">
                <label class="control-label">手机号</label>
                <div>
                    <input type="text" class="form-control input-sm" name="mobile" value="${feedbackOption.mobile}" />
                </div>
            </div>


            <div class="form-group has-info">
                <label class="control-label">状态</label>
                <div>
                    <select class="from-control input-sm" name="status" value="${feedbackOption.status}">
                        <option value="0">不限</option>

                        <c:if test="${feedbackOption.status==1}">
                            <option value="1" selected>未处理</option>
                        </c:if>
                        <c:if test="${feedbackOption.status!=1}">
                            <option value="1" >未处理</option>
                        </c:if>
                        <c:if test="${feedbackOption.status==2}">
                            <option value="2" selected>已解决</option>
                        </c:if>
                        <c:if test="${feedbackOption.status!=2}">
                            <option value="2" >已解决</option>
                        </c:if>
                    </select>
                </div>
            </div>
            <div class="form-group has-info">
                <label class="control-label">平台</label>
                <div>
                    <select class="from-control input-sm" name="system">

                        <option value="0">不限</option>
                        <c:if test="${feedbackOption.system==1}">
                            <option value="1" selected>iOS</option>
                        </c:if>
                        <c:if test="${feedbackOption.system!=1}">
                            <option value="1" >iOS</option>
                        </c:if>
                        <c:if test="${feedbackOption.system==2}">
                            <option value="2" selected>Android</option>
                        </c:if>
                        <c:if test="${feedbackOption.system!=2}">
                            <option value="2" >Android</option>
                        </c:if>
                    </select>
                </div>
            </div>
            <div class="form-group has-info">
                <label class="control-label">渠道</label>
                <div>
                    <input type="text" class="form-control input-sm" name="channel" value="${feedbackOption.channel}"/>
                </div>
            </div>
            <div class="form-group has-info">
                <label class="control-label">软件版本</label>
                <div>
                    <input type="text" class="form-control input-sm" name="clientVersion" value="${feedbackOption.clientVersion}" />
                </div>
            </div>
            <div class="form-group has-info">
                <label class="control-label">手机品牌</label>
                <div>
                    <input type="text" class="form-control input-sm" name="mobileBrand" value="${feedbackOption.mobileBrand}"/>
                </div>
            </div>

            <div class="row">
                <div class="form-group has-info col-md-3">
                    <label class="control-label">提交时间</label>
                    <div class="input-group date" data-date="1979-09-16T05:25:07Z"  data-link-field="startTimeInput" id="startTimePicker">
                        <input class="form-control" type="text" name="startTime" value="${feedbackOption.startTime}">
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
                        <input class="form-control" type="text" name="endTime" value="${feedbackOption.endTime}">
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
                    <a type="submit" class="btn btn-default" id="fbSearchBtn">查询</a>
                </div>
                <div class="form-group col-md-1">
                    <label class="control-label">&nbsp;&nbsp;&nbsp;</label>
                    <a class="btn btn-default" id="fbClearBtn">清空</a>
                </div>
                <div class="form-group col-md-1">
                    <label class="control-label">&nbsp;&nbsp;&nbsp;</label>
                    <a class="btn btn-default" id="fbRefreshBtn">刷新</a>
                </div>
            </div>
        </form>

        <div class="table-responsive">
            <table class="table table-striped table-bordered table-hover table-condensed">
                <thead>
                    <tr>
                        <th>id</th>
                        <th>用户id</th>
                        <th>手机号</th>
                        <th>时间</th>
                        <th>内容</th>
                        <th>回复</th>
                        <th>操作系统</th>
                        <th>系统版本</th>
                        <th>手机品牌</th>
                        <th>手机型号</th>
                        <th>软件版本</th>
                        <th>分辨率</th>
                        <th>渠道</th>
                        <th>状态</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="feedback" items="${feedbacks}">
                    <tr>
                        <td>${feedback.id}</td>
                        <td>${feedback.userId}</td>
                        <td>${feedback.mobile}</td>
                        <td style="width:130px">
                            <fmt:formatDate value="${feedback.createTime}" pattern="yyyy-MM-dd HH:mm"/>
                        </td>
                        <td>${feedback.content}</td>
                        <td>${feedback.reply}</td>
                        <c:if test="${feedback.system==1}">
                            <td>iOS</td>
                        </c:if>
                        <c:if test="${feedback.system==2}">
                            <td>Android</td>
                        </c:if>
                        <c:if test="${feedback.system!=2&&feedback.system!=1}">
                            <td>未知</td>
                        </c:if>
                        <td>${feedback.systemVersion}</td>
                        <td>${feedback.mobileBrand}</td>
                        <td>${feedback.mobileModel}</td>
                        <td>${feedback.clientVersion}</td>
                        <td>${feedback.mobileResolution}</td>
                        <td>${feedback.channel}</td>

                        <c:if test="${feedback.status==2}">
                            <td>已解决</td>
                        </c:if>
                        <c:if test="${feedback.status!=2}">
                            <td>
                                <button type="button" class="btn btn-xs opPrepareDeleteBtn">删除</button>
                                <button type="button" class="btn btn-xs btn-success opHandleBtn">解决</button>
                                <button type="button" class="btn btn-xs btn-success opPrepareReplyBtn">回复</button>
                            </td>
                        </c:if>

                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <%@include file="../../include/page.html"%>
    </div>

    <div id="replyModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">回复</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="removeForm">
                        <%--<div class="form-group has-info">--%>
                            <%--<label class="control-label col-xs-2">状态标记</label>--%>
                            <%--<button type="button" class="btn btn-xs " id="needFeedbackBtn">需反馈</button>--%>
                            <%--<button type="button" class="btn btn-xs " id="haveHandledBtn">已解决</button>--%>
                        <%--</div>--%>
                        <div class="form-group has-warning">
                            <label class="control-label">内容</label>
                            <textarea type="text" rows="4" class="form-control" id="replyInput"></textarea>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default btn-xs" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary btn-xs" id="sendReplyBtn">回复</button>
                </div>
            </div>
        </div>
    </div>

    <input type="hidden" id="userIdHidden" value="" />
    <input type="hidden" id="idHidden" value="" />

    <div id="deleteModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title"></h4>
                </div>
                <div class="modal-body">
                    <p>确认删除这条内容吗？</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default btn-xs" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary btn-xs" id="deleteBtn">删除</button>
                </div>
            </div>
        </div>
    </div>

    <!-- foot -->
    <%@include file="../../include/foot.html"%>
    <script  src="../../js/feedback.js"></script>

</body>

</html>