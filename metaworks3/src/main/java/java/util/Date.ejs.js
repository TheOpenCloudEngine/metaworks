var java_util_Date = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	
	this.inputId = mw3.createInputId(objectId);
	this.divObj = $('#' + this.inputId);
		
	var when = this.divObj.attr('when');
	if(when == 'edit' || when == 'new'){
		this.divObj.datepicker({
			monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],		// 월 한글로 출력
			dayNamesMin: ['일','월','화','수','목','금','토'],		// 요일 한글로 출력
		  	dateFormat: 'yy-mm-dd', //데이터 포멧형식
		  	showMonthAfterYear: true
		});	
		
		this.divObj.datepicker("setDate", mw3.objects[this.objectId]);
	}
}

java_util_Date.prototype = {
	getValue : function(){
		var when = this.divObj.attr('when');
		if(when == 'edit' || when == 'new'){
			return this.divObj.datepicker( "getDate" );
		}else{
			return mw3.objects[this.objectId];
		}
	},
	clear : function(){
		mw3.objects[this.objectId] = null;
		this.divObj.datepicker("setDate", null);
	}
}