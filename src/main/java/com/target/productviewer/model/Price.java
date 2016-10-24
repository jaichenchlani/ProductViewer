package com.target.productviewer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Price{

	@JsonProperty("value")
	private double value;
	@JsonProperty("currency_code")
	private String currency_code;
	
	public Price() {
		
	}
	
	public Price(double value, String currency_code) {
		this.value = value;
		this.currency_code = currency_code;
	}
	
	public Price(double value) {
		this.value = value;
		this.currency_code = "USD";
	}

	@Override
	public String toString() {
		return "Price [value=" + value + ", currency_code=" + currency_code + "]";
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	
}
