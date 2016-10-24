(function() {
  var myRetail = function($http) {
  
//	GET product details for a product based on Product ID. 
//	The response is a combination of fields from Target API and the price information from NOSQL Data Store
    var getProductDetails = function(productId) {
    	  var myUrl = "http://localhost:8080/api/v1/products/"
	      myUrl += productId;
	      console.log(myUrl);
	      return $http.get(myUrl)
	                  .then(function(response) {
	                    return response.data;
	                  });
    };
    
//	GET information for ALL the products setup in the NOSQL Data Store.
//	The response is a combination of fields from Target API and the price information from NOSQL Data Store
	var getProductList = function() {
	    var myUrl = "http://localhost:8080/api/v1/products"
	    console.log(myUrl);
	    return $http.get(myUrl)
	                  .then(function(response) {
	                    return response.data;
	                  });
    };
    
    
//	POST i.e. add pricing information for a Product ID in the NOSQL Data Store.
//	Called from AddProductPriceController.
    var addProduct = function(productId, currencyCode, priceValue) {
    	var myUrl = "http://localhost:8080/api/v1/products/"
    	myUrl += productId;
	    myUrl += "/";
	    myUrl += currencyCode;
	    myUrl += "/";
	    myUrl += priceValue;
    	console.log(myUrl);
	    return $http.post(myUrl)
	                  .then(function(response) {
	                    return response.data;
	                  });
    };
    
    
//	PUT i.e. Update pricing information for the Product ID in the NOSQL Data Store.
//	Called from ProductListController.
    var updatePrice = function(productId, newValue) {
	    var myUrl = "http://localhost:8080/api/v1/products/"
    	myUrl += productId;
	    myUrl += "/";
	    myUrl += newValue;
    	console.log(myUrl);
	    return $http.put(myUrl)
	                  .then(function(response) {
	                    return response.data;
	                  });
    };
    
//	DELETE the pricing information for the Product ID in the NOSQL Data Store.
//	Called from ProductListController.
    var deleteProduct = function(productId) {
	    var myUrl = "http://localhost:8080/api/v1/products/"
    	myUrl += productId;
    	console.log(myUrl);
	    return $http.delete(myUrl)
	                  .then(function(response) {
	                    return response.data;
	                  });
    };
    
    
    return {
      getProductDetails: getProductDetails,
      getProductList: getProductList,
      addProduct: addProduct,
      updatePrice: updatePrice,
      deleteProduct: deleteProduct
    };  
  };
  
  var module = angular.module("productViewer");
//Registering the service  
  module.factory("myRetail", myRetail);
  
}());