
var org_metaworks_ToPrepend = function(objectId, className){

	var object = mw3.objects[objectId];
	
	if(object){
		mw3.removeObject(objectId);
		
		var objKeys = mw3._createObjectKey(object.parent, true);
			
		if(objKeys && objKeys.length){
						        				
			for(var i=0; i<objKeys.length; i++){
				var mappedObjId = mw3.objectId_KeyMapping[objKeys[i]];
				
				if(mappedObjId){
					var faceHelper = mw3.getFaceHelper(mappedObjId);
					if(faceHelper && faceHelper.toPrepend){
						faceHelper.toPrepend(object.target);
					}else{
						var html = mw3.locateObject(object.target, null);//, "#"+mappedObjdivId);
						
						$("#objDiv_" + mappedObjId).prepend(html);	
					}
	
					break;
				}	
			}
		}
		
		mw3.onLoadFaceHelperScript();
	}

}