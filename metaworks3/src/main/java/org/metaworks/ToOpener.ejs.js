var org_metaworks_ToOpener = function(objectId, className){

	this.objectId = objectId;
	this.className = className;

	var object = mw3.objects[objectId];

	if(object){

		var callerObject = mw3.objects[mw3.recentCallObjectId];

		var targetOpenerId = callerObject.__openerObjectId;


		var parentDivs = $('#objDiv_' + mw3.recentCallObjectId).parents();

		parentDivs.each(function(index) {

			var classNameOfDiv = $(this).attr('classname');

			if (classNameOfDiv) {

				parentObjectId = $(this).attr('objectid');
				var parent = mw3.getObject(parentObjectId);

				if(parent && parent.__openerObjectId){
					targetOpenerId = parent.__openerObjectId;
				}
			}
		});


		var openerId = targetOpenerId;
		
		var faceHelper = mw3.getFaceHelper(openerId);
		
		if(faceHelper && faceHelper.toOpener){
			faceHelper.toOpener(object.target);
		}else{
			mw3.setObject(openerId, object.target);
			
			mw3.beanExpressions[this.objectId] = null;
			
			mw3.removeObject(this.objectId);
			mw3.onLoadFaceHelperScript();
		}
	}
};