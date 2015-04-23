var org_metaworks_widget_Window = function(objectId, className){
	this.objectId = objectId;
	this.className = className;

	this.divId = "objDiv_" + objectId;
	
	this.width = $("#" + this.divId).width();
	this.height = $("#" + this.divId).height();
	
	$("#" + this.divId).css("height","100%");
	$("#" + this.divId).parent().find("#info_" + objectId).remove();
}

org_metaworks_widget_Window.prototype.close = function(){
    $('#objDiv_' + this.objectId).next('div').hide();
	$('#objDiv_' + this.objectId).hide('clip',100);
	$('#objDiv_' + this.objectId).parent().remove();
}

org_metaworks_widget_Window.prototype.startLoading = function(){
	$("#loader_" + this.objectId).show();
}

org_metaworks_widget_Window.prototype.endLoading = function(){
	$("#loader_" + this.objectId).hide();

}
