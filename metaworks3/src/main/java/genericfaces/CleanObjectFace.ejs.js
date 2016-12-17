var CleanObjectFace = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];

	if(this.object == null)
		return true;
	
	
	var objectMetadata = mw3.getMetadata(className);
	
	if(objectMetadata && objectMetadata.faceOptions && objectMetadata.faceOptions['disableHeight']){
	}else{
		this.objectDiv.css({'height': '100%'});
	}

};