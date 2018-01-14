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
		
		$scope.add=function(){
			//validation here (TBD)
			//var individual={'classUri':$scope.mainClass,'propertyConditions':$scope.data,'annotations':$scope.mainObjectAnnotations};
			//console.log(individual);
			var individual=Adapter.createClassCondition($scope.mainClass,$scope.data,$scope.mainObjectAnnotations, $scope.classRelation);
			if(Adapter.isEmpty(individual.propertyConditions)){
				alert("No conditions were found");
				return;
			}
			//Services.update(angular.toJson(individual),$scope.elementName);
			Services.add(angular.toJson(individual),$scope.elementName, $scope.isAddIndividual).then(function(data){
				if(data=="ok"){
					alert($scope.type+" was added successfully. You will be redirected to the main "+$scope.mainClass+" page");
					//window.location.pathname="/view/"+$scope.mainClass;
				
				}else{
					alert("An error occured please try again later\n"+data)
				}

			});
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

         $scope.$watch('mainClass',function(newValue, oldValue){
            $scope.data = [];
            $scope.mainObjectAnnotations=[];
            //$scope.elementName='';    //TODO: Can I actually get rid of this? Causes issues if I'm changing main class when defining a mapping
            if(newValue!=null){
                if(window.location.href.indexOf('update')!=-1){

                    var tempPathParts=window.location.pathname.split('/');
                    $scope.elementName=tempPathParts[tempPathParts.length-1];
                    $scope.title="Update individual "+$scope.elementName;
                    $scope.type="To be updated individual";
                    $scope.buttonTxt="Update";
                    Services.getIndividualDataForUpdate($scope.elementName).then(function(data){
                        $scope.data = data.properties;
                        $scope.mainObjectAnnotations=data.annotations;
                    },function(){
                        alert("Error getting individual data.You will be redirected to the main "+$scope.mainClass+" page");
                    //  window.location.pathname="/view/"+$scope.mainClass;
                    });

                }else{
                    $scope.buttonTxt="Add";
                    $scope.className=newValue;
                    $scope.data.push(createIndividual(1,newValue));
                }
            }
        });

        var init = function(){
            return Services.getAllClasses().then(function(classes){
                $scope.allClasses = classes;
            })
        };

        return init();
    }]);
}());
