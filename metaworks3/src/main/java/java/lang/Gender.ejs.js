var java_lang_Boolean = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
}

java_lang_Boolean.prototype.getValue = function() {
	var checked = $('#chk_' + this.objectId).attr('checked');
	
	if(typeof checked == 'undefined')
		checked = false;
	else
		checked = true;
	
	return checked;
}