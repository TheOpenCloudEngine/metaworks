var org_metaworks_widget_menu_SubMenu = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.object = mw3.objects[this.objectId];
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.openerObjectId = mw3.recentOpenerObjectId[mw3.recentOpenerObjectId.length-1];
	this.openerDivId = mw3._getObjectDivId(this.openerObjectId);
	this.openerDiv = $('#' + this.openerDivId);
	this.openMenuDiv = this.openerDiv.find('.menu-' + mw3.recentCallMethodName);
	
	var offset = this.openMenuDiv.offset();

	this.objectDiv.parent().attr('objectId', this.objectId);
	
	try{
		if(this.openMenuDiv.hasClass('main-menu')){
			this.objectDiv.parent().css({
				top: offset.top + this.openMenuDiv.height()+14,
				left: offset.left,
				'background-color': 'white'
			});
		}else{
			this.objectDiv.parent().css({
				top: offset.top-8,
				left: offset.left + this.openMenuDiv.width()+1,
				'background-color': 'white'
			});
	
		}
	}catch(e){}
}

org_metaworks_widget_menu_SubMenu.prototype.callMethod = function(element, methodName){	
	mw3.call(this.objectId, methodName);
}	