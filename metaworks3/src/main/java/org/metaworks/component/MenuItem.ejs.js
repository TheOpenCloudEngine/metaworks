var org_metaworks_component_MenuItem = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];

	if(this.object == null)
		return true;
	
	// remove info div
	$('#' + mw3._getInfoDivId(this.objectId)).remove();
	
	this.objectDiv.addClass(this.object.type);

	if(this.object.subMenu)
		this.objectDiv.addClass("submenu");
	
	this.objectDiv.css({'min-width': '100px',
						'min-height': '0px',
						'max-width': '9961px',
						'max-height': '9992px',
						'position' : 'relative'});
	
	if(this.object.subMenu){
		// add event mouse over
		$(this.objectDiv).hover(
			function () {	
				$(this).children(':last').children(':first').css('left', $(this).outerWidth() + 'px').show();
			}, 
			function () {
				$(this).children(':last').children(':first').hide();
			}
		);		
	}else{
		// add event mouse down
		$(this.objectDiv).bind('mousedown', {objectId : this.objectId}, function(event){
			mw3.getFaceHelper(event.data.objectId).select();
		});		
	}
};

org_metaworks_component_MenuItem.prototype = {
	select : function(){
/*		if(event.stopPropagation){
			event.stopPropagation();
		}else if(window.event){
			window.event.cancelBubble = true;
		}*/
		
		var parent = this.objectDiv.parent('.menu');
		
		if(parent.length == 0)
			parent = this.objectDiv.parentsUntil('.c9-menu-btn');
		
		parent = parent.parent();
		
		var parentId = parent.attr('objectId');
		
		if(parentId){
			var parentFaceHelper = mw3.getFaceHelper(parentId);
			
			if(this.objectDiv.hasClass('submenu')){
				if(parentFaceHelper && parentFaceHelper.blur){
					parentFaceHelper.blur(this.object.id);
				}
			}else{
				if(parentFaceHelper && parentFaceHelper.action){
					parentFaceHelper.action(this.object.id);
				}			
			}
			
		}
	}
};