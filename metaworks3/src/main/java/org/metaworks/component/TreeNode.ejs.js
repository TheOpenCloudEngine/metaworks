var org_metaworks_component_TreeNode = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];
	this.metadata = mw3.getMetadata(this.className);
	
	if(this.object == null)
		return true;	
	
	if(this.object.hidden)
		this.objectDiv.addClass('minus15');
	
	this.nodeDiv = $('#' + this.objectDivId).children('div');
	this.nodeDiv.attr('objectId', this.objectId);
	this.treeDiv = this.nodeDiv.parentsUntil('.filemgr-tree').parent('.filemgr-tree');
	
	var faceHelper = this;
	
	if(this.object.folder){
		// calc status
		var status = '';
		if(this.object.expanded)
			status = 'min';
		else
			status = 'plus';
		
		if(this.object.loaded)
			status += 'last';
		
		this.nodeDiv.addClass(status);
		
		if(faceHelper.isCollapse())
			faceHelper.collapse();
		
		this.nodeDiv.children().children('span').bind('click', {objectId : this.objectId}, function(event){
			faceHelper.action(event);
		});		
	}
	
	// add event mouse click
	this.nodeDiv.bind('click', {objectId : this.objectId}, function(event){
		mw3.getFaceHelper(event.data.objectId).select(event);
	});
	
	
	this.nodeDiv.bind('dblclick', {objectId : this.objectId}, function(event){
		mw3.getFaceHelper(event.data.objectId).action(event);
	});
	
	if( this.object.selected){
		faceHelper.select();
	}
	
};

