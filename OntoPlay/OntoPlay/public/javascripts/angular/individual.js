// Code goes here
(function() {
	var app = angular.module('app', []);

  var mainController = function($scope,Ontoplay) {
    $scope.username = "Angular";
    Ontoplay.getUser($scope.username).then(setUser, onError);
    
    var setUser = function(data) {
        alert('qwe');
    	$scope.user = data;
        
        
      };
      
      var onError = function(reason) {

          alert('asd');
        }
  };

  app.controller('main', ["$scope","Ontoplay", mainController]);
  



}());