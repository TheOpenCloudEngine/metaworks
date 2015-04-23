var Window = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	
	this.divId = '#objDiv_' + this.objectId;
	this.divObj = $(this.divId);
	
	this.smallDivId = '#sm_' + this.objectId;
	
	var layout = $(this.divId).parent();	
	if(!layout.hasClass('pane')){
		layout = layout.parent();
	}	
	this.layoutDiv = layout;
	this.layoutName = layout.attr('layoutName');
		
	$('.mw3_window_last').removeClass('mw3_window_last');	
	
	this.divObj.addClass('mw3_window').addClass('mw3_layout').addClass('mw3_window_last').attr('objectId', objectId);
	//.css("padding-bottom","35px");
}

Window.prototype.setTitle = function(title){
	$(this.divId + " .mw3_window_title span").html(title);
}

Window.prototype.loaded = function(){
	var maximize = $('.mw3_window_maximize');
	
	if(maximize.length > 0)
		mw3.getFaceHelper(maximize.attr('objectId')).maximize();			
}

Window.prototype.destroy = function(){
	if(this.layout)
		this.layout.destroy();
	
	this.divObj.unbind('keyup');
			
	$('.mw3_windowpanel').find(this.smallDivId).remove();
}

Window.prototype.maximize = function(){
	var miximizeBtn = $('#window_miximize_' + this.objectId);
	
	var layout = $(this.divId).parent().closest('.mw3_layout');
	var layoutId = layout.attr('objectId');
	
	if(miximizeBtn.hasClass('togglebtnexp')){	
		this.divObj.addClass('mw3_window_maximize');
		
		// 최대화
		miximizeBtn.removeClass('togglebtnexp').addClass('togglebtnmini');
		
		mw3.getFaceHelper(layoutId).maximize(this.objectId);
		
	}else{
		this.divObj.removeClass('mw3_window_maximize');
		
		// 이전크기로 복원
		miximizeBtn.removeClass('togglebtnmini').addClass('togglebtnexp');

		mw3.getFaceHelper(layoutId).resume();
	}
}

Window.prototype.minimize = function(){

	this.showSmallWindow();
	
	var layoutId = $(this.divId).parent().closest('.mw3_layout').attr('objectId');
	if(layoutId)
		mw3.getFaceHelper(layoutId).hide(this.layoutName);
	
}

Window.prototype.resume = function(){
		
	this.hideSmallWindow();
	
	var layoutId = $(this.divId).parent().closest('.mw3_layout').attr('objectId');
	if(layoutId)
		mw3.getFaceHelper(layoutId).show(this.layoutName);
	
	if(typeof this.layout == 'undefined'){
		this.load();
		this.resizeChild();
	}
		
	
}

Window.prototype.showSmallWindow = function(){
	
	// 최초 입력을 위해 입력여부 확인
	if($('.mw3_windowpanel').find(this.smallDivId).length == 0)
		$(this.smallDivId).appendTo('.mw3_windowpanel');
	
	$(this.smallDivId).show();
	
}

Window.prototype.hideSmallWindow = function(){
	$(this.smallDivId).hide();
}

Window.prototype.startLoading = function(){
	$('#loader_' + this.objectId).show();
}

Window.prototype.endLoading = function(){
	$('#loader_' + this.objectId).hide();

}
