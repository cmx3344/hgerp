package com.hg.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import com.hg.dao.ProductJPADao;
import com.hg.dao.SysProcessDao;
import com.hg.dao.TempSonDao;
import com.hg.model.ProcessTempSon;
import com.hg.model.Product;
import com.hg.model.SysProcess;
import com.hg.util.DateUtil;


@Service
public class ProductService {
	
	@Inject
	private TempSonDao tempSonDao;
	@Inject
	private SysProcessDao sysProcessDao;
	@Inject
	private ProductJPADao productJPADao;
	
	public Product getProduct(Product p){
		ProcessTempSon son = new ProcessTempSon();
		if(null != p.getNextTempSonId()){
			son = tempSonDao.findOne(p.getNextTempSonId());
		}else{
			if(null == p.getTempSonId()){
				return p;
			}
			son = tempSonDao.findOne(p.getTempSonId());
		}
		List<ProcessTempSon> list = tempSonDao.findByParentId(son.getParentId());
		boolean a = true,b=true,c=true,d=true,e=true,f=true,g=true,h=true,i=true,j=true;
		for(ProcessTempSon s : list){
			if(s.getProcessId().intValue()==4){
				a = false;
			}
			if(s.getProcessId().intValue()==5){
				b = false;
			}
			if(s.getProcessId().intValue()==6){
				c = false;
			}
			if(s.getProcessId().intValue()==7){
				d = false;
			}
			if(s.getProcessId().intValue()==8){
				e = false;
			}
			if(s.getProcessId().intValue()==9){
				f = false;
			}
			if(s.getProcessId().intValue()==10){
				g = false;
			}
			if(s.getProcessId().intValue()==11){
				h = false;
			}
			if(s.getProcessId().intValue()==12){
				i = false;
			}
			if(s.getProcessId().intValue()==13){
				j = false;
			}
		}
		if(a){
			p.setBlankDateA("/");
			p.setBlankRemarkA("/");
			p.setBlankDate("/");
			p.setBlankRemark("/");
			p.setHeatNum("/");
		}
		if(b){
			p.setForgingInDateA("/");
			p.setForgingOutDateA("/");
			p.setForgingWorker("/");
			p.setForgingInDate("/");
			p.setForgingOutDate("/");
		}
		if(c){
			p.setHeatTreatInDateA("/");
			p.setHeatTreatOutDateA("/");
			p.setAfterForgingWorker("/");
			p.setAfterForgingInDate("/");
			p.setAtterForgingOutDate("/");
		}
		if(d){
			p.setRoughMachingWorker("/");
			p.setRoughMachingOutDate("/");
			p.setRoughMachingInDate("/");
		}
		if(e){
			p.setPerHeatTreatInDate("/");
			p.setPerHeatTreatOutDate("/");
			p.setPerHeatTreatWorker("/");
		}
		if(f){
			p.setDeliveryInDate("/");
			p.setDeliveryOutDate("/");
			p.setDeliveryWorker("/");
		}
		if(g){
			p.setFollowupInDate("/");
			p.setFollowupOutDate("/");
			p.setFollowupWorker("/");
		}
		if(h){
			p.setHalfFinishMachingInDate("/");
			p.setHalfFinishMachingOutDate("/");
			p.setHalfFinishMachingWorker("/");
		}
		if(i){
			p.setFinishMachingInDate("/");
			p.setFinishMachingOutDate("/");
			p.setFinishMachingWorker("/");
		}
		if(j){
			p.setSampleInDate("/");
			p.setSampleOutDate("/");
		}
		return p;
	}
	
	public Product inDate(Product product){
		ProcessTempSon son = tempSonDao.findOne(product.getTempSonId());
		SysProcess process = sysProcessDao.findOne(son.getProcessId());
		String now = DateUtil.getCurrentymd();
//		if(process.getId().intValue()==6){
//			if(product.getNextTempSonId()!=null&&product.getTime()==null){
//				product.setHeatTreatInDateA(now);
//			}else{
//				product.setAfterForgingInDate(now);
//			}
//		}
		if(process.getId().intValue()==7){
			product.setRoughMachingInDate(now);
		}
//		if(process.getId().intValue()==8){
//			product.setPerHeatTreatInDate(now);
//		}
		if(process.getId().intValue()==9){
			product.setDeliveryInDate(now);
		}
//		if(process.getId().intValue()==10){
//			product.setFollowupInDate(now);
//		}
		if(process.getId().intValue()==11){
			product.setHalfFinishMachingInDate(now);
		}
		if(process.getId().intValue()==12){
			product.setFinishMachingInDate(now);
		}
		return product;
	}
	
