var org_metaworks_component_HorizontalSplitBox = function(objectId, className){
	
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];

	if(this.object == null)
		return true;	
	
	
	this.objectDiv.addClass('mw3_resize').attr('objectId', this.objectId);
	this.resize = function(){
		var splitterId = mw3.getChildObjectId(this.objectId, 'splitter');
		
		mw3.getFaceHelper(splitterId).resize();
	};
};

