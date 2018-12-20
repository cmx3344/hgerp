package com.hg.model;

import com.hg.constant.AttendanceDay;

public class AttendanceDto {
	
	private Long id;
	
	private Double dueDay;//应出勤天数
    
	private Double attendanceDay;//出勤天数
	
	private Double weight;
	
	private Double lengthenHour;//延长小时
	
	private Double otherHour;
	
	private Double tool;//刀量具
	
	private Double qualityPrize;//质量奖金
	
	private String userName;//员工姓名
	
	private Double salary;
	
	private String groupName;//班组名称
	
	private Double unitPrice;
	
	private Double seniorityPay;//工龄工资
	
	private Double attendancePay;//出勤工资
	
	private Double piecePay;//计件工资
	
	private Double restDay;//休息日
	
	private Double restPay;//加班工资
	
	private Double otherPay;//工时工资
	
	private Double safetyPrize;//安全奖金
	
	private Double attendanceBonus;//全勤奖
	
	private Double examineMoney;//考核金额
	
	private Double total;//合计
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAttendanceDay() {
		return attendanceDay;
	}

	public void setAttendanceDay(Double attendanceDay) {
		this.attendanceDay = attendanceDay;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getLengthenHour() {
		return lengthenHour;
	}

	public void setLengthenHour(Double lengthenHour) {
		this.lengthenHour = lengthenHour;
	}

	public Double getOtherHour() {
		return otherHour;
	}

	public void setOtherHour(Double otherHour) {
		this.otherHour = otherHour;
	}

	public Double getTool() {
		return tool;
	}

	public void setTool(Double tool) {
		this.tool = tool;
	}

	public Double getQualityPrize() {
		return qualityPrize;
	}

	public void setQualityPrize(Double qualityPrize) {
		this.qualityPrize = qualityPrize;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public Double getDueDay() {
		return dueDay;
	}

	public void setDueDay(Double dueDay) {
		this.dueDay = dueDay;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getSeniorityPay() {
		return seniorityPay;
	}

	public void setSeniorityPay(Double seniorityPay) {
		this.seniorityPay = seniorityPay;
	}

	public Double getAttendancePay() {
		return attendancePay;
	}

	public void setAttendancePay(Double attendancePay) {
		this.attendancePay = attendancePay;
	}

	public Double getPiecePay() {
		return piecePay;
	}

	public void setPiecePay(Double piecePay) {
		this.piecePay = piecePay;
	}

	public Double getRestDay() {
		return restDay;
	}

	public void setRestDay(Double restDay) {
		this.restDay = restDay;
	}

	public Double getRestPay() {
		return restPay;
	}

	public void setRestPay(Double restPay) {
		this.restPay = restPay;
	}

	public Double getOtherPay() {
		return otherPay;
	}

	public void setOtherPay(Double otherPay) {
		this.otherPay = otherPay;
	}

	public Double getSafetyPrize() {
		return safetyPrize;
	}

	public void setSafetyPrize(Double safetyPrize) {
		this.safetyPrize = safetyPrize;
	}

	public Double getAttendanceBonus() {
		return attendanceBonus;
	}

	public void setAttendanceBonus(Double attendanceBonus) {
		this.attendanceBonus = attendanceBonus;
	}

	public Double getExamineMoney() {
		return examineMoney;
	}

	public void setExamineMoney(Double examineMoney) {
		this.examineMoney = examineMoney;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public AttendanceDto(Long id, Double attendanceDay, Double weight,
			Double lengthenHour, Double otherHour, Double tool,
			Double qualityPrize, String userName, Double salary,
			String groupName, Double unitPrice, Double seniorityPay,
			Double attendancePay, Double piecePay, Double restDay,
			Double restPay, Double otherPay, Double safetyPrize,
			Double attendanceBonus, Double examineMoney, Double total,Double dueDay) {
		super();
		this.id = id;
		this.attendanceDay = attendanceDay;
		this.weight = weight;
		this.lengthenHour = lengthenHour;
		this.otherHour = otherHour;
		this.tool = tool;
		this.qualityPrize = qualityPrize;
		this.userName = userName;
		this.salary = salary;
		this.groupName = groupName;
		this.unitPrice = unitPrice;
		this.seniorityPay = seniorityPay;
		this.attendancePay = attendancePay;
		this.piecePay = piecePay;
		this.restDay = restDay;
		this.restPay = restPay;
		this.otherPay = otherPay;
		this.safetyPrize = safetyPrize;
		this.attendanceBonus = attendanceBonus;
		this.examineMoney = examineMoney;
		this.total = total;
		this.dueDay = dueDay;
	}
}
