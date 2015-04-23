var org_metaworks_widget_Download = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	
	var object = mw3.objects[this.objectId];
	
	if(object.fileTransfer)
		dwr.engine.openInDownload(object.fileTransfer);
	
	mw3.removeObject(this.objectId);	
}