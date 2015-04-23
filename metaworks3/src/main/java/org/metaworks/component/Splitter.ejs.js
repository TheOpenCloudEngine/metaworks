var org_metaworks_component_Splitter = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.object = mw3.objects[this.objectId];

	if(this.object == null)
		return true;
	
	$('#' + mw3._getInfoDivId(this.objectId)).remove();
	
	this.objectDiv.addClass('splitter');
	this.objectDiv.css({'background-color': 'black',
						'position': 'absolute', 
						'min-width': '0px',
						'min-height': '0px',
						'max-width': '10000px',
						'max-height': '10000px',  
						'right': '0px', 						
						'bottom': '0px'});
	
	this.A = this.objectDiv.prev();	// left  or top
	this.B = this.objectDiv.next();	// right or bottom
	
	if(this.object.type == 'vertical'){	
		this.objectDiv.css({'top': '0px',
			'left' : this.A.css('width'),
			'width' : this.object.size + 'px'});

		this.opts = {
			origin : 'left',
			eventPos : 'pageX',
			pxSplit : 'offsetWidth',
			pxFixed: "offsetHeight",
			split: "width",
			fixed: 'height',
			side1: "left", 
			side2: "right",
			side3: 'top'
			};	
	}else{
		this.objectDiv.css({'top': this.A.css('height'),
			'left' : '0px',
			'height' : this.object.size + 'px'}); 

		this.opts = {
			origin : 'top',
			eventPos : 'pageY',
			pxSplit : 'offsetHeight',
			pxFixed: "offsetWidth",
			split: "height",
			fixed: 'width',
			side1: "top", 
			side2: "bottom",
			side3: 'left'
			};			

	}

	this.objectDiv.attr('unselectable', 'on');
	this.objectDiv.css({'user-select': 'none', 
						'-webkit-user-select': 'none',
						'-khtml-user-select': 'none', 
						'-moz-user-select': 'none'});
	
	this.objectDiv.bind('mousedown', {objectId : this.objectId}, function(event){
		mw3.getFaceHelper(event.data.objectId).startSplitMouse(event);
	});
	
	$(window).bind("resize", {objectId : this.objectId}, function(event){
		mw3.getFaceHelper(event.data.objectId).resize(event);
	});
	
	
	// toggle setting
	this.splitterToggle = this.objectDiv.children('.splitterToggle');
	this.splitterToggle.css(this.opts.split,'100%').css(this.opts.fixed,'10%');
	this.splitterToggle.bind('mousedown', {objectId : this.objectId}, function(event){
		mw3.getFaceHelper(event.data.objectId).toggle(event);
	});
	
	if(this.opts.origin == this.object.align)
		this.A._min = this.dimSum(this.A, "min-" + this.opts.split);
	else
		this.B._min = this.dimSum(this.B, "min-" + this.opts.split);
	
	this.resize();
	this.resplit(this.A[0][this.opts.pxSplit]);
};

