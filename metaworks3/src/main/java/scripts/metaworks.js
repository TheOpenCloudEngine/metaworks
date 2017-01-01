var Metaworks3 = function(errorDiv, dwr_caption, mwProxy){

				this.useLocalStorage = true;

				if(localStorage && localStorage["useLocalStorage"]){
					this.useLocalStorage = (localStorage["useLocalStorage"] == "true");
				}



				this.fn = {};
				
				this.metaworksMetadata = new Array();
				this.metaworksProxy = mwProxy;
				this.errorDiv = errorDiv;
				this.objectId = 0;
				this.dwrErrorDiv = dwr_caption;
				
				this.templates = {};
				
				this.HOW_NORMAL = "normal";
				this.HOW_STANDALONE = "standalone";
				this.HOW_IN_LIST = "inList";
				this.HOW_MINIMISED = "minimised";
				this.HOW_EVER = "however";
				this.how = this.HOW_EVER;
				
				this.WHEN_VIEW = "view";
				this.WHEN_EDIT = "edit";
				this.WHEN_NEW = "new";
				this.WHEN_READONLY = "readonly";
				this.WHEN_EVER = "whenever";
				this.when = this.WHEN_EVER;
				
				this.WHERE_MOBILE = "mobile";
				this.WHERE_PC = "pc";
				this.WHERE_EVER = "wherever";
				this.where = this.WHERE_EVER;
				
				this.MEDIA_PC = "pc";
				this.MEDIA_MOBILE = "mobile";
				this.MEDIA_EVER = "wherever"
				this.media = this.MEDIA_PC;
				
				this.DEVICE_MOBILE = "mobile";
				this.DEVICE_PC = "pc";
				this.DEVICE_EVER = "wherever";
				this.device = this.DEVICE_PC;
				
				this.SCROLL_OPTION_1 = "1";
				this.SCROLL_OPTION_N = "n";
				this.scrollOption= this.SCROLL_OPTION_1;  //N...
				this.MESSAGE_LOADING = '...  LOADING PROPERTY ...';
				
				this.needToConfirmMessage = null;
						
				this.base = "";
				
				this.objects = {};
				this.objectContexts = {};
				this.beanExpressions = {};
				
				this.objectId_KeyMapping = {};
				this.objectId_ClassNameMapping = {};
				
				this.face_ObjectIdMapping = {};
				this.objectIds_FaceMapping = {};
				
				this.loadedObjectIds=[];
				
				this.loaded = false;
				this.loadedScripts = {};				
				
				this.tests = {};
				
				this.optimizeObjectMemory = false;

				this.debugMode = false;

				this.debugPoint;

				
				this.afterCall = function(methodName, result){
					
				};

				
				this.targetObjectId;
				
				this.faceHelpers = {};
				
				this.mouseX = 0;
				this.mouseY = 0;
				
				this._metadata_version = 0;

				this.recentCallMethodName = null;
				this.recentCallObjectId = null;
				
			    this.popupDivId;
			    this.recentOpenerObjectId = [];
			    
			    this.browser = this.browserCheck();
			    	
			    this.afterLoadFaceHelper = {};
			    this.afterLoadFaceHelperCount = 0;
			    this.loadFaceHelperStatus = 'ready';
			    
			    this.isRecordingSession = false;
			    this.recording = [];
			    
			    this.recordingExceptClasses = {};
			    
			    this.testSet = {};
			    
			    this.dragging = false;
			    this.dragStartX = 0;
			    this.dragStartY = 0;


				this.alwaysSubmittedClasses = {};
			    
			    /*
			     * metaworks service array
			     */
			    this.metaworksServices = [];

				//for storing flags that @ServiceMethod(onLoad) has been done for each object.
				this.objectLoaded = {};
			    
			    // Netscape
			    // 5.0 (Windows NT 6.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.79 Safari/535.11
			    // Mozilla
			    // Win32 
			    
			    // Microsoft Internet Explorer
			    // 9 : 5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)
			    // 8 : 4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; Tablet PC 2.0; .NET4.0C)
			    // 7 : 4.0 (compatible; MSIE 7.0; Windows NT 6.1; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; Tablet PC 2.0; .NET4.0C)

			    // Mozilla
			    // Win32 

			    // ie6~ie8 : addachEvent
			    // other   : addEventListener
			    var mouseUp = function(e){
			    	mw3.mouseX = e.pageX;
	    			mw3.mouseY = e.pageY;
	    			
	    			mw3.dragStartX = 0;
   			 		mw3.dragStartY = 0;
   					
   					if(mw3.dragObject && mw3.dragging){
   						$(mw3.dragObject).trigger('dragend');
   						
   						if(mw3.dragObject['dropCommand']){
   							mw3.dragObject['dropCommand'] = null;
   						}
   						
   						var objectId = mw3.dragObject['objectId'];
   						if(objectId && mw3.objects[objectId]){
   							var typeName = mw3.objects[objectId].__className;
   	 						$(".onDrop_" + typeName.split('.').join('_')).css("border-width", "").css("border","");		
   						}
   					}

 					mw3.dragObject = null;
 					mw3.dragging = false;
 					
 					if($("#__dragGuide").length > 0)
 						$("#__dragGuide").hide();

			    };
			    
			    this.dragObject = null;
			    this.dragGuide = $("#__dragGuide");
			    
			    var mouseMove = function(e){
			    	var mouseX = e.pageX;
	    			var mouseY = e.pageY; 
	    			
// 					if(mw3.dragObject && (
// 							!(mw3.dragObject.style.left > mouseX-10 && mw3.dragObject.style.left < mouseX+10) || 
// 							!(mw3.dragObject.style.top > mouseY-10 && mw3.dragObject.style.top > mouseY +10)|| 
// 							mw3.dragObject.style.left + mw3.dragObject.style.width < mouseX ||
//  							mw3.dragObject.style.top + mw3.dragObject.style.height < mouseY
// 						)){
   			 		if( (mw3.dragStartX != 0 && (mouseX > mw3.dragStartX + 30 || mouseX < mw3.dragStartX - 30 )) || 
   			 			(mw3.dragStartY != 0 && (mouseY > mw3.dragStartY + 30 || mouseY < mw3.dragStartY - 30) )){
 						
 						if(!mw3.dragging){
 	 						if(console) console.log('drag  start');
 							 	 				
 	 						$(mw3.dragObject).trigger('dragstart');
 	 						
 							if(!$("#__dragGuide")[0]){
 								$('body').append("<div id='__dragGuide' style='position:absolute;top:100px;left:100px;background=#000000;width=40px;height=20px;z-index:999999'></div>");
 							}
 							
 	 						$("#__dragGuide").html(mw3.dragObject.innerHTML);

 	 						if(mw3.dragObject['dragCommand']){
 	 							eval(mw3.dragObject['dragCommand']);
// 	 							mw3.dragObject['dragCommand'] = null;
 	 						}
 	 						
 	 						mw3.dragging = true;
 	 						
 	 						var objectId = mw3.dragObject['objectId'];
 	 						if(objectId && mw3.objects[objectId]){
 	 							var typeName = mw3.objects[objectId].__className;
 	 	 						$(".onDrop_" + typeName.split('.').join('_')).css("border-width", "3px").css("border-style", "dashed").css("border-color", "orange");
 	 						} 							
 						}
 						
						$("#__dragGuide").css("left", mouseX+1);
 						$("#__dragGuide").css("top",mouseY);
 						$("#__dragGuide").show();

 						
 						
/* 						*/
 						
 						//mw3.dragGuide.show();
 						
// 						mw3.dragObject.style.position='absolute';
// 						mw3.dragObject.style.top = mw3.mouseX; 
// 						mw3.dragObject.style.left = mw3.mouseY;
 						
 					}
			    };
			    			    
			    
			    var keyUp = function(e){
	    			//console.debug(e.keyCode);
	    			
	    			// ESC
	    			if(e.keyCode == 27){
		    			if(mw3.popupDivId!=null){
	    					e.preventDefault();

	    					$("#" + mw3.popupDivId).remove();
	    					mw3.popupDivId = null;
	    					mw3.dragging = false;
	    					
	    					if(mw3.dragObject){
	 	 						var objectId = mw3.dragObject['objectId'];
	 	 						if(objectId && mw3.objects[objectId]){
	 	 							var typeName = mw3.objects[objectId].__className;
	 	 	 						$(".onDrop_" + typeName.split('.').join('_')).css("border-width", "").css("border","");		
	 	 						}
	    					}
	    					$("#__dragGuide").hide();
	    					
		    			}
		    			
	    			}
	    			
	    			if(e.keyCode == 84 && e.shiftKey && e.ctrlKey){
	    				if (console && console.log) console.log(JSON.stringify(mw3.templates));
	    			}
	    			
	    			if(e.keyCode == 123 && e.shiftKey){ //F12 -- let the recorder starts for testing automation
	    				
		    			if(mw3.isRecordingSession){
		    				
		    				if (console && console.log) console.log(JSON.stringify(mw3.recording));
		    				alert('Recording Done. See the logs for recorded JSON.');
		    				this.recording=[];
		    				mw3.isRecordingSession = false;
		    				
		    			}else{
		    				mw3.isRecordingSession = true;
		    				alert("Recording Started.");
		    			}
	    			}

	    			if((e.keyCode == 122 || e.keyCode == 121) && e.shiftKey){ //F11 -- tester prompt 
	    				
	    				var testJSON = prompt("Enter testing JSON:");
	    				mw3.testSet["__test__"] = eval(testJSON);
	    				
	    				if(e.keyCode == 122)
	    					mw3.startTest("__test__");
	    				else
	    					mw3.startTest("__test__", {guidedTour: true});
	    				
	    			}
			    }
			    
			    // eventListener 없을때 (ie6~ie8) 처리
			    if(document.addEventListener){
			    	document.addEventListener("mouseup",mouseUp,false);
			    	document.addEventListener("mousemove",mouseMove,false);
			    	document.addEventListener("keyup",keyUp,false);
			    }
			    
			    if(document.attachEvent){
			    	document.attachEvent("mouseup",mouseUp);
			    	document.attachEvent("mousemove",mouseMove);			    	
			    	document.attachEvent("keyup",keyUp);
			    }
			}

			Metaworks3.prototype.debug = function(argument, when){
				if(arguments.length > 1){					
					if(eval(when))
						$('#'+this.errorDiv).html("debugPoint: "+ argument)
				}else if(console)
					console.log('debugPoint: '+ argument);
				else
					alert('debugPoint: '+ argument);
			}

			
			Metaworks3.prototype.setBase = function(base){
				this.base = base;
			}
			
			Metaworks3.prototype.loadFaceHelper = function(objectId, actualface){
				//console.log('loadFaceHelper : ' + actualface);

//				if(!mw3.objects[objectId]){
//					return null;
//				}

//				var clsName = mw3.objects[objectId].__className;
//				
//				return this.loadFaceHelperWithClsName(clsName);
//				
//			}
//			
//			Metaworks3.prototype.loadFaceHelperWithClsName = function(clsName){

				/*
				 * 2012-04-05 cjw 임시 주석 처리
				 */
				/*
				if(this.objects[objectId]==null)
					return null;
				*/

				if(!this.face_ObjectIdMapping[objectId])
					return null;

				var face = null;
				var className = null;


				for(var i=0; i<this.face_ObjectIdMapping[objectId].length; i++){
					if(this.face_ObjectIdMapping[objectId][i].face == actualface){
						face = this.face_ObjectIdMapping[objectId][i].face;
						className = this.face_ObjectIdMapping[objectId][i].className;

						break;
					}
				}

				// load faceHelper
				var faceHelperClass = this.loadedScripts[face];

				if(typeof faceHelperClass == 'undefined')
					return false;
				else if(faceHelperClass == null)
					return true;

				//try @ServiceMethod(onLoad=true)
				if(this.objects[objectId]!=null && !this.objectLoaded[objectId]){
					try{

						var objectMetadata = mw3.getMetadata(className);

						for(var methodName in objectMetadata.serviceMethodContexts) {
							var methodContext = objectMetadata.serviceMethodContexts[methodName];

							if (methodContext.onLoad && !mw3.isHiddenMethodContext(methodContext, mw3.objects[objectId])) {
								mw3.objectLoaded[objectId] = true;

								mw3.call(objectId, methodContext.methodName, false, false
									, function(objId, result){
										mw3.objectLoaded[objId] = true;
										if(result && result.__objectId){
											mw3.objectLoaded[result.__objectId] = true;
										}
									}
								);

							}
						}

					}catch(e){
						console.log("Failed to invoke @ServiceMethod(onload) for "+ className +"(objectId:" + objectId + ")\n" + e.message);
					}
				}
				//else{
				//	mw3.loaded[objectId] = null;
				//}

				var thereIsHelperClass = false;
				try{
					//console.debug('eval faceHelper [' + objectId + '] -> ' + face);
					eval(faceHelperClass);
					thereIsHelperClass = true;

					if(thereIsHelperClass){
						try{
							var faceHelper = eval("new " + faceHelperClass + "('" + objectId + "', '"+ className + "')");

							if(faceHelper){
								this.faceHelpers[objectId] = faceHelper;

								if(faceHelper && faceHelper.loaded){
									faceHelper.loaded();
								}

								if(this.objects[objectId]!=null)
									this.objects[objectId]['__faceHelper'] = faceHelper;

								return true;
							}
						}catch(faceHelperLoadException){
							//TODO:
							var errMsg = "";

							if(faceHelperLoadException.lineNumber)
								errMsg += '(line ' + faceHelperLoadException.lineNumber + ')';

							errMsg += ' : ' + faceHelperLoadException.message;

							errMsg += ". Error when to intialize the faceHelper [" + faceHelperClass + "]. Detail error message is " + errMsg;


							throw new Error(errMsg + " : Stack is "+ faceHelperLoadException.stack);

						}
					}

				}catch(e){
					console.log("Error when to load facehelper [" + faceHelperClass + "] :");
					console.log(e);
				}
			}
			
			
			Metaworks3.prototype.onLoadFaceHelperScript_toBe = function(){

				if(mw3.loadedObjectIds.length > 0)
				for(var i=mw3.loadedObjectIds.length - 1; i>=0; i--){
				
					var objectIdAndFace = mw3.loadedObjectIds[i];
					
					var objectId = objectIdAndFace.objectId;
					var face = objectIdAndFace.face;

					if(mw3.objects[objectId]){
							
							var targetElement = document.getElementById(mw3._getObjectDivId(objectId));
									
							var objectContext = mw3.objectContexts[objectId];
							
							// object attr apply
							var htmlAttr = (objectContext && objectContext.__options && objectContext.__options['htmlAttr'] ? objectContext.__options['htmlAttr'] : null);
							
							if(htmlAttr)
								$(targetElement).attr(htmlAttr);
							
							var htmlAttrChild = (objectContext && objectContext.__options && objectContext.__options['htmlAttrChild'] ? objectContext.__options['htmlAttrChild'] : null);
							if(htmlAttrChild){
								$(targetElement).children(':first').attr(htmlAttrChild);
							}
							
							if(mw3.loadFaceHelper(objectId, face)){

								var object = mw3.objects[objectId];

								if(object!=null && object.__className){
				        			mw3.serviceMethodBinding(objectId, object.__className);
								}
							}
					}
						
		    	}

		    			
			};	

			Metaworks3.prototype.onLoadFaceHelperScript = function(){

				
				if(this.loadFaceHelperStatus != 'ready'){
					this.loadFaceHelperStatus = 'more';
					
					return;
				}

				this.loadFaceHelperStatus = 'process';
				
		    	for(var i in mw3.afterLoadFaceHelper){
		    		var face = mw3.afterLoadFaceHelper[i];
		    		
		    		if(face != null){
			    		objectIds = mw3.objectIds_FaceMapping[face];								    		
						
						//console.log(face);
						//console.log(objectIds);
						
						for(var objectId in objectIds){
							//console.log('in : ' + objectId);
							
							var targetElement = document.getElementById(mw3._getObjectDivId(objectId));
							
							/*
							if(targetElement == null || targetElement.innerHTML == mw3.MESSAGE_LOADING)
								break;
							*/
									
							var objectContext = mw3.objectContexts[objectId];
							
							// object attr apply
							var htmlAttr = (objectContext && objectContext.__options && objectContext.__options['htmlAttr'] ? objectContext.__options['htmlAttr'] : null);
							
							if(htmlAttr)
								$(targetElement).attr(htmlAttr);
							
							var htmlAttrChild = (objectContext && objectContext.__options && objectContext.__options['htmlAttrChild'] ? objectContext.__options['htmlAttrChild'] : null);
							if(htmlAttrChild){
								$(targetElement).children(':first').attr(htmlAttrChild);
							}
							
							if(mw3.loadFaceHelper(objectId, face)) {
								mw3.afterLoadFaceHelper[i] = null;
								mw3.objectIds_FaceMapping[face] = null;

							}

							var object = mw3.objects[objectId];

							if(object!=null && object.__className){
								var metadata = mw3.getMetadata(object.__className);

								// optimizing the object size

								if(object!=null && this.optimizeObjectMemory && metadata){

									for(var i in metadata.fieldDescriptors){
										var fd = metadata.fieldDescriptors[i];

										if(!fd.isKey && (!fd.attributes || !fd.attributes['keepAtClient']) && object[fd.name]!=null){
											object[fd.name] = null; //remove after rendering for memory saving.
										}
									}
								}

								//console.log(object.__className);
								mw3.serviceMethodBinding(objectId, object.__className);
							}

						}
		    		}
		    	}		
		    	
		    	if(this.loadFaceHelperStatus == 'more'){
		    		this.loadFaceHelperStatus = 'ready';
		    		
		    		this.onLoadFaceHelperScript();
		    	}else{
		    		this.loadFaceHelperStatus = 'ready';
		    	}
		    			
			};			
			
			Metaworks3.prototype.getFaceHelper = function(objectId){
				var registeredHelper = this.faceHelpers[objectId];
				
				return registeredHelper;
//				
//		TODO: 맨밖에서 부터 호출되게만 된다면 문제없음...
				
//				if(registeredHelper!=null)
//					return registeredHelper;
//				else{
//					this.loadFaceHelper(objectId);
//					
//					return this.faceHelpers[objectId];
//				}
//				
				
			};
			


			Metaworks3.prototype.setWhere = function(where){
				this.where = where;
			};


/**
 * Set default context
 *
 * The priority of context applied - 1. 객체 자체의 컨텍스트,  2. 객체의 컨텍스트가 없는 경우 .. metaworks 의 default contex.....
 *
 * @example:
 * mw3.setWhen(mw3.WHEN_EDIT);

 mainPage = new MetaworksObject({
					__className:"Login"

				}, '#mainPage')

 * @param when
 */

			Metaworks3.prototype.setWhen = function(when){
				this.when = when;
			};

			Metaworks3.prototype.setHow = function(how){
				this.how = how;
			};
			
			Metaworks3.prototype.setMedia = function(media){
				this.media = media;
			};

			Metaworks3.prototype.setDevice = function(device){
				this.device = device;
			};

			Metaworks3.prototype.setScrollOption = function(scrollOption){
				this.scrollOption = scrollOption;
			};

/**
 * context 들을 구조체에 넣어서 세팅하는 경우...
 *
 * @example
 * setContext({when:'edit', where:'mobile'});
 * @param context
 */
			Metaworks3.prototype.setContext = function(context){
				if(context.where!=null)
					this.setWhere(context.where);
				
				if(context.when != null){
					this.setWhen(context.when);
				}
				
				if(context.how != null){
					this.how = context.how;
				}
			};
			
			Metaworks3.prototype.getContext = function(){
				return {when: this.when, where: this.where, how: this.how};
			};

