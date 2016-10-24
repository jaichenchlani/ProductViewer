(function() {
        
//  Setup a Product and Pricing information in the NOSQL Data Store
	
	var app = angular.module("productViewer");
    
    function AddProductPriceController($scope, myRetail, $location, $log, $route) {
           	
//      Error Handling
        var onError = function(reason) {
        	$scope.error = "Could not fetch the Data.";
        };
       
        
//      Route to the updated product list, with the newly product showing up.
        var onAddComplete = function(data) {
        	myRetail.getProductList();
        	$location.path("/products");
        }
    	
//      Call the function defined in myRetail service to ADD the Product Price in the NOSQL Data Store
        $scope.AddProduct = function(productId, currencyCode, priceValue) {
        	console.log("Price:");
        	console.log(priceValue);
        	myRetail.addProduct(productId, currencyCode, priceValue).then(onAddComplete, onError);
        }
        
        console.log("from AddProductPriceController..");
        $scope.currencyCode = "USD";
    }
    
    console.log("AddProductPriceController.js..");
//  Register the controller
    app.controller("AddProductPriceController", AddProductPriceController);

})();