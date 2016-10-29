package com.target.productviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.get.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.target.productviewer.model.Price;
import com.target.productviewer.model.Product;
import com.target.productviewer.repository.ElasticSearch;
import com.target.productviewer.repository.ProductRepository;



@RestController
@RequestMapping("api/v1")
public class ProductController {

	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	private static ObjectMapper mapper = new ObjectMapper();
	Product product = new Product();
	
//	Returning Product Details for a single product based on Product ID
	@JsonIgnoreProperties
	@RequestMapping(value = "products/{productId}", method = RequestMethod.GET)
	public Product getProductById(@PathVariable(value="productId") String productId) {
		
		product = ProductRepository.getProductDetailsFromTargetAPI(productId);
		product.productId = productId;
		
//		log.info("Product ID: " + product.productId);
//		log.info("Product Name: " + product.productName);
		
		if(!product.productName.isEmpty()) {
			product.price = ProductRepository.getPriceFromDataStore(productId);
		} else {
			product.price = new Price(0);
		}
		
		log.info("Product ID " + productId + " returned to the UI.");
		return product;
	}
	
//	Get all the product price details from the Data Store
	@RequestMapping(value = "products", method = RequestMethod.GET)
	public List<Product> getProducts() {
		List<Product> productList = new ArrayList<Product>();
		try {
			productList = ProductRepository.getAllProductsFromDataStore();
		} catch (IOException e) {
			log.info("IO Exception in ProductController.getProducts.");
			e.printStackTrace();
		}
		log.info("Returning " + productList.size() + " products to the UI.");
		return productList;
	}
	
	@RequestMapping(value = "products/{productId}", method = RequestMethod.POST)
	public void addProductPrice(@RequestBody Price price, @PathVariable(value="productId") String productId) {
		
//		updateProductPrice is the common function used for both POST and PUT
		try {
//			log.info("Calling ProductRepository.updateProductPrice...");
			ProductRepository.updateProductPrice(productId, price);
		} catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		
//		Heading back to the UI.
		log.info("Product ID " + productId + " ADDED in the NOSQL Data Store.");
	}
	

	@RequestMapping(value = "products/{productId}", method = RequestMethod.PUT)
	public void updateProductPrice(@RequestBody Price price, @PathVariable(value="productId") String productId) {
		
//		updateProductPrice is the common function used for both POST and PUT
		try {
//				log.info("Calling ProductRepository.updateProductPrice...");
				ProductRepository.updateProductPrice(productId, price);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

		log.info("Product ID " + productId + " UPDATED in the NOSQL Data Store.");
	}
	
	
	@RequestMapping(value = "products/{productId}", method = RequestMethod.DELETE)
	public void deleteProduct(@PathVariable(value="productId") String productId) {
		
//		log.info("Calling ProductRepository.deleteProductPrice...");
		ProductRepository.deleteProductPrice(productId);
		
//		Leverage the getProducts() function to return the product list.
		log.info("Product ID " + productId + " DELETED from the NOSQL Data Store.");
	}
	
}
