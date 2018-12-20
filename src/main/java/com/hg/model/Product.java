package com.hg.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;



@Entity
@Table(name = "product")
public class Product implements Serializable,Cloneable{

	private static final long serialVersionUID = 131826276090506554L;

	private Long id;
	
	private String productNum;//生产编号
	
	private String code;//代号
	
	private String poNum;//合同编号
	
	private String poDate;//合同交货期
	
	private String poUpdateDate;//更改交货期
	
	private String remarksA;//备注1
	
	private String remarksB;//备注2
	
	private String material;//材质
	
	private String grade;//等级
	
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
	
	private Integer poQty;//订单总数
	
	private String blankWeight;//下料重
	
	private Integer qty;//数量
	
	private String description;//说明
	
	private String qualityRequire;//质量要求
	
	private String ndtRequire;//探伤要求
	
	private String deliveryState;//交货状态
	
	private String htRequire;//热处理要求
	
	private String htRequireB;//
	
	private String hdnsRequire;//硬度要求
	
	private String nextProcess;//后续工序
	
	private String otherRequire;//其他要求
	
	private String forgingEquip;//锻造工艺设备
	
	private String materialNum;//物料号
	
	private String dwgNum;//图号
	
	private String startDate;//下达日期
	
	private String blankDateA;//下料出坯日期
	
	private String blankRemarkA;//下料出坯备注
	
	private String blankInspectDate;//下料检验日期
	
	private String forgingWorkerA;//锻造出坯加工方
	
	private String forgingInDateA;//锻造出坯投入日期
	
	private String forgingOutDateA;//锻造出坯转出日期
	
	private String heatTreatInDateA;//热处理出坯投入日期
	
	private String heatTreatOutDateA;//热处理出坯转出日期
	
	private String blankDate;//下料日期
	
	private String blankRemark;//下料备注
	
	private String heatNum;//下料炉号
	
	private String hlSize;//合料或出坯规格
	
	private String supplier;//供应商
	
	private String ignotNum;//锭节号
	
	private String forgingWorker;//锻造加工方
	
	private String forgingInDate;//锻造转入
	
	private String forgingOutDate;//锻造转出
	
	private String forgingRemark;//锻造备注
	
	private String afterForgingWorker;//锻后热处理加工方
	
	private String afterForgingInDate;//锻后热处理转入
	
	private String atterForgingOutDate;//锻后热处理转出
	
	private String afterForgingOut;//外协单位
	
	private String roughMachingWorker;//粗加工加工方
	
	private String roughMachingInDate;//粗加工转入
	
	private String roughMachingOutDate;//粗加工转出
	
	private String roughMachingWeight;//粗加工重量
	
	private String roughMachingRemark;//粗加工调度备注
	
	private String roughMachingOutRemark;//粗加工外协备注
	
	private String roughMachingOutCom;//粗加工外协单位
	
	private String ispsa;//是否喷砂(粗车)
	
	private String perHeatTreatWorker;//性能热处理加工方
	
	private String perHeatTreatInDate;//性能热处理转入
	
	private String perHeatTreatOutDate;//性能热处理转出
	
	private String sampleInDate;//取样转入
	
	private String sampleOutDate;//取样转出
	
	private String deliveryWorker;//交货加工加工方
	
	private String deliveryInDate;//交货加工转入
	
	private String deliveryOutDate;//交货加工转出
	
	private String deliveryOutRemark;//交货加工外协备注
	
	private String deliveryOutCom;//交货加工外协单位
	
	private String ispsb;//是否喷砂(交货加工)
	
	private String followupWorker;//后续热处理加工方
	
	private String followupInDate;//后续热处理转入
	
	private String followupOutDate;//后续热处理转出
	
	private String halfFinishMachingWorker;//半精加工加工方
	
	private String halfFinishMachingInDate;//半精加工转入
	
	private String halfFinishMachingOutDate;//半精加工转出
	
	private String halfFinishOutRemark;//半精加工外协备注
	
	private String halfFinishOutCom;//半精加工外协单位
	
	private String ispsc;//是否喷砂(半精加工)
	