	public Product outDate(Product product){
		ProcessTempSon son = tempSonDao.findOne(product.getTempSonId());
		SysProcess process = sysProcessDao.findOne(son.getProcessId());
		String now = DateUtil.getCurrentymd();
		if(process.getId().intValue()==4){
			product.setBlankInspectDate(now);
		}
		if(process.getId().intValue()==5){
			if(null!=product.getIscp()&&product.getIscp()==1){
				product.setForgingOutDateA(now);
			}else{
				product.setForgingOutDate(now);
			}
		}
		if(process.getId().intValue()==6){
			if(null!=product.getIscp()&&product.getIscp()==1){
				product.setHeatTreatOutDateA(now);
			}else{
				product.setAtterForgingOutDate(now);
			}
		}
		if(process.getId().intValue()==7){
			product.setRoughMachingOutDate(now);
		}
		if(process.getId().intValue()==8){
			product.setPerHeatTreatOutDate(now);
		}
		if(process.getId().intValue()==9){
			product.setDeliveryOutDate(now);
		}
		if(process.getId().intValue()==10){
			product.setFollowupOutDate(now);
		}
		if(process.getId().intValue()==11){
			product.setHalfFinishMachingOutDate(now);
		}
		if(process.getId().intValue()==12){
			product.setFinishMachingOutDate(now);
		}
		if(process.getId().intValue()==13){
			product.setSampleOutDate(now);
		}
		return product;
	}
	
