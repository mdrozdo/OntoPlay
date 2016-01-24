(function() {
    'use strict';

    angular.module('Ontoplay').controller('Main', ['$scope','Services', function($scope,Services) {

        $scope.remove = function(parentScope,index) {
 
        	//if it's sub node	
        	if(parentScope.hasOwnProperty('id')){
        		parentScope.nodes.splice(index,1);
        		return;
        	}
        	//if it is main node
        	 $scope.data.splice(index,1);
        	
        	
        };

        $scope.newSubItem = function(scope,className) {
            var newNodeId = getId(scope.nodes);
            
            if(className=='' && newNodeId>1){
            	className=scope.nodes[0].className;
            }
            if(className==''){
            	alert("can't add element");
            	return;
            }
            scope.nodes.push(createIndividual(newNodeId,className));
        };
        
        var getId=function(data){
        	if(data.length==0)
        		return 1;
        	return (data[data.length-1].id)+1;
        }
	
        $scope.newItem = function(className) {
            var newNodeId = getId($scope.data);
            $scope.data.push(createIndividual(newNodeId,className));
        };
        
        var createIndividual=function(newNodeId,className){
        	return {
                'id': newNodeId,
                'className':className,
                'property':'off',
                'operator':'off',
                'inputType':'',
				'dataValue':'',
				'propertyClass':'off',
				'objectValue':'off',
                'nodes': []
            }
        }
		
		$scope.update=function(){
			//validation here (TBD)
			var individual={'properties':$scope.data};
			Services.update(angular.toJson(individual),$scope.individualName);
		}
        
        $scope.$on('addChild', function (event, scope,className) {
        	scope.nodes=[];
        	$scope.newSubItem(scope,className);
        	});
        
        $scope.$on('deleteChild', function (event, scope) {	
        	scope.nodes=[];
        	});

        

			
		     $scope.$watch('mainClass',function(oldValue,newValue){
				 $scope.data = [];
        	if(newValue!=null)
				$scope.data.push(createIndividual(1,newValue));
        });
        
    }]);

}());