	private String finishMachingWorker;//精加工加工方
	
	private String finishMachingInDate;//精加工转入
	
	private String finishMachingOutDate;//精加工转出
	
	private String finishMachingWeight;//精加工重
	
	private String finishMachingOutRemark;//精加工外协备注
	
	private String finishMachingOutCom;//精加工外协单位
	
	private String ispsd;//是否喷砂(精加工)
	
	private Long tempSonId;//模板子表主键
	
	private Long nextTempSonId;//模板主键
	
	private Integer state;//1.数据初始化  2.生产审核 3.生产中  4.检验  5.不合格  
	                      //6.待入库 7.返工 8.报废 9.入库,10.外协,11.调度,12.车加工取样,
	                      //13.暂停  14 隐藏  15.临时库 16.试样库 17.锻造分页面 18.外协分页面
	                      //19.热处理分页面 20.申请,21.返修 22.退货
	
	private Integer num;//排序序列 为了让拆分或报废的产品显示在一起
	
	private List<Product> listP;
	
	private Long processId;
	
	private String endDate;//结束日期  （条件检索临时使用）
	
	private String  ordera;//排序序号
	private Integer orderb;//排序序号
	private String orderd;//排序序号
	private Integer orderc;//排序序号
	private Integer ordere;//排序序号
	private Integer iscp;//是否做出坯
	private String inStoreDate;//入库日期
	private String lastDate;//最后完成日期
	
	private Integer ishd;//是否做合锻
	private Integer isback;//检验回退
	
	private String prevProcess;//上一个工序
	private String sampleNum;
	
	private String inspectTime;
	private Integer opState;//加工操作状态
	
	private String breakNum;//破坏件号
	
	private String processRemark;//工序备注
	private String forgingSize;//锻件尺寸
	
	private String initial;//首字母
	private String outSideInDate;//外协转入
	
	private String actUnitWeight;//实际单重
	
	@Transient
	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	@Transient
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Transient
	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	@Transient
	public List<Product> getListP() {
		return listP;
	}