/**
 * 클라이언트에 캐시된 metaworksMetadata들을 클리어함. 서버에서 메타데이터에 대한 변경 발생시에 reverse ajax호출하기도 함.
 * JRebel 등으로 클래스 구조를 동작 변경시...
 * @param objectTypeName 클리어하고자 하는 클래스명, 전체를 다 클리어 하려면 '*' 입력
 * @returns {Function}
 */
			Metaworks3.prototype.clearMetaworksType = function(objectTypeName){
				if(objectTypeName == "*"){
					this.metaworksMetadata = new Array();
					this.tests ={};
				}else
					this.metaworksMetadata[objectTypeName] = null;

				return function(){}; //for dwr dummy call
			};
			
			/**
			  *  deprecated  */
			Metaworks3.prototype.requestMetadataBatch = function(result){
				
//				var classNamesInTheObject = {};
//				
//				var collectingProperties = function(object){
//					for(var propName in object){
//						if(propName=='__className')
//							classNamesInTheObject[classNamesInTheObject.length] = object.__className;
//							
//						if(propName=='__objectId' || propName=='_prototype_') continue;
//							
//						collectingProperties(object[propName]);
//					}
//				}
				
			};
			
			/**
			  *  deprecated  */
			Metaworks3.prototype.importClasses = function(objectTypeNames){
			
				this.metaworksProxy.getMetaworksTypes(objectTypeNames, 
					{ 
		        		callback: function( webObjectTypes ){
		        			for(var i=0; i < webObjectTypes.length; i++)
		        				mw3._storeMetadata(webObjectTypes[i]);
						
		        		},

		        		async: true,
				
						timeout:10000, 
	                    
	                    errorHandler:function(errorString, exception) {
	                    } 
		    		}
				);
				
			};


			Metaworks3.prototype.checkUpdates = function(objectTypeName) {

				var version;

				Metaworks.getApplicationVersion(
					{
						callback: function (ver) {
							version = ver;
						},

						async: false,

						timeout: 10000,

						errorHandler: function (errorString, exception) {
						}
					}
				);

				if (localStorage && localStorage['version'] != version) {
					localStorage.clear(); //make it updated.
					localStorage['version'] = version;
				}
			}

			
			
			/**
			  *  메타데이터를 갖고옴... WebObjectType의 자바스크립트 객체  
			     파라미터 형식:  <className>
			     Array of String --> java.lang.String[], [Ljava.lang.String  즉, [L로 시작된 명칭은 어떤 클래스명으로된 Array 객체를 의미함.
			     
			     
			  */


			Metaworks3.prototype.getMetadata = function(objectTypeName){

				if(!objectTypeName || objectTypeName == null || objectTypeName.trim().length == 0) return;

				if(objectTypeName.length > 2 && objectTypeName.substr(-2) == '[]'){			//if array of some object type, use ArrayFace with mapped class mapping for the object type.
					return;
				}

				if(objectTypeName.length > 4 && objectTypeName.substr(0, 2) == '[L' && objectTypeName.substr(objectTypeName.length-1) == ';'){			//if array of some object type, use ArrayFace with mapped class mapping for the object type.
					return;
				}

				if(objectTypeName.indexOf(":") != -1)
					objectTypeName = objectTypeName.split(':')[0];

				if(!this.metaworksMetadata[objectTypeName]
				//|| true

				){ //caches the metadata
					//alert('getting metadata for ' + objectTypeName);

					if(this.useLocalStorage && localStorage && localStorage['metadata' + '_' + objectTypeName]){
						var metadataInCache = null;
						eval('metadataInCache = ' + localStorage['metadata'+ '_' + objectTypeName]);

						mw3._storeMetadata(metadataInCache);

					}
					else{

						this.metaworksProxy.getMetaworksType(objectTypeName,
							{
								callback: function( webObjectType ){
									//alert(webObjectType.name + "=" + dwr.util.toDescriptiveString(webObjectType, 5))

									mw3._storeMetadata(webObjectType);

									if(mw3.useLocalStorage && localStorage){
										localStorage['metadata_'+ objectTypeName] = JSON.stringify(webObjectType);
									}

								},

								async: false,

								timeout:10000,

								errorHandler:function(errorString, exception) {
									//alert(errorString);
									throw new Error(exception.javaClassName + ": " + exception.message);

									//document.getElementById(this.dwrErrorDiv).innerHTML = errorString;
								}
							}
						);
					}
				}

				var objectMetadata = this.metaworksMetadata[objectTypeName];

				return objectMetadata;
			};

				
			/**
			  *  metadata 캐시하기:  metaworksMetadata 에 넣어두기 와 후처리 작업들
			  
			 < 후처리 작업 1:  displayName 을 메시지 번들에서 가져오기>
			  
			  	각 필드에 displayName 초기값 세팅 --> 다국어처리.. 
//메시지 번들에서 관련 다국어 처리하여 리턴: 메시지 번들에 <클래스명>.<필드명>=XXX 이렇게 된 경우... 자동으로 해당 필드의 명칭으로 매핑됨.

--> 아닌 경우는 

모든 필드마다.. 

package com.abc;

class ClassA{

	@Face(displayName="$property.label")
	public getProperty(){...}

==> messages.properties 파일에 property.label=속성... 을 넣어서 해야 하나... 

properties 파일에 com.abc.property=속성



<후처리 작업 2: @Name 필드에 대한 설정>

public class ClassA{

	@Name  // 이 어노테이션이 붙은 필드가 화면에 출력할 명칭을 지닌 필드임.
	getName()
	
}

mw3.getMetadata('com.abc.ClassA')['nameFieldDescriptor']

로 얻어내면 바로 해당 클래스에 대한 name field 를 접근할 수 있음.

사용예:  object[mw3.getMetadata(object.__className)['nameFieldDescriptor']] ===> object 의 명칭을 리턴함. 자바에서 object.toString()가 유사한 목적.


<후처리 3: @Children 필드에 대한 설정>

@Face(ejsPath="dwr/metaworks/genericfaces/Tree.ejs")
public class Tree{

	@Name
	getTitle()
	
	@Children
	List<Tree> getChild()
	
}

==> 이렇게 해주면... Tree 를 알아서 화면에 Tree 형태로 뿌려줌.. webObjectType['childrenFieldDescriptor'] 로 접근하면 바로 얻어낼 수 있게 해줌.


<후처리 3: @Icon 필드에 대한 설정>


public class Tree{

	@Name
	getTitle()
	
	@Children
	List<Tree> getChild()
	
	@Icon
	getIconURL(){ return "/.../icons/"+getTitle() + ".gif"); }
	
}

mw3.where('in-tree');
mw3.locateObject(object);

---> 알아서 tree 를 ui 로 구성하여 뿌림..

==> 이렇게 해주면... Tree 를 알아서 화면에 Tree 형태로 뿌려줌.. webObjectType['childrenFieldDescriptor'] 로 접근하면 바로 얻어낼 수 있게 해줌.


<후처리 4: @TypeSelector>




<후처리 ..:  메서드 버튼에 대한 emssage 번들값 처리>

messages.properties 파일에 다음과 같이:

com.abc.ClassA.methodA=입력

버튼이 그려질때 [methodA] --> [입력]
			  
			  */

			Metaworks3.prototype._storeMetadata = function(webObjectType){
				
				var objectTypeName = webObjectType.name;
				mw3.metaworksMetadata[objectTypeName] = webObjectType;

				if(webObjectType.alwaysSubmitted){
					mw3.alwaysSubmittedClasses[webObjectType.name]=true;
				}
				
				webObjectType['version'] = mw3._metadata_version ++;
				
				for(var i=0; i<webObjectType.fieldDescriptors.length; i++){
					var fd = webObjectType.fieldDescriptors[i];
					
					var superClasses = webObjectType.superClasses;
					
					for(var superClsIdx = 0; superClsIdx<superClasses.length; superClsIdx++){
						var className = superClasses[superClsIdx];

						try{
						
							if(fd.displayName==null || fd.displayName.toUpperCase()==fd.name.toUpperCase()){
								var messageKey = className + "." + fd.name;
								
								//메시지 번들에서 관련 다국어 처리하여 리턴: 메시지 번들에 <클래스명>.<필드명>=XXX 이렇게 된 경우... 자동으로 해당 필드의 명칭으로 매핑됨.
								var message = mw3.getMessage(messageKey);
								
								if(messageKey!=message){
									
									fd.displayName = message;
									
									break;
								}
								
							}
						}catch(e){}
					}
					
					if(!fd.attributes) continue;
					
					if(fd.attributes['namefield']){
						webObjectType['nameFieldDescriptor'] = fd;
					}else
					if(fd.attributes['children']){
						webObjectType['childrenFieldDescriptor'] = fd;
					}
					if(fd.attributes['icon']){
						webObjectType['iconFieldDescriptor'] = fd;
					}
					if(fd.attributes['typeSelector']){
						webObjectType['typeSelector'] = fd;
					}
					
					for(var attributeName in fd.attributes){
						if(attributeName.indexOf('descriptor.') == 0)
							webObjectType[attributeName.substring(attributeName.indexOf('.')+1) + 'FieldDescriptor'] = fd;
					}
					
					
					fd['getOptionValue'] = function(option, defaultValue){
						if(this.options!=null && this.values!=null)
						
						for(var i=0; i<this.options.length && i<this.values.length; i++){
							if(option==this.options[i])
								return this.values[i];
						}
						
						if(defaultValue)
							return defaultValue;
					};
					
					webObjectType['getFieldNameForFieldDescriptorType'] = function(fieldDescriptorType){
						var fieldDescriptorName = fieldDescriptorType + 'FieldDescriptor';
						
						if(this[fieldDescriptorName]){
							return this[fieldDescriptorName].name;
						}
					}
					
				}

				//following methods are not null, it will creates the lazy-loaded tree mechanism.
				
				var serviceMethodMap = {};
				
				if(webObjectType.serviceMethodContexts)
				for(var i=0; i<webObjectType.serviceMethodContexts.length; i++){
					var serviceMethod = webObjectType.serviceMethodContexts[i];
					
					serviceMethodMap[serviceMethod.methodName] = serviceMethod;
					
					var superClasses = webObjectType.superClasses;
					
					for(var superClsIdx = 0; superClsIdx<superClasses.length; superClsIdx++){
						var className = superClasses[superClsIdx];

						try{
							if(serviceMethod.displayName==null || serviceMethod.displayName.toUpperCase()==serviceMethod.methodName.toUpperCase()){
								var messageKey = className + "." + serviceMethod.methodName;
								var message = mw3.getMessage(messageKey);
								
								if(messageKey!=message){
									
									serviceMethod.displayName = message;
									
									break;
								}
								
							}
						}catch(e){}
					}
					
					if(!serviceMethod.displayName)
						serviceMethod.displayName = serviceMethod.methodName.substr(0,1).toUpperCase() + serviceMethod.methodName.substr(1, serviceMethod.methodName.length-1).replace(/([a-z])([A-Z])/g, '$1 $2');
					
					
					if(serviceMethod.nameGetter){
						webObjectType['nameGetter'] = serviceMethod;
					}else
					if(serviceMethod.childrenGetter){
						webObjectType['childrenGetter'] = serviceMethod;
					}
					
					if(serviceMethod.keyBinding)
						webObjectType['focusable'] = true;
				}
				
				webObjectType['serviceMethodContextMap'] = serviceMethodMap;
				webObjectType['getOptionValue'] = function(option, defaultValue){
						if(this.faceOptions && this.faceOptions[option])
							return this.faceOptions[option];
						else if(defaultValue)
							return defaultValue;
				};

			};




			Metaworks3.prototype.isSuperClassOf = function(superClassName, className){

				var metadata = mw3.getMetadata(className);

				if(metadata){
					for(var i=0; i<metadata.superClasses.length; i++){
						if(metadata.superClasses[i] == superClassName){
							return true;
						}
					}
				}

				return false;

			}

			Metaworks3.prototype.getClosestObject = function(fromObjectId, findElementClassName, returnAll) {
				var resultObject;
				var resultObjectDistance = 0;
				var parentDivs = $('#objDiv_' + fromObjectId).parents();


				if(returnAll){
					resultObject = [];
				}


				parentDivs.each(function(index) {

					var classNameOfDiv = $(this).attr('classname');

					if(classNameOfDiv){

						parentObjectId = $(this).attr('objectid');

						try{
							var parent = mw3.objects[parentObjectId];

							if(!parent) return true;
						}catch(e){ return true; /*skip this parent*/}


						if(mw3.isSuperClassOf(findElementClassName, classNameOfDiv)) {
							if(!parent.__objectId){ //sometimes __objectId is damaged.
								parent.__objectId = parentObjectId;
							}

							if(returnAll) {
								resultObject.push(parent);
								return true;
							}else{
								resultObject = parent;
								return false; // let the $.each stop
							}

						}else{//finding in childs in parent (siblings)

							var parentMetadata = mw3.getMetadata(parent.__className);
							if(parentMetadata){
								parentMetadata.fieldDescriptors

								for(var fdIdx in parentMetadata.fieldDescriptors){
									var field = parentMetadata.fieldDescriptors[fdIdx];
									var fieldObject = parent[field.name];

									if(fieldObject && fieldObject.__className && mw3.isSuperClassOf(findElementClassName, fieldObject.__className)){



										if(returnAll) {
											resultObject.push(fieldObject);
										}else{
											resultObject = fieldObject;
											return false; // let the $.each stop
										}
									}
								}
							}

							return true;


						}

					}

				});


				if(returnAll && resultObject.length==0){
					return null;
				}

				return resultObject;
			}

			Metaworks3.prototype.orderBestObject = function(objectArray, fromObject){
				var fromObjectType = mw3.getMetadata(fromObject.__className);

				if(fromObjectType && fromObjectType.keyFieldDescriptor){
					if(objectArray && objectArray.length) {
						for (var i = 0; i < objectArray.length; i++) {
							if(objectArray[i][fromObjectType.keyFieldDescriptor.name] == fromObject[fromObjectType.keyFieldDescriptor.name]){
								//move to first order
								objectArray.splice(0, 0, objectArray.splice(i, 1)[0]);
							}
						}
					}

				}

				return objectArray;
			}
			
			
			Metaworks3.prototype.showObjectWithObjectId = function (objectId, objectTypeName, targetDiv, options){
				var object = this.getObject(objectId);

				return this.showObject(object, objectTypeName, {targetDiv: targetDiv, objectId: objectId, options: options});
			};
				
			/**
			
			contextValues ==> {value: object, fields :fieldReferencers, methods: methodReferencers}
			
			return html 
			**/
			
			Metaworks3.prototype._template = function(url, contextValues){


				if(this.useLocalStorage && localStorage && url && url.indexOf('?') > 0) url = url.split('?')[0];

				var templateEngine;
				if(mw3.templates[url]){
					templateEngine = mw3.templates[url];
				}else{

					if(mw3.usingTemplateEngine == 'jQote'){
						$.ajax(url, 							
							{
								async: false, 
								success:function(tmpl) {
									templateEngine = tmpl;
								}
							}
						);
						
						$.jqotec(templateEngine, null, url);
						
						//templateEngine = new EJS({url: url});
						mw3.templates[url] = url;
						templateEngine = url;
						
						
					}else {
						var templateSource;

						//caching templates
						if(this.useLocalStorage && localStorage) {



							if (localStorage['template_' + url]) {
								templateSource = localStorage['template_' + url];
							} else {

								$.ajax(url,
									{
										async: false,
										success: function (tmpl) {

											templateSource = tmpl;
										}
									}
								);

								if(templateSource != null && typeof templateSource == 'string' && templateSource.trim().length==0){
									templateSource = "  ";
								}

								localStorage['template_'+ url] = templateSource;
							}

							if(templateSource != null && typeof templateSource == 'string' && templateSource.trim().length==0){
								return ""; //no need to render.
							}

							templateEngine = new EJS({text: templateSource, name: url});
							mw3.templates[url] = templateEngine;

						}else{
							templateEngine = new EJS({url: url});
							mw3.templates[url] = templateEngine;

						}

					}

				}
				
				if(mw3.usingTemplateEngine == 'jQote'){
					return $.jqote(templateEngine, contextValues);
				}else{
					try {
						return templateEngine.render(contextValues);
					}catch(e){
						throw e;
					}
				}
				
			};
			/**
			
				화면에 객체를 렌더링 함
				
******				ejs 파일을 찾는 순서:
	1. 			objectTypeName 에서 얻기:  <className>:<ejs파일위치>
			
				mw3.showObject(string, 'java.lang.String:TextArea.ejs', 'div');
				
	2.			metadata 에서 얻기
	
				@Face(ejsPath="....") <---이걸 얻어서 ejs로 인식
				
	3. ejs context 를 설정: context 값들:
	
							value				: object, 
							objectTypeName		: objectTypeName, 
							targetDiv			: targetDiv, 
							objectMetadata		: (objectTypeName && objectTypeName.length > 0 ? this.getMetadata(objectTypeName) : null), 
							mw3					: this, 
							objectId			: objectId, 
							fields				: (objectRef ? objectRef.fields  : null),
							resources			: (objectRef ? objectRef.fields  : null), //TODO: later should be sent only with resources
							methods				: (objectRef ? objectRef.methods : null),
							descriptor			: descriptor,
							editFunction		: editFunction,
							options		
							
	4.	그리고 ejs 내에서 사용할 수 있는 기본 펑션 - include 를 포함함.
	
		ejs내에서 ... 
			....
			include('header.ejs');
			....
			include('footer.ejs');

		ejs 도 상속구조를 만들어야 하는 경우 사용할 수 있음.
		
		Class A, B, C -> C extends B, B extends A 의 경우, 그림을 그리는 공통영역은 같은데, B,C에 필드만 추가되었음.. A.ejs 에 fields.xxx.here()를 다 넣어놓을 수 없음... 
		
		class A{ String a;}, class B extends A{ String b;}, class C extends B {String c;}
		
		A.ejs
		
		<%=fields.a.here()%>
		
		B.ejs
		
		<%include('A.ejs')%>
		<%=fields.b.here()%>
		
		C.ejs
		
		<%include('B.ejs')%>
		<%=fields.c.here()%>	
	
			**/

			
			Metaworks3.prototype.showObject = function (object, objectTypeName, target){

				var org_object = object;


				//if(object && !object.__className){
				//	if(console)
				//		console.log("Object [" +  object + "] doesn't have __className property. Some classes are not registered in dwr.xml to be converted by MetaworksConverter.");
				//
				//}

				var objectId;
				var targetDiv;
				var options;

				if(target.objectId){
					objectId = target.objectId;
					targetDiv = target.targetDiv;
					options = target.options;
				}else{
					targetDiv = target;
					objectId = mw3.objectId;
				}

				/* replace with face when it is rendered */
				if(object && object._face_){
					var beanPropertyOption = {objectId: objectId, name: "_face_"};

					var html = mw3.locateObject(object._face_, null, null, null, beanPropertyOption);

					if(html.indexOf("<!--replace-->")==0){

						$(targetDiv).replaceWith(html);
					}else{
						$(targetDiv).html(html);
					}


					return html;
				}
					
					//alert( "showObject.object=" + dwr.util.toDescriptiveString(object, 5))
					//choosing strategy for actual Face file.
					var actualFace;
					
					if(objectTypeName.indexOf(":")>-1){
						typeNameAndFace = objectTypeName.split("\:");
						actualFace = typeNameAndFace[1];
						
						objectTypeName = typeNameAndFace[0];
					}
					
										
					var metadata = this.getMetadata(objectTypeName);
					
					//set the context if there's some desired 
					var currentContextWhen = this.when;
					var currentContextWhere = this.where;
					var currentContextHow = this.how;
					
					
					if(object && object.metaworksContext){
						this.setContext(object.metaworksContext);
					}

					if(options && options['when']){
						this.setWhen(options['when']);
					}
					
					if(options && options['how']){
						this.setHow(options['how']);
					}
					
					/*
					 * TODO : matching ejs context and ejs.js context
					if(object && object.metaworksContext){
						if(!object.metaworksContext.when)
							object.metaworksContext.when = this.when;
						
						if(!object.metaworksContext.where)
							object.metaworksContext.where = this.where;
						
						if(!object.metaworksContext.how)
							object.metaworksContext.how = this.how;
					}
					*/

					/*
					 * 2013/12/18 cjw
					 * view 당시의 metaworksContext 와 option 을 저장한다.
					 */
					this.objectContexts[objectId] = {};
					this.objectContexts[objectId]['__metaworksContext'] = {when:mw3.when, how:mw3.how, where:mw3.where};
					this.objectContexts[objectId]['__options'] = options;
					
					
					if(!actualFace && options && options['ejsPath']){
						metadata = this.getMetadata(objectTypeName);
					
						actualFace = options['ejsPath'];						
					}else if(!actualFace && objectTypeName.length > 2 && objectTypeName.substr(-2) == '[]'){			//if array of some object type, use ArrayFace with mapped class mapping for the object type.
						objectTypeName = objectTypeName.substr(0, objectTypeName.length - 2);
						metadata = this.getMetadata(objectTypeName);
						
						actualFace = metadata.faceForArray ? metadata.faceForArray : 'dwr/metaworks/genericfaces/ArrayFace.ejs';

					}else if(!actualFace && objectTypeName.length > 4 && objectTypeName.substr(0, 2) == '[L' && objectTypeName.substr(-1) == ';'){			//if array of some object type, use ArrayFace with mapped class mapping for the object type.
						objectTypeName = objectTypeName.substr(2, objectTypeName.length - 3);
						metadata = this.getMetadata(objectTypeName);
						
						actualFace = metadata.faceForArray ? metadata.faceForArray : 'dwr/metaworks/genericfaces/ArrayFace.ejs';

					}else{

						if(!actualFace && object && object.constructor && object.constructor.toString().indexOf('Array') != -1){
							if(metadata && metadata.faceForArray){
								actualFace = metadata && metadata.faceForArray ? metadata.faceForArray : 'dwr/metaworks/genericfaces/ArrayFace.ejs';
							}else{
								try{
									metadata = this.getMetadata(object[0].__className);
								}catch(e){}
								
								actualFace = metadata && metadata.faceForArray ? metadata.faceForArray : 'dwr/metaworks/genericfaces/ArrayFace.ejs';
							}
						}							
						
						if(!actualFace){
							var faceMappingByContext = metadata.faceMappingByContext;
							
							if(faceMappingByContext)
							for(var i=0; i<faceMappingByContext.length; i++){
								var faceMap;
								
								try{
									eval("faceMap = " + faceMappingByContext[i]); //json notation in there
								}catch(e){
									throw new Error("Error when to map the Face.ejsPathMappingByContext expression '" + faceMappingByContext[i] + '". check the JSON expression exactly.');
								}
								
								if(			
											(faceMap.when == null 	|| faceMap.when == this.when)
										&& 	(faceMap.where == null 	|| faceMap.where == this.where)
										&& 	(faceMap.how == null 	|| faceMap.how == this.how)
									){
									actualFace = faceMap.face;
									
									if(actualFace.indexOf('faces/') == 0){
										actualFace = actualFace.substr('faces/'.length);
									}
									
									break;
								}
							}
						}
						
						if(!actualFace){
							actualFace = metadata.faceComponentPath;	
						}
						
						
						
						if(!actualFace){
							
							if(object.constructor.toString().indexOf('Array') != -1){
								try{
									metadata = this.getMetadata(object[0].__className);
								}catch(e){}
								
								actualFace = metadata.faceForArray ? metadata.faceForArray : 'dwr/metaworks/genericfaces/ArrayFace.ejs';
							}else
								actualFace = 'dwr/metaworks/genericfaces/ObjectFace.ejs';//even though there's no mapping, use ObjectFace

						}
							
					}				
					
					var editFunction = "mw3.editObject('" + objectId + "', '" + objectTypeName + "')";
					
					//create links between objectId and face bi-directionally
					var faceInfo = {
							face: actualFace,
							className: objectTypeName
					};
					this.face_ObjectIdMapping[objectId] = [];
					this.face_ObjectIdMapping[objectId].push(faceInfo);					
					
					if(this.objectIds_FaceMapping [actualFace] == null){
						this.objectIds_FaceMapping [actualFace]={};
					}
					this.objectIds_FaceMapping [actualFace][objectId] = faceInfo;
						
					//console.log(actualFace + ' - ' + objectId);
					
					//end

					var objectRef = this._createObjectRef(object, objectId);
					
					var descriptor = (options ? options['descriptor']: null);
					
					if(descriptor!=null){
						descriptor['getOptionValue'] = function(option, defaultValue){
							if(this.options!=null && this.values!=null)
							
							for(var i=0; i<this.options.length && i<this.values.length; i++){
								if(option==this.options[i])
									return this.values[i];
							}
							
							if(defaultValue)
								return defaultValue;
						}
					
						//only when the descriptor has some options, the object is given to access to it's descriptor among properties in it's parent object.
						// 2012-03-16 cjw descriptor null 오류로 위치 수정
						if(descriptor.options && object)
							object['__descriptor'] = descriptor;
					}
					
					if(actualFace.indexOf("genericfaces") == 0){ //TODO: will need to be optional
						actualFace = "dwr/metaworks/" + actualFace;
					}					
					mw3._importFaceHelper(actualFace);
					
					mw3.loadedObjectIds.push({objectId: objectId, face: actualFace});

					
					try {
						//alert("selected face : " + actualFace);
						var url = this.base + (actualFace.indexOf('dwr') == 0 ? '/':'/metaworks/') + actualFace;
						
						
						var metadata = null;
						
						if(objectTypeName)
							metadata = this.getMetadata(objectTypeName);
						
						if(metadata)
							url = url + "?ver=" + metadata.version; //let it refreshed
						
						var contextValues = {
							value				: object,
							realValue			: org_object,
							objectTypeName		: objectTypeName,
							targetDiv			: targetDiv, 
							objectMetadata		: (objectTypeName && objectTypeName.length > 0 ? this.getMetadata(objectTypeName) : null), 
							mw3					: this, 
							objectId			: objectId,
							fields				: (objectRef ? objectRef.fields  : null),
							getField			: (objectRef ? objectRef.getField  : null),
							getMethod			: (objectRef ? objectRef.getMethod  : null),
							here				: (objectRef ? objectRef.here  : null),
							caller				: (objectRef ? objectRef.caller  : null),
							resources			: (objectRef ? objectRef.fields  : null), //TODO: later should be sent only with resources
							methods				: (objectRef ? objectRef.methods : null),
							descriptor			: descriptor,
							editFunction		: editFunction,
							options				: options								
						};
						
				   		contextValues['include'] = function(ejsPath){
				   			var actualFace = ejsPath;
				   			
							if(actualFace.indexOf("genericfaces") == 0){ //TODO: will need to be optional
								actualFace = "dwr/metaworks/" + actualFace;
							}
							
							var faceInfo = {
									face: actualFace,
									className: objectTypeName
							};
							
							mw3.face_ObjectIdMapping[objectId].push(faceInfo);
							
							if(mw3.objectIds_FaceMapping [actualFace] == null){
								mw3.objectIds_FaceMapping [actualFace]={};
							}							
							mw3.objectIds_FaceMapping [actualFace][objectId] = faceInfo;
							
							mw3._importFaceHelper(actualFace);
							
							var url = mw3.base + (actualFace.indexOf('dwr') == 0 ? '/':'/metaworks/') + actualFace;

							return mw3._template(url, contextValues);
				   		};
				   						   		
						var html = mw3._template(url, contextValues);
						
						if(targetDiv == null)							
							return html;

						if(this.debugMode){
							$(targetDiv).attr({face:actualFace});
						}
						
						//#DEBUG POINT
						if(html && html.indexOf("<!--replace-->")==0){

							$(targetDiv).replaceWith(html);
						}else{
							$(targetDiv).html(html);
						}

						this.fireEvent("located", object);

					   
					} catch(e) {

						e.targetObject = object;
						e.targetObjectId = objectId;

						this.template_error(e, actualFace)
						throw e;
					} finally{
						this.setWhen(currentContextWhen);
						this.setWhere(currentContextWhere)
						this.setHow(currentContextHow)

					}
					
					

					
					
					
					
					
					
					
					
					
					
					
					
					//load scripts if there is.
//					var directFaceName = 'faces/' + objectTypeName.split('.').join('/') + ".ejs";
//					this._importFaceHelper(actualFace, //try to load face helper by using the class name directly first
//							function(){
							//mw3._importFaceHelper(actualFace)
//							}, 
//							directFaceName
//					);

//					mw3.getFaceHelper(objectId);			
					//end

					return objectId;
				};
			
			Metaworks3.prototype._importFaceHelper = function(actualFace, onSuccess, onError){

				var initializingFaceHelper = function() {

					// console.debug("initializingFaceHelper --> " + actualFace);
					
					//TODO: may cause unnecessary javascript object creation - performance & memory waste
					//mw3.onLoadFaceHelperScript(actualFace);
					
					//mw3.onLoadFaceHelperScript();					
					//mw3.loaded = true;

					if(onSuccess) onSuccess();

				};

				var byClassLoader = actualFace.indexOf('dwr') == 0;

				if(!byClassLoader)
					actualFace = 'dwr/metaworks/' + actualFace;


				var url = this.base + '/' + actualFace + ".js";
				
//				   $.ajax({
//				        url: url,
//				        type:'GET',
//				        success:function(content,code)
//				        {
								
			   	// set call order
				var faceHelperIndex = 0;				
				if(this.afterLoadFaceHelperCount > 0){
					for(var i in this.afterLoadFaceHelper){
						if(this.afterLoadFaceHelper[i] != null && this.afterLoadFaceHelper[i] == actualFace){
							faceHelperIndex = i;
						}
					}
				}
											
				if(faceHelperIndex == 0){
					this.afterLoadFaceHelperCount++;
					
					faceHelperIndex = this.afterLoadFaceHelperCount;						
				}
				this.afterLoadFaceHelper[faceHelperIndex] = actualFace;
				
				// import script
				if(typeof this.loadedScripts[url] == 'undefined'){
					if(!mw3.importScript(url, initializingFaceHelper)){
						mw3.loadedScripts[url] = null;
						
						return;
					}
					
					var startPos = 0;

					var faceHelper;
					
//					if(byClassLoader){
					if(actualFace.indexOf('genericfaces') != -1){
						startPos = 'dwr/metaworks/genericfaces/'.length;

					}else{
						startPos = 'dwr/metaworks/'.length;
					}

					faceHelper = actualFace.substr(startPos, actualFace.lastIndexOf('.') - startPos).split('/').join('_');
					//}else
					//	faceHelper = actualFace.substr(0, actualFace.lastIndexOf('.')).split('/').join('_');
					
					mw3.loadedScripts[actualFace] = faceHelper; //now the namespace may not cause any collision.

				}
			};

			Metaworks3.prototype.importScript = function(scriptUrl, afterLoadScript){

				var scriptString = false;

				if(!this.loadedScripts[scriptUrl]){
				   mw3.loadedScripts[scriptUrl] = scriptUrl;


					//TODO: asynchronous processing performance issues later on
					if(this.useLocalStorage && localStorage){
						if(localStorage['scripts_' + scriptUrl]){
							scriptString = localStorage['scripts_' + scriptUrl];

							if(scriptString && scriptString=="//no script") //there is no script
								return false;
						}
					}

					if(!scriptString) {
						$.ajax({
							async: false,
							url: scriptUrl,
							type: 'GET',
							success: function(data){
								if (data && data.length > 0) {
									scriptString = data;


								}
							},


							error: function (xhr) {
								//TODO: looks undesired validation or something is happening guessing by
								//      the successful request is treated as an error.
								//      It probably harmful to performance, so someday it should be fixed.

								if (xhr.status == '200') {
									this.success();
								}

								//alert(e.message);
							}
						});
					}

					if(scriptString){


						//eval(scriptString);
						//eval(scriptString);

						//if(typeof afterLoadScript == 'function')
						//	afterLoadScript();


						// add script url
						var head = document.getElementsByTagName('head')[0];
						var script = document.createElement('script');
						script.type = 'text/javascript';


						if(this.useLocalStorage && localStorage){
							script.innerText = scriptString;
							script.innerHTML = scriptString;
						}else
							script.src= scriptUrl;

						head.appendChild(script);

						if (typeof afterLoadScript == 'function') {
							script.onload = afterLoadScript;

							script.onreadystatechange = function () { //for IE
								if (this.readyState == 'complete' || this.readyState == 'loaded') {
									afterLoadScript;
								}
							}
						}

						if(this.useLocalStorage && localStorage)
							localStorage['scripts_' + scriptUrl] = scriptString;

					}else{
						if(this.useLocalStorage && localStorage)
							localStorage['scripts_' + scriptUrl] = "//no script";

					}


				}else{
					if(typeof afterLoadScript == 'function')
						afterLoadScript();
				}

				return scriptString;
			};
			
			Metaworks3.prototype.importStyle = function(url){
				if(!this.loadedScripts[url])
				try{
					var head= document.getElementsByTagName('head')[0];
					var css= document.createElement('link');
					css.type= 'text/css';
					css.rel = 'stylesheet';
					css.href= url;
					head.appendChild(css);
					this.loadedScripts[url] = url;
				}catch(e){
					e=e;
				}
			};
			
			Metaworks3.prototype._createObjectKey = function(value){

				if(value && value.__className){
					
					if(value.__className=="Number" || value.__className=="String")
						return value;

					var metadata = this.getMetadata(value.__className);
					
					var clsNames;
					
					if(arguments.length>1) //means to generate super class' key combination as well.
						clsNames = metadata.superClasses;
					else{
						clsNames = [value.__className];
					}
					
					var id = "";					
					var ids = [];
					
					var returnValues=[];
					var j=0;
					
					// 2012-04-16 key value null 경우 undefined 안먹게 수정
					if(metadata.keyFieldDescriptor && value[metadata.keyFieldDescriptor.name])
						id = this._createObjectKey(value[metadata.keyFieldDescriptor.name], true);

					if(id instanceof Array){
					    ids = id;
					}else{
						ids.push(id);
					}
					
					for(var h=0; h<ids.length; h++){
						for(var i=0; i<clsNames.length; i++){
							if(ids[h])
								returnValues[j++] = clsNames[i] + '@' + ids[h];
							else
								returnValues[j++] = clsNames[i];
						}
					}

					//add placeholder candidates without @id again
					for(var i=0; i<clsNames.length; i++){
						if(metadata.keyFieldDescriptor)
							returnValues[j++] = clsNames[i];  													
					}
					
					
					
					if(arguments.length>1){						
						return returnValues;
					}else
						return returnValues[0];
				}else 
					return value;
			};
			
			Metaworks3.prototype.locateObject = function(value, className, divName, options, beanPropertyOption){
				var objectId = ++ this.objectId;
				var divId =  this._getObjectDivId(objectId);
				var infoDivId = this._getInfoDivId(objectId);
				var html; 
				
				mw3.loadedObjectIds=[]; //clear everytime

				
				var className;
				
				if(className){
				}else if(value.__className){
					className = value.__className;
				}else{

					if(typeof value == 'string'){
						className = "java.lang.String";
					}else if(typeof value =='number'){
						className = "java.lang.Number";
					}else {
						className = "java.lang.Object";
					}
				}

				// 2012-04-04 cjw java.lang.String 일 경우 faceHelper 호출을 위해 공백으로 초기화
				//if(value == null && className == 'java.lang.String')
				//	value = '';
					
				this.newBeanProperty(objectId);

				//console.log('locateObject : ' + objectId);
				
				this.objects[objectId] = value; //caches the values
				this._armObject(objectId, value); //empower the object !
				
				
				// 2012-09-27 cjw 슈퍼클래스까지 keyMapping
				/*
				var objKey = this._createObjectKey(value);
				if(objKey)
					this.objectId_KeyMapping[objKey] = objectId;
				*/
				
				if(value && !(value instanceof Array) && typeof value == 'object')
					this.putObjectIdKeyMapping(objectId, value, true);
				
				
				this._wireObject(value, objectId);
				
				if(beanPropertyOption)
					this.addBeanProperty(beanPropertyOption.objectId, ((beanPropertyOption.name.indexOf('[') != 0)?'.':'') + beanPropertyOption.name);
				
				var metadata = this.getMetadata(className);


				/*
				if(!value && className.indexOf('java.lang')!=0 && (!metadata || !metadata.faceOptions || !metadata.faceOptions['renderNull'])){

					if(this.debugMode)
						return '[empty]';
					else
						return '<empty>';

				}
				*/
				var elementTag = 
					(options && options['htmlTag'] ? 
						options['htmlTag'] 
						: 
						( metadata && metadata.faceOptions && metadata.faceOptions['htmlTag'] ? 
								metadata.faceOptions['htmlTag'] : "div"
						)
					);
				
				var elementClass = 
					(options && options['htmlClass'] ? 
							options['htmlClass'] 
							: 
							( metadata && metadata.faceOptions && metadata.faceOptions['htmlClass'] ? 
									metadata.faceOptions['htmlClass'] : ""
							)
						);

				
				if(metadata){
					if(metadata.onDropTypes){
						for(var typeName in metadata.onDropTypes){
							elementClass = elementClass + " onDrop_"+typeName.split('.').join('_');
						}
					}
					
				}
				
				var elementSubTag = "";
				
				if(elementTag == 'tr')
					elementSubTag = 'td';
				else if(elementTag == 'ul')
					elementSubTag = 'li';
				else if(elementTag == 'dl')
					elementSubTag = 'dd';
				
				// 2013-07-31 DOM 생성방법 수정 및 DOM 객체에 name 설정 추가
				var locateObjectDOM = $('<div>');

				/////// TODO: later this is healthy for html DOM size
				//var mainDOM = this.debugMode ?
				//	$('<' + elementTag + '>').attr({'id': divId, 'className': className, 'objectId': objectId}) :
				//	$('<' + elementTag + '>').attr({'id': divId});

				var mainDOM =
					$('<' + elementTag + '>').attr({'id': divId, 'className': className, 'objectId': objectId});

				if(elementClass)
					mainDOM.addClass(elementClass);
				
				if(options && options['name'])
					mainDOM.attr('name', options['name']);
				
				if(metadata && metadata.focusable)
					mainDOM.attr('tabindex', objectId).css('outline-style', 'none');
				
				if(elementSubTag)
					$('<' + elementSubTag + '>').text(mw3.MESSAGE_LOADING).appendTo(mainDOM);
				else
					mainDOM.text(mw3.MESSAGE_LOADING);
				
				var scriptHTML = '';


				var batchHTML = mw3.showObjectWithObjectId(this.objectId, className, null, options);


				var html;

				if(batchHTML.indexOf("<!--replace-->")==0){

					html = (batchHTML);
				}else{
					mainDOM.html(batchHTML);

					locateObjectDOM.append(mainDOM);
					locateObjectDOM.append($('<div>').attr('id', infoDivId))

					html = locateObjectDOM.html();// + scriptHTML;

				}


				if(divName){ //when locateObject method has been called for just positioning purpose not the html generation.
					var divId = divName;
					
					if(options && options['prepend'])
						$(divId).prepend(html);
					else
						$(divId).append(html);
					
					this.targetObjectId = objectId; 
					
					return this;
				}
				
				//#DEBUG POINT
				return html;
			};
			
			Metaworks3.prototype.setObject = function(value/*, objectTypeName*/){
				var objectTypeName;
				var objectId;
				
				/*
				 * 2013/10/18 jinwon cho
				 * refactoring arguments logic
				 */
				if(arguments.length == 1){
					objectId = this.targetObjectId;
				}else{
					objectId = arguments[0];
					value = arguments[1];
					objectTypeName = arguments[2];
				}
				
				var divId =  "#objDiv_" + objectId;
				
				/*
				$(divId).replaceWith(function(){
					if(!objectTypeName)
						objectTypeName = value.__className;
					
					html = mw3.locateObject(value, objectTypeName, null);
					
					return html;
				});
	
				mw3.removeObject(objectId);
								
				return true;
				*/
				
				if(this.recentCallMethodName && objectId == this.recentCallObjectId){
					if(mw3.getFaceHelper(objectId) && mw3.getFaceHelper(objectId).endLoading){
						mw3.getFaceHelper(objectId).endLoading(this.recentCallMethodName);
					}else{
						mw3.endLoading(objectId, this.recentCallMethodName);
					}
					
					if(mw3.getFaceHelper(objectId) && mw3.getFaceHelper(objectId).showStatus){
						mw3.getFaceHelper(objectId).showStatus( this.recentCallMethodName + " DONE.");
					}else{
		
						mw3.showInfo(objectId, this.recentCallMethodName + " DONE");	
					}
				}
				
				mw3.removeObject(objectId, true);
				
				if(objectTypeName){
				}else if(value.__className){
					objectTypeName = value.__className
				}else if(value.constructor.toString().indexOf('Array') != -1){
					objectTypeName = ":dwr/metaworks/genericfaces/ArrayFace.ejs";
					
					if(value.length==0){ //append class info since emptry array doesn't contain any class info
						var existingObj = this.objects[objectId];
						if(existingObj.__className)
							objectTypeName = existingObj.__className + objectTypeName;
					}
				}else
					objectTypeName = ":dwr/metaworks/genericfaces/ObjectFace.ejs";
				
				//alert( dwr.util.toDescriptiveString(value, 5))
				

				
				
				// 2012-03-27 cjw destroy event
    			if(this.objects[objectId]){
    				if(this.getFaceHelper(objectId) && this.getFaceHelper(objectId).destroy)
    					mw3.getFaceHelper(objectId).destroy();
    			}

    			// 2013-10-02 cjw unbind
    			$(divId).unbind();
    			
    			this._armObject(objectId, value); //let the methods and some special fields available
				this.objects[objectId] = value; //change the cached value
				this.faceHelpers[objectId] = null;
				
				
				/*
				 * 2013/01/18 jinwon
				 * super class 까지
				 */
				if(value && !(value instanceof Array) && typeof value == 'object'){
					var objKeys = this._createObjectKey(value, true);
					if(objKeys && objKeys.length){
						for(var i=0; i<objKeys.length; i++){
							this.objectId_KeyMapping[objKeys[i]] = objectId;
						}
					}
				}
			
				/*
				var objKey = this._createObjectKey(value);
				if(objKey!=null){
					this.objectId_KeyMapping[objKey] = objectId;
				}*/
				
				
				///// auto wiring object to its class name /////
				this._wireObject(value, objectId);
					
				this.newBeanProperty(objectId);
				
				this.showObjectWithObjectId(objectId, objectTypeName, divId);
				
				return this._withTarget(objectId);

			};
			
			Metaworks3.prototype.getObjectReference = function(){
				return this._createObjectRef(this.targetObjectId, getObject());
			};

			Metaworks3.prototype.getParentMetaworksContext = function(objId){

				var parentDivs = $('#objDiv_' + objId).parents();

				var metaworksContext;
				parentDivs.each(function(index) {

					var parentObjectId = $(this).attr('id');

					if (parentObjectId && parentObjectId.indexOf("objDiv_") == 0) {

						parentObjectId = parentObjectId.substr("objDiv_".length);
						var parent = mw3.getObject(parentObjectId);

						if(parent && parent.metaworksContext){
							metaworksContext = parent.metaworksContext;

							return false;
						}
					}
				});

				return metaworksContext;
			}

			
			Metaworks3.prototype.removeObject = function(objectId, keepDiv){
				if(arguments.length == 0)
					objectId = this.targetObjectId;				

				var divId =  "#" + this._getObjectDivId(objectId);
				var infoDivId =  "#" + this._getInfoDivId(objectId);
                  
				if(objectId == mw3.recentOpenerObjectId[mw3.recentOpenerObjectId.length - 1])
				    mw3.recentOpenerObjectId.pop();
				
				if(mw3.objects[objectId] && !mw3.objects[objectId]['__cached']){
					this.newBeanProperty(objectId);
					
					// 2012-04-04 cjw destroy 호출 후 removeObject
					var faceHelper = this.getFaceHelper(objectId);
					
	    			if(faceHelper && faceHelper.destroy)
	    				faceHelper.destroy();
					
					this.objects[objectId] = null;
					this.faceHelpers[objectId] = null;
					this.beanExpressions[objectId] = null;
					
					/*
					var parent = $(divId).parent();
					if(parent.hasClass('target_stick') || parent.hasClass('target_popup')){
						console.debug('remove popup');
						
						parent.remove();
					}
					*/
				}
				
				$(divId).triggerHandler('destroy');
				
				if(!keepDiv)
					$(divId).remove();
					
				$(infoDivId).remove();

				//TODO: the objectId_KeyMapping also need to clear with back mapping or key generation with value;
				
				return this._withTarget(objectId);
			};
			
			Metaworks3.prototype._withTarget = function (objectId){
				this.targetObjectId = objectId;
				
				return this;
			};
				
			//TODO: this method is not required anymore. (by [dtfmt])
			Metaworks3.prototype.newBeanProperty = function(parentObjectId){
				var beanExpression = {};
				
				var beanPaths = this.beanExpressions[parentObjectId];
				if(beanPaths)
				for(var propName in beanPaths){
					var beanPath = beanPaths[propName];

					var obj = this.objects[beanPath.valueObjectId];

					if(obj && !(obj instanceof Array) && typeof obj == 'object'){
						var objKey = this._createObjectKey(obj);
						this.objectId_KeyMapping[objKey] = null;
					}
					
					// 2012-04-16 chlid destroy call
					this.removeObject(beanPath.valueObjectId, true);
					
					/*
					var faceHelper = this.getFaceHelper(beanPath.valueObjectId);
					
					if(faceHelper && faceHelper.destroy)
						faceHelper.destroy();
					
					this.objects[beanPath.valueObjectId] = null;					
					this.faceHelpers[beanPath.valueObjectId] = null;
					
					this.newBeanProperty(beanPath.valueObjectId);
					*/
				}
				
				this.beanExpressions[parentObjectId]=beanExpression;
			};
			
			Metaworks3.prototype.addBeanProperty = function(parentObjectId, fieldName){
				//console.log('addBeanProperty : ' + parentObjectId + ' , ' + fieldName);
				
				var beanExpression = this.beanExpressions[parentObjectId];
				
				if(beanExpression == null)
					beanExpression = {};
				else{
					//detecting the first moment that re-setting the field properties [dtfmt]
					if(beanExpression[fieldName] != null)
						beanExpression = {};
				}
				
				beanExpression[fieldName] = 
				{
					fieldName		: fieldName,
					valueObjectId	: this.objectId
				}
				
				this.beanExpressions[parentObjectId]=beanExpression;
			};
				
			Metaworks3.prototype.template_error = function(e, actualFace) {
				var errorElement = document.getElementById(this.errorDiv);

				if(e.lineNumber){
					if(e.lineText)
						var message = "["+actualFace+"] at line "+e.lineNumber+": "+e.lineText+": "+e.message;
					else
						var message = "["+actualFace+"] at line "+e.lineNumber+": "+e.message;
				}else
					var message = "["+actualFace+"] "+e.message;

				if(e.targetObjectId) {
					message = message + "\n - objectId is [" + e.targetObjectId + "]";
					//mw3.debugPoint = actualFace;
				}

				//if(e.targetObject){
				//	message = message + "\n - object value is [" + JSON.stringify(e.targetObject) + "]";
				//}

				//if(mw3.template_line)
				//	message = message + ": Actual Line Number is " + mw3.template_line;

				if(errorElement){
					errorElement.style.display = 'block'
					errorElement.innerHTML = "<span><font color=#FB7524>" + message + "</font></span>";
					errorElement.className = 'error';
				}else{
					if(console)
						console.log(message);
					else
						alert(message);
				}

				if(actualFace.indexOf('?') > 0){
					mw3.debugPoint = actualFace.split('?')[0];
				}else{
					mw3.debugPoint = actualFace;
				}

				mw3.debugMode = true;
			};
				
			Metaworks3.prototype.createInputId = function(objectId){
				return "_mapped_input_for_" + objectId;
			};
			
			Metaworks3.prototype.getInputElement = function(objectId, propName){
				var beanPaths = mw3.beanExpressions[objectId];
				if(!beanPaths) return null;
				
				var beanPath = beanPaths["." + propName];
				if(!beanPath) return null;
				
				var inputId = mw3.createInputId(beanPath.valueObjectId); //try object.resourceName.__objectId
				
				var inputElement = document.getElementById(inputId); //will be "mw3.getInputElement(object.resoureceName)"

				return inputElement;
			};
			
			Metaworks3.prototype.getChildObjectId = function(parentObjectId, propName){
				var beanPaths = mw3.beanExpressions[parentObjectId];
				if(!beanPaths) return null;
				
				var beanPath = beanPaths["." + propName];
				if(!beanPath){
					var beanPath = beanPaths["[" + propName + "]"];
					if(!beanPath)
						return null;
				}
				
				return beanPath.valueObjectId;
			};
			
			Metaworks3.prototype.getObject = function(){
				var objectId;
				
				if(arguments.length == 0)
					objectId = this.targetObjectId;
				else if(arguments.length == 1)
					objectId = arguments[0];

				var value = null;
				

				var faceHelper = this.getFaceHelper(objectId);

				if(faceHelper && faceHelper.getValue){ //if there's face helper and there's customized getter exists, use it.
					value = faceHelper.getValue();
				}else{
					value = this.getObjectFromUI(objectId);
				}

				//Since we believe getObjectFromUI is inherently getting the right data.
				if(value==null)
					value = this.objects[objectId];
				
				//sometimes the armed object may be unarmed by the user-defined facehelper.getValue() method
				if(value && !value.__objectId)
					this._armObject(objectId, value);
				
				return value;
			};
			
			Metaworks3.prototype.getObjectFromUI = function(objectId){
				var objectId;
				
				var value = null;
				
				var tagId = this.createInputId(objectId);
				
				var inputTag = document.getElementById(tagId);
				if(inputTag){
					value = dwr.util.getValue(tagId); //this would prohibit File object damaged
				}
				var beanPaths = this.beanExpressions[objectId];
				if(beanPaths)
				for(var propName in beanPaths){
					var beanPath = beanPaths[propName];
					
					try{
						eval("this.objects[objectId]" + beanPath.fieldName + "=this.getObject('" + beanPath.valueObjectId + "')");
					}catch(e){
						debugger;

						throw new Error("Error when to map the " + propName + " beanExpressions. Cause error is "+ e.message + ", Stack trace is " + e.stack);
					}
				}
				
				//if(!inputTag && !beanPaths){
				if(value==null)
						value = this.objects[objectId];
					
				//}
				
				//sometimes the armed object may be unarmed by the user-defined facehelper.getValue() method
				if(value && !value.__objectId)
					this._armObject(objectId, value);
				
				return value;
			};
			
			Metaworks3.prototype._getInfoDivId = function(objId){
				return "info_" + objId;
			};
			Metaworks3.prototype._getObjectDivId = function(objId){
				return "objDiv_" + objId;
			};
			Metaworks3.prototype._getMethodDivId = function(objId, methodName){
				return "method_" + objId + "_" + methodName;
			};


			Metaworks3.prototype.getAutowiredObject = function(className, requireExactOne){

				var autowiredObjectId;
				var autowirable;

				if(className.indexOf("@") > 0){

					autowiredObjectId = this.objectId_KeyMapping[className];

					var classNameAndId = className.split("@");
					className = classNameAndId[0];


				}else{
					autowiredObjectId = this.objectId_ClassNameMapping[className];
				}

				if (autowiredObjectId) {

					if (autowiredObjectId.__isAutowiredDirectValue) { //means direct value not id pointer
						autowirable = autowiredObjectId.value;
					} else
						autowirable = this.getObject(autowiredObjectId);
				}



				if(autowirable && autowirable.__className){

					if (requireExactOne){
						if(autowirable.__className == className)
							return autowirable;
						else
							return null;
					}


					//prevent undesired object return: sometimes returns wrong objects (which is in difference class) due to the old object id is not properly cleaned in object-ClassMapping by replacing object returns
					var metadata = this.getMetadata(autowirable.__className);
					var superClsOfAutowirable = metadata.superClasses;

					for(var i=0; i<superClsOfAutowirable.length; i++){
						var superClassName = superClsOfAutowirable[i];

						if(className == superClassName) return autowirable;
					}

				}
				return null;
			};
			
			Metaworks3.prototype.getBestObject = function(object){
				var objKeys = mw3._createObjectKey(object, true);
				
				if(objKeys && objKeys.length){
								        				
					for(var i=0; i<objKeys.length; i++){
						
						var mappedObjId = mw3.objectId_KeyMapping[objKeys[i]];
			
						if(mappedObjId){
							return this.objects[mappedObjId];				
						}
						
					}
				}


			};


			Metaworks3.prototype.clientSideCall = function (objectId, methodName){
				try{
					var infoDivId = "#"+this._getInfoDivId(objectId);
					$(infoDivId).html("<img src='dwr/metaworks/images/circleloading.gif'>");

					var object = this.getObject();
					var functionName = object.__className.replace('.', '_') + "_" + methodName;
					eval(functionName+"('" + objectId + "')");
					
					// 2012-04-12 cjw showInfo 로 변경
					this.showInfo( methodName + " DONE." );					
					/*
					$(infoDivId).html("<font color=blue> " + methodName + " DONE. </font>");
					$(infoDivId).slideDown(500, function(){
						setTimeout(function() {
							$( infoDivId ).slideUp(500);
						}, 5000 );
					});
					*/
					
				}catch(e){
					$(infoDivId).html("<font color=red>Error: "+ e.message +" [RETRY]</font> ");
					
				}
			};
			
			Metaworks3.prototype.startLoading = function(objId){
				var infoDivId = "#"+this._getInfoDivId(objId);
				
				$(infoDivId).css('display', 'block').html("<img src='dwr/metaworks/images/circleloading.gif' align=middle>");
			};
						
			Metaworks3.prototype.endLoading = function(objId){
				var infoDivId = "#"+this._getInfoDivId(objId);
				
				$(infoDivId).css('display', 'block').html("");
			};
    		
			Metaworks3.prototype.call = function (svcNameAndMethodName){
				mw3.loaded = false;
//				mw3.debug("call start");

				var objId;
				
				if(arguments.length > 1){
					objId = svcNameAndMethodName;
					svcNameAndMethodName = arguments[1];

				}else if(arguments.length ==1 ){
					objId = this.targetObjectId;
				}
				
				// mw3.log('________call : ' + svcNameAndMethodName + '[' + objId + '] ---> ' + new Date().getTime());
				
				// 2012-04-14 cjw 재귀호출 막음
				// 2012-04-18 이전 수정 버전 문제로 재수정 (undefined 일때 true 가 되여야함)
				var getAgain = (arguments.length > 2 ? (typeof arguments[2] != 'undefined' ? arguments[2] : true) : true);				
				//var getAgain = (arguments.length > 2 ? arguments[2] : true);
				var sync = (arguments.length > 3 ? arguments[3] : false);
				// 2012-11-25 cjw add callback function
				var callback = (arguments.length > 4 ? arguments[4] : null);
				// 2012-11-25 cjw add callback function
				var test = (arguments.length > 5 ? arguments[5] : null);
				
//				if(typeof objId == 'number'){ //check if number
	
				if(test){
					var object = mw3.objects[objId];
				}else{
//					var objectFromUI = this.getObjectFromUI(objId);
					
//					if(objectFromUI.__faceHelper && getAgain){
					var object = mw3.getObject(objId);
					//}else{
					//	var object = objectFromUI;
					//}
				}

				var originalObject = object;

				//var thisMetaworks = this;
				var divId = "objDiv_" + objId;
				
				//if(object && object.metaworksContext)
				//	this.setContext(object.metaworksContext);
				//this.setWhen(this.WHEN_VIEW);
				
				if(svcNameAndMethodName.indexOf('.') == -1){ //if there only methodname has been provided, guess the service name

					var className = object.__className;
					//var serviceName = className.subtring(className.lastIndexOf('.')) + 'Service';
					//svcNameAndMethodName = serviceName + "." + svcNameAndMethodName;
					
					
					var objectMetadata = this.getMetadata(className);
					var serviceMethodContext;
					
					
					if(objectMetadata && objectMetadata.serviceMethodContextMap && objectMetadata.serviceMethodContextMap[svcNameAndMethodName]){
						serviceMethodContext = objectMetadata.serviceMethodContextMap[svcNameAndMethodName];
						
						if(serviceMethodContext && serviceMethodContext.validate == true){
							if(!this.validObject(object)){
								if(this.getFaceHelper(objId) && this.getFaceHelper(objId).endLoading){
									this.getFaceHelper(objId).endLoading(svcNameAndMethodName);
								}else{
									this.endLoading(objId, svcNameAndMethodName);
								}
								
			        			if(this.getFaceHelper(objId) && this.getFaceHelper(objId).showStatus){
			        				this.getFaceHelper(objId).showStatus( svcNameAndMethodName + " INVALID.");
			        			}else{
			        				this.showInfo(objId, svcNameAndMethodName + " INVALID");	
			        			}							
								
			        			return false;						
							}
						}
						
						
						if(serviceMethodContext){
							if(serviceMethodContext.callByContent == false){
								if(serviceMethodContext.payload){

									var beanPaths = [];
									for(var key in serviceMethodContext.payload){

										if(key.indexOf("wireParamCls:") != 0)
											beanPaths.push(key);
									}

									var objectForCall = mw3.___copyBeanPathsOnly(object, beanPaths);

									//var objectForCall = {__className: object.__className, metaworksContext: object.metaworksContext};
									// for(var i in objectMetadata.fieldDescriptors){
									// 	var fd = objectMetadata.fieldDescriptors[i];
									//
									// 	if(serviceMethodContext.payload[fd.name])
									// 		objectForCall[fd.name] = object[fd.name];
									//
									// }
									
									object = objectForCall;
									
								}else								
									object = this._createKeyObject(object); //default option
							}else{
								
								var objectForCall = {__className: object.__className, metaworksContext: object.metaworksContext};

								//we have to copy all the field values for objectForCall since there's too many additional JSON fields for call methods.
								for(var i in objectMetadata.fieldDescriptors){
									var fd = objectMetadata.fieldDescriptors[i];
									
									if(object!=null && object[fd.name]!=null && (!serviceMethodContext.except || !serviceMethodContext.except[fd.name]))
										objectForCall[fd.name] = object[fd.name];
								}
									
								object = objectForCall;
								
							}

						}
					}else{
						if(console)
							console.log('serviceMethodContext is not found!');
						
						return;
					}

					var placeholder = null;
					if(serviceMethodContext && serviceMethodContext.target!="none"){
						var loader = serviceMethodContext.loader;						
						
						if(loader && serviceMethodContext.target=="popup" && loader[0].indexOf("java.lang.Object")==-1){

	        				try{
		        				mw3.popupDivId = 'popup_' + objId;
		        				$('body').append("<div id='" + mw3.popupDivId + "' class='target_popup' style='z-index:10;position:absolute; top:0px; left:0px'></div>");
		        				placeholder = mw3.locateObject({__className: loader[0]}, null, '#' + mw3.popupDivId).targetObjectId;

								mw3.onLoadFaceHelperScript();
	        				}catch(e){if(consol) console.log(e)}
						}
						
						if(placeholder && this.getFaceHelper(placeholder) && this.getFaceHelper(placeholder).startLoading){
							this.getFaceHelper(placeholder).startLoading(svcNameAndMethodName);
						}else if(placeholder){
							this.startLoading(placeholder, svcNameAndMethodName);
						}
						else if(this.getFaceHelper(objId) && this.getFaceHelper(objId).startLoading){
							this.getFaceHelper(objId).startLoading(svcNameAndMethodName);
						}else{
							this.startLoading(objId, svcNameAndMethodName);
						}
					}


    				//alert("call.argument=" + dwr.util.toDescriptiveString(object, 5))
    				
					var autowiredObjects = {};
					
					////// auto wiring objects //////////
					
					// in case of the object is an interface, find the service class and find out autowired field in there.
					if(objectMetadata.interface){
						var startPosOfClassName = className.lastIndexOf(".");
						implClassName = className.substring(startPosOfClassName+2);
						implClassName = className.substring(0, startPosOfClassName) + "." + implClassName;
						
						objectMetadata = this.getMetadata(implClassName); //WARN: TODO: may cause following naive reference for 'objectMetadta' have problem.
						
					}

					var autowiringTargets = [];
					if(objectMetadata.autowiredFields)
						autowiringTargets = objectMetadata.autowiredFields;



					//code for parameterized invocation: For AutowiredFromClient Parameter
					var prefixForWireParamCls = "wireParamCls:";
					if(serviceMethodContext && serviceMethodContext.payload){
						for(var keyName in serviceMethodContext.payload){
							if(keyName.indexOf(prefixForWireParamCls) == 0){


								var wireParamClsName = keyName.substring(prefixForWireParamCls.length);

								var selection, payload;
								if(wireParamClsName.indexOf(":") > 0){
									var clsNameAndSelect = wireParamClsName.split(":");
									wireParamClsName = clsNameAndSelect[0];
									selection = clsNameAndSelect[1];

									if(clsNameAndSelect.length > 2){
										payload = clsNameAndSelect[2];
									}

								}

								autowiringTargets[keyName] = {
									field: wireParamClsName,
									select: selection,
									payload: payload
								};

							}
						}
					}


					if(autowiringTargets){
						for(var fieldName in autowiringTargets){
							var autowiredField = autowiringTargets[fieldName];

							var autowiredClassName = autowiredField.field;
							var autowiredSelect = autowiredField.select;
							var autowiredBeanPath = autowiredField.payload;

							// Condition object to be autowired 
							if(autowiredSelect != null && autowiredSelect.length > 0){
							     for(var i in this.objects){
							    	 
							    	 if(this.objects[i] && this.objects[i].__className){
							    		 var sameClass = false;
								    	 var autowiredObjectMetadata = this.getMetadata(this.objects[i].__className);
	 							    	 
								    	 for(var j=0; j<autowiredObjectMetadata.superClasses.length; j++){
								    		 if(autowiredObjectMetadata.superClasses[j] == autowiredClassName){
								    			 sameClass = true;
								    			 
								    			 break;
								    		 }
								    	 }
						    			 
						    			 if(sameClass){
							    			 var isSelect = false;
											 try {
												 with (mw3.getObject(objId)) {
													 var autowiredObject = this.objects[i];
													 isSelect = eval(autowiredSelect);
												 }
											 }catch(e){};

							    			 if(isSelect){
							    				 autowiredObjects[fieldName] = this.getObject(i);

							    				 break;
							    			 }
						    			 }
							    	 }
							     }
							}else{
								// The default option is that the Nearest object to be autowired
								try{

									var try1 = mw3.getClosestObject(objId, autowiredClassName); //try logical distance firstly.

									if(try1){

										autowiredObjects[fieldName] = try1;

									}else{  /// if fail try old logic (physical distance)

										var srcObjectDiv = $('#' + mw3._getObjectDivId(objId));
										if(srcObjectDiv.length > 0){
											var srcObjects = [];

											for(var targetClassName in this.objectId_KeyMapping){
												if(targetClassName == autowiredClassName || targetClassName.indexOf(autowiredClassName + '@') == 0){
													var targetObjectDiv = $('#' + mw3._getObjectDivId(this.objectId_KeyMapping[targetClassName]));
													if(targetObjectDiv.length > 0){
														srcObjects.push({
															objectId: this.objectId_KeyMapping[targetClassName],
															distance: srcObjectDiv.distanceTo(targetObjectDiv)
														});
													}
												}
											}

											if(srcObjects.length > 2){
												srcObjects.sort(function(a, b) {return a.distance - b.distance});

												autowiredObjects[fieldName] = this.getObject(srcObjects[0].objectId);
											}
										}

									}



								}catch(e){
									if(console)
										console.log(e);
								}
								
								// Default autowired
								if(!autowiredObjects[fieldName])
									autowiredObjects[fieldName] = this.getAutowiredObject(autowiredClassName);	
								
								if(autowiredObjects[fieldName] && autowiredObjects[fieldName].__objectId)
									autowiredObjects[fieldName] = this.getObject(autowiredObjects[fieldName].__objectId);
							}


						//filt out the undesired fields for optimizing payload size
						if(autowiredBeanPath)
							try{
								var autowiredBeanPaths;
								eval("autowiredBeanPaths = " + autowiredBeanPath);

                                autowiredObjects[fieldName] = mw3.___copyBeanPathsOnly(autowiredObjects[fieldName]);

							}catch(e){console.log(e);}
						}


					}


					for(var classNameForAutowiring in mw3.alwaysSubmittedClasses){

						var fieldKeyName = prefixForWireParamCls + classNameForAutowiring;

						if(autowiredObjects[fieldKeyName] == null){

							autowiredObjects[fieldKeyName] = mw3.getClosestObject(objId, classNameForAutowiring);

							if(autowiredObjects[fieldKeyName] == null){
								autowiredObjects[fieldKeyName] = mw3.getAutowiredObject(classNameForAutowiring);
							}
						}
					}

					var returnValue;
					
					var objectKey = this._createObjectKey(object);
					
					//This lets the called object doesn't have identifier, it should be focused to be set result if the result is it's type.
					if(objectKey && objectKey.indexOf("@")==-1)
						this.objectId_KeyMapping[objectKey] = objId;

					

					if(serviceMethodContext && serviceMethodContext.target!="none"){

						if(serviceMethodContext.loadOnce && serviceMethodContext['cachedObjectId']){
							return this.__showResult(object, this.objects[serviceMethodContext['cachedObjectId']], objId, svcNameAndMethodName, serviceMethodContext, placeholder, divId );
						}
					}
					
					
					var metaworksService = new MetaworksService(className, object, svcNameAndMethodName, autowiredObjects, objId, divId, placeholder, callback, sync, serviceMethodContext);
					this.metaworksServices.push(metaworksService);
					
					metaworksService.setIndex(this.metaworksServices.indexOf(metaworksService));
					
					returnValue = metaworksService.call();
					
					///// just after call, 
					//CollectGarbage();
					
					if(serviceMethodContext.target=="none"){
						return returnValue;
					}
					
//					mw3.debug("call render done");
					
					if(serviceMethodContext.target=="popup" || serviceMethodContext.target=="stick"){
						return this;
					}
					return this._withTarget(objId);
					
				}
				

				
				//$(infoDivId).html("<font color=red> LOADING... </font>");

				eval(svcNameAndMethodName+"(object, {async: false, callback: function(obj){mw3.setObject(objId, obj)}});");
				return this._withTarget(objId);

			};

			Metaworks3.prototype.___copyBeanPathsOnly = function(origValue, autowiredBeanPaths){
				if(autowiredBeanPaths){

					var filteredValue = {__className: origValue.__className, metaworksContext: origValue.metaworksContext};


					if(!autowiredBeanPaths.length){
						autowiredBeanPaths = [autowiredBeanPaths];
					}

					for(var i=0; i<autowiredBeanPaths.length; i++){
						var beanPath = autowiredBeanPaths[i];

						var whereCondition = beanPath.indexOf('[');

						if (whereCondition > -1){
							var whereConditionEnd = beanPath.indexOf(']');

							var conditionPart = beanPath.substring(whereCondition+1, whereConditionEnd);
							var arrayPropName = beanPath.substring(0, whereCondition);
							var arrayBeanPath = beanPath.substring(whereConditionEnd+1);
							var arrayOfOrigValue = eval("origValue." + arrayPropName);//origValue[arrayPropName];

							filteredValue = this.___beanCopy("."+arrayPropName, origValue, filteredValue);
							eval("filteredValue." + arrayPropName + " = []");

							for(var j=0; j<arrayOfOrigValue.length; j++){

								var accept = false;
								var value = origValue;
								var __index = j;

								with(arrayOfOrigValue[j]){
									try{
										eval("accept = " + (conditionPart));
									}catch(ex){console.log(ex);}
								}

								if(!accept) continue;

								var elem = this.___beanCopy(arrayBeanPath, arrayOfOrigValue[j]);

								eval("filteredValue." + arrayPropName + ".push(elem)");

							}
						}else{

							if(beanPath.indexOf(".") > -1){

								filteredValue = this.___beanCopy("."+ beanPath, origValue, filteredValue);

// 								var propName = beanPath.split(".")[0];

// 								var paths = beanPath.split(".");

// 								for(var pathIdx=1; pathIdx < paths.length; pathIdx++){
// 									path = paths[pathIdx];
// 									fullPath = fullPath + "." + path;

// 										(pathIdx == paths.length -1) {

// 								}

// 								eval("filteredValue." + propName + " = filteredValueWithProp." + propName);

							}else{
								eval("filteredValue." + beanPath + " = origValue." + beanPath);
							}
						}

					}

					return filteredValue;
				}


				return origValue;

			}

			Metaworks3.prototype.___beanCopy = function(arrayBeanPath, origValue, existingValue){
				var elem = {
					__className: origValue.__className
				};

				if(existingValue)
					elem = existingValue;

				if(arrayBeanPath.indexOf(".") > -1){

					var fullPath = "";
					var paths = arrayBeanPath.split(".");

					for(var pathIdx=1; pathIdx < paths.length; pathIdx++){
						path = paths[pathIdx];
						fullPath = fullPath + "." + path;

						if(existingValue && eval("elem" + fullPath)) continue;

						if(pathIdx == paths.length -1) {
							eval("elem" + fullPath + " = origValue" + fullPath);
						}else{
							eval("elem" + fullPath + " = {__className: origValue" + fullPath + ".__className}");
						}
					}

				}else
					elem = origValue;

				return elem;
			}


			Metaworks3.prototype.showInfo = function(objId, message){
				var infoDivId = "#"+this._getInfoDivId(objId);

				$(infoDivId).html("<center><font color=eeeeee> " + message + "</font></center>");
				$(infoDivId).slideDown(500, function(){
					setTimeout(function() {
						$( infoDivId ).slideUp(500);
					}, 5000 );
				});
			};
			
			Metaworks3.prototype.showError = function(objId, message, methodName){
				if( this.getFaceHelper(objId) && this.getFaceHelper(objId).endLoading)
					this.getFaceHelper(objId).endLoading();	
				else
					mw3.endLoading();
				
				var infoDivId = "#"+this._getInfoDivId(objId);				
				$(infoDivId).html("<center><font color=red> " + message + "<input type=button onclick=\"mw3.getObject('" + objId + "')."+ methodName + "()\" value='RETRY'></font></center>");
			};
			
			Metaworks3.prototype.alert = function(message){
				alert(this.localize(message));
			};
			
			Metaworks3.prototype.showValidation = function(objId, inputObjectId, isValid, message){
				var infoDivId = "#"+this._getInfoDivId(objId);
				
				if(!isValid)
					$(infoDivId).html("<center><font color=red> " + mw3.localize(message) + "</font></center>");
				else
					$(infoDivId).html("");
			};
			
			
			Metaworks3.prototype._armObject = function(objId, object){
				if(!object || !object.__className) return;
				
				if(object.__className=="Number" || object.__className=="String"){
					return; 
				}


				if(object.__objectId==null && objId==null){//means needed to be stored in the objectId    //TODO: may cause dangling object and memory leak
					mw3.objects[objId = ++mw3.objectId] = object;
				}

				if(objId==null && object.__objectId!=null){
					objId = object.__objectId;
				}
				
				object['__objectId'] = objId;

				var objectMetadata = this.getMetadata(object.__className);
    			
			   for(var methodName in objectMetadata.serviceMethodContextMap){
			   		var methodContext = objectMetadata.serviceMethodContextMap[methodName];
			   		
			   		//if(methodContext.clientSideCall)
			   		//	eval("object['"+methodName+"'] = function(){return mw3.clientSideCall(this.__objectId, '"+methodName+"');}");
			   		//else{
			   			eval("object['"+methodName+"'] = function(getAgain, callback){return mw3.call(this.__objectId, '"+methodName+"', getAgain, false, callback);}");			   			
//			   		}
			   }
			   
			   
			   object['__toString'] = function(){
				   
				   var metadata = objectMetadata;
				   var nameFieldValue = this;
				   
				   while(metadata && metadata.nameFieldDescriptor!=null){
					   nameFieldValue = nameFieldValue[metadata.nameFieldDescriptor.name];
					   
					   if(typeof nameFieldValue == 'string'){
						   return nameFieldValue;
						   
					   }else{
						   if(nameFieldValue && nameFieldValue.__className){
							   metadata = mw3.getMetadata(nameFieldValue.__className);
						   }else
							   metadata = null;
					   }
					   
				   }
			   
				   return objectMetadata.displayName;
				   
			   }
			   
			   object['__getFaceHelper'] = function(){
				   return mw3.getFaceHelper(this.__objectId);
			   }
			   
			   object['getFaceHelper'] = object['__getFaceHelper']; //TODO: the previous __getFacehelper needed to be deprecated permantly some day.
			   
			};

			//TODO: looks better than _armObject
