var org_metaworks_model_SortableList = function(objectId, className){

    this.objectId = objectId;
    $("#objDiv_" + objectId).find("ul").sortable();

}

org_metaworks_model_SortableList.prototype.getValue = function(){

    var elements = [];
    $("#objDiv_" + this.objectId).find("li").each(function(elem){
        if(this.id && this.id.indexOf("objDiv_")==0){
            var objectId = this.id.substr("objDiv_".length);

            elements.push(mw3.objects[objectId]);
        }
    })

    var object = mw3.objects[this.objectId];
    object.elements = elements;

    return object;

}