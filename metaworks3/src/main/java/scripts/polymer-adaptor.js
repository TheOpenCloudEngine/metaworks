mw3.Polymer = function(proto){
    var metadata = mw3.getMetadata(proto.java);
    if(!proto.properties) proto.properties = {};
    var typeMapping = {
        'java.lang.String': String,
        'java.lang.int': Number,
        'java.lang.Number': Number,
        'java.lang.long': Number,
        'java.lang.boolean': Boolean,
        'java.lang.Boolean': Boolean,
        'java.util.Date': Date,
    };
    for(var idx in metadata.fieldDescriptors){
        var field = metadata.fieldDescriptors[idx];
        var type = typeMapping[field.className];
        if(!type) type = Object;
        proto.properties[field.name] = {
            type: type
        }
    }
    proto.properties.__className = {
        type: String,
        value: proto.java
    }
    for(var idx in metadata.serviceMethodContexts){
        var method = metadata.serviceMethodContexts[idx];
        proto["_"+method.methodName] = function(){
            var serviceMethodContext = method;
            var objectForCall;
            var object = this;
            var objectMetadata = metadata;
            if(serviceMethodContext.callByContent == false){
                if(serviceMethodContext.payload){
                    var beanPaths = [];
                    for(var key in serviceMethodContext.payload){
                        if(key.indexOf("wireParamCls:") != 0)
                            beanPaths.push(key);
                    }
                    objectForCall = mw3.___copyBeanPathsOnly(this, beanPaths);
                }else
                    objectForCall = this._createKeyObject(object); //default option
            }else{
                objectForCall = {__className: object.__className, metaworksContext: object.metaworksContext};
                //we have to copy all the field values for objectForCall since there's too many additional JSON fields for call methods.
                for(var i in objectMetadata.fieldDescriptors){
                    var fd = objectMetadata.fieldDescriptors[i];
                    if(object!=null && object[fd.name]!=null && (!serviceMethodContext.except || !serviceMethodContext.except[fd.name]))
                        objectForCall[fd.name] = object[fd.name];
                }
            }
            mw3.metaworksProxy.callMetaworksService(this.__className, objectForCall, method.methodName, null,
                {
                    callback: function(result){
                        returnValue = result;
                        var target = object;
                        if(serviceMethodContext.target != "none"){
                            if(result.__className == object.__className){
                            }else{
                                var moduleId = mw3.polymerMapping[result.__className];
                                target = document.querySelector(moduleId);
                            }
                        }
                        if(target){
                            objectMetadata = mw3.getMetadata(target.__className);
                            for(var i in objectMetadata.fieldDescriptors){
                                var fd = objectMetadata.fieldDescriptors[i];
                                target[fd.name] = result[fd.name];
                            }
                        }
                    },
                    async: method.target!="none",
                    errorHandler:function(errorString, exception) {
                        if(method.target=="none")
                            throw exception;
                    }
                }
            );
        }
    }

    Polymer(proto);
    if(!mw3.polymerMapping)
        mw3.polymerMapping = {};
    mw3.polymerMapping[proto.java] = proto.is;
};
