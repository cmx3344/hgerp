package com.hg.controller.productProcess;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hg.dao.ProductJPADao;
import com.hg.dao.productProcess.ForgingSizeDao;
import com.hg.model.Product;
import com.hg.model.productProcess.ForgingSize;
import com.hg.service.ProductProcessService;
import com.hg.util.ExcelUtil;
import com.hg.util.PageInfo;
import com.hg.util.PageResponse;


@Controller
@RequestMapping("productProcess")
public class ProductProcessController {
	
	@Inject
	private ForgingSizeDao forgingSizeDao;
	@Inject
	private ProductProcessService productProcessService;
	@Inject
	private ProductJPADao productJPADao;
	
	
	@RequestMapping("/list")
	public String list(){
		
		return "main/productProcess/list";
	}
	
	@RequestMapping("/getList")
	@ResponseBody
	public Object getList(PageInfo pageinfo,ForgingSize size){
		
		return productProcessService.queryList(pageinfo, size);
	}
	
	
	@RequestMapping("/delete")
	@ResponseBody
	public Object delete(Long id){
		forgingSizeDao.delete(id);
		return "success";
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public Object update(ForgingSize size){
		return forgingSizeDao.save(size);
	}
	
	@RequestMapping("/importexcel")
	public String importexcel(){
		
		return "main/productProcess/import";
	}
	
	
	@RequestMapping("/doExcelImport")
	@ResponseBody
    public String doExcelImport(String excelFile,HttpServletRequest request,Integer sheetIndex,Integer serialNum,Integer forgingNum,Integer currentNum) throws IOException{
		String realPath = request.getSession().getServletContext().getRealPath("/files/"); 
		if(excelFile==null||("").equals(excelFile)){
			return "fileNotUpload";
		}
		Workbook wb = ExcelUtil.writeExcel("xlsx", realPath+"/"+excelFile);
		File ff = new File(realPath+"/"+excelFile);
		if(!ff.exists()){
			return "fileNotExist";
		}
		Sheet sheet = wb.getSheetAt(sheetIndex);
		if(sheet == null){
			return "sheetNotExists";
		}
		int rowNum=sheet.getLastRowNum();
		SimpleDateFormat simple = new SimpleDateFormat("yy-MM-dd-HH-mm-ss");
		StringBuffer b1 = new StringBuffer();
		String now = simple.format(new Date());
			for(int x = currentNum-1;x<=rowNum;x++){
				Row row = sheet.getRow(x);
				if(row==null){
					break;
				}
				ForgingSize forging = new ForgingSize();
				if(row.getCell(serialNum-1)!=null){
					try {
						forging.setOptime(now);
						row.getCell(serialNum-1).setCellType(Cell.CELL_TYPE_STRING);
						forging.setForgingNum(row.getCell(serialNum-1).getStringCellValue());
						try {
							row.getCell(forgingNum-1).setCellType(Cell.CELL_TYPE_STRING);
							forging.setForgingSize(row.getCell(forgingNum-1).getStringCellValue());
						} catch (Exception e) {
							forging.setForgingSize("");
						}
						List<ForgingSize> listf = forgingSizeDao.findByForgingNum(row.getCell(serialNum-1).getStringCellValue());
						if(null!=listf&&listf.size()>0){
							forging.setId(listf.get(0).getId());
						}
						forgingSizeDao.save(forging);
						if(null != forging.getForgingNum()&&!("").equals(forging.getForgingNum())){
							List<Product> listp = productJPADao.findBySerialNum(forging.getForgingNum());
							for(Product pro : listp){
								pro.setForgingSize(forging.getForgingSize());
								productJPADao.save(pro);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						b1.append(x).append("/");
					}
				}else{
					b1.append(x).append("/");
				}
				
			}
			System.out.println(b1);
		return "success";
	}
}
