Fetch the Product Details from the API 
https://api.target.com/products/v3/13860428?fields=descriptions&id_type=TCIN&key=43cJWpLjH8Z8oR18KdrZDBKAgLLQKJjz
and display a couple of attributes on the product page.


Test Data:
13054704 - Stand Alone
15117729 - Variation Child
13860428: Title Authority Child
51731130 - Variation Parent
50361936 - Collection Parent


Create ElasticDB Index/Type: myelasticdb/myretail, with the document definition below.
{
    "value": 199.99,
    "currency_code": "USD"
}


Open Issues:
Product Controller: When no error, displaying error in console when looking for Error key in JSON. No problem on the UI though.
Product.html: Displaying "&#174" instead of the trademark symbol in productName on Product page.
