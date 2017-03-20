(function() {
    'use strict';

    angular.module('Ontoplay').controller('Child', ['$scope','Services','$rootScope', function($scope,Services,$rootScope) {
    	//move it to service
    	var closeDialog=function(){
    		if ($("#dialog").hasClass('ui-dialog-content')){
    			$('#dialog').dialog('close');
    			}
    	}
        
        $scope.changedProperty = function() {
			reset(true,true,true,true);
			closeDialog();
		
        	if($scope.data.property!='off'){
        		Services.getOperators(encodeURIComponent($scope.data.property),Services.isAddIndividual()).then(function(data){
					$scope.operators=data.operators;
					$scope.data.inputType=data.inputType;
					},onError);
        	}
        }
        
        
        
        $scope.changedOperator=function(){
        	
			reset(false,true,true,true);
			
			if($scope.data.operator!='off' && $scope.data.inputType=="object"){
				
				Services.getClasses(encodeURIComponent($scope.data.property)).then(function(data){
				
					$scope.propertyClasses=data;
				
				},onError);
			}				
        
			 // $scope.$emit("addChild", scope, $scope.data.selectedUser);
        }
		
		$scope.changedClass=function(){
			reset(false,false,false,true);
			if($scope.data.propertyClass=='off')
				return;
			//get the individuals
			if($scope.data.operator=="equalToIndividual"){
				Services.getIndividuals($scope.data.propertyClass).then(function(data){$scope.propertyIndividuals=data;
					
				
				},onError);
			}
			
			//add new node to sube nodes
			else {
				$scope.$emit("addChild", $scope.data, $scope.data.propertyClass);
			}
		}
        
        
        var onError=function(){        	
        	alert('error');
        }
        
        $scope.$watch('className',function(oldValue,newValue){
        	$scope.propertyIndividuals=[];
        	if(newValue!=null){
				Services.getProperties(newValue).then(function(data){$scope.properties=data;},onError);
				//Incase of update
				if($scope.data.operator!='off'){
					Services.getOperators(encodeURIComponent($scope.data.property),Services.isAddIndividual()).then(function(data){
					$scope.operators=data.operators;
					},onError);				
				}
				
				if($scope.data.propertyClass!='off'){
				Services.getClasses(encodeURIComponent($scope.data.property)).then(function(data){				
					$scope.propertyClasses=data;
				},onError);
				}
				
				if($scope.data.objectValue!="off"){			
				Services.getIndividuals($scope.data.propertyClass).then(function(data){$scope.propertyIndividuals=data;});
				}
			}
				
        });
		
			var reset=function(hideOperator,hideDataValue,hideClasses,hideSubNodes){
                if(hideOperator){
                        $scope.data.operator="off";
                        $scope.data.inputType='';
                        $scope.data.annotations=[];
                        $scope.data.objectAnnotations=[];
                    }
                if(hideDataValue)
                    $scope.data.dataValue="";
                if(hideClasses){
                    $scope.data.propertyClass="off";
                    $scope.propertyClasses=[];
                    $scope.data.objectAnnotations=[];
                }
                if(hideSubNodes){
                    $scope.data.objectValue="off";
                    $scope.propertyIndividuals=[];
                    $scope.data.nodes=[];
                    $scope.data.objectAnnotations=[];
                }

            }
			
			$scope.openAnnotationProperties=function(){
				if($scope.data.property=='off'){
					alert("Please, select a property first");
					return;
				}							
				$rootScope.$broadcast("showDialog", $scope.data.annotations,$scope.data.property);
			}
			
			$scope.openAnnotationForClass=function(objectClass){
				if(objectClass=='off'){
					return;
				}							
				objectClass=getObjectClassUri(objectClass);
				$rootScope.$broadcast("showDialog", $scope.data.objectAnnotations,objectClass);			
				}
			
			var getObjectClassUri=function (objectClass){
				
				for(var i=0;i<$scope.propertyClasses.length;i++)
					if($scope.propertyClasses[i].localName==objectClass)
						return $scope.propertyClasses[i].uri;
				return objectClass;
			}

    }]);

}());
