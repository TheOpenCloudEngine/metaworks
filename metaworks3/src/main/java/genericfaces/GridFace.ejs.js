var GridFace = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];

	var metadata = mw3.getMetadata(this.className);
	
	this.showCheckBox = !metadata.getOptionValue('hideCheckBox', false);
	this.platMode = metadata.getOptionValue('platMode', false);
	this.popupWidth = metadata.getOptionValue('popupWidth', 300);
	this.callbackAddBtn = metadata.getOptionValue('callbackAddBtn', null);
	this.callbackConfirmBtn = metadata.getOptionValue('callbackConfirmBtn', null);
	
	if(this.object == null)
		return true;
	
	this.objectDiv
		.addClass('mw3_grid')
		.bind('loaded', {objectId: this.objectId}, function(event){
			mw3.getFaceHelper(event.data.objectId).unbind();
			mw3.getFaceHelper(event.data.objectId).refreshOrder();
			mw3.getFaceHelper(event.data.objectId).bind();
		});
	
};

GridFace.prototype = {
	getValue : function(){
		
		for(var i=0; i<this.object.length; i++){
			if(!this.object[i].metaworksContext)
				this.object[i].metaworksContext = {};
			
			this.object[i].metaworksContext.how = null;
		}
		
		var checked = [];
		if(this.showCheckBox){
			checked = this.objectDiv.find('[name=check_' + this.objectId + ']:checked');
		}else{
			checked = this.objectDiv.find('tr.selected');
		}
		
		for(var i=0; i<checked.length; i++){
			var checkedObjectId = $(checked[i]).attr('objectId');
			
			var item = mw3.getObject(checkedObjectId);
			item.metaworksContext.how = 'checked';
		}
		
		return this.object;
		
	},
	
	add : function(object){
		var options = mw3.getMetadata(object.__className);
		
		options['htmlTag'] =  'tr';
		options['ejsPath'] =  'dwr/metaworks/genericfaces/GridFace_Row.ejs';
		options['parentId'] =  this.objectId;
		
		var html = mw3.locateObject(object, object.__className, null, options);
		
		this.object.push(object);
		this.objectDiv.children('table').append(html);
		
		mw3.onLoadFaceHelperScript();
	},
	
	bind : function(){
		
		if(!this.showCheckBox){
			$('#listitem_' + this.objectId + ' tbody tr').bind('click', {objectId: this.objectId}, function(event){
				$(this).siblings().removeClass('selected').end().addClass('selected');
			});
		}
		
		/*var itemId = $(this).parent().attr('objectId');
		var itemObject = mw3.clone(mw3.getObject(itemId));
		itemObject.metaworksContext.when = mw3.WHEN_EDIT;

		$('#edititem_' + event.data.objectId).html(mw3.locateObject(itemObject, itemObject.__className));*/

	},
	
	unbind : function(){
		$('#listitem_' + this.objectId + ' tbody tr td').unbind();
	},
	
	addItem : function(){
		this.unbind();
		
		var metadata = mw3.getMetadata(this.className);
		
		var addObject = null;
		
		if(!this.platMode){
			var addObject = {
				__className : this.className,
			};
			
			var confirmDivId = 'confirm_' + this.objectId;
			var metadata = mw3.getMetadata(this.className);
			var confirm = mw3.localize('적용');
			var cancel = mw3.localize('취소');
			var displayName = metadata.displayName + ' ' + mw3.localize('추가');
			
			$('body').append("<div id='" + confirmDivId + "'></div>");
			
			var options = {when : 'edit'};
			mw3.locateObject(addObject, null, '#' + confirmDivId, options);
			
			if(this.callbackAddBtn){
				if(metadata.serviceMethodContextMap[this.callbackAddBtn].target == 'none'){
					addObject = mw3.call(addObject.__objectId, this.callbackAddBtn);
					
					$('#' + confirmDivId).empty();
					
					mw3.locateObject(addObject, null, '#' + confirmDivId, options);
					
					var addMetadata = mw3.getMetadata(addObject.__className);
					var displayName = addMetadata.displayName; 
				}else{
					mw3.call(addObject.__objectId, this.callbackAddBtn);
				}
			}else{
				addObject = mw3.clone(mw3.getObject(addObject.__objectId));
			}
			
			$('#' + confirmDivId).dialog({
				title : displayName,
				objectId : addObject.__objectId,
				parentId : this.objectId,
	            resizable: false,
	            width : this.popupWidth,
	            modal: true,
	            buttons: {
	            	confirm: function(event) {
	                	var objectId = $(this).dialog('option', 'objectId');
	                	var parentId = $(this).dialog('option', 'parentId');
	                	var parentFaceHelper = mw3.getFaceHelper(parentId);
	                	
	                	if(parentFaceHelper.callbackConfirmBtn){
	                		object = mw3.getObject(objectId);
	                		object = mw3.call(object.__objectId, parentFaceHelper.callbackConfirmBtn);
	                		mw3.locateObject(object, object.__className);
	                	}else{
	                		object = mw3.getObject(objectId);
	                		object['metaworksContext'] = {when : mw3.WHEN_VIEW};
	                	}
	                	
	                	if(mw3.validObject(object)){
		                    $( this ).dialog( "close" );                    
		                    
		                    $(mw3.getFaceHelper(parentId).add(object));
	                	}
	                },
	                cancel: function() {
	                	$( this ).dialog( "close" );
	                }
	            },
	            
	            close: function( event, ui ) {
	            	var objectId = $(this).dialog('option', 'objectId');
	            	
	            	mw3.removeObject(objectId);
	            	
	            	$( this ).dialog( "destroy" );
	            	$( this ).remove();
	            }
	        });
			
			
			
		}else{
			var editDiv = $('#edititem_' + this.objectId).children(':first');
			var editObjectId = editDiv.attr('objectid');

			
			if(this.callbackAddBtn){
				if(metadata.serviceMethodContextMap[this.callbackAddBtn].target == 'none'){
					addObject = mw3.call(editObjectId, this.callbackAddBtn);
				}else{
					mw3.call(editObjectId, this.callbackAddBtn);
				}
			}else{
				addObject = mw3.clone(mw3.getObject(editObjectId));
			}
			
			if(addObject){
				addObject.metaworksContext.when = mw3.WHEN_VIEW;
				addObject.metaworksContext.how = mw3.HOW_IN_LIST;
				
				var options = mw3.getMetadata(addObject.__className);
				options['htmlTag'] =  'tr';
				options['ejsPath'] =  'dwr/metaworks/genericfaces/GridFace_Row.ejs';
				options['parentId'] =  this.objectId;
				
				var html = mw3.locateObject(addObject, addObject.__className, null, options);
				
				if(mw3.validObject(addObject)){
					this.object.push(addObject);
					this.objectDiv.children('table').append(html);
					
					var editObject = mw3.clone(addObject);
					editObject.metaworksContext.when = mw3.WHEN_EDIT;
					editDiv.replaceWith(mw3.locateObject(editObject, editObject.__className));
					
					mw3.onLoadFaceHelperScript();
					
					this.refreshOrder();
					
					this.objectDiv.trigger('change');
				}
			}
		}
		
		this.bind();
	},

	modifyItem : function(){
		this.unbind();
		
		var editDiv = $('#edititem_' + this.objectId).children(':first');
		var editObjectId = editDiv.attr('objectid');
		
		var modifyObject = mw3.clone(mw3.getObject(editObjectId));
		modifyObject.metaworksContext.when = mw3.WHEN_VIEW;
		
		var metadata = mw3.getMetadata(modifyObject.__className);		
		
		if(metadata.keyFieldDescriptor){
			var keyFieldName = metadata.keyFieldDescriptor.name;
			
			for(var i=0; i<this.object.length; i++){
				if(this.object[i][keyFieldName] == modifyObject[keyFieldName]){
					var options = metadata;
					options['htmlTag'] =  'tr';
					options['ejsPath'] =  'dwr/metaworks/genericfaces/GridFace_Row.ejs';
					options['parentId'] =  this.objectId;
					
					$('#' + mw3._getObjectDivId(this.object[i].__objectId)).replaceWith(mw3.locateObject(modifyObject, modifyObject.__className, null, options));
					
					this.object[i] = modifyObject;
					
					break;
				}
			}
		}
	},

	remove : function(){
		this.unbind();
		
		var checked = [];
		if(this.showCheckBox){
			checked = this.objectDiv.find(':checkbox:checked');
		}else{
			checked = this.objectDiv.find('tr.selected');
		}
		
		for(var i=0; i<checked.length; i++){
			var checkedObjectId = $(checked[i]).attr('objectId');
			
			for(var j=0; j<this.object.length; j++){
				if(checkedObjectId == this.object[j].__objectId){
					if(j == 0)
						this.object.shift();
					else
						this.object.splice(j,1);	
					
					break;
				}
			}
			
			mw3.removeObject(checkedObjectId);
		}
		
		var metadata = mw3.getMetadata(this.className);
		
		if(metadata && metadata.faceOptions && metadata.faceOptions.callbackRemoveBtn){
			if(metadata.serviceMethodContextMap[metadata.faceOptions.callbackRemoveBtn].target == 'none'){
				var editDiv = $('#edititem_' + this.objectId).children(':first');
				var editObjectId = editDiv.attr('objectid');
				
				var editObject = mw3.call(editObjectId, metadata.faceOptions.callbackRemoveBtn);
				
				editObject.metaworksContext.when = mw3.WHEN_EDIT;
				editDiv.replaceWith(mw3.locateObject(editObject, editObject.__className));
			}
		}
		
		this.refreshOrder();
		this.objectDiv.trigger('change');
		
		this.bind();
	},
	
	removeItem : function(){
		
		var checked = [];
				
		if(this.showCheckBox){
			checked = this.objectDiv.find(':checkbox:checked');
		}else{
			checked = this.objectDiv.find('tr.selected');
		}
		
		if(checked.length == 0){
			alert('Please select an item to delete.');
			return false;
		}
		
		var confirmDivId = 'confirm_' + this.objectId;
		
		var html = "<div id='" + confirmDivId + "'>총 $1개를 삭제하시겠습니까?</div>";
		html = html.replace('$1', checked.length);
		
		$('body').append(html);
		
		var metadata = mw3.getMetadata(this.className);
		var confirm = mw3.localize('$Confirm');
		var cancel = mw3.localize('$Cancel');
		
		$('#' + confirmDivId).dialog({
			title : metadata.displayName + ' ' + '삭제',			
			objectId : this.objectId,
            resizable: false,
            modal: true,
            buttons: {
            	confirm: function(event) {
            		var objectId = $(this).dialog('option', 'objectId');
            		
                    $( this ).dialog( "close" );                    
                    $( this ).remove();
                    
                    $(mw3.getFaceHelper(objectId).remove());
                },
                cancel: function() {
                    $( this ).dialog( "close" );
                    $( this ).remove();
                }
            }
        });
		
		this.bind();
	},
	
	refreshOrder : function(){
		for(var i=0; i<this.object.length; i++){
			var item = this.object[i];
			
			if(item.getFaceHelper() && item.getFaceHelper().setRownum){
				item.getFaceHelper().setRownum(i+1);
			}
		}
	},
	
	toogleCheck : function(){
		var checkYn = this.objectDiv.find('[name=checkall_' + this.objectId + ']').attr('checked');
		
		if(checkYn)
			checkYn = true;
		else
			checkYn = false;
		
		this.objectDiv.find('[name=check_' + this.objectId + ']').attr('checked', checkYn);
	}	
};