var lastRowId;
var lastColId;

var org _metaworks_widget_grid_Grid= function(objectId, className){
	this.objectId = objectId;
	var grid = mw3.objects[objectId];
	
	jQuery("#grid_" + this.objectId ).jqGrid({
		datatype: "local",
		height: 250,
	   	colNames:grid.columnNames,
	   	colModel:[
	   		{name:'id',index:'id', width:60, sorttype:"int"},
	   		{name:'invdate',index:'invdate', width:90, sorttype:"date", editable:true},
	   		{name:'name',index:'name', width:100, editable:true},
	   		{name:'amount',index:'amount', width:80, align:"right",sorttype:"float", editable:true},
	   		{name:'tax',index:'tax', width:80, align:"right",sorttype:"float", editable:true},		
	   		{name:'total',index:'total', width:80,align:"right",sorttype:"float", editable:true},		
	   		{name:'note',index:'note', width:150, sortable:false, editable:true}		
	   	],
	    viewrecords: true,
	    cellEdit : true,
	    onCellSelect : function(rowid, iCol, cellcontent) {
	    	//alert("rowid : " + rowid + ", col : " + iCol + ", content : " + cellcontent);
	    	if(rowid && iCol && (rowid !== lastRowId || iCod !== lastColId)) {
	    		var cell = {
	    				__className :"org.metaworks.widget.grid.GridCell" ,
	    				row : rowid,
	    				col : iCol,
	    				content : cellcontent
	    		};
	    		grid.cell = cell;
	    		
	    		grid.data = null;
	    			
	    		grid = grid.changeCell();
	    		
	    		alert(grid.cell.contents);
	    	}
	    	lastRowId = rowid;
	    	lastColId = iCol;
	    },
		editurl: "local",
	   	caption: "Manipulating Array Data"
	});

	for(var i=0;i<=grid.data.length;i++){
		jQuery("#grid_" + this.objectId).jqGrid('addRowData',i+1,grid.data[i]);
	}
		
}