	public void getDateColumn(Long processId,String start,String end,StringBuffer where,String startDateB,String endDateB,String forgingWorker){
		   where.append(" and a.state != 6");
		   if(null != processId&&processId.intValue()==1){
				if(null != start&&null != end&&!("").equals(start)&&!("").equals(end)){
					where.append(" and a.start_date >= '"+start+"' and a.start_date <= '"+end+"'");
				}
				if(null != start&&!("").equals(start)&&(null == end||("").equals(end))){
					where.append(" and a.start_date = '"+start+"'");
				}
			}else{
				if(null != start&&null != startDateB&&!("").equals(start)&&!("").equals(startDateB)){
					if(processId.intValue()==4){
						where.append(" and a.blank_date >= '"+start+"' and a.blank_date <= '"+startDateB+"'");
					}
					if(processId.intValue()==5){
						where.append(" and a.forging_in_date >= '"+start+"' and a.forging_in_date <= '"+startDateB+"'");
					}
					if(processId.intValue()==6){
						where.append(" and a.after_forging_in_date >= '"+start+"' and a.after_forging_in_date <= '"+startDateB+"'");
					}
					if(processId.intValue()==7){
						where.append(" and a.rough_maching_in_date >= '"+start+"' and a.rough_maching_in_date <= '"+startDateB+"'");
					}
					if(processId.intValue()==8){
						where.append(" and a.per_heat_treat_in_date >= '"+start+"' and a.per_heat_treat_in_date <= '"+startDateB+"'");
					}
					if(processId.intValue()==9){
						where.append(" and a.delivery_in_date >= '"+start+"' and a.delivery_in_date <= '"+startDateB+"'");
					}
					if(processId.intValue()==10){
						where.append(" and a.followup_in_date >= '"+start+"' and a.followup_in_date <= '"+startDateB+"'");
					}
					if(processId.intValue()==11){
						where.append(" and a.half_finish_maching_in_date >= '"+start+"' and a.half_finish_maching_in_date <= '"+startDateB+"'");
					}
					if(processId.intValue()==12){
						where.append(" and a.finish_maching_in_date >= '"+start+"' and a.finish_maching_in_date <= '"+startDateB+"'");
					}
					if(processId.intValue()==13){
						where.append(" and a.sample_in_date >= '"+start+"' and a.sample_in_date <= '"+startDateB+"'");
					}
				}
				if((null != start&&!("").equals(start))&&(null == startDateB||("").equals(startDateB))){
					if(processId.intValue()==4){
						where.append(" and a.blank_date = '"+start+"' ");
					}
					if(processId.intValue()==5){
						where.append(" and a.forging_in_date = '"+start+"'");
					}
					if(processId.intValue()==6){
						where.append(" and a.after_forging_in_date = '"+start+"'");
					}
					if(processId.intValue()==7){
						where.append(" and a.rough_maching_in_date = '"+start+"'");
					}
					if(processId.intValue()==8){
						where.append(" and a.per_heat_treat_in_date = '"+start+"'");
					}
					if(processId.intValue()==9){
						where.append(" and a.delivery_in_date = '"+start+"'");
					}
					if(processId.intValue()==10){
						where.append(" and a.followup_in_date = '"+start+"'");
					}
					if(processId.intValue()==11){
						where.append(" and a.half_finish_maching_in_date = '"+start+"'");
					}
					if(processId.intValue()==12){
						where.append(" and a.finish_maching_in_date = '"+start+"'");
					}
					if(processId.intValue()==13){
						where.append(" and a.sample_in_date = '"+start+"'");
					}
				}
				if(null != end&&!("").equals(end)&&null != endDateB&&!("").equals(endDateB)){
					if(processId.intValue()==4){
						where.append(" and a.blank_date >= '"+end+"' and a.blank_date <= '"+endDateB+"'");
					}
					if(processId.intValue()==5){
						where.append(" and a.forging_out_date >= '"+end+"' and a.forging_out_date <= '"+endDateB+"'");
					}
					if(processId.intValue()==6){
						where.append(" and a.after_forging_in_date = '"+end+"' and a.forging_out_date <= '"+endDateB+"'");
					}
					if(processId.intValue()==7){
						where.append(" and a.rough_maching_out_date = '"+end+"' and a.rough_maching_out_date <= '"+endDateB+"'");
					}
					if(processId.intValue()==8){
						where.append(" and a.per_heat_treat_out_date = '"+end+"' and a.per_heat_treat_out_date <= '"+endDateB+"'");
					}
					if(processId.intValue()==9){
						where.append(" and a.delivery_out_date = '"+end+"' and a.delivery_out_date <= '"+endDateB+"'");
					}
					if(processId.intValue()==10){
						where.append(" and a.followup_out_date = '"+end+"' and a.followup_out_date <= '"+endDateB+"'");
					}
					if(processId.intValue()==11){
						where.append(" and a.half_finish_maching_out_date = '"+end+"' and a.half_finish_maching_out_date <= '"+endDateB+"'");
					}
					if(processId.intValue()==12){
						where.append(" and a.finish_maching_out_date = '"+end+"' and a.finish_maching_out_date <= '"+endDateB+"'");
					}
					if(processId.intValue()==13){
						where.append(" and a.sample_out_date = '"+end+"' and a.sample_out_date <= '"+endDateB+"'");
					}
				}
				if(null != end&&!("").equals(end)&&(null == endDateB||("").equals(endDateB))){
					if(processId.intValue()==4){
						where.append(" and a.blank_date = '"+end+"'");
					}
					if(processId.intValue()==5){
						where.append(" and a.forging_out_date = '"+end+"'");
					}
					if(processId.intValue()==6){
						where.append(" and a.atter_forging_out_date = '"+end+"'");
					}
					if(processId.intValue()==7){
						where.append(" and a.rough_maching_out_date = '"+end+"'");
					}
					if(processId.intValue()==8){
						where.append(" and a.per_heat_treat_out_date = '"+end+"'");
					}
					if(processId.intValue()==9){
						where.append(" and a.delivery_out_date = '"+end+"'");
					}
					if(processId.intValue()==10){
						where.append(" and a.followup_out_date = '"+end+"'");
					}
					if(processId.intValue()==11){
						where.append(" and a.half_finish_maching_out_date = '"+end+"'");
					}
					if(processId.intValue()==12){
						where.append(" and  a.finish_maching_out_date = '"+end+"'");
					}
					if(processId.intValue()==13){
						where.append(" and a.sample_out_date = '"+end+"'");
					}
				}
				if(null != forgingWorker&&!("").equals(forgingWorker)){
					if(processId.intValue()==5){
						where.append(" and a.forging_worker like '%"+forgingWorker+"%'");
					}
					if(processId.intValue()==6){
						where.append(" and a.after_forging_worker like '%"+forgingWorker+"%'");
					}
					if(processId.intValue()==7){
						where.append(" and a.rough_maching_worker like '%"+forgingWorker+"%'");
					}
					if(processId.intValue()==8){
						where.append(" and a.per_heat_treat_worker like '%"+forgingWorker+"%'");
					}
					if(processId.intValue()==9){
						where.append(" and a.delivery_worker like '%"+forgingWorker+"%'");
					}
					if(processId.intValue()==10){
						where.append(" and a.followup_worker like '%"+forgingWorker+"%'");
					}
					if(processId.intValue()==11){
						where.append(" and a.half_finish_maching_worker like '%"+forgingWorker+"%'");
					}
					if(processId.intValue()==12){
						where.append(" and a.finish_maching_worker like '%"+forgingWorker+"%'");
					}
				}
			}
	}
	
