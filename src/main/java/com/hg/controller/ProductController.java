package com.hg.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;















import com.hg.constant.AttendanceDay;
import com.hg.constant.ProductChn;
import com.hg.dao.MachiningRecordDao;
import com.hg.dao.OperateRecordDao;
import com.hg.dao.ProcessTempDao;
import com.hg.dao.ProductDao;
import com.hg.dao.ProductJPADao;
import com.hg.dao.SysProcessDao;
import com.hg.dao.TempSonDao;
import com.hg.dao.UserDao;
import com.hg.dao.ncr.NcrDao;
import com.hg.dao.ncr.NcrSonDao;
import com.hg.dao.productProcess.ForgingSizeDao;
import com.hg.model.MachiningRecord;
import com.hg.model.Ncr;
import com.hg.model.NcrSon;
import com.hg.model.OperateRecord;
import com.hg.model.ProcessTemp;
import com.hg.model.ProcessTempSon;
import com.hg.model.Product;
import com.hg.model.ProductTj;
import com.hg.model.SysProcess;
import com.hg.model.User;
import com.hg.model.productProcess.ForgingSize;
import com.hg.service.ProductService;
import com.hg.service.ReportService;
import com.hg.util.DateUtil;
import com.hg.util.ExcelUtil;
import com.hg.util.ItextUtil;
import com.hg.util.OtherUtil;
import com.hg.util.PageInfo;
import com.hg.util.PageResponse;
import com.hg.util.StringUtil;
import com.itextpdf.text.DocumentException;


@Controller
@RequestMapping("product")
public class ProductController {
	
	@Inject
	private ProductDao productDao;
	@Inject
	private ProductJPADao productJPADao;
	@Inject
	private TempSonDao tempSonDao;
	@Inject
	private OperateRecordDao operateRecordDao;
	@PersistenceContext
	private EntityManager entityManager;
	@Inject
	private ProductService productService;
    @Inject
    private UserDao userDao;
    @Inject
    private SysProcessDao sysProcessDao;
    @Inject
    private ProcessTempDao processTempDao;
	@Inject
	private MachiningRecordDao machiningRecordDao;
	@Inject
	private ForgingSizeDao forgingSizeDao;
	@Inject
	private NcrDao ncrDao;
	@Inject
	private NcrSonDao ncrSonDao;
	//生产信息导入页面
	@RequestMapping("/list")
	public String list(){
		return "main/product/list";
	}
	
	@RequestMapping("/getList")
	@ResponseBody
	public Object getList(PageInfo page,Product bean){
		Integer pageNum = page.getPage()-1;
		StringBuffer where = new StringBuffer();
		where.append("a.state = "+bean.getState()+"");
		if(null != bean){
			if(null != bean.getSerialNum()&&!("").equals(bean.getSerialNum())){
				where.append(" and a.serial_num like '%"+bean.getSerialNum()+"%'");
			}
            if(null != bean.getMaterial()&&!("").equals(bean.getMaterial())){
            	where.append(" and a.material like '%"+bean.getMaterial()+"%'");
			}
		}
		String sql = "select "+getColumns()+" from product a left join process_temp_son b on a.temp_son_id=b.id left join process_temp c on b.parent_id=c.id where "+where+" order by a.code,a.ordera,a.orderb,a.orderd,a.orderc,a.ordere limit "+(pageNum*page.getRows())+","+page.getRows()+"";
		List<Map<String, Object>> list =  productDao.getSqlQuery(sql);
		PageResponse res = new PageResponse();
		String sql2 = "select count(*) from product a left join process_temp_son b on a.temp_son_id=b.id left join process_temp c on b.parent_id=c.id where "+where+"";
		res.setRows(list);
		res.setTotal(productDao.getCount(sql2));
		return res;
	}
	
	public String getColumns(){
		return "a.product_num productNum,a.code,a.po_num poNum,a.po_date poDate,a.po_update_date poUpdateDate,"
			 + "a.material,a.grade,a.serial_num serialNum,a.order_num orderNum,a.sizea sizeA,"
			 + "a.sizeb sizeB,a.sizec sizeC,a.sized sizeD,a.sizee sizeE,a.sizef sizeF,a.sizeg sizeG,a.sizeh sizeH,"
			 + "a.po_qty poQty,a.blank_weight blankWeight,a.qty,a.description,a.product_qty productQty,a.quality_require qualityRequire,"
			 + "a.ndt_require ndtRequire,a.delivery_state deliveryState,a.ht_require htRequire,a.ht_requireb htRequireB,a.hdns_require hdnsRequire,"
			 + "a.next_process nextProcess,a.other_require otherRequire,a.forging_equip forgingEquip,a.material_num materialNum,a.dwg_num dwgNum,"
			 + "a.remarksa remarksA,a.remarksb remarksB,c.temp_name tempName,a.id,a.temp_son_id tempSonId,a.state,a.ordera,a.orderb,a.orderc,a.orderd,a.isback ";
	}
	
	//滚动表（可操作数据）
	@RequestMapping("/manageList")
	public String manageList(){
		
		return "main/product/manageList";
	}
	//滚动表(可操作数据)
	@RequestMapping("/getManageList")
	@ResponseBody
	public Object getManageList(PageInfo page,Product bean,Long processIdB,String startDateB,String endDateB,String poDateB,String forgingWorker){
		Integer pageNum = page.getPage()-1;
		StringBuffer where = new StringBuffer();
		if(null != bean.getState()){
        	where.append("a.state = "+bean.getState()+"");
		}else{
			where.append("a.state in (3,4,5,6,7,8,10,11,12,13,17,18,19,20,21,22)");
		}
		if(null != bean){
			if(null != bean.getSerialNum()&&!("").equals(bean.getSerialNum())){
				where.append(" and a.serial_num like '%"+bean.getSerialNum().trim()+"%'");
			}
            if(null != bean.getCode()&&!("").equals(bean.getCode())){
            	where.append(" and a.code like '%"+bean.getCode().trim()+"%'");
			}
            if(null != bean.getInitial()&&!("").equals(bean.getInitial())){
            	where.append(" and a.serial_num like '"+bean.getInitial().trim()+"%'");
			}
            if(null != bean.getBlankRemark()&&!("").equals(bean.getBlankRemark())){
            	where.append(" and a.blank_remark like '%"+bean.getBlankRemark().trim()+"%'");
			}
            if(null != bean.getOrderNum()&&!("").equals(bean.getOrderNum())){
            	where.append(" and a.order_num = '"+bean.getOrderNum().trim()+"'");
			}
            if((null != bean.getPoDate()&&!("").equals(bean.getPoDate()))&&(null == poDateB||("").equals(poDateB))){
            	where.append(" and a.po_date like '%"+bean.getPoDate().substring(2)+"%'");
			}else{
				if((null != bean.getPoDate()&&!("").equals(bean.getPoDate()))&&null != poDateB&&!("").equals(poDateB)){
					where.append(" and a.po_date >= '"+bean.getPoDate().substring(2)+"' and a.po_date<= '"+poDateB.substring(2)+"'");
				}
			}
            if(null != bean.getMaterial()&&!("").equals(bean.getMaterial())){
            	where.append(" and a.material like '%"+bean.getMaterial()+"%'");
			}
            if(null != processIdB){
    			where.append(" and c.id = "+processIdB+" and a.state != 6");
            }
		}
		if(null != bean.getProcessId()){
			productService.getDateColumn(bean.getProcessId(), bean.getStartDate(), bean.getEndDate(),where,startDateB,endDateB,forgingWorker);
		}
		PageResponse res = new PageResponse();
		String sql = "select a.*,c.chn_name from product a left join process_temp_son b on a.temp_son_id=b.id left join sys_process c on b.process_id=c.id where "+where+" order by a.code,a.ordera,a.orderb,a.orderd,a.orderc,a.ordere limit "+(pageNum*page.getRows())+","+page.getRows()+"";
		List<Map<String, Object>> list =  productDao.getSqlQuery(sql);
		res.setRows(list);
		Object total = productDao.getCount("select count(*) from product a left join process_temp_son b on a.temp_son_id=b.id left join sys_process c on b.process_id=c.id where "+where+"");
		res.setTotal(total);
		return res;
	}
	
	@RequestMapping("/cjmanageList")
	public String cjmanageList(){
		
		return "main/product/manageList_chejian";
	}
	
	@RequestMapping("/dispatchList")
	public String dispatchList(Integer type,Model model){
		return "main/dispatch/list"+type;
	}
	
	@RequestMapping("/getDispatchList")
	@ResponseBody
	public Object getDispatchList(PageInfo page,Product bean){
		Integer pageNum = page.getPage()-1;
		StringBuffer where = new StringBuffer();
		where.append("a.state in (11,13)");
		if(null != bean){
			if(null != bean.getSerialNum()&&!("").equals(bean.getSerialNum())){
				where.append(" and a.serial_num like '%"+bean.getSerialNum().trim()+"%'");
			}
            if(null != bean.getTempSonId()){
            	where.append(" and c.id = "+bean.getTempSonId()+"");
			}
		}
		PageResponse res = new PageResponse();
		String sql = "select a.*,c.chn_name from product a join process_temp_son b on a.temp_son_id=b.id join sys_process c on b.process_id=c.id where "+where+" order by a.code,a.ordera,a.orderb,a.orderd,a.orderc,a.ordere limit "+(pageNum*page.getRows())+","+page.getRows()+"";
		List<Map<String, Object>> list =  productDao.getSqlQuery(sql);
		res.setRows(list);
		Object total = productDao.getCount("select count(*) from product a join process_temp_son b on a.temp_son_id=b.id join sys_process c on b.process_id=c.id where "+where+"");
		res.setTotal(total);
		return res;
	}
	
	@RequestMapping("/deleteProduct")
	@ResponseBody
	public String deleteProduct(@RequestParam(value = "ids", required = false) List<Long> ids){
		for(Long id : ids){
			productJPADao.delete(id);
		}
		return "success";
	}
	
	@RequestMapping("/productList")
	public String productList(Integer type,Model model){
		return "main/product/list"+type;
	}
	
	@RequestMapping("/getProductList")
	@ResponseBody
	public Object getProductList(PageInfo page,Product bean,Integer pt,String hdj,String sn){
		Integer pageNum = page.getPage()-1;
		StringBuffer where = new StringBuffer();
		if(null != pt){
			if(pt.intValue()==1){
				where.append("a.state = 17");
			}
			if(pt.intValue()==2){
				where.append("a.state = 19");
			}
		}else{
			where.append("a.state = 3");
		}
		if(null != bean){
			if(null != bean.getSerialNum()&&!("").equals(bean.getSerialNum())){
				where.append(" and a.serial_num like '%"+bean.getSerialNum().trim()+"%'");
			}
			if(null != bean.getInitial()&&!("").equals(bean.getInitial())){
            	where.append(" and a.serial_num like '"+bean.getInitial().trim()+"%'");
			}
            if(null != bean.getBlankDateA()&&!("").equals(bean.getBlankDateA())){
            	where.append(" and a.blank_datea = '"+bean.getBlankDateA().trim()+"'");
			}
            if(null != bean.getBlankDate()&&!("").equals(bean.getBlankDate())){
            	where.append(" and a.blank_date = '"+bean.getBlankDate().trim()+"'");
			}
            if(null != bean.getHeatNum()&&!("").equals(bean.getHeatNum())){
            	where.append(" and a.heat_num like '%"+bean.getHeatNum().trim()+"%'");
			}
            if(null != bean.getForgingInDateA()&&!("").equals(bean.getForgingInDateA())){
            	where.append(" and a.forging_in_datea = '"+bean.getForgingInDateA().trim()+"'");
			}
            if(null != bean.getForgingInDate()&&!("").equals(bean.getForgingInDate())){
            	where.append(" and a.forging_in_date = '"+bean.getForgingInDate().trim()+"'");
			}
            if(null != bean.getForgingWorkerA()&&!("").equals(bean.getForgingWorkerA())){
            	where.append(" and a.forging_workera like '%"+bean.getForgingWorkerA().trim()+"%'");
			}
            if(null != bean.getForgingWorker()&&!("").equals(bean.getForgingWorker())){
            	where.append(" and a.forging_worker like '%"+bean.getForgingWorker().trim()+"%'");
			}
            if(null != bean.getHeatTreatInDateA()&&!("").equals(bean.getHeatTreatInDateA())){
            	where.append(" and a.heat_treat_in_datea = '"+bean.getHeatTreatInDateA().trim()+"'");
			}
            if(null != bean.getAfterForgingInDate()&&!("").equals(bean.getAfterForgingInDate())){
            	where.append(" and a.after_forging_in_date = '"+bean.getAfterForgingInDate().trim()+"'");
			}
            if(null != bean.getAfterForgingWorker()&&!("").equals(bean.getAfterForgingWorker())){
            	where.append(" and a.after_forging_worker like '%"+bean.getAfterForgingWorker().trim()+"%'");
			}
            if(null != bean.getForgingRemark()&&!("").equals(bean.getForgingRemark())){
            	where.append(" and a.forging_remark like '%"+bean.getForgingRemark().trim()+"%'");
			}
            if(null != bean.getSampleNum()&&!("").equals(bean.getSampleNum())){
            	where.append(" and a.sample_num like '%"+bean.getSampleNum().trim()+"%'");
			}
            if(null != bean.getRoughMachingInDate()&&!("").equals(bean.getRoughMachingInDate())){
            	where.append(" and a.rough_maching_in_date like '%"+bean.getRoughMachingInDate().trim()+"%'");
			}
            if(null != bean.getDeliveryInDate()&&!("").equals(bean.getDeliveryInDate())){
            	where.append(" and a.delivery_in_date like '%"+bean.getDeliveryInDate().trim()+"%'");
			} 
            if(null != bean.getHalfFinishMachingInDate()&&!("").equals(bean.getHalfFinishMachingInDate())){
            	where.append(" and a.half_finish_maching_in_date like '%"+bean.getHalfFinishMachingInDate().trim()+"%'");
			}
            if(null != bean.getFinishMachingInDate()&&!("").equals(bean.getFinishMachingInDate())){
            	where.append(" and a.finish_maching_in_date like '%"+bean.getFinishMachingInDate().trim()+"%'");
			}
            if(null != bean.getPoDate()&&!("").equals(bean.getPoDate())){
            	where.append(" and a.po_date like '%"+bean.getPoDate().trim()+"%'");
			}
            if(null != bean.getMaterial()&&!("").equals(bean.getMaterial())){
            	where.append(" and a.material like '%"+bean.getMaterial().trim()+"%'");
			}
            if(null != bean.getPerHeatTreatInDate()&&!("").equals(bean.getPerHeatTreatInDate())){
            	where.append(" and a.per_heat_treat_in_date like '%"+bean.getPerHeatTreatInDate().trim()+"%'");
			}
            if(null != bean.getTempSonId()){
            	where.append(" and c.id = "+bean.getTempSonId()+"");
			}
		}
		if(hdj!=null&&!("").equals(hdj)&&sn!=null){
			String orders = hdj.replaceAll("\\.", ",").replaceAll("，", ",").trim();
			String[] str = orders.split(",");
			where.append(" and (");
			int index = sn.lastIndexOf("-");
			String snum = sn.substring(0, index);
			for(int x = 0;x<str.length;x++){
				if(x==0){
					where.append("a.serial_num = '"+snum+"-"+str[x]+"'");
				}else{
					where.append("or a.serial_num = '"+snum+"-"+str[x]+"'");
				}
			}
			where.append(")");
		}
		PageResponse res = new PageResponse();
		String order  = "a.code,a.ordera,a.orderb,a.orderd,a.orderc,a.ordere";
		if(page.getOrder()!=null&&!("").equals(page.getOrder())){
			order = "a."+page.getSort()+" "+page.getOrder();
		}else{
			if(bean.getTempSonId().intValue()==5){
				if(null != pt){
					if(pt.intValue()==1){
						order = "a.forging_equip,a.ordera,a.orderb,a.serial_num,a.orderc";
					}
				}else{
					order = "a.forging_equip,a.po_date,a.ordera,a.orderb,a.serial_num,a.orderc";
				}
			}
			if(bean.getTempSonId().intValue()==6){
				order = "a.forging_out_date,a.material";
			}
		}
		String sql = "select a.*,c.chn_name,(a.qty*a.blank_weight) as totalWeight from product a join process_temp_son b on a.temp_son_id=b.id join sys_process c on b.process_id=c.id where "+where+" order by "+order+" limit "+(pageNum*page.getRows())+","+page.getRows()+"";
		List<Map<String, Object>> list =  productDao.getSqlQuery(sql);
		res.setRows(list);
		Object total = productDao.getCount("select count(*) from product a join process_temp_son b on a.temp_son_id=b.id join sys_process c on b.process_id=c.id where "+where+"");
		res.setTotal(total);
		return res;
	}
	
	//获取取样工序信息
	@RequestMapping("/getSampleList")
	@ResponseBody
	public Object getSampleList(PageInfo page,Product bean,Integer qtype){
		Integer pageNum = page.getPage()-1;
		StringBuffer where = new StringBuffer();
		if(null != qtype&&qtype.intValue()==13){
			where.append("a.state = 12");
		}else{
			where.append("a.state = 3 ");
		}
		if(null != bean){
			if(null != bean.getSerialNum()&&!("").equals(bean.getSerialNum())){
				where.append(" and a.serial_num like '%"+bean.getSerialNum().trim()+"%'");
			}
            if(null != bean.getBlankDateA()&&!("").equals(bean.getBlankDateA())){
            	where.append(" and a.blank_datea = '"+bean.getBlankDateA().trim()+"'");
			}
            if(null != bean.getBlankDate()&&!("").equals(bean.getBlankDate())){
            	where.append(" and a.blank_date = '"+bean.getBlankDate().trim()+"'");
			}
            if(null != bean.getHeatNum()&&!("").equals(bean.getHeatNum())){
            	where.append(" and a.heat_num like '%"+bean.getHeatNum().trim()+"%'");
			}
            if(null != bean.getForgingInDateA()&&!("").equals(bean.getForgingInDateA())){
            	where.append(" and a.forging_in_datea = '"+bean.getForgingInDateA().trim()+"'");
			}
            if(null != bean.getForgingInDate()&&!("").equals(bean.getForgingInDate())){
            	where.append(" and a.forging_in_date = '"+bean.getForgingInDate().trim()+"'");
			}
            if(null != bean.getForgingWorkerA()&&!("").equals(bean.getForgingWorkerA())){
            	where.append(" and a.forging_workera like '%"+bean.getForgingWorkerA().trim()+"%'");
			}
            if(null != bean.getForgingWorker()&&!("").equals(bean.getForgingWorker())){
            	where.append(" and a.forging_worker like '%"+bean.getForgingWorker().trim()+"%'");
			}
            if(null != bean.getHeatTreatInDateA()&&!("").equals(bean.getHeatTreatInDateA())){
            	where.append(" and a.heat_treat_in_datea = '"+bean.getHeatTreatInDateA().trim()+"'");
			}
            if(null != bean.getAfterForgingInDate()&&!("").equals(bean.getAfterForgingInDate())){
            	where.append(" and a.after_forging_in_date = '"+bean.getAfterForgingInDate().trim()+"'");
			}
            if(null != bean.getAfterForgingWorker()&&!("").equals(bean.getAfterForgingWorker())){
            	where.append(" and a.after_forging_worker like '%"+bean.getAfterForgingWorker().trim()+"%'");
			}
            if(null != bean.getTempSonId()){
            	where.append(" and c.id = "+bean.getTempSonId()+"");
			}
		}
		PageResponse res = new PageResponse();
		String sql = "select a.*,c.chn_name from product a join process_temp_son b on a.temp_son_id=b.id join sys_process c on b.process_id=c.id where "+where+" order by a.code,a.ordera,a.orderb,a.orderd,a.orderc,a.ordere limit "+(pageNum*page.getRows())+","+page.getRows()+"";
		List<Map<String, Object>> list =  productDao.getSqlQuery(sql);
		res.setRows(list);
		Object total = productDao.getCount("select count(*) from product a join process_temp_son b on a.temp_son_id=b.id join sys_process c on b.process_id=c.id where "+where+"");
		res.setTotal(total);
		return res;
	}
	
	@RequestMapping("/outsideList")
	public String outsideList(Integer type,Model model){
		return "main/outside/list"+type;
	}
	
	@RequestMapping("/getOutsideList")
	@ResponseBody
	public Object getOutsideList(PageInfo page,Product bean,Integer pt){
		Integer pageNum = page.getPage()-1;
		StringBuffer where = new StringBuffer();
		if(null != pt){
			if(pt.intValue()==1){
				where.append("a.state = 18");
			}
		}else{
			where.append("a.state = 10");
		}
		if(null != bean){
			if(null != bean.getSerialNum()&&!("").equals(bean.getSerialNum())){
				where.append(" and a.serial_num like '%"+bean.getSerialNum().trim()+"%'");
			}
			if(null != bean.getBlankDateA()&&!("").equals(bean.getBlankDateA())){
            	where.append(" and a.blank_datea = '"+bean.getBlankDateA().trim()+"'");
			}
            if(null != bean.getBlankDate()&&!("").equals(bean.getBlankDate())){
            	where.append(" and a.blank_date = '"+bean.getBlankDate().trim()+"'");
			}
            if(null != bean.getHeatNum()&&!("").equals(bean.getHeatNum())){
            	where.append(" and a.heat_num like '%"+bean.getHeatNum().trim()+"%'");
			}
            if(null != bean.getForgingInDateA()&&!("").equals(bean.getForgingInDateA())){
            	where.append(" and a.forging_in_datea = '"+bean.getForgingInDateA().trim()+"'");
			}
            if(null != bean.getForgingInDate()&&!("").equals(bean.getForgingInDate())){
            	where.append(" and a.forging_in_date = '"+bean.getForgingInDate().trim()+"'");
			}
            if(null != bean.getForgingWorkerA()&&!("").equals(bean.getForgingWorkerA())){
            	where.append(" and a.forging_workera like '%"+bean.getForgingWorkerA().trim()+"%'");
			}
            if(null != bean.getForgingWorker()&&!("").equals(bean.getForgingWorker())){
            	where.append(" and a.forging_worker like '%"+bean.getForgingWorker().trim()+"%'");
			}
            if(null != bean.getHeatTreatInDateA()&&!("").equals(bean.getHeatTreatInDateA())){
            	where.append(" and a.heat_treat_in_datea = '"+bean.getHeatTreatInDateA().trim()+"'");
			}
            if(null != bean.getAfterForgingInDate()&&!("").equals(bean.getAfterForgingInDate())){
            	where.append(" and a.after_forging_in_date = '"+bean.getAfterForgingInDate().trim()+"'");
			}
            if(null != bean.getAfterForgingWorker()&&!("").equals(bean.getAfterForgingWorker())){
            	where.append(" and a.after_forging_worker like '%"+bean.getAfterForgingWorker().trim()+"%'");
			}
            if(null != bean.getPerHeatTreatWorker()&&!("").equals(bean.getPerHeatTreatWorker())){
            	where.append(" and a.per_heat_treat_worker like '%"+bean.getPerHeatTreatWorker().trim()+"%'");
			}
            if(null != bean.getForgingRemark()&!("").equals(bean.getForgingRemark())){
            	where.append(" and a.forging_remark like '%"+bean.getForgingRemark().trim()+"%'");
			}
            if(null != bean.getAfterForgingOut()&&!("").equals(bean.getAfterForgingOut())){
            	where.append(" and a.after_forging_out like '%"+bean.getAfterForgingOut().trim()+"%'");
			}
            if(null != bean.getProcessRemark()&&!("").equals(bean.getProcessRemark())){
            	where.append(" and a.process_remark like '%"+bean.getProcessRemark().trim()+"%'");
			}
            if(null != bean.getRoughMachingOutCom()&&!("").equals(bean.getRoughMachingOutCom())){
            	if(("*").equals(bean.getRoughMachingOutCom())){
            		where.append(" and (a.rough_maching_out_com is null or a.rough_maching_out_com='')");
            	}else{
            		where.append(" and a.rough_maching_out_com like '%"+bean.getRoughMachingOutCom().trim()+"%'");
            	}
            }
            if(null != bean.getDeliveryOutCom()&&!("").equals(bean.getDeliveryOutCom())){
            		if(("*").equals(bean.getDeliveryOutCom())){
                		where.append(" and (a.delivery_out_com is null or a.delivery_out_com='')");
                	}else{
                		where.append(" and a.delivery_out_com like '%"+bean.getDeliveryOutCom().trim()+"%'");
                	}
			}
            if(null != bean.getHalfFinishOutCom()&&!("").equals(bean.getHalfFinishOutCom())){
            	if(("*").equals(bean.getHalfFinishOutCom())){
            		where.append(" and (a.half_finish_out_com is null or a.half_finish_out_com='')");
            	}else{
            		where.append(" and a.half_finish_out_com like '%"+bean.getHalfFinishOutCom().trim()+"%'");
            	}
			}
            if(null != bean.getFinishMachingOutCom()&&!("").equals(bean.getFinishMachingOutCom())){
            	if(("*").equals(bean.getFinishMachingOutCom())){
            		where.append(" and (a.finish_maching_out_com is null or a.finish_maching_out_com='')");
            	}else{
            		where.append(" and a.finish_maching_out_com like '%"+bean.getFinishMachingOutCom().trim()+"%'");
            	}
			}
            if(null != bean.getTempSonId()){
            	where.append(" and c.id = "+bean.getTempSonId()+"");
			}
		}
		PageResponse res = new PageResponse();
		String order ="order by a.code,a.ordera,a.orderb,a.orderd,a.orderc,a.ordere";
		if(page.getOrder()!=null&&!("").equals(page.getOrder())){
			order = "order by a."+page.getSort()+" "+page.getOrder();
		}
		String sql = "select a.*,c.chn_name from product a join process_temp_son b on a.temp_son_id=b.id join sys_process c on b.process_id=c.id where "+where+" "+order+" limit "+(pageNum*page.getRows())+","+page.getRows()+"";
		List<Map<String, Object>> list =  productDao.getSqlQuery(sql);
		res.setRows(list);
		Object total = productDao.getCount("select count(*) from product a join process_temp_son b on a.temp_son_id=b.id join sys_process c on b.process_id=c.id where "+where+"");
		res.setTotal(total);
		return res;
	}
	
