var org_uengine_codi_mw3_model_Popup = function(objectId, className) {
	this.objectId = objectId;
	this.className = className;
	this.divId = "#objDiv_" + this.objectId;
	this.divObj = $("#objDiv_" + this.objectId).closest('.target_popup');
	
	var modalWindow = $('.ui-dialog');
	
	var zIndex = 100;
	
	if(modalWindow.length > 0){
		zIndex = $(modalWindow[modalWindow.length-1]).css('z-index');
		zIndex = String(Number(zIndex)+1);
	}		
	
	this.divObj.addClass("mw3_popup").attr("objectid", objectId).addClass("clue-right-rounded").addClass("cluetip-rounded").css({position:'absolute','z-index':zIndex,display:'none'});
	
	var object = mw3.objects[this.objectId];
	
	//var openerDiv = $("#objDiv_" + mw3.recentOpenerObjectId);
//	var x = openerDiv.offset().left + (openerDiv.children()[0].offsetWidth);
//	var y = openerDiv.offset().top;
	
	var faceHelper = this;
	faceHelper.createPopup(object.width, object.height, mw3.mouseX, mw3.mouseY);
	
	//faceHelper.bind();
};

org_uengine_codi_mw3_model_Popup.prototype = {
	createPopup : function(w,h,x,y){
	
		var session = mw3.getAutowiredObject("org.uengine.codi.mw3.model.Session");
		var ux = '';
		
		if(session)
			ux = session.ux;
		
		var popLayerWidth = w;
		var popLayerHeight = h;
		
		var bodyHeight = $('body').height();
		var bodyWidth = $('body').width();
		
		var divId = '#objDiv_' + this.objectId;
		
		var arrow = $(divId + ' .cluetip-arrows');
		
		var innerHeight = h-39;	
		
		if('phone' == ux){
			this.divObj.find('.cluetip-outer').css('width', '100%');
		
			this.divObj.css({'top':'10px','left':'10px','width':bodyWidth-50,'height':bodyHeight-30});	
			$(divId + ' #addcontact-con').height(bodyHeight-70);
			arrow.css({'display':'none'});		
		}else{
			this.divObj.css({top:y-28,left:x,width:popLayerWidth,height:popLayerHeight});			
			$(divId + ' #addcontact-con').height(innerHeight);
			arrow.css({top:10});	
		}
		
		this.divObj.removeClass('clue-left-rounded');
		this.divObj.addClass('clue-right-rounded');
		
		
		if('phone' != ux){
		
			if(bodyWidth && popLayerWidth + x > bodyWidth){
				this.divObj.removeClass('clue-right-rounded');
				
				if(x-30 - popLayerWidth < 0){
					this.divObj.css({left:0});
				}else{
					this.divObj.css({left:x-30 - popLayerWidth});
					this.divObj.addClass('clue-left-rounded');
				}
			};
			
			if(bodyHeight && popLayerHeight + y > bodyHeight){    	
				this.divObj.css({top:bodyHeight - popLayerHeight - 18});
				arrow.css({top:popLayerHeight-40});
			};
		}
		
		var top = this.divObj.css('top');
		top = top.substring(0, top.length - 2);
		
		if(top < 0){
			var arrowTop = arrow.css('top');			
			
			this.divObj.css('top', 0);
			arrow.css({top:popLayerHeight+Number(top)-40});
		}

		this.divObj.show();
	},
	destoryPopup : function() {
		mw3.endLoading(this.objectId);
		this.divObj.remove();
	},
	destroy : function() {
		this.destoryPopup();
	}
};