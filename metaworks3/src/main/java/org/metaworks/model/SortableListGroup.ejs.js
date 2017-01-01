var org_metaworks_model_SortableListGroup = function(objectId, className){

    var sortableDivs = $("#objDiv_" + objectId).find("ul");

    setTimeout(function(){
        $(sortableDivs[0]).sortable( "option", "connectWith", $(sortableDivs[1]) );
        $(sortableDivs[1]).sortable( "option", "connectWith", $(sortableDivs[0]) );
    }, 1000);

}