
var SUCCESS = 0;
var ERROR = 1;

$(function(){
	
	
	var userId = getCookie('userId');
	
	//网页加载以后，立即读取笔记本列表
	//loadNotebooks();
	//将初始页号page绑定到document上
	$(document).data('page',0);
	loadPagedNotebooks();
	//绑定笔记本单击事件显示笔记列表
	$('#notebook-list').on('click','.notebook',loadNotes);
	
	//绑定笔记单击事件显示笔记内容
	$('#note_list').on('click','li',loadNote);
	 
	//绑定添加笔记事件
	$('#note_list').on('click', '#add_note', showAddNoteDialog);
	$('#can').on('click','.create-note',addNote);
	$('#can').on('click','.close,.cancel',closeDialog)
	
	//绑定添加笔记本事件
	$('#notebook-list').on('click','#add_notebook',showAddNotebookDialog);
	$('#can').on('click','.create-notebook',addNotebook);
	$('#can').on('click','.close,.cancel',closeDialog);
	
	//绑定保存笔记事件
	$('#save_note').on('click',updatenote);
	
	//绑定笔记子菜单的事件
	$('#note_list').on('click','.btn_note_menu',showNoteMenu);
	$(document).click(hideNoteMenu);
	
	//绑定移动菜单事件
	$('#note_list').on('click','.btn_move',showMoveNoteDialog);
	//监听移动笔记对话框中的确定按钮
	$('#can').on('click', '.move-note', moveNote);
	
	//把笔记放到回收站里
	$('#note_list').on('click','.btn_delete',showDeleteNoteDialog);
	
	//监听删除笔记对话框中的确定按钮
	$('#can').on('click', '.delete-note', deleteNote);
	
	//监听回收站按钮被点击
	$('#trash_button').click(showTrashBin);
	
	//记载更多按钮被点击
	$('#notebook-list').on('click','.more',loadPagedNotebooks);
	
	//清除笔记事件
	$('#trash-bin').on(
		    'click', '.btn_delete', showDeleterollbackNoteDialog);
	//监听清除笔记对话框中的确定按钮
	$('#can').on('click', '.deleterollback-note', deleterollbackNote);
	
	//撤销删除事件
	$('#trash-bin').on(
		    'click', '.btn_replay', showReplayDialog);
	$('#can').on('click', '.btn-replay', replayNote);
	
	startHeartbeat();
});

function loadPagedNotebooks(){
	console.log('more');
	var page = $(document).data('page');
	var userId = getCookie('userId');
	var url = 'notebook/page.do';
	var data = {
			userId:userId,
			page:page
	};
	$.getJSON(url,data,function(result){
		console.log(result);
		if(result.state==SUCCESS){
			var notebooks = result.data;
			showPagedNotebooks(notebooks,page);
			$(document).data('page',page+1);
		}
	});
}

function showPagedNotebooks(notebooks,page){
	var ul = $('#notebook-list ul');
	console.log(ul);
	if(page==0){
		ul.empty();
	}else {
		//删除下一页按钮
		ul.find('.more').remove();
	}
	
	for (var i = 0; i < notebooks.length; i++) {
		var notebook = notebooks[i];
		var li = notebookTemplate.replace('[name]',notebook.name);
		console.log(li);
		var li = $(li);
		//将notebook.id绑定到li上
		li.data('notebookId',notebook.id);
		ul.append(li);
	}
	if(notebooks.length != 0){
		ul.append(moreTemplate);
	}
	
}
var moreTemplate = '<li class="online more"> '+
					'<a>'+
					'<i class="fa fa-plus" title="online" rel="tooltip-bottom"> '+
					'</i>加载更多...</a></li>';

/**
 * 心跳检查
 * */
function startHeartbeat(){
    var url = "user/heartbeat.do";
    setInterval(function(){
        $.getJSON(url, function(result){
            //console.log(result.data);
        });
    }, 5000);
}

