<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Tyutf_8pe" content="text/html; charset=utf-8">
<title>登陆页面</title>
</head>
<script src="../js/jquery.min.js"></script>
<body>
	<!-- 后台使用原生String进行接收 -->
	<form action="login1.do" method="post">
		<div>
			账号：<input type="text" name="userName" /> 密码：<input type="password"
				name="password"> <input type="submit" value="登陆1">
		</div>
		<!-- 后台使用String接收json字符串并返回json -->
		<div>
			账号：<input type="text" id="userName2" /> 密码：<input type="password"
				id="password2"> <input type="button" value="登陆2"
				onclick="login2()">
		</div>
		<!-- 后台使用JavaBean接收json字符串并返回json -->
		<div>
			账号：<input type="text" id="userName3" /> 密码：<input type="password"
				id="password3"> <input type="button" value="登陆3"
				onclick="login3()">
		</div>
		<!-- 用户修改密码，用于测试事务控制 -->
		<div>
			账号：<input type="text" id="userName4" /> 密码：<input type="password"
				id="password4"> <input type="button" value="修改"
				onclick="login4()">
		</div>
		</br> </br>
		<div>
			下载地址：<input type="text" id="downloadUrl" /> 存放路径：<input type="text"
				id="filePath" /> 文件名称：<input type="text" id="fileName" /> 线程个数：<input
				type="text" id="threadNum" /> <input type="button" value="多线程下载"
				onclick="web()">
		</div>
		</br> </br>
		<div>
			<input type="button" value="开始" onclick="web1()"> <input
				type="button" value="暂停" onclick="web2()"> <input
				type="button" value="重启" onclick="web3()">
		</div>
		</br> </br>
	</form>
	</br>
	</br>
	<!-- form表单上传附件 -->
	<form action="fileupload.do" method="post"
		enctype="multipart/form-data">
		<div>
			上传文件1：<input type="file" name="file1"> 上传文件2：<input
				type="file" name="file2"> <input type="submit" value="提交">
		</div>
	</form>
	</br>
	</br>

	<!-- 导出excel -->
	<form action="exportExcel.do" method="post">
		<div>
			<input type="button" value="分页导出excel" onclick="exportExcel1()">
		</div>
		</br> </br>
		<div>
			<input type="button" value="全部导出excel" onclick="exportExcel2()">
		</div>
	</form>
