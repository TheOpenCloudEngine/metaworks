var org_metaworks_component_CheckBox = function(objectId, className) {
	this.objectId = objectId;
	this.className = className;
	
};
org_metaworks_component_CheckBox.prototype = {
		getValue : function(){
			var object = mw3.objects[this.objectId];
			var checkBoxName = "checkBox_" + this.objectId;
			var selected = "";
			$("input:checkbox[name='"+checkBoxName+"']:checked").each(function(i){
				var tempVal = $(this).val();
				if( i > 0 ){
					selected += ",";
				}
				selected += tempVal;
			});
				
			object.selected = selected;
			return object;
		},
		change : function(val, text){
		}
	};