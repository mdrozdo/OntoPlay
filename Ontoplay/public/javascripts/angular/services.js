
(function(){
  var Services=function($http){
    
    var getIndividuals=function(className){
        return $http.get('/individuals/class/' + className).then(function(response){
            return response.data;
          
        });
    }
	
	var getProperties=function(className){
		 return $http.get('/properties/class/' + className).then(function(response){
            return response.data;
          
        });
	}
	
	var getOperators=function(propertyUri){
		 return $http.get('/properties/operators/' + propertyUri).then(function(response){
            return response.data;
          
        });
	}
	
	var getClasses=function(className){
		 return $http.get('/class/property/' + className).then(function(response){
            return response.data;
          
        });
	}
	
	var updateIndividual=function(data,individualName){
	
		var dataToBeSend={'data':data,'name':individualName};
		console.log(dataToBeSend);
			 return $http.post('/checkAngular', dataToBeSend).then(function(response){
            return response.data;
          
        });
	}
    
 
    
    return {
      getIndividuals:getIndividuals,
	  getProperties:getProperties,
	  getOperators:getOperators,
	  getClasses:getClasses,
	  update:updateIndividual
    };
  }
   var app = angular.module('Ontoplay');
   app.factory("Services",Services);
  
}());