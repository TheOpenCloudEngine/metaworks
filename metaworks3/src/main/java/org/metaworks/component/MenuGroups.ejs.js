var org_metaworks_component_MenuGroups = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];
	
	this.objectDiv.addClass('c9-mbar-bcont');
	
	// make menu groups for ArrayFace.ejs
	var groupsDiv = this.objectDiv.children(':first');
	groupsDiv.addClass('c9-mbar-cont');
	
};