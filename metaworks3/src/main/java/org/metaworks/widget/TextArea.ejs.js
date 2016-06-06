var org_metaworks_widget_TextArea = function(objectId, className){

    this.objectId = objectId;

}

org_metaworks_widget_TextArea.prototype = {

    getValue : function() {

        var object = mw3.objects[this.objectId];

        if(!object){
            object = {
                __className: 'org.metaworks.widget.TextArea'
            };
        }

        var textarea = $('#textarea_' + this.objectId);

        object.text = textarea.val();

        return object;
    }

}