	@RequestMapping("/inspectList")
	public String inspectList(Integer type,Model model){
		return "main/inspect/list"+type;
	}
	
	@RequestMapping("/getInspectList")
	@ResponseBody
	public Object getInspectList(PageInfo page,Product bean){
		Integer pageNum = page.getPage()-1;
		StringBuffer where = new StringBuffer();
		where.append("a.state in (4,20)");
		if(null != bean){
			if(null != bean.getSerialNum()&&!("").equals(bean.getSerialNum())){
				where.append(" and a.serial_num like '%"+bean.getSerialNum()+"%'");
			}
			if(null != bean.getBlankDateA()&&!("").equals(bean.getBlankDateA())){
            	where.append(" and a.blank_datea = '"+bean.getBlankDateA().trim()+"'");
			}
            if(null != bean.getBlankDate()&&!("").equals(bean.getBlankDate())){
            	where.append(" and a.blank_date = '"+bean.getBlankDate().trim()+"'");
			}
            if(null != bean.getHeatNum()&&!("").equals(bean.getHeatNum())){
            	where.append(" and a.heat_num like '%"+bean.getHeatNum().trim()+"%'");
			}
            if(null != bean.getForgingInDateA()&&!("").equals(bean.getForgingInDateA())){
            	where.append(" and a.forging_in_datea = '"+bean.getForgingInDateA().trim()+"'");
			}
            if(null != bean.getForgingInDate()&&!("").equals(bean.getForgingInDate())){
            	where.append(" and a.forging_in_date = '"+bean.getForgingInDate().trim()+"'");
			}
            if(null != bean.getForgingWorkerA()&&!("").equals(bean.getForgingWorkerA())){
            	where.append(" and a.forging_workera like '%"+bean.getForgingWorkerA().trim()+"%'");
			}
            if(null != bean.getForgingWorker()&&!("").equals(bean.getForgingWorker())){
            	where.append(" and a.forging_worker like '%"+bean.getForgingWorker().trim()+"%'");
			}
            if(null != bean.getHeatTreatInDateA()&&!("").equals(bean.getHeatTreatInDateA())){
            	where.append(" and a.heat_treat_in_datea = '"+bean.getHeatTreatInDateA().trim()+"'");
			}
            if(null != bean.getAfterForgingInDate()&&!("").equals(bean.getAfterForgingInDate())){
            	where.append(" and a.after_forging_in_date = '"+bean.getAfterForgingInDate().trim()+"'");
			}
            if(null != bean.getAfterForgingWorker()&&!("").equals(bean.getAfterForgingWorker())){
            	where.append(" and a.after_forging_worker like '%"+bean.getAfterForgingWorker().trim()+"%'");
			}
            if(null != bean.getForgingRemark()&&!("").equals(bean.getForgingRemark())){
            	where.append(" and a.forging_remark like '%"+bean.getForgingRemark().trim()+"%'");
			}
            if(null != bean.getProcessRemark()&&!("").equals(bean.getProcessRemark())){
            	where.append(" and a.process_remark like '%"+bean.getProcessRemark().trim()+"%'");
			}
            if(null != bean.getInspectTime()&&!("").equals(bean.getInspectTime())){
            	where.append(" and a.inspect_time like '%"+bean.getInspectTime().trim()+"%'");
			}
            if(null != bean.getTempSonId()){
            	where.append(" and c.id = "+bean.getTempSonId()+"");
			}
		}
		String order = "order by a.state desc,a.code,a.ordera,a.orderb,a.orderd,a.orderc,a.ordere";
		if(null != page.getOrder()&&!("").equals(page.getOrder())){
			order = "order by a."+page.getSort()+" "+page.getOrder();
		}
		PageResponse res = new PageResponse();
		String sql = "select a.*,c.chn_name from product a join process_temp_son b on a.temp_son_id=b.id join sys_process c on b.process_id=c.id where "+where+" "+order+" limit "+(pageNum*page.getRows())+","+page.getRows()+"";
		List<Map<String, Object>> list =  productDao.getSqlQuery(sql);
		res.setRows(list);
		Object total = productDao.getCount("select count(*) from product a join process_temp_son b on a.temp_son_id=b.id join sys_process c on b.process_id=c.id where "+where+"");
		res.setTotal(total);
		return res;
	}
	
	//总表
	@RequestMapping("/allList")
	public String allList(){
		
		return "main/outside/allList";
	}
	//滚动表(可操作数据)
	@RequestMapping("/getAllList")
	@ResponseBody
	public Object getAllList(PageInfo page,Product bean,String outCom,String remark){
		Integer pageNum = page.getPage()-1;
		StringBuffer where = new StringBuffer();
		where.append("a.state in (3,4,5,6,7,8,9,10,11,12,13,15)");
		if(null != bean){
			if(null != bean.getSerialNum()&&!("").equals(bean.getSerialNum())){
				where.append(" and a.serial_num like '%"+bean.getSerialNum().trim()+"%'");
			}
            if(null != bean.getCode()&&!("").equals(bean.getCode())){
            	where.append(" and a.code like '%"+bean.getCode().trim()+"%'");
			}
            if(null != bean.getOrderNum()&&!("").equals(bean.getOrderNum())){
            	where.append(" and a.order_num = '"+bean.getOrderNum().trim()+"'");
			}
            if(null != bean.getPoDate()&&!("").equals(bean.getPoDate())){
            	where.append(" and a.po_date like '%"+bean.getPoDate()+"%'");
			}
            if(null != bean.getMaterial()&&!("").equals(bean.getMaterial())){
            	where.append(" and a.material like '%"+bean.getMaterial()+"%'");
			}
		}
		if(null != bean.getProcessId()){
			productService.getDateColumn(bean.getProcessId(), bean.getStartDate(), bean.getEndDate(),where,null,null,null);
		}
		if(null != bean.getProcessId()){
			productService.getDateColumn2(bean.getProcessId(), remark, outCom, where);
		}
		PageResponse res = new PageResponse();
		String sql = "select a.*,c.chn_name from product a left join process_temp_son b on a.temp_son_id=b.id left join sys_process c on b.process_id=c.id where "+where+" order by a.code,a.ordera,a.orderb,a.orderd,a.orderc,a.ordere limit "+(pageNum*page.getRows())+","+page.getRows()+"";
		List<Map<String, Object>> list =  productDao.getSqlQuery(sql);
		res.setRows(list);
		Object total = productDao.getCount("select count(*) from product a left join process_temp_son b on a.temp_son_id=b.id left join sys_process c on b.process_id=c.id where "+where+"");
		res.setTotal(total);
		return res;
	}
	
	
	@RequestMapping("/examineList")
	public String examineList(){
		
		return "main/product/examineList";
	}
	
	
	@RequestMapping("/setTemp")
	public String setTemp(String ids,Model model){
		model.addAttribute("ids", ids);
		return "main/product/setTemp";
	}
	
	@RequestMapping("/doSetTemp")
	@ResponseBody
	public String doSetTemp(String ids,Long tempId){
		ProcessTempSon son = tempSonDao.findByParentIdAndOrderNum(tempId, 1);
		if(null != son){
			String[] str = ids.split(",");
			for(int x = 0;x<str.length;x++){
				Product p = productJPADao.findOne(Long.parseLong(str[x]));
				if(null != p){
					p.setTempSonId(son.getId());
					productJPADao.save(p);
				}
			}
		}else{
			return "fail";
		}
		return "success";
	}
	
	@RequestMapping("/checkProcess")
	public String checkProcess(String ids,Model model){
		model.addAttribute("ids", ids);
		return "main/product/checkProcess";
	}
	
	@RequestMapping("/doCheckProcess")
	@ResponseBody
	public String doCheckProcess(@RequestParam(value = "ids", required = false) List<Long> ids,Long tempSonId,HttpSession session,Long tempId,Integer state){
		
		List<Product> list = (List<Product>) productJPADao.findAll(ids);
		for(Product p : list){
			String userChnName = (String) session.getAttribute("userChnName");
			p.setTempSonId(tempSonId);
			p.setState(state);
			p.setIsback(2);
			productService.getProduct(p);
			productJPADao.save(p);
			ProcessTemp temp = processTempDao.findOne(tempId);
			OperateRecord operRecord = new OperateRecord();
	    	operRecord.setOperateTime(DateUtil.getCurrentTime());
	    	operRecord.setProductId(p.getId());
	    	operRecord.setUsername(userChnName);
	    	if(null != tempSonId){
	    		ProcessTempSon son = tempSonDao.findOne(tempSonId);
	    		if(null != son){
	    			SysProcess sys = sysProcessDao.findOne(son.getProcessId());
	    			if(null != sys){
	    				operRecord.setElementName("调整工艺路线为:"+temp.getTempName()+" 并且调整工序到"+sys.getChnName());
	    	    		operateRecordDao.save(operRecord);
	    			}
	    		}
	    	}
		}
		return "success";
	}
	
	@RequestMapping("/getTempSons")
	@ResponseBody
	public Object getTempSons(Long id){
		List<ProcessTempSon> son = tempSonDao.findByParentId(id);
		for(ProcessTempSon s : son){
			SysProcess proc = sysProcessDao.findOne(s.getProcessId());
			s.setProcName(proc.getChnName());
		}
		return son;
	}
	
	//做出坯
	@RequestMapping("/toCp")
	@ResponseBody
	public String toCp(@RequestParam(value = "ids", required = false) List<Long> ids,HttpSession session,Integer type){
		List<Product> list = (List<Product>) productJPADao.findAll(ids);
		String userChnName = (String) session.getAttribute("userChnName");
		for(Product p : list){
			if(type.intValue()==1){
				p.setIscp(1);
				productJPADao.save(p);
				OperateRecord operRecord = new OperateRecord();
		    	operRecord.setOperateTime(DateUtil.getCurrentTime());
		    	operRecord.setProductId(p.getId());
		    	operRecord.setUsername(userChnName);
		    	operRecord.setElementName("做出坯");
		    	operateRecordDao.save(operRecord);
			}else{
				p.setIscp(null);
				productJPADao.save(p);
				OperateRecord operRecord = new OperateRecord();
		    	operRecord.setOperateTime(DateUtil.getCurrentTime());
		    	operRecord.setProductId(p.getId());
		    	operRecord.setUsername(userChnName);
		    	operRecord.setElementName("取消出坯");
		    	operateRecordDao.save(operRecord);
			}
		}
		return "success";
	}
	
	//做合锻
	@RequestMapping("/toHd")
	@ResponseBody
	public String toHd(@RequestParam(value = "ids", required = false) List<Long> ids,HttpSession session){
		List<Product> list = (List<Product>) productJPADao.findAll(ids);
		String userChnName = (String) session.getAttribute("userChnName");
		for(Product p : list){
			ProcessTempSon son = new ProcessTempSon();
			if(p.getNextTempSonId()!=null){
				son = tempSonDao.findOne(p.getNextTempSonId());
			}else{
				son = tempSonDao.findOne(p.getTempSonId());
			}
			ProcessTempSon son2 = tempSonDao.findByParentIdAndOrderNum(son.getParentId(), 1);
			p.setTempSonId(son2.getId());
			p.setIshd(1);
			productJPADao.save(p);
			OperateRecord operRecord = new OperateRecord();
	    	operRecord.setOperateTime(DateUtil.getCurrentTime());
	    	operRecord.setProductId(p.getId());
	    	operRecord.setUsername(userChnName);
	    	operRecord.setElementName("做合锻");
	    	operateRecordDao.save(operRecord);
		}
		return "success";
	}
	
	//合锻后返回锻后热处理
	@RequestMapping("/toBack")
	@ResponseBody
	public String toBack(@RequestParam(value = "ids", required = false) List<Long> ids,HttpSession session){
		List<Product> list = (List<Product>) productJPADao.findAll(ids);
		for(Product p : list){
			if(null == p.getIshd()){
				return "fail";
			}
		}
		String userChnName = (String) session.getAttribute("userChnName");
		for(Product p : list){
			ProcessTempSon son = new ProcessTempSon();
			if(p.getNextTempSonId()!=null){
				son = tempSonDao.findOne(p.getNextTempSonId());
			}else{
				son = tempSonDao.findOne(p.getTempSonId());
			}
			ProcessTempSon son2 = tempSonDao.findByParentIdAndProcessId(son.getParentId(), 6L);
			p.setTempSonId(son2.getId());
			p.setIshd(null);
			productJPADao.save(p);
			OperateRecord operRecord = new OperateRecord();
	    	operRecord.setOperateTime(DateUtil.getCurrentTime());
	    	operRecord.setProductId(p.getId());
	    	operRecord.setUsername(userChnName);
	    	operRecord.setElementName("合锻后返回锻后热处理");
    		operateRecordDao.save(operRecord);
		}
		return "success";
	}
	
	//进入下一个节点
	@RequestMapping("/nextElement")
	@ResponseBody
	public String nextElement(@RequestParam(value = "ids", required = false) List<Long> ids,HttpSession session){
		List<Product> list = (List<Product>) productJPADao.findAll(ids);
		for(Product product : list){
			if(product.getTempSonId()==null){
				return "fail";
			}
		}
		for(Product product : list){
			product.setState(2);
			product = productService.getProduct(product);
			productJPADao.save(product);
			String userChnName = (String) session.getAttribute("userChnName");
			OperateRecord record = new OperateRecord();
			record.setElementName("计划单进入审核");
			record.setOperateTime(DateUtil.getCurrentTime());
			record.setProductId(product.getId());
			record.setUsername(userChnName);
			operateRecordDao.save(record);
		}
		return "success";
	}
	
	//外协
	@RequestMapping("/outside")
	@ResponseBody
	public String outside(@RequestParam(value = "ids", required = false) List<Long> ids,HttpSession session){
		List<Product> list = (List<Product>) productJPADao.findAll(ids);
		for(Product product : list){
			ProcessTempSon son = tempSonDao.findOne(product.getTempSonId());
			if(son.getProcessId().intValue()==5){
				product.setForgingWorker("*");
			}
            if(son.getProcessId().intValue()==6){
				product.setAfterForgingWorker("*");
			}
            if(son.getProcessId().intValue()==7){
				product.setRoughMachingWorker("*");
			}
            if(son.getProcessId().intValue()==8){
				product.setPerHeatTreatWorker("*");
			}
            if(son.getProcessId().intValue()==9){
				product.setDeliveryWorker("*");
			}
            if(son.getProcessId().intValue()==10){
				product.setFollowupWorker("*");
			}
            if(son.getProcessId().intValue()==11){
				product.setHalfFinishMachingWorker("*");
			}
            if(son.getProcessId().intValue()==12){
				product.setFinishMachingWorker("*");
			}
			product.setState(10);
			product.setOutSideInDate(DateUtil.getCurrentymd());
			productJPADao.save(product);
			ProcessTempSon ts = tempSonDao.findOne(product.getTempSonId());
			if(null != ts){
		      SysProcess sys = sysProcessDao.findOne(ts.getProcessId());
			  String userChnName = (String) session.getAttribute("userChnName");
			  OperateRecord record = new OperateRecord();
			  record.setElementName(sys.getChnName()+"进入外协");
			  record.setOperateTime(DateUtil.getCurrentTime());
			  record.setProductId(product.getId());
			  record.setUsername(userChnName);
			  operateRecordDao.save(record);
			}
		}
		return "success";
	}
	
	//外协
	@RequestMapping("/backWork")
	@ResponseBody
	public String backWork(@RequestParam(value = "ids", required = false) List<Long> ids,HttpSession session){
		List<Product> list = (List<Product>) productJPADao.findAll(ids);
		for(Product product : list){
			product.setState(3);
			product.setOpState(2);
			productJPADao.save(product);
			ProcessTempSon ts = tempSonDao.findOne(product.getTempSonId());
			if(null != ts){
		      SysProcess sys = sysProcessDao.findOne(ts.getProcessId());
			  String userChnName = (String) session.getAttribute("userChnName");
			  OperateRecord record = new OperateRecord();
			  record.setElementName(sys.getChnName()+"外协回退到本厂调度页面");
			  record.setOperateTime(DateUtil.getCurrentTime());
			  record.setProductId(product.getId());
			  record.setUsername(userChnName);
			  operateRecordDao.save(record);
			}
		}
		return "success";
	}
	
	//入库
	@RequestMapping("/inStore")
	@ResponseBody
	public String inStore(@RequestParam(value = "ids", required = false) List<Long> ids,HttpSession session){
		List<Product> list = (List<Product>) productJPADao.findAll(ids);
//		for(Product product : list){
//			if(product.getState().intValue()!=6){
//				return "fail";
//			}
//		}
		for(Product product : list){
			product.setState(9);
			product.setInStoreDate(DateUtil.getCurrentTime());
			productJPADao.save(product);
			  String userChnName = (String) session.getAttribute("userChnName");
			  OperateRecord record = new OperateRecord();
			  record.setElementName("入库");
			  record.setOperateTime(DateUtil.getCurrentTime());
			  record.setProductId(product.getId());
			  record.setUsername(userChnName);
			  operateRecordDao.save(record);
		}
		return "success";
	}
	
	//破坏
	@RequestMapping("/breakOut")
	@ResponseBody
	public String breakOut(@RequestParam(value = "ids", required = false) List<Long> ids,HttpSession session){
		List<Product> list = (List<Product>) productJPADao.findAll(ids);
		for(Product product : list){
			product.setState(16);
			productJPADao.save(product);
			String userChnName = (String) session.getAttribute("userChnName");
			OperateRecord record = new OperateRecord();
			record.setElementName("入试样库");
			record.setOperateTime(DateUtil.getCurrentTime());
			record.setProductId(product.getId());
			record.setUsername(userChnName);
			operateRecordDao.save(record);
		}
		return "success";
	}
	
	@RequestMapping("/setOutDate")
	public String setOutDate(String ids,Model model,Integer type){
		model.addAttribute("ids", ids);
		model.addAttribute("outDate", DateUtil.getCurrentymd());
		model.addAttribute("type", type);
		return "main/inspect/setOutDate";
	}
	
	@RequestMapping("/setForgingDate")
	public String setForgingDate(String ids,Model model){
		model.addAttribute("ids", ids);
		return "main/product/setForgingDate";
	}
	