org_metaworks_component_TreeNode.prototype = {
	loaded : function(){
		if(this.object != null){
			this.nodeDiv.triggerHandler('loaded', [this.object.id, this.objectId]);
			this.treeDiv.triggerHandler('loadedNode', [this.object.id, this.objectId]);
		}
	},
	destroy : function() {
		if(this.object != null){
			this.objectDiv.unbind();
			this.nodeDiv.unbind();
			this.nodeDiv.children().children('span').unbind();
		}
	},
	
	/*
	startLoading : function(){
		
	},
	
	endLoading : function(){
		this.nodeDiv.removeClass('loading');		
	},
	*/
	
	showStatus : function(message){
		if('expand DONE.' == message){
			this.nodeDiv.removeClass('loading');
		
			this.nodeDiv.addClass('minlast');
			this.nodeDiv.removeClass('min');
			
			this.object.loaded = true;
		}
	},
	
	draggable : function(command){
		this.objectDiv.find('.label-content:first').draggable({
			      appendTo: "body",
			      helper: function( event ) {
			          return $('<div>').addClass('filemgr-tree').append($(this).clone());
			      },
			      zIndex: 100,
				  start: function(event, ui) {
					  eval(command);
			      },
			      drag: function() {
			      },
			      stop: function() {
			      }
			    });
	},
	toAppend : function(appendobject){
		if(!this.object.expanded){
			this.object.expanded = true;
			
			mw3.call(this.objectId, 'expand');
			
			return true;
		}
		
		if(appendobject != null && ( appendobject.length == undefined || appendobject.length == 0) ){
			// array 로 생성하여 준다.
			var tempObject = [];
			tempObject[0] = appendobject;
			appendobject = tempObject;
		}
		
		if(appendobject != null && appendobject.length > 0){
			for(var i=0; i<appendobject.length; i++){
				$('<div>').appendTo(this.objectDiv.children('u').show()).html(mw3.locateObject(appendobject[i], null));
				
				this.push(appendobject[i]);				
				
				this.treeDiv.trigger('toAppended');
			}
		}else{
			this.nodeDiv.removeClass('minlast');
		}
		
		//mw3.onLoadFaceHelperScript();
	},
	
	push : function(item){
		if(this.object == null || typeof this.object == 'undefined')
			return true;
		
		var childObjectId;
		
		if(this.object.child == null || typeof this.object.child == 'undefined' || this.object.child.length == 0){
			childObjectId = ++ mw3.objectId;
			
			this.object.child = [];
			mw3.objects[childObjectId] = this.object.child;
			
			var beanName = 
			{
				'.child' : 
				{
					fieldName : '.child' ,
					valueObjectId : childObjectId
				
				}
			};

			mw3.beanExpressions[this.objectId] = beanName;
		   
		}else{
			childObjectId = mw3.beanExpressions[this.objectId]['.child'].valueObjectId;
		}

		var lastPropName = '0';
		
		if(mw3.beanExpressions[childObjectId]){
			for(var propName in mw3.beanExpressions[childObjectId]){
				lastPropName = propName.substring(1, propName.length-1);
				lastPropName = Number(lastPropName)	 + 1;
			}
		}else{
			mw3.beanExpressions[childObjectId] = {};
		}
		
		lastPropName = '[' + lastPropName + ']'; 
		
		var beanName = 
		{
			fieldName : lastPropName ,
			valueObjectId : item.__objectId
		};
		
		mw3.beanExpressions[childObjectId][lastPropName] = beanName;
		
		this.object.child.push(item);
	},
	
	isExpand : function(){
		return (this.nodeDiv.hasClass('min') || this.nodeDiv.hasClass('minlast'));
	},
	
	isCollapse : function(){
		return (this.nodeDiv.hasClass('plus') || this.nodeDiv.hasClass('pluslast'));
	},

	check : function(){
		this.nodeDiv.find('input[type=checkbox]:visible').each(function(){
			if($(this).is(':checked'))
				$(this).prop('checked', false);
			else
				$(this).prop('checked', true);
		});
	},
	select : function(event){
		if( event ){
			if(event.stopPropagation){
				event.stopPropagation();
			}else if(window.event){
				window.event.cancelBubble = true;
			}
		}
		
		if(!this.nodeDiv.hasClass('selected')){
			this.treeDiv.find('.item-fix.selected').removeClass('selected');
			this.nodeDiv.addClass('selected');

			this.treeDiv.trigger('change', [this.objectId]);			

			if(!mw3.isHiddenMethodContext(this.metadata.serviceMethodContextMap['select'], this.object)){
				 mw3.call(this.objectId, 'select');
			}
		}else{
			this.treeDiv.find('.item-fix.selected').removeClass('selected');
			this.nodeDiv.addClass('selected');			
		}
		
		this.check();
	},
	
	action : function(event){
		if(event.stopPropagation){
			event.stopPropagation();
		}else if(window.event){
			window.event.cancelBubble = true;
		}
		
		if(this.object.folder){
			if(this.isExpand())
				this.collapse();
			else if(this.isCollapse())
				this.expand();
		}else{
			// 마우스 이벤트가 걸려있지 않으면 action 을 태운다.
			if( this.objectDiv[0]['mouseCommand'] != null ){
				eval(this.objectDiv[0]['mouseCommand']);
			}else{
				mw3.call(this.objectId, 'action');
			}
		}
		
	},
	expand : function(){
		if(this.nodeDiv.hasClass('pluslast')){
			this.nodeDiv.removeClass('pluslast');
			this.nodeDiv.addClass('minlast');
		}else if(this.nodeDiv.hasClass('plus')){
			this.nodeDiv.removeClass('plus');
			this.nodeDiv.addClass('min');
		}
		
		this.object.expanded = true;
		
		if(this.object.loaded){
			this.objectDiv.children('u').show();
		}else{
			this.nodeDiv.addClass('loading');
			
			mw3.call(this.objectId, 'expand');
		}
		
		this.treeDiv.trigger('expanded');
	},
	
	collapse : function(){
		if(this.nodeDiv.hasClass('minlast')){
			this.nodeDiv.removeClass('minlast');
			this.nodeDiv.addClass('pluslast');
		}else if(this.nodeDiv.hasClass('min')){
			this.nodeDiv.removeClass('min');
			this.nodeDiv.addClass('plus');
		}
		
		this.objectDiv.children('u').hide();
		this.object.expanded = false;
		
		this.treeDiv.trigger('collapsed', [this.objectId]);
	},
	changeName : function(name){
		this.nodeDiv.find('.caption').html(name);
	}
};