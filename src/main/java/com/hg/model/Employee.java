package com.hg.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "sys_employee")
public class Employee {
	
	private Long id;
	
	private String jobNumber;//工号
	
	private String userName;
	
	private String startTime;//上班时间
	
	private String endTime;//下班时间
	
	private String nationality;//民族 
	
	private String maritalStatus;//婚姻状况
	
	private String gender;//性别
	
	private String education;//教育背景
	
	private String identificationNumber;//身份证号
	
	private String birthday;//生日
	
	private String age;//年纪
	
	private String depId;//部门
	
	private Long groupId;//班组
	
	private String station;//岗位
	
	private String structure;//所属结构  生产 销售  管理 
	
	private String residence;//户籍
	
	private String currentAddress;//现居住地
	
	private String contactNumber;//联系电话
	
	private String inDate;//入职日期
	
	private String ageOfWork;//工龄
	
	private String probationPeriod;//试用期止
	
	private String departureDate;//离职日期
	
	private String socialSecurityNumber;//社保编号
	
	private String providentFundAccount;//公积金账号
	
	private String remarks;//备注
	
	private String poStartDate;//合同起
	
	private String poEndDate;//合同止
	
	private String supplementalAgreement;//补充协议
	
	private String  identityCardStart;//身份证起
	
	private String identityCardEnd;//身份证止
	
	private String major;//专业
	
	private String graduateFrom;//毕业院校
	
	private String studyStart;//学习时间起
	
	private String studyEnd;//学习时间末
	
	private String schoolModel;//学习模式
	
	private String certificateNumber;//证书编号
	
	private String studyForm;//学习形式
	
	private String credential;//职称证书
	
	private String travellingCrane;//行车
	
	private String forklift;//叉车
	
	private String electricSoldering;//电焊
	
	private String safetyOfficer;//安全员
	
	private String electricianCertificate;//电工证
	
	private String workLocation;//工作地点
	
	private String month;//生日
	
	private String retireDate;//退休日期
	
	private String line;//条线
	
	private Double salary;//岗位工资
	
	
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}

	public String getResidence() {
		return residence;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	public String getCurrentAddress() {
		return currentAddress;
	}

	public void setCurrentAddress(String currentAddress) {
		this.currentAddress = currentAddress;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getAgeOfWork() {
		return ageOfWork;
	}

	public void setAgeOfWork(String ageOfWork) {
		this.ageOfWork = ageOfWork;
	}

	public String getProbationPeriod() {
		return probationPeriod;
	}

	public void setProbationPeriod(String probationPeriod) {
		this.probationPeriod = probationPeriod;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}


	public String getProvidentFundAccount() {
		return providentFundAccount;
	}

	public void setProvidentFundAccount(String providentFundAccount) {
		this.providentFundAccount = providentFundAccount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPoStartDate() {
		return poStartDate;
	}

	public void setPoStartDate(String poStartDate) {
		this.poStartDate = poStartDate;
	}

	public String getPoEndDate() {
		return poEndDate;
	}

	public void setPoEndDate(String poEndDate) {
		this.poEndDate = poEndDate;
	}

	public String getSupplementalAgreement() {
		return supplementalAgreement;
	}

	public void setSupplementalAgreement(String supplementalAgreement) {
		this.supplementalAgreement = supplementalAgreement;
	}

	public String getIdentityCardStart() {
		return identityCardStart;
	}

	public void setIdentityCardStart(String identityCardStart) {
		this.identityCardStart = identityCardStart;
	}

	public String getIdentityCardEnd() {
		return identityCardEnd;
	}

	public void setIdentityCardEnd(String identityCardEnd) {
		this.identityCardEnd = identityCardEnd;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getGraduateFrom() {
		return graduateFrom;
	}

	public void setGraduateFrom(String graduateFrom) {
		this.graduateFrom = graduateFrom;
	}

	public String getStudyStart() {
		return studyStart;
	}

	public void setStudyStart(String studyStart) {
		this.studyStart = studyStart;
	}

	public String getStudyEnd() {
		return studyEnd;
	}

	public void setStudyEnd(String studyEnd) {
		this.studyEnd = studyEnd;
	}

	public String getSchoolModel() {
		return schoolModel;
	}

	public void setSchoolModel(String schoolModel) {
		this.schoolModel = schoolModel;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public String getStudyForm() {
		return this.studyForm;
	}

	public void setStudyForm(String studyForm) {
		this.studyForm = studyForm;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public String getTravellingCrane() {
		return travellingCrane;
	}

	public void setTravellingCrane(String travellingCrane) {
		this.travellingCrane = travellingCrane;
	}

	public String getForklift() {
		return forklift;
	}

	public void setForklift(String forklift) {
		this.forklift = forklift;
	}

	public String getElectricSoldering() {
		return electricSoldering;
	}

	public void setElectricSoldering(String electricSoldering) {
		this.electricSoldering = electricSoldering;
	}

	public String getSafetyOfficer() {
		return safetyOfficer;
	}

	public void setSafetyOfficer(String safetyOfficer) {
		this.safetyOfficer = safetyOfficer;
	}

	public String getElectricianCertificate() {
		return electricianCertificate;
	}

	public void setElectricianCertificate(String electricianCertificate) {
		this.electricianCertificate = electricianCertificate;
	}

	public String getWorkLocation() {
		return workLocation;
	}

	public void setWorkLocation(String workLocation) {
		this.workLocation = workLocation;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getRetireDate() {
		return retireDate;
	}

	public void setRetireDate(String retireDate) {
		this.retireDate = retireDate;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	
	
}