	//进入下一个工序
	@RequestMapping("/nextProcess")
	@ResponseBody
	public String nextProcess(@RequestParam(value = "ids", required = false) List<Long> ids,Integer type,String forgingDate,HttpSession session,String forgingWork){
		List<Product> list = (List<Product>) productJPADao.findAll(ids);
		List<ProcessTempSon> listTempSon = tempSonDao.findByParentId(0L);
		String userChnName = (String) session.getAttribute("userChnName");
		if(null == listTempSon||listTempSon.size()==0){
			ProcessTempSon ps = new ProcessTempSon();
			ps.setOrderNum(0);
			ps.setParentId(0L);
			ps.setProcessId(6L);
			tempSonDao.save(ps);
			listTempSon = new ArrayList<ProcessTempSon>();
			listTempSon.add(ps);
		}
		if(type.intValue() == 1){//送检
			for(Product product : list){
				product.setState(4);
				product.setOpState(null);
				product.setInspectTime(DateUtil.getCurrentTime());
				productJPADao.save(product);
				ProcessTempSon son = tempSonDao.findOne(product.getTempSonId());
				if(null != son){
					SysProcess sys = sysProcessDao.findOne(son.getProcessId());
					OperateRecord record = new OperateRecord();
					if(product.getIscp()!=null && product.getIscp().intValue()==1){
						record.setElementName(sys.getChnName()+"出坯(送检)");
					}else{
						record.setElementName(sys.getChnName()+"(送检)");
					}
					record.setOperateTime(DateUtil.getCurrentTime());
					record.setProductId(product.getId());
					record.setUsername(userChnName);
					operateRecordDao.save(record);
					MachiningRecord mr = new MachiningRecord();
					
					mr.setOpTime(DateUtil.getCurrentTime());
					mr.setProcessName(sys.getChnName());
					mr.setOperator(userChnName);
					mr.setSerialNum(product.getSerialNum());
					mr.setOrderNum(product.getOrderNum());
					if(sys.getId().intValue()==7){
						mr.setCompany(product.getRoughMachingOutCom());
						mr.setContent(product.getRoughMachingOutRemark());
						machiningRecordDao.save(mr);
					}
					if(sys.getId().intValue()==9){
						mr.setCompany(product.getDeliveryOutCom());
						mr.setContent(product.getDeliveryOutRemark());
						machiningRecordDao.save(mr);
					}
					if(sys.getId().intValue()==11){
						mr.setCompany(product.getHalfFinishOutCom());
						mr.setContent(product.getHalfFinishOutRemark());
						machiningRecordDao.save(mr);
					}
					if(sys.getId().intValue()==12){
						mr.setCompany(product.getFinishMachingOutCom());
						mr.setContent(product.getFinishMachingOutRemark());
						machiningRecordDao.save(mr);
					}
				}
			}
		}else if(type.intValue() == 2){//进入下个工序或者下个工序的调度
			for(Product product : list){
				product.setOpState(null);
				product.setIsback(null);
				product.setProcessRemark(null);
				productService.processInit(product);
				ProcessTempSon ts = tempSonDao.findOne(product.getTempSonId());
				if(null != ts){
					SysProcess sys = sysProcessDao.findOne(ts.getProcessId());
					OperateRecord record = new OperateRecord();
					if(product.getIscp()!=null && product.getIscp().intValue()==1){
						record.setElementName(sys.getChnName()+"出坯(转出)");
					}else{
						record.setElementName(sys.getChnName()+"(转出)");
					}
					record.setOperateTime(DateUtil.getCurrentTime());
					record.setProductId(product.getId());
					record.setUsername(userChnName);
					operateRecordDao.save(record);
					if(null != product.getIscp()&&product.getIscp().intValue()==1){
						if(ts.getProcessId().intValue()==4){
							product.setPrevProcess("下料出坯");
						}
						if(ts.getProcessId().intValue()==5){
							product.setPrevProcess("锻造出坯");
						}
						if(ts.getProcessId().intValue()==6){
							product.setPrevProcess("热处理出坯");
						}
					}else{
						SysProcess sysp = sysProcessDao.findOne(ts.getProcessId());
						product.setPrevProcess(sysp.getChnName());
					}
				}
				product = productService.outDate(product);
				if(null != product.getNextTempSonId()){
					//热处理出坯结束后跳转逻辑,值为1的话跳转到下料，否则执行下个工序
					if(product.getIscp()!=null&&product.getIscp()==1){
						product.setState(3);
						product.setTempSonId(product.getNextTempSonId());
						product.setIscp(null);
						product.setNextTempSonId(null);
						productJPADao.save(product);
						continue;
					}
				}else{
					ProcessTempSon son = tempSonDao.findOne(product.getTempSonId());
					ProcessTempSon next = tempSonDao.findByParentIdAndOrderNum(son.getParentId(), son.getOrderNum()+1);
					ProcessTempSon first = tempSonDao.findByParentIdAndOrderNum(son.getParentId(), 1);
					if(son.getProcessId().intValue()==5&&(product.getIscp()!=null&&product.getIscp()==1)){
						product.setState(11);
						product.setTempSonId(listTempSon.get(0).getId());
						product.setNextTempSonId(first.getId());
						productJPADao.save(product);
					}else{
						if(null == next){
							product.setState(6);
							product.setLastDate(DateUtil.getCurrentymd());
						}else{
							if(next.getProcessId().intValue()>=6&&next.getProcessId().intValue()<=12){
								product.setState(11);
							}else{
								product.setState(3);
							}
							product.setTempSonId(next.getId());
						}
						productJPADao.save(product);
					}
				}
			}
		}else if(type.intValue() == 3){//从调度进入生产
			for(Product product : list){
				product.setState(3);
				product.setOpState(null);
				product.setIsback(null);
				product = productService.inDate(product);
				productJPADao.save(product);
				ProcessTempSon ts = tempSonDao.findOne(product.getTempSonId());
				if(null != ts){
					SysProcess sys = sysProcessDao.findOne(ts.getProcessId());
					OperateRecord record = new OperateRecord();
					if(product.getIscp()!=null && product.getIscp().intValue()==1){
						record.setElementName(sys.getChnName()+"出坯(转入)");
					}else{
						record.setElementName(sys.getChnName()+"(转入)");
					}
					record.setOperateTime(DateUtil.getCurrentTime());
					record.setProductId(product.getId());
					record.setUsername(userChnName);
					operateRecordDao.save(record);
				}
			}
		}else if(type.intValue() == 4){ //从锻后热处理转入下个工序
			for(Product product : list){
				if(null != product.getIscp()&&product.getIscp().intValue()==1){
					continue ;
				}
				if((null != product.getHdnsRequire()&&!("").equals(product.getHdnsRequire()))&&!("—").equals(product.getHdnsRequire())){
					if((null==product.getHtRequireB()||("").equals(product.getHtRequireB()))||(null != product.getHtRequireB()&&("—").equals(product.getHtRequireB()))){
						continue ;
					}
				}
				productService.processInit(product);
				OperateRecord record = new OperateRecord();
				record.setElementName("锻后热处理直接进入下个工序");
				record.setOperateTime(DateUtil.getCurrentTime());
				record.setProductId(product.getId());
				record.setUsername(userChnName);
				operateRecordDao.save(record);
				if(null != product.getIscp()&&product.getIscp().intValue()==1){
					product.setPrevProcess("热处理出坯");
				}else{
					product.setPrevProcess("锻后热处理");
				}
				product = productService.outDate(product);
				ProcessTempSon son = tempSonDao.findOne(product.getTempSonId());
				ProcessTempSon next = tempSonDao.findByParentIdAndOrderNum(son.getParentId(), son.getOrderNum()+1);
				if(null == next){
					product.setState(6);
				}else{
					if(next.getProcessId().intValue()>=6&&next.getProcessId().intValue()<=12){
						product.setState(11);
					}else{
						product.setState(3);
					}
					product.setTempSonId(next.getId());
				}
				productJPADao.save(product);
			}
		}else if(type.intValue() == 5){//从取样到车加工取样
			for(Product product : list){
				product.setState(12);
				product = productService.inDate(product);
				productJPADao.save(product);
				OperateRecord record = new OperateRecord();
				record.setElementName("取样转入车加工取样");
				record.setOperateTime(DateUtil.getCurrentTime());
				record.setProductId(product.getId());
				record.setUsername(userChnName);
				operateRecordDao.save(record);
			}
		}else if(type.intValue() == 6){//直接转入不合格
			for(Product product : list){
				product.setState(5);
				productJPADao.save(product);
				OperateRecord record = new OperateRecord();
				record.setElementName("从生产管理转入不合格");
				record.setOperateTime(DateUtil.getCurrentTime());
				record.setProductId(product.getId());
				record.setUsername(userChnName);
				operateRecordDao.save(record);
			}
		}else if(type.intValue() == 7){//暂停
			for(Product product : list){
				product.setState(13);
				productJPADao.save(product);
				OperateRecord record = new OperateRecord();
				record.setElementName("暂停");
				record.setOperateTime(DateUtil.getCurrentTime());
				record.setProductId(product.getId());
				record.setUsername(userChnName);
				operateRecordDao.save(record);
			}
		}else if(type.intValue() == 8){//转入外协
			for(Product product : list){
				product.setState(10);
				product.setIsback(1);
				product.setOpState(null);
				productJPADao.save(product);
				OperateRecord record = new OperateRecord();
				record.setElementName("从检验返回外协");
				record.setOperateTime(DateUtil.getCurrentTime());
				record.setProductId(product.getId());
				record.setUsername(userChnName);
				operateRecordDao.save(record);
			}
		}
		else if(type.intValue() == 9){//删除
			for(Product product : list){
				product.setState(14);
				productJPADao.save(product);
				OperateRecord record = new OperateRecord();
				record.setElementName("删除");
				record.setOperateTime(DateUtil.getCurrentTime());
				record.setProductId(product.getId());
				record.setUsername(userChnName);
				operateRecordDao.save(record);
			}
		}
		else if(type.intValue() == 10){//标记为已操作
			for(Product product : list){
				product.setOpState(1);
				product.setIsback(1);
				productJPADao.save(product);
				OperateRecord record = new OperateRecord();
				record.setElementName("标记为已操作!");
				record.setOperateTime(DateUtil.getCurrentTime());
				record.setProductId(product.getId());
				record.setUsername(userChnName);
				operateRecordDao.save(record);
			}
		}
		else if(type.intValue() == 11){//从检验返回到本厂总任务页面
			for(Product product : list){
				product.setState(3);
				product.setOpState(null);
				product.setIsback(1);
				productJPADao.save(product);
				OperateRecord record = new OperateRecord();
				record.setElementName("返回到本厂调度");
				record.setOperateTime(DateUtil.getCurrentTime());
				record.setProductId(product.getId());
				record.setUsername(userChnName);
				operateRecordDao.save(record);
			}
		}else if(type.intValue() == 12){//从当前工序返回到上个工序的检验
			for(Product product : list){
				if(product.getNextTempSonId()!=null){
					ProcessTempSon son1 = tempSonDao.findOne(product.getNextTempSonId());
					ProcessTempSon son2 = tempSonDao.findOne(product.getTempSonId());
					ProcessTempSon son3 = tempSonDao.findByParentIdAndProcessId(son1.getParentId(), son2.getProcessId());
					ProcessTempSon prev = tempSonDao.findByParentIdAndOrderNum(son3.getParentId(), son3.getOrderNum()-1);
					if(null != prev){
						product.setTempSonId(prev.getId());
						product.setNextTempSonId(null);
						product.setIsback(1);
						product.setState(4);
						productJPADao.save(product);
						SysProcess proc = sysProcessDao.findOne(son3.getProcessId());
						OperateRecord record = new OperateRecord();
						record.setElementName("从"+proc.getChnName()+"返回上个工序的检验");
						record.setOperateTime(DateUtil.getCurrentTime());
						record.setProductId(product.getId());
						record.setUsername(userChnName);
						operateRecordDao.save(record);
					}
				}else{
					ProcessTempSon son = tempSonDao.findOne(product.getTempSonId());
					SysProcess proc = sysProcessDao.findOne(son.getProcessId());
					ProcessTempSon prev = tempSonDao.findByParentIdAndOrderNum(son.getParentId(), son.getOrderNum()-1);
					if(null != prev){
						product.setTempSonId(prev.getId());
						product.setIsback(1);
						product.setState(4);
						productJPADao.save(product);
						OperateRecord record = new OperateRecord();
						record.setElementName("从"+proc.getChnName()+"返回上个工序的检验");
						record.setOperateTime(DateUtil.getCurrentTime());
						record.setProductId(product.getId());
						record.setUsername(userChnName);
						operateRecordDao.save(record);
					}else{
						product.setState(2);
						product.setIsback(1);
						productJPADao.save(product);
						OperateRecord record = new OperateRecord();
						record.setElementName("从"+proc.getChnName()+"返回生产审核");
						record.setOperateTime(DateUtil.getCurrentTime());
						record.setProductId(product.getId());
						record.setUsername(userChnName);
						operateRecordDao.save(record);
					}
				}
			}
		}else if(type.intValue() == 13){//从当前工序返回到上个工序的检验
			for(Product product : list){
				product.setState(15);
				productJPADao.save(product);
				ProcessTempSon son = tempSonDao.findOne(product.getTempSonId());
				SysProcess proc = sysProcessDao.findOne(son.getProcessId());
				OperateRecord record = new OperateRecord();
				record.setElementName("从"+proc.getChnName()+"转入临时库");
				record.setOperateTime(DateUtil.getCurrentTime());
				record.setProductId(product.getId());
				record.setUsername(userChnName);
				operateRecordDao.save(record);
			}
		}else if(type.intValue() == 14){//从当前工序返回到调度
			for(Product product : list){
				product.setState(11);
				product.setIsback(1);
				productJPADao.save(product);
				ProcessTempSon son = tempSonDao.findOne(product.getTempSonId());
				SysProcess proc = sysProcessDao.findOne(son.getProcessId());
				OperateRecord record = new OperateRecord();
				record.setElementName("从"+proc.getChnName()+"转入"+proc.getChnName()+"调度");
				record.setOperateTime(DateUtil.getCurrentTime());
				record.setProductId(product.getId());
				record.setUsername(userChnName);
				operateRecordDao.save(record);
			}
		}else if(type.intValue() == 15){//从当前工序跳转到本厂分页面
			for(Product product : list){
				product.setState(17);
				product.setIsback(null);
				product.setOpState(null);
				if(null != product.getIscp()&&product.getIscp().intValue()==1){
					product.setForgingInDateA(forgingDate);
					product.setForgingWorkerA(forgingWork);
				}else{
					product.setForgingInDate(forgingDate);
					product.setForgingWorker(forgingWork);
				}
				productJPADao.save(product);
				ProcessTempSon son = tempSonDao.findOne(product.getTempSonId());
				SysProcess proc = sysProcessDao.findOne(son.getProcessId());
				OperateRecord record = new OperateRecord();
				record.setElementName("从"+proc.getChnName()+"转入"+proc.getChnName()+"分页面");
				record.setOperateTime(DateUtil.getCurrentTime());
				record.setProductId(product.getId());
				record.setUsername(userChnName);
				operateRecordDao.save(record);
			}
		}else if(type.intValue() == 16){//从当前工序跳转到外协分页面
			for(Product product : list){
				product.setState(18);
				product.setIsback(null);
				product.setOpState(null);
				if(null != product.getIscp()&&product.getIscp().intValue()==1){
					product.setForgingInDateA(forgingDate);
				}else{
					product.setForgingInDate(forgingDate);
				}
				ProcessTempSon tempSon = tempSonDao.findOne(product.getTempSonId());
				if(null != tempSon&&tempSon.getProcessId().intValue()==5){
					product.setForgingInDate(DateUtil.getCurrentymd());
				}
				productJPADao.save(product);
				ProcessTempSon son = tempSonDao.findOne(product.getTempSonId());
				SysProcess proc = sysProcessDao.findOne(son.getProcessId());
				OperateRecord record = new OperateRecord();
				record.setElementName("从"+proc.getChnName()+"转入"+proc.getChnName()+"外协分页面");
				record.setOperateTime(DateUtil.getCurrentTime());
				record.setProductId(product.getId());
				record.setUsername(userChnName);
				operateRecordDao.save(record);
			}
		}else if(type.intValue() == 17){//从当前子页面返回到外协总页面
			for(Product product : list){
				product.setState(10);
				product.setIsback(null);
				product.setOpState(null);
				if(null != product.getIscp()&&product.getIscp().intValue()==1){
					product.setForgingInDateA(forgingDate);
				}else{
					product.setForgingInDate(forgingDate);
				}
				productJPADao.save(product);
				ProcessTempSon son = tempSonDao.findOne(product.getTempSonId());
				SysProcess proc = sysProcessDao.findOne(son.getProcessId());
				OperateRecord record = new OperateRecord();
				record.setElementName("从"+proc.getChnName()+"转入"+proc.getChnName()+"外协分页面");
				record.setOperateTime(DateUtil.getCurrentTime());
				record.setProductId(product.getId());
				record.setUsername(userChnName);
				operateRecordDao.save(record);
			}
		}else if(type.intValue() == 18){//从工序总调度转入子页面
			for(Product product : list){
				product.setState(19);
				product.setIsback(null);
				product.setOpState(null);
				productJPADao.save(product);
				ProcessTempSon son = tempSonDao.findOne(product.getTempSonId());
				SysProcess proc = sysProcessDao.findOne(son.getProcessId());
				OperateRecord record = new OperateRecord();
				record.setElementName("从"+proc.getChnName()+"分调度转入"+proc.getChnName()+"分页面");
				record.setOperateTime(DateUtil.getCurrentTime());
				record.setProductId(product.getId());
				record.setUsername(userChnName);
				operateRecordDao.save(record);
			}
		}
		return "success";
	}
	
	//查看节点操作记录
	@RequestMapping("/showOperateRecord")
	public String showOperateRecord(Long productId,Model model){
		List<OperateRecord> list = operateRecordDao.findByProductId(productId);
		model.addAttribute("list", list);
		return "main/product/operateRecord";
	}
	
	//修改初始化数据
	@RequestMapping("/update")
	@ResponseBody
	public String update(Product p,HttpSession session){
		productService.splitSerialNum(p);
		productJPADao.save(p);
		return "success";
	}
	//查看工序
	@RequestMapping("/showProcess")
	public String showProcess(Long id,Model model){
		Product p = productJPADao.findOne(id);
		if(null != p.getNextTempSonId()){
			ProcessTempSon son = tempSonDao.findOne(p.getNextTempSonId());
			List<ProcessTempSon> list = tempSonDao.findByParentId(son.getParentId());
			for(ProcessTempSon son2 : list){
				SysProcess process = sysProcessDao.findOne(son2.getProcessId());
				son2.setProcName(process.getChnName());
			}
			model.addAttribute("list", list);
		}else{
			ProcessTempSon son = tempSonDao.findOne(p.getTempSonId());
			List<ProcessTempSon> list = tempSonDao.findByParentId(son.getParentId());
			for(ProcessTempSon son2 : list){
				SysProcess process = sysProcessDao.findOne(son2.getProcessId());
				son2.setProcName(process.getChnName());
			}
			model.addAttribute("list", list);
		}
		return "main/product/showProcess";
	}
	
	@RequestMapping("/endList")
	public String endList(Integer type,Model model){
		return "main/product/endList";
	}
	
	@RequestMapping("/getEndList")
	@ResponseBody
	public Object getEndList(PageInfo page,Product bean,Long processIdB,String startDateB,String endDateB,String poDateB,String forgingWorker){
		Integer pageNum = page.getPage()-1;
		StringBuffer where = new StringBuffer();
		where.append("a.state = 9");
		if(null != bean){
			if(null != bean.getSerialNum()&&!("").equals(bean.getSerialNum())){
				where.append(" and a.serial_num like '%"+bean.getSerialNum()+"%'");
			}
            if(null != bean.getOrderNum()&&!("").equals(bean.getOrderNum())){
            	where.append(" and a.order_num = '"+bean.getOrderNum()+"'");
			}
		}
		if(null != bean.getProcessId()){
			productService.getDateColumn(bean.getProcessId(), bean.getStartDate(), bean.getEndDate(),where,startDateB,endDateB,forgingWorker);
		}
		PageResponse res = new PageResponse();
		String sql = "select a.* from product a where "+where+" limit "+(pageNum*page.getRows())+","+page.getRows()+"";
		List<Map<String, Object>> list =  productDao.getSqlQuery(sql);
		res.setRows(list);
		Object count = productDao.getCount("select count(*) from product a where "+where+"");
		res.setTotal(count);
		return res;
	}
	
	//试样库
	@RequestMapping("/sampleListB")
	public String sampleListB(){
			
		return "main/product/sampleList";
	}
	
	//试样库
	@RequestMapping("/getSampleListB")
	@ResponseBody
	public Object getSampleListB(PageInfo page,Product bean,Long processIdB,String startDateB,String endDateB,String poDateB,String forgingWorker){
		Integer pageNum = page.getPage()-1;
		StringBuffer where = new StringBuffer();
		where.append("a.state = 16");
		if(null != bean){
			if(null != bean.getSerialNum()&&!("").equals(bean.getSerialNum())){
				where.append(" and a.serial_num like '%"+bean.getSerialNum()+"%'");
			}
            if(null != bean.getOrderNum()&&!("").equals(bean.getOrderNum())){
            	where.append(" and a.order_num = '"+bean.getOrderNum()+"'");
			}
		}
		if(null != bean.getProcessId()){
			productService.getDateColumn(bean.getProcessId(), bean.getStartDate(), bean.getEndDate(),where,startDateB,endDateB,forgingWorker);
		}
		PageResponse res = new PageResponse();
		String sql = "select a.* from product a where "+where+" limit "+(pageNum*page.getRows())+","+page.getRows()+"";
		List<Map<String, Object>> list =  productDao.getSqlQuery(sql);
		res.setRows(list);
		Object count = productDao.getCount("select count(*) from product a where "+where+"");
		res.setTotal(count);
		return res;
	}
	
	
	//异常件或已严重拖期数据
	@RequestMapping("/otherList")
	public String otherList(){
		return "main/product/otherList";
	}
	
	@RequestMapping("/getOtherList")
	@ResponseBody
	public Object getOtherList(PageInfo page,Product bean,Long processIdB,String startDateB,String endDateB,String poDateB,String forgingWorker){
		Integer pageNum = page.getPage()-1;
		StringBuffer where = new StringBuffer();
		where.append("a.state = 15");
		if(null != bean){
			if(null != bean.getSerialNum()&&!("").equals(bean.getSerialNum())){
				where.append(" and a.serial_num like '%"+bean.getSerialNum()+"%'");
			}
            if(null != bean.getOrderNum()&&!("").equals(bean.getOrderNum())){
            	where.append(" and a.order_num = '"+bean.getOrderNum()+"'");
			}
            if(null != bean.getMaterial()&&!("").equals(bean.getMaterial())){
            	where.append(" and a.material like '%"+bean.getMaterial()+"%'");
			}
		}
		if(null != bean.getProcessId()){
			productService.getDateColumn(bean.getProcessId(), bean.getStartDate(), bean.getEndDate(),where,startDateB,endDateB,forgingWorker);
		}
		PageResponse res = new PageResponse();
		String sql = "select a.* from product a where "+where+" limit "+(pageNum*page.getRows())+","+page.getRows()+"";
		List<Map<String, Object>> list =  productDao.getSqlQuery(sql);
		res.setRows(list);
		Object count = productDao.getCount("select count(*) from product a where "+where+"");
		res.setTotal(count);
		return res;
	}
	
	
	@RequestMapping("/excelImport")
	public String excelImport(){
		return "main/product/excelImport";
	}
	
	@RequestMapping("/toStart")
	@ResponseBody
	public String doStart(@RequestParam(value = "ids", required = false) List<Long> ids,HttpSession session){
		List<Product> list = (List<Product>) productJPADao.findAll(ids);
		String userChnName = (String) session.getAttribute("userChnName");
		
		for(Product product : list){
			if(product.getQty()==null){
				return product.getSerialNum()+" "+product.getOrderNum();
			}
		}
		
		for(Product product : list){
			ProcessTempSon tempSon = tempSonDao.findOne(product.getTempSonId());
			if(tempSon.getProcessId().intValue()>=6&&tempSon.getProcessId().intValue()<=12){
				product.setState(11);
			}else{
				product.setState(3);
			}
			product.setStartDate(DateUtil.getCurrentymd());
			product.setActUnitWeight(product.getBlankWeight());
//			product.setActQty(product.getQty());
			productJPADao.save(product);
			OperateRecord record = new OperateRecord();
			record.setElementName("生产下发");
			record.setOperateTime(DateUtil.getCurrentTime());
			record.setProductId(product.getId());
			record.setUsername(userChnName);
			operateRecordDao.save(record);
		}
		return "success";
	}
	
	@RequestMapping("/addSample")
	public String addSample(Long id,Model model){
		Product product = productJPADao.findOne(id);
		model.addAttribute("bean", product);
		return "main/product/addSample";
	}
	
	@RequestMapping("/doAddSample")
	@ResponseBody
	public String doAddSample(Product p,HttpSession session,Long processId){
		if(null == p.getListP()||p.getListP().size()<1){
			return "fail";
		}
		for(Product pro : p.getListP()){
			if(pro.getQty()==null){
				return "qty";
			}
		}
		Product product = productJPADao.findOne(p.getId());
		int x = 0;
		for(Product pro : p.getListP()){
			ProcessTempSon son = new ProcessTempSon();
			if(pro.getNextTempSonId()!=null){
				son = tempSonDao.findOne(product.getNextTempSonId());
			}else{
				son = tempSonDao.findOne(product.getTempSonId());
			}
			ProcessTempSon son2 = tempSonDao.findByParentIdAndProcessId(son.getParentId(), processId);
			if(son2!=null){
				product.setTempSonId(son2.getId());
			}
			product.setId(null);
			product.setPoQty(null);
			product.setSerialNum(pro.getSerialNum());
			product.setOrderNum(pro.getOrderNum());
			product.setState(3);
			productService.splitSerialNum(product);
			product.setQty(pro.getQty());
//			product.setActQty(pro.getActQty());
			product.setSizeA(pro.getSizeA());
			product.setSizeB(pro.getSizeB());
			product.setSizeC(pro.getSizeC());
			product.setSizeD(pro.getSizeD());
			product.setSizeE(pro.getSizeE());
			product.setSizeF(pro.getSizeF());
			product.setSizeG(pro.getSizeG());
			product.setSizeH(pro.getSizeH());
			entityManager.clear();
			productJPADao.save(product);
			x++;
		}
		return "success";
	}
	
	@RequestMapping("/toSplit")
	public String toSplit(Long id,Model model){
		Product product = productJPADao.findOne(id);
		model.addAttribute("bean", product);
		return "main/product/split";
	}
	
	@RequestMapping("/doSplit")
	@ResponseBody
	public String doSplit(Product p,String orderNumx,HttpSession session,Integer state){
		if(null == p.getListP()||p.getListP().size()<2){
			return "fail";
		}
		String userChnName = (String) session.getAttribute("userChnName");
		Product product = productJPADao.findOne(p.getId());
		List<OperateRecord> listRecord = operateRecordDao.findByProductId(p.getId());
		int x = 0;
		for(Product pro : p.getListP()){
			
			if(pro.getQty()==null){
				return "qty";
			}
			
			if(pro.getOrderNum()!=null&&!("").equals(pro.getOrderNum())){
				pro.setOrderNum(pro.getOrderNum().replaceAll("-", "~"));
				String[] s = pro.getOrderNum().split("~");
				if(s.length==2){
					try {
						Integer a = Integer.parseInt(s[0]);
						Integer b = Integer.parseInt(s[1]);
						Integer c = b-a+1;
						if(c.intValue()==pro.getQty().intValue()){
							
						}else {
							if(state.intValue()==0){
								return "fail3";
							}
						}
					} catch (Exception e) {
						
					}
				}
			}
		}
		for(Product pro : p.getListP()){
			if(x==0){
				product.setSerialNum(pro.getSerialNum());
				product.setOrderNum(pro.getOrderNum());
				product.setActUnitWeight(pro.getActUnitWeight());
				productService.splitSerialNum(product);
				product.setQty(pro.getQty());
//				product.setActQty(pro.getActQty());
				productJPADao.save(product);
				entityManager.clear();
			}else{
				product.setId(null);
				product.setPoQty(null);
				product.setSerialNum(pro.getSerialNum());
				product.setOrderNum(pro.getOrderNum());
				product.setActUnitWeight(pro.getActUnitWeight());
				productService.splitSerialNum(product);
				product.setQty(pro.getQty());
//				product.setActQty(pro.getActQty());
				productJPADao.save(product);
				entityManager.clear();
				for(OperateRecord prod : listRecord){
					prod.setId(null);
					prod.setProductId(product.getId());
					operateRecordDao.save(prod);
				}
			}
			OperateRecord operRecord = new OperateRecord();
	    	operRecord.setOperateTime(DateUtil.getCurrentTime());
	    	operRecord.setProductId(p.getId());
	    	operRecord.setUsername(userChnName);
	    	operRecord.setElementName(product.getSerialNum()+" "+orderNumx+"-> 拆分为:锻件编号:"+product.getSerialNum()+" 序号:"+product.getOrderNum()+" 数量:"+product.getQty()+" 实际单重:"+product.getActUnitWeight());
    		operateRecordDao.save(operRecord);
			x++;
		}
		return "success";
	}
	
