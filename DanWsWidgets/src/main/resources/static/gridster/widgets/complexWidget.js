angular.module('App').controller('complexWidgetController', ['$scope', function($scope){
	
}])

angular.module('App').directive("complexWidget", function() {
	return {
	    scope: {
	    	widget: "="
	    },
	    controller: "complexWidgetController",
		templateUrl: 'widgets/complexWidget.html'
	}
});