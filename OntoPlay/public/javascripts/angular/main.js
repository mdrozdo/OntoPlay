(function() {
    'use strict';

    angular.module('Ontoplay').controller('Main', ['$scope','Services','$rootScope','Adapter', function($scope,Services,$rootScope,Adapter) {

    	//move it to service
    	var closeDialog=function(){
    		if ($("#dialog").hasClass('ui-dialog-content')){
    			$('#dialog').dialog('close');
    			}
    	}
    	
        $scope.remove = function(parentScope,index) {
        	
        	closeDialog();
        	//if it's sub node	
        	if(parentScope.hasOwnProperty('id')){
        		parentScope.nodes.splice(index,1);
        		return;
        	}
        	//if it is main node
        	 $scope.data.splice(index,1);
        	
        	
        };

        $scope.newSubItem = function(scope,className) {
        	closeDialog();
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
        	closeDialog();
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
                'nodes': [],
        		'annotations':[],
        		'objectAnnotations':[]
            }
        }
		
		$scope.update=function(){
			//validation here (TBD)
			//var individual={'classUri':$scope.mainClass,'propertyConditions':$scope.data,'annotations':$scope.mainObjectAnnotations};
			//console.log(individual);
			var individual=Adapter.createClassCondition($scope.mainClass,$scope.data,$scope.mainObjectAnnotations);
			if(Adapter.isEmpty(individual.propertyConditions)){
				alert("No conditions were found");
				return;
			}
			//Services.update(angular.toJson(individual),$scope.individualName);
			Services.update(angular.toJson(individual),$scope.individualName);
		}
        
        $scope.$on('addChild', function (event, scope,className) {
        	scope.nodes=[];
        	$scope.newSubItem(scope,className);
        	});
        
        $scope.$on('deleteChild', function (event, scope) {	
        	scope.nodes=[];
        	});
        	
        	$scope.showAnnotationFormMainClass=function(className){
        	
        		if(typeof className === 'undefined' || className=='' ){
					return;
				}							
				$rootScope.$broadcast("showDialog", $scope.mainObjectAnnotations,className);
			}
        		
		     $scope.$watch('mainClass',function(oldValue,newValue){
				 $scope.data = [];
				 $scope.mainObjectAnnotations=[];
				 $scope.individualName='';
        	if(newValue!=null)
				$scope.data.push(createIndividual(1,newValue));
		     });
		     
		 
        
    }]);

}());