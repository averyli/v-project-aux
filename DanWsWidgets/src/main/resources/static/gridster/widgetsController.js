var app = angular.module('App', [ 'gridster' ]);

app.controller("widgetsController",
		[ '$scope', 'gridsterConfig', 'myGridsterConfig', function($scope, gridsterConfig, myGridsterConfig) {

			// gridster configuration
			$scope.gridsterOpts = myGridsterConfig;
			
			// our widget abstraction
			function widget(name, directive) {
				this.x = 0;
				this.y = 0;
				this.size = {
					x : 2,
					y : 2
				};
				this.name = name;
				this.directive = directive;
			}
			
			// mapping our widget abstraction for gridster
			$scope.customItemMap = {
					sizeX : 'widget.size.x',
					sizeY : 'widget.size.y',
					row : 'widget.x',
					col : 'widget.y',
					minSizeX : '2',
					minSizeY : '2',
					maxSizeY : '10'
				};

			// widgets collection
			$scope.myWidgets = [];
			
			// collection on widget templates
			$scope.availableWidgetTemplates = [{
				name: "simple",
				directive: "simple-Widget"
			},{
				name: "complex",
				directive: "complex-Widget"
			},{
				name: "custom",
				directive: "custom-Widget"
			}]
			
			// adding a new widget
			$scope.addWidget = function(name, directive) {
				$scope.myWidgets.push(new widget(name, directive));
			}
			
			$scope.increaseWidgets = function() {
				
				angular.forEach($scope.myWidgets, function(widget) {
					if (widget.name =="denis") {
					widget.size.x=widget.size.x+1;
					widget.size.y=widget.size.x+1;
					}
				})
			}
			
		} ]);

app.directive('addDirective', function($parse, $compile) {

	  return {
	    compile: function compile(tElement, tAttrs) {

	      var directiveGetter = $parse(tAttrs.addDirective);

	      return function postLink(scope, element) {

	        element.removeAttr('add-directive');

	        var directive = directiveGetter(scope);
	        element.attr(directive, '');

	        $compile(element)(scope);
	      };
	    }
	  };
	});

app.factory('myGridsterConfig', function() {
	var gridsterOpts = {
					columns: 20, // the width of the grid, in columns
					pushing: true, // whether to push other items out of the way on move or resize
					floating: true, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
					swapping: false, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
					width: 'auto', // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
					colWidth: 'auto', // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
					rowHeight: 'match', // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
					margins: [10, 10], // the pixel distance between each widget
					outerMargin: true, // whether margins apply to outer edges of the grid
					sparse: false, // "true" can increase performance of dragging and resizing for big grid (e.g. 20x50)
					isMobile: false, // stacks the grid items if true
					mobileBreakPoint: 600, // if the screen is not wider that this, remove the grid layout and stack the items
					mobileModeEnabled: true, // whether or not to toggle mobile mode when screen width is less than mobileBreakPoint
					minColumns: 1, // the minimum columns the grid must have
					minRows: 1, // the minimum height of the grid, in rows
					maxRows: 100,
					defaultSizeX: 2, // the default width of a gridster item, if not specifed
					defaultSizeY: 1, // the default height of a gridster item, if not specified
					minSizeX: 3, // minimum column width of an item
					maxSizeX: null, // maximum column width of an item
					minSizeY: 1, // minumum row height of an item
					maxSizeY: null, // maximum row height of an item
					resizable: {
					   enabled: true,
					   handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw']
					  
					},
					draggable: {
					   enabled: true, // whether dragging items is supported
					   scrollSensitivity: 20,
					   scrollSpeed: 15
					}
				};
	return gridsterOpts;
})