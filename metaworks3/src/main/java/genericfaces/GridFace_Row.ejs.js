var GridFace_Row = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];
	
	if(this.object == null)
		return true;
	
};

GridFace_Row.prototype = {
	loaded : function(){
		this.objectDiv.trigger('loaded');
	},
	edit : function(object){
		var options = mw3.getMetadata(object.__className);
		
		options['htmlTag'] =  'tr';
		options['ejsPath'] =  'dwr/metaworks/genericfaces/GridFace_Row.ejs';
		options['parentId'] =  this.objectId;
		
		var html = mw3.locateObject(object, object.__className, null, options);

		this.objectDiv.replaceWith(html);
		
		mw3.onLoadFaceHelperScript();
	},
	
	editItem : function(){
		
		var confirmDivId = 'confirm_' + this.objectId;
		
		$('body').append("<div id='" + confirmDivId + "'></div>");
		
		var options = {when : 'edit'};
		
		mw3.locateObject(this.object, null, '#' + confirmDivId, options);
		
		var metadata = mw3.getMetadata(this.className);
		var confirm = mw3.localize('$Confirm');
		var cancel = mw3.localize('$Cancel');
		
		$('#' + confirmDivId).dialog({
			title : metadata.displayName + ' ' + mw3.localize('$Edit'),			
			objectId : this.object.__objectId,
			parentId : this.objectId,
            resizable: false,
            modal: true,
            buttons: {
            	confirm: function(event) {
                	var objectId = $(this).dialog('option', 'objectId');
                	var parentId = $(this).dialog('option', 'parentId');
                	
                	var object = mw3.getObject(objectId);
                	
                    $( this ).remove();
                    
                    $(mw3.getFaceHelper(parentId).edit(object));
                },
                cancel: function() {
                    $( this ).dialog( "close" );
                }
            },
	        close: function( event, ui ) {
	        	var objectId = $(this).dialog('option', 'objectId');
	        	
	        	mw3.removeObject(objectId);
	        	
	        	$( this ).dialog( "destroy" );
	        	$( this ).remove();
	        }
        });
	},
	
	setRownum : function(rownum){
		$('#rownum_' + this.objectId).html(rownum);
	}
};
