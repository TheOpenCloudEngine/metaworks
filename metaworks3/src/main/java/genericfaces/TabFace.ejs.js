/*
TabFace.prototype = new ArrayFace();
TabFace.superclass = ArrayFace;
TabFace.prototype.constructor = TabFace;
*/

var TabFace = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.object = mw3.objects[this.objectId];
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);

	this.tabTemplate = "<li><a href='#{href}' id='#{ui-id}'>#{label}</a> <span class='ui-icon ui-icon-close' role='presentation'>Remove Tab</span></li>",
	this.tabCounter = this.object.length;
	this.tabCounter++;
	
	this.layout = this.objectDiv.addClass('mw3_layout').layout();
	this.tabs = $('#tabs-' + this.objectId).tabs();
	$('#tabs-' + this.objectId).css({'padding':'20px','background':'none'})

	this.tabs.delegate( "span.ui-icon-close", "click", {objectId: this.objectId}, function(event) {
		var panelId = $( this ).closest( "li" ).attr( "aria-controls" );
		var objectId = $( "#" + panelId ).children(':first').attr('objectId');

		$('#' + mw3._getObjectDivId(objectId)).trigger('close');
	});
	
	this.closeTab = function(panelId) {
		$( this ).closest( "li" ).remove();
		
		var objectId = $( "#" + panelId ).children(':first').attr('objectId');
		
		$('#tabs-' + this.objectId).find('ul li').filter(function(index){
			if(panelId == $(this).attr("aria-controls"))
				$(this).remove();
		});

		this.tabs.tabs( "refresh" );
		this.layout.resizeAll();
		
		var tabsNav = this.tabs.children();
		if(tabsNav.children().length == 0)
			tabsNav.hide();
	}
	
	$(this.tabs).on("tabsactivate", {objectId: this.objectId}, function(event, ui) {
		var layout = mw3.getFaceHelper(event.data.objectId).getLayout();
		layout.resizeAll();
	});
	
	 
	this.getTabs = function(){
		return this.tabs;
	}

	this.getLayout = function(){
		return this.layout;
	}
	
	this.destroy = function(){
		this.layout.destroy();
	}

	this.addTab = function(object){
		var prevObjectId = mw3.objectIndexOf(object);
		if(prevObjectId && mw3.objects[prevObjectId]){
			var metadata = mw3.getMetadata(mw3.objects[prevObjectId].__className);
			if(metadata.keyFieldDescriptor){
				if(object[metadata.keyFieldDescriptor.name]){
					var index = $('#' + mw3._getObjectDivId(prevObjectId)).closest('.ui-tabs-panel').prevAll().length;
					
					this.tabs.tabs('option', 'active', index);
					
					return;					
				}
			}
		}
		
		var label = mw3.getObjectNameValue(object, true);
		var id = "tabs-" + this.tabCounter,
        li = $( this.tabTemplate.replace( /#\{href\}/g, "#" + id ).replace( /#\{label\}/g, label ).replace( /#\{ui-id\}/g, 'ui-id-' + this.tabCounter ) );
		
		
		this.tabs.find( ".ui-tabs-nav" ).append( li );
		this.tabs.find( ".ui-layout-content" ).append( "<div id='" + id + "' style='height: 100%'></div>" );
		this.tabs.tabs( "refresh" );
		this.tabs.children().show();
		this.tabs.tabs( "option", "active", this.tabs.find( ".ui-tabs-nav li" ).length-1 );
		
		this.tabCounter++;
		
		this.add(id, object);
	}
	
	this.add = function(targetDivId, item){
		var arrayObj = mw3.getObject(this.objectId);
		arrayObj[arrayObj.length] = item;
		
		mw3.locateObject(
				item, 
				item.__className, 
				'#' + targetDivId, 
				null, 
				{
					objectId: this.objectId,
					name: '[' + (arrayObj.length-1).toString() + ']'
				}
		);
	}
}