	@RequestMapping("/doBack")
	@ResponseBody
	public String doBack(@RequestParam(value = "ids", required = false) List<Long> ids){
		List<Product> list = (List<Product>) productJPADao.findAll(ids);
		for(Product product : list){
			product.setState(product.getState()-1);
			product.setStartDate("");
			productJPADao.save(product);
		}
		return "success";
	}
	
	@RequestMapping("/qtyTotal")
	public String qtyTotal(@RequestParam(value = "ids", required = false) List<Long> ids,Model model){
		List<Product> list = (List<Product>) productJPADao.findAll(ids);
		Integer total = 0;
		for(Product product : list){
			if(null != product.getQty()&&product.getState().intValue()!=5&&product.getState().intValue()!=7&&product.getState().intValue()!=8){
				total = total +product.getQty();
			}
		}
		model.addAttribute("total", total);
		return "main/product/qtyTotal";
	}
	
	@RequestMapping("/qtyTotalWeight")
	public String qtyTotalWeight(@RequestParam(value = "ids", required = false) List<Long> ids,Model model){
		List<Product> list = (List<Product>) productJPADao.findAll(ids);
		BigDecimal total = new BigDecimal(0);
		for(Product product : list){
			
			if(null != product.getIscp()&&product.getIscp().intValue()==1){
				try {
					
					String n = product.getBlankRemarkA()==null||("").equals(product.getBlankRemarkA())?"0*0":product.getBlankRemarkA();
					String[] nn = n.split("[*]");
					if(nn.length==2){
						BigDecimal x = new BigDecimal(nn[0]).multiply(new BigDecimal(nn[1]));
						total = total.add(x);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(null != product.getQty()&&StringUtil.isNumber(product.getBlankWeight())){
				BigDecimal a = new BigDecimal(product.getQty()).multiply(new BigDecimal(product.getBlankWeight()));
				total = total.add(a);
			}
		}
		model.addAttribute("total", total);
		return "main/product/qtyTotal";
	}
	
	//编辑工序中的内容
	@RequestMapping("/editMore")
	@ResponseBody
	public String editMore(Product p,Integer ptype,HttpSession session,Long id) throws CloneNotSupportedException{
		
		Product product = productJPADao.findOne(p.getId());
		Product pppp = (Product)product.clone();
		String eleName= "";
		if(ptype.intValue()==1){
			product.setBlankDateA(p.getBlankDateA());
			product.setBlankRemarkA(p.getBlankRemarkA());
			product.setBlankDate(p.getBlankDate());
			product.setBlankRemark(p.getBlankRemark());
			product.setHeatNum(p.getHeatNum());
			product.setHlSize(p.getHlSize());
			product.setIgnotNum(p.getIgnotNum());
			product.setSupplier(p.getSupplier());
			product.setActUnitWeight(p.getActUnitWeight());
			productService.splitSerialNum(product);
			productJPADao.save(product);
			if(product.getIscp()!=null&&product.getIscp().intValue()==1){
				eleName = "下料出坯";
			}else{
				eleName = "下料";
			}
		}
		if(ptype.intValue()==2){
			product.setForgingWorkerA(p.getForgingWorkerA());
			product.setForgingInDate(p.getForgingInDate());
			product.setForgingInDateA(p.getForgingInDateA());
			product.setForgingRemark(p.getForgingRemark());
			product.setForgingWorker(p.getForgingWorker());
			product.setProcessRemark(p.getProcessRemark());
			productJPADao.save(product);
			if(product.getIscp()!=null&&product.getIscp().intValue()==1){
				eleName = "锻造出坯";
			}else{
				eleName = "锻造";
			}
		}
		if(ptype.intValue()==3){
			product.setHeatTreatInDateA(p.getHeatTreatInDateA());
			product.setAfterForgingOut(p.getAfterForgingOut());
			product.setAfterForgingInDate(p.getAfterForgingInDate());
			product.setAfterForgingWorker(p.getAfterForgingWorker());
			product.setSampleNum(p.getSampleNum());
			productJPADao.save(product);
			if(product.getIscp()!=null&&product.getIscp().intValue()==1){
				eleName = "热处理出坯";
			}else{
				eleName = "锻后热处理";
			}
		}
		if(ptype.intValue()==4){
			product.setIspsa(p.getIspsa());
			product.setRoughMachingWorker(p.getRoughMachingWorker());
			product.setRoughMachingWeight(p.getRoughMachingWeight());
			product.setRoughMachingRemark(p.getRoughMachingRemark());
			product.setRoughMachingOutCom(p.getRoughMachingOutCom());
			product.setRoughMachingOutRemark(p.getRoughMachingOutRemark());
			product.setProcessRemark(p.getProcessRemark());
			productJPADao.save(product);
			eleName = "粗加工";
		}
		if(ptype.intValue()==5){
			product.setPerHeatTreatInDate(p.getPerHeatTreatInDate());
			product.setSampleNum(p.getSampleNum());
			product.setPerHeatTreatWorker(p.getPerHeatTreatWorker());
			productJPADao.save(product);
			eleName = "性能热处理";
		}
		if(ptype.intValue()==6){
			product.setDeliveryWorker(p.getDeliveryWorker());
			product.setIspsb(p.getIspsb());
			product.setDeliveryOutCom(p.getDeliveryOutCom());
			product.setDeliveryOutRemark(p.getDeliveryOutRemark());
			product.setProcessRemark(p.getProcessRemark());
			productJPADao.save(product);
			eleName = "交货加工";
		}
		if(ptype.intValue()==7){
			product.setFollowupInDate(p.getFollowupInDate());
			product.setFollowupWorker(p.getFollowupWorker());
			productJPADao.save(product);
			eleName = "后续热处理";
		}
		if(ptype.intValue()==8){
			product.setHalfFinishMachingWorker(p.getHalfFinishMachingWorker());
			product.setHalfFinishOutCom(p.getHalfFinishOutCom());
			product.setHalfFinishOutRemark(p.getHalfFinishOutRemark());
			product.setIspsc(p.getIspsc());
			productJPADao.save(product);
			eleName = "半精加工";
		}
		if(ptype.intValue()==9){
			product.setIspsd(p.getIspsd());
			product.setFinishMachingWorker(p.getFinishMachingWorker());
			product.setFinishMachingWeight(p.getFinishMachingWeight());
			product.setFinishMachingOutCom(p.getFinishMachingOutCom());
			product.setFinishMachingOutRemark(p.getFinishMachingOutRemark());
			productJPADao.save(product);
			eleName = "精加工";
		}
		if(ptype.intValue()==10){
			product.setSampleInDate(p.getSampleInDate());
			productJPADao.save(product);
			eleName = "取样";
		}
		if(ptype.intValue()==11){
			product.setPoUpdateDate(p.getPoUpdateDate());
			product.setRemarksA(p.getRemarksA());
			product.setRemarksB(p.getRemarksB());
			product.setSerialNum(p.getSerialNum());
//			product.setOrderNum(p.getOrderNum());
//			productService.splitSerialNum(product);
			product.setSizeA(p.getSizeA());
			product.setSizeB(p.getSizeB());
			product.setSizeC(p.getSizeC());
			product.setSizeD(p.getSizeD());
			product.setSizeE(p.getSizeE());
			product.setSizeF(p.getSizeF());
			product.setSizeG(p.getSizeG());
			product.setSizeH(p.getSizeH());
			product.setBlankWeight(p.getBlankWeight());
			product.setQualityRequire(p.getQualityRequire());
			product.setNdtRequire(p.getNdtRequire());
			product.setDeliveryState(p.getDeliveryState());
			product.setHtRequire(p.getHtRequire());
			product.setHtRequireB(p.getHtRequireB());
			product.setHdnsRequire(p.getHdnsRequire());
			product.setNextProcess(p.getNextProcess());
			product.setOtherRequire(p.getOtherRequire());
			product.setForgingEquip(p.getForgingEquip());
			product.setMaterialNum(p.getMaterialNum());
			product.setDwgNum(p.getDwgNum());
			product.setDescription(p.getDescription());
			product.setMaterial(p.getMaterial());
			product.setGrade(p.getGrade());
//			product.setQty(p.getQty());
			product.setCode(p.getCode());
			product.setPoQty(p.getPoQty());
			product.setBlankRemark(p.getBlankRemark());
			product.setBlankRemarkA(p.getBlankRemarkA());
			product.setPoDate(p.getPoDate());
			product.setHeatNum(p.getHeatNum());
			product.setForgingWorker(p.getForgingWorker());
			product.setAfterForgingWorker(p.getAfterForgingWorker());
			product.setRoughMachingWorker(p.getRoughMachingWorker());
			product.setPerHeatTreatWorker(p.getPerHeatTreatWorker());
			product.setDeliveryWorker(p.getDeliveryWorker());
			product.setFollowupWorker(p.getFollowupWorker());
			product.setStartDate(p.getStartDate());
			product.setBlankDateA(p.getBlankDateA());
			product.setForgingWorkerA(p.getForgingWorkerA());
			product.setForgingInDateA(p.getForgingInDateA());
			product.setForgingOutDateA(p.getForgingOutDateA());
			product.setHeatTreatInDateA(p.getHeatTreatInDateA());
			product.setHeatTreatOutDateA(p.getHeatTreatOutDateA());
			product.setBlankDate(p.getBlankDate());
			product.setForgingInDate(p.getForgingInDate());
			product.setForgingOutDate(p.getForgingOutDate());
			product.setAfterForgingInDate(p.getAfterForgingInDate());
			product.setAtterForgingOutDate(p.getAtterForgingOutDate());
			product.setRoughMachingInDate(p.getRoughMachingInDate());
			product.setRoughMachingOutDate(p.getRoughMachingOutDate());
			product.setPerHeatTreatInDate(p.getPerHeatTreatInDate());
			product.setPerHeatTreatOutDate(p.getPerHeatTreatOutDate());
			product.setSampleInDate(p.getSampleInDate());
			product.setSampleOutDate(p.getSampleOutDate());
			product.setDeliveryInDate(p.getDeliveryInDate());
			product.setDeliveryOutDate(p.getDeliveryOutDate());
			product.setFollowupInDate(p.getFollowupInDate());
			product.setFollowupOutDate(p.getFollowupOutDate());
			product.setStartDate(p.getStartDate());
			productJPADao.save(product);
			eleName = "滚动表";
		}
		if(ptype.intValue()==12){
			product.setRoughMachingRemark(p.getRoughMachingRemark());
			productJPADao.save(product);
			eleName = "调度修改数量为:"+p.getQty()+" 修改序号为:"+p.getOrderNum();
		}
		if(ptype.intValue()==13){
			product.setBreakNum(p.getBreakNum());
			productJPADao.save(product);
		}
		try {
			Map<String, String> map = OtherUtil.compareFields(pppp, product, null);
			Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator(); 
			String userChnName = (String) session.getAttribute("userChnName");
			OperateRecord record = new OperateRecord();
			while (entries.hasNext()) { 
				  Map.Entry<String, String> entry = entries.next(); 
				  record.setElementName(ProductChn.getChnCloumns().get(entry.getKey())+": "+entry.getValue());	
			}
			record.setOperateTime(DateUtil.getCurrentTime());
			record.setProductId(product.getId());
			record.setUsername(userChnName);
			if(null != record.getElementName()&&!("").equals(record.getElementName())){
				record.setElementName(record.getElementName().replaceAll("null", ""));
				operateRecordDao.save(record);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "success";
	}
	
	@RequestMapping("/doExcelImport")
	@ResponseBody
    public String doExcelImport(String excelFile,HttpServletRequest request,Integer sheetIndex) throws IOException{
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
		List<Product> list = new ArrayList<Product>();
		SimpleDateFormat simple = new SimpleDateFormat("yy-MM-dd");
		if(rowNum>=1){
			List<String> listStr = new ArrayList<String>();
			for(int x = 3;x<=rowNum;x++){
				Row row = sheet.getRow(x);
				if(row==null){
					break;
				}
				Product product = new Product();
				if(null != row.getCell(0)&& row.getCell(0).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
						product.setProductNum(row.getCell(0).getStringCellValue());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				if(null != row.getCell(2)&&row.getCell(2).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
						product.setPoNum(row.getCell(2).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				try {
				    if(null != row.getCell(3)&&row.getCell(3).getCellType()!=Cell.CELL_TYPE_BLANK&&null != row.getCell(3).getDateCellValue()){
						product.setPoDate(simple.format(row.getCell(3).getDateCellValue()));
				   }
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(null != row.getCell(4)&&row.getCell(4).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
						product.setPoUpdateDate(row.getCell(4).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(5)&&row.getCell(5).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
						product.setRemarksA(row.getCell(5).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(6)&&row.getCell(6).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
						product.setRemarksB(row.getCell(6).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(7)&&row.getCell(7).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
						product.setMaterial(row.getCell(7).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(8)&&row.getCell(8).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
						product.setGrade(row.getCell(8).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(9)&&row.getCell(9).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
						product.setSerialNum(row.getCell(9).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					break;
				}
				if(null != row.getCell(10)&&row.getCell(10).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(10).setCellType(Cell.CELL_TYPE_STRING);
						product.setOrderNum(row.getCell(10).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(11)&&row.getCell(11).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(11).setCellType(Cell.CELL_TYPE_STRING);
					    product.setSizeA(row.getCell(11).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(12)&&row.getCell(12).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(12).setCellType(Cell.CELL_TYPE_STRING);  
						if(!StringUtil.isNullOrSpace(row.getCell(12).getStringCellValue())&&StringUtil.isNumber(row.getCell(12).getStringCellValue())){
							BigDecimal bd = new BigDecimal(row.getCell(12).getStringCellValue()).setScale( 1, BigDecimal.ROUND_UP );
							product.setSizeB(bd.toString());
						}else{
							product.setSizeB(row.getCell(12).getStringCellValue());
						}
					} catch (Exception e) {
						product.setSizeB(row.getCell(12).getStringCellValue());
						e.printStackTrace();
					}
				}
				if(null != row.getCell(13)&&row.getCell(13).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(13).setCellType(Cell.CELL_TYPE_STRING);
						product.setSizeC(row.getCell(13).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(14)&&row.getCell(14).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(14).setCellType(Cell.CELL_TYPE_STRING);
						if(!StringUtil.isNullOrSpace(row.getCell(14).getStringCellValue())&&StringUtil.isNumber(row.getCell(14).getStringCellValue())){
							BigDecimal bd = new BigDecimal(row.getCell(14).getStringCellValue()).setScale( 1, BigDecimal.ROUND_UP );
							product.setSizeD(bd.toString());
						}else{
							product.setSizeD(row.getCell(14).getStringCellValue());
						}
					} catch (Exception e) {
						product.setSizeD(row.getCell(14).getStringCellValue());
						e.printStackTrace();
					}
				}
				if(null != row.getCell(15)&&row.getCell(15).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(15).setCellType(Cell.CELL_TYPE_STRING);
						product.setSizeE(row.getCell(15).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(16)&&row.getCell(16).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(16).setCellType(Cell.CELL_TYPE_STRING);
						if(!StringUtil.isNullOrSpace(row.getCell(16).getStringCellValue())){
							BigDecimal bd = new BigDecimal(row.getCell(16).getStringCellValue()).setScale( 1, BigDecimal.ROUND_UP );
							product.setSizeF(bd.toString());
						}else{
							product.setSizeF(row.getCell(16).getStringCellValue());
						}
					} catch (Exception e) {
						product.setSizeF(row.getCell(16).getStringCellValue());
						e.printStackTrace();
					}
				}
				if(null != row.getCell(17)&&row.getCell(17).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(17).setCellType(Cell.CELL_TYPE_STRING);
						product.setSizeG(row.getCell(17).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(18)&&row.getCell(18).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(18).setCellType(Cell.CELL_TYPE_STRING);
						if(!StringUtil.isNullOrSpace(row.getCell(18).getStringCellValue())){
							BigDecimal bd = new BigDecimal(row.getCell(18).getStringCellValue()).setScale( 1, BigDecimal.ROUND_UP );
							product.setSizeH(bd.toString());
						}else{
							product.setSizeH(row.getCell(18).getStringCellValue());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(19)&&row.getCell(19).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(19).setCellType(Cell.CELL_TYPE_STRING);
						product.setPoQty(stringToInteger(row.getCell(19).getStringCellValue()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(20)&&row.getCell(20).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(20).setCellType(Cell.CELL_TYPE_STRING);
						product.setBlankWeight(row.getCell(20).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(21)&&row.getCell(21).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(21).setCellType(Cell.CELL_TYPE_STRING);
						product.setQty(stringToInteger(row.getCell(21).getStringCellValue()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(22)&&row.getCell(22).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(22).setCellType(Cell.CELL_TYPE_STRING);
						product.setDescription(row.getCell(22).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(23)&&row.getCell(23).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(23).setCellType(Cell.CELL_TYPE_STRING);
						product.setQualityRequire(row.getCell(23).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(24)&&row.getCell(24).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(24).setCellType(Cell.CELL_TYPE_STRING);
						product.setNdtRequire(row.getCell(24).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(25)&&row.getCell(25).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(25).setCellType(Cell.CELL_TYPE_STRING);
						product.setDeliveryState(row.getCell(25).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(26)&&row.getCell(26).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(26).setCellType(Cell.CELL_TYPE_STRING);
						product.setHtRequire(row.getCell(26).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(27)&&row.getCell(27).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(27).setCellType(Cell.CELL_TYPE_STRING);
						product.setHtRequireB(row.getCell(27).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(28)&&row.getCell(28).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(28).setCellType(Cell.CELL_TYPE_STRING);
						product.setNextProcess(row.getCell(28).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(29)&&row.getCell(29).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(29).setCellType(Cell.CELL_TYPE_STRING);
						product.setHdnsRequire(row.getCell(29).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(30)&&row.getCell(30).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(30).setCellType(Cell.CELL_TYPE_STRING);
						product.setOtherRequire(row.getCell(30).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(31)&&row.getCell(31).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(31).setCellType(Cell.CELL_TYPE_STRING);
						product.setForgingEquip(row.getCell(31).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(32)&&row.getCell(32).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(32).setCellType(Cell.CELL_TYPE_STRING);
						product.setMaterialNum(row.getCell(32).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null != row.getCell(33)&&row.getCell(33).getCellType()!=Cell.CELL_TYPE_BLANK){
					try {
						row.getCell(33).setCellType(Cell.CELL_TYPE_STRING);
						product.setDwgNum(row.getCell(33).getStringCellValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null == product.getOrderNum()||("").equals(product.getOrderNum())){
					if(null != product.getPoQty()&&product.getPoQty()>1){
						product.setOrderNum("1~"+product.getPoQty());
					}
					if(null != product.getPoQty()&&product.getPoQty().intValue()==1){
						product.setOrderNum("1");
					}
				}
				product.setSerialNum(product.getSerialNum().trim().replace("~", "-"));
				int len = product.getSerialNum().length();
				StringBuffer n = new StringBuffer();
				for(int j = 0;j<len;j++){
					char v = product.getSerialNum().charAt(j);
					if (Character.isDigit(v)) {
						break;
		            }else{
		            	n.append(v);
		            }
				}
				product.setCode(n.toString());
				listStr.add(product.getSerialNum()+product.getOrderNum());
				list.add(product);
			}
			HashSet h = new HashSet(listStr);
			if(h.size()!=list.size()){
				File f = new File(realPath+"/"+excelFile);
				if(f.exists()){
					f.delete();
				}
				return "selfRepeat";
			}
			Integer num = 1;
			for(Product p : list){
				p.setNum(num);
				p.setState(1);
				if(null == p.getSerialNum()||("").equals(p.getSerialNum())){
					continue;
				}
				productService.splitSerialNum(p);
				List<ForgingSize> listf = forgingSizeDao.findByForgingNum(p.getSerialNum());
				if(listf!=null&&listf.size()>0){
					p.setForgingSize(listf.get(0).getForgingSize());
				}
				productJPADao.save(p);
				num ++;
			}
		}
		File f = new File(realPath+"/"+excelFile);
		if(f.exists()){
			f.delete();
		}
		return "success";
	}
	
	public Integer stringToInteger(String str){
		if(str == null || ("").equals(str)){
			return null;
		}
		return Integer.parseInt(str);
	}
	
	//导出下料单
    @RequestMapping(value="/exportBlankInfo")
    public void exportBlankInfo(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "ids", required = false) List<Long> ids) throws Exception{
    		String uuid = StringUtil.getUUID();
    		String realPath = request.getSession().getServletContext().getRealPath("/"); 
    		File file = new File(realPath+"excel/");
           if(!file.exists()){
        	   file.mkdir();
           }
            Workbook wb = ExcelUtil.writeExcel("xls", realPath+"excel/下料单.xls");
    		Sheet sheet = wb.getSheetAt(0);
    		List<Product> list = (List<Product>) productJPADao.findAllOrder(ids);
    		int y = 3;
    		for(Product son : list){
    			Row row = sheet.getRow(y);
    			if(null == row){
    				row = sheet.createRow(y);
    			}
    			Cell n1 = row.getCell(1);
    			if(n1==null){
    				n1 = row.createCell(1);
    			}
    			if(null != son.getIscp()&&son.getIscp().intValue()==1){
    				n1.setCellValue(son.getSerialNum()+"P");
    			}else{
    				n1.setCellValue(son.getSerialNum());    			
    			}
    			Cell n2 = row.getCell(2);
    			if(n2==null){
    				n2 = row.createCell(2);
    			}
    				n2.setCellValue(son.getOrderNum());
    			Cell n5 = row.getCell(5);
    			if(n5==null){
    				n5 = row.createCell(5);
    			}
    			n5.setCellValue(son.getSizeA());
    			Cell n6 = row.getCell(6);
    			if(n6==null){
    				n6 = row.createCell(6);
    			}
    			n6.setCellValue(son.getSizeB());
    			Cell n7 = row.getCell(7);
    			if(n7==null){
    				n7=row.createCell(7);
    			}
    			n7.setCellValue(son.getSizeC());
    			Cell n8 = row.getCell(8);
    			if(n8==null){
    				n8=row.createCell(8);
    			}
    			n8.setCellValue(son.getSizeD());
    			Cell n9 = row.getCell(9);
    			if(n9==null){
    				n9=row.createCell(9);
    			}
    			n9.setCellValue(son.getSizeE());
    			Cell n10 = row.getCell(10);
    			if(n10==null){
    				n10=row.createCell(10);
    			}
    			n10.setCellValue(son.getSizeF());
    			Cell n11 = row.getCell(11);
    			if(n11==null){
    				n11=row.createCell(11);
    			}
    			n11.setCellValue(son.getSizeG());
    			Cell n12 = row.getCell(12);
    			if(n12==null){
    				n12=row.createCell(12);
    			}
    			n12.setCellValue(son.getSizeH());
    			Cell n13 = row.getCell(13);
    			if(n13==null){
    				n13=row.createCell(13);
    			}
    			n13.setCellValue(son.getMaterial());
    			Cell n14 = row.getCell(14);
    			if(n14==null){
    				n14=row.createCell(14);
    			}
    			n14.setCellValue(son.getGrade());
    			Cell n15 = row.getCell(15);
    			if(n15==null){
    				n15=row.createCell(15);
    			}
    			n15.setCellValue(son.getDescription());
    			Cell n16 = row.getCell(16);
    			if(n16==null){
    				n16=row.createCell(16);
    			}
    			n16.setCellValue(son.getHlSize());
    			Cell n17 = row.getCell(17);
    			if(n17==null){
    				n17=row.createCell(17);
    			}
    			if(null != son.getIscp()&&son.getIscp().intValue()==1){
    				try {
    					
    					String n = son.getBlankRemarkA()==null||("").equals(son.getBlankRemarkA())?"0":son.getBlankRemarkA();
        				n17.setCellValue(Integer.parseInt(n));
    				} catch (Exception e) {
    					n17.setCellValue(son.getBlankRemarkA());
					}
    			}else{
                    if(null!=son.getBlankRemark()&&son.getBlankRemark().contains("库存")){
                    	
    				}else{
    					try {
    						String n = son.getBlankWeight()==null||("").equals(son.getBlankWeight())?"0":son.getBlankWeight();
    						n17.setCellValue(Integer.parseInt(n));
						} catch (Exception e) {
							// TODO: handle exception
						}
    				}
    			}
    			Cell n18 = row.getCell(18);
    			if(n18==null){
    				n18=row.createCell(18);
    			}
    			if(null != son.getQty()){
    				n18.setCellValue(son.getQty());
    			}
    			Cell n19 = row.getCell(19);
    			if(n19==null){
    				n19=row.createCell(19);
    			}
    			try {
    				if(null != son.getIscp()&&son.getIscp().intValue()==1){
    					if(son.getBlankRemarkA()!=null&&!("").equals(son.getBlankRemarkA())&&son.getBlankRemarkA().contains("*")){
    						String[] ss = son.getBlankRemarkA().split("[*]");
    						if(ss.length==2){
    							BigDecimal x = new BigDecimal(ss[0]).multiply(new BigDecimal(ss[1]));
    							n19.setCellValue(x.doubleValue());
    						}
    					}else{
    						n19.setCellValue(0);
    					}
    				}else{
    					BigDecimal total = new BigDecimal(son.getBlankWeight()).multiply(new BigDecimal(son.getQty()));
            			n19.setCellValue(total.doubleValue());
    				}
    			} catch (Exception e) {
				}
    			Cell n20 = row.getCell(20);
    			if(n20==null){
    				n20=row.createCell(20);
    			}
    			n20.setCellValue(son.getSupplier());
    			Cell n21 = row.getCell(21);
    			if(n21==null){
    				n21=row.createCell(21);
    			}
    			n21.setCellValue(son.getIgnotNum());
    			Cell n22 = row.getCell(22);
    			if(n22==null){
    				n22=row.createCell(22);
    			}
    			n22.setCellValue(son.getHeatNum());
    			Cell n23 = row.getCell(23);
    			if(n23==null){
    				n23=row.createCell(23);
    			}
    			n23.setCellValue(son.getBlankRemark());
    		    y++;
    		}
    		
    		try {
    			this.setResponseHeader(response, uuid+".xls");
    			OutputStream os = response.getOutputStream();
    			wb.write(os);
    			os.flush();
    			os.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    }
    
    @RequestMapping(value="/exportForging")
    public void exportForging(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "ids", required = false) List<Long> ids) throws Exception{
    		String uuid = StringUtil.getUUID();
    		String realPath = request.getSession().getServletContext().getRealPath("/"); 
    		File file = new File(realPath+"excel/");
           if(!file.exists()){
        	   file.mkdir();
           }
           Workbook wb = ExcelUtil.writeExcel("xls", realPath+"excel/锻造施工单.xls");
    		Sheet sheet = wb.getSheetAt(0);
    		//sheet.setForceFormulaRecalculation(true);
    		List<Product> list = (List<Product>) productJPADao.select(ids,"2500T压机");
    		int y = 3;
    		for(Product son : list){
    			if(null!=son.getIscp()&&son.getIscp()==1){
    				if(!StringUtil.isNullOrSpace(son.getForgingWorkerA())&&("2500T压机").equals(son.getForgingWorkerA())){
    				}else{
    					continue;
    				}
    			}else{
    				if(!StringUtil.isNullOrSpace(son.getForgingWorker())&&("2500T压机").equals(son.getForgingWorker())){
    				}else{
    					continue;
    				}
    			}
    			Row row = sheet.getRow(y);
    			if(null == row){
    				row = sheet.createRow(y);
    			}
    			Cell n1 = row.getCell(1);
    			if(n1==null){
    				n1 = row.createCell(1);
    			}
    			if(null != son.getIscp()&&son.getIscp().intValue()==1){
    				n1.setCellValue(son.getSerialNum()+"P");
    			}else{
    				n1.setCellValue(son.getSerialNum());    			
    			}
    			Cell n2 = row.getCell(2);
    			if(n2==null){
    				n2 = row.createCell(2);
    			}
    				n2.setCellValue(son.getOrderNum());
    			Cell n5 = row.getCell(5);
    			if(n5==null){
    				n5 = row.createCell(5);
    			}
    			n5.setCellValue(son.getSizeA());
    			Cell n6 = row.getCell(6);
    			if(n6==null){
    				n6 = row.createCell(6);
    			}
    			n6.setCellValue(son.getSizeB());
    			Cell n7 = row.getCell(7);
    			if(n7==null){
    				n7=row.createCell(7);
    			}
    			n7.setCellValue(son.getSizeC());
    			Cell n8 = row.getCell(8);
    			if(n8==null){
    				n8=row.createCell(8);
    			}
    			n8.setCellValue(son.getSizeD());
    			Cell n9 = row.getCell(9);
    			if(n9==null){
    				n9=row.createCell(9);
    			}
    			n9.setCellValue(son.getSizeE());
    			Cell n10 = row.getCell(10);
    			if(n10==null){
    				n10=row.createCell(10);
    			}
    			n10.setCellValue(son.getSizeF());
    			Cell n11 = row.getCell(11);
    			if(n11==null){
    				n11=row.createCell(11);
    			}
    			n11.setCellValue(son.getSizeG());
    			Cell n12 = row.getCell(12);
    			if(n12==null){
    				n12=row.createCell(12);
    			}
    			n12.setCellValue(son.getSizeH());
    			Cell n13 = row.getCell(13);
    			if(n13==null){
    				n13=row.createCell(13);
    			}
    			n13.setCellValue(son.getMaterial());
    			Cell n14 = row.getCell(14);
    			if(n14==null){
    				n14=row.createCell(14);
    			}
    			n14.setCellValue(son.getGrade());
    			
    			Cell n17 = row.getCell(17);
    			if(n17==null){
    				n17=row.createCell(17);
    			}
    			if(son.getForgingSize()!=null&&!("").equals(son.getForgingSize())){
    				n17.setCellType(Cell.CELL_TYPE_STRING);
    				n17.setCellValue(son.getForgingSize());
    			}
    			Cell n18 = row.getCell(18);
    			if(n18==null){
    				n18=row.createCell(18);
    			}
    			if(null != son.getIscp()&&son.getIscp().intValue()==1){
    				n18.setCellValue(son.getBlankRemarkA());
    			}else{
    				n18.setCellValue(son.getBlankWeight());
    			}
    			Cell n19 = row.getCell(19);
    			if(n19==null){
    				n19=row.createCell(19);
    			}
    			if(null == son.getIscp()){
    				if(null != son.getQty()){
        				n19.setCellValue(son.getQty().toString());
        			}
    			}else{
    				n19.setCellValue("");
    			}
    			Cell n20 = row.getCell(20);
    			if(n20==null){
    				n20=row.createCell(20);
    			}
    			try {
    				if(null != son.getIscp()&&son.getIscp().intValue()==1){
    					if(StringUtil.isNumber(son.getBlankRemarkA())){
    						BigDecimal total = new BigDecimal(son.getBlankRemarkA()).multiply(new BigDecimal(son.getQty()));
            				n20.setCellValue(total.toString());
    					}else{
    						n20.setCellValue("");
    					}
    				}else{
    					BigDecimal total = new BigDecimal(son.getBlankWeight()).multiply(new BigDecimal(son.getQty()));
        				n20.setCellValue(total.toString());
    				}
    			} catch (Exception e) {
				}
    			Cell n21 = row.getCell(21);
    			if(n21==null){
    				n21=row.createCell(21);
    			}
    			n21.setCellValue(son.getHeatNum());
    			
    		    y++;
    		}
    		
    		List<Product> list2 = (List<Product>) productJPADao.select(ids,"5T锤");
    		y = 36;
    		for(Product son : list2){
    			if(null!=son.getIscp()&&son.getIscp()==1){
    				if(!StringUtil.isNullOrSpace(son.getForgingWorkerA())&&("5T锤").equals(son.getForgingWorkerA())){
    				}else{
    					continue;
    				}
    			}else{
    				if(!StringUtil.isNullOrSpace(son.getForgingWorker())&&("5T锤").equals(son.getForgingWorker())){
    				}else{
    					continue;
    				}
    			}
    			Row row = sheet.getRow(y);
    			Cell n1 = row.getCell(1);
    			if(n1==null){
    				n1 = row.createCell(1);
    			}
    			if(null != son.getIscp()&&son.getIscp().intValue()==1){
    				n1.setCellValue(son.getSerialNum()+"P");
    			}else{
    				n1.setCellValue(son.getSerialNum());    			
    			}
    			Cell n2 = row.getCell(2);
    			if(n2==null){
    				n2 = row.createCell(2);
    			}
    				n2.setCellValue(son.getOrderNum());
    			Cell n5 = row.getCell(5);
    			if(n5==null){
    				n5 = row.createCell(5);
    			}
    			n5.setCellValue(son.getSizeA());
    			Cell n6 = row.getCell(6);
    			if(n6==null){
    				n6 = row.createCell(6);
    			}
    			n6.setCellValue(son.getSizeB());
    			Cell n7 = row.getCell(7);
    			if(n7==null){
    				n7=row.createCell(7);
    			}
    			n7.setCellValue(son.getSizeC());
    			Cell n8 = row.getCell(8);
    			if(n8==null){
    				n8=row.createCell(8);
    			}
    			n8.setCellValue(son.getSizeD());
    			Cell n9 = row.getCell(9);
    			if(n9==null){
    				n9=row.createCell(9);
    			}
    			n9.setCellValue(son.getSizeE());
    			Cell n10 = row.getCell(10);
    			if(n10==null){
    				n10=row.createCell(10);
    			}
    			n10.setCellValue(son.getSizeF());
    			Cell n11 = row.getCell(11);
    			if(n11==null){
    				n11=row.createCell(11);
    			}
    			n11.setCellValue(son.getSizeG());
    			Cell n12 = row.getCell(12);
    			if(n12==null){
    				n12=row.createCell(12);
    			}
    			n12.setCellValue(son.getSizeH());
    			Cell n13 = row.getCell(13);
    			if(n13==null){
    				n13=row.createCell(13);
    			}
    			n13.setCellValue(son.getMaterial());
    			Cell n14 = row.getCell(14);
    			if(n14==null){
    				n14=row.createCell(14);
    			}
    			n14.setCellValue(son.getGrade());
    			
    			Cell n18 = row.getCell(18);
    			if(n18==null){
    				n18=row.createCell(18);
    			}
    			if(null != son.getIscp()&&son.getIscp().intValue()==1){
    			    n18.setCellValue(son.getBlankRemarkA());
    			}else{
    				n18.setCellValue(son.getBlankWeight());
    			}
    			Cell n19 = row.getCell(19);
    			if(n19==null){
    				n19=row.createCell(19);
    			}
    			if (null!=son.getQty()) {
    				n19.setCellValue(son.getQty().toString());
				}
    			Cell n20 = row.getCell(20);
    			if(n20==null){
    				n20=row.createCell(20);
    			}
    			try {
    				if(null != son.getIscp()&&son.getIscp().intValue()==1){
    					if(StringUtil.isNumber(son.getBlankRemarkA())){
    						BigDecimal total = new BigDecimal(son.getBlankRemarkA()).multiply(new BigDecimal(son.getQty()));
            				n20.setCellValue(total.toString());
    					}else{
    						n20.setCellValue("");
    					}
    				}else{
    					BigDecimal total = new BigDecimal(son.getBlankWeight()).multiply(new BigDecimal(son.getQty()));
        				n20.setCellValue(total.toString());
    				}
    			} catch (Exception e) {
				}
    			Cell n21 = row.getCell(21);
    			if(n21==null){
    				n21=row.createCell(21);
    			}
    			n21.setCellValue(son.getHeatNum());
    			
    		    y++;
    		}
    		
    		List<Product> list3 = (List<Product>) productJPADao.select(ids,"8T锤");
    		y = 70;
    		for(Product son : list3){
    			if(null!=son.getIscp()&&son.getIscp()==1){
    				if(!StringUtil.isNullOrSpace(son.getForgingWorkerA())&&("8T锤").equals(son.getForgingWorkerA())){
    				}else{
    					continue;
    				}
    			}else{
    				if(!StringUtil.isNullOrSpace(son.getForgingWorker())&&("8T锤").equals(son.getForgingWorker())){
    				}else{
    					continue;
    				}
    			}
    			Row row = sheet.getRow(y);
    			Cell n1 = row.getCell(1);
    			if(n1==null){
    				n1 = row.createCell(1);
    			}
    			if(null != son.getIscp()&&son.getIscp().intValue()==1){
    				n1.setCellValue(son.getSerialNum()+"P");
    			}else{
    				n1.setCellValue(son.getSerialNum());    			
    			}
    			Cell n2 = row.getCell(2);
    			if(n2==null){
    				n2 = row.createCell(2);
    			}
    				n2.setCellValue(son.getOrderNum());
    			Cell n5 = row.getCell(5);
    			if(n5==null){
    				n5 = row.createCell(5);
    			}
    			n5.setCellValue(son.getSizeA());
    			Cell n6 = row.getCell(6);
    			if(n6==null){
    				n6 = row.createCell(6);
    			}
    			n6.setCellValue(son.getSizeB());
    			Cell n7 = row.getCell(7);
    			if(n7==null){
    				n7=row.createCell(7);
    			}
    			n7.setCellValue(son.getSizeC());
    			Cell n8 = row.getCell(8);
    			if(n8==null){
    				n8=row.createCell(8);
    			}
    			n8.setCellValue(son.getSizeD());
    			Cell n9 = row.getCell(9);
    			if(n9==null){
    				n9=row.createCell(9);
    			}
    			n9.setCellValue(son.getSizeE());
    			Cell n10 = row.getCell(10);
    			if(n10==null){
    				n10=row.createCell(10);
    			}
    			n10.setCellValue(son.getSizeF());
    			Cell n11 = row.getCell(11);
    			if(n11==null){
    				n11=row.createCell(11);
    			}
    			n11.setCellValue(son.getSizeG());
    			Cell n12 = row.getCell(12);
    			if(n12==null){
    				n12=row.createCell(12);
    			}
    			n12.setCellValue(son.getSizeH());
    			Cell n13 = row.getCell(13);
    			if(n13==null){
    				n13=row.createCell(13);
    			}
    			n13.setCellValue(son.getMaterial());
    			Cell n14 = row.getCell(14);
    			if(n14==null){
    				n14=row.createCell(14);
    			}
    			n14.setCellValue(son.getGrade());
    			
    			Cell n18 = row.getCell(18);
    			if(n18==null){
    				n18=row.createCell(18);
    			}
    			if(null != son.getIscp()&&son.getIscp().intValue()==1){
    			    n18.setCellValue(son.getBlankRemarkA());
    			}else{
    				n18.setCellValue(son.getBlankWeight());
    			}
    			Cell n19 = row.getCell(19);
    			if(n19==null){
    				n19=row.createCell(19);
    			}
    			if (null!=son.getQty()) {
    				n19.setCellValue(son.getQty().toString());
				}
    			Cell n20 = row.getCell(20);
    			if(n20==null){
    				n20=row.createCell(20);
    			}
    			try {
    				if(null != son.getIscp()&&son.getIscp().intValue()==1){
    					if(StringUtil.isNumber(son.getBlankRemarkA())){
    						BigDecimal total = new BigDecimal(son.getBlankRemarkA()).multiply(new BigDecimal(son.getQty()));
            				n20.setCellValue(total.toString());
    					}else{
    						n20.setCellValue("");
    					}
    				}else{
    					BigDecimal total = new BigDecimal(son.getBlankWeight()).multiply(new BigDecimal(son.getQty()));
        				n20.setCellValue(total.toString());
    				}
    			} catch (Exception e) {
				}
    			Cell n21 = row.getCell(21);
    			if(n21==null){
    				n21=row.createCell(21);
    			}
    			n21.setCellValue(son.getHeatNum());
    			
    		    y++;
    		}
    		
    		List<Product> list4 = (List<Product>) productJPADao.select(ids,"小锤");
    		y = 103;
    		for(Product son : list4){
    			if(null!=son.getIscp()&&son.getIscp()==1){
    				if(!StringUtil.isNullOrSpace(son.getForgingWorkerA())&&("小锤").equals(son.getForgingWorkerA())){
    				}else{
    					continue;
    				}
    			}else{
    				if(!StringUtil.isNullOrSpace(son.getForgingWorker())&&("小锤").equals(son.getForgingWorker())){
    				}else{
    					continue;
    				}
    			}
    			Row row = sheet.getRow(y);
    			Cell n1 = row.getCell(1);
    			if(n1==null){
    				n1 = row.createCell(1);
    			}
    			if(null != son.getIscp()&&son.getIscp().intValue()==1){
    				n1.setCellValue(son.getSerialNum()+"P");
    			}else{
    				n1.setCellValue(son.getSerialNum());    			
    			}
    			Cell n2 = row.getCell(2);
    			if(n2==null){
    				n2 = row.createCell(2);
    			}
    				n2.setCellValue(son.getOrderNum());
    			Cell n5 = row.getCell(5);
    			if(n5==null){
    				n5 = row.createCell(5);
    			}
    			n5.setCellValue(son.getSizeA());
    			Cell n6 = row.getCell(6);
    			if(n6==null){
    				n6 = row.createCell(6);
    			}
    			n6.setCellValue(son.getSizeB());
    			Cell n7 = row.getCell(7);
    			if(n7==null){
    				n7=row.createCell(7);
    			}
    			n7.setCellValue(son.getSizeC());
    			Cell n8 = row.getCell(8);
    			if(n8==null){
    				n8=row.createCell(8);
    			}
    			n8.setCellValue(son.getSizeD());
    			Cell n9 = row.getCell(9);
    			if(n9==null){
    				n9=row.createCell(9);
    			}
    			n9.setCellValue(son.getSizeE());
    			Cell n10 = row.getCell(10);
    			if(n10==null){
    				n10=row.createCell(10);
    			}
    			n10.setCellValue(son.getSizeF());
    			Cell n11 = row.getCell(11);
    			if(n11==null){
    				n11=row.createCell(11);
    			}
    			n11.setCellValue(son.getSizeG());
    			Cell n12 = row.getCell(12);
    			if(n12==null){
    				n12=row.createCell(12);
    			}
    			n12.setCellValue(son.getSizeH());
    			Cell n13 = row.getCell(13);
    			if(n13==null){
    				n13=row.createCell(13);
    			}
    			n13.setCellValue(son.getMaterial());
    			Cell n14 = row.getCell(14);
    			if(n14==null){
    				n14=row.createCell(14);
    			}
    			n14.setCellValue(son.getGrade());
    			
    			Cell n18 = row.getCell(18);
    			if(n18==null){
    				n18=row.createCell(18);
    			}
    			if(null != son.getIscp()&&son.getIscp().intValue()==1){
    			    n18.setCellValue(son.getBlankRemarkA());
    			}else{
    				n18.setCellValue(son.getBlankWeight());
    			}
    			Cell n19 = row.getCell(19);
    			if(n19==null){
    				n19=row.createCell(19);
    			}
    			if (null!=son.getQty()) {
    				n19.setCellValue(son.getQty().toString());
				}
    			Cell n20 = row.getCell(20);
    			if(n20==null){
    				n20=row.createCell(20);
    			}
    			try {
    				if(null != son.getIscp()&&son.getIscp().intValue()==1){
    					if(StringUtil.isNumber(son.getBlankRemarkA())){
    						BigDecimal total = new BigDecimal(son.getBlankRemarkA()).multiply(new BigDecimal(son.getQty()));
            				n20.setCellValue(total.toString());
    					}else{
    						n20.setCellValue("");
    					}
    				}else{
    					BigDecimal total = new BigDecimal(son.getBlankWeight()).multiply(new BigDecimal(son.getQty()));
        				n20.setCellValue(total.toString());
    				}
    			} catch (Exception e) {
				}
    			Cell n21 = row.getCell(21);
    			if(n21==null){
    				n21=row.createCell(21);
    			}
    			n21.setCellValue(son.getHeatNum());
    			
    		    y++;
    		}
    		
    		try {
    			
    			this.setResponseHeader(response, uuid+".xls");
    			OutputStream os = response.getOutputStream();
    			wb.write(os);
    			os.flush();
    			os.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    }
    
    //导出锻造外协施工单
    @RequestMapping(value="/exportForgingOut")
    public void exportForgingOut(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "ids", required = false) List<Long> ids) throws Exception{
    		String uuid = StringUtil.getUUID();
    		String realPath = request.getSession().getServletContext().getRealPath("/"); 
    		File file = new File(realPath+"excel/");
           if(!file.exists()){
        	   file.mkdir();
           }
            Workbook wb = ExcelUtil.writeExcel("xls", realPath+"excel/外协锻造样表.xls");
    		Sheet sheet = wb.getSheetAt(0);
    		sheet.setForceFormulaRecalculation(true);
    		List<Product> list = (List<Product>) productJPADao.findAllOrder(ids);
    		int y = 5;
    		Row row1 = sheet.getRow(1);
    		row1.getCell(3).setCellValue(list.get(0).getForgingRemark());
    		for(Product son : list){
    			Row row = sheet.getRow(y);
    			if(null == row){
    				row = sheet.createRow(y);
    			}
    			Cell n2 = row.getCell(2);
    			if(n2==null){
    				n2 = row.createCell(2);
    			}
    			n2.setCellValue(son.getSerialNum());    			
    			Cell n3 = row.getCell(3);
    			if(n3==null){
    				n3 = row.createCell(3);
    			}
    			n3.setCellValue(son.getOrderNum());
    			Cell n4 = row.getCell(4);
    			if(n4==null){
    				n4 = row.createCell(4);
    			}
    			n4.setCellValue(son.getDwgNum());
    			Cell n5 = row.getCell(5);
    			if(n5==null){
    				n5 = row.createCell(5);
    			}
    			n5.setCellValue(son.getDescription());
    			Cell n6 = row.getCell(6);
    			if(n6==null){
    				n6 = row.createCell(6);
    			}
    			n6.setCellValue(son.getSizeA());
    			Cell n7 = row.getCell(7);
    			if(n7==null){
    				n7=row.createCell(7);
    			}
    			n7.setCellValue(son.getSizeB());
    			Cell n8 = row.getCell(8);
    			if(n8==null){
    				n8=row.createCell(8);
    			}
    			n8.setCellValue(son.getSizeC());
    			Cell n9 = row.getCell(9);
    			if(n9==null){
    				n9=row.createCell(9);
    			}
    			n9.setCellValue(son.getSizeD());
    			Cell n10 = row.getCell(10);
    			if(n10==null){
    				n10=row.createCell(10);
    			}
    			n10.setCellValue(son.getSizeE());
    			Cell n11 = row.getCell(11);
    			if(n11==null){
    				n11=row.createCell(11);
    			}
    			n11.setCellValue(son.getSizeF());
    			Cell n12 = row.getCell(12);
    			if(n12==null){
    				n12=row.createCell(12);
    			}
    			n12.setCellValue(son.getSizeG());
    			Cell n13 = row.getCell(13);
    			if(n13==null){
    				n13=row.createCell(13);
    			}
    			n13.setCellValue(son.getSizeH());
    			Cell n14 = row.getCell(14);
    			if(n14==null){
    				n14=row.createCell(14);
    			}
    			n14.setCellValue(son.getMaterial());
    			Cell n15 = row.getCell(15);
    			if(n15==null){
    				n15=row.createCell(15);
    			}
    			n15.setCellValue(son.getGrade());
    			Cell n18 = row.getCell(18);
    			if(n18==null){
    				n18=row.createCell(18);
    			}
    			if(son.getForgingSize()!=null&&!("").equals(son.getForgingSize())){
    				n18.setCellType(Cell.CELL_TYPE_STRING);
    				n18.setCellValue(son.getForgingSize());
    			}
    			Cell n19 = row.getCell(19);
    			if(n19==null){
    				n19=row.createCell(19);
    			}
    			if(!(son.getBlankWeight()).equals(son.getActUnitWeight())){
    				CellStyle style = wb.createCellStyle();
        			style.cloneStyleFrom(n19.getCellStyle());
            		style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
            		n19.setCellStyle(style);
    			}
    			n19.setCellValue(son.getActUnitWeight());
    			Cell n20 = row.getCell(20);
    			if(n20==null){
    				n20=row.createCell(20);
    			}
    			n20.setCellValue(son.getQty());
    			Cell n21 = row.getCell(21);
    			if(n21==null){
    				n21=row.createCell(21);
    			}
    			try {
    				BigDecimal total = new BigDecimal(son.getActUnitWeight()).multiply(new BigDecimal(son.getQty()));
    				n21.setCellValue(total.doubleValue());
    			} catch (Exception e) {
				}
    			Cell n22 = row.getCell(22);
    			if(n22==null){
    				n22=row.createCell(22);
    			}
    			n22.setCellValue(son.getHeatNum());
    		    y++;
    		}
    		
    		try {
    			this.setResponseHeader(response, uuid+".xls");
    			OutputStream os = response.getOutputStream();
    			wb.write(os);
    			os.flush();
    			os.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    }
    
    //导出滚动表
    @RequestMapping(value="/exportAllInfos")
    public void exportAllInfos(HttpServletRequest request,HttpServletResponse response) throws Exception{
    		String uuid = StringUtil.getUUID();
    		String realPath = request.getSession().getServletContext().getRealPath("/"); 
    		File file = new File(realPath+"excel/");
           if(!file.exists()){
        	   file.mkdir();
           }
            Workbook wb = ExcelUtil.writeExcel("xlsx", realPath+"excel/滚动表.xlsx");
    		Sheet sheet = wb.getSheetAt(0);
    		String sql = "select a.*,c.chn_name from product a left join process_temp_son b on a.temp_son_id=b.id left join sys_process c on b.process_id=c.id where a.state in (3,4,5,6,7,8,10,11,12,13) order by a.code,a.ordera,a.orderb,a.orderd,a.orderc,a.ordere ";
    		List<Map<String, Object>> list =  productDao.getSqlQuery(sql);
    		productService.exportAllInfos(sheet, list);
    		try {
    			this.setResponseHeader(response, uuid+".xls");
    			OutputStream os = response.getOutputStream();
    			wb.write(os);
    			os.flush();
    			os.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    }
    
    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @RequestMapping(value = "/toMessage")
    public String toMessage(HttpSession session,Model model,@RequestParam(value = "ids", required = false) List<Long> ids) {
    	Long userId = (Long) session.getAttribute("userId");
    	List<Product> listp = (List<Product>) productJPADao.findAll(ids);
    	List<User> list = userDao.findAll();
    	List<User> list2 = new ArrayList<User>();
    	for(User u : list){
    		if(userId.intValue() != u.getId().intValue()){
    			list2.add(u);
    		}
    	}
    	StringBuffer message = new StringBuffer();
    	for(Product p : listp){
    		message.append(p.getSerialNum()).append("-").append(p.getOrderNum()).append("    ");
    	}
    	model.addAttribute("list", list2);
    	model.addAttribute("message", message);
        return "main/user/toMessage";
    }
    
    
    //设置工序
    @RequestMapping(value = "/toProcess")
    public String toProcess(HttpSession session,Model model,String ids) {
//    	List<Product> list = productJPADao.findAllOrder(ids);
//    	for(Product product : list){
//    		ProcessTempSon son = new ProcessTempSon();
//        	if(null != product.getNextTempSonId()){
//        		if(null != son.getParentId()&&son.getParentId().intValue()!=0){
//        			son = tempSonDao.findOne(product.getNextTempSonId());
//        		}else{
//        			son = tempSonDao.findOne(product.getTempSonId());
//        		}
//        	}else{
//        		son = tempSonDao.findOne(product.getTempSonId());
//        	}
//    	}
//    	Product product = productJPADao.findOne(id);sss
    	
//    	List<ProcessTempSon> list = tempSonDao.findByParentId(son.getParentId());
//    	for(ProcessTempSon s : list){
//    		SysProcess process = sysProcessDao.findOne(s.getProcessId());
//    		s.setProcName(process.getChnName());
//    	}
//    	model.addAttribute("list", list);
    	model.addAttribute("ids", ids);
        return "main/product/toProcess";
    }
    
    @RequestMapping(value = "/doProcess")
    @ResponseBody
    public String doProcess(Long processId,@RequestParam(value = "ids", required = false) List<Long> ids,Integer state,HttpSession session) {
    	List<Product> list = productJPADao.findAllOrder(ids);
    	String userChnName = (String) session.getAttribute("userChnName");
    	SysProcess sysp = sysProcessDao.findOne(processId);
    	for(Product product : list){
    		ProcessTempSon son = new ProcessTempSon();
        	if(null != product.getNextTempSonId()){
        		if(null != son.getParentId()&&son.getParentId().intValue()!=0){
        			son = tempSonDao.findOne(product.getNextTempSonId());
        		}else{
        			son = tempSonDao.findOne(product.getTempSonId());
        		}
        	}else{
        		son = tempSonDao.findOne(product.getTempSonId());
        	}
        	ProcessTempSon son2 = tempSonDao.findByParentIdAndProcessId(son.getParentId(), processId);
    		
    		if(son2==null){
    			return "fail";
    		}
        	
    	}
    	for(Product product : list){
    		OperateRecord operRecord = new OperateRecord();
        	operRecord.setOperateTime(DateUtil.getCurrentTime());
        	operRecord.setProductId(product.getId());
        	operRecord.setUsername(userChnName);
        	
    		ProcessTempSon son = new ProcessTempSon();
        	if(null != product.getNextTempSonId()){
        		if(null != son.getParentId()&&son.getParentId().intValue()!=0){
        			son = tempSonDao.findOne(product.getNextTempSonId());
        		}else{
        			son = tempSonDao.findOne(product.getTempSonId());
        		}
        	}else{
        		son = tempSonDao.findOne(product.getTempSonId());
        	}
        	ProcessTempSon son2 = tempSonDao.findByParentIdAndProcessId(son.getParentId(), processId);
    		product.setState(state);
    		if(son2!=null){
    			if(state.intValue()!=6){
    				product.setIsback(2);
    				product.setTempSonId(son2.getId());
    				operRecord.setElementName("修改工序到"+sysp.getChnName());
    				product.setLastDate(DateUtil.getCurrentymd());
	        		operateRecordDao.save(operRecord);
    			}else{
    				if(null != sysp){
    	        		operRecord.setElementName("修改工序到待入库");
    	        		operateRecordDao.save(operRecord);
    	        	}
    			}
            	productJPADao.save(product);
    		}
        	
    	}
        return "success";
    }
    
    //转入下料
    @RequestMapping(value = "/toBlank")
    @ResponseBody
    public String toBlank(@RequestParam(value = "ids", required = false) List<Long> ids,HttpSession session) {
    	List<Product> list = (List<Product>) productJPADao.findAll(ids);
    	String userChnName = (String) session.getAttribute("userChnName");
    	for(Product p : list){
    		if(null != p.getIscp()&&p.getIscp().intValue()==1){
				p.setPrevProcess("热处理出坯");
			}else{
				p.setPrevProcess("锻后热处理");
			}
    		ProcessTempSon son = new ProcessTempSon();
    		if(p.getNextTempSonId()!=null){
    			son = tempSonDao.findOne(p.getNextTempSonId());
    		}else{
    			return "fail";
    		}
    		OperateRecord operRecord = new OperateRecord();
        	operRecord.setOperateTime(DateUtil.getCurrentTime());
        	operRecord.setProductId(p.getId());
        	operRecord.setUsername(userChnName);
        	operRecord.setElementName(p.getPrevProcess()+"转入下料");
        	operateRecordDao.save(operRecord);
    		ProcessTempSon son2 = tempSonDao.findByParentIdAndProcessId(son.getParentId(), 4L);
    		if(null != son2){
    			p.setTempSonId(son2.getId());
    			p.setIscp(null);
    			p.setNextTempSonId(null);
    			productJPADao.save(p);
    		}
    	}
        return "success";
    }
    
    //批量编辑
    @RequestMapping(value = "/toBatchEdit")
    public String toBatchEdit(Model model,String ids,Integer type,@RequestParam(value = "ids", required = false) List<Long> idsb) {
    	List<Product> listp = (List<Product>) productJPADao.findAll(idsb);
    	model.addAttribute("ids", ids);
    	model.addAttribute("heatNum", listp.get(0).getHeatNum());
    	model.addAttribute("state", listp.get(0).getState());
    	model.addAttribute("type", type);
        return "main/product/batchEdit";
    }
    
    //批量编辑
    @RequestMapping(value = "/doBatchEdit")
    @ResponseBody
    public String doBatchEdit(Product product,@RequestParam(value = "ids", required = false) List<Long> ids,Integer type,
    		Integer a,Integer b,Integer c,Integer d,Integer e,Integer f,Integer g,Integer h,HttpSession session) {
    	List<Product> listp = (List<Product>) productJPADao.findAll(ids);
    	String userChnName = (String) session.getAttribute("userChnName");
    	if(type.intValue()==1){//下料
    		for(Product p : listp){
        		if(a!=null&&a.intValue()==1){
        			p.setHlSize(product.getHlSize());
        		}
        		if(b!=null&&b.intValue()==1){
        			p.setSupplier(product.getSupplier());
        		}
        		if(c!=null&&c.intValue()==1){
        			p.setIgnotNum(product.getIgnotNum());
        		}
        		if(d!=null&&d.intValue()==1){
        			p.setBlankDateA(product.getBlankDateA());
        		}
        		if(e!=null&&e.intValue()==1){
        			p.setBlankRemarkA(product.getBlankRemarkA());
        		}
        		if(f!=null&&f.intValue()==1){
        			p.setBlankDate(product.getBlankDate());
        		}
        		if(g!=null&&g.intValue()==1){
        			p.setBlankRemark(product.getBlankRemark());
        		}
        		if(h!=null&&h.intValue()==1){
        			p.setHeatNum(product.getHeatNum());
        		}
        		productJPADao.save(p);
        		
//        		OperateRecord operRecord = new OperateRecord();
//            	operRecord.setOperateTime(DateUtil.getCurrentTime());
//            	operRecord.setProductId(p.getId());
//            	operRecord.setUsername(userChnName);
//            	operRecord.setElementName("批量编辑");
//            	operateRecordDao.save(operRecord);
        	}
    	}
    	if(type.intValue()==2){
    		for(Product p : listp){
    			if(a!=null&&a.intValue()==1){
        			p.setForgingWorkerA(product.getForgingWorkerA());
        		}
    			if(b!=null&&b.intValue()==1){
        			p.setForgingInDateA(product.getForgingInDateA());
        		}
        		if(c!=null&&c.intValue()==1){
        			p.setForgingOutDateA(product.getForgingOutDateA());
        		}
        		if(d!=null&&d.intValue()==1){
        			p.setForgingWorker(product.getForgingWorker());
        		}
        		if(e!=null&&e.intValue()==1){
        			p.setForgingInDate(product.getForgingInDate());
        		}
        		if(f!=null&&f.intValue()==1){
        			p.setForgingOutDate(product.getForgingOutDate());
        		}
        		productJPADao.save(p);
    		}
    	}
    	if(type.intValue()==3){
    		for(Product p : listp){
    			if(a!=null&&a.intValue()==1){
        			p.setHeatTreatInDateA(product.getHeatTreatInDateA());
        		}
        		if(b!=null&&b.intValue()==1){
        			p.setHeatTreatOutDateA(product.getHeatTreatOutDateA());
        		}
        		if(c!=null&&c.intValue()==1){
        			p.setAfterForgingWorker(product.getAfterForgingWorker());
        		}
        		if(d!=null&&d.intValue()==1){
        			p.setAfterForgingInDate(product.getAfterForgingInDate());
        		}
        		if(e!=null&&e.intValue()==1){
        			p.setAfterForgingOut(product.getAfterForgingOut());
        		}
        		productJPADao.save(p);
    		}
    	}
    	if(type.intValue()==4){
    		for(Product p : listp){
    			if(a!=null&&a.intValue()==1){
        			p.setRoughMachingWorker(product.getRoughMachingWorker());
        		}
        		if(c!=null&&c.intValue()==1){
        			p.setIspsa(product.getIspsa());
        		}
        		productJPADao.save(p);
    		}
    	}
    	if(type.intValue()==5){
    		for(Product p : listp){
    			if(a!=null&&a.intValue()==1){
        			p.setPerHeatTreatWorker(product.getPerHeatTreatWorker());
        		}
        		if(b!=null&&b.intValue()==1){
        			p.setPerHeatTreatInDate(product.getPerHeatTreatInDate());
        		}
        		if(c!=null&&c.intValue()==1){
        			p.setPerHeatTreatOutDate(product.getPerHeatTreatOutDate());
        		}
        		productJPADao.save(p);
    		}
    	}
    	if(type.intValue()==6){
    		for(Product p : listp){
    			if(a!=null&&a.intValue()==1){
        			p.setDeliveryWorker(product.getDeliveryWorker());
        		}
        		if(b!=null&&b.intValue()==1){
        			p.setIspsb(product.getIspsb());
        		}
        		productJPADao.save(p);
    		}
    	}
    	if(type.intValue()==7){
    		for(Product p : listp){
    			if(a!=null&&a.intValue()==1){
        			p.setFollowupWorker(product.getFollowupWorker());
        		}
        		if(b!=null&&b.intValue()==1){
        			p.setFollowupInDate(product.getFollowupInDate());
        		}
        		if(c!=null&&c.intValue()==1){
        			p.setFollowupOutDate(product.getFollowupOutDate());
        		}
        		productJPADao.save(p);
    		}
    	}
    	if(type.intValue()==8){
    		for(Product p : listp){
    			if(a!=null&&a.intValue()==1){
        			p.setHalfFinishMachingWorker(product.getHalfFinishMachingWorker());
        		}
        		if(b!=null&&b.intValue()==1){
        			p.setIspsc(product.getIspsc());
        		}
        		productJPADao.save(p);
    		}
    	}
    	if(type.intValue()==9){
    		for(Product p : listp){
    			if(a!=null&&a.intValue()==1){
        			p.setFinishMachingWorker(product.getFinishMachingWorker());
        		}
    			if(b!=null&&b.intValue()==1){
        			p.setFinishMachingWeight(product.getFinishMachingWeight());
        		}
        		if(c!=null&&c.intValue()==1){
        			p.setIspsd(product.getIspsd());
        		}
        		productJPADao.save(p);
    		}
    	}
    	if(type.intValue()==10){
    		for(Product p : listp){
    			if(a!=null&&a.intValue()==1){
        			p.setSampleInDate(product.getSampleInDate());
        		}
        		if(b!=null&&b.intValue()==1){
        			p.setSampleOutDate(product.getSampleOutDate());
        		}
        		productJPADao.save(p);
    		}
    	}
    	if(type.intValue()==11){
    		for(Product p : listp){
    			if(a!=null&&a.intValue()==1){
        			p.setForgingInDate(product.getForgingInDate());
        		}
        		if(b!=null&&b.intValue()==1){
        			p.setForgingRemark(product.getForgingRemark());
        		}
        		productJPADao.save(p);
    		}
    	}
    	if(type.intValue()==12){
    		for(Product p : listp){
    			if(a!=null&&a.intValue()==1){
        			p.setRoughMachingOutRemark(product.getRoughMachingOutRemark());
        		}
        		if(b!=null&&b.intValue()==1){
        			p.setRoughMachingOutCom(product.getRoughMachingOutCom());
        		}
        		productJPADao.save(p);
    		}
    	}
    	if(type.intValue()==13){
    		for(Product p : listp){
    			if(a!=null&&a.intValue()==1){
        			p.setDeliveryOutRemark(product.getDeliveryOutRemark());
        		}
        		if(b!=null&&b.intValue()==1){
        			p.setDeliveryOutCom(product.getDeliveryOutCom());
        		}
        		productJPADao.save(p);
    		}
    	}
    	if(type.intValue()==14){
    		for(Product p : listp){
    			if(a!=null&&a.intValue()==1){
        			p.setHalfFinishOutRemark(product.getHalfFinishOutRemark());
        		}
        		if(b!=null&&b.intValue()==1){
        			p.setHalfFinishOutCom(product.getHalfFinishOutCom());
        		}
        		productJPADao.save(p);
    		}
    	}
    	if(type.intValue()==15){
    		for(Product p : listp){
    			if(a!=null&&a.intValue()==1){
        			p.setFinishMachingOutRemark(product.getFinishMachingOutRemark());
        		}
        		if(b!=null&&b.intValue()==1){
        			p.setFinishMachingOutCom(product.getFinishMachingOutCom());
        		}
        		productJPADao.save(p);
    		}
    	}
    	if(type.intValue()==16){
    		for(Product p : listp){
        		p.setRoughMachingWeight(product.getRoughMachingWeight());
        		productJPADao.save(p);
    		}
    	}
        return "success";
    }
    
    //导出金工施工单
    @RequestMapping(value="/exportJG")
    public void exportJG(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "ids", required = false) List<Long> ids) throws Exception{
    		String uuid = StringUtil.getUUID();
    		String realPath = request.getSession().getServletContext().getRealPath("/"); 
    		File file = new File(realPath+"excel/");
           if(!file.exists()){
        	   file.mkdir();
           }
            Workbook wb = ExcelUtil.writeExcel("xlsx", realPath+"excel/金工施工单.xlsx");
    		Sheet sheet = wb.getSheetAt(0);
    		List<Product> list = (List<Product>) productJPADao.findAllOrder(ids);
    		int y = 3;
    		for(Product son : list){
    			Row row = sheet.getRow(y);
    			if(null == row){
    				row = sheet.createRow(y);
    			}
    			Cell n1 = row.getCell(0);
    			if(n1==null){
    				n1 = row.createCell(0);
    			}
    			n1.setCellValue(son.getSerialNum());
    			Cell n2 = row.getCell(1);
    			if(n2==null){
    				n2 = row.createCell(1);
    			}
    			n2.setCellValue(son.getOrderNum());
    			Cell n3 = row.getCell(3);
    			if(n3==null){
    				n3 = row.createCell(3);
    			}
    			n3.setCellValue(son.getSizeA());
    			Cell n4 = row.getCell(4);
    			if(n4==null){
    				n4 = row.createCell(4);
    			}
    			n4.setCellValue(son.getSizeB());
    			Cell n5 = row.getCell(5);
    			if(n5==null){
    				n5=row.createCell(5);
    			}
    			n5.setCellValue(son.getSizeC());
    			Cell n6 = row.getCell(6);
    			if(n6==null){
    				n6=row.createCell(6);
    			}
    			n6.setCellValue(son.getSizeD());
    			Cell n7 = row.getCell(7);
    			if(n7==null){
    				n7=row.createCell(7);
    			}
    			n7.setCellValue(son.getSizeE());
    			Cell n8 = row.getCell(8);
    			if(n8==null){
    				n8=row.createCell(8);
    			}
    			n8.setCellValue(son.getSizeF());
    			Cell n9 = row.getCell(9);
    			if(n9==null){
    				n9=row.createCell(9);
    			}
    			n9.setCellValue(son.getSizeG());
    			Cell n10 = row.getCell(10);
    			if(n10==null){
    				n10=row.createCell(10);
    			}
    			n10.setCellValue(son.getSizeH());
    			Cell n11 = row.getCell(11);
    			if(n11==null){
    				n11=row.createCell(11);
    			}
    			n11.setCellValue(son.getDescription());
    			Cell n12 = row.getCell(12);
    			if(n12==null){
    				n12=row.createCell(12);
    			}
    			if(son.getForgingSize()!=null&&!("").equals(son.getForgingSize())){
    				n12.setCellType(Cell.CELL_TYPE_STRING);
    				n12.setCellValue(son.getForgingSize());
    			}
    			Cell n13 = row.getCell(13);
    			if(n13==null){
    				n13=row.createCell(13);
    			}
    			n13.setCellValue(son.getMaterial());
    			Cell n14 = row.getCell(14);
    			if(n14==null){
    				n14=row.createCell(14);
    			}
    			n14.setCellValue(son.getGrade());
    			Cell n15 = row.getCell(15);
    			if(n15==null){
    				n15=row.createCell(15);
    			}
    			if(null != son.getQty()){
    				n15.setCellValue(son.getQty());
    			}
    			Cell n16 = row.getCell(16);
    			if(n16==null){
    				n16=row.createCell(16);
    			}
    			n16.setCellValue(son.getNdtRequire());
    		    y++;
    		}
    		
    		try {
    			this.setResponseHeader(response, uuid+".xlsx");
    			OutputStream os = response.getOutputStream();
    			wb.write(os);
    			os.flush();
    			os.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    }
    
    //导出二次热处理施工检验单
    @RequestMapping(value="/exportXn")
    public void exportXn(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "ids", required = false) List<Long> ids) throws Exception{
    		String uuid = StringUtil.getUUID();
    		String realPath = request.getSession().getServletContext().getRealPath("/"); 
    		File file = new File(realPath+"excel/");
           if(!file.exists()){
        	   file.mkdir();
           }
            Workbook wb = ExcelUtil.writeExcel("xls", realPath+"excel/性能热处理.xls");
    		Sheet sheet = wb.getSheetAt(0);
    		List<Product> list = (List<Product>) productJPADao.findAllOrder(ids);
    		int y = 4;
    		for(Product son : list){
    			Row row = sheet.getRow(y);
    			if(null == row){
    				row = sheet.createRow(y);
    			}
    			Cell n1 = row.getCell(1);
    			if(n1==null){
    				n1 = row.createCell(1);
    			}
    			n1.setCellValue(son.getSerialNum());
    			Cell n2 = row.getCell(2);
    			if(n2==null){
    				n2 = row.createCell(2);
    			}
    				n2.setCellValue(son.getOrderNum());
    			Cell n5 = row.getCell(5);
    			if(n5==null){
    				n5 = row.createCell(5);
    			}
    			n5.setCellValue(son.getSizeA());
    			Cell n6 = row.getCell(6);
    			if(n6==null){
    				n6 = row.createCell(6);
    			}
    			n6.setCellValue(son.getSizeB());
    			Cell n7 = row.getCell(7);
    			if(n7==null){
    				n7=row.createCell(7);
    			}
    			n7.setCellValue(son.getSizeC());
    			Cell n8 = row.getCell(8);
    			if(n8==null){
    				n8=row.createCell(8);
    			}
    			n8.setCellValue(son.getSizeD());
    			Cell n9 = row.getCell(9);
    			if(n9==null){
    				n9=row.createCell(9);
    			}
    			n9.setCellValue(son.getSizeE());
    			Cell n10 = row.getCell(10);
    			if(n10==null){
    				n10=row.createCell(10);
    			}
    			n10.setCellValue(son.getSizeF());
    			Cell n11 = row.getCell(11);
    			if(n11==null){
    				n11=row.createCell(11);
    			}
    			n11.setCellValue(son.getSizeG());
    			Cell n12 = row.getCell(12);
    			if(n12==null){
    				n12=row.createCell(12);
    			}
    			n12.setCellValue(son.getSizeH());
    			Cell n13 = row.getCell(13);
    			if(n13==null){
    				n13=row.createCell(13);
    			}
    			n13.setCellValue(son.getDescription());
    			Cell n14 = row.getCell(14);
    			if(n14==null){
    				n14=row.createCell(14);
    			}
    			if(null != son.getQty()){
    				n14.setCellValue(son.getQty());
    			}
    			Cell n15 = row.getCell(15);
    			if(n15==null){
    				n15=row.createCell(15);
    			}
    			n15.setCellValue(son.getMaterial());
    			Cell n16 = row.getCell(16);
    			if(n16==null){
    				n16=row.createCell(16);
    			}
    			n16.setCellValue(son.getHeatNum());
    			Cell n17 = row.getCell(17);
    			if(n17==null){
    				n17=row.createCell(17);
    			}
    			n17.setCellValue(son.getRoughMachingWeight());
    			Cell n32 = row.getCell(32);
    			if(n32==null){
    				n32=row.createCell(32);
    			}
    			n32.setCellValue(son.getIspsa());
    		    y++;
    		}
    		
    		try {
    			this.setResponseHeader(response, uuid+".xls");
    			OutputStream os = response.getOutputStream();
    			wb.write(os);
    			os.flush();
    			os.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    }
    
    
    //导出锻后热处理施工单
    @RequestMapping(value="/exportAfterForging")
    public void exportAfterForging(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "ids", required = false) List<Long> ids) throws Exception{
    		String uuid = StringUtil.getUUID();
    		String realPath = request.getSession().getServletContext().getRealPath("/"); 
    		File file = new File(realPath+"excel/");
           if(!file.exists()){
        	   file.mkdir();
           }
            Workbook wb = ExcelUtil.writeExcel("xls", realPath+"excel/锻后热处理施工单.xls");
    		Sheet sheet = wb.getSheetAt(0);
    		sheet.setForceFormulaRecalculation(true);
    		List<Product> list = (List<Product>) productJPADao.findAllOrder(ids);
    		int y = 4;
    		for(Product son : list){
    			Row row = sheet.getRow(y);
    			if(null == row){
    				row = sheet.createRow(y);
    			}
    			Cell n1 = row.getCell(1);
    			if(n1==null){
    				n1 = row.createCell(1);
    			}
    			if(null != son.getIscp()&&son.getIscp().intValue()==1){
    				n1.setCellValue(son.getSerialNum()+"P");
    			}else{
    				n1.setCellValue(son.getSerialNum());    			
    			}
    			Cell n2 = row.getCell(2);
    			if(n2==null){
    				n2 = row.createCell(2);
    			}
    				n2.setCellValue(son.getOrderNum());
    			Cell n3 = row.getCell(3);
        		if(n3==null){
        			n3 = row.createCell(3);
        		}
        			n3.setCellValue(son.getHeatNum());
    			Cell n5 = row.getCell(5);
    			if(n5==null){
    				n5 = row.createCell(5);
    			}
    			n5.setCellValue(son.getSizeA());
    			Cell n6 = row.getCell(6);
    			if(n6==null){
    				n6 = row.createCell(6);
    			}
    			n6.setCellValue(son.getSizeB());
    			Cell n7 = row.getCell(7);
    			if(n7==null){
    				n7=row.createCell(7);
    			}
    			n7.setCellValue(son.getSizeC());
    			Cell n8 = row.getCell(8);
    			if(n8==null){
    				n8=row.createCell(8);
    			}
    			n8.setCellValue(son.getSizeD());
    			Cell n9 = row.getCell(9);
    			if(n9==null){
    				n9=row.createCell(9);
    			}
    			n9.setCellValue(son.getSizeE());
    			Cell n10 = row.getCell(10);
    			if(n10==null){
    				n10=row.createCell(10);
    			}
    			n10.setCellValue(son.getSizeF());
    			Cell n11 = row.getCell(11);
    			if(n11==null){
    				n11=row.createCell(11);
    			}
    			n11.setCellValue(son.getSizeG());
    			Cell n12 = row.getCell(12);
    			if(n12==null){
    				n12=row.createCell(12);
    			}
    			n12.setCellValue(son.getSizeH());
    			Cell n13 = row.getCell(13);
    			if(n13==null){
    				n13=row.createCell(13);
    			}
    			if(son.getForgingSize()!=null&&!("").equals(son.getForgingSize())){
    				n13.setCellType(Cell.CELL_TYPE_STRING);
    				n13.setCellValue(son.getForgingSize());
    			}
    			Cell n14 = row.getCell(14);
    			if(n14==null){
    				n14=row.createCell(14);
    			}
    			n14.setCellValue(son.getDescription());
    			Cell n15 = row.getCell(15);
    			if(n15==null){
    				n15=row.createCell(15);
    			}
    			if(null != son.getQty()){
    				n15.setCellValue(son.getQty());
    			}
    			Cell n16 = row.getCell(16);
    			if(n16==null){
    				n16=row.createCell(16);
    			}
    			n16.setCellValue(son.getMaterial());
    			Cell n17 = row.getCell(17);
    			if(n17==null){
    				n17=row.createCell(17);
    			}
    			n17.setCellValue(son.getGrade());
    			Cell n18 = row.getCell(18);
    			if(n18==null){
    				n18=row.createCell(18);
    			}
    			if(son.getIscp()!=null&&son.getIscp().intValue()==1){
    				n18.setCellValue(son.getBlankRemarkA());
    			}else{
    				n18.setCellValue(son.getBlankWeight());
    			}
    			Cell n20 = row.getCell(20);
    			if(n20==null){
    				n20=row.createCell(20);
    			}
    			n20.setCellValue(son.getHtRequire());
    			Cell n21 = row.getCell(21);
    			if(n21==null){
    				n21=row.createCell(21);
    			}
    			n21.setCellValue(son.getHdnsRequire());
    			Cell n22 = row.getCell(22);
    			if(n22==null){
    				n22=row.createCell(22);
    			}
    			n22.setCellValue(son.getDeliveryState());
    			Cell n25 = row.getCell(25);
    			if(n25==null){
    				n25=row.createCell(25);
    			}
    			n25.setCellValue(son.getBlankRemark());
    		    y++;
    		}
    		
    		try {
    			this.setResponseHeader(response, uuid+".xls");
    			OutputStream os = response.getOutputStream();
    			wb.write(os);
    			os.flush();
    			os.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    }
    
    // 查询操作记录
  	@RequestMapping("/showMachingRecord")
  	public String showMachingRecord(){
  		return "main/product/showMachingRecord";
  	}
  	
    // 查询操作记录
  	@RequestMapping("/getMachingRecord")
  	@ResponseBody
  	public Object getMachingRecord(PageInfo page,MachiningRecord mr){
  		Integer pageNum = page.getPage()-1;
  		StringBuffer sb = new StringBuffer();
  		if(null != mr){
  			if(null != mr.getSerialNum()&&!("").equals(mr.getSerialNum())){
  				sb.append(" and serial_num like '%"+mr.getSerialNum()+"%'");
  			}
  			if(null != mr.getOrderNum()&&!("").equals(mr.getOrderNum())){
  				sb.append(" and order_num = '"+mr.getOrderNum()+"'");
  			}
  			if(null != mr.getOpTime()&&!("").equals(mr.getOpTime())){
  				sb.append(" and op_time like '%"+mr.getOpTime()+"%'");
  			}
  			if(null != mr.getOperator()&&!("").equals(mr.getOperator())){
  				sb.append(" and operator = '"+mr.getOperator()+"'");
  			}
  			if(null != mr.getProcessName()&&!("").equals(mr.getProcessName())){
  				sb.append(" and process_name = '"+mr.getProcessName()+"'");
  			}
  		}
  		String sql ="select * from machining_record where 1=1 "+sb+" order by process_name limit "+(pageNum*page.getRows())+","+page.getRows()+"";
  		List<Map<String, Object>> list =  productDao.getSqlQuery(sql);
  		PageResponse res = new PageResponse();
		res.setRows(list);
		Object total = productDao.getCount("select count(*) from machining_record where 1=1 "+sb+" ");
		res.setTotal(total);
		return res;
  	}
    
    // 查询操作记录
  	@RequestMapping("/showRecord")
  	public String showRecord(){
  		return "main/product/showRecord";
  	}
  	
    // 查询操作记录
  	@RequestMapping("/getRecord")
  	@ResponseBody
  	public Object getRecord(PageInfo page,OperateRecord or){
  		Integer pageNum = page.getPage()-1;
  		StringBuffer sb = new StringBuffer();
  		if(null != or){
  			if(null != or.getSerialNum()&&!("").equals(or.getSerialNum())){
  				sb.append(" and a.serial_num like '%"+or.getSerialNum()+"%'");
  			}
  			if(null != or.getOrderNum()&&!("").equals(or.getOrderNum())){
  				sb.append(" and a.order_num = '"+or.getOrderNum()+"'");
  			}
  			if(null != or.getOperateTime()&&!("").equals(or.getOperateTime())){
  				sb.append(" and b.operate_time like '%"+or.getOperateTime()+"%'");
  			}
  			if(null != or.getElementName()&&!("").equals(or.getElementName())){
  				sb.append(" and b.element_name like '%"+or.getElementName()+"%'");
  			}
  			if(null != or.getUsername()&&!("").equals(or.getUsername())){
  				sb.append(" and b.username = '"+or.getUsername()+"'");
  			}
  		}
  		String sql ="select a.serial_num,a.order_num,b.element_name,b.operate_time,b.username from product a join operate_record b on a.id=b.product_id where 1=1 "+sb+" limit "+(pageNum*page.getRows())+","+page.getRows()+"";
  		List<Map<String, Object>> list =  productDao.getSqlQuery(sql);
  		PageResponse res = new PageResponse();
		res.setRows(list);
		Object total = productDao.getCount("select count(*) from product a join operate_record b on a.id=b.product_id where 1=1 "+sb+" ");
		res.setTotal(total);
		return res;
  	}
  	
    // 修改序号
   	@RequestMapping("/toUpdateOrderNum")
   	public String toUpdateOrderNum(Long id,Model model){
   		Product product = productJPADao.findOne(id);
   		model.addAttribute("bean", product);
   		return "main/product/upateOrderNum";
   	}
   	
   // 修改序号
   	@RequestMapping("/doUpdateOrderNum")
   	@ResponseBody
   	public String doUpdateOrderNum(Long id,String orderNum,HttpSession session,Integer qty,String serialNum,String actUnitWeight){
   		if(null == qty){
   			return "qty";
   		}
   		String userChnName = (String) session.getAttribute("userChnName");
   		
   		List listP = productJPADao.findBySerialNumAndOrderNumAndId(serialNum, orderNum, id);
   		if(listP!=null&&listP.size()>0){
   			return "fail";
   		}
   		Product product = productJPADao.findOne(id);
   		OperateRecord record = new OperateRecord();
		record.setElementName(product.getSerialNum()+" "+product.getOrderNum()+" 修改序号为:"+serialNum+" "+orderNum+"   数量:"+qty+" 实际重量:"+actUnitWeight);
   		product.setOrderNum(orderNum);
   		product.setQty(qty);
   		product.setSerialNum(serialNum);
   		product.setActUnitWeight(actUnitWeight);
   		productService.splitSerialNum(product);
   		productJPADao.save(product);
		record.setOperateTime(DateUtil.getCurrentTime());
		record.setProductId(product.getId());
		record.setUsername(userChnName);
		operateRecordDao.save(record);
   		return "success";
   	}
  	
    // 添加标记
   	@RequestMapping("/addMarkup")
   	public String addMarkup(String ids,Model model){
   		model.addAttribute("ids", ids);
   		return "main/product/addMarkup";
   	}
   	
    // 添加标记
  	@RequestMapping("/doAddMarkup")
  	@ResponseBody
  	public String doAddMarkup(Integer opState,@RequestParam(value = "ids", required = false) List<Long> ids){
  		List<Product> list = (List<Product>) productJPADao.findAll(ids);
  		for(Product product : list){
  			product.setOpState(opState);
  			productJPADao.save(product);
  		}
  		return "success";
  	}
  	
    //导出外协信息
    @RequestMapping(value="/exportOutInfo")
    public void exportOutInfo(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "ids", required = false) List<Long> ids) throws Exception{
    	   String uuid = StringUtil.getUUID();
    	   String realPath = request.getSession().getServletContext().getRealPath("/"); 
    	   File file = new File(realPath+"excel/");
           if(!file.exists()){
        	   file.mkdir();
           }
    		Workbook wb = new XSSFWorkbook();
    		Sheet sheet = wb.createSheet();
    		Row rowTop = sheet.createRow(0);
    		Cell c0 = rowTop.createCell(0);
    		c0.setCellValue("锻件编号");
    		Cell c1 = rowTop.createCell(1);
    		c1.setCellValue("序号");
    		Cell c2 = rowTop.createCell(2);
    		c2.setCellValue("");
    		Cell c3 = rowTop.createCell(3);
    		c3.setCellValue("");
    		Cell c4 = rowTop.createCell(4);
    		c4.setCellValue("");
    		Cell c5 = rowTop.createCell(5);
    		c5.setCellValue("");
    		Cell c6 = rowTop.createCell(6);
    		c6.setCellValue("");
    		Cell c7 = rowTop.createCell(7);
    		c7.setCellValue("");
    		Cell c8 = rowTop.createCell(8);
    		c8.setCellValue("");
    		Cell c9 = rowTop.createCell(9);
    		c9.setCellValue("");
    		Cell c10 = rowTop.createCell(10);
    		c10.setCellValue("材质");
    		Cell c11 = rowTop.createCell(11);
    		c11.setCellValue("数量");
    		int x = 1;
    		List<Product> list = (List<Product>) productJPADao.findAll(ids);
    		for(Product product : list){
    			Row row = sheet.createRow(x);
    			Cell c00 = row.createCell(0);
        		c00.setCellValue(product.getSerialNum());
        		Cell c110 = row.createCell(1);
        		c110.setCellValue(product.getOrderNum());
        		Cell c22 = row.createCell(2);
        		c22.setCellValue(product.getSizeA());
        		Cell c33 = row.createCell(3);
        		c33.setCellValue(product.getSizeB());
        		Cell c44 = row.createCell(4);
        		c44.setCellValue(product.getSizeC());
        		Cell c55 = row.createCell(5);
        		c55.setCellValue(product.getSizeD());
        		Cell c66 = row.createCell(6);
        		c66.setCellValue(product.getSizeE());
        		Cell c77 = row.createCell(7);
        		c77.setCellValue(product.getSizeF());
        		Cell c88 = row.createCell(8);
        		c88.setCellValue(product.getSizeG());
        		Cell c99 = row.createCell(9);
        		c99.setCellValue(product.getSizeH());
        		Cell c100 = row.createCell(10);
        		c100.setCellValue(product.getMaterial());
        		Cell c111 = row.createCell(11);
        		if(null != product.getQty()){
        			c111.setCellValue(product.getQty());
        		}
        		x++;
    		}
    		try {
    			this.setResponseHeader(response, uuid+".xls");
    			OutputStream os = response.getOutputStream();
    			wb.write(os);
    			os.flush();
    			os.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    }		
    
    //导出检验信息
    @RequestMapping(value="/exportInspectInfo")
    public void exportInspectInfo(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "ids", required = false) List<Long> ids) throws Exception{
    	   String uuid = StringUtil.getUUID();
    	   String realPath = request.getSession().getServletContext().getRealPath("/"); 
    	   File file = new File(realPath+"excel/");
           if(!file.exists()){
        	   file.mkdir();
           }
    		Workbook wb = new XSSFWorkbook();
    		Sheet sheet = wb.createSheet();
    		Row rowTop = sheet.createRow(0);
    		Cell c0 = rowTop.createCell(0);
    		c0.setCellValue("锻件编号");
    		Cell c1 = rowTop.createCell(1);
    		c1.setCellValue("序号");
    		Cell c2 = rowTop.createCell(2);
    		c2.setCellValue("");
    		Cell c3 = rowTop.createCell(3);
    		c3.setCellValue("");
    		Cell c4 = rowTop.createCell(4);
    		c4.setCellValue("");
    		Cell c5 = rowTop.createCell(5);
    		c5.setCellValue("");
    		Cell c6 = rowTop.createCell(6);
    		c6.setCellValue("");
    		Cell c7 = rowTop.createCell(7);
    		c7.setCellValue("");
    		Cell c8 = rowTop.createCell(8);
    		c8.setCellValue("");
    		Cell c9 = rowTop.createCell(9);
    		c9.setCellValue("");
    		Cell c10 = rowTop.createCell(10);
    		c10.setCellValue("材质");
    		Cell c11 = rowTop.createCell(11);
    		c11.setCellValue("数量");
    		Cell c12 = rowTop.createCell(12);
    		c12.setCellValue("重量");
    		Cell c13 = rowTop.createCell(13);
    		c13.setCellValue("备注");
    		int x = 1;
    		List<Product> list = (List<Product>) productJPADao.findAll(ids);
    		for(Product product : list){
    			Row row = sheet.createRow(x);
    			Cell c00 = row.createCell(0);
        		c00.setCellValue(product.getSerialNum());
        		Cell c110 = row.createCell(1);
        		c110.setCellValue(product.getOrderNum());
        		Cell c22 = row.createCell(2);
        		c22.setCellValue(product.getSizeA());
        		Cell c33 = row.createCell(3);
        		c33.setCellValue(product.getSizeB());
        		Cell c44 = row.createCell(4);
        		c44.setCellValue(product.getSizeC());
        		Cell c55 = row.createCell(5);
        		c55.setCellValue(product.getSizeD());
        		Cell c66 = row.createCell(6);
        		c66.setCellValue(product.getSizeE());
        		Cell c77 = row.createCell(7);
        		c77.setCellValue(product.getSizeF());
        		Cell c88 = row.createCell(8);
        		c88.setCellValue(product.getSizeG());
        		Cell c99 = row.createCell(9);
        		c99.setCellValue(product.getSizeH());
        		Cell c100 = row.createCell(10);
        		c100.setCellValue(product.getMaterial());
        		Cell c111 = row.createCell(11);
        		if(null != product.getQty()){
        			c111.setCellValue(product.getQty());
        		}
        		Cell c122 = row.createCell(12);
        		c122.setCellValue(product.getRoughMachingWeight());
        		Cell c133 = row.createCell(13);
        		c133.setCellValue(product.getProcessRemark());
        		x++;
    		}
    		try {
    			this.setResponseHeader(response, uuid+".xls");
    			OutputStream os = response.getOutputStream();
    			wb.write(os);
    			os.flush();
    			os.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    }		
    
    //导出锻后热处理施工单
    @RequestMapping(value="/exportFindBlank")
    public void exportFindBlank(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "ids", required = false) List<Long> ids) throws Exception{
    		String uuid = StringUtil.getUUID();
    		String realPath = request.getSession().getServletContext().getRealPath("/"); 
    		File file = new File(realPath+"excel/");
           if(!file.exists()){
        	   file.mkdir();
           }
            Workbook wb = ExcelUtil.writeExcel("xlsx", realPath+"excel/N装架找料单.xlsx");
    		Sheet sheet = wb.getSheetAt(0);
    		sheet.setForceFormulaRecalculation(true);
    		List<Product> list = (List<Product>) productJPADao.findAllOrder(ids);
    		int y = 3;
    		for(Product son : list){
    			if(y==36||y==75||y==114||y==153||y==192||y==231||y==270||y==309||y==348){
    				y=y+6;
    			}
    			Row row = sheet.getRow(y);
    			if(null == row){
    				row = sheet.createRow(y);
    			}
    			Cell n1 = row.getCell(1);
    			if(n1==null){
    				n1 = row.createCell(1);
    			}
    			if(null != son.getIscp()&&son.getIscp().intValue()==1){
    				n1.setCellValue(son.getSerialNum()+"P");
    			}else{
    				n1.setCellValue(son.getSerialNum());    			
    			}
    			Cell n2 = row.getCell(2);
    			if(n2==null){
    				n2 = row.createCell(2);
    			}
    				n2.setCellValue(son.getOrderNum());
    			Cell n3 = row.getCell(3);
    			if(n3==null){
    				n3 = row.createCell(3);
    			}
    			n3.setCellValue(son.getSizeA());
    			Cell n4 = row.getCell(4);
    			if(n4==null){
    				n4 = row.createCell(4);
    			}
    			n4.setCellValue(son.getSizeB());
    			Cell n5 = row.getCell(5);
    			if(n5==null){
    				n5=row.createCell(5);
    			}
    			n5.setCellValue(son.getSizeC());
    			Cell n6 = row.getCell(6);
    			if(n6==null){
    				n6=row.createCell(6);
    			}
    			n6.setCellValue(son.getSizeD());
    			Cell n7 = row.getCell(7);
    			if(n7==null){
    				n7=row.createCell(7);
    			}
    			n7.setCellValue(son.getSizeE());
    			Cell n8 = row.getCell(8);
    			if(n8==null){
    				n8=row.createCell(8);
    			}
    			n8.setCellValue(son.getSizeF());
    			Cell n9 = row.getCell(9);
    			if(n9==null){
    				n9=row.createCell(9);
    			}
    			n9.setCellValue(son.getSizeG());
    			Cell n10 = row.getCell(10);
    			if(n10==null){
    				n10=row.createCell(10);
    			}
    			n10.setCellValue(son.getSizeH());
    			Cell n11 = row.getCell(11);
    			if(n11==null){
    				n11=row.createCell(11);
    			}
    			if(null != son.getQty()){
    			   n11.setCellValue(son.getQty());
    			}
    			Cell n12 = row.getCell(12);
    			if(n12==null){
    				n12=row.createCell(12);
    			}
    				n12.setCellValue(son.getMaterial());
    			Cell n13 = row.getCell(13);
    			if(n13==null){
    				n13=row.createCell(13);
    			}
    			n13.setCellValue(son.getHdnsRequire());
    			Cell n14 = row.getCell(14);
    			if(n14==null){
    				n14=row.createCell(14);
    			}
    			if(son.getIscp()!=null&&son.getIscp().intValue()==1){
    				n14.setCellValue(son.getBlankRemarkA());
    			}else{
    				n14.setCellValue(son.getBlankWeight());
    			}
    		    y++;
    		}
    		
    		try {
    			this.setResponseHeader(response, uuid+".xls");
    			OutputStream os = response.getOutputStream();
    			wb.write(os);
    			os.flush();
    			os.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    }
  	
    //数据统计
  	@RequestMapping("/getProductData")
  	@ResponseBody
  	public Object getProductData() throws ParseException{
  		Map<String, Object> map = new HashMap<String, Object>();
  		
  		//本厂
  		List<Integer> list = new ArrayList<Integer>();
  	    //下料
  		Object obj1 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11) and b.process_id = 4");
  		list.add(Integer.parseInt(""+obj1));
  	    //锻造
  		Object obj2 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11,17,10,18) and b.process_id = 5");//加外协状态10,18
  		list.add(Integer.parseInt(""+obj2));
  	    //锻后
  		Object obj3 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11,19,10) and b.process_id = 6");//加外协10
  		list.add(Integer.parseInt(""+obj3));
  		Object obj4 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11) and b.process_id = 7");
  		list.add(Integer.parseInt(""+obj4));
  		 //性能
  		Object obj5 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11,19,10) and b.process_id = 8");//加外协10
  		list.add(Integer.parseInt(""+obj5));
  		Object obj6 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11,12) and b.process_id = 13");
  		list.add(Integer.parseInt(""+obj6));
  		Object obj7 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11) and b.process_id = 9");
  		list.add(Integer.parseInt(""+obj7));
  		Object obj8 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11) and b.process_id = 10");
  		list.add(Integer.parseInt(""+obj8));
  		Object obj9 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11) and b.process_id = 11");
  		list.add(Integer.parseInt(""+obj9));
  		Object obj10 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11) and b.process_id = 12");
  		list.add(Integer.parseInt(""+obj10));
  		
  		map.put("self", list);
  		
  		//外协
  		List<Integer> list2 = new ArrayList<Integer>();
  	    //下料
  		Object obj11 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 4");
  		list2.add(Integer.parseInt(""+obj11));
  		//锻造
//  		Object obj22 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10,18) and b.process_id = 5");
  		list2.add(0);
  	    //锻后
//  		Object obj33 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 6");
  		list2.add(0);
  		Object obj44 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 7");
  		list2.add(Integer.parseInt(""+obj44));
  	    //性能
//  	Object obj55 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 8");
  		list2.add(0);
  		Object obj66 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 13");
  		list2.add(Integer.parseInt(""+obj66));
  		Object obj77 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 9");
  		list2.add(Integer.parseInt(""+obj77));
  		Object obj88 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 10");
  		list2.add(Integer.parseInt(""+obj88));
  		Object obj99 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 11");
  		list2.add(Integer.parseInt(""+obj99));
  		Object obj100 = productDao.getCount("select sum(a.qty) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 12");
  		list2.add(Integer.parseInt(""+obj100));
  		map.put("out", list2);
  		
  	//本厂
  		List<Double> list3 = new ArrayList<Double>();
  		Object obj111 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11) and b.process_id = 4");
  		list3.add(Double.parseDouble(""+obj111));
  		Object obj222 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11,17,10,18) and b.process_id = 5");
  		list3.add(Double.parseDouble(""+obj222));
  		Object obj333 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11,19,10) and b.process_id = 6");
  		list3.add(Double.parseDouble(""+obj333));
  		Object obj444 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11) and b.process_id = 7");
  		list3.add(Double.parseDouble(""+obj444));
  		Object obj555 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11,19,10) and b.process_id = 8");
  		list3.add(Double.parseDouble(""+obj555));
  		Object obj666 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11,12) and b.process_id = 13");
  		list3.add(Double.parseDouble(""+obj666));
  		Object obj777 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11) and b.process_id = 9");
  		list3.add(Double.parseDouble(""+obj777));
  		Object obj888 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11) and b.process_id = 10");
  		list3.add(Double.parseDouble(""+obj888));
  		Object obj999 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11) and b.process_id = 11");
  		list3.add(Double.parseDouble(""+obj999));
  		Object obj1000 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (3,4,11) and b.process_id = 12");
  		list3.add(Double.parseDouble(""+obj1000));
  		
  		map.put("self2", list3);
  		
  		//外协
  		List<Double> list4 = new ArrayList<Double>();
  		Object obj1111 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 4");
  		list4.add(Double.parseDouble(""+obj1111));
//  	Object obj2222 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10,18) and b.process_id = 5");
  		list4.add(0D);
//  		Object obj3333 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 6");
  		list4.add(0D);
  		Object obj4444 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 7");
  		list4.add(Double.parseDouble(""+obj4444));
//  		Object obj5555 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 8");
  		list4.add(0D);
  		Object obj6666 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 13");
  		list4.add(Double.parseDouble(""+obj6666));
  		Object obj7777 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 9");
  		list4.add(Double.parseDouble(""+obj7777));
  		Object obj8888 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 10");
  		list4.add(Double.parseDouble(""+obj8888));
  		Object obj9999 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 11");
  		list4.add(Double.parseDouble(""+obj9999));
  		Object obj10000 = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state in (10) and b.process_id = 12");
  		list4.add(Double.parseDouble(""+obj10000));
  		
  		map.put("out2", list4);
  		return map;
  	}
  	
    //数据统计
  	@RequestMapping("/getProductDatab")
  	@ResponseBody
  	public Object getProductDatab() throws ParseException{
  		Map<String, Object> map = new HashMap<String, Object>();
  	    Date d=new Date();   
 	    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");  
 	    Long time = 24 * 60 * 60 * 1000L;
 	    String now = df.format(d);
 	    String first = now.substring(0,7)+"-01";
 	    Date firstDate=df.parse(first);
 	    int t = 0;
 	    Date current = new Date(firstDate.getTime() + t * time);
 	    List<Double> list5 = new ArrayList<Double>();
 	    List<String> list6 = new ArrayList<String>();
 	    List<Double> list7 = new ArrayList<Double>();
	    List<String> list8 = new ArrayList<String>();
 	    while(current.compareTo(d)<=0){
 	       Object blank = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a where a.blank_date = '"+df.format(current)+"'");
 	       list5.add(Double.parseDouble(""+blank));
 	       list6.add(df.format(current));
 	       t++;
 	       current = new Date(firstDate.getTime() + t * time);
 	    }
 	    map.put("blank", list5);
 	    map.put("blanksubject", list6);
 	    map.put("month", now.substring(0,7));
  		return map;
  	}	
  	
  	
  	@RequestMapping("/getProductDatac")
  	@ResponseBody
  	public Object getProductDatac() throws ParseException{
  		Map<String, Object> map = new HashMap<String, Object>();
  	    Date d=new Date();   
 	    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");  
 	    Long time = 24 * 60 * 60 * 1000L;
 	    String now = df.format(d);
 	    String first = now.substring(0,7)+"-01";
 	    Date firstDate=df.parse(first);
 	    int t = 0;
 	    Date current = new Date(firstDate.getTime() + t * time);
 	    List<Double> list5 = new ArrayList<Double>();
 	    List<String> list6 = new ArrayList<String>();
 	    List<Double> list7 = new ArrayList<Double>();
	    List<String> list8 = new ArrayList<String>();
 	    while(current.compareTo(d)<=0){
 	       Object blank = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a where a.last_date = '"+df.format(current)+"'");
 	       list5.add(Double.parseDouble(""+blank));
 	       list6.add(df.format(current));
 	       t++;
 	       current = new Date(firstDate.getTime() + t * time);
 	    }
 	    map.put("blank", list5);
 	    map.put("blanksubject", list6);
 	    map.put("month", now.substring(0,7));
  		return map;
  	}	
  	
  	@RequestMapping("/getProductDatad")
  	@ResponseBody
  	public Object getProductDatad(String dateinput,Integer type) throws ParseException{
 	    if(type==null){
 	    	type = 1;
 	    }
 	    SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM");
 	    if(dateinput==null){
 		  dateinput = simple.format(new Date());
	    }
 	    String cellspacing="10";
 	    List<ProductTj> list = new ArrayList<ProductTj>();
 	    List<String> dates = AttendanceDay.getDaysByMonth(dateinput);
 	    StringBuffer table = new StringBuffer();
 	    table.append("<table border=\"0\" class=\"chart\" style=\"padding:0;margin:0;width:100%;font-size:20px;\" cellspacing=\"0\"><tr>");
 	    if(type==1){
 	    	int x=1;
 	    	for(String str : dates){
 	    		ProductTj bean = new ProductTj();
 	    		Object weight = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a where a.blank_date = '"+str+"'");
 	    		Object qty = productDao.getCount("select round(sum(a.qty)) from product a where a.blank_date = '"+str+"'");
 	    		StringBuffer tableSon = new StringBuffer();
 	    		tableSon.append("<table border=\"1\" style=\"padding:0;margin:0;width:100%;\" cellspacing=\"0\">");
 	    		tableSon.append("<tr>").append("<td colspan=\"2\" style=\"text-align:center;\">").append(str).append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append("件数").append("</td>").append("<td>").append("重量").append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append(qty).append("</td>").append("<td>").append(weight).append("</td>").append("</tr>");
 	    		tableSon.append("</table>");
 	    		if(x%7==0){
 	    			table.append("<td>").append(tableSon).append("</td></tr><tr>");
 	    		}else{
 	    			table.append("<td>").append(tableSon).append("</td>");
 	    		}
 	    		x++;
 	    	}
 	    	table.append("</tr>");
 	    }if(type==2){
	    	int x=1;
	    	for(String str : dates){
	    		ProductTj bean = new ProductTj();
	    		Object weight = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a where a.atter_forging_out_date = '"+str+"'");
	    		Object qty = productDao.getCount("select round(sum(a.qty)) from product a where a.atter_forging_out_date = '"+str+"'");
	    		StringBuffer tableSon = new StringBuffer();
 	    		tableSon.append("<table border=\"1\" style=\"padding:0;margin:0;width:100%;\" cellspacing=\"0\">");
 	    		tableSon.append("<tr>").append("<td colspan=\"2\" style=\"text-align:center;\">").append(str).append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append("件数").append("</td>").append("<td>").append("重量").append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append(qty).append("</td>").append("<td>").append(weight).append("</td>").append("</tr>");
 	    		tableSon.append("</table>");
 	    		if(x%7==0){
 	    			table.append("<td>").append(tableSon).append("</td></tr><tr>");
 	    		}else{
 	    			table.append("<td>").append(tableSon).append("</td>");
 	    		}
	    		x++;
	    	}
	    	table.append("</tr>");
	    }if(type==3){
	    	int x=1;
	    	for(String str : dates){
	    		ProductTj bean = new ProductTj();
	    		Object weight = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a where a.rough_maching_out_date = '"+str+"'");
	    		Object qty = productDao.getCount("select round(sum(a.qty)) from product a where a.rough_maching_out_date = '"+str+"'");
	    		StringBuffer tableSon = new StringBuffer();
 	    		tableSon.append("<table border=\"1\" style=\"padding:0;margin:0;width:100%;\" cellspacing=\"0\">");
 	    		tableSon.append("<tr>").append("<td colspan=\"2\" style=\"text-align:center;\">").append(str).append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append("件数").append("</td>").append("<td>").append("重量").append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append(qty).append("</td>").append("<td>").append(weight).append("</td>").append("</tr>");
 	    		tableSon.append("</table>");
 	    		if(x%7==0){
 	    			table.append("<td>").append(tableSon).append("</td></tr><tr>");
 	    		}else{
 	    			table.append("<td>").append(tableSon).append("</td>");
 	    		}
	    		x++;
	    	}
	    	table.append("</tr>");
	    }if(type==4){
	    	int x=1;
	    	for(String str : dates){
	    		ProductTj bean = new ProductTj();
	    		Object weight = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a where a.per_heat_treat_out_date = '"+str+"'");
	    		Object qty = productDao.getCount("select round(sum(a.qty)) from product a where a.per_heat_treat_out_date = '"+str+"'");
	    		StringBuffer tableSon = new StringBuffer();
 	    		tableSon.append("<table border=\"1\" style=\"padding:0;margin:0;width:100%;\" cellspacing=\"0\">");
 	    		tableSon.append("<tr>").append("<td colspan=\"2\" style=\"text-align:center;\">").append(str).append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append("件数").append("</td>").append("<td>").append("重量").append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append(qty).append("</td>").append("<td>").append(weight).append("</td>").append("</tr>");
 	    		tableSon.append("</table>");
 	    		if(x%7==0){
 	    			table.append("<td>").append(tableSon).append("</td></tr><tr>");
 	    		}else{
 	    			table.append("<td>").append(tableSon).append("</td>");
 	    		}
	    		x++;
	    	}
	    	table.append("</tr>");
	    }if(type==5){
	    	int x=1;
	    	for(String str : dates){
	    		ProductTj bean = new ProductTj();
	    		Object weight = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a where a.delivery_out_date = '"+str+"'");
	    		Object qty = productDao.getCount("select round(sum(a.qty)) from product a where a.delivery_out_date = '"+str+"'");
	    		StringBuffer tableSon = new StringBuffer();
 	    		tableSon.append("<table border=\"1\" style=\"padding:0;margin:0;width:100%;\" cellspacing=\"0\">");
 	    		tableSon.append("<tr>").append("<td colspan=\"2\" style=\"text-align:center;\">").append(str).append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append("件数").append("</td>").append("<td>").append("重量").append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append(qty).append("</td>").append("<td>").append(weight).append("</td>").append("</tr>");
 	    		tableSon.append("</table>");
 	    		if(x%7==0){
 	    			table.append("<td>").append(tableSon).append("</td></tr><tr>");
 	    		}else{
 	    			table.append("<td>").append(tableSon).append("</td>");
 	    		}
	    		x++;
	    	}
	    	table.append("</tr>");
	    }
	    table.append("</table>");
  		return table;
  	}	
  	
 	@RequestMapping("/getProductDatae")
  	@ResponseBody
  	public Object getProductDatae(String dateinput,Integer type) throws ParseException{
 	    if(type==null){
 	    	type = 1;
 	    }
 	    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
 	    String year = dateinput;
 	    if(dateinput==null){
 	    	year = yearFormat.format(new Date());
	    }
 	    String cellspacing="10";
 	    List<ProductTj> list = new ArrayList<ProductTj>();
 	    StringBuffer table = new StringBuffer();
 	    table.append("<table border=\"0\" class=\"chart\" style=\"padding:0;margin:0;width:100%;font-size:20px;\" cellspacing=\"0\"><tr>");
 	    if(type==1){//下料
 	    	for(Integer x=1;x<=6;x++){
 	    		String month = (year+"-"+"0"+x);
 	    		Object weight = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a where a.blank_date like '%"+month+"%'");
 	    		Object qty = productDao.getCount("select round(sum(a.qty)) from product a where a.blank_date like '%"+month+"%'");
 	    		StringBuffer tableSon = new StringBuffer();
 	    		tableSon.append("<table border=\"1\" style=\"padding:0;margin:0;width:100%;\" cellspacing=\"0\">");
 	    		tableSon.append("<tr>").append("<td colspan=\"2\" style=\"text-align:center;\">").append(month).append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append("件数").append("</td>").append("<td>").append("重量").append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append(qty).append("</td>").append("<td>").append(weight).append("</td>").append("</tr>");
 	    		tableSon.append("</table>");
 	    		if(x==6){
 	    			table.append("<td>").append(tableSon).append("</td></tr><tr>");
 	    		}else{
 	    			table.append("<td>").append(tableSon).append("</td>");
 	    		}
 	    	}
 	    	for(int x=7;x<=12;x++){
 	    		String month = year+"-"+(x<10?"0"+x:x);
 	    		Object weight = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a where a.blank_date like '%"+month+"%'");
 	    		Object qty = productDao.getCount("select round(sum(a.qty)) from product a where a.blank_date like '%"+month+"%'");
 	    		StringBuffer tableSon = new StringBuffer();
 	    		tableSon.append("<table border=\"1\" style=\"padding:0;margin:0;width:100%;\" cellspacing=\"0\">");
 	    		tableSon.append("<tr>").append("<td colspan=\"2\" style=\"text-align:center;\">").append(month).append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append("件数").append("</td>").append("<td>").append("重量").append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append(qty).append("</td>").append("<td>").append(weight).append("</td>").append("</tr>");
 	    		tableSon.append("</table>");
 	    		if(x==12){
 	    			table.append("<td>").append(tableSon).append("</td></tr>");
 	    		}else{
 	    			table.append("<td>").append(tableSon).append("</td>");
 	    		}
 	    	}
 	    }if(type==2){//atter_forging_out_date
 	    	for(Integer x=1;x<=6;x++){
 	    		String month = (year+"-"+"0"+x);
 	    		Object weight = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a where a.atter_forging_out_date like '%"+month+"%'");
 	    		Object qty = productDao.getCount("select round(sum(a.qty)) from product a where a.atter_forging_out_date like '%"+month+"%'");
 	    		StringBuffer tableSon = new StringBuffer();
 	    		tableSon.append("<table border=\"1\" style=\"padding:0;margin:0;width:100%;\" cellspacing=\"0\">");
 	    		tableSon.append("<tr>").append("<td colspan=\"2\" style=\"text-align:center;\">").append(month).append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append("件数").append("</td>").append("<td>").append("重量").append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append(qty).append("</td>").append("<td>").append(weight).append("</td>").append("</tr>");
 	    		tableSon.append("</table>");
 	    		if(x==6){
 	    			table.append("<td>").append(tableSon).append("</td></tr><tr>");
 	    		}else{
 	    			table.append("<td>").append(tableSon).append("</td>");
 	    		}
 	    	}
 	    	for(int x=7;x<=12;x++){
 	    		String month = year+"-"+(x<10?"0"+x:x);
 	    		Object weight = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a where a.atter_forging_out_date like '%"+month+"%'");
 	    		Object qty = productDao.getCount("select round(sum(a.qty)) from product a where a.atter_forging_out_date like '%"+month+"%'");
 	    		StringBuffer tableSon = new StringBuffer();
 	    		tableSon.append("<table border=\"1\" style=\"padding:0;margin:0;width:100%;\" cellspacing=\"0\">");
 	    		tableSon.append("<tr>").append("<td colspan=\"2\" style=\"text-align:center;\">").append(month).append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append("件数").append("</td>").append("<td>").append("重量").append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append(qty).append("</td>").append("<td>").append(weight).append("</td>").append("</tr>");
 	    		tableSon.append("</table>");
 	    		if(x==12){
 	    			table.append("<td>").append(tableSon).append("</td></tr>");
 	    		}else{
 	    			table.append("<td>").append(tableSon).append("</td>");
 	    		}
 	    	}
	    }if(type==3){//rough_maching_out_date
	    	for(Integer x=1;x<=6;x++){
	    		String month = (year+"-"+"0"+x);
 	    		Object weight = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a where a.rough_maching_out_date like '%"+month+"%'");
 	    		Object qty = productDao.getCount("select round(sum(a.qty)) from product a where a.rough_maching_out_date like '%"+month+"%'");
 	    		StringBuffer tableSon = new StringBuffer();
 	    		tableSon.append("<table border=\"1\" style=\"padding:0;margin:0;width:100%;\" cellspacing=\"0\">");
 	    		tableSon.append("<tr>").append("<td colspan=\"2\" style=\"text-align:center;\">").append(month).append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append("件数").append("</td>").append("<td>").append("重量").append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append(qty).append("</td>").append("<td>").append(weight).append("</td>").append("</tr>");
 	    		tableSon.append("</table>");
 	    		if(x==6){
 	    			table.append("<td>").append(tableSon).append("</td></tr><tr>");
 	    		}else{
 	    			table.append("<td>").append(tableSon).append("</td>");
 	    		}
 	    	}
 	    	for(int x=7;x<=12;x++){
 	    		String month = year+"-"+(x<10?"0"+x:x);
 	    		Object weight = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a where a.rough_maching_out_date like '%"+month+"%'");
 	    		Object qty = productDao.getCount("select round(sum(a.qty)) from product a where a.rough_maching_out_date like '%"+month+"%'");
 	    		StringBuffer tableSon = new StringBuffer();
 	    		tableSon.append("<table border=\"1\" style=\"padding:0;margin:0;width:100%;\" cellspacing=\"0\">");
 	    		tableSon.append("<tr>").append("<td colspan=\"2\" style=\"text-align:center;\">").append(month).append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append("件数").append("</td>").append("<td>").append("重量").append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append(qty).append("</td>").append("<td>").append(weight).append("</td>").append("</tr>");
 	    		tableSon.append("</table>");
 	    		if(x==12){
 	    			table.append("<td>").append(tableSon).append("</td></tr>");
 	    		}else{
 	    			table.append("<td>").append(tableSon).append("</td>");
 	    		}
 	    	}
	    }if(type==4){//per_heat_treat_out_date
	    	for(Integer x=1;x<=6;x++){
	    		String month = (year+"-"+"0"+x);
 	    		Object weight = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a where a.per_heat_treat_out_date like '%"+month+"%'");
 	    		Object qty = productDao.getCount("select round(sum(a.qty)) from product a where a.per_heat_treat_out_date like '%"+month+"%'");
 	    		StringBuffer tableSon = new StringBuffer();
 	    		tableSon.append("<table border=\"1\" style=\"padding:0;margin:0;width:100%;\" cellspacing=\"0\">");
 	    		tableSon.append("<tr>").append("<td colspan=\"2\" style=\"text-align:center;\">").append(month).append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append("件数").append("</td>").append("<td>").append("重量").append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append(qty).append("</td>").append("<td>").append(weight).append("</td>").append("</tr>");
 	    		tableSon.append("</table>");
 	    		if(x==6){
 	    			table.append("<td>").append(tableSon).append("</td></tr><tr>");
 	    		}else{
 	    			table.append("<td>").append(tableSon).append("</td>");
 	    		}
 	    	}
 	    	for(int x=7;x<=12;x++){
 	    		String month = year+"-"+(x<10?"0"+x:x);
 	    		Object weight = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a where a.per_heat_treat_out_date like '%"+month+"%'");
 	    		Object qty = productDao.getCount("select round(sum(a.qty)) from product a where a.per_heat_treat_out_date like '%"+month+"%'");
 	    		StringBuffer tableSon = new StringBuffer();
 	    		tableSon.append("<table border=\"1\" style=\"padding:0;margin:0;width:100%;\" cellspacing=\"0\">");
 	    		tableSon.append("<tr>").append("<td colspan=\"2\" style=\"text-align:center;\">").append(month).append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append("件数").append("</td>").append("<td>").append("重量").append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append(qty).append("</td>").append("<td>").append(weight).append("</td>").append("</tr>");
 	    		tableSon.append("</table>");
 	    		if(x==12){
 	    			table.append("<td>").append(tableSon).append("</td></tr>");
 	    		}else{
 	    			table.append("<td>").append(tableSon).append("</td>");
 	    		}
 	    	}
	    }if(type==5){//delivery_out_date
	    	for(Integer x=1;x<=6;x++){
	    		String month = (year+"-"+"0"+x);
 	    		Object weight = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a where a.delivery_out_date like '%"+month+"%'");
 	    		Object qty = productDao.getCount("select round(sum(a.qty)) from product a where a.delivery_out_date like '%"+month+"%'");
 	    		StringBuffer tableSon = new StringBuffer();
 	    		tableSon.append("<table border=\"1\" style=\"padding:0;margin:0;width:100%;\" cellspacing=\"0\">");
 	    		tableSon.append("<tr>").append("<td colspan=\"2\" style=\"text-align:center;\">").append(month).append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append("件数").append("</td>").append("<td>").append("重量").append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append(qty).append("</td>").append("<td>").append(weight).append("</td>").append("</tr>");
 	    		tableSon.append("</table>");
 	    		if(x==6){
 	    			table.append("<td>").append(tableSon).append("</td></tr><tr>");
 	    		}else{
 	    			table.append("<td>").append(tableSon).append("</td>");
 	    		}
 	    	}
 	    	for(int x=7;x<=12;x++){
 	    		String month = year+"-"+(x<10?"0"+x:x);
 	    		Object weight = productDao.getCount("select round(sum(a.qty*a.blank_weight/1000)) from product a where a.delivery_out_date like '%"+month+"%'");
 	    		Object qty = productDao.getCount("select round(sum(a.qty)) from product a where a.delivery_out_date like '%"+month+"%'");
 	    		StringBuffer tableSon = new StringBuffer();
 	    		tableSon.append("<table border=\"1\" style=\"padding:0;margin:0;width:100%;\" cellspacing=\"0\">");
 	    		tableSon.append("<tr>").append("<td colspan=\"2\" style=\"text-align:center;\">").append(month).append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append("件数").append("</td>").append("<td>").append("重量").append("</td>").append("</tr>");
 	    		tableSon.append("<tr>").append("<td>").append(qty).append("</td>").append("<td>").append(weight).append("</td>").append("</tr>");
 	    		tableSon.append("</table>");
 	    		if(x==12){
 	    			table.append("<td>").append(tableSon).append("</td></tr>");
 	    		}else{
 	    			table.append("<td>").append(tableSon).append("</td>");
 	    		}
 	    	}
	    }
	    table.append("</table>");
  		return table;
  	}	
 	
 	//下料按材质统计
 	@RequestMapping("/showMaterials")
 	public Object showMaterials(){
 		return "main/product/blankMaterial";
 	}
  	
 	//下料按材质统计
 	@RequestMapping("/getInfoByMaterial")
 	@ResponseBody
 	public Object getInfoByMaterial(){
 		List<Map<String, Object>> list = productDao.getSqlQuery("select DISTINCT a.material from product a join process_temp_son b on a.temp_son_id=b.id where a.state = 3 and b.process_id = 4 and a.material is not null");
        List<ProductTj> listp = new ArrayList<ProductTj>();
 		for(Map<String, Object> map : list){
        	ProductTj bean = new ProductTj();
    		Object weight = productDao.getCount("select round(sum(a.qty*a.blank_weight)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state = 3 and b.process_id = 4 and a.material = '"+map.get("material")+"'");
    		Object qty = productDao.getCount("select round(sum(a.qty)) from product a join process_temp_son b on a.temp_son_id=b.id where a.state = 3 and b.process_id = 4 and a.material = '"+map.get("material")+"'");
    		bean.setMaterial(map.get("material"));
    		bean.setWeight(weight);
    		bean.setQty(qty);
    		listp.add(bean);
 		}
 		return listp;
 	}
 	
    //检验判定不合格状态
   	@RequestMapping("/toUpdateNcrState")
   	public String toUpdateNcrState(Long id,Model model){
   		model.addAttribute("id", id);
   		return "main/ncr/updateNcrState";
   	}
  	
  //检验判定不合格状态
   	@RequestMapping("/updateNcrState")
   	@ResponseBody
   	public String updateNcrState(Long id,Integer state){
   		Product p = productJPADao.findOne(id);
   		p.setState(state);
   		productJPADao.save(p);
   		return "success";
   	}
   	
    // 数据初始化
  	@RequestMapping("/codeInit")
  	@ResponseBody
  	public String codeInit(){
  		List<Ncr> listN = (List<Ncr>) ncrDao.findAll();
  		for(Ncr ncr : listN){
  			List<NcrSon> listSon = ncrSonDao.findByParentId(ncr.getId());
  			StringBuffer snum = new StringBuffer();
  			StringBuffer order = new StringBuffer();
  			StringBuffer type = new StringBuffer();
  			for(NcrSon son : listSon){
  				Product pro = productJPADao.findOne(son.getProductId());
  				snum.append(pro.getSerialNum()).append("<br>");
  				order.append(pro.getOrderNum()).append("<br>");
  				if(son.getHandleType().intValue()==1){
  					type.append("返工");
  				}
                if(son.getHandleType().intValue()==2){
	                type.append("报废");
  				}
                if(son.getHandleType().intValue()==3){
	                type.append("让步");
	            }
                if(son.getHandleType().intValue()==4){
	                type.append("申请");
	            }
                if(son.getHandleType().intValue()==5){
	                type.append("返修");
	            }
                if(son.getHandleType().intValue()==6){
	                type.append("退货");
	            }
  			}
  			ncr.setSerialNum(snum.toString());
  			ncr.setOrderNum(order.toString());
  			ncr.setHandleType(type.toString());
  			ncrDao.save(ncr);
  		}
  		return "success";
  	}
}
