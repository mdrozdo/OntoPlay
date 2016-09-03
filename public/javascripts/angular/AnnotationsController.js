(function() {
    'use strict';
//For both classes and properties
    angular.module('Ontoplay').controller('AnnotationsController', ['$scope','Services','Dialog', function($scope,Services,Dialog) {
    	$scope.tempType='text';
        
        $scope.$on('showDialog', function (event, annotationsProperties,component) {	
        	$scope.tempAnnotation='off';
        	$scope.tempValue='';
        	$scope.property='';
        	resetDefulatAnnotations();
        	$scope.annotationsProperties=annotationsProperties;
        	Dialog.open('dialog');
			Services.getAnnotationProperties(encodeURIComponent(component)).then(function(data){
				
				$scope.annotationsOptions=data;
				if(component.indexOf('#')>-1 && component.split('#').length==2 )
				$scope.property=component.split('#')[1];
				if(component.indexOf('#')==-1  )
					$scope.property=component
			},onError);
			
        	});
        
        var onError=function(){        	
        	alert('error');
        }
        $scope.addAnnotation=function(){
        	var annotationObject=createAnnotationProperty($scope.tempAnnotation,$scope.tempValue);
        	$scope.annotationsProperties.push(annotationObject);
        	$scope.tempAnnotation='off'
        	$scope.tempValue='';
        	$scope.tempType='text';
        }
        
        $scope.addDefaultAnnotation=function(){
        	var annotationObject=createAnnotationProperty($scope.tempDefaultAnnotation,$scope.tempDefaultValue);
        	$scope.annotationsProperties.push(annotationObject);
        	resetDefulatAnnotations();
        }
        
        var createAnnotationProperty=function(tempAnnotation,tempValue){
        	//To check if annoatation exist or not
        	var annotationIndex=getAnnotationindex(tempAnnotation);
        	if(annotationIndex!=-1){
        		$scope.deleteAnnotation(annotationIndex);
        	}
        	var id=getId();
        	var annotationObject={'id':id,'localName':'','value':'','uri':''};
        	annotationObject.id=id;
        	annotationObject.uri=tempAnnotation;
        	
        	annotationObject.localName=getAnnotationPropertyName(tempAnnotation);
        	annotationObject.value=tempValue;
        	return annotationObject;
        }
        
        var getAnnotationPropertyName=function(tempAnnotation){
        	for(var counter=0;counter<$scope.annotationsOptions.length;counter++){
        		if($scope.annotationsOptions[counter].uri==tempAnnotation){

        			return $scope.annotationsOptions[counter].localName;
        		}
        	}
        }
        
        var getAnnotationindex=function(tempAnnotation){
        	for(var counter=0;counter<$scope.annotationsProperties.length;counter++){
        		if($scope.annotationsProperties[counter].uri==tempAnnotation){
        			return counter
        		}
        	}
        	return -1;
        }
        
       
        
        
        
        var getId=function(){
        	var arrayLength=$scope.annotationsProperties.length;
        	if(arrayLength==0){
        		return 1;
        	}
        	return ($scope.annotationsProperties[arrayLength-1].id+1);
        }
        $scope.deleteAnnotation=function(index){
        	 $scope.annotationsProperties.splice(index,1);
        	
        }
        
        $scope.onChange=function(){
        	for(var counter=0;counter<$scope.annotationsOptions.length;counter++){
        		if($scope.annotationsOptions[counter].uri==$scope.tempAnnotation){
        			$scope.tempType=$scope.annotationsOptions[counter].inputType
        			break;
        		}
        	}
        }
        
        $scope.changeDefaultAnnotationButtonValue=function(){
        	if($scope.defaultAnnotationButtonValue=='less')
        		$scope.defaultAnnotationButtonValue='more';
        	else
        		$scope.defaultAnnotationButtonValue='less';        	
        }
        
        var resetDefulatAnnotations=function (){
        	$scope.tempDefaultAnnotation='off'
            $scope.tempDefaultValue='';
        	$scope.defaultAnnotationButtonValue='more';
        }
        resetDefulatAnnotations();
        
        
    }]);
    

}());