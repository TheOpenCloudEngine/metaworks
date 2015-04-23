var org_metaworks_filter_ContextFilter = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.object = mw3.objects[this.objectId];
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);

	this.match = function(target){
		for(var key in this.object.compareContextList){
			var compareContextName = this.object.compareContextList[key]
			
			if(!(target.metaworksContext && this.object.metaworksContext &&
			   target.metaworksContext[compareContextName] == this.object.metaworksContext[compareContextName])){
				
				return false;
			}
		}
		
		return true;
	}
	
	this.loaded = function(){
		$(this.objectDiv).trigger('loaded');
	}
}