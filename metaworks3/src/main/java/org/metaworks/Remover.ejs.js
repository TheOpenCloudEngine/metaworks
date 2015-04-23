var org_metaworks_Remover = function(objectId, className){
	this.object = mw3.objects[objectId];	
	var triggerObjId = null;
		
	if(this.object == null)
		return true;	
	
	if(typeof this.object.target == 'string'){
		if(this.object.target == 'opener')
			triggerObjId = mw3.recentOpenerObjectId[mw3.recentOpenerObjectId.length - 1];
		if(this.object.target == 'self')
			triggerObjId = mw3.recentCallObjectId;
				
	}else{
		var objKeys = [];
		if(this.object.match)
			objKeys.push(mw3._createObjectKey(this.object.target));
		else
			objKeys = mw3._createObjectKey(this.object.target, true);
		
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
				
		if(faceHelper && faceHelper.remover)
			faceHelper.remover(this.object.target);
		else	
			mw3.removeObject(triggerObjId);
	}
	
	this.loaded = function(){
		if(mw3.objects[objectId] != null)
			mw3.removeObject(objectId);		
	};

};

