package com.hg.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "attendance")
public class Attendance {
	
	private Long id;
	
	private Long emplId;
	
	/**
	 * 手动输入部分
	 */
	private Double attendanceDay;//出勤天数
	
	private Double weight;
	
	private Double lengthenHour;//延长小时
	
	private Double otherHour;
	
	private Double tool;//刀量具
	
	private Double qualityPrize;//质量奖金
	
	/**
	 * 系统计算部分
	 */
	
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
	
	private Double dueDay;//应出勤天数
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmplId() {
		return emplId;
	}

	public void setEmplId(Long emplId) {
		this.emplId = emplId;
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

	public Double getDueDay() {
		return dueDay;
	}

	public void setDueDay(Double dueDay) {
		this.dueDay = dueDay;
	}

}
