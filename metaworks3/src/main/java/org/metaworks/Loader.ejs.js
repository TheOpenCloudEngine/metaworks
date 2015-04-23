var org_metaworks_Loader = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];

	if(this.object == null)
		return true;	

	var targetObjectId = mw3.getChildObjectId(this.objectId, 'target');
	
	if(targetObjectId && this.object.caller)
		mw3.call(targetObjectId, this.object.caller);
}