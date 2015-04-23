var ArrayFace = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.object = mw3.objects[this.objectId];
	this.objectDivId = mw3._getObjectDivId(this.objectId);
}

ArrayFace.prototype = {
	getValue: function(){
		var toBeReturned = [];
		if(this.object){
			var j=0;
			for(var i=0; i<this.object.length; i++){
				if(this.object[i] && this.object[i].__objectId){
					var element = mw3.getObject(this.object[i].__objectId);
					if(element){
						toBeReturned[j++] = element;
					}
				}
			}
		}

		return toBeReturned;
	},
	addNew : function(){
		var arrayObj = mw3.getObject(this.objectId);
		var newOne = {__className: this.className, metaworksContext: {when: mw3.WHEN_NEW}};

		this.add(newOne);
	},

	add : function(item){
		var arrayObj = mw3.getObject(this.objectId);
		arrayObj[arrayObj.length] = item;

		mw3.locateObject(
			item,
			item.__className,
			'#' + this.objectDivId,
			null,
			{
				objectId: this.objectId,
				name: '[' + (arrayObj.length-1).toString() + ']'
			}
		);
	},

	remove: function(item){
		var removeItemObjectKeys = mw3._createObjectKey(item, true);

		var pos = this.indexOf(removeItemObjectKeys);

		if(pos == 0 && pos == this.object.length-1){
			this.object = [];
		}else if(pos == this.object.length-1){
			this.object.pop();
		}else if(pos == 0){
			this.object.shift();
		}else{
			this.object.splice(pos,pos);
		}
	},

	contains : function(item){
		return this.indexOf(item) > -1;
	},

	indexOf : function(item){
		var targetObjectKeys = mw3._createObjectKey(item, true);

		for(var i=0; i<this.object.length; i++){
			if(this.compareKey(targetObjectKeys, this.object[i])){
				return i;

				break;
			}
		}

		return -1;
	},

	compareKey : function(targetObjKeys, compareObject){

		if(targetObjKeys && targetObjKeys.length){
			var matchKeyCnt = 0;

			matchKeyCnt = targetObjKeys[0].split('@').length;

			for(var i=0; i<targetObjKeys.length; i++){
				if(matchKeyCnt > targetObjKeys[i].split('@').length)
					break;

				if(!compareObject ||
					Object.prototype.toString.call(compareObject) === '[object String]' ||
					Object.prototype.toString.call(compareObject) === '[object Array]')
					continue;

				var compareObjKey = mw3._createObjectKey(compareObject);
				if(targetObjKeys[i] == compareObjKey){
					return true;
				}
			}
		}
	}

}