	public void getDateColumn2(Long processId,String remark,String com,StringBuffer where){
		   
		if(processId.intValue()==7){
			if(null != remark&&!("").equals(remark)){
				where.append(" and a.rough_maching_out_remark like '%"+remark+"%'");
			}
			if(null != com&&!("").equals(com)){
				where.append(" and a.rough_maching_out_com like '%"+com+"%'");
			}
		}
		if(processId.intValue()==9){
			if(null != remark&&!("").equals(remark)){
				where.append(" and a.delivery_out_remark like '%"+remark+"%'");
			}
			if(null != com&&!("").equals(com)){
				where.append(" and a.delivery_out_com like '%"+com+"%'");
			}
		}
		if(processId.intValue()==11){
			if(null != remark&&!("").equals(remark)){
				where.append(" and a.half_finish_out_remark like '%"+remark+"%'");
			}
			if(null != com&&!("").equals(com)){
				where.append(" and a.half_finish_out_com like '%"+com+"%'");
			}
		}
		if(processId.intValue()==12){
			if(null != remark&&!("").equals(remark)){
				where.append(" and a.finish_maching_out_remark like '%"+remark+"%'");
			}
			if(null != com&&!("").equals(com)){
				where.append(" and a.finish_maching_out_com like '%"+com+"%'");
			}
		}
        
	}
	
	public void exportAllInfos(Sheet sheet,List<Map<String, Object>> list){
		
		List<String> listT = new ArrayList<String>();
		Row rowT =sheet.getRow(1);
		int rowlen = rowT.getLastCellNum();
		for(int x = 0;x<=rowlen;x++){
			Cell cell =rowT.getCell(x);
			if(null != cell){
				listT.add(cell.getStringCellValue());
			}
		}
		int y = 1;
		for(Map<String, Object> son : list){
			Row row =sheet.createRow(y);
			int c = 0;
			for(String str : listT){
				Cell cell =row.createCell(c);
				if(null != son.get(str)){
					cell.setCellValue(son.get(str).toString());
				}
				c++;
			}
		    y++;
		}
		
	}
	
	public void splitSerialNum(Product product){
		if(null != product.getSerialNum()&&!("").equals(product.getSerialNum())){
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
		}
		if(null != product.getOrderNum()&&!("").equals(product.getOrderNum())){
			StringBuffer m = new StringBuffer();
			a:for(int j = 0;j<product.getOrderNum().length();j++){
			char v = product.getOrderNum().charAt(j);
			if (Character.isDigit(v)) {
				m.append(v);
	        }else{
	        	break a;
	        }
		}
			if(product.getOrderNum().contains("B")||product.getOrderNum().contains("b")){
				product.setOrdere(5555);
				String[] x = product.getOrderNum().split("B");
				if(x.length>=2){
					try {
						Integer y = Integer.parseInt(x[1]);
						product.setOrdere(5555+y);
					} catch (Exception e) {
						product.setOrdere(5555);
					}
				}
			}else if(product.getOrderNum().contains("M")||product.getOrderNum().contains("m")){
				product.setOrdere(6666);
			}
			else if(product.getOrderNum().contains("P")||product.getOrderNum().contains("p")){
				product.setOrdere(7777);
			}
			else if(product.getOrderNum().contains("S")||product.getOrderNum().contains("s")){
            	product.setOrdere(8888);
			}else if(product.getOrderNum().contains("F")||product.getOrderNum().contains("f")){
            	product.setOrdere(8889);
			}else{
				product.setOrdere(0);
			}
			if(null !=m&&!("").equals(m)){
//				System.out.println(m);
				try {
					product.setOrderc(Integer.parseInt(m.toString()));
				} catch (Exception e) {
					product.setOrderc(9999);
					e.printStackTrace();
				}
			}
		}else{
			product.setOrderc(9999);
			product.setOrdere(9999);
		}
		if(null != product.getSerialNum()&&!("").equals(product.getSerialNum())){
			product.setSerialNum(product.getSerialNum().trim().replace("~", "-"));
			String[] xx = product.getSerialNum().split("-");
			StringBuffer m = new StringBuffer();
			m.append("");
			if(xx.length==2){
				product.setOrdera(xx[0]);
				for(int j = 0;j<xx[1].length();j++){
					char v = xx[1].charAt(j);
					if (Character.isDigit(v)) {
						m.append(v);
		            }
				}
				product.setOrderd(xx[1]);
			}else if(xx.length==3){
				product.setOrdera(xx[0]+xx[1]);
				for(int j = 0;j<xx[2].length();j++){
					char v = xx[2].charAt(j);
					if (Character.isDigit(v)) {
						m.append(v);
		            }
				}
				product.setOrderd(xx[2]);
			}else{
				product.setOrdera(product.getSerialNum());
			}
			if(!("").equals(m.toString())){
				product.setOrderb(Integer.parseInt(m.toString()));
			}
		}
		
	}
	
