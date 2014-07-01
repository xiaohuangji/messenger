<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../include/jsp.html"%>

<html>
<head>
    <%@include file="../../include/base.html"%>
    <script src="../../js/bootstrap-datetimepicker.js"></script>
    <link rel="stylesheet" type="text/css" href="../../css/bootstrap-datetimepicker.css">
    <title>消息推送</title>

</head>
<body>
    <!-- nav -->
    <%@include file="../../include/operationNav.jsp"%>

    <!-- Begin page content -->
    <div class="container">

        <div class="page-header">
            <h1>消息推送</h1>
        </div>

        <div class="table-responsive">
            <button type="button" class="btn btn-primary btn-sm" id="preparePushJobBtn">推送新消息</button>
            <br>
            <br>
            <table class="table table-striped table-bordered table-hover table-condensed">
                <thead>
                    <tr>
                        <th>id</th>
                        <th>内容</th>
                        <th>范围</th>
                        <th>时间</th>
                        <th>人数</th>
                        <th>发布者</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>

                <c:forEach var="pushJobRaw" items="${pushJobRaws}">
                    <tr>
                        <td>${pushJobRaw.id}</td>
                        <td>${pushJobRaw.content}</td>
                        <td>${pushJobRaw.filterDesc}</td>
                        <td>
                            <fmt:formatDate value="${pushJobRaw.triggerDate}" pattern="yyyy-MM-dd HH:mm"/>
                        </td>
                        <td>${pushJobRaw.userCount}</td>
                        <td>${pushJobRaw.operator}</td>
                        <c:if test="${pushJobRaw.status==4}">
                            <td>无效</td>
                        </c:if>
                        <c:if test="${pushJobRaw.status==3}">
                            <td>已完成</td>
                        </c:if>
                        <c:if test="${pushJobRaw.status==2}">
                            <td>已取消</td>
                        </c:if>
                        <c:if test="${pushJobRaw.status==1}">
                            <td>
                                <button type="button" class="btn btn-xs opPrepareCancelBtn">取消</button>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
        </div>
        <%@include file="../../include/page.html"%>
    </div>

    <div id="pushjobModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">推送任务</h4>
                </div>
                <div class="modal-body">
                    <div class="container">
                        <form class="form-horizontal" id="pushjobForm">

                            <div class="row">
                                <div class="form-group has-info col-md-6">
                                    <label class="control-label">内容*</label>
                                    <textarea type="text" rows="5" class="form-control" name="content"></textarea>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group has-info col-md-6">
                                    <label class="control-label">链接</label>
                                    <input type="text" class="form-control" name="link"></input>
                                </div>
                            </div>

                            <div class="row">
                                <div class="form-group has-info col-md-2">
                                    <label class="control-label">身份</label>
                                    <div>
                                        <select class="from-control input-sm" name="filterIsVerified">
                                            <option value="-1">请选择用户</option>
                                            <option value="1">认证用户</option>
                                            <option value="0">未认证用户</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group has-info col-md-2">
                                    <label class="control-label">性别</label>
                                    <div>
                                        <select class="from-control input-sm" name="filterGender">
                                            <option value="0">请选择性别</option>
                                            <option value="1">男</option>
                                            <option value="2">女</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group has-info col-md-2">
                                    <label class="control-label">定时*</label>
                                    <div class="input-group date"  id="triggerDatePicker">
                                        <input class="form-control input-sm" type="text" name="triggerDate"/>
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-th"></span>
                                        </span>
                                    </div>
                                </div>
                             </div>
                            <div class="row">
                                <div class="form-group has-info col-md-2">
                                    <label class="control-label">职位</label>
                                    <%--<input type="text" class="form-control input-sm" name="filterJobType"/>--%>
                                    <div>
                                        <select class="from-control input-sm" name="filterJobType">
                                            <option value="0">全部</option>
                                            <option value="1">会计/出纳</option>
                                            <option value="2">财务</option>
                                            <option value="3">审计</option>
                                            <option value="4">金融</option>
                                            <option value="5">技术研发</option>
                                            <option value="6">咨询/顾问</option>
                                            <option value="7">产品</option>
                                            <option value="8">运营</option>
                                            <option value="9">质量管理</option>
                                            <option value="10">项目管理</option>
                                            <option value="11">销售</option>
                                            <option value="12">商务拓展</option>
                                            <option value="13">渠道/分销</option>
                                            <option value="14">客户服务</option>
                                            <option value="15">市场营销</option>
                                            <option value="16">公关</option>
                                            <option value="18">生产/制造</option>
                                            <option value="19">编辑/文案</option>
                                            <option value="20">管理</option>
                                            <option value="21">人力资源</option>
                                            <option value="22">法律</option>
                                            <option value="23">行政</option>
                                            <option value="24">教师</option>
                                            <option value="25">培训</option>
                                            <option value="26">贸易/进出口</option>
                                            <option value="27">采购</option>
                                            <option value="28">物流/供应链</option>
                                            <option value="29">医疗/健康</option>
                                            <option value="30">艺术</option>
                                            <option value="31">设计/创意</option>
                                            <option value="32">工程</option>
                                            <option value="33">物业管理</option>
                                            <option value="34">科研</option>
                                            <option value="35">翻译</option>
                                            <option value="36">公务员</option>
                                            <option value="37">其他</option>
                                            <option value="38">技术支持</option>
                                        </select>
                                    </div>

                                </div>
                                <div class="form-group has-info col-md-offset-1 col-md-2">
                                    <label class="control-label">行业</label>
                                    <%--<input type="text" class="form-control input-sm" name="filterIndustry"/>--%>
                                    <div>
                                        <select class="from-control input-sm" name="filterIndustry">
                                            <optgroup label="全部">
                                                <option value="0">全部</option>
                                            </optgroup>

                                            <optgroup label="IT行业">
                                                <option value="13">计算机软件</option>
                                                <option value="14">计算机硬件</option>
                                                <option value="15">IT服务</option>
                                                <option value="16">互联网</option>
                                                <option value="17">电子商务</option>
                                                <option value="18">游戏</option>
                                                <option value="19">通信</option>
                                                <option value="20">电子/半导体</option>
                                            </optgroup>

                                            <optgroup label="金融行业">
                                                <option value="21">银行</option>
                                                <option value="22">保险</option>
                                                <option value="23">证券/基金/期货</option>
                                                <option value="24">投资</option>
                                            </optgroup>

                                            <optgroup label="专业服务">
                                                <option value="25">会计/审计</option>
                                                <option value="26">人力资源</option>
                                                <option value="27">管理咨询</option>
                                                <option value="28">法律</option>
                                                <option value="29">检测/认证</option>
                                                <option value="30">翻译</option>
                                            </optgroup>

                                            <optgroup label="教育培训行业">
                                                <option value="31">高等教育</option>
                                                <option value="32">初中等教育</option>
                                                <option value="33">培训</option>
                                            </optgroup>

                                            <optgroup label="消费品行业">
                                                <option value="34">日用品/化妆品</option>
                                                <option value="35">食品/饮料</option>
                                                <option value="36">服装/纺织</option>
                                                <option value="37">家电/数码产品</option>
                                                <option value="38">奢侈品/珠宝</option>
                                                <option value="39">家具/家居</option>
                                                <option value="40">酒品</option>
                                                <option value="41">烟草业</option>
                                                <option value="42">办公用品</option>
                                            </optgroup>
                                            <optgroup label="文化传媒行业">
                                                <option value="43">广告/公关/会展</option>
                                                <option value="44">报纸/杂志</option>
                                                <option value="45">广播</option>
                                                <option value="46">影视</option>
                                                <option value="47">出版</option>
                                                <option value="48">艺术/工艺</option>
                                                <option value="49">体育</option>
                                                <option value="50">动漫</option>
                                            </optgroup>

                                            <optgroup label="建筑/房地产行业">
                                                <option value="51">建筑设计/规划</option>
                                                <option value="52">土木工程</option>
                                                <option value="53">房地产</option>
                                                <option value="54">物业管理</option>
                                                <option value="55">建材</option>
                                                <option value="56">装修装潢</option>
                                            </optgroup>

                                            <optgroup label="贸易物流行业">
                                                <option value="57">进出口</option>
                                                <option value="58">批发/零售</option>
                                                <option value="59">商店/超市</option>
                                                <option value="60">物流/仓储</option>
                                                <option value="61">运输/铁路/航空</option>
                                            </optgroup>

                                            <optgroup label="制造工业">
                                                <option value="62">化工</option>
                                                <option value="63">原材料/加工</option>
                                                <option value="64">新材料</option>
                                                <option value="65">汽车</option>
                                                <option value="66">机械/自动化</option>
                                                <option value="67">军工/国防</option>
                                                <option value="68">采矿/金属</option>
                                                <option value="69">原油/能源</option>
                                                <option value="70">造纸</option>
                                                <option value="71">印刷/包装</option>
                                                <option value="72">航天/造船</option>
                                            </optgroup>

                                            <optgroup label="医疗/卫生">
                                                <option value="73">医疗/护理</option>
                                                <option value="74">医疗器械</option>
                                                <option value="75">生物技术</option>
                                                <option value="76">医药</option>
                                                <option value="77">动物医疗</option>
                                            </optgroup>

                                            <optgroup label="服务业">
                                                <option value="78">酒店</option>
                                                <option value="79">餐饮</option>
                                                <option value="80">旅游</option>
                                                <option value="81">休闲/娱乐/健身</option>
                                                <option value="82">私人/家政服务</option>
                                                <option value="83">图书馆/展览馆</option>
                                            </optgroup>

                                            <optgroup label="其他">
                                                <option value="85">环境</option>
                                                <option value="86">农/林/牧/渔</option>
                                                <option value="87">研究所/研究院</option>
                                                <option value="88">公共事业</option>
                                                <option value="89">非营利组织</option>
                                                <option value="90">政府部门</option>
                                            </optgroup>

                                        </select>
                                    </div>
                                </div>
                                <div class="form-group has-info col-md-offset-1 col-md-2">
                                    <label class="control-label">地区</label>
                                    <%--<input type="text" class="form-control input-sm" name="filterCity"/>--%>
                                    <div>
                                        <select class="from-control input-sm" name="filterCity">
                                            <option value="">全部</option>
                                            <option>北京市</option>
                                            <option>上海市</option>
                                            <option>天津市</option>
                                            <option>重庆市</option>
                                            <option>河北省</option>
                                            <option>山西省</option>
                                            <option>内蒙古自治区</option>
                                            <option>辽宁省</option>
                                            <option>吉林省</option>
                                            <option>黑龙江省</option>
                                            <option>江苏省</option>
                                            <option>浙江省</option>
                                            <option>安徽省</option>
                                            <option>福建省</option>
                                            <option>江西省</option>
                                            <option>山东省</option>
                                            <option>河南省</option>
                                            <option>湖北省</option>
                                            <option>湖南省</option>
                                            <option>广东省</option>
                                            <option>广西壮族自治区</option>
                                            <option>海南省</option>
                                            <option>四川省</option>
                                            <option>贵州省</option>
                                            <option>云南省</option>
                                            <option>西藏自治区</option>
                                            <option>陕西省</option>
                                            <option>甘肃省</option>
                                            <option>青海省</option>
                                            <option>宁夏回族自治区</option>
                                            <option>新疆维吾尔族自治区</option>                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">

                                <div class="form-group has-info col-md-2">
                                    <label class="control-label">App版本</label>
                                    <input type="text" class="form-control input-sm" name="filterClientVersion"/>
                                </div>
                                <div class="form-group has-info col-md-2">
                                    <label class="control-label">系统</label>
                                    <div>
                                        <select class="from-control input-sm" name="filterMobileOs">
                                            <option value="0">全部</option>
                                            <option value="1">iOS</option>
                                            <option value="2">Android</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group has-info col-md-2">
                                    <label class="control-label">操作者*</label>
                                    <input type="text" class="form-control input-sm" name="operator"/>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default btn-sm" data-dismiss="modal">取消</button>
                        <button type="button" class="btn btn-primary btn-sm" id="confirmPushJobBtn">发布</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <input type="hidden" id="idHidden" value="" />

    <div id="cancelModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title"></h4>
                </div>
                <div class="modal-body">
                    <p>确认取消这条推送吗？</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default btn-xs" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary btn-xs" id="cancelBtn">确认取消</button>
                </div>
            </div>
        </div>
    </div>

    <!-- foot -->
    <%@include file="../../include/foot.html"%>
    <script type="application/javascript" src="../../js/pushJob.js"></script>

</body>

</html>