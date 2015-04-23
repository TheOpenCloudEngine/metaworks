
var org_metaworks_ToPrev = function(objectId, className){

	var object = mw3.objects[objectId];
	
	if(object){
		mw3.removeObject(objectId);
		
		var objKeys = mw3._createObjectKey(object.next, true);
			
		if(objKeys && objKeys.length){
			var matchKeyCnt = 0;
			if(object.match)
				matchKeyCnt= objKeys[0].split('@').length;
			
			for(var i=0; i<objKeys.length; i++){
				if(object.match && (matchKeyCnt > objKeys[i].split('@').length))
					break;
				
				var mappedObjId = mw3.objectId_KeyMapping[objKeys[i]];
				if(mappedObjId){
					var faceHelper = mw3.getFaceHelper(mappedObjId);
					
					if(faceHelper && faceHelper.toPrev){
						faceHelper.toPrev(object.target);
					}else{
						var html = mw3.locateObject(object.target, null);//, "#"+mappedObjdivId);
						
						$(html).insertBefore("#objDiv_" + mappedObjId);
					}
	
					break;
				}	
			}
		}
		
		mw3.onLoadFaceHelperScript();
	}

}