var org_metaworks_model_SortableList = function(objectId, className){

    this.objectId = objectId;
    var ul = $("#objDiv_" + objectId).find("ul").first();

    // ul.children("li").each(function(elem){
    //     if(this.id && this.id.indexOf("objDiv_")==0){
    //         $(this).draggable({
    //             start: function() {
    //                 ul.css({ transform: 'scale(.5)' })
    //             },
    //             stop: function() {
    //             }
    //         });
    //     }
    // })


    ul.sortable({
        placeholder: "highlight",
        start: function(event, ui) {
            // Resize elements
            $(this).sortable('refreshPositions');
        },
        // stop: function(){
        //     $(this).css({ transform: 'scale(1)' })
        // }
    });

}

org_metaworks_model_SortableList.prototype.getValue = function(){

    var elements = [];
    $("#objDiv_" + this.objectId).find("li").each(function(elem){
        if(this.id && this.id.indexOf("objDiv_")==0){
            var objectId = this.id.substr("objDiv_".length);

            elements.push(mw3.getObject(objectId));
        }
    })

    var object = mw3.objects[this.objectId];
    object.elements = elements;

    return object;

}

org_metaworks_model_SortableList.prototype.addNew = function() {
    var object = mw3.objects[this.objectId];
    var ul = $("#objDiv_" + this.objectId).find("ul").first();

    var elemVal = mw3.newObject( object.elementClassName );
    var newEl = {
        __className: 'org.metaworks.model.SortableElement',
        value: elemVal,
        metaworksContext:{
            when: 'edit'
        }
    };

    var html = mw3.locateObject(newEl, null);

    ul.append(html);

}