</body>
</html>
<script type="text/javascript">
	//测试后台使用String接收json串
	function login2() {
		var userName = $("#userName2").val();
		var password = $("#password2").val();
		var obj = {
			"userName" : userName,
			"password" : password
		};
		var jsonObj = JSON.stringify(obj);
		console.log("入参JSON串：" + jsonObj);
		$.ajax({
			type : "POST",
			url : "http://localhost:8080/ssm/user/login2.do",
			//url : "http://192.168.10.12:8081/ssm/user/login2.do",
			data : jsonObj,
			dataType : 'json',
			contentType : 'application/json;charset=UTF-8',
			success : function(data) {
				alert("调用成功");
				console.log(data);
			},
			error : function(jqXHR) {
				alert("发生错误：" + jqXHR.status);
			}
		});
	}
	//测试后台使用自定义JavaBean接收
	function login3() {
		var userName = $("#userName3").val();
		var password = $("#password3").val();

		var obj = {
			"reqHead" : {},
			"reqBody" : {
				"userName" : userName,
				"password" : password
			}
		};
		var jsonObj = JSON.stringify(obj);
		console.log("入参JSON串：" + jsonObj);
		$.ajax({
			type : "POST",
			url : "http://localhost:8080/ssm/user/login3.do",
			//url : "http://192.168.10.12:8081/ssm/user/login3.do",
			data : jsonObj,
			dataType : 'json',
			contentType : 'application/json;charset=UTF-8',
			success : function(data) {
				alert("调用成功");
				console.log(data);
			},
			error : function(jqXHR) {
				alert("发生错误：" + jqXHR.status);
			}
		});
	}
	//测试ssm的事务控制
	function login4() {
		var userName = $("#userName4").val();
		var password = $("#password4").val();

		var obj = {
			"reqHead" : {},
			"reqBody" : {
				"userName" : userName,
				"password" : password
			}
		};
		var jsonObj = JSON.stringify(obj);
		console.log("入参JSON串：" + jsonObj);
		$.ajax({
			type : "POST",
			url : "http://localhost:8080/ssm/user/login4.do",
			//url : "http://192.168.10.12:8081/ssm/user/login4.do",
			data : jsonObj,
			dataType : 'json',
			contentType : 'application/json;charset=UTF-8',
			success : function(data) {
				alert(data.rspHead.SUCCESS_MESSAGE);
			},
			error : function(jqXHR) {
				alert("发生错误：" + jqXHR.status);
			}
		});
	}

	//测试后台使用自定义JavaBean接收
	function web() {
		var downloadUrl = $("#downloadUrl").val();
		var filePath = $("#filePath").val();
		var fileName = $("#fileName").val();
		var threadNum = $("#threadNum").val();
		var obj = {
			"downloadUrl" : downloadUrl,
			"filePath" : filePath,
			"fileName" : fileName,
			"threadNum" : threadNum
		}
		var jsonObj = JSON.stringify(obj);
		console.log("入参JSON串：" + jsonObj);
		$.ajax({
			type : "POST",
			url : "http://localhost:8080/ssm/web/web5.do",
			//url : "http://192.168.10.12:8081/ssm/web/web5.do",
			data : jsonObj,
			dataType : 'json',
			contentType : 'application/json;charset=UTF-8',
			success : function(data) {
				alert("调用成功");
				console.log(data);
			},
			error : function(jqXHR) {
				alert("发生错误：" + jqXHR.status);
			}
		});
	}

	//断点续传--开始
	//生成uuid
	var uuid = "1";
	function web1() {
		var obj = {
			"reqHead" : {},
			"reqBody" : {
				"uuid" : uuid
			}
		};
		var jsonObj = JSON.stringify(obj);
		$.ajax({
			type : "POST",
			url : "http://localhost:8080/ssm/user/pointStart.do",
			dataType : 'json',
			data : jsonObj,
			contentType : 'application/json;charset=UTF-8',
			success : function(data) {
				alert("文件下载结束！");
				console.log(data);
			},
			error : function(jqXHR) {
				alert("发生错误：" + jqXHR.status);
			}
		});
	}

	//断点续传--暂停
	function web2() {
		var obj = {
			"reqHead" : {},
			"reqBody" : {
				"uuid" : uuid
			}
		};
		var jsonObj = JSON.stringify(obj);
		$.ajax({
			type : "POST",
			url : "http://localhost:8080/ssm/user/pointStop.do",
			dataType : 'json',
			data : jsonObj,
			contentType : 'application/json;charset=UTF-8',
			success : function(data) {
				alert("文件下载暂停！");
				console.log(data);
			},
			error : function(jqXHR) {
				alert("发生错误：" + jqXHR.status);
			}
		});
	}

	//断点续传--重启
	function web3() {
		var obj = {
			"reqHead" : {},
			"reqBody" : {
				"uuid" : uuid
			}
		};
		var jsonObj = JSON.stringify(obj);
		$.ajax({
			type : "POST",
			url : "http://localhost:8080/ssm/user/pointRestart.do",
			dataType : 'json',
			data : jsonObj,
			contentType : 'application/json;charset=UTF-8',
			success : function(data) {
				alert("文件重启下载结束！");
				console.log(data);
			},
			error : function(jqXHR) {
				alert("发生错误：" + jqXHR.status);
			}
		});
	}

	//ajax附件上传
	function fileUpload() {
		var formData = new FormData(document.getElementById("upload-form"));
		$.ajax({
			url : "http://localhost:8080/ssm/user/fileupload1.do",
			//url : "http://192.168.10.12:8081/ssm/user/fileupload1.do",
			method : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			success : function(resp) {
				alert("上传成功");//成功提示
			}
		});
	}

	//导出excel,注意Ajax是不能做导出的，以为ajax只能解析文本，无法解析流
	function exportExcel1() {
		window.location.href = "http://localhost:8080/ssm/user/exportExcel.do?currentPage=1&pageNum=10";
		//window.location.href = "http://192.168.10.12:8081/ssm/user/exportExcel.do?currentPage=1&pageNum=10";
	}

	//全部导出，多个excel文件打成压缩包
	function exportExcel2() {
		window.location.href = "http://localhost:8080/ssm/user/exportExcel2.do";
		//window.location.href = "http://192.168.10.12:8081/ssm/user/exportExcel2.do";
	}
</script>