	public void processInit(Product p){
		if(p.getIscp()!=null&&p.getIscp().intValue()==1){
			if(null !=p.getNextTempSonId()){
				p.setBlankDateA(p.getBlankDateA()==null||("").equals(p.getBlankDateA())?"-":p.getBlankDateA());
				p.setBlankRemarkA(p.getBlankRemarkA()==null||("").equals(p.getBlankRemarkA())?"-":p.getBlankRemarkA());
				p.setForgingInDateA(p.getForgingInDateA()==null||("").equals(p.getForgingInDateA())?"-":p.getForgingInDateA());
				p.setForgingOutDateA(p.getForgingOutDateA()==null||("").equals(p.getForgingOutDateA())?"-":p.getForgingOutDateA());
				p.setHeatTreatInDateA(p.getHeatTreatInDateA()==null||("").equals(p.getHeatTreatInDateA())?"-":p.getHeatTreatInDateA());
				p.setHeatTreatOutDateA(p.getHeatTreatOutDateA()==null||("").equals(p.getHeatTreatOutDateA())?"-":p.getHeatTreatOutDateA());
			}else{
				ProcessTempSon son = tempSonDao.findOne(p.getTempSonId());
				if(son.getProcessId().intValue()==4){
					p.setBlankDateA(p.getBlankDateA()==null||("").equals(p.getBlankDateA())?"-":p.getBlankDateA());
					p.setBlankRemarkA(p.getBlankRemarkA()==null||("").equals(p.getBlankRemarkA())?"-":p.getBlankRemarkA());
				}
				if(son.getProcessId().intValue()==5){
					p.setForgingInDateA(p.getForgingInDateA()==null||("").equals(p.getForgingInDateA())?"-":p.getForgingInDateA());
					p.setForgingOutDateA(p.getForgingOutDateA()==null||("").equals(p.getForgingOutDateA())?"-":p.getForgingOutDateA());
				}
				if(son.getProcessId().intValue()==6){
					p.setHeatTreatInDateA(p.getHeatTreatInDateA()==null||("").equals(p.getHeatTreatInDateA())?"-":p.getHeatTreatInDateA());
					p.setHeatTreatOutDateA(p.getHeatTreatOutDateA()==null||("").equals(p.getHeatTreatOutDateA())?"-":p.getHeatTreatOutDateA());
				}
			}
		}else{
			if(null != p.getTempSonId()){
				ProcessTempSon s = tempSonDao.findOne(p.getTempSonId());
				if(s.getProcessId().intValue()==4){//下料
					p.setBlankDateA(p.getBlankDateA()==null||("").equals(p.getBlankDateA())?"-":p.getBlankDateA());
					p.setBlankRemarkA(p.getBlankRemarkA()==null||("").equals(p.getBlankRemarkA())?"-":p.getBlankRemarkA());
					p.setForgingInDateA(p.getForgingInDateA()==null||("").equals(p.getForgingInDateA())?"-":p.getForgingInDateA());
					p.setForgingOutDateA(p.getForgingOutDateA()==null||("").equals(p.getForgingOutDateA())?"-":p.getForgingOutDateA());
					p.setHeatTreatInDateA(p.getHeatTreatInDateA()==null||("").equals(p.getHeatTreatInDateA())?"-":p.getHeatTreatInDateA());
					p.setHeatTreatOutDateA(p.getHeatTreatOutDateA()==null||("").equals(p.getHeatTreatOutDateA())?"-":p.getHeatTreatOutDateA());
					p.setBlankDate(p.getBlankDate()==null||("").equals(p.getBlankDate())?"-":p.getBlankDate());
					p.setBlankRemark(p.getBlankRemark()==null||("").equals(p.getBlankRemark())?"-":p.getBlankRemark());
					p.setHeatNum(p.getHeatNum()==null||("").equals(p.getHeatNum())?"-":p.getHeatNum());
				}
				if(s.getProcessId().intValue()==5){//锻造
					p.setForgingWorker(p.getForgingWorker()==null||("").equals(p.getForgingWorker())?"-":p.getForgingWorker());
					p.setForgingInDate(p.getForgingInDate()==null||("").equals(p.getForgingInDate())?"-":p.getForgingInDate());
					p.setForgingOutDate(p.getForgingOutDate()==null||("").equals(p.getForgingOutDate())?"-":p.getForgingOutDate());
					p.setForgingRemark(p.getForgingRemark()==null||("").equals(p.getForgingRemark())?"-":p.getForgingRemark());
				}
				if(s.getProcessId().intValue()==6){//锻后热处理
					p.setAfterForgingWorker(p.getAfterForgingWorker()==null||("").equals(p.getAfterForgingWorker())?"-":p.getAfterForgingWorker());
					p.setAfterForgingInDate(p.getAfterForgingInDate()==null||("").equals(p.getAfterForgingInDate())?"-":p.getAfterForgingInDate());
					p.setAtterForgingOutDate(p.getAtterForgingOutDate()==null||("").equals(p.getAtterForgingOutDate())?"-":p.getAtterForgingOutDate());
					
				}
				if(s.getProcessId().intValue()==7){//粗加工
					p.setRoughMachingWorker(p.getRoughMachingWorker()==null||("").equals(p.getRoughMachingWorker())?"-":p.getRoughMachingWorker());
					p.setRoughMachingInDate(p.getRoughMachingInDate()==null||("").equals(p.getRoughMachingInDate())?"-":p.getRoughMachingInDate());
					p.setRoughMachingOutDate(p.getRoughMachingOutDate()==null||("").equals(p.getRoughMachingOutDate())?"-":p.getRoughMachingOutDate());
					
				}
				if(s.getProcessId().intValue()==8){//性能热处理
					p.setPerHeatTreatWorker(p.getPerHeatTreatWorker()==null||("").equals(p.getPerHeatTreatWorker())?"-":p.getPerHeatTreatWorker());
					p.setPerHeatTreatInDate(p.getPerHeatTreatInDate()==null||("").equals(p.getPerHeatTreatInDate())?"-":p.getPerHeatTreatInDate());
					p.setPerHeatTreatOutDate(p.getPerHeatTreatOutDate()==null||("").equals(p.getPerHeatTreatOutDate())?"-":p.getPerHeatTreatOutDate());
					
				}
				if(s.getProcessId().intValue()==9){//交货加工
					p.setDeliveryWorker(p.getDeliveryWorker()==null||("").equals(p.getDeliveryWorker())?"-":p.getDeliveryWorker());
					p.setDeliveryInDate(p.getDeliveryInDate()==null||("").equals(p.getDeliveryInDate())?"-":p.getDeliveryInDate());
					p.setDeliveryOutDate(p.getDeliveryOutDate()==null||("").equals(p.getDeliveryOutDate())?"-":p.getDeliveryOutDate());
					
				}
				if(s.getProcessId().intValue()==10){//后续热处理
					p.setFollowupInDate(p.getFollowupInDate()==null||("").equals(p.getFollowupInDate())?"-":p.getFollowupInDate());
					p.setFollowupOutDate(p.getFollowupOutDate()==null||("").equals(p.getFollowupOutDate())?"-":p.getFollowupOutDate());
					p.setFollowupWorker(p.getFollowupWorker()==null||("").equals(p.getFollowupWorker())?"-":p.getFollowupWorker());
					
				}
				if(s.getProcessId().intValue()==11){//半精加工
					p.setHalfFinishMachingWorker(p.getHalfFinishMachingWorker()==null||("").equals(p.getHalfFinishMachingWorker())?"-":p.getHalfFinishMachingWorker());
					p.setHalfFinishMachingInDate(p.getHalfFinishMachingInDate()==null||("").equals(p.getHalfFinishMachingInDate())?"-":p.getHalfFinishMachingInDate());
					p.setHalfFinishMachingOutDate(p.getHalfFinishMachingOutDate()==null||("").equals(p.getHalfFinishMachingOutDate())?"-":p.getHalfFinishMachingOutDate());
					
				}
				if(s.getProcessId().intValue()==12){//精加工
					p.setFinishMachingWorker(p.getFinishMachingWorker()==null||("").equals(p.getFinishMachingWorker())?"-":p.getFinishMachingWorker());
					p.setFinishMachingInDate(p.getFinishMachingInDate()==null||("").equals(p.getFinishMachingInDate())?"-":p.getFinishMachingInDate());
					p.setFinishMachingOutDate(p.getFinishMachingOutDate()==null||("").equals(p.getFinishMachingOutDate())?"-":p.getFinishMachingOutDate());
				}
				if(s.getProcessId().intValue()==13){//取样
					p.setSampleInDate(p.getSampleInDate()==null||("").equals(p.getSampleInDate())?"-":p.getSampleInDate());
					p.setSampleOutDate(p.getSampleOutDate()==null||("").equals(p.getSampleOutDate())?"-":p.getSampleOutDate());
				}		
			}
		}
	}
	