/** 显示恢复笔记对话框 */
function showReplayDialog(){
    var li = $(this).parent().parent();
    var id = li.data('noteId');

    $(document).data('replayItem', li);

    if(id){
        $('#can').load('alert/alert_replay.html', loadReplayOptions);
        $('.opacity_bg').show();
        return;
    }
    alert('必须选择笔记!');
}
function loadReplayOptions(){
    var url = 'notebook/list.do';
    var data={userid:getCookie('userId')};
    $.getJSON(url, data, function(result){
        if(result.state==SUCCESS){
            var notebooks = result.data;
            //清楚全部的笔记本下拉列表选项
            //添加新的笔记本列表选项
            $('#replaySelect').empty();
            var id=$(document).data('notebookId');
            //console.log(notebooks);
            for(var i=0; i<notebooks.length; i++){
                var notebook = notebooks[i];
                var opt=$('<option></option>')
                    .val(notebook.id)
                    .html(notebook.name);
                //console.log(notebook);
                //默认选定当时笔记的笔记本ID
                if(notebook.id==id){
                    opt.attr('selected','selected');
                }
                $('#replaySelect').append(opt);
            }
        }else{
            alert(result.message);
        }
    });

}

function replayNote(){
    var li = $(document).data('replayItem');
    var noteId = li.data('noteId');
    var url = 'note/replay.do';
    var nId = $('#replaySelect').val();
    var data = {noteId:noteId, notebookId:nId};
    //console.log(data);
    $.post(url, data, function(result){
        if(result.state==SUCCESS){
            closeDialog();
            li.slideUp(200, function(){$(this).remove()});
        }else{
            alert(result.message);
        }
    });
}

//清除笔记本事件
function showDeleterollbackNoteDialog() {
	//console.log("delete");
	$('#can').load('./alert/alert_delete_rollback.html');
	var li = $(this).parent().parent();
	//将Id数据绑定到document上，删除笔记需要用到
	var noteId = li.data('noteId');
	$(document).data('noteId',noteId);
	$(this).addClass('chicked');
	//console.log(li.data('noteId'));
}
function deleterollbackNote() {
	var noteId = $(document).data('noteId');
	//console.log(noteId);
	//console.log("rollebacknote");
	var url = 'note/clear.do';
	var data = {
			noteId:noteId
	};
	$.post(url,data,function(result){
		if(result.state==SUCCESS){
			//删除成功将页面上的记录删除
			//console.log(result);
			var li = $('#trash-bin .chicked').parent().parent();
            var lis = li.siblings();
            if(lis.size()>0){
                lis.eq(0).click();
            }else{
                $('#input_note_title').val("");
                um.setContent("");
            }
            li.remove();
            closeDialog();//关闭对话框!
		} else {
			console.log(result.message);
		}
	});
}


/** 监听回收站按钮被点击 */
function showTrashBin(){
	//console.log("showTrashBin");
    $('#trash-bin').show() ;
    $('#note_list').hide() ;
    loadTrashBin(); //加载删除笔记列表
}

function loadTrashBin() {
	 var url = 'note/recycle.do';
	    var data = {userId: getCookie('userId')};
	    $.getJSON(url, data, function(result){
	        if(result.state==SUCCESS){
	            showNotesInTrashBin(result.data);
	        }else{
	            alert(result.message);
	        }
	    });
}
function showNotesInTrashBin(notes){
    var ul = $('#trash-bin ul');
    ul.empty();
    for(var i=0; i<notes.length; i++){
        var note = notes[i];
        var li = trashBinItem.replace('[title]', note.title);
        li = $(li);
        li.data('noteId', note.id);
        var id = li.data('noteId');
       // console.log(id);
        ul.append(li);
    }
}

var trashBinItem = 
			    '<li class="disable">'+
			        '<a><i class="fa fa-file-text-o" title="online" rel="tooltip-bottom"></i>'+
			        ' [title]'+
			        '<button type="button" class="btn btn-default btn-xs btn_position btn_delete">'+
			            '<i class="fa fa-times"></i>'+
			        '</button>'+
			        '<button type="button" class="btn btn-default btn-xs btn_position_2 btn_replay" id="btn_replay">'+
			            '<i class="fa fa-reply"></i>'+
			        '</button></a>'+
			    '</li>';


