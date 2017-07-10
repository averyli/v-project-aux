angular.module('App').controller('cstomWidgetController', ['$scope', function($scope){
	
}])

angular.module('App').directive("customWidget", function() {
	return {
	    scope: {
	    	widget: "="
	    },
	    controller: "cstomWidgetController",
		templateUrl: 'widgets/custom/customWidget.html'
	}
});/**
 * 
 */