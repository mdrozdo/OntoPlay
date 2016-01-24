(function(){
  var Ontoplay=function($http){
    
    var getUser=function(){
        return $http.get('/individuals/getPropertyCondition?conditionId=1&classUri=http%3A%2F%2Fwww.tan.com%23Product&propertyUri=http%3A%2F%2Fwww.tan.com%23hasColor').then(function(response){
            return response.data;
          
        });
    }
    
    var getRepos=function(user){
      return $http.get(user.repos_url).then(function(response){
        return response.data;
      });
    }
    
    var getRepo=function(username,repositoryName){
           return $http.get('https://api.github.com/repos/'+username+'/'+repositoryName).
           then(function(response){
        return response.data;
      });
    }
    
    var getRepoDetails=function(url){
           return $http.get(url).
           then(function(response){
        return response.data;
      });
    }
    
    return {
      getUser:getUser,
      getRepos:getRepos,
      getRepo:getRepo,
      getRepoDetails:getRepoDetails
    };
  }
   var app = angular.module('app');
   app.factory("Ontoplay",Ontoplay);
  
}());