var ObjectFace = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
}

ObjectFace.prototype.newObject = function(){
	mw3.setObject(this.objectId, {__className: this.className});
	
}