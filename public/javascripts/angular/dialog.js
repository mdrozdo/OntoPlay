(function(){
  var Dialog=function(){
  	var create=function(elementId){
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
  	}
	  var open=function(elementId){		    
		  $('#'+elementId).dialog('open');
	  }
	  
	  
	  
	  return{
	  	create:create,
		  open:open
	  };
  }
  var app = angular.module('Ontoplay');
  app.factory("Dialog",Dialog);
}());