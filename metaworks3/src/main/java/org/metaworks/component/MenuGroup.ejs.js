var org_metaworks_component_MenuGroup = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];
	
	// make menu groups for ArrayFace.ejs
	var groupDiv = this.objectDiv.children(':first');
	groupDiv.addClass('basic').addClass('fakehbox').addClass('aligncenter');
	groupDiv.css({'padding': '0px 5px',
				  'position': 'static', 
				  'min-width': '0px',
				  'min-height': '0px',
				  'max-width': '9990px',
			      'max-height': '10000px'});
};