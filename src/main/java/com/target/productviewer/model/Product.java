package com.target.productviewer.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class Product {
	
	public String productId;
	public String productName;
	public Price price;
	public Object rootNode;
	
	public Product() {
		
	}
	
}
