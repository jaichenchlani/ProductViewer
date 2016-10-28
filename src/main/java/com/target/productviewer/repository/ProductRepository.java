package com.target.productviewer.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.productviewer.model.Price;
import com.target.productviewer.model.Product;

public class ProductRepository {
	
	private static ObjectMapper mapper = new ObjectMapper();
	private static final Logger log = LoggerFactory.getLogger(ProductRepository.class);
	
//	Get the price from the NOSQL Data Store
	public static Price getPriceFromDataStore(String productId) {
//		log.info("Entering ProductRepository.getPriceFromDataStore...");
		Price dataStorePrice = new Price(0, "USD");
		try {
			GetResponse elasticResponse = ElasticSearch.getDocumentById(ElasticSearch.ES_INDEX, ElasticSearch.ES_TYPE, productId);
			dataStorePrice = mapper.readValue(elasticResponse.getSourceAsString(), Price.class);		
		} catch (JsonProcessingException e) {
			log.info("NOSQL JsonProcessingException...");
			e.printStackTrace();
		} catch (IOException e) {
			log.info("NOSQL IOException...");
			e.printStackTrace();
		} catch (NullPointerException e) {
			dataStorePrice.setValue(0);
			log.info("NOSQL NullPointerException...");
		}
		
		return dataStorePrice;
	}
	
//	Get the other Product Details from the Target API
	public static Product getProductDetailsFromTargetAPI(String productId) {
//		log.info("Entering ProductRepository.getProductDetailsFromTargetAPI...");
		Product product = new Product();
		String responseBody = null;
		RestTemplate restTemplate = new RestTemplate();
		String myUrl = "https://api.target.com/products/v3/";
		myUrl += productId;
		myUrl += "?fields=descriptions&id_type=TCIN&key=43cJWpLjH8Z8oR18KdrZDBKAgLLQKJjz";

		try {
			ResponseEntity<String> responseJsonString = restTemplate.getForEntity(myUrl, String.class);
//			Extracting the JSON Body from the Response
			responseBody = responseJsonString.getBody();
			
			HttpStatus statusCode = responseJsonString.getStatusCode();
//			log.info("HttpStatus statusCode: " + statusCode);
			
		} catch (NullPointerException e) {
			log.info("NullPointerException...");
			e.printStackTrace();
		}
		
		Map<String, Object> productMap = null;
		
		try {
				productMap = mapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {
            });
			JsonNode rootNode = mapper.readTree(responseBody);
			product.rootNode = rootNode;
		
			JsonNode itemNode = rootNode.findPath("items");
			
//		    Product Name
//			Item Node is an Array, hence need to loop through.
			for (final JsonNode objNode : itemNode) {
				JsonNode onlineDescriptionNode = objNode.findPath("online_description");
			    JsonNode onlineDescriptionValueNode = onlineDescriptionNode.findPath("value");
			    product.productName = onlineDescriptionValueNode.asText();
			}
			
		} catch (JsonParseException e) {
			log.info("TargetAPI JsonParseException...");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.info("TargetAPI JsonMappingException...");
			e.printStackTrace();
		} catch (IOException e) {
			log.info("TargetAPI IOException...");
			e.printStackTrace();
		};
		
		product.productId = productId;
		return product;
	}
	
	public static List<Product> getAllProductsFromDataStore() throws IOException {
//		log.info("Entering ProductRepository.getAllProductsFromDataStore...");
		QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
		List<Product> productList = new ArrayList<Product>();
//		For performance reasons, run the search in small batches of 500.
		int from =0; 
		int batchSize = 500;
		while(true) {
			SearchResponse response = ElasticSearch.search(ElasticSearch.ES_INDEX, ElasticSearch.ES_TYPE, queryBuilder, from, batchSize);
			List<Product> thisBatchProducts = buildProductList(response);
			
//			If the total search results are <= the batch size, break the while loop.
			if (thisBatchProducts.size() <= batchSize) {
				break;
			}
			from += batchSize;
		}
//		log.info("Exiting ProductRepository.getAllProductsFromDataStore. Returning " + productList.size() + " products.");
		return productList;
	}
	

//	Access modifier is marked as PRIVATE, as this method is not supposed to be called from outside of this class.
	private static List<Product> buildProductList(SearchResponse response) throws IOException {
//		log.info("Entering ProductRepository.buildProductList...");
		List<Product> productList = new ArrayList<Product>();
		String productId = new String();
		
		long n = response.getHits().getTotalHits();
//		log.info("Total Hits on the Data Store: " + n);
		for (int i=0; i<n; i++) {
			Price price = mapper.readValue(response.getHits().getAt(i).getSourceAsString(), Price.class);
			productId = response.getHits().getAt(i).getId();
//			log.info("Product # " + (i+1) + ": " + productId);
			Product product = ProductRepository.getProductDetailsFromTargetAPI(productId);
			product.price = price;
			productList.add(product);
		}
//		log.info("Exiting ProductRepository.buildProductList. Returning " + productList.size() + " products.");
		return productList;
	}
	
//	public boolean createProductPrice(Price price) throws JsonProcessingException {
//		return updateProduct(price);
//	}
	
	public static boolean updateProductPrice(String productId, Price price) throws JsonProcessingException {
//		log.info("Entering ProductRepository.updateProductPrice...");
		price.toString();
		try {
			IndexResponse response = ElasticSearch.setDocumentById(ElasticSearch.ES_INDEX, ElasticSearch.ES_TYPE, productId, mapper.writeValueAsString(price));
		} catch (JsonParseException e) {
			log.info("ProductRepository.updateProductPrice JsonParseException...");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.info("ProductRepository.updateProductPrice JsonMappingException...");
			e.printStackTrace();
		} catch (IOException e) {
			log.info("ProductRepository.updateProductPrice IOException...");
			e.printStackTrace();
		};
//		log.info("Exiting ProductRepository.updateProductPrice...");
		return true;
	}
	
	public static boolean deleteProductPrice(String productId) {
        DeleteResponse response = ElasticSearch.deleteDocumentById(ElasticSearch.ES_INDEX, ElasticSearch.ES_TYPE, productId);
        return response.isFound();
    }
	
	
}
