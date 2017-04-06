
(function(){
  var Adapter=function(){
	   var getClassStructure=function(){
	    	return {'classUri':'','propertyConditions':'','annotations':''};
	    }
	    var createClassCondition=function(classUri,propertyConditions,annotations, classRelation){
	    	var temp=getClassStructure();
	    	temp.classUri=classUri;
	    	temp.annotations=annotations;
	    	temp.classRelation = classRelation;
	    	temp.propertyConditions=extractProperties(propertyConditions);
	    	return temp;
	    }
	    
	    var extractProperties=function(propertyConditions){
	    	var propertiesHolder=[];
	    	var currentProperty;
	    	for(var counter=0;counter<propertyConditions.length;counter++){
	    		currentProperty=propertyConditions[counter];
	    		
	    		//Don't add if there is not property
	    		if(isEmpty(currentProperty.property) )
	    			continue;
	    		
	    		//Don't add if the operator is off
	    		if(isEmpty(currentProperty.operator) )
	    			continue;
	    		
	    	
	    		if(!isEmpty(currentProperty.inputType) && currentProperty.inputType=="object"){

	    			//IndividualValueCondition
	    			if(currentProperty.operator=='equalToIndividual'){
	    				
	    				if(!isEmpty(currentProperty.objectValue))
	    					propertiesHolder.push(createIndividualValueCondition(currentProperty.property
			    					,currentProperty.objectValue,currentProperty.annotations));
	    				continue;
	    			}
	    			if(!isEmpty(currentProperty.propertyClass) && !isEmpty(currentProperty.nodes.length) ){
	    				propertiesHolder.push(createClassConstraintValue(
	    										currentProperty.property,
	    										currentProperty.propertyClass,
	    										currentProperty.nodes,
	    										currentProperty.objectAnnotations,
	    										currentProperty.annotations
	    										));	
	    			}
	    			continue;
	    		}
	    		//if we are here then, it's a data property
	    		//check if value is not empty
	    		if(!isEmpty(currentProperty.dataValue)){
	    			propertiesHolder.push(createDateProperty(currentProperty.property,currentProperty.operator
	    					,currentProperty.dataValue,currentProperty.annotations));
	    		}
	    		
	    	}
	    	return propertiesHolder;
	    }
	    
	    function createDateProperty(propertyUri,operator,datatypeValue,annotations){
	    	var tempDataProperty={"propertyUri":"","operator":"","datatypeValue":"",'annotations':[]};
	    	tempDataProperty.propertyUri=propertyUri;
	    	tempDataProperty.operator=operator;
	    	tempDataProperty.datatypeValue=datatypeValue;
	    	if(!isEmpty(annotations))
	    		tempDataProperty.annotations=annotations;
	    	return tempDataProperty;
	    }
	    
	    function createIndividualValueCondition(propertyUri,individualValue,annotations){
	    	var tempIndividualValueCondition={"propertyUri":"",
	    									  "operator":"equalToIndividual",
	    									  "individualValue":"",
	    									  'annotations':[]};
	    	tempIndividualValueCondition.propertyUri=propertyUri;
	    	tempIndividualValueCondition.individualValue=individualValue;
	     	if(!isEmpty(annotations))
	     		tempIndividualValueCondition.annotations=annotations;
	    	return tempIndividualValueCondition;
	    				
	    }
	    
	    var createClassConstraintValue=function(propertyUri,propertyClass,nodes,objectAnnotations,annotations){
	    	
	    	var tempClassConstraintValue={"propertyUri":"","operator":"describedWith","classConstraintValue":{},'annotations':[]};
	    	tempClassConstraintValue.propertyUri=propertyUri;
	    	tempClassConstraintValue.annotations=annotations;
	    	tempClassConstraintValue.classConstraintValue=createClassCondition(
					propertyClass,
					nodes,
					objectAnnotations
					);
			return tempClassConstraintValue
	    
	    }
	    
	     var isEmpty=function(variable){
	    	 if( typeof variable === 'undefined' || variable === null || variable.length==0 || variable=='off'){
	    		    return true
	    		}
	    	 return false;
	     }
	     return {
	    	 createClassCondition:createClassCondition,
	    	 isEmpty:isEmpty
	     };
  
  }
   var app = angular.module('Ontoplay');
   app.factory("Adapter",Adapter);
  
}());