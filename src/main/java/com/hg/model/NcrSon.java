package com.hg.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ncr_son")
public class NcrSon {
	
	private Long id;
	
	private Long parentId;
	
	private Long productId;
	
	private Integer handleType;//处理方式
	
    private String serialNum;//锻件编号
	
	private String orderNum;//序号
	
	private String sizeA;
	
	private String sizeB;
	
	private String sizeC;
	
	private String sizeD;
	
	private String sizeE;
	
	private String sizeF;
	
	private String sizeG;
	
	private String sizeH;
	
	private Integer qty;//数量
	
	private String blankWeight;//下料重
	
	private String material;//材质
	
	private String heatNum;//下料炉号
	
	private String totalWeight;//总重
	
	private Integer processId;
	
	private Integer state;
	
	public NcrSon() {
		super();
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getHandleType() {
		return handleType;
	}
	public void setHandleType(Integer handleType) {
		this.handleType = handleType;
	}
	@Transient
	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	@Transient
	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	@Transient
	public String getSizeA() {
		return sizeA;
	}

	public void setSizeA(String sizeA) {
		this.sizeA = sizeA;
	}

	@Transient
	public String getSizeB() {
		return sizeB;
	}

	public void setSizeB(String sizeB) {
		this.sizeB = sizeB;
	}

	@Transient
	public String getSizeC() {
		return sizeC;
	}

	public void setSizeC(String sizeC) {
		this.sizeC = sizeC;
	}

	@Transient
	public String getSizeD() {
		return sizeD;
	}

	public void setSizeD(String sizeD) {
		this.sizeD = sizeD;
	}

	@Transient
	public String getSizeE() {
		return sizeE;
	}

	public void setSizeE(String sizeE) {
		this.sizeE = sizeE;
	}

	@Transient
	public String getSizeF() {
		return sizeF;
	}

	public void setSizeF(String sizeF) {
		this.sizeF = sizeF;
	}

	@Transient
	public String getSizeG() {
		return sizeG;
	}

	public void setSizeG(String sizeG) {
		this.sizeG = sizeG;
	}

	@Transient
	public String getSizeH() {
		return sizeH;
	}

	public void setSizeH(String sizeH) {
		this.sizeH = sizeH;
	}

	@Transient
	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	@Transient
	public String getBlankWeight() {
		return blankWeight;
	}

	public void setBlankWeight(String blankWeight) {
		this.blankWeight = blankWeight;
	}

	@Transient
	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	@Transient
	public String getHeatNum() {
		return heatNum;
	}

	public void setHeatNum(String heatNum) {
		this.heatNum = heatNum;
	}
	
	@Transient
	public String getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(String totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	@Transient
	public Integer getProcessId() {
		return processId;
	}
	public void setProcessId(Integer processId) {
		this.processId = processId;
	}
	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public NcrSon(Long id, Long parentId, Long productId, String serialNum,
			String orderNum, String sizeA, String sizeB, String sizeC,
			String sizeD, String sizeE, String sizeF, String sizeG,
			String sizeH, Integer qty, String blankWeight, String material,
			String heatNum,Integer handleType,Integer state) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.productId = productId;
		this.serialNum = serialNum;
		this.orderNum = orderNum;
		this.sizeA = sizeA;
		this.sizeB = sizeB;
		this.sizeC = sizeC;
		this.sizeD = sizeD;
		this.sizeE = sizeE;
		this.sizeF = sizeF;
		this.sizeG = sizeG;
		this.sizeH = sizeH;
		this.qty = qty;
		this.blankWeight = blankWeight;
		this.material = material;
		this.heatNum = heatNum;
		this.handleType = handleType;
		this.state = state;
	}
}
