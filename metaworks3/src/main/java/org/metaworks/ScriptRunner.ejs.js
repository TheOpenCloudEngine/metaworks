var org_metaworks_ScriptRunner = function(objectId, className){
	this.object = mw3.objects[objectId];	
	
	if(this.object == null)
		return true;	
	
	eval(this.object.script);
	
	mw3.removeObject(objectId);
};

