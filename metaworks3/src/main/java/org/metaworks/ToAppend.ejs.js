var org_metaworks_ToAppend = function(objectId, className){
	this.object = mw3.objects[objectId];	
	var triggerObjId = null;
		
	if(this.object == null)
		return true;	
	
	if(typeof this.object.parent == 'string'){
		if(this.object.parent == 'opener')
			triggerObjId = mw3.recentOpenerObjectId[mw3.recentOpenerObjectId.length - 1];
		if(this.object.parent == 'self')
			triggerObjId = mw3.recentCallObjectId;
				
	}else{
		var objKeys = [];
		if(this.object.match)
			objKeys.push(mw3._createObjectKey(this.object.parent));
		else
			objKeys = mw3._createObjectKey(this.object.parent, true);
		
		if(objKeys && objKeys.length){
			for(var i=0; i<objKeys.length; i++){			
				var mappedObjId = mw3.objectId_KeyMapping[objKeys[i]];
	
				if(mappedObjId){
					triggerObjId = mappedObjId;
					break;
				}	
			}
		}
	}
	
	if(triggerObjId){
		var faceHelper = mw3.getFaceHelper(triggerObjId);
		
		if(faceHelper && faceHelper.toAppend){
			faceHelper.toAppend(this.object.target);
		}else{
			var html = mw3.locateObject(this.object.target, null);//, "#"+mappedObjdivId);
			
			$("#objDiv_" + mappedObjId).append(html);	
		}			
		
		mw3.onLoadFaceHelperScript();
	}
	
	this.loaded = function(){
		if(mw3.objects[objectId] != null)
			mw3.removeObject(objectId);		
	};	
}