	public void processInit2(Product p){
		if(p.getIscp()!=null&&p.getIscp().intValue()==1){
			if(null !=p.getNextTempSonId()){
				p.setBlankDateA(p.getBlankDateA()==null||("").equals(p.getBlankDateA())?"-":p.getBlankDateA());
				p.setBlankRemarkA(p.getBlankRemarkA()==null||("").equals(p.getBlankRemarkA())?"-":p.getBlankRemarkA());
				p.setForgingInDateA(p.getForgingInDateA()==null||("").equals(p.getForgingInDateA())?"-":p.getForgingInDateA());
				p.setForgingOutDateA(p.getForgingOutDateA()==null||("").equals(p.getForgingOutDateA())?"-":p.getForgingOutDateA());
			}else{
				ProcessTempSon son = tempSonDao.findOne(p.getTempSonId());
				if(son.getProcessId().intValue()==5){
					p.setBlankDateA(p.getBlankDateA()==null||("").equals(p.getBlankDateA())?"-":p.getBlankDateA());
					p.setBlankRemarkA(p.getBlankRemarkA()==null||("").equals(p.getBlankRemarkA())?"-":p.getBlankRemarkA());
				}
				if(son.getProcessId().intValue()==6){
					p.setBlankDateA(p.getBlankDateA()==null||("").equals(p.getBlankDateA())?"-":p.getBlankDateA());
					p.setBlankRemarkA(p.getBlankRemarkA()==null||("").equals(p.getBlankRemarkA())?"-":p.getBlankRemarkA());
					p.setForgingInDateA(p.getForgingInDateA()==null||("").equals(p.getForgingInDateA())?"-":p.getForgingInDateA());
					p.setForgingOutDateA(p.getForgingOutDateA()==null||("").equals(p.getForgingOutDateA())?"-":p.getForgingOutDateA());
				}
			}
		}else{
			if(null != p.getTempSonId()){
				ProcessTempSon son = tempSonDao.findOne(p.getTempSonId());
				List<ProcessTempSon> listAll = tempSonDao.findByParentId(son.getParentId());
				for(ProcessTempSon s: listAll){
					if(son.getOrderNum().intValue()>s.getOrderNum().intValue()){
						if(s.getProcessId().intValue()==4){//下料
							p.setBlankDateA(p.getBlankDateA()==null||("").equals(p.getBlankDateA())?"-":p.getBlankDateA());
							p.setBlankRemarkA(p.getBlankRemarkA()==null||("").equals(p.getBlankRemarkA())?"-":p.getBlankRemarkA());
							p.setForgingInDateA(p.getForgingInDateA()==null||("").equals(p.getForgingInDateA())?"-":p.getForgingInDateA());
							p.setForgingOutDateA(p.getForgingOutDateA()==null||("").equals(p.getForgingOutDateA())?"-":p.getForgingOutDateA());
							p.setHeatTreatInDateA(p.getHeatTreatInDateA()==null||("").equals(p.getHeatTreatInDateA())?"-":p.getHeatTreatInDateA());
							p.setHeatTreatOutDateA(p.getHeatTreatOutDateA()==null||("").equals(p.getHeatTreatOutDateA())?"-":p.getHeatTreatOutDateA());
							p.setBlankDate(p.getBlankDate()==null||("").equals(p.getBlankDate())?"-":p.getBlankDate());
							p.setBlankRemark(p.getBlankRemark()==null||("").equals(p.getBlankRemark())?"-":p.getBlankRemark());
							p.setHeatNum(p.getHeatNum()==null||("").equals(p.getHeatNum())?"-":p.getHeatNum());
						}
						if(s.getProcessId().intValue()==5){//锻造
							p.setForgingWorker(p.getForgingWorker()==null||("").equals(p.getForgingWorker())?"-":p.getForgingWorker());
							p.setForgingInDate(p.getForgingInDate()==null||("").equals(p.getForgingInDate())?"-":p.getForgingInDate());
							p.setForgingOutDate(p.getForgingOutDate()==null||("").equals(p.getForgingOutDate())?"-":p.getForgingOutDate());
							p.setForgingRemark(p.getForgingRemark()==null||("").equals(p.getForgingRemark())?"-":p.getForgingRemark());
						}
						if(s.getProcessId().intValue()==6){//锻后热处理
							p.setAfterForgingWorker(p.getAfterForgingWorker()==null||("").equals(p.getAfterForgingWorker())?"-":p.getAfterForgingWorker());
							p.setAfterForgingInDate(p.getAfterForgingInDate()==null||("").equals(p.getAfterForgingInDate())?"-":p.getAfterForgingInDate());
							p.setAtterForgingOutDate(p.getAtterForgingOutDate()==null||("").equals(p.getAtterForgingOutDate())?"-":p.getAtterForgingOutDate());
							
						}
						if(s.getProcessId().intValue()==7){//粗加工
							p.setRoughMachingWorker(p.getRoughMachingWorker()==null||("").equals(p.getRoughMachingWorker())?"-":p.getRoughMachingWorker());
							p.setRoughMachingInDate(p.getRoughMachingInDate()==null||("").equals(p.getRoughMachingInDate())?"-":p.getRoughMachingInDate());
							p.setRoughMachingOutDate(p.getRoughMachingOutDate()==null||("").equals(p.getRoughMachingOutDate())?"-":p.getRoughMachingOutDate());
							
						}
						if(s.getProcessId().intValue()==8){//性能热处理
							p.setPerHeatTreatWorker(p.getPerHeatTreatWorker()==null||("").equals(p.getPerHeatTreatWorker())?"-":p.getPerHeatTreatWorker());
							p.setPerHeatTreatInDate(p.getPerHeatTreatInDate()==null||("").equals(p.getPerHeatTreatInDate())?"-":p.getPerHeatTreatInDate());
							p.setPerHeatTreatOutDate(p.getPerHeatTreatOutDate()==null||("").equals(p.getPerHeatTreatOutDate())?"-":p.getPerHeatTreatOutDate());
							
						}
						if(s.getProcessId().intValue()==9){//交货加工
							p.setDeliveryWorker(p.getDeliveryWorker()==null||("").equals(p.getDeliveryWorker())?"-":p.getDeliveryWorker());
							p.setDeliveryInDate(p.getDeliveryInDate()==null||("").equals(p.getDeliveryInDate())?"-":p.getDeliveryInDate());
							p.setDeliveryOutDate(p.getDeliveryOutDate()==null||("").equals(p.getDeliveryOutDate())?"-":p.getDeliveryOutDate());
							
						}
						if(s.getProcessId().intValue()==10){//后续热处理
							p.setFollowupInDate(p.getFollowupInDate()==null||("").equals(p.getFollowupInDate())?"-":p.getFollowupInDate());
							p.setFollowupOutDate(p.getFollowupOutDate()==null||("").equals(p.getFollowupOutDate())?"-":p.getFollowupOutDate());
							p.setFollowupWorker(p.getFollowupWorker()==null||("").equals(p.getFollowupWorker())?"-":p.getFollowupWorker());
							
						}
						if(s.getProcessId().intValue()==11){//半精加工
							p.setHalfFinishMachingWorker(p.getHalfFinishMachingWorker()==null||("").equals(p.getHalfFinishMachingWorker())?"-":p.getHalfFinishMachingWorker());
							p.setHalfFinishMachingInDate(p.getHalfFinishMachingInDate()==null||("").equals(p.getHalfFinishMachingInDate())?"-":p.getHalfFinishMachingInDate());
							p.setHalfFinishMachingOutDate(p.getHalfFinishMachingOutDate()==null||("").equals(p.getHalfFinishMachingOutDate())?"-":p.getHalfFinishMachingOutDate());
							
						}
						if(s.getProcessId().intValue()==12){//精加工
							p.setFinishMachingWorker(p.getFinishMachingWorker()==null||("").equals(p.getFinishMachingWorker())?"-":p.getFinishMachingWorker());
							p.setFinishMachingInDate(p.getFinishMachingInDate()==null||("").equals(p.getFinishMachingInDate())?"-":p.getFinishMachingInDate());
							p.setFinishMachingOutDate(p.getFinishMachingOutDate()==null||("").equals(p.getFinishMachingOutDate())?"-":p.getFinishMachingOutDate());
						}
						if(s.getProcessId().intValue()==13){//取样
							p.setSampleInDate(p.getSampleInDate()==null||("").equals(p.getSampleInDate())?"-":p.getSampleInDate());
							p.setSampleOutDate(p.getSampleOutDate()==null||("").equals(p.getSampleOutDate())?"-":p.getSampleOutDate());
						}
					}
				}
			}
		}
	}

}
