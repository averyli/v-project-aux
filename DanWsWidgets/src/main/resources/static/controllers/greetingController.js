angular.module('Application').controller("GreetingController",['$scope', function ($scope) {
	$scope.greeting = "Hi, this is angularJS";
	$scope.request = {
			client: "MyClient",
			count: 0
	}
	
	$scope.check = "A";
	$scope.response = {};
	$scope.ping="";
	var webSocket = new SockJS('/incoming');
	var stompClient = Stomp.over(webSocket);
	stompClient.connect({}, function(frame) {
		stompClient.subscribe('/outgoing/answer', function(response) {
			showResponse(response.body);
		})
	});

	var webSocketPing = new SockJS('/incoming');
	var stompClientPing = Stomp.over(webSocketPing);
	stompClientPing.connect({}, function(frame) {
		stompClientPing.subscribe('/outgoing/pong', function(response) {
			ping();
		})
	});
	
	function showResponse(data) {
		$scope.greeting = "greeting"
		$scope.response = data;
		$scope.check = "Den"
		$scope.$apply()
	}
	
	function ping() {
		$scope.ping+="got pong! ";
		$scope.$apply()
	}
	
	$scope.changeCheck = function(){
		$scope.check = "changed Den";
	}
	
	$scope.sendRequest = function() {
		stompClient.send("/app/request",{}, JSON.stringify($scope.request))
	}
	
}]);