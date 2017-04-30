var org_metaworks_model_SortableElement = function(objectId, className){

    this.objectId = objectId;

}

org_metaworks_model_SortableElement.prototype = {

    remove: function(object){

        $("#objDiv_" + this.objectId).remove();

    }

}