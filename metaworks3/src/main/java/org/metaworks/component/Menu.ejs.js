var org_metaworks_component_Menu = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];
	
	if(this.object == null)
		return true;
	
	//function
	// function
	this.destroy = function(){
		this.objectDiv.unbind();	
	};
	
	this.select = function(){
		if(event.stopPropagation){
			event.stopPropagation();
		}else if(window.event){
			window.event.cancelBubble = true;
		}
		
		if(this.objectDiv.hasClass('c9-menu-btnDown'))
			this.blur();
		else
			this.focus();
	};
	
	this.focus = function(){
		var curMenu = $('.c9-menu-btnDown');
		
		// blur
		if(curMenu.length){
			var curMenuObjectId = curMenu.attr('objectId');
			
			mw3.getFaceHelper(curMenuObjectId).blur();
		}
		
		this.objectDiv.addClass('c9-menu-btnDown');
		this.objectDiv.children('.menu').show();
	};
	
	this.blur = function(){
		// blur
		this.objectDiv.removeClass('c9-menu-btnDown');
		this.objectDiv.children('.menu').hide();
		
		var parent = this.objectDiv.parentsUntil('.menu');
		if(parent.length > 0){
			var parentObjectId = parent.attr('objectId');
			
			if(parentObjectId && mw3.getFaceHelper(parentObjectId) && mw3.getFaceHelper(parentObjectId).select)
				mw3.getFaceHelper(parentObjectId).select();
		}
	};
	
	this.action = function(menuId){
		
		var run = false;
		
		if(this[menuId]){
			run = true;
			this[menuId]();
		}
		
		if(!run){
			var objectMetadata = mw3.getMetadata(this.className);
			
			for(var method in objectMetadata.serviceMethodContextMap){
				if(menuId == method){
					run = true;
					mw3.call(this.objectId, menuId);
					
					break;
				}
			}
		}
		
		if(!run)
			alert(menuId + " clicked! (need face helper or service method)");
		
		this.blur();
	};
	
	
	
	var sub = false;
	if(this.object.sub != null && typeof this.object.sub != 'undefined' && this.object.sub)
		sub = true;
	
	var context = false;
	if(this.object.context != null && typeof this.object.context != 'undefined' && this.object.context)
		context = true;
	
	// remove info div
	$('#' + mw3._getInfoDivId(this.objectId)).remove();

	// make menu for ArrayFace.ejs
	var menuDiv = this.objectDiv.children(':last').prev();
	
	menuDiv.addClass('menu').addClass('downward').addClass('moveright');
	//{
	menuDiv.css({
				 'position': 'absolute',
				 'min-width': '0px',
				 'min-height': '0px',
				 'max-width': '9998px',
			     'max-height': '9992px',
			     'display': 'none',
			     'z-index': '200000',
			     'top': '0px',
			     'left': '0px',
			     'opacity': '1'});

	this.objectDiv.attr('objectId', this.objectId);
	
	if(sub){
		menuDiv.css({'top': '0px'});
	}
	
	if(!sub){
		if(context){
			this.focus();
			
			closeOutsideContainer(this.objectDivId);
			
		}else{
			this.objectDiv.addClass('c9-menu-btn');
			this.objectDiv.addClass('c9-menu-btnBool');
			this.objectDiv.css({'margin': '0px',
								'min-width': '26px',
								'min-height': '0px',
								'max-width': '10000px',
								'max-height': '10000px'});
			
			// add event mouse over
			$(this.objectDiv).hover(
				function () {
					$(this).addClass('c9-menu-btnOver');
								
					var curMenu = $('.c9-menu-btnDown');
					if(curMenu.length > 0){
						var objectId = $(this).attr('objectId');
						
						mw3.getFaceHelper(objectId).focus();
					}
				}, 
				function () {
					$(this).removeClass('c9-menu-btnOver');
				}
			);
			
			// add event mouse down
			$(this.objectDiv).bind('mousedown', {objectId : this.objectId}, function(event){
				mw3.getFaceHelper(event.data.objectId).select();
			});		
		}
			
	}
	
	this.startLoading = function(){
		
	};
	this.endLoading = function(){
		
	};
	this.showStatus = function(){
		
	};

};