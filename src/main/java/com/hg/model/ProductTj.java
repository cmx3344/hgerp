package com.hg.model;

import java.util.List;

public class ProductTj {
	
	private String date;
	
	private Object qty;
	
	private Object weight;
	
	private Object material;
	
	private List<ProductTj> son;
	
	public List<ProductTj> getSon() {
		return son;
	}

	public void setSon(List<ProductTj> son) {
		this.son = son;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Object getQty() {
		return qty;
	}

	public void setQty(Object qty) {
		this.qty = qty;
	}

	public Object getWeight() {
		return weight;
	}

	public void setWeight(Object weight) {
		this.weight = weight;
	}

	public Object getMaterial() {
		return material;
	}

	public void setMaterial(Object material) {
		this.material = material;
	}

}
