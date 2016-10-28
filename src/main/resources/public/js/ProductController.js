(function() {
   
//  Product Detail Page Controller. Displays the chosen fields in a tabular format.
	
    var app = angular.module("productViewer");

    function ProductController($scope, myRetail, $routeParams, $log) {
        
        var onProductComplete = function(data){

//        	$log.info("From onProductComplete...");
        	$log.info(data);

        	$scope.productId = data.productId;
        	$scope.productName = data.productName;
        	
        	if (data.price.value) {
//        		Product price is setup in the NOSQL Data Store. Go ahead and display. 
        		$scope.price = data.price.currency_code + " " + data.price.value;
        	} else {
//        		Product price is NOT setup in the NOSQL Data Store. Display and alternative text indicating.
        		$scope.price = "Not defined in Data Store.";
        	}
        	
//        	Bind scope variables with the displayed fields on the Product Detail page.
        	$scope.relationDescription = data.rootNode.product_composite_response.items[0].relation_description;
        	$scope.parentId = data.rootNode.product_composite_response.items[0].parent_id;
    		$scope.DPCI = data.rootNode.product_composite_response.items[0].dpci;
    		$scope.dataPageLink = data.rootNode.product_composite_response.items[0].data_page_link;
        	$scope.features = data.rootNode.product_composite_response.items[0].features;
        	$scope.alternateDescription = data.rootNode.product_composite_response.items[0].alternate_description;
        	
//        	Logic to pull the online short description from the Target API JSON.
        	if ($scope.alternateDescription) {
	        	for(var i=0; i< $scope.alternateDescription.length; i++) {        		
	        		if ($scope.alternateDescription[i].type == "DETL" ||
	        				$scope.alternateDescription[i].type == "AUX2") {
	        			$scope.onlineShortDescription = $scope.alternateDescription[i].value;
	        		}	
	        	}
        	}
        	
//        	Target API returns with an error message, if the Product ID is not valid. Display the error message when there is one.
        	if($scope.productName=="") {
        		$scope.productError = data.rootNode.product_composite_response.items[0].errors[0].message;
        	}
        }
        
//      Error Handling
        var onError = function(reason) {
        	$scope.error = "Could not fetch the Data.";
        };
        
//      console.log("from ProductController..");
        $scope.productId = $routeParams.productId;
        myRetail.getProductDetails($scope.productId).then(onProductComplete, onError);
    }
    
//  console.log("ProductController.js..");
//  Register the controller
    app.controller("ProductController", ProductController);

})();