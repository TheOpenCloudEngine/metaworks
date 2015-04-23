var HiddenFace = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.object = mw3.objects[this.objectId];
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.refresh = function(object){
		for(var key in object)
			this.object[key] = object[key];
		
		$(this.objectDiv).trigger('change');
	}
}