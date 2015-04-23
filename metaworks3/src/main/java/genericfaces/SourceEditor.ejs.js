var SourceEditor = function(objectId, className){
	this.objectId = objectId;

	this.divName = "sourceEditor_" + objectId;	

//	$("#"+divName).width(400);
//	$("#"+divName).height(200);
	

    this.editor = ace.edit(this.divName);
    this.editor.setTheme("ace/theme/eclipse");

    var JavaScriptMode = require("ace/mode/java").Mode;
    this.editor.getSession().setMode(new JavaScriptMode());
    
}

SourceEditor.prototype.getValue = function(){
	return this.editor.getSession().getValue();
}