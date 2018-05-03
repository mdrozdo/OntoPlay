
(function(){
  var Services=function($http){
    
    var getIndividuals=function(className){
    	// var classNameParts=className.split("#");
    	// className=classNameParts[classNameParts.length-1];
		return $http.get('/api/individuals/class/' + encodeURIComponent(className)).then(function(response){
                 var data=response.data;
			
			data.unshift({"uri":"off","localName":"Select an individual"});
			return response.data;
          
        });
    }
	
	var getProperties=function(className){
		 return $http.get('/api/properties/class/' + className).then(function(response){
			 var data=response.data;
			 data.unshift({'localName':'Select a property','uri':'off'});
            return data;
          
        });
	}
	
	var getOperators=function(propertyUri,isDescriptionOfIndividual){
		return $http.get('/api/properties/operators/' + propertyUri+'/'+isDescriptionOfIndividual).then(function(response){						
			var data=response.data;
			data.operators.unshift({displayValue: "Select an operator", realValue: "off"});
			return data;
          
        });
	}
	
	var getIndividualDataForUpdate=function(individualName){
		return $http.get('/api/individuals/data/' + individualName).then(function(response){
       
			return response.data;
          
        });
	}
	
	var getClasses=function(propertyName){
		return $http.get('/api/class/property/' + propertyName).then(function(response){
          var data= response.data;		  
		  data.unshift({"uri":"off","localName":"Select a class"});		
		  return data;
          
        });
	}

	var getAllClasses=function(){
		return $http.get('/api/class').then(function(response){
              var data= response.data;
    		  //data.unshift({"uri":"off","localName":"Select a class"});
    		  return data;

            });
    	}

	var add=function(data,elementName, isAddIndividual){
		var url='';
		if(isAddIndividual)
			url='/individuals/save';
		else
			url="/class/save";
		var dataToBeSend={'conditionJson':data,'name':elementName};
			 return $http.post(url, dataToBeSend).then(function(response){
            return response.data;
          
        });
	}
	
	var getAnnotationProperties=function(componentUri){
		return $http.get('/api/annotationProperties/get/'+encodeURIComponent(componentUri)).then(function(response){
			return response.data;
			
		});
		
	}
 
    
    return {
      getIndividuals:getIndividuals,
	  getProperties:getProperties,
	  getOperators:getOperators,
	  getClasses:getClasses,
	  add:add,
	  getAnnotationProperties:getAnnotationProperties,
	  getIndividualDataForUpdate:getIndividualDataForUpdate,
	  getAllClasses:getAllClasses
    };
  }
   var app = angular.module('Ontoplay');
   app.factory("Services",Services);
  
}());
