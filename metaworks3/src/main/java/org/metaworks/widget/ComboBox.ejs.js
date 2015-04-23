var org_metaworks_widget_ComboBox = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	
	$("#objDiv_" + objectId).addClass("mw3_subMenu");
	
	var opener = $('#objDiv_' + mw3.recentOpenerObjectId);
	var menuLeft = opener.attr('menuLeft');
	var menuTop = opener.attr('menuTop');

	var subMenu = $('#objDiv_' + objectId).parent();
		
	subMenu.addClass('mw3_subMenu').attr('methodName', mw3.recentCallMethodName);
	
	if(menuLeft)
		subMenu.css('left', menuLeft + 'px');
	
	if(menuTop)
		subMenu.css('top', menuTop + 'px');
}

org_metaworks_widget_ComboBox.prototype.selectDone = function(item){
	var comboBox = mw3.objects[this.objectId];
	comboBox.selectedItem = item;
	
	comboBox.selectDone();
}	