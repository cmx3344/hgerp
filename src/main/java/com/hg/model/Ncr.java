package com.hg.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "ncr")
public class Ncr {
	
	private Long id;
	
	private Long productId;
	
	private String reportPerson;//报告人
	
	private String reportDate;//报告日期
	
	private String ncrNum;//报告编号
	
	private String handler;//处理人
	
	private String handleTime;//处理时间
	
	private Integer state;//状态
	
	private String fromProcess;//发生工序/状态
	
	private String units;//单位
	
	private String unitType;//单位类型
	
	private String responsibleDep;//责任部门
	
	private String equipment;//设备
	
	private String ncrType;//不合格类型
	
	private String reason;//不合格原因
	
    private String handleType;//处理方式
	
	private String handleOpinion;//处理意见
	
	private String backDate;//回单日期
	
	private String remarks;//备注
	
    private String operator;//报告人
	
	private String opTime;//报告日期
	
	private String examestart;//发起审核
	
	private String exameend;//结束审核
	
	private String executeUnit;//执行单位
	
	private String serialNum;//锻件编号
	
	private String orderNum;//序号
	
	private List<NcrFiles> files;
	
	private List<NcrSon> listSon;
	
	private String filePath;//不合格报告路径
	
	@Transient
	public List<NcrSon> getListSon() {
		return listSon;
	}

	public void setListSon(List<NcrSon> listSon) {
		this.listSon = listSon;
	}

	@Transient
	public List<NcrFiles> getFiles() {
		return files;
	}

	public void setFiles(List<NcrFiles> files) {
		this.files = files;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOpTime() {
		return opTime;
	}

	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(String handleTime) {
		this.handleTime = handleTime;
	}

	public String getHandleType() {
		return handleType;
	}

	public void setHandleType(String handleType) {
		this.handleType = handleType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getNcrNum() {
		return ncrNum;
	}

	public void setNcrNum(String ncrNum) {
		this.ncrNum = ncrNum;
	}

	public String getFromProcess() {
		return fromProcess;
	}

	public void setFromProcess(String fromProcess) {
		this.fromProcess = fromProcess;
	}

	public String getHandleOpinion() {
		return handleOpinion;
	}

	public void setHandleOpinion(String handleOpinion) {
		this.handleOpinion = handleOpinion;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getResponsibleDep() {
		return responsibleDep;
	}

	public void setResponsibleDep(String responsibleDep) {
		this.responsibleDep = responsibleDep;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public String getNcrType() {
		return ncrType;
	}

	public void setNcrType(String ncrType) {
		this.ncrType = ncrType;
	}

	public String getBackDate() {
		return backDate;
	}

	public void setBackDate(String backDate) {
		this.backDate = backDate;
	}

	public String getReportPerson() {
		return reportPerson;
	}

	public void setReportPerson(String reportPerson) {
		this.reportPerson = reportPerson;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getExamestart() {
		return examestart;
	}

	public void setExamestart(String examestart) {
		this.examestart = examestart;
	}

	public String getExameend() {
		return exameend;
	}

	public void setExameend(String exameend) {
		this.exameend = exameend;
	}

	public String getExecuteUnit() {
		return executeUnit;
	}

	public void setExecuteUnit(String executeUnit) {
		this.executeUnit = executeUnit;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
}