//			Metaworks3.prototype.armObject = function(object){
//				if(!object || !object.__className) return;
//				
//				if(object.__className=="Number" || object.__className=="String"){
//					return; 
//				}
//
//					
//				var objectMetadata = this.getMetadata(object.__className);
//    			
//			   for(var methodName in objectMetadata.serviceMethodContexts){
//			   		var methodContext = objectMetadata.serviceMethodContexts[methodName];
//			   		
//			   		if(methodContext.clientSideCall)
//			   			eval("object['"+methodName+"'] = function(){return mw3.clientSideCall(this, '"+methodName+"');}");
//			   		else
//			   			eval("object['"+methodName+"'] = function(getAgain){return mw3.call(this, '"+methodName+"', getAgain);}");
//			   }
//			   
//			   object['__toString'] = function(){
//				   
//				   if(objectMetadata.nameFieldDescriptor!=null){
//					   var nameFieldValue = this[objectMetadata.nameFieldDescriptor.name];
//					   
//					   if(nameFieldValue && nameFieldValue.__toString){
//						   return nameFieldValue.__toString();
//					   }else{
//						   return nameFieldValue;
//					   }
//				   }else{
//					   return objectMetadata.displayName;
//				   }
//			   }
//			   
//			}

			
			function showupInstruction(methodDivId, instruction, options){
				   	var methodDiv = $("#" + methodDivId);
				   	
				   	if(instruction.indexOf("$")==0){
				   		instruction = mw3.localize(instruction);
				   	}
				   	
				   	var targetObject = methodDiv.children()[0];
				   	
				   	if(!targetObject)
				   		targetObject = methodDiv[0];
				   	
				   	if(!targetObject && console)
				   		console.log("There's no div id with '"+methodDivId + "'");

				   	if(!targetObject.offsetWidth && console)
				   		console.log("There's no offset width with div id '"+methodDivId + "'");

				   	$('body').append("<div id='instructionR' onclick=\"this.style.display='none'\"><div class='instructionDisc'>" + instruction + "</div></div>");

					$("#instructionR").css({"top": methodDiv.offset().top - 60 + "px", "left": (methodDiv.offset().left + targetObject.offsetWidth - 350) + "px" });
//					$("#instruction").slideDown(500);
					
					$( "#instructionR" ).effect( 'pulsate', 300 );
					
					if(options && options.onclick)
						$("#instructionR").onclick(function(){options.onclick();});
					
//					setTimeout(function(){
//						$( "#instruction" ).effect( 'shake',300 );
//						
//						setTimeout(function(){
//							$( "#instruction" ).effect( 'shake',300 );
//						},700);				
//					},700);		

			};

			Metaworks3.prototype.startTest = function(scenarioName, options){
				this.test(null, scenarioName + "[0]", options);
			};

			Metaworks3.prototype.test = function(objectId, testName, options){
				  $( "#instructionR" ).slideUp(500, function(){
					  $('#instructionR').remove();							  
				  });
				  

				var guidedTour = options && options['guidedTour'];
				
				if(options==null)
					options = {};
				
				if(!options['scenarioName'])
					options['scenarioName'] = testName;
				
				var recordedTest = false;
				var testIndex =0;
				
			   if(objectId==null && testName.indexOf("[") > -1){ // in case that the test is recorded one
				   var scenarioNameAndIndex = testName.split("[");
				   var scenarioname = scenarioNameAndIndex[0];
				   testIndex = eval(scenarioNameAndIndex[1].split("]")[0]);

				   test = mw3.testSet[scenarioname][testIndex];
				   
				   if(test==null){
					   
					   if(console)
						   console.log('Test ' + scenarioname + ' successfully done.');
					   else
						   alert('Test ' + scenarioname + ' successfully done.');
					   
					   return;
				   }

				   test.scenario = scenarioname;

				   recordedTest = true;

			   }else{ // in case that the test is inside the annotation

					var value = this.objects[objectId];
					
					var objectMetadata = this.getMetadata(value.__className); 
					
	//				var testStarter;
					
					//tests is indexed test hashmap for easily finding test by class name
					if(!this.tests[value.__className]){
						
						this.tests[value.__className] = {};
						var testsForTheClass = this.tests[value.__className];
						
						for(var i=0; i<objectMetadata.fieldDescriptors.length; i++){
							var fieldDescriptor = objectMetadata.fieldDescriptors[i];
							if(fieldDescriptor.attributes){
								var scenario = fieldDescriptor.attributes['test'];
								
								if(scenario){
									for(var scenarioName in scenario){
										var test = scenario[scenarioName];
										
										test['fieldName'] = fieldDescriptor.name;
										
										testsForTheClass[scenarioName + "." + fieldDescriptor.name] = test;
										if(test.starter){
											testsForTheClass[testName] = test;
										}
									}
								}
								
							}
							
						}
						
					   for(var methodName in objectMetadata.serviceMethodContexts){
					   		var methodContext = objectMetadata.serviceMethodContexts[methodName];
					   		
					   		if(methodContext.attributes){
								var scenario = methodContext.attributes['test'];
								
								if(scenario){
									
									for(var scenarioName in scenario){
										var test = scenario[scenarioName];
									
										test['methodName'] = methodContext.methodName;
										testsForTheClass[test.scenario + "." + methodContext.methodName + "()"] = test;
										if(test.starter){
											testsForTheClass[testName] = test;
										}
									}
									
								}
								
							}
					   		
					   }
					}
					
					//해당 시작한 클래스명에 대한 테스트들을 끌어옴. 근데, 이는 recorded 된 test들에 대해서는 별 의미가 없음.
				   var testsForTheClass = this.tests[value.__className];  
				   
				   if(testName.indexOf(".") == -1 && options && options.scenarioName && options.scenarioName!=testName){
					   testName = options.scenarioName + "." + testName;
				   }
			   
				   test = testsForTheClass[testName];
			   
				   if(test==null){
					   
						   alert('test is null. Please check your starter @Test name is same with the scenario name or set by starter=true.');
				   
				   			return;
				   }
			   }
			   
			   
			   var next = null;
			   if(recordedTest){
				   next = test.scenario + "[" + (testIndex+1) + "]";
				   
			   }else{
				   next = (test.next && test.next[0] ? test.scenario + "." + test.next[0] : null);
			   }

			   if(test.fieldName){

				   if(guidedTour){
						var beanPaths = this.beanExpressions[value.__objectId];
						if(beanPaths){
							var beanPath = beanPaths["."+test.fieldName];

						   var fieldDivId = "objDiv_" + beanPath.valueObjectId;
						   //var fieldDiv = $("#" + fieldDivId);
						   
						   var instruction = (test.instruction && test.instruction[0] ? test.instruction[0] : "Enter Here");
							
						   if(next)
						   //next = "\""+next+"\"";
							   instruction = instruction + "<input type=button value='Next' onclick=\"mw3.test(" + value.__objectId + ", '" + next + "',{guidedTour:true})\">";
						   else{
							   instruction = instruction + "<input type=button value='Done !' onclick=\"$('#instructionR').remove()\">";
							   
						   }
						   
						   
						   setTimeout(
								function(){
							   		showupInstruction(
						   				fieldDivId, 
						   				instruction
//						   				, 
//							   			{
//									   		onclick: function(){
//									   			mw3.test(, next, options);
//									   		}
//										}
							   		);
						   		}, 
						   		500
						   	);
						   
						}
				   }else{
					   
					   if(test.value!=null){					   
						   value[test.fieldName] = eval(test.value[0]);
						   this.setObject(objectId, value);
					   }

					   if(next && next.indexOf("autowiredObject.") >= 0){
					   		var posLastDot = next.lastIndexOf(".");
					   		
					   		var prefixLength = "autowiredObject.".length + next.indexOf(".") + 1;
					   		
					   		var className = next.substr(prefixLength, posLastDot - prefixLength);
					   		var methodName = next.substr(posLastDot + 1);
						   						   
						   value = this.getAutowiredObject(className);
						   
						   this.test(value.__objectId, methodName, options);
						   
					   } else{
						   value = this.objects[objectId];

						   this.test(value.__objectId, next, options);

					   }					   


				   }


					   
			   }else{  //---- in case of method ------
				   
				   
				   var returnValue;
				   
				   if(test.methodName){
					   
						  /// installing the call handler to continue after the call 
						  mw3.afterCall = function(methodName, result){
							  
							  if(methodName != test.methodName) return;

							  if(recordedTest){
								  mw3.test(null, next, options);
								  
								  return;
							  }

							   //var next = (test && test.next && test.next[0] ? test.scenario + "." + test.next[0] : null);

							   if(next==null){  // detect end!
								   mw3.afterCall = null;
								   
								  $( "#instructionR" ).slideUp(500, function(){
									  $('#instructionR').remove();							  
									   //TODO: done message should be here!
									  
									  //var congratulations = "Congratulations! Your guided Tour has been finished.";
									  //congratulations = mw3.localize("$congratulations");
									   mw3.alert("$congratulations");
								  });

								   return;
							   }
							   
							   if(next && next.indexOf("returnValue.") >= 0){
								   if(returnValue.metaworksMetadata){
									   value = mw3.objects[this.targetObjectId];
									   
								   }else{
									   value = returnValue;
								   }
								   
								   //next = next.substr("returnValue.".length);
								   
								   mw3.test(value.__objectId, testName, options);
							   }else if(next && next.indexOf("autowiredObject.") >= 0){
							   		var posLastDot = next.lastIndexOf(".");
							   		
							   		var prefixLength = "autowiredObject.".length + next.indexOf(".") + 1;
							   		
							   		var className = next.substr(prefixLength, posLastDot - prefixLength);
							   		
							   		var methodName = next.substr(posLastDot + 1);

							   		if(className.indexOf("@") > -1){
							   			value = this.objects[this.objectId_KeyMapping[className]]; 
							   		}else{
							   			
									    value = this.getAutowiredObject(className);
							   			
							   		}
							   		
							   
								   
//								   var objectIdFirst = value.__objectId;
//								   for(var i=objectIdFirst; i>0; i--){
//									   
//								   }
								   
								  if(value)
									  mw3.test(value.__objectId, methodName, options);
								  
							   } else{
								   value = mw3.objects[objectId];
								   
								  if(value)
									  mw3.test(value.__objectId, next, options);
							   }

						   }
	   
					   //in case that the recorded test...
					   var enterValueContext = null;
					   
					   if(recordedTest && test.value!=null){
						   
						   objectId = this.objectId_KeyMapping[test.objectKey];
						   
						   if(objectId==null){
							   
							   var errorMsg = "[Test] During test [" + test.scenario + "], object key [" + test.objectKey + "] is not found. Quit the test.";
							   if(console)
								   console.log(errorMsg);
							   else
								   alert(errorMsg);
							   
							   return;
						   }
						   

						   //setting values for only existing property value except the null property
						   var value = this.objects[objectId];
						   var metadata = this.getMetadata(test.value.__className);
						   for(var idx in metadata.fieldDescriptors){
							   
							   var fieldDescriptor = metadata.fieldDescriptors[idx];
							   if(test.value[fieldDescriptor.name]){
								   enterValueContext = (enterValueContext ? enterValueContext + ", " : "") + fieldDescriptor.displayName + " : " + test.value[fieldDescriptor.name];
								   
								   value[fieldDescriptor.name] = test.value[fieldDescriptor.name];								   
							   }else
							   	   delete value[fieldDescriptor.name];
							   
						   }
					   }
							   

					   if(guidedTour){
						   
						   if(recordedTest){
							   test.instruction = [];
							   test.instruction[0] = ((enterValueContext && false) ? "Enter " + enterValueContext + "\n and " : "") + "Click " + test.methodName + ".";
						   }
						   
						   var methodDivId = "method_" + value.__objectId + "_" + test.methodName;
						   var methodDiv = $("#" + methodDivId);
						   
						   
						   var instruction = (test.instruction && test.instruction[0] ? test.instruction[0] : "Click Here");

							
							setTimeout("showupInstruction('"+methodDivId+"','"+ instruction +"')", 700);
						  
					   }else{
						   

						   returnValue = this.call(value.__objectId, test.methodName, true, true, null, true); //sync call
					   }
					   
					   
				   }  
				   
			   }
				
			};

			Metaworks3.prototype._wireObject = function(value, objectId){ //TODO: need to give someday '__objectId' to all the values?

				if(value && value.__className){
					
					var objectMetadata = this.getMetadata(value.__className); 
	
					for(var i=0; i<objectMetadata.superClasses.length; i++){
						var className = objectMetadata.superClasses[i];
						
						this.objectId_ClassNameMapping[className] = objectId; //TODO: may problematic due to the last instance will replace old value.
					} 
					
					
					for(var i=0; i<objectMetadata.fieldDescriptors.length; i++){
						var fieldDescriptor = objectMetadata.fieldDescriptors[i];
						if(fieldDescriptor.attributes && fieldDescriptor.attributes['autowiredtoclient']){ //means this field never have change to registered or autowired since it is not called by ObjectFace.ejs or custom faces.
							var fieldValue = value[fieldDescriptor.name];
							
							if(fieldValue && fieldValue.__className){
								var objectMetadataFD = this.getMetadata(fieldValue.__className); 
								
								for(var j=0; j<objectMetadataFD.superClasses.length; j++){
									var className = objectMetadataFD.superClasses[j];
									
									this.objectId_ClassNameMapping[className] = {value: fieldValue, __isAutowiredDirectValue: true};
								}
							}
							
						}
					}
				}
			};
			


			Metaworks3.prototype.editObject = function(){
				
				
				var objectId;
				var objectTypeName;
				
				if(arguments.length == 0 ){
					objectId 		= this.targetObjectId;
				}else
				if(arguments.length == 1 ){
					objectTypeName 	= arguments[0];
					objectId = this.targetObjectId;
				}else if(arguments.length == 2){
					objectId 		= arguments[0];
					objectTypeName 	= arguments[1];
				}
				

				var when = mw3.when;
				
				mw3.setWhen(mw3.WHEN_EDIT);
				
				var object = this.getObject(objectId);
				
				if('java.lang.String' != objectTypeName){
					if(!object.metaworksContext)
						object.metaworksContext = {}
				
					object.metaworksContext['when'] = mw3.WHEN_EDIT;
				}
	 			
				mw3.showObjectWithObjectId(
						objectId,
						objectTypeName,
						"#objDiv_" + objectId
				);
				
				mw3.setWhen(when);
				
				//TODO: its kind of dummy.
				//mw3.loadFaceHelper(objectId);
	 			
	 			return this._withTarget(objectId);
			};
			

			
			Metaworks3.prototype._createKeyObject = function(object){
				if(!object || !object.__className){
					return object;
				}
				
				if(object.__className=="Number" || object.__className=="String")
					return object;
				
				var keyObject = {__className: object.__className};
				var objectMetadata = this.getMetadata(object.__className);

				keyObject["metaworksContext"] = object["metaworksContext"]; //carries metaworksContex as well.
				
				if(objectMetadata.keyFieldDescriptor == null)//if there's no key field. just send it's class name and metaworksContext only
					return keyObject;
					//throw "Service class '" + object.__className + "' doesn't have key field. You have to annotate @org.metaworks.annotation.Id to the service class' GETTER method. NOT the SETTER.";
				
				var keyFieldName = objectMetadata.keyFieldDescriptor.name;
				
				keyObject[keyFieldName] = this._createKeyObject(object[keyFieldName]);
			
				return keyObject;
			};
			
			Metaworks3.prototype._createObjectRef = function(object, objectId){
				if(!object || !object.__className) return null;
				
				if(object.__className=="Number" || object.__className=="String"){ return null; }

					
				var fields = {};
				var methods = {};
				
				var objectMetadata = this.getMetadata(object.__className);
				
				for(var i=0; i<objectMetadata.fieldDescriptors.length; i++){
					var fd = objectMetadata.fieldDescriptors[i];
					fields[fd.name] = new FieldRef(object, objectId, fd);
				}
				
				
			    if(objectMetadata.serviceMethodContexts){
				   for(var key in objectMetadata.serviceMethodContexts){
				   		var methodContext = objectMetadata.serviceMethodContexts[key];
				   		
				   		methods[methodContext.methodName] = new MethodRef(object, objectId, methodContext);
				   }
			    }

				methods['contextMenus'] = new MethodRef(object, objectId, {methodName: 'contextMenus'});

				var getField = function(fieldName){
					if(fields[fieldName]) return fields[fieldName];

					throw new Exception("Field [" + fieldName + "] is not defined in your class: " + object.__className + ".");
				};

				var getMethod = function(methodName){
					if(methods[methodName]) return methods[methodName];

					throw new Exception("Method [" + methodName + "] is not defined in your class: " + object.__className + ".");
				};

				var here = function(name){
					if(fields[name]) return fields[name].here();
					if(methods[name]) return methods[name].here();

					throw new Exception("No Field or Method [" + name + "] is not defined in your class: " + object.__className + ".");
				};

				var caller = function(name){
					if(methods[name]) return methods[name].caller();

					throw new Exception("No Method [" + name + "] is not defined in your class: " + object.__className + ".");
				};


				var objectRef={object: object, objectId: objectId, objectMetadata: objectMetadata, fields: fields, methods: methods, getField: getField, getMethod: getMethod, here: here, caller: caller};
				return objectRef;
			};
			
			Metaworks3.prototype.isHidden = function(field){
				if(!field) return false;

				return this.isHiddenFieldDescriptor(field.fieldDescriptor, field.object);
			}
			
			Metaworks3.prototype.isHiddenFieldDescriptor = function(fd, object){
				var isHidden = false;
				
				var metaworksContext = this.getContext();
				
				if(object.metaworksContext)
					metaworksContext = object.metaworksContext;
				
				if(object.__objectId){
					metaworksContext = this.objectContexts[object.__objectId]['__metaworksContext'];
					
					if(metaworksContext.when == "__design"){
						return !fd.attributes['resource']; 
					}
				}
				
				if(fd.attributes){
					if(fd.attributes['hidden.when']){
						if(fd.attributes['hidden.when'] == metaworksContext.when)
							isHidden = true;
					}

					if(fd.attributes['hidden.where']){
						if(fd.attributes['hidden.where'] == metaworksContext.where)
							isHidden = true;
					}

					if(fd.attributes['hidden.how']){
						if(fd.attributes['hidden.how'] == metaworksContext.how)
							 isHidden = true;
					}
					
					// && metaworksContext.when == '___hidden___'

					if(fd.attributes['hidden']) 
						isHidden = true;
						
					if(fd.attributes['show.when']){
						if(fd.attributes['show.when'] != metaworksContext.when)
							isHidden = false;
					} 
					
					if(fd.attributes['available.when'] || 
					   fd.attributes['available.where'] ||
					   fd.attributes['available.how'] ||
					   fd.attributes['available.condition']){
					   
					   isHidden = true;
					}
					
					if(fd.attributes['available.when']){
						if(fd.attributes['available.when'][metaworksContext.when] != null)
							isHidden = false;
					} 
					
					if(fd.attributes['available.where']){
						if(fd.attributes['available.where'][metaworksContext.where] != null)
							isHidden = false;
					} 

					if(fd.attributes['available.how']){
						if(fd.attributes['available.how'][metaworksContext.how] != null)
							isHidden = false;
					} 

					if(fd.attributes['available.condition'] && object){
						for(var key in fd.attributes['available.condition']){
							var condition = fd.attributes['available.condition'][key]; 
							var validateCondition = true;

							var evaluatedObject = object;
							if(object.__className
								&& mw3.isSuperClassOf("org.metaworks.Face", object.__className)
								&& object._realValue){

								evaluatedObject = object._realValue;
							}

			    			if(condition != null){
			    				with(evaluatedObject){
			    					try{
			    						validateCondition = eval(condition);
			    					}catch(e){
			    						validateCondition = false;
			    					}
			    				}
			    			}			
			    			
			    			if(validateCondition)
			    				isHidden = false;
						}
					}					
				} 
				
				return isHidden;
			};
			
			Metaworks3.prototype.isHiddenMethod = function(method){
				return this.isHiddenMethodContext(method.methodContext, method.object);
			};
			
			Metaworks3.prototype.isHiddenMethodContext = function(methodContext, object){
				var isHidden = false;

				if(!this.objectContexts[object.__objectId]) return false;

				var metaworksContext = this.objectContexts[object.__objectId]['__metaworksContext'];
				
				if(methodContext.attributes){
					if(methodContext.attributes['hidden.when']){
						if(methodContext.attributes['hidden.when'][metaworksContext.when]!=null){
							isHidden = true;
						}
					}
					
					if(methodContext.attributes['hidden.where']){
						if(methodContext.attributes['hidden.where'][metaworksContext.where]!=null){
							isHidden = true;
						}
					}
	
					if(methodContext.attributes['hidden.how']){
						if(methodContext.attributes['hidden.how'][metaworksContext.how]!=null){
							isHidden = true;
						}
					}
					
					if(methodContext.attributes['hidden']) {
						isHidden = true;
					}

				}

				if((methodContext.when && methodContext.when.indexOf('whenever|') == -1) || 
				   (methodContext.where && methodContext.where.indexOf('wherever|') == -1) || 
				   (methodContext.how) || 
				   (methodContext.attributes && methodContext.attributes['available.condition'])){
				 	
					isHidden = true;
				}
				
				if(methodContext.when != null && metaworksContext.when.length > 0 && methodContext.when.indexOf('whenever|') == -1){
					if(methodContext.when.indexOf(metaworksContext.when + '|') > -1)
						isHidden = false;						
				}
				
				if(methodContext.where != null && metaworksContext.where.length > 0 && methodContext.where.indexOf('wherever|') == -1){
					if(methodContext.where.indexOf(metaworksContext.where + '|') > -1)
						isHidden = false;						
				}

				if(methodContext.how != null && metaworksContext.how.length > 0){
					if(methodContext.how.indexOf(metaworksContext.how + '|') > -1)
						isHidden = false;						
				}

				if(methodContext.attributes && methodContext.attributes['available.condition'] && object){
					for(var key in methodContext.attributes['available.condition']){
						var condition = methodContext.attributes['available.condition'][key];
						var validateCondition = false;
						if(condition != null){

							try{
								with(object)
									validateCondition = eval(condition);
							}catch(e){
								console.log("Error in condition statement:");
								console.log(e);
							}
						}

						if(validateCondition){
							isHidden = false;
						}
					}
				}
				
				return isHidden;				
			}
			
			Metaworks3.prototype.validationCondition = function (validator, object){
				var validationCondition = true;
				
    			var validationConditionExpression = validator.condition;    			
    			if(validationConditionExpression != null){
    				with(object){
    					validationCondition = eval(validationConditionExpression);
    				}
    			}			
    			
    			return validationCondition;
			}
			
			Metaworks3.prototype.validationAvailableUnder = function (validator, object){
				var validationAvailableUnder = false;
				
				if(validator){
					if(validator instanceof Array){
						for(var i=0; i<validator.length; i++){
			    			var validationAvailableUnderExpression = validator[i].availableUnder;    			
			    			if(validationAvailableUnderExpression != null){
			    				with(object){
			    					validationAvailableUnder = eval(validationAvailableUnderExpression);
			    				}
			    			}								
						}
					}else{
		    			var validationAvailableUnderExpression = validator.availableUnder;    			
		    			if(validationAvailableUnderExpression != null){
		    				with(object){
		    					validationAvailableUnder = eval(validationAvailableUnderExpression);
		    				}
		    			}						
					}
				}
				
    			return validationAvailableUnder;
			};
			
			Metaworks3.prototype.validField = function (objId, fieldName){
				var object = this.getObject(objId);
				
				return this.validObject(object, fieldName);
			}
			
			Metaworks3.prototype.validObject = function(object, name){
				var metadata = this.getMetadata(object.__className);
				var isValid = true;
				var objId = object.__objectId;
				
				if(metadata && metadata.fieldDescriptors && metadata.fieldDescriptors.length > 0){
					var fieldOrder = [];
					
					if(metadata.faceOptions && metadata.faceOptions.fieldOrder){
						var fieldOrderValue = metadata.faceOptions.fieldOrder;
						var newFieldOrder = [];
						
						if(fieldOrderValue.indexOf(',')>0){
							tempOrder = fieldOrderValue.split(',');
						}else{
							tempOrder.push(fieldOrderValue);
						}
						
						for (var i=0; i<tempOrder.length; i++){
							var tempSubOrder = [];
							
							if(tempOrder[i].indexOf('-') > -1){
								tempSubOrder = tempOrder[i].split('-');						
							}else{
								tempSubOrder.push(tempOrder[i]);
							}
							
							for (var j=0; j<tempSubOrder.length; j++){
								for(var k = 0; k < metadata.fieldDescriptors.length; ++k){
									var fd = metadata.fieldDescriptors[k];
									
									if(fd.name == tempSubOrder[j]){
										fieldOrder.push(k);
										
										break;
									}
								}
							}
								
						}
						
						for(var i = 0; i < metadata.fieldDescriptors.length; i++){
							var isExistField = false;
							
							for (var j=0; j<fieldOrder.length; j++){
								if(i == fieldOrder[j]){
									isExistField = true;
									
									break;
								}
							}
							
							if(!isExistField)
								fieldOrder.push(i);
							
						}
					}else{
						for(var i = 0; i < metadata.fieldDescriptors.length; ++i){
							fieldOrder.push(i);
						}
					}
										
					for(var i = 0; i < fieldOrder.length; i++){					   
						if(!isValid)
							continue;
						
						var fd = metadata.fieldDescriptors[fieldOrder[i]];
					   
						if(typeof name != 'undefined' && fd.name != name)
							continue
							
						try {
							var isObject = object[fd.name] instanceof Object;
							var isArray = Object.prototype.toString.call(object[fd.name]) == '[object Array]';
						   
							if(isArray){
								for(var j=0; j<object[fd.name].length; j++){
									if(!isValid)
										continue;
								   
									if(object[fd.name][j].__className)
										if(!this.validObject(object[fd.name][j]))
											isValid = false;									   
								}
							}else{
								if(isObject && fd.name == 'metaworksContext')						   
									continue;
								   
								if(fd.attributes.validator && object.__objectId) {
						    		for(var j = 0; j < fd.attributes.validator.length; ++j) {						    			
						    			var validator = fd.attributes.validator[j];
						    			
						    			if(validator.availableUnder == null || validator.availableUnder == ''){
						    				if(this.validationCondition(validator, object)){
								    			var result = this.validation(object, fd.name, validator);
								    			var inputObjectId = this.getChildObjectId(object.__objectId, fd.name);
								    			var message;
								    			
						    					if(!result)				    						
						    						message = this.getValidationMessage(fd, validator)
						    					
												if(this.getFaceHelper(objId) && this.getFaceHelper(objId).showValidation)
													this.getFaceHelper(objId).showValidation(inputObjectId, result, message);
												else
													this.showValidation(objId, inputObjectId, result, message);
												
												if(!result){
													isValid = false;
													
													break;
												}
						    				}
						    			}
						    		}
							   }
							   
							   if(isObject){
								   if(object[fd.name].__className){
//									   if(this.validationAvailableUnder(fd.attributes.validator, object)){
										   if(!this.validObject(object[fd.name]))
											   isValid = false;
//									   }
								   }
							   }   
						   }

				    	} catch(e) {
				    		if(window.console)
				    			console.log('error = ' + e);
				    	}
				   }
				}
				
				return isValid;
			};
			
			Metaworks3.prototype.validation = function(object, name, validator){
			
				var validation = true;
				var value = object[name];
				
				if(validator.name == 'notnull'){
					if(value == null || value == '')
						validation = false;
				}else if(validator.name == 'null'){
					if(value != null && value != '')
						validation = false;
				}else if(validator.name == 'maxbyte' && validator.options){
					var len = 0;
			        for (var i = 0; i < value.length; i++) {
			            if (value.charCodeAt(i) > 128) {
			                len++;
			            }
			            len++;
			        }			
					if(len > Number(validator.options[0]))
						validation = false;				
					
				}else if(validator.name == 'maxlength' && validator.options){
					var length = 0;
					if(value)
						length = value.length;
					
					if(length > validator.options[0])
						validation = false;					
				}else if(validator.name == 'minlength' && validator.options){
					var length = 0;
					if(value)
						length = value.length;
					
					if(length < validator.options[0])
						validation = false;					
				}else if(validator.name == 'numberzero'){
					if(value == 0)
						validation = false;
				}else if(validator.name == 'regularexpression'){
					if(validator.options){
						for(var i=0; i<validator.options.length; i++){
							var exp = eval(validator.options[i]);
							if(!exp.test(value)){
								validation = false;
								
								break;
							}								
						}
					}
					
				}else if(validator.name == 'condition'){
					if(validator.options){
						for(var i=0; i<validator.options.length; i++){
							var validationCondition = validator.options[i];					
		    				with(object){
		    					validation = !(eval(validationCondition));
		    				}							
						}
					}
				}else if(validator.name == 'asserttrue'){
					if(value != true)
						validation = false;
					
				}else if(validator.name == 'assertfalse'){
					if(value != false)
						validation = false;
				}

				return validation;
			};
			
			Metaworks3.prototype.getValidationMessage = function(fd, validator){
				var message = null;
				
				if(typeof validator.message != 'undefined' && validator.message != null && validator.message.length > 0)
					message = validator.message;	
				else{
					if(validator.name == 'isnull')
						message = fd.displayName + '\'s fields is null.';
					else if(validator.name == 'maxbyte')
						message = fd.displayName + '\'s byte is greater than ' + validator.options[0] + '.';
					else if(validator.name == 'maxlength')
						message = fd.displayName + '\'s length value is greater than ' + validator.options[0] + '.';
					else if(validator.name == 'minlength' && validator.options)
						message = fd.displayName + '\'s value is less than ' + validator.options[0] + '.';
					else if(validator.name == 'numberzero')
						message = fd.displayName + '\'s value is zero.';
					else if(validator.name == 'regularexpression')
						message = fd.displayName + '\'s Regular expression failed inspection.';
				}
				
				return message;
			};
			
			
			Metaworks3.prototype.log = function(object){
				if(window.console)
					console.log(object);				
			};
			
			//브라우저 종류 및 버전확인  
			Metaworks3.prototype.browserCheck = function(){
				var name = "";
				var ver = 0;
				
				if(navigator.appName.charAt(0) == "N"){ 
					if(navigator.userAgent.indexOf("Chrome") != -1){
						name = "Chrome";						
					}else if(navigator.userAgent.indexOf("Firefox") != -1){
						name = "Firefox";
					}else if(navigator.userAgent.indexOf("Safari") != -1){
						name = "Safari";
					}
				}else if(navigator.appName.charAt(0) == "M"){
					name = "MSIE";
				}
				
				ver = getInternetVersion(name);
				
				return name + ' ' + ver;
			}; 	
			
			Metaworks3.prototype.localize = function(){
				//var message = original;
				var message = arguments[0];
				
				if(message != null && typeof message =='string' && message.indexOf('$')==0 && this.getMessage) {
					message = message.substring(1);
					message = this.getMessage(message);
				}
				
				return message;
			};
			
			Metaworks3.prototype.getMessage = function(message){
				return message;
			};
			Metaworks3.prototype.metadataBundle = function(message){
				return message;
			};
			
			Metaworks3.prototype.clone = function(obj) {
			    // Handle the 3 simple types, and null or undefined
			    if (null == obj || "object" != typeof obj) return obj;
			    
			    // Handle Date
			    if (obj instanceof Date) {
			        var copy = new Date();
			        copy.setTime(obj.getTime());
			        return copy;
			    }

			    // Handle Array
			    if (obj instanceof Array) {
			        var copy = [];
			        for (var i = 0, len = obj.length; i < len; i++) {
			            copy[i] = this.clone(obj[i]);
			        }
			        return copy;
			    }

			    // Handle Object
			    if (obj instanceof Object) {
			        var copy = {};
			        for (var attr in obj) {
			            if (attr != '__faceHelper' && obj.hasOwnProperty(attr)) copy[attr] = this.clone(obj[attr]);
			        }
			        return copy;
			    }

			    throw new Error("Unable to copy obj! Its type isn't supported.");
			};
						
			if(!Metaworks) alert('Metaworks DWR service looks not available. Metaworks will not work');
			if(!$().jquery) alert('JQuery library not installed. Metaworks will not work');
			
			var mw3 = new Metaworks3('template_caption', 'dwr_caption', Metaworks);
			
			mw3.windowFocus = true;
			mw3.windowActiveElement;

			try{
				mw3.windowActiveElement = document.activeElement;
				
				if(mw3.browser.indexOf('MSIE') > -1){
					$(document).focusin(function(event){
						mw3.windowFocus = true;
					}).focusout(function(event){
						if (mw3.windowActiveElement != document.activeElement) {
							mw3.windowActiveElement = document.activeElement;
							return;
						}
						mw3.windowFocus = false;
					});
				}else{
					$(window).focus(function(event){
						mw3.windowFocus = true;
					}).blur(function(event){
						if (mw3.windowActiveElement != document.activeElement) {
							mw3.windowActiveElement = document.activeElement;
							return;
						}
						mw3.windowFocus = false;
					});				
				}
			}catch(e){				
			}

			
			Metaworks3.prototype.serviceMethodBinding = function(objectId, objectTypeName){
				//load the key or mouse bindings, context menu 
				var contextMenuMethods = [];
				var object = mw3.objects[objectId];
				var metadata = null;
				
				if(objectTypeName)
					metadata = mw3.getMetadata(objectTypeName);
				
				var faceHelper = mw3.getFaceHelper(objectId);
				
				var targetDivId = mw3._getObjectDivId(objectId);
				var theDiv = $('#' + targetDivId);
				
				if(theDiv[0] && metadata) {


					if(metadata.submittedOnDrag){
						theDiv.draggable({
							appendTo: "body",
							helper: "clone",
							zIndex: 100,
							start: function (event, ui) {
								var className = $(this).attr('className');

								if (className) {
									$(".onDrop_" + className.split('.').join('_')).css("border-width", "3px").css("border-style", "dashed").css("border-color", "orange");
								} else {
									console.log('drag method need className of element. you probably use "refresh" statement in the first line of ejs. Add className.');
								}


							},
							drag: function (event, ui) {
							},
							stop: function () {
								var className = $(this).attr('className');

								if (className)
									$(".onDrop_" + className.split('.').join('_')).css("border-width", "").css("border-style", "").css("border-color", "");
							}
						});

					}


					for (var methodName in metadata.serviceMethodContextMap) {
						var methodContext = metadata.serviceMethodContextMap[methodName];

						//console.log('try : ' + methodName);

						if (mw3.isHiddenMethodContext(methodContext, object) && !methodContext.bindingHidden)
							continue;

						//console.log('pass : ' + methodName);
						//console.log(methodContext);

						// make call method
						var command = "if(mw3.objects['" + objectId + "']!=null) mw3.call(" + objectId + ", '" + methodName + "')";
						if (methodContext.needToConfirm) {
							if (this.needToConfirmMessage) {
								command = "if (confirm(\'" + mw3.localize(mw3.needToConfirmMessage, methodContext.displayName) + "'))" + command;
							} else {
								command = "if (confirm(\'Are you sure to " + mw3.localize(methodContext.displayName) + " this?\'))" + command;
							}
						}

						if (methodContext.eventBinding && methodContext.eventBinding.length > 0) {
							for (var i = 0; i < methodContext.eventBinding.length; i++) {
								var eventBinding = methodContext.eventBinding[i];

								for (var j = 0; j < methodContext.bindingFor.length; j++) {
									var bindingFor = methodContext.bindingFor[j];
									var bindingDivId;

									if ('@this' == bindingFor) {
										bindingDivId = '#' + targetDivId;
									} else if ('@page' == bindingFor) {
										bindingDivId = 'document';
									} else {
										var bindingFieldId = mw3.getChildObjectId(objectId, bindingFor);

										bindingDivId = '#' + mw3._getObjectDivId(bindingFieldId);
									}

									if(!$(bindingDivId).data("events") || !$(bindingDivId).data("events")[eventBinding]) { //only once
                                        $(bindingDivId).bind(eventBinding, {command: command}, function (event) {
                                            event.stopPropagation();

                                            eval(event.data.command);
                                        });
                                    }
								}
							}
						}

						if (methodContext.keyBinding && methodContext.keyBinding.length > 0) {
							for (var i = 0; i < methodContext.keyBinding.length; i++) {
								var keyBinding = methodContext.keyBinding[i];

								if (keyBinding.indexOf("@Global") > -1) {
									/*
									 * 2013-03-25 cjw
									 * global 일때 keyBinding 문제 해결
									 */
									keyBinding = keyBinding.substr(0, keyBinding.length - "@Global".length);

									shortcut.remove(keyBinding);
									shortcut.add(keyBinding, command);
								} else {
									shortcut.add(keyBinding, command/*function() {
									 eval(command);
									 }*/, {
										target: targetDivId
									});
								}
							}
						}

						//mouse binding installation
						if (methodContext.mouseBinding) {
							var which = 3;
							var eventType = "mouseup";
							if (methodContext.mouseBinding == "right") {
								which = 3;
								eventType = "mouseup";
							} else if (methodContext.mouseBinding == "left") {
								which = 1;
								eventType = "click";
							} else if (methodContext.mouseBinding == "dblclick" || methodContext.mouseBinding == "double-click") {
								which = 1;
								eventType = "dblclick";

							}

							if (methodContext.mouseBinding == "drag" || methodContext.mouseBinding == "drag-enableDefault") {
								if (faceHelper && faceHelper.draggable) {
									faceHelper.draggable(command);
								} else {
									theDiv[0]['dragCommand'] = command;
									theDiv.draggable({
										appendTo: "body",
										helper: "clone",
										zIndex: 100,
										start: function (event, ui) {
											var className = $(this).attr('className');

											if (className) {
												$(".onDrop_" + className.split('.').join('_')).css("border-width", "3px").css("border-style", "dashed").css("border-color", "orange");
											} else {
												console.log('drag method need className of element. you probably use "refresh" statement in the first line of ejs. Add className.');
											}

											eval(this['dragCommand']);
										},
										drag: function (event, ui) {
										},
										stop: function () {
											var className = $(this).attr('className');

											if (className)
												$(".onDrop_" + className.split('.').join('_')).css("border-width", "").css("border-style", "").css("border-color", "");
										}
									});
								}
							}//end of case 'drag start'
							else if (methodContext.mouseBinding == "drop") {
								theDiv[0]['dropCommand'] = command;

								theDiv.droppable({
									greedy: true,
									drop: function (event, ui) {
										mw3.dropX = event.pageX;
										mw3.dropY = event.pageY;

										$(this).removeClass('ui-state-active');

										if (mw3.debugMode) {
											console.log("Trying drop command: " + this['dropCommand']);
										}

										eval(this['dropCommand']);
									},

									over: function (event, ui) {

										//no need to over background - sometimes look dirty.
										//var className = ui.draggable.attr('classname');
										//
										//if(className){
										//	var dropName;
										//	if(className.indexOf('.') > -1)
										//		dropName = "onDrop_" + className.split('.').join('_');
										//	else
										//		dropName = className;
										//
										//	if($(this).hasClass(dropName)){
										//		$(this).addClass('ui-state-active');
										//	}
										//}
									},

									out: function (event, ui) {
										$(this).removeClass('ui-state-active');
									}
								});
							}//end of case 'drop'

							//case of general mouse click
							else {

								if (!theDiv[0]['mouseCommand' + which]) { //to ensure the event is only once registered.
									theDiv[0]['mouseCommand' + which] = command;

									// click(mouse right) is contextmenu block
									if (which == 3) {
										theDiv[0].oncontextmenu = function () {
											return false;
										};
									}

									var mouseEvent = function (e) {
										if (e.which == e.data.which) {
											e.stopPropagation(); //stops to propagate to parent that means consumes the event here.
											mw3.mouseX = e.pageX;
											mw3.mouseY = e.pageY;

											eval(this['mouseCommand' + e.which]);
										}
									};

									$(theDiv[0]).bind(eventType, {which: which}, mouseEvent);
								}
							}


						} // end of mouse binding installation

						if (methodContext.inContextMenu) {
							contextMenuMethods[contextMenuMethods.length] = methodContext;
						}

					}
				}
			   
			   //install context menu
			   if(contextMenuMethods.length > 0){				   
				   var menuItems = [];
				   var subMenuItems = [];
				   var makeSubMenu = false;
				   var prevGroup = null;
				   
				   for(var i=0; i<contextMenuMethods.length; i++){							   
					   var serviceMethodContext = contextMenuMethods[i];
					   
					   if(prevGroup != serviceMethodContext.group){
						   if(serviceMethodContext.group){
							   makeSubMenu = true;
						   }else{
							   makeSubMenu = false;
							   
							   var subMenu = {
								   text: prevGroup, 
								   submenu: {
									   id: prevGroup + objectId,
									   itemdata: subMenuItems
								   }
							   };
							   
							   menuItems[menuItems.length] = subMenu;
							   subMenuItems = [];
						   }
						   
						   prevGroup = serviceMethodContext.group;
					   }
					   
					   // context menu enable/disable 처리에 대한 faceHelper assist function 추가					   
					   if(mw3.getFaceHelper(objectId) && !methodContext.bindingHidden && mw3.getFaceHelper(objectId).ableContextMenu && !mw3.getFaceHelper(objectId).ableContextMenu(serviceMethodContext.methodName))
						   continue;

					   var command = "mw3.call("+objectId+", '"+serviceMethodContext.methodName+"')";

					   if(serviceMethodContext.needToConfirm) {						   
						   if(this.needToConfirmMessage){							   
							   command = "if (confirm(\'"+ mw3.localize(mw3.needToConfirmMessage, serviceMethodContext.displayName) +"\'))" + command;
						   }
						   else {
							   command = "if (confirm(\'Are you sure to "+mw3.localize(serviceMethodContext.displayName)+" this?\'))" + command;
						   }
					   }
				   		
					   var menuItem = { 
							   text: mw3.localize(serviceMethodContext.displayName) + (serviceMethodContext.keyBinding ? '(' + serviceMethodContext.keyBinding[0] + ')' : ''), 
							   onclick: { fn: 
								   function(){
								   		eval(this._oOnclickAttributeValue.command);
								   },
								   command: command
							   } 
					   };
					   
					   if(makeSubMenu)
						   subMenuItems[subMenuItems.length] = menuItem;
					   else
						   menuItems[menuItems.length] = menuItem;
				   }
				   
//				   theDiv.append("<div id='contextmenu_" + objectId + "'></div>");
				   
				   if(menuItems.length){
					   YAHOO.util.Event.onContentReady(targetDivId, function () {
					   		var menu = YAHOO.widget.MenuManager.getMenu("_contextmenu_" + objectId);
					   		if(menu)
					   			menu.destroy();
					   		
						    menu = new YAHOO.widget.ContextMenu(
								"_contextmenu_" + objectId,
								{
									zindex: 2000,
									trigger: theDiv[0],
									itemdata: menuItems,
									lazyload: true
								}
							);
					   });
					   
					   $('#' + targetDivId).attr('contextMenu', 'true');
				   }
			   }		
			}

			Metaworks3.prototype.getServiceMethodByGroup = function(objectId, groupId){
				var serviceMethods = [];
				var object = mw3.objects[objectId];
				var metadata = mw3.getMetadata(object.__className);
				
			    for(var methodName in metadata.serviceMethodContextMap){
			   		var methodContext = metadata.serviceMethodContextMap[methodName];
			   	
				    if(mw3.isHiddenMethodContext(methodContext, object) && !methodContext.bindingHidden)
					   continue;

		   			if(methodContext.group == groupId){
		   				serviceMethods[serviceMethods.length] = new MethodRef(object, objectId, methodContext);;
		   			}
		   		}
		   		
		   		return serviceMethods;
			};
			
			Metaworks3.prototype.makeMenuItemsByServiceMethod = function(serviceMethods){
				var menuItems = [];
				
				for(var i=0; i<serviceMethods.length; i++){
					var serviceMethod = serviceMethods[i];
					var command = serviceMethod.caller();
					
					var menuItem = { 
			   			text: mw3.localize(serviceMethod.methodContext.displayName) + (serviceMethod.methodContext.keyBinding ? '(' + serviceMethod.methodContext.keyBinding[0] + ')' : ''),
						onclick: { fn: 
							function(){
								eval(this._oOnclickAttributeValue.command);
							},
							command: command
						} 
				   	};
				   	
				   	menuItems[menuItems.length] = menuItem;
			   }
			   
			   return menuItems;
		   };
	   
			Metaworks3.prototype.showPopup = function(objId, serviceMethodContext, result){
				$('body').append("<div id='" + mw3.popupDivId + "' class='target_" + serviceMethodContext.target + "' style='position: absolute; z-index:10; top:" + mw3.mouseY + "px; left:" + mw3.mouseX + "px'></div>");
				
				mw3.locateObject(result, null, '#' + mw3.popupDivId);
			};

			Metaworks3.prototype.showStick = function(objId, serviceMethodContext, result){

				var x = mw3.mouseX;
				var y = mw3.mouseY;

				var objectDiv = $('#objDiv_' + mw3.recentCallObjectId);
				if(objectDiv){
					var methodDiv = objectDiv.find('#methodDiv_' + mw3.recentCallMethodName);

					if(methodDiv && methodDiv.offset()){
						x = methodDiv.offset().left;
						y = methodDiv.offset().top;

						mw3.mouseX = x + methodDiv.width() / 2;
						mw3.mouseY = y + methodDiv.height() / 2;
					}
				}

				$('body').append("<div id='" + mw3.popupDivId + "' class='target_" + serviceMethodContext.target + "' style='position: absolute; z-index:10; top:" + y + "px; left:" + x + "px'></div>");
				
				mw3.locateObject(result, null, '#' + mw3.popupDivId);
				
				// stick mode is auto close
				if(serviceMethodContext.target == 'stick')
					closeOutsideContainer(mw3.popupDivId);				

			};
			Metaworks3.prototype.showOverPopup = function(objId, serviceMethodContext, result){
				var zIndex = 10;
				if( serviceMethodContext.target=="popupOverPopup" ){
					var modalWindow = $('.ui-dialog');
					if(modalWindow.length > 0){
						zIndex = $(modalWindow[modalWindow.length-1]).css('z-index');
						zIndex = String(Number(zIndex)+1);
					}else{
						zIndex = 101;
					}		
				}
				
				$('body').append("<div id='" + mw3.popupDivId + "' class='target_" + serviceMethodContext.target + "' style='z-index:"+zIndex+";position:absolute; top:" + mw3.mouseY + "px; left:" + mw3.mouseX + "px'></div>");
				
				mw3.locateObject(result, null, '#' + mw3.popupDivId);				
				
			};

			Metaworks3.prototype.getValueForFieldDescriptor = function(object, type, realname){
				if(object && object.__className){
					var metadata = mw3.getMetadata(object.__className);
					
					var fieldName = metadata.getFieldNameForFieldDescriptorType(type);
					
					if(fieldName || realname){
						if(typeof object[fieldName] == 'object')
							return this.getValueForFieldDescriptor(object[fieldName], type);
						else
							return object[fieldName];
					}else{
						return metadata.displayName;
					}
				}
				
				return null;

			};
			
			Metaworks3.prototype.getObjectNameValue = function(object, realname){
				return this.getValueForFieldDescriptor(object, 'name', realname);
			};

			Metaworks3.prototype.getObjectKeyValue = function(object){
				return this.getValueForFieldDescriptor(object, 'key');
			};
			
			Metaworks3.prototype.putObjectIdKeyMapping = function(objectId, value, isMakeSuperClass){
				var objKeys = this._createObjectKey(value, isMakeSuperClass);
				
				if(objKeys && objKeys.length){
					for(var i=0; i<objKeys.length; i++)
						this.objectId_KeyMapping[objKeys[i]] = objectId;
				}
			};

			Metaworks3.prototype.putObjectIdKeyMappingAll = function(objectId, value, isMakeSuperClass){
				this.putObjectIdKeyMapping(objectId, value, isMakeSuperClass);
				
				for(var key in value){
					var childObjectId = this.getChildObjectId(objectId, key);
					if(childObjectId)
						this.putObjectIdKeyMappingAll(childObjectId, value[key], true);
				}
			};

			Metaworks3.prototype.objectIndexOf = function(object, isMakeSuperClass){
				var objKeys = mw3._createObjectKey(object, isMakeSuperClass);
				
				if(objKeys && objKeys.length){
					for(var i=0; i<objKeys.length; i++){			
						var mappedObjId = mw3.objectId_KeyMapping[objKeys[i]];
			
						return mappedObjId;
					}
				}
			};
			
			Metaworks3.prototype.copyObjectToObject = function(ori, dst, depth){
				--depth;
				
				if(depth < 0)
					return;
				
				for(var key in dst){
					if(Object.prototype.toString.call(ori[key]) === '[object Object]')
						this.copyObjectToObject(ori[key], dst[key], depth);
					else
						ori[key] = dst[key];
				}
			};


