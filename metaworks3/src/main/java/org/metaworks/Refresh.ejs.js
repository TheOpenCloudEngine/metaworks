var org_metaworks_Refresh = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	faceHelper = this;
	
	this.object = mw3.objects[objectId];
	if(this.object){
		if(this.object.self){
			mw3.removeObject(objectId);
			
			faceHelper.refresh(mw3.recentCallObjectId, this.object.target);
		}else{			
			this.targetObjKeys = mw3._createObjectKey(this.object.target, true);
			
			if(this.object.filter){
				$('#' + mw3._getObjectDivId(this.objectId)).bind('loaded', {objectId: this.objectId}, function(event){
					var filterObjectId = $(event.target).attr('objectId');
					
					mw3.getFaceHelper(event.data.objectId).compare(filterObjectId);
				});
			}else{				
				faceHelper.compare();
			}
		}
		mw3.onLoadFaceHelperScript();
	}
};

org_metaworks_Refresh.prototype = {
		compare: function(filterObjectId){
			var exist = false;
			
			for(var i in mw3.objects){
				if(this.compareKey(mw3.objects[i])){
					
				   if(!filterObjectId || mw3.getFaceHelper(filterObjectId).match(mw3.objects[i])){
					   exist = true;
					   this.refresh(i);
					   
					   //break;
				   }
				}
			}
			
			if(!exist){
				for(var i=0; i<this.targetObjKeys.length; i++){
					var mappedObjId = mw3.objectId_KeyMapping[this.targetObjKeys[i]];
					
					if(mappedObjId){
						this.refresh(mappedObjId);
						
						break;
					}
				}
			}
			
			mw3.removeObject(this.objectId);
		},
		compareKey : function(compareObject){
			if(!compareObject || 
					   Object.prototype.toString.call(compareObject) === '[object Boolean]' ||
					   Object.prototype.toString.call(compareObject) === '[object String]' ||
					   Object.prototype.toString.call(compareObject) === '[object Array]' || 
					   Object.prototype.toString.call(compareObject) === '[object Date]')
						return false;

			if(this.targetObjKeys && this.targetObjKeys.length){
				var matchKeyCnt = 0;
				
				if(this.object.match)
					matchKeyCnt = this.targetObjKeys[0].split('@').length;
				
				for(var i=0; i<this.targetObjKeys.length; i++){
					var compareObjKey = mw3._createObjectKey(compareObject);
					
					if(this.object.match && (matchKeyCnt > compareObjKey.split('@').length))
						break;
					
					if(this.targetObjKeys[i] == compareObjKey){
						return true;
					}
				}
			}
		},
	refresh : function(targetObjId){
		var faceHelper = mw3.getFaceHelper(targetObjId);
		
		if(faceHelper && faceHelper.refresh){
			faceHelper.refresh(this.object.target);
		}else{
			mw3.setObject(targetObjId, this.object.target);
		}	
	}
		
}
