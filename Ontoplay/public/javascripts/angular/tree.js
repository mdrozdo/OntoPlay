(function() {
	'use strict';

	angular.module('demoApp').controller('BasicExampleCtrl',
			[ '$scope', function($scope) {
				$scope.remove = function(scope) {
					scope.remove();
				};
				
				$scope.blisterPackTemplates=[{id:1,name:"a"},{id:2,name:"b"},{id:3,name:"c"}];
				
				$scope.toggle = function(scope) {
					scope.toggle();
				};

				$scope.moveLastToTheBeginning = function() {
					var a = $scope.data.pop();
					$scope.data.splice(0, 0, a);
				};

				$scope.newSubItem = function(scope) {
					var newNodeId = scope.nodes.length + 1;
					scope.nodes.push({
					 id: newNodeId,
					 nodes: []
					 });
				};

				$scope.newItem = function() {
					var newNodeId = $scope.data.length + 1;
					alert(newNodeId);
					$scope.data.push({
						id : newNodeId,
						nodes : []
					});
				};

				 $scope.changedValue=function(item){
					    alert(item);
				 } 
					
				$scope.data = [ {
					'id' : 1,
					'title' : 'node1',
					'nodes' : [ {
						'id' : 11,
						'title' : 'node1.1',
						'nodes' : [ {
							'id' : 111,
							'title' : 'node1.1.1',
							'nodes' : []
						} ]
					}, {
						'id' : 12,
						'title' : 'node1.2',
						'nodes' : []
					} ]
				} ];
			} ]);

}());
