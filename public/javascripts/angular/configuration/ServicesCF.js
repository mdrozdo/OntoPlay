
(function(){
  var ServicesCF=function($http){
    
    var getAnnotations=function(){
        return $http.get('/annotation/configuration/getAnnotations').then(function(response){
            return response.data;
          
        });
    }
    
    var getComponents=function(){
        return $http.get('/annotation/configuration/getComponenets').then(function(response){
            return response.data;
          
        });
    }
    
    var getRelations=function(annotationIri){
    	return $http.get('/annotation/configuration/getRelations/'+annotationIri).then(function(response){
            return response.data;
          
        });
    	
    }
    
    var addRelation=function(annotationIri,annotationName,componentIri,componentName,componentType,relationType){
		var dataToBeSend={'annotationIri':annotationIri,
							'annotationName':annotationName,
				          'componentIri':componentIri,
				          'componentName':componentName,
						  'componentType':componentType,
						  'relationType':relationType};
		 return $http.post('/annotation/configuration/addRelation', dataToBeSend).then(function(response){
	            return response.data;
	        });
    }
    
    var deleteRelation=function(annotationId,componentId){
		var dataToBeSend={'annotationId':annotationId,
							'componentId':componentId
				          };
		 return $http.post('/annotation/configuration/deleteRelation', dataToBeSend).then(function(response){
	            return response.data;
	        });
    }
    
    var deleteAllRelations=function(annotationId,componentId){
		var dataToBeSend={'annotationId':annotationId,
							
				          };
		 return $http.post('/annotation/configuration/deleteAllRelations', dataToBeSend).then(function(response){
	            return response.data;
	        });
    }
    
    return {
    	getAnnotations:getAnnotations,
    	getComponents:getComponents,
    	getRelations:getRelations,
    	addRelation:addRelation,
    	deleteRelation:deleteRelation,
    	deleteAllRelations:deleteAllRelations
    };
  }
   var app = angular.module('OntoplayConfiguration');
   app.factory("ServicesCF",ServicesCF);
  
}());