//删除笔记事件
function showDeleteNoteDialog() {
	$('#can').load('./alert/alert_delete_note.html');
}
function deleteNote() {
	console.log('deleteNote');
	var url = 'note/delete.do';
	console.log($(document).data('note'));
	var noteId = $(document).data('note').id;
	var title = null;
	var body = null;
	var stateId = 0;
	var data = {
			noteId:noteId
	};
	$.post(url,data,function(result){
		 if(result.state==SUCCESS){
	            //删除成功, 在当前笔记列表中删除笔记
	            //将笔记列表中的第一个设置为当前笔记, 否则清空编辑区域
	            var li = $('#note_list .checked').parent();
	            var lis = li.siblings();
	            if(lis.size()>0){
	                lis.eq(0).click();
	            }else{
	                $('#input_note_title').val("");
	                um.setContent("");
	            }
	            li.remove();
	            closeDialog();//关闭对话框!
	       }else{
	            alert(result.message);
	       }
	});
}
//移动笔记事件
function showMoveNoteDialog() {
	console.log('showNoteMoveDialog');
	$('#can').load('./alert/alert_move.html',loadNotebookOptions);
	
}
/** 加载移动笔记对话框中的笔记本列表 */
function loadNotebookOptions() {
	var url = 'notebook/list.do';
    var data={userid:getCookie('userId')};
    console.log(data);
    $.getJSON(url, data, function(result){
        if(result.state==SUCCESS){
            var notebooks = result.data;
            //清楚全部的笔记本下拉列表选项
            //添加新的笔记本列表选项
            $('#moveSelect').empty();
            var id=$(document).data('notebookId');
            for(var i=0; i<notebooks.length; i++){
                var notebook = notebooks[i];
                var opt=$('<option></option>')
                    .val(notebook.id)
                    .html(notebook.name);
                //默认选定当时笔记的笔记本ID
                if(notebook.id==id){
                    opt.attr('selected','selected');
                }
                $('#moveSelect').append(opt);
            }
        }else{
            alert(result.message);
        }
    });
}

/** 移动笔记事件处理方法 */
function moveNote(){
    var url = 'note/move.do';
    var id = $(document).data('note').id;
    var bookId=$('#moveSelect').val();
    //笔记本ID没有变化, 就不移动了!
    if(bookId==$(document).data('notebookId')){
    	return;
    }
    var data = {noteId:id, notebookId:bookId};
    console.log(data);
    $.post(url, data, function(result){
        if(result.state==SUCCESS){
            //移动成功, 在当前笔记列表中删除移动的笔记
            //将笔记列表中的第一个设置为当前笔记, 否则清空边编辑区域
            var li = $('#note_list .checked').parent();
            var lis = li.siblings();
            if(lis.size()>0){
                lis.eq(0).click();
            }else{
                $('#input_note_title').val("");
                um.setContent("");
            }
            li.remove();
            closeDialog();//关闭对话框!
        }else{
            alert(result.message);
        }
    });
}

function hideNoteMenu() {
	$('.note_menu').hide();
}

//笔记子菜单的事件
function showNoteMenu() {
	//找到菜单对象
	var btn = $(this);
	//$('.note_menu').hide(function() {
	
	//如果当前是被选定的笔记就弹出子菜单
	btn.parent('.checked').next().toggle();
	//btn.parent('.checked')获取class为'.checked'，如果不符合就返回空
	//});
	
	return false;//阻止点击事件继续传播
}

