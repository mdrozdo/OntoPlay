(function() {
    'use strict';

    angular
        .module('OntoplayConfiguration')
        .controller(
            'AnnotationsCFController', [
                '$scope',
                'ServicesCF',
                function($scope, ServicesCF) {
                    var OntologyComponents = [];

                    var getAnnotationProperties = function() {

                        ServicesCF
                            .getAnnotations()
                            .then(
                                function(data) {
                                    $scope.currentAnnotationProperty = 'off';
                                    $scope.annotationProperties = data;
                                }, onError);
                    }

                    var getAnnotationName = function() {
                        for (var counter = 0; counter < $scope.annotationProperties.length; counter++) {
                            if ($scope.annotationProperties[counter].uri == $scope.currentAnnotationProperty)
                                return $scope.annotationProperties[counter].localName;
                        }
                    }

                    var getOntologyComponents = function() {

                        $scope.currentOntologyComponentsType = 'off';
                        // selectd option in the UI
                        $scope.currentOntologyComponent = 'off';
                        // available options in the UI
                        $scope.currentOntologyComponents = [];

                        ServicesCF
                            .getComponents()
                            .then(
                                function(data) {
                                    $scope.OntologyComponentsTypes = [];
                                    for (var key in data) {
                                        $scope.OntologyComponentsTypes
                                            .push(key);
                                    }
                                    OntologyComponents = data;
                                }, onError);
                    }

                    var getComponentName = function() {
                        for (var counter = 0; counter < $scope.currentOntologyComponents.length; counter++) {
                            if ($scope.currentOntologyComponents[counter].uri == $scope.currentOntologyComponent)
                                return $scope.currentOntologyComponents[counter].localName;
                        }
                    }

                    var getRelations = function() {
                    	if($scope.currentAnnotationProperty=='off'){
                    		$scope.relationsData=[];
                    		return;
                    	}
                        ServicesCF.getRelations(
                                encodeURIComponent($scope.currentAnnotationProperty))
                            .then(function(data) {
                                $scope.relationsData = data;
                              


                            }, onError);
                    }
                    
                    var setRelationTypes=function(){
                    	$scope.relationTypes=['text','number','date'];
                    	$scope.currentRelationType='off';
                    }

                    var onError = function() {
                        alert('error');
                    }

                    var resetComponents = function(withCurrentOntologyComponentsType) {
                        if(withCurrentOntologyComponentsType)
                        	$scope.currentOntologyComponentsType = 'off';
                        $scope.currentOntologyComponent = 'off';
                        $scope.currentRelationType='off';
                        $scope.currentOntologyComponents = [];
                    }

                    $scope.changeAnnotation = function() {
                        resetComponents(true);
                        getRelations();
                    }

                    $scope.onComponentTypeChange = function() {

                        resetComponents(false);
                        if ($scope.currentOntologyComponentsType != 'off')
                            $scope.currentOntologyComponents = OntologyComponents[$scope.currentOntologyComponentsType];
                    }

                    $scope.addAnnotation = function() {
                        ServicesCF
                            .addRelation(
                                $scope.currentAnnotationProperty,
                                getAnnotationName(),
                                $scope.currentOntologyComponent,
                                getComponentName(),
                                $scope.currentOntologyComponentsType,
                                $scope.currentRelationType)
                            .then(function(data) {
                                if (data.success) {
                                    resetComponents(true);
                                    getRelations();
                                }
                            }, onError)
                    }

                    $scope.deleteRelation = function(annotationId, componentId) {
                    	ServicesCF.deleteRelation(annotationId,componentId)
                    	.then(function(data){
                    		if (data.success) {
                                getRelations();
                            }
                    	},onError);
                    }
                    
                    $scope.deleteAllRelations=function(annotationId){
                    	var isConfirmed=confirm("Are you sure you want to delete all relations?");
                    	if(isConfirmed)
                      	ServicesCF.deleteAllRelations(annotationId)
                    	.then(function(data){
                    		if (data.success) {
                                getRelations();
                            }
                    	},onError);
                    }
                    
                    getAnnotationProperties();
                    getOntologyComponents();
                    setRelationTypes();
                    
                    
                }
            ]);
}());