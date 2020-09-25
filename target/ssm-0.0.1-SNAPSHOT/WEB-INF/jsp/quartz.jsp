<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Tyutf_8pe" content="text/html; charset=utf-8">
<title>定时任务</title>
</head>
<script src="../js/jquery.min.js"></script>
<body>
	<div>
		<input type="button" value="定时任务开始" onclick="quartzStart()"/> 
		<input type="button" value="定时任务暂停" onclick="quartzStop()"/> 
		<input type="button" value="定时任务重启" onclick="quartzRestart()"/> 
		<input type="button" value="定时任务删除" onclick="quartzDelete()"/>
	</div>
</body>
</html>
<script type="text/javascript">
	//定时任务开启
	function quartzStart() {
		var obj = {
				"jobId" : "12",
				"jobName":"23",
				"jobGroup":"34",
				"jobStatus":"45",
				"jobStatus":"1",
				"cronExpression":"0/5 * * * * ?",
				"desc":"开启定时任务"
		};
		var jsonObj = JSON.stringify(obj);
		$.ajax({
			type : "POST",
			url : "http://localhost:8080/ssm/quartz/quartzStart.do",
			dataType : 'json',
			data : jsonObj,
			contentType : 'application/json;charset=UTF-8',
			success : function(data) {
				alert("定时任务开启");
			},
			error : function(jqXHR) {
				alert("发生错误：" + jqXHR.status);
			}
		});
	}
	
	//定时任务停止
	function quartzStop() {
		var obj = {
				"jobId" : "12",
				"jobName":"23",
				"jobGroup":"34",
				"jobStatus":"45",
				"jobStatus":"1",
				"cronExpression":"0 */1 * * * ?",
				"desc":"暂停定时任务"
		};
		var jsonObj = JSON.stringify(obj);
		$.ajax({
			type : "POST",
			url : "http://localhost:8080/ssm/quartz/quartzStop.do",
			dataType : 'json',
			data : jsonObj,
			contentType : 'application/json;charset=UTF-8',
			success : function(data) {
				alert("暂停定时任务成功");
			},
			error : function(jqXHR) {
				alert("发生错误：" + jqXHR.status);
			}
		});
	}
	
	//定时任务重启
	function quartzRestart() {
		var obj = {
				"jobId" : "12",
				"jobName":"23",
				"jobGroup":"34",
				"jobStatus":"45",
				"jobStatus":"1",
				"cronExpression":"0 */1 * * * ?",
				"desc":"重启定时任务"
		};
		var jsonObj = JSON.stringify(obj);
		$.ajax({
			type : "POST",
			url : "http://localhost:8080/ssm/quartz/quartzRestart.do",
			dataType : 'json',
			data : jsonObj,
			contentType : 'application/json;charset=UTF-8',
			success : function(data) {
				alert("重启定时任务成功");
			},
			error : function(jqXHR) {
				alert("发生错误：" + jqXHR.status);
			}
		});
	}
	
	//定时任务删除
	function quartzDelete() {
		var obj = {
				"jobId" : "12",
				"jobName":"23",
				"jobGroup":"34",
				"jobStatus":"45",
				"jobStatus":"1",
				"cronExpression":"0 */1 * * * ?",
				"desc":"删除定时任务"
		};
		var jsonObj = JSON.stringify(obj);
		$.ajax({
			type : "POST",
			url : "http://localhost:8080/ssm/quartz/quartzDelete.do",
			dataType : 'json',
			data : jsonObj,
			contentType : 'application/json;charset=UTF-8',
			success : function(data) {
				alert("删除定时任务成功");
			},
			error : function(jqXHR) {
				alert("发生错误：" + jqXHR.status);
			}
		});
	}
</script>