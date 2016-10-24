(function() {
        
//  Home Page of the Application, with the search field for Product ID.
	var app = angular.module("productViewer");
    
    function MainController($scope, $location) {
        
        $scope.search = function(productId) {
        	if(productId) {
//        		Loads the Product Detail page for the specified Product ID
        		$location.path("/product/" + productId);
        	} else {
//        		If the Product ID is not provided, load the Product List page with all products setup in the NOSQL Data Store
        		$location.path("/products");
        	}
        }
        
        console.log("from MainController..");
    }
    
    console.log("MainController.js..");
//  Register the controller
    app.controller("MainController", MainController);

})();