///// event handling framework ///////

			Metaworks3.prototype.addEventListener = function(event, func, data){
				if(!this.listeners)
					this.listeners = [];

				var id = this.listeners.length;
				this.listeners.push({event: event, func: func, data: data});

				return id;
			}

			Metaworks3.prototype.removeEventListener = function(eventId){
				this.listners.splice(eventId, 1)
			}

			Metaworks3.prototype.fireEvent = function(event, object){
				if(!this.listeners) return;

				for(var i=0; i<this.listeners.length; i++){
					var listener = this.listeners[i];
					if(listener.event == event){
						listener.func(object, listener.data);
					}
				}
			}

			
			////// reference objects //////
			
			var FieldRef = function(object, objectId, fieldDescriptor){
				this.object = object;
				this.objectId = objectId;
				this.fieldDescriptor = fieldDescriptor;
			};
			
			
			FieldRef.prototype.here = function(context){
				if(mw3.isHidden(this))					
					return "";
				
				var html;
				
				var face;
				
				var value = this.object[this.fieldDescriptor.name];
				
				var className = value ? value.__className : null;
				
				if(value!=null && className==null){
					if(value.split)
						className = "java.lang.String";
					else
					if(typeof value == 'number')
						className = "java.lang.Number";
				}
				
				if(!className)
					className = this.fieldDescriptor.className;
					
				if(className == null) throw new Error("FieldRef [" + fieldDescriptor.name + "] doesn't have className. Redering cancelled.");

					
				var oldContext = mw3.getContext();
				if(context!=null){
					//mw3.setContext(context);
					
				}
				
				var parentWhen = mw3.when;
				var when = null;				

				if(context!=null && context.when){
					when = context.when;
				}else{
					when = parentWhen;
				}
				
				if(when == mw3.WHEN_VIEW)
					face = className + (this.fieldDescriptor.viewFace ? ":" +this.fieldDescriptor.viewFace : "");
				else
					face = className + (this.fieldDescriptor.inputFace ? ":" +this.fieldDescriptor.inputFace : "");

				
				var designMode = (when == "__design");
				var designModeDepth2 = (when == "__design-depth2");
				
				if(this.fieldDescriptor.attributes){
					
					if(this.fieldDescriptor.attributes['resource']){
						if(designModeDepth2){
							when = mw3.WHEN_EDIT; //TODO: should not work for inner objects recursively.
						}else{
							when = mw3.WHEN_VIEW;
						}
						
						value = this.fieldDescriptor.attributes['resource'];
					}

					// when 에 따른 noneditable
					if(this.fieldDescriptor.attributes['noneditable']){
						if(this.fieldDescriptor.attributes['noneditable.when']){
							if(this.fieldDescriptor.attributes['noneditable.when'][mw3.when] != null)
								when = mw3.WHEN_VIEW;
						}else if(this.fieldDescriptor.attributes['noneditable.where']){
							if(this.fieldDescriptor.attributes['noneditable.where'][mw3.where] != null)
								when = mw3.WHEN_VIEW;
						}else if(this.fieldDescriptor.attributes['noneditable.how']){
							if(this.fieldDescriptor.attributes['noneditable.how'][mw3.how] != null)
								when = mw3.WHEN_VIEW;
						}else{
							when = mw3.WHEN_VIEW;
						}
						
						if(typeof context == 'undefined')
							context = {};
						
						context['when'] = when;
					}					
				}
				
				var options = {descriptor: this.fieldDescriptor};
				// set name for dom
				options['name'] = this.fieldDescriptor.name;
				
				if(context){
					for(key in context)
						options[key] = context[key];
					/*
					if(context['htmlTag']) options['htmlTag'] = context['htmlTag'];					
					if(context['htmlAttr']) options['htmlAttr'] = context['htmlAttr'];					
					if(context['htmlAttrChild']) options['htmlAttrChild'] = context['htmlAttrChild'];
					if(context['ejsPath']) options['ejsPath'] = context['ejsPath'];
					*/
				}
							
				var beanPropertyOption = {objectId: this.objectId, name: this.fieldDescriptor.name};
				
				if(!designMode){ //means general mode
					if(when && context && context.when) // && parentWhen && when != parentWhen)
						options['when'] = when;

					html = mw3.locateObject(value, face, null, options, beanPropertyOption);
				}else if(!designModeDepth2){ //means just design mode
					options['when'] = '__design-depth2';
					
					html = mw3.locateObject(value, face, null, options, beanPropertyOption);
				}else // means this fields is within the designee 
					html = this.fieldDescriptor.displayName + " Here.";
				
//				mw3.setContext(oldContext);
				
				
				//mw3.addBeanProperty(this.objectId, "." + this.fieldDescriptor.name);
				
				
				
				return html;
			};
			
			
			var MethodRef = function(object, objectId, methodContext){
				this.object = object;
				this.objectId = objectId;
				this.methodContext = methodContext;
			};

			MethodRef.prototype.caller = function(){				
				
				if(mw3.needToConfirmMessage) {
					return 'if(event && event.stopPropagation)	 event.stopPropagation(); else if(window.event) window.event.cancelBubble = true;'+(this.methodContext.needToConfirm ? 'if (confirm(\'' + mw3.localize(mw3.needToConfirmMessage, this.methodContext.displayName) + '\'))':'')  + 'mw3.getObject(' + this.objectId + ').' + this.methodContext.methodName + '()';
				} else {
					return 'if(event && event.stopPropagation)	 event.stopPropagation(); else if(window.event) window.event.cancelBubble = true;'+(this.methodContext.needToConfirm ? 'if (confirm(\'Are you sure to ' + this.methodContext.displayName + ' this?\'))':'')  + 'mw3.getObject(' + this.objectId + ').' + this.methodContext.methodName + '()';
				}
				
				//return 'window.event.stopPropagation();'+(this.methodContext.needToConfirm ? 'if (confirm(\'Are you sure to ' + this.methodContext.displayName + ' this?\'))':'')  + 'mw3.getObject(' + this.objectId + ').' + this.methodContext.methodName + '()';
			};
			
			MethodRef.prototype.here = function(){

				if(this.methodContext.methodName == 'contextMenus'){

					var template = "dwr/metaworks/genericfaces/ContextMenus.ejs";

					var contextValues = {
						mw3					: mw3,
						objectId			: this.objectId,
						object				: this.object,
						objectMetadata 		: mw3.getMetadata(this.object.__className)

					}

					var templateEngine = new EJS({url: mw3.base + '/' + template, context: contextValues});

					var html = templateEngine.render(contextValues);

					return html;
				}


				if(mw3.isHiddenMethod(this))					
					return "";
		   			
		   		var template;
		   		if(arguments.length == 1){
		   			template = arguments[0];
		   		}else{
		   			template = "dwr/metaworks/genericfaces/MethodFace.ejs";//"<input type=button value='<%=methodName%>' onclick=\"mw3.call(<%=objectId%>, '<%=methodName%>')\">";
		   		}
		   		
		   		var contextValues = {
					mw3					: mw3, 
					objectId			: this.objectId, 
					method				: this,
					methodName			: this.methodContext.methodName,
					displayName			: this.methodContext.displayName,
					methodContext		: this.methodContext,
					object				: this.object
				}
		   		
				var templateEngine = new EJS({url: mw3.base + (template.indexOf('dwr') == 0 ? '/':'/metaworks/') + template, context: contextValues});
				
				var html = templateEngine.render(contextValues);		   		
		   		
		   		/*
				var template = new EJS({url: mw3.base + (template.indexOf('dwr') == 0 ? '/':'/metaworks/') + template, context: contextValues});
				template.debug_mode = true;
				
				var html = template.render(contextValues);
				*/
		   		
				//html = "<div style='display:inline-block' id=method_" + this.objectId + "_" + this.methodContext.methodName + ">" + html + "</div>";
				
				return html;
			};



			MethodRef.prototype.call = function(){
				mw3.call(this.objectId, this.methodName);
			};

			MethodRef.prototype.isAvailable = function(){
				return !mw3.isHiddenMethod(this)
			};
			
			
			var MetaworksObject = function(object, divName){
				this.object = object;
				this.divName = divName;
				
				return mw3.locateObject(object, null, divName).getObject();
			};


			function getInternetVersion(ver) { 
				var rv = -1; // Return value assumes failure.      
				var ua = navigator.userAgent;  
				var re = null;
				if(ver == "MSIE"){
					re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
				}else{
					re = new RegExp(ver+"/([0-9]{1,}[\.0-9]{0,})");
				}
				if (re.exec(ua) != null){ 
					rv = parseFloat(RegExp.$1);
				} 
				return rv;  
			}
			
			function closeOutsideContainer(divId){
				$('#' + divId).one('destroy', {divId: divId}, function(event){
					$('body').unbind('mousedown.cos_' + event.data.divId);
					var objectId = $(this).children(':first').attr('objectId');
					mw3.removeObject(objectId);
				});

				$('body').bind('mousedown.cos_' + divId, {divId: divId}, function(event){
					var container = $('#' + event.data.divId);

					var result = isMouseInContanier(container, event);
					if(!result)
						$(container).trigger('destroy');
				});
			};
			
			function isMouseInContanier(container, event){
				var containerOffset = container.offset();
				if( containerOffset ){
					containerOffset.right = parseInt(containerOffset.left) + container.width();
					containerOffset.bottom = parseInt(containerOffset.top) + container.height();
	
					if ('select-one' == event.target.type ||
						((containerOffset.left <= event.pageX && event.pageX <= containerOffset.right) && 
						(containerOffset.top <= event.pageY && event.pageY <= containerOffset.bottom))){
						return true;
					}else{
						return false;
					}
				}else{
					return false;
				}
			}
			
			Date.prototype.format = function(f) {
			    if (!this.valueOf()) return " ";
			 
			    var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
			    var d = this;
			     
			    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
			        switch ($1) {
			            case "yyyy": return d.getFullYear();
			            case "yy": return (d.getFullYear() % 1000).zf(2);
			            case "MM": return (d.getMonth() + 1).zf(2);
			            case "dd": return d.getDate().zf(2);
			            case "E": return weekName[d.getDay()];
			            case "HH": return d.getHours().zf(2);
			            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
			            case "mm": return d.getMinutes().zf(2);
			            case "ss": return d.getSeconds().zf(2);
			            case "a/p": return d.getHours() < 12 ? "오전" : "오후";
			            default: return $1;
			        }
			    });
			};
			 
			String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
			String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
			Number.prototype.zf = function(len){return this.toString().zf(len);
};

