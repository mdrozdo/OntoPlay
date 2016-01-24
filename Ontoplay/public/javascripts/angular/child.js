(function() {
    'use strict';

    angular.module('Ontoplay').controller('Child', ['$scope','Github','Services', function($scope,Github,Services) {
		//input box type
    	/*Demo*/	
        var setUser = function(data) {
            //Github.getRepos(data).then(setRepos,onError);            
         };
         
         var setRepos = function(data) {
        	 $scope.properties =data;           
          }; 
        
        $scope.changedProperty = function() {
			reset(true,true,true,true);
		
        	if($scope.data.property!='off'){
        		Services.getOperators(encodeURIComponent($scope.data.property)).then(function(data){
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
        
        var setUsers=function(data){
        	
        	$scope.users=data;
        }
        
        var onError=function(){        	
        	alert('error');
        }
        
        $scope.$watch('className',function(oldValue,newValue){
        	if(newValue!=null)
				Services.getProperties(newValue).then(function(data){$scope.properties=data;},onError);
        });
		
			var reset=function(hideOperator,hideDataValue,hideClasses,hideSubNodes){
			if(hideOperator){
					$scope.data.operator="off";
					$scope.data.inputType='';
				}
			if(hideDataValue)
				$scope.data.dataValue="";
			if(hideClasses){
				$scope.data.propertyClass="off";
				$scope.propertyClasses=[];
			}
			if(hideSubNodes){
				$scope.data.objectValue="off";
				$scope.propertyIndividuals=[];
				$scope.data.nodes=[];
			}
			
		}

    }]);

}());