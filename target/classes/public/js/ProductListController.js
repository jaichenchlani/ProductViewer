(function() {
     
//  Product List Page Controller. Displays the Product ID, Name and Price information in a tabular format.
//	Also provides the Add, Edit and Delete features for setting up Product Pricing information in the NOSQL Data Store.
	
    var app = angular.module("productViewer");

    function ProductListController($scope, myRetail, $location, $log, $route, $window) {
        
        var onProductListComplete = function(data){
//        	$log.info("From onProductListComplete...");
        	$log.info(data);
        	$scope.products = data;
        }
        
//      Error Handling
        var onError = function(reason) {
        	$log.info(reason);
        	$scope.error = "Data Store not available. Please try again later.";
        };
        
//      Refresh the product list post the Price UPDATE is complete on the back end
        var onUpdatePriceComplete = function(data) {
//        	$log.info("From onUpdatePriceComplete...");
        	$log.info(data);
        	$scope.products = data;
        	myRetail.getProductList().then(onProductListComplete, onError);
        	$route.reload();
        }
        
//      Refresh the product list post the Product Price DELETE is complete on the back end
        var onDeleteProductComplete = function(data) {
//        	$log.info("From onDeleteProductComplete...");
        	$log.info(data);
        	$scope.products = data;
        	myRetail.getProductList().then(onProductListComplete, onError);
        	$route.reload();
        }
        
//      Call the function defined in myRetail service to delete the Product Price setup in the NOSQL Data Store
        $scope.DeleteProduct = function(productId) {
        	myRetail.deleteProduct(productId).then(onDeleteProductComplete, onError);
        }
        
//      Call the function defined in myRetail service to update the Product Price setup in the NOSQL Data Store
        $scope.UpdatePrice = function(productId, newValue) {
        	myRetail.updatePrice(productId, newValue).then(onUpdatePriceComplete, onError);
        }
        
//      console.log("from ProductListController..");
        $scope.productSortOrder = "+productId";
        myRetail.getProductList().then(onProductListComplete, onError);
    }
    
//  console.log("ProductListController.js..");
//  Register the controller
    app.controller("ProductListController", ProductListController);

})();