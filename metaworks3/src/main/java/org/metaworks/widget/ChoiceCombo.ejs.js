var org_metaworks_widget_ChoiceCombo = function(objectId, className) {
	this.objectId = objectId;
	this.className = className;
	
	var object = mw3.objects[this.objectId];
	
	if(object != null && object.__descriptor && object.__descriptor.getOptionValue('changeEvent')){
		$('#' + mw3.createInputId(this.objectId)).bind('change', {objectId : this.objectId},function(event){
			var change = $(this).find('option:selected');
			
			mw3.getFaceHelper(event.data.objectId).change(change.val(), change.text());
		});
	}
}

org_metaworks_widget_ChoiceCombo.prototype = {
	getValue : function(){
		var object = mw3.objects[this.objectId];
		
		var combo = $('#select_' + this.objectId);
		
		if(combo.length > 0){
			var change = combo.find('option:selected');
			
			object.selected = change.val();
			object.selectedText = change.text();				
		}
		
		return object;
	},
	change : function(val, text){
	}
}