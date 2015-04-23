var java_lang_String = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];

	this.placeholder = this.objectDiv.find('input');
	
	if(this.placeholder.attr('placeholder') && this.placeholder.textPlaceholder){
		this.placeholder.textPlaceholder();
	}
}

java_lang_String.prototype = {
	destroy : function(){
		if(this.placeholder)
			this.placeholder.unbind();
	}
}

