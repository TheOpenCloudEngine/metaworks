var WindowPanel = function(objectId, className){
	this.objectId = objectId;
	this.className = className;

	$("#objDiv_" + objectId).addClass("mw3_windowpanel");
}