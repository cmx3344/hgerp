package com.hg.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "process_temp")
public class ProcessTemp {
	
	private Long id;
	
	private String tempName;
	
	private String state;
	
	private String remarks;
	
	private List<ProcessTempSon> listSon;
	
	@Transient
	public List<ProcessTempSon> getListSon() {
		return listSon;
	}

	public void setListSon(List<ProcessTempSon> listSon) {
		this.listSon = listSon;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
