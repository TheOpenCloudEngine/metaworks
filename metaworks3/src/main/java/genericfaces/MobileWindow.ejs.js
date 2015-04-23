var MobileWindow = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	
	var object = mw3.objects[this.objectId];
	
	if(object && object.metaworksContext && object.metaworksContext.where == 'mobile')
		$.mobile.changePage('#popup_' + this.objectId);
} 

MobileWindow.prototype.close = function(){	
	$("#objDiv_" + this.objectId).remove();
}