/*保存笔记事件方法*/
function updatenote() {
	var title = $('#input_note_title').val();
	var url = 'note/update.do';
	var note = $(document).data('note');
	//console.log(note);
	var data = {noteId:note.id};
	var modified = false;
	if(title && title!=note.title){
        data.title = title;
        modified = true;
	}
	var body = um.getContent();
    if(body && body != note.body ){
        data.body = body;
        modified = true;
    }
	if(modified){
		$.post(url,data,function(result){
			 if(result.state == 0){
                //console.log("Success!");
                //内存中的 note 改成新的数据
                note.title = title;
                note.body = body;
                var l = $('#note-list .checked').parent();
                $('#note-list .checked').remove();
                var li = noteTemplate.replace( '[title]', title);
                var a = $(li).find('a');
                a.addClass('checked');
                l.prepend(a);     
			 }else{
	            alert(result.mesage);
	         }     
		});
	}
}


/*显示添加笔记本的对话框*/
function showAddNotebookDialog() {
	 $('#can').load('alert/alert_notebook.html', function(){
         $('#input_note').focus();
     });
     $('.opacity_bg').show();
}

/*添加笔记本*/
function addNotebook() {
	var url = 'notebook/add.do';
	var name = $('#can #input_notebook').val();
	var data = {
			name:name,
			userId:getCookie('userId')
	};
	//console.log(data);
	$.post(url,data,function(result){
		console.log(result);
		if(result.state==SUCCESS){
			var notebook=result.data;
			//找到显示笔记本列表的ul对象
            var ul = $('#notebook-list ul');
            //创建新新的笔记本列表项目 li 
            var li = notebookTemplate.replace(
                    '[name]', notebook.name);
            li = $(li);
            //设置选定效果
            ul.find('a').removeClass('checked');
            li.find('a').addClass('checked');
            //绑定id
            $(document).data(
        			'notebookId', li.data('notebookId',notebook.id));
            //插入到笔记列表的第一个位置
            ul.prepend(li);
            //关闭添加对话框
            closeDialog(); 
            //showNoteBooks(notebook);
		}else{
            alert(result.message);
        }
	});
}

function closeDialog(){
    $('.opacity_bg').hide();
    $('#can').empty();
}

/*添加笔记*/
function addNote(){
    var url = 'note/add.do';
    var notebookId=$(document).data('notebookId');
    var title = $('#can #input_note').val();

    var data = {userId:getCookie('userId'),
        notebookId:notebookId,
        title:title};
    //console.log(data);
    $.post(url, data, function(result){
        if(result.state==SUCCESS){
            var note=result.data;
            console.log(note);
           
            //找到显示笔记列表的ul对象
            var ul = $('#note_list ul');
            //创建新新的笔记列表项目 li 
            var li = noteTemplate.replace(
                    '[title]', note.title);
            li = $(li);
            //设置选定效果
            ul.find('a').removeClass('checked');
            li.find('a').addClass('checked');
            li.data('noteId',note.id);
            //插入到笔记列表的第一个位置
            ul.prepend(li);
            //关闭添加对话框
            closeDialog(); 
            showNote(note);
        }else{
            alert(result.message);
        }
    });
}

/*显示添加笔记本的对话康框*/
function showAddNoteDialog(){
    var id = $(document).data('notebookId');
    console.log(id);
    if(id){
        $('#can').load('alert/alert_note.html', function(){
            $('#input_note').focus();
        });
        $('.opacity_bg').show();
        return;
    }
    alert('必须选择笔记本!');
}

/** 当点击笔记时加载笔记内容*/
function loadNote() {
	
	var li = $(this);
	
	//将笔记Id绑定到document上
	$(document).data('noteId',li.data('noteId'));
	
	//获取在显示时候绑定到li中的笔记ID值
	var id = li.data('noteId');
	
	//设置选中高亮效果
	li.parent().find('a').removeClass('checked');
	li.find('a').addClass('checked');
	
	var url = 'note/load.do';
	var data= {noteId: id };
	
	$.getJSON(url, data, function(result){
		//console.log(result);
		if(result.state==SUCCESS){
			var note = result.data;
			showNote(note);
			//console.log(note);
		}else{
			alert(result.message);
		}
	});
	
}