var MetaworksService = function(className, object, svcNameAndMethodName, autowiredObjects, objId, divId, placeholder, callback, sync, serviceMethodContext){
	this.faceHelperQueue = [];
	this.index = null;
	
	this.setIndex = function(index){
		this.index = index;
	};

	this.__replaceWithFace = function(object, forFace){
		if(Array.isArray(object)){
			for(var i in object){
				object[i] = this.__replaceWithFace(object[i], forFace);
			}
		}

		if(!object || !object.__className) return object;

		if(!forFace && mw3.isSuperClassOf("org.metaworks.Face", object.__className)){
			if(object._realValue){
				var face = object;
				object = object._realValue;
				face._realValue = null;
				object["_face_"] = face;
			}
		}

		var classDefinition = mw3.getMetadata(object.__className);

		if(classDefinition)
			for(var fieldIdx in classDefinition.fieldDescriptors){
				var field = classDefinition.fieldDescriptors[fieldIdx];

				if(!field) continue;

				var fieldValue = object[field.name];

				fieldValue = this.__replaceWithFace(fieldValue);

				object[field.name] = fieldValue;
			}

		return object;
	}

	this.__replaceByFace = function(object){

		if(Array.isArray(object)){
			for(var i in object){
				object[i] = this.__replaceByFace(object[i]);
			}
		}

		if(!object || !object.__className) return object;

		if(object._face_){

			//object = {
			//	__className: "org.metaworks.FaceWrapped",
			//	//value: object,
			//	face: object._face_
			//}

			object = object._face_;
		}

		for(var fieldIdx in object){
			var fieldValue = object[fieldIdx];

			fieldValue = this.__replaceByFace(fieldValue);

			object[field] = fieldValue;
		}

		return object;
	}


	this.__showResult = function(object, result, objId, svcNameAndMethodName, serviceMethodContext, placeholder, divId, callback ){
		var startTime = new Date().getTime();
//		mw3.log('__showResult : [' + objId + ']' + svcNameAndMethodName + ' ---> ' + new Date().getTime());		

		//object = JSON.parse(JSON.stringify(object));//clone the object
		//object = this.__replaceByFace(object);

		mw3.requestMetadataBatch(result);
		
		// 2012-03-19 cjw 기존 소스가 ejs.js 생성자 호출 보다 늦게 method 값을 할당하여 맨위로 올림
		mw3.recentCallMethodName = svcNameAndMethodName;
		mw3.recentCallObjectId = objId;
		
		if(mw3.isRecordingSession && !mw3.recordingExceptClasses[object.__className]){
			
			var objectKey = mw3._createObjectKey(object);
			var next = "autowiredObject." + objectKey + "." + svcNameAndMethodName;
			
			mw3.recording[mw3.recording.length] = {
				//next: next,
				value: object,
				objectKey: objectKey,
				methodName: svcNameAndMethodName//,
				//scenario: "testScenario"
			};
		}
		
		//alert("call.result=" + dwr.util.toDescriptiveString(result, 5))
//		mw3.debug("call result");

		if(mw3.debugMode){
			console.log("  - return value: ");
			console.log(result);
		}

		if(result){
			if(serviceMethodContext.target=="none"){
				// none mode is object return
			}else if(serviceMethodContext.target=="self"){
				if(!result.metaworksContext){

					//find parent metaworksContext
					var parentContext = mw3.getParentMetaworksContext(objId);

					result.metaworksContext = parentContext;

				}

				mw3.setObject(objId, result);
								
			}else if(serviceMethodContext.target=="popup" || serviceMethodContext.target=="stick" || serviceMethodContext.target=="popupOverPopup"){
				//store the recently added object Id for recent opener
				//mw3.recentOpenerObjectId[objId];
				result.__openerObjectId = objId;
				
				mw3.popupDivId = serviceMethodContext.target + '_' + objId;
				
				$('#' + mw3.popupDivId).remove();

				if(placeholder)
					mw3.removeObject(placeholder);
				
				if(serviceMethodContext.target == 'popup'){
					mw3.showPopup(objId, serviceMethodContext, result);
				}else if(serviceMethodContext.target == 'popupOverPopup'){
					mw3.showOverPopup(objId, serviceMethodContext, result);
				}else{
					mw3.showStick(objId, serviceMethodContext, result);
				}								
			}else if(serviceMethodContext.target=="opener"){


				var targetOpenerId = object.__openerObjectId;


				var parentDivs = $('#objDiv_' + objId).parents();

				parentDivs.each(function(index) {

					var classNameOfDiv = $(this).attr('classname');

					if (classNameOfDiv) {

						parentObjectId = $(this).attr('objectid');
						var parent = mw3.getObject(parentObjectId);

						if(parent && parent.__openerObjectId){
							targetOpenerId = parent.__openerObjectId;
						}
					}
				});

				if(targetOpenerId)
					mw3.setObject(targetOpenerId, result);
				else
					console.log("Target object id is not valid. TO_OPENER target is ignored.");

				
			}else{ //case of target is "auto"
				var results = result.length ? result: [result];
				
    			var mappedObjId;
				for(var j=0; j < results.length; j++){
					var result_ = results[j];
					
					if(result_ == null)
						continue;
					
        			var objKeys = mw3._createObjectKey(result_, true);
        			var neverShowed = true;
        			
        			if(serviceMethodContext.target == 'auto'){


						var closestSameWithResults = mw3.getClosestObject(mw3.recentCallObjectId, result_.__className, true);

						if(closestSameWithResults && closestSameWithResults.length){

							var closestSameWithResult = mw3.orderBestObject(closestSameWithResults, result_)[0];

							mw3.setObject(closestSameWithResult.__objectId, result_);
							neverShowed = false;
						}

	        			if(objKeys && objKeys.length){
	        				for(var i=0; i<objKeys.length && neverShowed; i++){
		        				mappedObjId = mw3.objectId_KeyMapping[objKeys[i]];
		        				
		        				var mappedObjdivId = "objDiv_" + mappedObjId;
		        				if(mappedObjId && document.getElementById(mappedObjdivId)){ //if there's mappedObjId exists, replace that div part.
		        					if(serviceMethodContext.target=="append"){
		        						mw3.locateObject(result_, null, "#"+mappedObjdivId);
		        					}else if(serviceMethodContext.target=="prepend"){
										mw3.locateObject(result_, null, "#"+mappedObjdivId, {prepend: true});
									}else{
		        						mw3.setObject(mappedObjId, result_);
		        					}
		        					
			        				neverShowed = false;
		        				}
	        				}
	        			}
        			}

        			if(neverShowed){
        				if(serviceMethodContext.target=="append"){
    						mw3.locateObject(result_, null, "#"+divId);
    					}else if(serviceMethodContext.target=="prepend"){
							mw3.locateObject(result_, null, "#"+divId, {prepend: true});
						}else{
    						mw3.setObject(objId, result_);
    					}
    					
    					neverShowed = false;
        			}
				}
				
				if(neverShowed){
					if(serviceMethodContext.target=="append"){
						mw3.locateObject(result, null, "#"+divId);
					}else if(serviceMethodContext.target=="prepend"){
						mw3.locateObject(result, null, "#"+divId, {prepend: true});
					}else{
						mw3.setObject(objId, result);
					}
				}
			}
		}

		//after call the request, the call-originator should be focused again.
		var sourceObjectIdNewlyGotten = mw3.objectId_KeyMapping[objectKey];
		if(sourceObjectIdNewlyGotten){
			// 2012-03-21 cjw 자동 focus 를 하지 않기 위해 수정
			//$("#objDiv_" + sourceObjectIdNewlyGotten).focus();
			//objId = sourceObjectIdNewlyGotten;
		}
		
		// 2012-04-16 faceHelper call change
		if(serviceMethodContext.target != "none"){
			if(serviceMethodContext.loadOnce){
				result['__cached'] = true;
				serviceMethodContext['cachedObjectId'] = mw3.targetObjectId;
			}
			    				
			mw3.onLoadFaceHelperScript();
			
			if(mw3.getFaceHelper(objId) && mw3.getFaceHelper(objId).endLoading){
				mw3.getFaceHelper(objId).endLoading(svcNameAndMethodName);
			}else{
				mw3.endLoading(objId, svcNameAndMethodName);
			}
				
			//TODO: why different method name?  showStatus and showInfo
			if(mw3.getFaceHelper(objId) && mw3.getFaceHelper(objId).showStatus){
				mw3.getFaceHelper(objId).showStatus( svcNameAndMethodName + " DONE.");
			}else{

				mw3.showInfo(objId, svcNameAndMethodName + " DONE");	
			}
		}
		
		if(callback && typeof callback == 'function')
			callback(objId, result);
		
		if(mw3.afterCall)
			mw3.afterCall(svcNameAndMethodName, result);
		
		//mw3.log('++showResult : [' + objId + ']' + svcNameAndMethodName + ' ---> ' + (new Date().getTime() - startTime));
		
		return result;
	};
	
	this.call = function(){
		var returnValue = null;
		var metaworksServiceIndex = this.index;
		var loaded = false;
		
		if(mw3.debugMode){
			console.log("metaworks call:");
			console.log("	className: "+ className);
			console.log("	object: ");
			console.log(object);
			console.log("	svcNameAndMethodName: "+ svcNameAndMethodName);
			console.log("	autowiredObjects: ");
			console.log(autowiredObjects);
			
		}

		if(!serviceMethodContext.clientSide){
			mw3.metaworksProxy.callMetaworksService(className, object, svcNameAndMethodName, autowiredObjects,
				{
					callback: function(result){

						var metaworksService = mw3.metaworksServices[metaworksServiceIndex];
	//					result = metaworksService.__replaceWithFace(result);

						returnValue = result;

						if(serviceMethodContext.target != "none"){


							metaworksService.__showResult(object, result, objId, svcNameAndMethodName, serviceMethodContext, placeholder, divId, callback);
							mw3.metaworksServices[metaworksServiceIndex] = null;
						}
					},

	        		async: !sync && serviceMethodContext.target!="none",
	        		
	        		errorHandler:function(errorString, exception) {
	        			if(serviceMethodContext.target=="none")
	        				throw exception;
	        			
        				if(placeholder){
        					mw3.removeObject(placeholder);
        				}
	        			
	        			if(mw3.objects[objId] && mw3.getFaceHelper(objId) && mw3.getFaceHelper(objId).showError){
		        			if(!exception)
		        				mw3.getFaceHelper(objId).showError( errorString, svcNameAndMethodName );
		        			else
		        				mw3.getFaceHelper(objId).showError( (exception.targetException ? exception.targetException.message : exception.message), svcNameAndMethodName );
						
						}else{
							if(!exception)
								mw3.showError(objId, errorString, svcNameAndMethodName);
							else
								mw3.showError(objId, (exception.targetException ? exception.targetException.message : exception.message), svcNameAndMethodName, exception);
						}
	        		}
			
	    		}
			);
		}else{
			var facehelper = mw3.getFaceHelper(objId);

			if(facehelper && facehelper[serviceMethodContext.methodName]){
				var clientSideFunc = facehelper[serviceMethodContext.methodName];
				clientSideFunc(object, autowiredObjects);
			}else{
				alert("you have defined your service method as clientSide=true but, there's no defined javascript function in your ejs.js (facehelper)");
			}
		}

		return returnValue;
	};

};

var MetaworksListener = {
	listenerList : [],
	
	addListener : function(){
		
	},
	
	init : function(){
		document.body.addEventListener("DOMNodeInserted", function (ev) {
			//console.log('DOMNodeInserted');
			//console.log(ev);
		});		
	}	
}

$.fn.extend({ 
	distanceTo : function(elem2) {
		var o1 = this.offset();
		var o2 = elem2.offset();
		
		var dx = o1.left - o2.left;
		var dy = o1.top - o2.top;
		var distance = Math.sqrt(dx * dx + dy * dy);
	
		return distance;
	}
});


var extend = function (target, parentClassName) {

	if(parentClassName.indexOf('.') > 0){
		parentClassName = parentClassName.replace(/\./g,'_');
	}

	try{
		eval(parentClassName);

	}catch(e){
		var ejsPath = parentClassName.replace(/\_/g,'/') + '.ejs';
		mw3._importFaceHelper(ejsPath);
	}

	var parent = eval(parentClassName);

	target.prototype = Object.create(parent.prototype);
};
