(function() {
  
  var app = angular.module("productViewer", ["ngRoute"]);
  console.log("From App.js....");
  console.log(app);
  
  app.config(function($routeProvider) {
    $routeProvider
//    Main Search Form  
    .when("/main", {
        templateUrl: "main.html",
        controller: "MainController"
      })
//    Product Details Page
      .when("/product/:productId", {
        templateUrl: "product.html",
        controller: "ProductController"
      })
//    Product List, enabling the POST, PUT and DELETE features.
      .when("/products", {
        templateUrl: "productlist.html",
        controller: "ProductListController"
      })
//    Setup a new product and pricing information in NOSQL Data Store
      .when("/add", {
        templateUrl: "addproductprice.html",
        controller: "AddProductPriceController"
      })
      .otherwise({redirectTo:"/main"});
  });
  
}());