function showNote(note){
	//显示笔记标题
	$('#input_note_title').val(note.title);
	//显示笔记内容
	um.setContent(note.body);
	
	//绑定笔记信息, 用于保存操作
    $(document).data('note', note);
    //console.log($(document).data('note'));
}


/** 笔记本项目点击事件处理方法，加载全部笔记列表*/
function loadNotes(){
	 
	 $('#trash-bin').hide() ;
	 $('#note_list').show() ;
	
	//当前被点击的对象
	var li = $(this);
	
	//在被点击的笔记本li增加选定效果
	li.parent().find('a').removeClass('checked');
	li.find('a').addClass('checked');
	
	//绑定笔记本ID， 用于添加笔记功能
	$(document).data(
		'notebookId', li.data('notebookId'));
	
	var url = 'note/list.do';
	var data = {notebookId:li.data('notebookId')};
	$.getJSON(url,data,function(result){
		if(result.state==SUCCESS){
			var notes = result.data;
			showNotes(notes);
		}else{
			console.log(result.message);
		}
	});
	
}

/*笔记本项目点击事件处理方法，显示笔记列表*/
function showNotes(notes){
	//将每个笔记对象显示到屏幕的ul区域
	//console.log("showNotes");
	 var ul = $('#note_list ul');
	 ul.empty();
	 for(var i=0; i<notes.length; i++){
	        var note = notes[i];
	        if (note.statusId==1) {
	        	var li = noteTemplate.replace(
		                '[title]', note.title);
		        li = $(li);
		        //绑定noteid
		        li.data('noteId',note.id);
		        //console.log(li.data('noteId'));
		        ul.append(li);
			}
	   }
	
}


/*加载笔记本列表*/
function loadNotebooks(){
	
	var li = $(li);
	
	//在被点击的笔记本li增加选定效果
	li.parent().find('a').removeClass('checked');
	li.find('a').addClass('checked');
	
	//利用ajax从服务器获取（get）数据
	var url = 'notebook/list.do';
	var data = {userid:getCookie('userId')};
	$.getJSON(url,data,function(result){
		if(result.state==SUCCESS){
			var notebooks = result.data;
			//在showNotebooks方法中将全部的笔记本数据notebooks显示到notebook-list区域
			showNoteBooks(notebooks);
		}else{
			console.log(result.message);
		}
	});
	
	
}

/**在notebook-list区域中显示笔记本列表*/
function showNoteBooks(notebooks){
	var ul = $('#notebook-list ul');
	ul.empty();
	for(var i=0;i<notebooks.length;i++){
		var notebook = notebooks[i];
		var li = notebookTemplate.replace('[name]',notebook.name);
		var li = $(li);
		//将notebook.id绑定到li上
		li.data('notebookId',notebook.id);
		ul.append(li);
	}
}

var notebookTemplate =  '<li class="online notebook"> '+
						'<a>'+
						'<i class="fa fa-book" title="online" rel="tooltip-bottom"> '+
						'</i>[name]</a></li>';

var noteTemplate = '<li class="online note">'+
					'<a>'+
					'<i class="fa fa-file-text-o" title="online" rel="tooltip-bottom"></i> [title]<button type="button" class="btn btn-default btn-xs btn_position btn_slide_down btn_note_menu" ><i class="fa fa-chevron-down"></i></button>'+
					'</a>'+
					'<div class="note_menu" tabindex="-1">'+
					'<dl>'+
						'<dt><button type="button" class="btn btn-default btn-xs btn_move" id="btn_move" title="移动至..."><i class="fa fa-random"></i></button></dt>'+
						'<dt><button type="button" class="btn btn-default btn-xs btn_share" title="分享"><i class="fa fa-sitemap"></i></button></dt>'+
						'<dt><button type="button" class="btn btn-default btn-xs btn_delete" title="删除"><i class="fa fa-times"></i></button></dt>'+
					'</dl>'+
					'</div>'+
					'</li>';

