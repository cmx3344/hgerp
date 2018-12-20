package com.hg.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Example;

import com.hg.model.Ncr;
import com.hg.model.NcrFiles;
import com.hg.model.NcrSon;
import com.hg.model.Product;
import com.hg.util.ItextUtil;
import com.hg.util.OtherUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ReportService {

	
	
	private static BaseFont bf ;
	
	private static String path="";
	
	private  static SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	
	static{
		try {
			bf =  ItextUtil.getBaseFont();
			path = OtherUtil.getAbsolutePath();
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String ncrFirstPage(Ncr ncr,List<NcrSon> list,List<Product> listp) throws FileNotFoundException, DocumentException{
		
		com.itextpdf.text.Document doc = ItextUtil.getDocument(0, 0, 10, 5);
		String now = simple.format(new Date());
		OtherUtil.fileMkdir(path+"ncrReport/"+now);
		PdfWriter.getInstance(doc, new FileOutputStream(path+"ncrReport/"+now+"/ncrFirstPage.pdf"));  
		Font font = ItextUtil.getFont(bf, 20, Font.BOLD);
		Font font2 = ItextUtil.getFont(bf, 10, Font.NORMAL);
		Font font3 = ItextUtil.getFont(bf, 8, Font.NORMAL);
		doc.open();
		PdfPTable table = ItextUtil.getPdfPTable(new float[]{1,10}, 96);
		PdfPCell cell = ItextUtil.getImageCell(path+"image/logo.png",30, 30);
		cell.setRowspan(3);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell.setBorderWidth(0);
		table.addCell(cell);
		cell = ItextUtil.getDefaultTextCell("不合格品处置报告 Nonconforming Report", font, 10);
		cell.setBorderWidth(0);
		cell.setPaddingLeft(50);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCellByRight("NO:"+ncr.getNcrNum(), font2, 10);
		cell.setColspan(2);
		cell.setBorder(0);
		table.addCell(cell);
		doc.add(table);
		table = ItextUtil.getPdfPTable(new float[]{1,1,1,1,1,1,1,1,1,1,1,1,0.32f}, 96);
		
		cell = ItextUtil.getTextCell("产品状态\nProduct status", font2, 35);
		cell.setColspan(2);
		table.addCell(cell);
		
		PdfPTable table2 = ItextUtil.getPdfPTable(new float[]{1,2,1,2,1,2,1,2,1,2,1,2,1,2}, 100);
		cell = ItextUtil.getImageCell(("原料").equals(ncr.getFromProcess())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setBorder(0);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("原料", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell = ItextUtil.getImageCell(("毛坯").equals(ncr.getFromProcess())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("毛坯", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell = ItextUtil.getImageCell(("粗加工").equals(ncr.getFromProcess())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("粗加工", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell = ItextUtil.getImageCell(("精加工").equals(ncr.getFromProcess())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("精加工", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell = ItextUtil.getImageCell(("热处理").equals(ncr.getFromProcess())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("热处理", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell = ItextUtil.getImageCell(("客退").equals(ncr.getFromProcess())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("客退", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell = ItextUtil.getImageCell(("其他").equals(ncr.getFromProcess())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("其他", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table2.addCell(cell);
		//第二行
		cell =ItextUtil.getTextCell("Raw material", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("Forging", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("Rough maching", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("Precise maching", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("Heat treatment", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("Customer return", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("otherss", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table2.addCell(cell);
		
		cell = new PdfPCell(table2);
		cell.setColspan(10);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCell("质量部填写", font3, 15);
		cell.setRowspan(7);
		table.addCell(cell);
		
		
		PdfPTable table3 = ItextUtil.getPdfPTable(new float[]{1,2,1,2,1,2}, 100);
		cell = ItextUtil.getImageCell(("供应商").equals(ncr.getUnitType())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table3.addCell(cell);
		cell =ItextUtil.getTextCell("供应商", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table3.addCell(cell);
		cell = ItextUtil.getImageCell(("工序").equals(ncr.getUnitType())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table3.addCell(cell);
		cell =ItextUtil.getTextCell("工序", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table3.addCell(cell);
		cell = ItextUtil.getImageCell(("客户").equals(ncr.getUnitType())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table3.addCell(cell);
		cell =ItextUtil.getTextCell("客户", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table3.addCell(cell);
		cell =ItextUtil.getTextCell("Supplier", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table3.addCell(cell);
		cell =ItextUtil.getTextCell("Process", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table3.addCell(cell);
		cell =ItextUtil.getTextCell("Customer", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table3.addCell(cell);
		cell = new PdfPCell(table3);
		cell.setColspan(3);
		table.addCell(cell);
		cell = ItextUtil.getTextCell(ncr.getUnits(), font3, 25);
		cell.setColspan(3);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("锻件编号\nForging NO.", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		StringBuffer sb = new StringBuffer();
		Integer total = 0;
		for(Product son : listp){
			sb.append(son.getSerialNum()+" ~ "+son.getOrderNum());
			sb.append("\n");
			try {
				total = total+son.getQty();
			} catch (Exception e) {
				total = total+1;
			}
		}
		cell = ItextUtil.getTextCell(sb.toString(), font2, 25);
		cell.setColspan(4);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("规格型号\nSpecifications", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		StringBuffer size = new StringBuffer();
		size.append(listp.get(0).getSizeA()).append(listp.get(0).getSizeB()).append(listp.get(0).getSizeC())
		.append(listp.get(0).getSizeD()).append(listp.get(0).getSizeE()).append(listp.get(0).getSizeF()).append(listp.get(0).getSizeG())
		.append(listp.get(0).getSizeH());
		cell = ItextUtil.getTextCell(size.toString(), font3, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("材料牌号\nMaterial type", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell(listp.get(0).getMaterial(), font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("不良数量\nQuantity", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell(total.toString(), font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCell("检验员\nInspertor", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell(ncr.getReportPerson(), font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("材料炉号\nBatch NO.", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell(listp.get(0).getHeatNum(), font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("报告日期\nDate", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell(ncr.getReportDate(), font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		
//		PdfPTable table4 = ItextUtil.getPdfPTable(new float[]{1}, 100);
		cell = ItextUtil.getTextCellByLeft("不良问题描述 Noconforming description:", font2, 25);
		cell.setBorderWidthBottom(0);
		cell.setColspan(12);
		table.addCell(cell);
//		cell = new PdfPCell(table4);
//		cell.setBorder(0);
//		cell.setBorderWidthLeft(1);
//		cell.setColspan(12);
//		table.addCell(cell);
		cell = ItextUtil.getTextCellByLeft(ncr.getReason(), font2, 100);
		cell.setBorderWidthBottom(0);
		cell.setBorderWidthTop(0);
		cell.setColspan(12);
		table.addCell(cell);
		
//		PdfPTable table5 = ItextUtil.getPdfPTable(new float[]{1}, 100);
		String signa = ncr.getExamestart()==null?"":ncr.getExamestart();
		cell = ItextUtil.getTextCellByRight("审核签名 Sign:"+signa, font2, 25);
		cell.setColspan(12);
		cell.setBorderWidthTop(0);
		table.addCell(cell);
//		cell = new PdfPCell(table5);
//		cell.setColspan(12);
//		cell.setBorder(0);
//		cell.setBorderWidthLeft(1);
//		table.addCell(cell);
		
		cell = ItextUtil.getTextCell("不合格品处理意见\nNonconforming\nconclusion\n(技术部主管及以上填写)", font2, 25);
		cell.setColspan(2);
		cell.setRowspan(4);
		table.addCell(cell);
		
		PdfPTable table6 = ItextUtil.getPdfPTable(new float[]{1,2,1,2,1,2,1,2,1,2,1,2}, 90);
		boolean boo = list!=null&&list.size()>0&&list.get(0).getHandleType()!=null;
		cell = ItextUtil.getImageCell(boo&&list.get(0).getHandleType().intValue()==3?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setBorder(0);
		table6.addCell(cell);
		cell =ItextUtil.getTextCell("让步", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table6.addCell(cell);
		cell = ItextUtil.getImageCell(boo&&list.get(0).getHandleType().intValue()==1?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table6.addCell(cell);
		cell =ItextUtil.getTextCell("返工", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table6.addCell(cell);
		cell = ItextUtil.getImageCell(boo&&list.get(0).getHandleType().intValue()==5?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table6.addCell(cell);
		cell =ItextUtil.getTextCell("返修", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table6.addCell(cell);
		cell = ItextUtil.getImageCell(boo&&list.get(0).getHandleType().intValue()==2?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table6.addCell(cell);
		cell =ItextUtil.getTextCell("报废", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table6.addCell(cell);
		cell = ItextUtil.getImageCell(boo&&list.get(0).getHandleType().intValue()==6?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table6.addCell(cell);
		cell =ItextUtil.getTextCell("退货", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table6.addCell(cell);
		cell = ItextUtil.getImageCell(boo&&list.get(0).getHandleType().intValue()==7||boo&&list.get(0).getHandleType().intValue()==4?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table6.addCell(cell);
		cell =ItextUtil.getTextCell("其他", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table6.addCell(cell);
		//第二行
		cell =ItextUtil.getTextCell("Deviation", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table6.addCell(cell);
		cell =ItextUtil.getTextCell("Rework", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table6.addCell(cell);
		cell =ItextUtil.getTextCell("Sorting", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table6.addCell(cell);
		cell =ItextUtil.getTextCell("Scrap", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table6.addCell(cell);
		cell =ItextUtil.getTextCell("Reject", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table6.addCell(cell);
		cell =ItextUtil.getTextCell("Others", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table6.addCell(cell);
		cell = new PdfPCell(table6);
		cell.setColspan(10);
		table.addCell(cell);
		cell =ItextUtil.getTextCell("技术部", font3, 10);
		cell.setRowspan(4);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCell("责任单位:\nAccountability unit", font3, 25);
		cell.setColspan(2);
		cell.setBorder(0);
		table.addCell(cell);
		cell = ItextUtil.getTextCellByLeft(ncr.getResponsibleDep(), font2, 25);
		cell.setBorder(0);
		cell.setColspan(4);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("执行单位:\nExecution unit", font3, 25);
		cell.setColspan(2);
		cell.setBorder(0);
		cell.setBorderWidthLeft(1);
		table.addCell(cell);
		cell = ItextUtil.getTextCellByLeft(ncr.getExecuteUnit(), font2, 25);
		cell.setColspan(2);
		cell.setBorder(0);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCellByLeft("备注Note:"+ncr.getRemarks(), font2, 25);
		cell.setColspan(6);
		cell.setBorderWidthRight(0);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setRowspan(2);
		table.addCell(cell);
		
		String signb = ncr.getExameend()==null?"":ncr.getExameend();
		cell = ItextUtil.getTextCellByRight("签名Sign:"+signb, font2, 25);
		cell.setColspan(4);
		cell.setRowspan(2);
		cell.setBorderWidthLeft(0);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setPaddingRight(100);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCell("评审意见会签NCR review conmment (各相关部门主管及以上人员会签，如有异议则需总经理判决)", font2, 25);
		cell.setColspan(12);
		table.addCell(cell);
		cell = ItextUtil.getTextCellByRight("参与评审部门", font3, 25);
		cell.setRowspan(4);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCell("质量部/QA", font2, 20);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("生产部/Production", font2, 20);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("技术部/Enginieer", font2, 20);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("销售部/Sales", font2, 20);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("采购部/Purchase", font2, 20);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("仓库/Warehouse", font2, 20);
		cell.setColspan(2);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCell("", font3, 80);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font3, 80);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font3, 80);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font3, 80);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font3, 80);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font3, 80);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("签名:\nSign", font2, 20);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 20);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("签名:\nSign", font2, 20);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 20);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("签名:\nSign", font2, 20);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 20);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("签名:\nSign", font2, 20);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 20);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("签名:\nSign", font2, 20);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 20);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("签名:\nSign", font2, 20);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 20);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCell("总经理判卷GM conclusion\n(最终处置结论)", font2, 35);
		cell.setColspan(4);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 35);
		cell.setColspan(8);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("总经理", font3, 35);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("原因分析Root cause", font3, 50);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorderWidthRight(0);
		cell.setRowspan(2);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 35);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorderWidthLeft(0);
		cell.setBorderWidthRight(0);
		cell.setRowspan(2);
		cell.setColspan(6);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("签名Sign:", font2, 35);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setBorderWidthLeft(0);
		cell.setBorderWidthRight(0);
		cell.setRowspan(2);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 35);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setBorderWidthLeft(0);
		cell.setRowspan(2);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("责任单位", font3, 35);
		cell.setRowspan(2);
		table.addCell(cell);
		
		
		
		cell = ItextUtil.getTextCell("纠正/返修部门:\nRework Dept", font2, 30);
		cell.setBorderWidthRight(0);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 30);
		cell.setBorderWidthRight(0);
		cell.setBorderWidthLeft(0);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("负责人:\nOwner", font2, 30);
		cell.setBorderWidthRight(0);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 30);
		cell.setBorderWidthRight(0);
		cell.setBorderWidthLeft(0);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("完成日期:\nComplete date", font2, 30);
		cell.setBorderWidthRight(0);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 30);
		cell.setBorderWidthRight(0);
		cell.setBorderWidthLeft(0);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("返修部", font3, 30);
		table.addCell(cell);
		
		PdfPTable table7 = ItextUtil.getPdfPTable(new float[]{1,2,1,2,1,2}, 100);
		cell =ItextUtil.getTextCellByLeft("纠正/返修后检验结论:", font3, 10);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		cell.setColspan(2);
		table7.addCell(cell);
		cell = ItextUtil.getImageCell(path+"image/checkbox_yes.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table7.addCell(cell);
		cell =ItextUtil.getTextCell("合格", font3, 10);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table7.addCell(cell);
		cell = ItextUtil.getImageCell(path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table7.addCell(cell);
		cell =ItextUtil.getTextCell("不合格", font3, 10);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table7.addCell(cell);
		cell =ItextUtil.getTextCellByLeft("Rework status", font2, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table7.addCell(cell);
		cell =ItextUtil.getTextCell("Accept", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table7.addCell(cell);
		cell =ItextUtil.getTextCell("Nonconforming", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table7.addCell(cell);
		cell = new PdfPCell(table7);
		cell.setColspan(6);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCell("检验员/日期\nInspector/date", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 25);
		cell.setColspan(4);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("质量部", font3, 25);
		cell.setRowspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCellByLeft("纠正/返修后不合格处理结论:\nUnqualified processing conclusion after repair", font2, 25);
		cell.setColspan(6);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("执行部门\nRun Dept.", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 25);
		cell.setColspan(4);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCell("返修/报废\n费用统计\nCost statistic", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
	
		PdfPTable table8 = ItextUtil.getPdfPTable(new float[]{1,1,1,1,1,1,1}, 100);
		cell = ItextUtil.getTextCell("原料费用\nMaterial", font2, 15);
		table8.addCell(cell);
		cell = ItextUtil.getTextCell("锻造费用\nForging", font2, 15);
		table8.addCell(cell);
		cell = ItextUtil.getTextCell("热处理费用\nHT", font2, 15);
		table8.addCell(cell);
		cell = ItextUtil.getTextCell("机加工费用\nMaching", font2, 15);
		table8.addCell(cell);
		cell = ItextUtil.getTextCell("检验费用\nInspect", font2, 15);
		table8.addCell(cell);
		cell = ItextUtil.getTextCell("其他费用\nOthers", font2, 15);
		table8.addCell(cell);
		cell = ItextUtil.getTextCell("合计\nTotal", font2, 15);
		table8.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 15);
		table8.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 15);
		table8.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 15);
		table8.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 15);
		table8.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 15);
		table8.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 15);
		table8.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 15);
		table8.addCell(cell);
		cell = new PdfPCell(table8);
		cell.setColspan(10);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("生产计划", font2, 25);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCell("备注", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		
		
		StringBuffer result = new StringBuffer();
		result.append("1.此表使用时机：适用公司内部所有批量不良、重复发生不良、客诉不良;").append("\n");
		result.append("2.技术部界定责任单位、执行单位，如有必要组织评审会议，同时此表的评审意见会签栏由各部门主管或经理以上填写;").append("\n");
		result.append("3.若责任单位或执行单位不执行，则由不配合单位召集相关单位做评审并签核到总经理处;").append("\n");
		result.append("4.批量或重大不合格，由质量部向责任单位或供应商发放《纠正预防措施报告》。").append("\n");
		cell = ItextUtil.getTextCellByLeft(result.toString(), font3, 25);
		cell.setColspan(11);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("本单由质量部开出,相关部门填写完毕后,最后由生产计划转至质量部存档", font2, 25);
		cell.setColspan(8);
		cell.setBorder(0);
		table.addCell(cell);
		cell = ItextUtil.getTextCellByRight("文件编号:HG/QR-8.3-01", font2, 25);
		cell.setColspan(5);
		cell.setBorder(0);
		table.addCell(cell);
		
		doc.add(table);
	    doc.close();
	    return path+"ncrReport/"+now+"/ncrFirstPage.pdf";
	}
	
	
	public static String ncrSecondPage(Ncr ncr,List<NcrSon> list,List<Product> listp,List<NcrFiles> listFile) throws FileNotFoundException, DocumentException{
		com.itextpdf.text.Document doc = ItextUtil.getDocument(0, 0, 10, 5);
		String now = simple.format(new Date());
		OtherUtil.fileMkdir(path+"ncrReport/"+now);
		PdfWriter.getInstance(doc, new FileOutputStream(path+"ncrReport/"+now+"/ncrSecondPage.pdf"));  
		Font font = ItextUtil.getFont(bf, 20, Font.BOLD);
		Font font2 = ItextUtil.getFont(bf, 10, Font.NORMAL);
		Font font3 = ItextUtil.getFont(bf, 8, Font.NORMAL);
		doc.open();
		PdfPTable table = ItextUtil.getPdfPTable(new float[]{1,10}, 96);
		PdfPCell cell = ItextUtil.getImageCell(path+"image/logo.png",30, 30);
		cell.setRowspan(3);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell.setBorderWidth(0);
		table.addCell(cell);
		cell = ItextUtil.getDefaultTextCell("不合格品处置报告 Nonconforming Report", font, 10);
		cell.setBorderWidth(0);
		cell.setPaddingLeft(50);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCellByRight("NO:"+ncr.getNcrNum(), font2, 10);
		cell.setColspan(2);
		cell.setBorder(0);
		table.addCell(cell);
		doc.add(table);
		table = ItextUtil.getPdfPTable(new float[]{1,1,1,1,1,1,1,1,1,1,1,1,0.32f}, 96);
		
		cell = ItextUtil.getTextCell("产品状态\nProduct status", font2, 35);
		cell.setColspan(2);
		table.addCell(cell);
		
		PdfPTable table2 = ItextUtil.getPdfPTable(new float[]{1,2,1,2,1,2,1,2,1,2,1,2,1,2}, 100);
		cell = ItextUtil.getImageCell(("原料").equals(ncr.getFromProcess())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setBorder(0);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("原料", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell = ItextUtil.getImageCell(("毛坯").equals(ncr.getFromProcess())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("毛坯", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell = ItextUtil.getImageCell(("粗加工").equals(ncr.getFromProcess())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("粗加工", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell = ItextUtil.getImageCell(("精加工").equals(ncr.getFromProcess())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("精加工", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell = ItextUtil.getImageCell(("热处理").equals(ncr.getFromProcess())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("热处理", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell = ItextUtil.getImageCell(("客退").equals(ncr.getFromProcess())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("客退", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell = ItextUtil.getImageCell(("其他").equals(ncr.getFromProcess())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("其他", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table2.addCell(cell);
		//第二行
		cell =ItextUtil.getTextCell("Raw material", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("Forging", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("Rough maching", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("Precise maching", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("Heat treatment", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("Customer return", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table2.addCell(cell);
		cell =ItextUtil.getTextCell("otherss", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table2.addCell(cell);
		
		cell = new PdfPCell(table2);
		cell.setColspan(10);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCell("质量部填写", font3, 15);
		cell.setRowspan(7);
		table.addCell(cell);
		
		
		PdfPTable table3 = ItextUtil.getPdfPTable(new float[]{1,2,1,2,1,2}, 100);
		cell = ItextUtil.getImageCell(("供应商").equals(ncr.getUnitType())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table3.addCell(cell);
		cell =ItextUtil.getTextCell("供应商", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table3.addCell(cell);
		cell = ItextUtil.getImageCell(("工序").equals(ncr.getUnitType())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table3.addCell(cell);
		cell =ItextUtil.getTextCell("工序", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table3.addCell(cell);
		cell = ItextUtil.getImageCell(("客户").equals(ncr.getUnitType())?path+"image/checkbox_yes.png":path+"image/checkbox_no.png",10, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		cell.setBorder(0);
		table3.addCell(cell);
		cell =ItextUtil.getTextCell("客户", font3, 15);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		cell.setBorder(0);
		table3.addCell(cell);
		cell =ItextUtil.getTextCell("Supplier", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table3.addCell(cell);
		cell =ItextUtil.getTextCell("Process", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table3.addCell(cell);
		cell =ItextUtil.getTextCell("Customer", font3, 10);
		cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		cell.setBorder(0);
		cell.setColspan(2);
		table3.addCell(cell);
		cell = new PdfPCell(table3);
		cell.setColspan(3);
		table.addCell(cell);
		cell = ItextUtil.getTextCell(ncr.getUnits(), font3, 25);
		cell.setColspan(3);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("锻件编号\nForging NO.", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		StringBuffer sb = new StringBuffer();
		Integer total = 0;
		for(Product son : listp){
			sb.append(son.getSerialNum()+" ~ "+son.getOrderNum());
			sb.append("\n");
			try {
				total = total+son.getQty();
			} catch (Exception e) {
				total = total+1;
			}
		}
		cell = ItextUtil.getTextCell(sb.toString(), font2, 25);
		cell.setColspan(4);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("规格型号\nSpecifications", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		StringBuffer size = new StringBuffer();
		size.append(listp.get(0).getSizeA()).append(listp.get(0).getSizeB()).append(listp.get(0).getSizeC())
		.append(listp.get(0).getSizeD()).append(listp.get(0).getSizeE()).append(listp.get(0).getSizeF()).append(listp.get(0).getSizeG())
		.append(listp.get(0).getSizeH());
		cell = ItextUtil.getTextCell(size.toString(), font3, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("材料牌号\nMaterial type", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell(listp.get(0).getMaterial(), font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("不良数量\nQuantity", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell(total.toString(), font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCell("检验员\nInspertor", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell(ncr.getReportPerson(), font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("材料炉号\nBatch NO.", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell(listp.get(0).getHeatNum(), font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("报告日期\nDate", font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		cell = ItextUtil.getTextCell(ncr.getReportDate(), font2, 25);
		cell.setColspan(2);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCell("不良问题描述 Noconforming description", font2, 25);
		cell.setColspan(12);
		table.addCell(cell);
		PdfPTable table4 = ItextUtil.getPdfPTable(new float[]{1,1,1}, 100);
		if(null != listFile&&listFile.size()>0){
			for(NcrFiles ncrf : listFile){
				cell = ItextUtil.getImageCell(path+"files"+ncrf.getFile(), 180, 180);
				cell.setPadding(1);
				cell.setBorderWidth(0);
				table4.addCell(cell);
			}
			if(listFile.size()%3!=0){
				for(int x =0;x<3-(listFile.size()%3);x++){
					cell = ItextUtil.getTextCell("", font2, 25);
					cell.setBorderWidth(0);
					table4.addCell(cell);
				}
			}
		}
		cell = new PdfPCell(table4);
		cell.setColspan(12);
		cell.setMinimumHeight(500);
		cell.setBorderWidthBottom(0);
		table.addCell(cell);
		String signa = ncr.getExamestart()==null?"":ncr.getExamestart();
		cell = ItextUtil.getTextCellByRight("审核签名 Sign:"+signa, font2, 25);
		cell.setColspan(12);
		cell.setPaddingRight(100);
		cell.setBorderWidthTop(0);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCell("备注", font2, 25);
		cell.setColspan(1);
		table.addCell(cell);
		cell = ItextUtil.getTextCell(ncr.getRemarks(), font2, 50);
		cell.setColspan(12);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCell("本单由质量部开出,相关部门填写完毕后,最后由生产计划转至质量部存档", font2, 25);
		cell.setColspan(8);
		cell.setBorder(0);
		table.addCell(cell);
		cell = ItextUtil.getTextCellByRight("文件编号:HG/QR-8.3-01", font2, 25);
		cell.setColspan(5);
		cell.setBorder(0);
		table.addCell(cell);
		
		doc.add(table);
	    doc.close();
	    return path+"ncrReport/"+now+"/ncrSecondPage.pdf";
	}
	
	public static String ncrThirdPage(Ncr ncr,List<NcrSon> list,List<Product> listp,List<NcrFiles> listFile) throws FileNotFoundException, DocumentException{
		com.itextpdf.text.Document doc = ItextUtil.getDocument(0, 0, 10, 5);
		String now = simple.format(new Date());
		OtherUtil.fileMkdir(path+"ncrReport/"+now);
		PdfWriter.getInstance(doc, new FileOutputStream(path+"ncrReport/"+now+"/ncrThirdPage.pdf"));  
		Font font = ItextUtil.getFont(bf, 20, Font.BOLD);
		Font font2 = ItextUtil.getFont(bf, 10, Font.NORMAL);
		Font font4 = ItextUtil.getFont(bf, 15, Font.NORMAL);
		doc.open();
		PdfPTable table = ItextUtil.getPdfPTable(new float[]{1,1,1,2,1,1.5f}, 96);
		
		PdfPCell cell = ItextUtil.getTextCell("锻件超声波检测报告", font, 25);
		cell.setColspan(6);
		cell.setBorderWidthBottom(0);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("Forging Ultrasonic Examinating Report", font4, 25);
		cell.setColspan(6);
		cell.setBorderWidthTop(0);
		cell.setBorderWidthBottom(0);
		table.addCell(cell);
		
		cell = ItextUtil.getTextCellByLeft("HG/QR-8.2.4-06", font4, 25);
		cell.setColspan(3);
		cell.setBorderWidthTop(0);
		cell.setBorderWidthRight(0);
		table.addCell(cell);
		cell = ItextUtil.getTextCellByRight("编号:"+ncr.getNcrNum(), font4, 25);
		cell.setColspan(3);
		cell.setBorderWidthTop(0);
		cell.setBorderWidthLeft(0);
		table.addCell(cell);
		StringBuffer sb = new StringBuffer();
		Integer total = 0;
		for(Product son : listp){
			sb.append(son.getSerialNum()+" ~ "+son.getOrderNum());
			sb.append("\n");
			try {
				total = total+son.getQty();
			} catch (Exception e) {
				total = total+1;
			}
		}
		ItextUtil.setPdfBody(table, new PdfPCell[]{
				ItextUtil.getTextCell("锻件名称", font2, 25),
				ItextUtil.getTextCell("锻件", font2, 25),
				ItextUtil.getTextCell("客户名称", font2, 25),
				ItextUtil.getTextCell(sb.toString(), font2, 25),
				ItextUtil.getTextCell("炉号", font2, 25),
				ItextUtil.getTextCell(listp.get(0).getHeatNum(), font2, 25)
		});
		StringBuffer size = new StringBuffer();
		size.append(listp.get(0).getSizeA()).append(listp.get(0).getSizeB()).append(listp.get(0).getSizeC())
		.append(listp.get(0).getSizeD()).append(listp.get(0).getSizeE()).append(listp.get(0).getSizeF()).append(listp.get(0).getSizeG())
		.append(listp.get(0).getSizeH());
		ItextUtil.setPdfBody(table, new PdfPCell[]{
				ItextUtil.getTextCell("材料牌号", font2, 25),
				ItextUtil.getTextCell(listp.get(0).getMaterial(), font2, 25),
				ItextUtil.getTextCell("锻件规格", font2, 25),
				ItextUtil.getTextCell(size.toString(), font2, 25),
				ItextUtil.getTextCell("数量", font2, 25),
				ItextUtil.getTextCell(total.toString(), font2, 25)
		});
		ItextUtil.setPdfBody(table, new PdfPCell[]{
				ItextUtil.getTextCell("热处理", font2, 25),
				ItextUtil.getTextCell(listp.get(0).getHtRequireB(), font2, 25),
				ItextUtil.getTextCell("锻件状态", font2, 25),
				ItextUtil.getTextCell(ncr.getFromProcess(), font2, 25),
				ItextUtil.getTextCell("锻件形状", font2, 25),
				ItextUtil.getTextCell("/", font2, 25)
		});
		ItextUtil.setPdfBody(table, new PdfPCell[]{
				ItextUtil.getTextCell("仪器型号", font2, 25),
				ItextUtil.getTextCell("CTS-4020", font2, 25),
				ItextUtil.getTextCell("探头型号", font2, 25),
				ItextUtil.getTextCell("2.5PΦ20Z", font2, 25),
				ItextUtil.getTextCell("扫查方式", font2, 25),
				ItextUtil.getTextCell("锯齿形扫查法", font2, 25)
		});
		ItextUtil.setPdfBody(table, new PdfPCell[]{
				ItextUtil.getTextCell("对比试块", font2, 25),
				ItextUtil.getTextCell("工件大平底", font2, 25),
				ItextUtil.getTextCell("表面补偿", font2, 25),
				ItextUtil.getTextCell("--", font2, 25),
				ItextUtil.getTextCell("检测比例", font2, 25),
				ItextUtil.getTextCell("100.00%", font2, 25)
		});
		ItextUtil.setPdfBody(table, new PdfPCell[]{
				ItextUtil.getTextCell("探伤方法", font2, 25),
				ItextUtil.getTextCell("纵波反射法", font2, 25),
				ItextUtil.getTextCell("耦合剂", font2, 25),
				ItextUtil.getTextCell("机油", font2, 25),
				ItextUtil.getTextCell("检测面", font2, 25),
				ItextUtil.getTextCell("--", font2, 25)
		});
		ItextUtil.setPdfBody(table, new PdfPCell[]{
				ItextUtil.getTextCell("探测灵敏度", font2, 25),
				ItextUtil.getTextCell("Φ2", font2, 25),
				ItextUtil.getTextCell("验收标准", font2, 25),
				ItextUtil.getTextCell("JB/T5000.15-1998", font2, 25),
				ItextUtil.getTextCell("合格级别", font2, 25),
				ItextUtil.getTextCell("Ⅱ", font2, 25)
		});
		cell = ItextUtil.getTextCell("检测情况", font, 30);
		cell.setBorderWidthBottom(0);
		cell.setColspan(6);
		table.addCell(cell);
		PdfPTable imgTable = ItextUtil.getPdfPTable(new float[]{1,1,1}, 100);
		if(null != listFile&&listFile.size()>0){
			for(NcrFiles ncrf : listFile){
				cell = ItextUtil.getImageCell(path+"files"+ncrf.getFile(), 200, 200);
				cell.setPadding(1);
				cell.setBorderWidth(0);
				imgTable.addCell(cell);
			}
			if(listFile.size()%3!=0){
				for(int x =0;x<3-(listFile.size()%3);x++){
					cell = ItextUtil.getTextCell("", font2, 25);
					cell.setBorderWidth(0);
					imgTable.addCell(cell);
				}
			}
		}
		cell = new PdfPCell(imgTable);
		cell.setColspan(6);
		cell.setMinimumHeight(450);
		cell.setBorderWidthBottom(0);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("评定结果", font2, 25);
		cell.setColspan(1);
		table.addCell(cell);
		cell = ItextUtil.getTextCellByLeft("   不符合JB/T5000.15-1998 Ⅱ级的要求，该工件不合格", font2, 35);
		cell.setColspan(5);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("报告编制", font2, 25);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("李绍基", font2, 25);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("报告审核", font2, 25);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("苏建明", font2, 25);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("无损检测专用章", font2, 25);
		table.addCell(cell);
		cell = ItextUtil.getTextCell("", font2, 25);
		table.addCell(cell);
		
		doc.add(table);
	    doc.close();
	    return path+"ncrReport/"+now+"/ncrThirdPage.pdf";
	}
}