org_metaworks_component_Splitter.prototype = {
		destroy : function(){
			this.objectDiv.unbind('mousedown');
		},
		dimSum : function (jq, dims) {
			var sum = 0;
			for ( var i=1; i < arguments.length; i++ )
				sum += Math.max(parseInt(jq.css(arguments[i])) || 0, 0);
			
			return sum;
		},		
		startSplitMouse : function (event) {
			if(this.opts.origin == this.object.align){
				this.A._posSplit = this.A[0][this.opts.pxSplit] - event[this.opts.eventPos];
			}else
				this.A._posSplit = this.A.offset()[this.opts.origin] + this.A[0][this.opts.pxSplit] + this.B.offset()[this.opts.origin] - event[this.opts.eventPos] + this.B[0][this.opts.pxSplit];
			
			if (console && console.log) console.log('this.A.offset()[this.opts.origin]' + this.A.offset()[this.opts.origin]);
			if (console && console.log) console.log('this.B.offset()[this.opts.origin] : ' + this.B.offset()[this.opts.origin]);
			if (console && console.log) console.log('this.A[0][this.opts.pxSplit] : ' + this.A[0][this.opts.pxSplit]);
			if (console && console.log) console.log('event[this.opts.eventPos] : ' + event[this.opts.eventPos]);
			if (console && console.log) console.log('this.B[0][this.opts.pxSplit] : ' + this.B[0][this.opts.pxSplit]);
			
			this.A.css('-webkit-user-select', 'none');
			this.B.css('-webkit-user-select', 'none');
			
			$(document)
				.bind('mousemove', {objectId : this.objectId}, function(event){
					mw3.getFaceHelper(event.data.objectId).doSplitMouse(event);
				})
				.bind('mouseup', {objectId : this.objectId}, function(event){
					mw3.getFaceHelper(event.data.objectId).endSplitMouse(event);
				});
		},
		doSplitMouse : function (event) {
			var newPos;
			
			if (console && console.log) console.log('this.A._posSplit : ' + this.A._posSplit);
			if (console && console.log) console.log('event[this.opts.eventPos] : ' + event[this.opts.eventPos]);
			
			if(this.opts.origin == this.object.align)
				newPos = this.A._posSplit+event[this.opts.eventPos];
			else
				newPos = this.A._posSplit-event[this.opts.eventPos];
			
			if (console && console.log) console.log('move : ' + this.A._posSplit + ' : ' + event[this.opts.eventPos]);
			
			this.resplit(newPos);
			
			//var newPos = A._posSplit+evt[opts.eventPos];
			//if ( opts.outline ) {
			//	newPos = Math.max(0, Math.min(newPos, splitter._DA - bar._DA));
			//	bar.css(opts.origin, newPos);
			//} else 
			//	resplit(newPos);
		},
		endSplitMouse : function (event) {
			this.A.css('-webkit-user-select', 'text');
			this.B.css('-webkit-user-select', 'text');
			
			$(document)
				.unbind('mousemove')
				.unbind('mouseup');
		},
		
		resplit : function(newPos){
			if(this.opts.origin == this.object.align){
				newPos = Math.max(this.A._min, this.object.min, Math.min(newPos, this.max));
				
				this.A.css(this.opts.split, newPos);
				this.B.css(this.opts.side1, newPos + this.object.size);
				this.objectDiv.css(this.opts.side1, this.A[0][this.opts.pxSplit]);
			}else{
				if (console && console.log) console.log(newPos + ' : ' + this.max);
				
				newPos = Math.max(newPos, this.B._min, this.object.min, Math.min(newPos, this.max));
				
				this.B.css(this.opts.split, newPos);
				this.A.css(this.opts.side2, newPos + this.object.size);
				this.objectDiv.css(this.opts.side1, this.A[0][this.opts.pxSplit]);							
			}
		},
		
		toggle : function(event){
			if(event.stopPropagation){
				event.stopPropagation();
			}else if(window.event){
				window.event.cancelBubble = true;
			}
			
			var status = 'undock';
			var minPos;
			
			if(this.opts.origin == this.object.align){
				minPos = Math.max(this.A._min, this.object.min);
				
				if(this.A[0][this.opts.pxSplit] == minPos)
					status = 'dock';
			}else{
				minPos = Math.max(this.B._min, this.object.min);
				
				if(this.B[0][this.opts.pxSplit] == minPos)
					status = 'dock';				
			}
			
			if(status == 'dock')
				this.undock();
			else
				this.dock();
		},
		dock : function(){
			var newPos = 0;
						
			if(this.opts.origin == this.object.align)
				this.A._posSplit = this.A[0][this.opts.pxSplit];
			else
				this.A._posSplit = this.B[0][this.opts.pxSplit];
			
			this.resplit(newPos);
		},
		undock : function(){
			var newPos = this.A._posSplit;
	
			if(typeof newPos != 'undefined')
				this.resplit(newPos);
		},
		resize : function(){
			if(this.opts){
				if(this.opts.origin == this.object.align){
					this.objectDiv.css(this.opts.side1, this.A[0][this.opts.pxSplit]);
				}else{
					this.objectDiv.css(this.opts.side1, this.A[0][this.opts.pxSplit]);
				}
				
				this.max = this.A[0][this.opts.pxSplit]  + this.B[0][this.opts.pxSplit];
				
				this.splitterToggle.css(this.opts.side3, this.objectDiv[0][this.opts.pxFixed] / 2 - (this.splitterToggle[0][this.opts.pxFixed] / 2) + 'px');
			}
		}
};
