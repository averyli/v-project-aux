angular.module('App').controller('simpleWidgetController', ['$scope', function($scope){
	$scope.greeting = "Hi, this is simpleWidgetController"
}])

angular.module('App').directive("simpleWidget", function() {
	return {
	    scope: {
	    	widget: "="
	    },
	    controller: "simpleWidgetController",
		templateUrl: 'widgets/simpleWidget.html'
	}
});