var WindowTab = function(objectId, className){
	
	this.objectId = objectId;
	this.className = className;

	this.divId = "objDiv_" + objectId;
	this.smallDivId = "sm_" + objectId;
	
	this.width = $("#" + this.divId).width();
	this.height = $("#" + this.divId).height();
		
	$("#" + this.divId).addClass("mw3_window").css("height","100%");	
	$("#" + this.divId).parent().find("#info_" + objectId).remove();
}

WindowTab.prototype.minimize = function(layoutName){

	// 최초 입력을 위해 입력여부 확인
	if($(".mw3_windowpanel").find("#" + this.smallDivId).length == 0)
		$("#" + this.smallDivId).appendTo(".mw3_windowpanel");
	
	$("#" + this.smallDivId).show();
		
	var layoutId = $("#objDiv_" + this.objectId).closest(".mw3_layout").attr("objectId");
	if(layoutId)
		mw3.getFaceHelper(layoutId).hide(layoutName);
}


WindowTab.prototype.resume = function(layoutName){
	
	$("#" + this.smallDivId).hide();
	
	var layoutId = $("#objDiv_" + this.objectId).closest(".mw3_layout").attr("objectId");
	if(layoutId)
		mw3.getFaceHelper(layoutId).show(layoutName);
}

WindowTab.prototype.startLoading = function(){
	$("#loader_" + this.objectId).show();
}

WindowTab.prototype.endLoading = function(){
	$("#loader_" + this.objectId).hide();

}
