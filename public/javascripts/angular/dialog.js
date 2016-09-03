(function(){
  var Dialog=function(){
	  var open=function(elementId){
		    $( '#'+elementId ).dialog({
		    	width:400,
		    	height:500,
		        autoOpen: false,
		        show: {
		          effect: "blind",
		          duration: 1000
		        },
		        hide: {
		          effect: "explode",
		          duration: 1000
		        }
		      });
		  $('#'+elementId).dialog('open');
	  }
	  
	  
	  
	  return{
		  open:open
	  };
  }
  var app = angular.module('Ontoplay');
  app.factory("Dialog",Dialog);
}());