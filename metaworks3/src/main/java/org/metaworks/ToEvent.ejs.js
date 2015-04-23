var org_metaworks_ToEvent = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	this.object = mw3.objects[this.objectId];
	
	var faceHelper = this;
	
	if(this.object){
		if(typeof this.object.target == 'string'){
			var triggerObjId = null;
			
			mw3.removeObject(this.objectId);
			
			if(this.object.target == 'opener')
				triggerObjId = mw3.recentOpenerObjectId[mw3.recentOpenerObjectId.length - 1];
			if(this.object.target == 'self')
				triggerObjId = mw3.recentCallObjectId;
				
			if(triggerObjId)
				faceHelper.toEvent(triggerObjId);
		}else{
			this.targetObjKeys = mw3._createObjectKey(this.object.target, true);
			
			if(this.object.filter){
				this.objectDiv.bind('loaded', {objectId: this.objectId}, function(event){
					var filterObjectId = $(event.target).attr('objectId');
					
					mw3.getFaceHelper(event.data.objectId).compare(filterObjectId);
				});
			}else{	
				faceHelper.compare();
			}
		}
	}
};

org_metaworks_ToEvent.prototype = {
	compare: function(filterObjectId){
		var exist = false;
		
		for(var i in mw3.objects){
			if(this.compareKey(mw3.objects[i])){
				
			   if(!filterObjectId || mw3.getFaceHelper(filterObjectId).match(mw3.objects[i])){
				   exist = true;
				   this.toEvent(i);
				   
				   break;
			   }
			}
		}
		
		if(!exist){
			for(var i=0; i<this.targetObjKeys.length; i++){
				var mappedObjId = mw3.objectId_KeyMapping[this.targetObjKeys[i]];
				
				if(mappedObjId){
					this.toEvent(mappedObjId);
					
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
				   Object.prototype.toString.call(compareObject) === '[object Array]')
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
	toEvent : function(targetObjId){
		$('#' + mw3._getObjectDivId(targetObjId)).trigger(this.object.event, [this.object.data]);
	}
}