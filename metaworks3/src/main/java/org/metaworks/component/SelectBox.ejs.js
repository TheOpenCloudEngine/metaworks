var org_metaworks_component_SelectBox = function(objectId, className) {
	this.objectId = objectId;
	this.className = className;
	
	var object = mw3.objects[this.objectId];

	var selectBoxId = "#select_" + objectId ;
	
	/*
	if(object != null && object.__descriptor && object.__descriptor.getOptionValue('changeEvent')){
		$('#' + mw3.createInputId(this.objectId)).bind('change', {objectId : this.objectId},function(event){
			var change = $(this).find('option:selected');
			
			mw3.getFaceHelper(event.data.objectId).change(change.val(), change.text());
		});
	}
	*/

	if(object.multiple && $(selectBoxId).multiselect) {
		$(selectBoxId).multiselect(
			{enableFiltering: true, enableFullValueFiltering: true}
		);
	}


	$(selectBoxId).bind('click', function(e){

		var element = $(e.target);
		var optionSize = element[0].length;

		if(optionSize < 2){
			var objectId = element[0].id.split("_")[1];
			var obj = mw3.objects[objectId];

			if(obj.loadOptions)
				obj.loadOptions();
		}

	});
};

org_metaworks_component_SelectBox.prototype = {
	getValue : function(){
		var object = mw3.objects[this.objectId];

		var combo = $('#select_' + this.objectId);

		if(combo.length > 0){
			var change = combo.find('option:selected');

			var sep = "";

			object.selected = "";
			object.selectedText = "";

			if(change.length && change.length > 1){
				for(var i=0; i<change.length; i++){
					object.selected = object.selected + sep + change[i].value;
					object.selectedText = object.selectedText + sep + change[i].text;
					sep = ", ";
				}
			}else{
				object.selected = change.val();
				object.selectedText = change.text();
			}
		}

		return object;
	},
	change : function(val, text){
	}
};