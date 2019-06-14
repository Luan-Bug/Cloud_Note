
$(function(){
	
	//console.log('Hello World!');
	//定义单击事件
	$("#login").click(loginAction);
	//定义失去焦点事件
	$('#count').blur(checkName);
	$('#password').blur(checkPassword);
	//注册功能
	$('#regist_button').click(registAction);
	$('#regist_username').blur(checkRegistName);
	$('#regist_password').blur(checkRegistPassword);
	$('#final_password').blur(checkRegistConfirm);
});

//检查密码格式
function checkRegistPassword(){
	var password = $('#regist_password').val().trim();
	var rule = /^\w{4,10}$/;
	if(rule.test(password)){
		$('#regist_password').next().hide();
		return true;
	}
	$('#regist_password').next().show()
	  .find('span').html('密码太短啦');
	return false;
}
//检查确认密码
function checkRegistConfirm(){
	var password = $('#final_password').val().trim();
	var confirm = $('#regist_password').val().trim();
	if(password && password == confirm){
		$('#final_password').next().hide();
		return true;
	}
	$('#final_password').next().show()
		.find('span').html('确认密码不一致');
	return false;
}
//检查用户名称格式
function checkRegistName(){
	var name = $('#regist_username').val().trim();
	var rule = /^\w{4,10}$/;
	if(rule.test(name)){
		$('#regist_username').next().hide();
		return true;
	}
	$('#regist_username').next().show()
	  .find('span').html('4~10字符');
	return false;
}

//注册事件
function registAction(){
	console.log('registAction');
	//检验界面参数
	var password = $('#regist_password').val().trim();
	var name = $('#regist_username').val().trim();
	var confirm = $('#regist_password').val().trim();
	var nick = $('#nickname').val().trim();
	var n = checkRegistName() *
			checkRegistPassword() *
			checkRegistConfirm();
	if(n!=1){
		console.log('error');
		return ;
	}
	/*var saccess = checkRegistName()*checkRegistConfirm()*checkRegistPassword();
	if(saccess = 0){
		console.log('error');
		alert("请修改注册内容");
		return;
	}*/
	 
	//发起ajax请求
	var url = 'user/add.do';
	var data = {name:name,
			password:password,
			nick:nick,
			confirm:confirm
			};
	$.post(url,data,function(result){
			console.log(result);
			if(result.state==0){
				//退回登录界面
				$('#back').click();
				var name = result.data.name;
				$('#count').val(name);
				//触发获得焦点事件
				$('#password').focus();
				$('#regist_username').val('');
				$('#regist_password').val('');
				$('#final_password').val('');
				$('#nickname').val('');
			}else if(result.state==3){
				$('#regist_password').next().show()
				  .find('span').html(result.message);
			}else if(result.state==4){
				$('#regist_username').next().show()
				  .find('span').html(result.message);
			}else{
				alert(result.message);
			}
		});
	//得到响应以后，更新页面
}


function checkName(){
	var name = $('#count').val();
	var rule = /^\w{4,10}$/;
	if(! rule.test(name)){
		$('#count').next().html("账号格式不正确");
		return false;
	}
	$('#count').next().empty();
	return true;
	
}


function checkPassword(){
	var password = $('#password').val();
	var rule = /^\w{4,10}$/;
	if(! rule.test(password)){
		$('#password').next().html("密码格式不正确");
		return false;
	}
	$('#password').next().empty();
	return true;
	
}
function loginAction(){
	//获取用户输入的用户名密码
	var name = $('#count').val();
	var password = $('#password').val();
	var n=checkName()+checkPassword();
	if(n!=2){
		return;
	}
	//设置发送的数据
	console.log(name);
	console.log(password);
	var data = {'name':name,
				'password':password
				};
	$.ajax({
		url:'user/login.do',
		data: data,
		type:'post',
		dataType:'json',
		success:function(result){
			console.log(result);
			if(result.state==0){
				//登录成功!
				var user = result.data;
				console.log(user);
				//将userid保存到cookie
				setCookie("userId",user.id);
				var userid = getCookie('userId');
				//跳转到 edit.html
				location.href='edit.html';
				console.log(userid);
			}else{
				var msg = result.message;
				if(result.state==2){
					$('#count').next().html(msg);
				}else if(result.state==3){
					$('#password').next().html(msg);
				}else{
					alert(msg);
				}
			}
		},
		error: function(e){
			alert("通信失败!");
		}
	});
}