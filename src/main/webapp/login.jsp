<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" +
            request.getServerName() + ":" +
            request.getServerPort()+
            request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script>
        $(function () {

            if (window.top!=window){
                window.top.location=window.location;
            }

            /*让账号框在每次页面加载时都重置*/
            $("#loginAct").val("");
            /*让账号框在每次页面加载后都被选中*/
            $("#loginAct").focus();
            /*让用户敲下回车键也能登录*/
            $(window).keydown(function (event) {
                /*判断敲下的键是否为回车键*/
                if (event.keyCode == 13) {
                    login();
                }
            });
            /*给按钮绑定登录事件*/
            $("#submitBtn").click(function (){
            	login();
			});
        });
        /*登录事件,一定要写在$function的外面*/
        function login() {
        	/*获取账号密码并去除空格*/
            var loginPwd = $.trim($("#loginPwd").val());
            var loginAct = $.trim($("#loginAct").val());
            /*判断账号密码是否为空*/
            if (loginAct == "" || loginPwd == "") {
            	/*为空就输出错误信息,并结束请求*/
                $("#msg").html("账号或者密码不能为空");
                return false;
            }

            $.ajax({
				url:"settings/user/login.do",
				data:{
                    loginAct:loginAct,
                    loginPwd:loginPwd
                },
				type:"post",
				dataType:"json",
				success:function (data){
				    if (data.success){
				        window.location.href="workbench/index.jsp";
                    }else {
				        $("#msg").html(data.msg);
                    }
				}
			});
        }
    </script>
</head>
<body>
<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
    <img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
    <div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">
        CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
    <div style="position: absolute; top: 0px; right: 60px;">
        <div class="page-header">
            <h1>登录</h1>
        </div>
        <form action="workbench/index.jsp" class="form-horizontal" role="form">
            <div class="form-group form-group-lg">
                <div style="width: 350px;">
                    <input class="form-control" type="text" placeholder="用户名" id="loginAct">
                </div>
                <div style="width: 350px; position: relative;top: 20px;">
                    <input class="form-control" type="password" placeholder="密码" id="loginPwd">
                </div>
                <div class="checkbox" style="position: relative;top: 30px; left: 10px;">
                    <span id="msg" style="color: red"></span>
                </div>
                <%--按钮写在form表单中的默认行为就是提交表单--%>
                <button id="submitBtn" type="button" class="btn btn-primary btn-lg btn-block"
                        style="width: 350px; position: relative;top: 45px;">登录
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>