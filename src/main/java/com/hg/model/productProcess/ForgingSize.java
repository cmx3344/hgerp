package com.hg.model.productProcess;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table
public class ForgingSize {
	
	private Long id;
	
	private String forgingNum;
	
	private String forgingSize;
	
	private String sizes;
	
	private String optime;
	
	
	@Transient
	public String getSizes() {
		return sizes;
	}

	public void setSizes(String sizes) {
		this.sizes = sizes;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getForgingNum() {
		return forgingNum;
	}

	public void setForgingNum(String forgingNum) {
		this.forgingNum = forgingNum;
	}

	public String getForgingSize() {
		return forgingSize;
	}

	public void setForgingSize(String forgingSize) {
		this.forgingSize = forgingSize;
	}

	public String getOptime() {
		return optime;
	}

	public void setOptime(String optime) {
		this.optime = optime;
	}
	
}
