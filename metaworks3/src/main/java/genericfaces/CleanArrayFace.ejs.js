var CleanArrayFace = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
}

CleanArrayFace.prototype.addNew = function(){
	var arrayObj = mw3.getObject(this.objectId);
	var newOne = {__className: this.className, metaworksContext: {when: mw3.WHEN_NEW}};
	arrayObj[arrayObj.length] = newOne;
	
	var divId = "#objDiv_" + this.objectId; 
	
	mw3.locateObject(newOne, this.className, divId);
	
}