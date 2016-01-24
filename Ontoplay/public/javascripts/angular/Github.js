
(function(){
  var Github=function($http){
    
    var getUser=function(userName){
        return $http.get('https://api.github.com/users/' + userName).then(function(response){
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
   var app = angular.module('Ontoplay');
   app.factory("Github",Github);
  
}());