	public void setListP(List<Product> listP) {
		this.listP = listP;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductNum() {
		return productNum;
	}

	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPoNum() {
		return poNum;
	}

	public void setPoNum(String poNum) {
		this.poNum = poNum;
	}

	public String getPoDate() {
		return poDate;
	}

	public void setPoDate(String poDate) {
		this.poDate = poDate;
	}

	public String getPoUpdateDate() {
		return poUpdateDate;
	}

	public void setPoUpdateDate(String poUpdateDate) {
		this.poUpdateDate = poUpdateDate;
	}

	public String getRemarksA() {
		return remarksA;
	}

	public void setRemarksA(String remarksA) {
		this.remarksA = remarksA;
	}

	public String getRemarksB() {
		return remarksB;
	}

	public void setRemarksB(String remarksB) {
		this.remarksB = remarksB;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
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

	public String getSizeA() {
		return sizeA;
	}

	public void setSizeA(String sizeA) {
		this.sizeA = sizeA;
	}

	public String getSizeB() {
		return sizeB;
	}

	public void setSizeB(String sizeB) {
		this.sizeB = sizeB;
	}

	public String getSizeC() {
		return sizeC;
	}

	public void setSizeC(String sizeC) {
		this.sizeC = sizeC;
	}

	public String getSizeD() {
		return sizeD;
	}

	public void setSizeD(String sizeD) {
		this.sizeD = sizeD;
	}

	public String getSizeE() {
		return sizeE;
	}

	public void setSizeE(String sizeE) {
		this.sizeE = sizeE;
	}

	public String getSizeF() {
		return sizeF;
	}

	public void setSizeF(String sizeF) {
		this.sizeF = sizeF;
	}

	public String getSizeG() {
		return sizeG;
	}

	public void setSizeG(String sizeG) {
		this.sizeG = sizeG;
	}

	public String getSizeH() {
		return sizeH;
	}

	public void setSizeH(String sizeH) {
		this.sizeH = sizeH;
	}

	public Integer getPoQty() {
		return poQty;
	}

	public void setPoQty(Integer poQty) {
		this.poQty = poQty;
	}

	public String getBlankWeight() {
		return blankWeight;
	}

	public void setBlankWeight(String blankWeight) {
		this.blankWeight = blankWeight;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getQualityRequire() {
		return qualityRequire;
	}

	public void setQualityRequire(String qualityRequire) {
		this.qualityRequire = qualityRequire;
	}

	public String getNdtRequire() {
		return ndtRequire;
	}

	public void setNdtRequire(String ndtRequire) {
		this.ndtRequire = ndtRequire;
	}

	public String getDeliveryState() {
		return deliveryState;
	}

	public void setDeliveryState(String deliveryState) {
		this.deliveryState = deliveryState;
	}

	public String getHtRequire() {
		return htRequire;
	}

	public void setHtRequire(String htRequire) {
		this.htRequire = htRequire;
	}

	public String getHtRequireB() {
		return htRequireB;
	}

	public void setHtRequireB(String htRequireB) {
		this.htRequireB = htRequireB;
	}

	public String getHdnsRequire() {
		return hdnsRequire;
	}

	public void setHdnsRequire(String hdnsRequire) {
		this.hdnsRequire = hdnsRequire;
	}

	public String getNextProcess() {
		return nextProcess;
	}

	public void setNextProcess(String nextProcess) {
		this.nextProcess = nextProcess;
	}

	public String getOtherRequire() {
		return otherRequire;
	}

	public void setOtherRequire(String otherRequire) {
		this.otherRequire = otherRequire;
	}

	public String getForgingEquip() {
		return forgingEquip;
	}

	public void setForgingEquip(String forgingEquip) {
		this.forgingEquip = forgingEquip;
	}

	public String getMaterialNum() {
		return materialNum;
	}

	public void setMaterialNum(String materialNum) {
		this.materialNum = materialNum;
	}

	public String getDwgNum() {
		return dwgNum;
	}

	public void setDwgNum(String dwgNum) {
		this.dwgNum = dwgNum;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getBlankDateA() {
		return blankDateA;
	}

	public void setBlankDateA(String blankDateA) {
		this.blankDateA = blankDateA;
	}

	public String getBlankRemarkA() {
		return blankRemarkA;
	}

	public void setBlankRemarkA(String blankRemarkA) {
		this.blankRemarkA = blankRemarkA;
	}

	public String getForgingInDateA() {
		return forgingInDateA;
	}

	public void setForgingInDateA(String forgingInDateA) {
		this.forgingInDateA = forgingInDateA;
	}

	public String getForgingOutDateA() {
		return forgingOutDateA;
	}

	public void setForgingOutDateA(String forgingOutDateA) {
		this.forgingOutDateA = forgingOutDateA;
	}

	public String getHeatTreatInDateA() {
		return heatTreatInDateA;
	}

	public void setHeatTreatInDateA(String heatTreatInDateA) {
		this.heatTreatInDateA = heatTreatInDateA;
	}

	public String getHeatTreatOutDateA() {
		return heatTreatOutDateA;
	}

	public void setHeatTreatOutDateA(String heatTreatOutDateA) {
		this.heatTreatOutDateA = heatTreatOutDateA;
	}

	public String getBlankDate() {
		return blankDate;
	}

	public void setBlankDate(String blankDate) {
		this.blankDate = blankDate;
	}

	public String getBlankRemark() {
		return blankRemark;
	}

	public void setBlankRemark(String blankRemark) {
		this.blankRemark = blankRemark;
	}

	public String getHeatNum() {
		return heatNum;
	}

	public void setHeatNum(String heatNum) {
		this.heatNum = heatNum;
	}

	public String getForgingWorker() {
		return forgingWorker;
	}

	public void setForgingWorker(String forgingWorker) {
		this.forgingWorker = forgingWorker;
	}

	public String getForgingInDate() {
		return forgingInDate;
	}

	public void setForgingInDate(String forgingInDate) {
		this.forgingInDate = forgingInDate;
	}

	public String getForgingOutDate() {
		return forgingOutDate;
	}

	public void setForgingOutDate(String forgingOutDate) {
		this.forgingOutDate = forgingOutDate;
	}

	public String getAfterForgingWorker() {
		return afterForgingWorker;
	}

	public void setAfterForgingWorker(String afterForgingWorker) {
		this.afterForgingWorker = afterForgingWorker;
	}

	public String getAfterForgingInDate() {
		return afterForgingInDate;
	}

	public void setAfterForgingInDate(String afterForgingInDate) {
		this.afterForgingInDate = afterForgingInDate;
	}

	public String getAtterForgingOutDate() {
		return atterForgingOutDate;
	}

	public void setAtterForgingOutDate(String atterForgingOutDate) {
		this.atterForgingOutDate = atterForgingOutDate;
	}

	public String getRoughMachingWorker() {
		return roughMachingWorker;
	}

	public void setRoughMachingWorker(String roughMachingWorker) {
		this.roughMachingWorker = roughMachingWorker;
	}

	public String getRoughMachingInDate() {
		return roughMachingInDate;
	}

	public void setRoughMachingInDate(String roughMachingInDate) {
		this.roughMachingInDate = roughMachingInDate;
	}

	public String getRoughMachingOutDate() {
		return roughMachingOutDate;
	}

	public void setRoughMachingOutDate(String roughMachingOutDate) {
		this.roughMachingOutDate = roughMachingOutDate;
	}

	public String getPerHeatTreatWorker() {
		return perHeatTreatWorker;
	}

	public void setPerHeatTreatWorker(String perHeatTreatWorker) {
		this.perHeatTreatWorker = perHeatTreatWorker;
	}

	public String getPerHeatTreatInDate() {
		return perHeatTreatInDate;
	}

	public void setPerHeatTreatInDate(String perHeatTreatInDate) {
		this.perHeatTreatInDate = perHeatTreatInDate;
	}

	public String getPerHeatTreatOutDate() {
		return perHeatTreatOutDate;
	}

	public void setPerHeatTreatOutDate(String perHeatTreatOutDate) {
		this.perHeatTreatOutDate = perHeatTreatOutDate;
	}

	public String getDeliveryWorker() {
		return deliveryWorker;
	}

	public void setDeliveryWorker(String deliveryWorker) {
		this.deliveryWorker = deliveryWorker;
	}

	public String getDeliveryInDate() {
		return deliveryInDate;
	}

	public void setDeliveryInDate(String deliveryInDate) {
		this.deliveryInDate = deliveryInDate;
	}

	public String getDeliveryOutDate() {
		return deliveryOutDate;
	}

	public void setDeliveryOutDate(String deliveryOutDate) {
		this.deliveryOutDate = deliveryOutDate;
	}

	public String getFollowupWorker() {
		return followupWorker;
	}

	public void setFollowupWorker(String followupWorker) {
		this.followupWorker = followupWorker;
	}

	public String getFollowupInDate() {
		return followupInDate;
	}

	public void setFollowupInDate(String followupInDate) {
		this.followupInDate = followupInDate;
	}

	public String getFollowupOutDate() {
		return followupOutDate;
	}

	public void setFollowupOutDate(String followupOutDate) {
		this.followupOutDate = followupOutDate;
	}

	public String getHalfFinishMachingWorker() {
		return halfFinishMachingWorker;
	}

	public void setHalfFinishMachingWorker(String halfFinishMachingWorker) {
		this.halfFinishMachingWorker = halfFinishMachingWorker;
	}

	public String getHalfFinishMachingInDate() {
		return halfFinishMachingInDate;
	}

	public void setHalfFinishMachingInDate(String halfFinishMachingInDate) {
		this.halfFinishMachingInDate = halfFinishMachingInDate;
	}

	public String getHalfFinishMachingOutDate() {
		return halfFinishMachingOutDate;
	}

	public void setHalfFinishMachingOutDate(String halfFinishMachingOutDate) {
		this.halfFinishMachingOutDate = halfFinishMachingOutDate;
	}

	public String getFinishMachingWorker() {
		return finishMachingWorker;
	}

	public void setFinishMachingWorker(String finishMachingWorker) {
		this.finishMachingWorker = finishMachingWorker;
	}

	public String getFinishMachingInDate() {
		return finishMachingInDate;
	}

	public void setFinishMachingInDate(String finishMachingInDate) {
		this.finishMachingInDate = finishMachingInDate;
	}

	public String getFinishMachingOutDate() {
		return finishMachingOutDate;
	}

	public void setFinishMachingOutDate(String finishMachingOutDate) {
		this.finishMachingOutDate = finishMachingOutDate;
	}

	public Long getTempSonId() {
		return tempSonId;
	}

	public void setTempSonId(Long tempSonId) {
		this.tempSonId = tempSonId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getSampleInDate() {
		return sampleInDate;
	}

	public void setSampleInDate(String sampleInDate) {
		this.sampleInDate = sampleInDate;
	}

	public String getSampleOutDate() {
		return sampleOutDate;
	}

	public void setSampleOutDate(String sampleOutDate) {
		this.sampleOutDate = sampleOutDate;
	}

	public Long getNextTempSonId() {
		return nextTempSonId;
	}

	public void setNextTempSonId(Long nextTempSonId) {
		this.nextTempSonId = nextTempSonId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getHlSize() {
		return hlSize;
	}

	public void setHlSize(String hlSize) {
		this.hlSize = hlSize;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getIgnotNum() {
		return ignotNum;
	}

	public void setIgnotNum(String ignotNum) {
		this.ignotNum = ignotNum;
	}

	public String getRoughMachingWeight() {
		return roughMachingWeight;
	}

	public void setRoughMachingWeight(String roughMachingWeight) {
		this.roughMachingWeight = roughMachingWeight;
	}

	public String getFinishMachingWeight() {
		return finishMachingWeight;
	}

	public void setFinishMachingWeight(String finishMachingWeight) {
		this.finishMachingWeight = finishMachingWeight;
	}

	public String getForgingWorkerA() {
		return forgingWorkerA;
	}

	public void setForgingWorkerA(String forgingWorkerA) {
		this.forgingWorkerA = forgingWorkerA;
	}

	public String getOrdera() {
		return ordera;
	}

	public void setOrdera(String ordera) {
		this.ordera = ordera;
	}

	public Integer getOrderb() {
		return orderb;
	}

	public void setOrderb(Integer orderb) {
		this.orderb = orderb;
	}

	public Integer getIscp() {
		return iscp;
	}

	public void setIscp(Integer iscp) {
		this.iscp = iscp;
	}

	public Integer getIshd() {
		return ishd;
	}

	public void setIshd(Integer ishd) {
		this.ishd = ishd;
	}

	public String getInStoreDate() {
		return inStoreDate;
	}

	public void setInStoreDate(String inStoreDate) {
		this.inStoreDate = inStoreDate;
	}

	public String getPrevProcess() {
		return prevProcess;
	}

	public void setPrevProcess(String prevProcess) {
		this.prevProcess = prevProcess;
	}

	public String getIspsa() {
		return ispsa;
	}

	public void setIspsa(String ispsa) {
		this.ispsa = ispsa;
	}

	public String getIspsb() {
		return ispsb;
	}

	public void setIspsb(String ispsb) {
		this.ispsb = ispsb;
	}

	public String getIspsc() {
		return ispsc;
	}

	public void setIspsc(String ispsc) {
		this.ispsc = ispsc;
	}

	public String getIspsd() {
		return ispsd;
	}

	public void setIspsd(String ispsd) {
		this.ispsd = ispsd;
	}

	public String getForgingRemark() {
		return forgingRemark;
	}

	public void setForgingRemark(String forgingRemark) {
		this.forgingRemark = forgingRemark;
	}

	public String getSampleNum() {
		return sampleNum;
	}

	public void setSampleNum(String sampleNum) {
		this.sampleNum = sampleNum;
	}

	public Integer getOrderc() {
		return orderc;
	}

	public void setOrderc(Integer orderc) {
		this.orderc = orderc;
	}

	public String getAfterForgingOut() {
		return afterForgingOut;
	}

	public void setAfterForgingOut(String afterForgingOut) {
		this.afterForgingOut = afterForgingOut;
	}

	public String getRoughMachingRemark() {
		return roughMachingRemark;
	}

	public void setRoughMachingRemark(String roughMachingRemark) {
		this.roughMachingRemark = roughMachingRemark;
	}

	public String getRoughMachingOutRemark() {
		return roughMachingOutRemark;
	}

	public void setRoughMachingOutRemark(String roughMachingOutRemark) {
		this.roughMachingOutRemark = roughMachingOutRemark;
	}

	public String getDeliveryOutRemark() {
		return deliveryOutRemark;
	}

	public void setDeliveryOutRemark(String deliveryOutRemark) {
		this.deliveryOutRemark = deliveryOutRemark;
	}

	public String getHalfFinishOutRemark() {
		return halfFinishOutRemark;
	}

	public void setHalfFinishOutRemark(String halfFinishOutRemark) {
		this.halfFinishOutRemark = halfFinishOutRemark;
	}

	public String getFinishMachingOutRemark() {
		return finishMachingOutRemark;
	}

	public void setFinishMachingOutRemark(String finishMachingOutRemark) {
		this.finishMachingOutRemark = finishMachingOutRemark;
	}

	public String getRoughMachingOutCom() {
		return roughMachingOutCom;
	}

	public void setRoughMachingOutCom(String roughMachingOutCom) {
		this.roughMachingOutCom = roughMachingOutCom;
	}

	public String getDeliveryOutCom() {
		return deliveryOutCom;
	}

	public void setDeliveryOutCom(String deliveryOutCom) {
		this.deliveryOutCom = deliveryOutCom;
	}

	public String getHalfFinishOutCom() {
		return halfFinishOutCom;
	}

	public void setHalfFinishOutCom(String halfFinishOutCom) {
		this.halfFinishOutCom = halfFinishOutCom;
	}

	public String getFinishMachingOutCom() {
		return finishMachingOutCom;
	}

	public void setFinishMachingOutCom(String finishMachingOutCom) {
		this.finishMachingOutCom = finishMachingOutCom;
	}

	public String getInspectTime() {
		return inspectTime;
	}

	public void setInspectTime(String inspectTime) {
		this.inspectTime = inspectTime;
	}

	public String getOrderd() {
		return orderd;
	}

	public void setOrderd(String orderd) {
		this.orderd = orderd;
	}

	public Integer getOrdere() {
		return ordere;
	}

	public void setOrdere(Integer ordere) {
		this.ordere = ordere;
	}

	public Integer getOpState() {
		return opState;
	}

	public void setOpState(Integer opState) {
		this.opState = opState;
	}

	public Integer getIsback() {
		return isback;
	}

	public void setIsback(Integer isback) {
		this.isback = isback;
	}

	public String getBlankInspectDate() {
		return blankInspectDate;
	}

	public void setBlankInspectDate(String blankInspectDate) {
		this.blankInspectDate = blankInspectDate;
	}

	public String getBreakNum() {
		return breakNum;
	}

	public void setBreakNum(String breakNum) {
		this.breakNum = breakNum;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public String getProcessRemark() {
		return processRemark;
	}

	public void setProcessRemark(String processRemark) {
		this.processRemark = processRemark;
	}

	public Product(Long id, Long processId) {
		super();
		this.id = id;
		this.processId = processId;
	}

	public Product() {
		super();
	}

	public String getForgingSize() {
		return forgingSize;
	}

	public void setForgingSize(String forgingSize) {
		this.forgingSize = forgingSize;
	}

	public String getOutSideInDate() {
		return outSideInDate;
	}

	public void setOutSideInDate(String outSideInDate) {
		this.outSideInDate = outSideInDate;
	}
	
	
	public String getActUnitWeight() {
		return actUnitWeight;
	}

	public void setActUnitWeight(String actUnitWeight) {
		this.actUnitWeight = actUnitWeight;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}
