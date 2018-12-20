package com.hg.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hg.dao.OperateRecordDao;
import com.hg.dao.ProductDao;
import com.hg.dao.ProductJPADao;
import com.hg.dao.SysProcessDao;
import com.hg.dao.TempSonDao;
import com.hg.dao.UserMessageDao;
import com.hg.dao.ncr.NcrDao;
import com.hg.dao.ncr.NcrFilesDao;
import com.hg.dao.ncr.NcrSonDao;
import com.hg.model.Ncr;
import com.hg.model.NcrFiles;
import com.hg.model.NcrSon;
import com.hg.model.OperateRecord;
import com.hg.model.ProcessTempSon;
import com.hg.model.Product;
import com.hg.model.SysProcess;
import com.hg.model.UserMessage;
import com.hg.service.NcrService;
import com.hg.service.ProductService;
import com.hg.service.ReportService;
import com.hg.util.DateUtil;
import com.hg.util.FileOperateUtil;
import com.hg.util.ItextUtil;
import com.hg.util.OtherUtil;
import com.hg.util.PageInfo;
import com.hg.util.PageResponse;
import com.itextpdf.text.DocumentException;


@Controller
@RequestMapping("ncr")
public class NcrController {
	
	@Inject
	private NcrDao ncrDao;
	@Inject
	private ProductJPADao productJPADao;
	@Inject
	private ProductDao productDao;
	@Inject
	private TempSonDao tempSonDao;
	@Inject
	private SysProcessDao sysProcessDao;
	@Inject
	private OperateRecordDao operateRecordDao;
	@Inject
	private NcrFilesDao ncrFilesDao;
	@Inject
	private NcrSonDao ncrSonDao;
	@Inject
	private NcrService ncrService;
 	@Inject
 	private ProductService productService;
 	@Inject
 	private UserMessageDao userMessageDao;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	@RequestMapping("/toNcr")
	public String toNcr(Model model,String ids){
		String[] s = ids.split(",");
		Product p = productJPADao.findOne(Long.parseLong(s[0]));
		model.addAttribute("ids", ids);
		model.addAttribute("forgingWorker", p.getForgingWorker());
	    model.addAttribute("opTime", DateUtil.getCurrentymd());
		return "main/ncr/toNcr";
	}
	
	
	@RequestMapping("/doNcr")
	@ResponseBody
	public String doNcr(Ncr ncr,HttpSession session,Ncr n,@RequestParam(value = "ids", required = false) List<Long> ids){
		String userChnName = (String) session.getAttribute("userChnName");
		
		List<Product> list = (List<Product>) productJPADao.findAll(ids);
		ncr.setState(1);
		ProcessTempSon pson = tempSonDao.findOne(list.get(0).getTempSonId());
		SysProcess sp = sysProcessDao.findOne(pson.getProcessId());
//		ncr.setFromProcess(sp.getChnName());
		ncr.setOperator(userChnName);
		ncr.setOpTime(DateUtil.getCurrentTime());
		ncrDao.save(ncr);
		StringBuffer snum = new StringBuffer();
		StringBuffer order = new StringBuffer();
		for(Product pro : list){
			snum.append(pro.getSerialNum()).append("<br>");
			order.append(pro.getOrderNum()).append("<br>");
			if(n.getFiles()!=null&&n.getFiles().size()>0){
				for(NcrFiles files : n.getFiles()){
					files.setParentId(ncr.getId());
					String[] fs = files.getFile().split("/");
					if(fs.length==4){
						files.setName(fs[3]);
					}
					ncrFilesDao.save(files);
				}
			}
			pro.setState(5);
			productJPADao.save(pro);
			NcrSon son = new NcrSon();
			son.setParentId(ncr.getId());
			son.setProductId(pro.getId());
			ncrSonDao.save(son);
			OperateRecord operRecord = new OperateRecord();
	    	operRecord.setOperateTime(DateUtil.getCurrentTime());
	    	operRecord.setProductId(pro.getId());
	    	operRecord.setUsername(userChnName);
	    	operRecord.setElementName("从"+sp.getChnName()+"发起不合格");
			operateRecordDao.save(operRecord);
		}
		ncr.setSerialNum(snum.toString());
		ncr.setOrderNum(order.toString().replace("null", "-"));
		ncrDao.save(ncr);
		return "success";
	}
	
	@RequestMapping("/list")
	public String productList(){
		return "main/ncr/list";
	}
	
	
	
	@RequestMapping("/getNcrList")
	@ResponseBody
	public Object getNcrList(PageInfo page,Ncr bean){
		
		return ncrService.queryList(page, bean);
	}
	
	@RequestMapping("/checkFile")
	public Object checkFile(Long id,Model model){
		List<NcrFiles> list = ncrFilesDao.findByParentId(id);
		model.addAttribute("list", list);
		return "main/ncr/filemanage";
	}
	
	@RequestMapping("/getNcrSon")
	public Object getNcrSon(Long id,Model model){
		Ncr ncr = ncrDao.findOne(id);
		   List<NcrSon> list = ncrSonDao.findByParentId(id);
		   for(NcrSon son : list){
			   try {
				   BigDecimal total = new BigDecimal(son.getQty()).multiply(new BigDecimal(son.getBlankWeight()));
				   son.setTotalWeight(total.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		   }
		   model.addAttribute("list", list);
		   model.addAttribute("ncr", ncr);
		return "main/ncr/detail";
	}
	
	@RequestMapping("/toHandle")
	public String toHandle(Long id,Model model){
		Ncr ncr = ncrDao.findOne(id);
		
		List<NcrFiles> listFile = ncrFilesDao.findByParentId(id);
		for(NcrFiles f : listFile){
			f.setFile("/files"+f.getFile());
		}
		List<NcrSon> listSon = ncrSonDao.findByParentId(ncr.getId());
		for(NcrSon son : listSon){
			Product product = productJPADao.findOne(son.getProductId());
			son.setSerialNum(product.getSerialNum());
			son.setOrderNum(product.getOrderNum());
		}
		model.addAttribute("bean", ncr);
		model.addAttribute("listFile", listFile);
		model.addAttribute("listSon", listSon);
		return "main/ncr/handle";
	}

	@RequestMapping("/doHandle")
	@ResponseBody
	public String doHandle(Ncr ncr, HttpSession session, Ncr nn) throws FileNotFoundException, DocumentException {
		Ncr n = ncrDao.findOne(ncr.getId());
		String userChnName = (String) session.getAttribute("userChnName");
		Long userId = (Long) session.getAttribute("userId");
		n.setHandler(userChnName);
		n.setState(2);
		n.setHandleTime(DateUtil.getCurrentTime());
		n.setHandleType(ncr.getHandleType());
		n.setRemarks(ncr.getRemarks());
		n.setBackDate(ncr.getBackDate());
		n.setResponsibleDep(ncr.getResponsibleDep());
		n.setExameend(ncr.getExameend());
		n.setExamestart(ncr.getExamestart());
		n.setExecuteUnit(ncr.getExecuteUnit());
		List<Long> listid = new ArrayList<Long>();
		for (NcrSon ncrson : nn.getListSon()) {
			ncrSonDao.save(ncrson);
			listid.add(ncrson.getProductId());
		}
		
//		List<String> list = new ArrayList<String>();
//		List<Product> listpp = (List<Product>) productJPADao.findAll(listid);
//		String a = ReportService.ncrFirstPage(n,nn.getListSon(),listpp);
//		list.add(a);
//		
//		List<NcrFiles> listFile = ncrFilesDao.findByParentId(n.getId());
//        if(listFile!=null&&listFile.size()>0){
//        	String b = ReportService.ncrSecondPage(n,nn.getListSon(),listpp,listFile);
//        	list.add(b);
//		}
//        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//    	String now = simple.format(new Date());
//		OtherUtil.fileMkdir(OtherUtil.getAbsolutePath()+"ncrAllReport/"+now);
//    	ItextUtil.morePdfTopdf(list, OtherUtil.getAbsolutePath()+"ncrAllReport/"+now+"/"+n.getNcrNum()+".pdf");
//        n.setFilePath("ncrAllReport/"+now+"/"+n.getNcrNum()+".pdf");
        ncrDao.save(n);
        StringBuffer type = new StringBuffer();
		for (NcrSon ncrson : nn.getListSon()) {
			NcrSon ncrson2 = ncrSonDao.findOne(ncrson.getId());
			OperateRecord operRecord = new OperateRecord();
			if (ncrson.getHandleType().intValue() == 1) {
				Product pro = productJPADao.findOne(ncrson.getProductId());
				pro.setState(7);
				pro.setOrdere(0);
				productJPADao.save(pro);
				ncrson2.setState(1);
				ncrSonDao.save(ncrson2);
				type.append("返工").append("<br>");
				n.setState(3);
//				List<OperateRecord> listO = operateRecordDao
//						.findByProductId(pro.getId());
//				entityManager.clear();
//				pro.setId(null);
//				pro.setState(ncrson.getState());
//				pro.setOrdere(1);
//				if (null != pro.getNextTempSonId()) {
//					ProcessTempSon son = tempSonDao.findOne(pro
//							.getNextTempSonId());
//					List<ProcessTempSon> listp = tempSonDao.findByParentId(son
//							.getParentId());
//					a: for (ProcessTempSon s : listp) {
//						if (s.getProcessId().intValue() == ncrson
//								.getProcessId().intValue()) {
//							pro.setTempSonId(s.getId());
//							break a;
//						}
//					}
//				} else {
//					ProcessTempSon son = tempSonDao.findOne(pro.getTempSonId());
//					List<ProcessTempSon> listp = tempSonDao.findByParentId(son
//							.getParentId());
//					a: for (ProcessTempSon s : listp) {
//						if (s.getProcessId().intValue() == ncrson
//								.getProcessId().intValue()) {
//							pro.setTempSonId(s.getId());
//							break a;
//						}
//					}
//				}
//				productJPADao.save(pro);
				operRecord.setElementName("处置为返工");
//				UserMessage message = new UserMessage();
//				message.setMessageTime(DateUtil.getCurrentTime());
//				message.setMyId(userId);
//				message.setOperator(userChnName);
//				message.setState(1);
//				message.setType(1);
//				message.setSubject(pro.getSerialNum()+" ~ "+pro.getOrderNum()+"处置为返工");
//				message.setUserId(3L);
//				userMessageDao.save(message);
//				for (OperateRecord op : listO) {
//					op.setId(null);
//					op.setProductId(pro.getId());
//					operateRecordDao.save(op);
//				}
			} else if (ncrson.getHandleType().intValue() == 2) {
				Product pro = productJPADao.findOne(ncrson.getProductId());
				pro.setState(8);
				productJPADao.save(pro);
				operRecord.setElementName("处置为报废");
				type.append("报废").append("<br>");
			} else if (ncrson.getHandleType().intValue() == 3) {
				Product pro = productJPADao.findOne(ncrson.getProductId());
				pro.setState(4);
				productJPADao.save(pro);
				operRecord.setElementName("处置为让步");
				type.append("让步").append("<br>");
			} else if (ncrson.getHandleType().intValue() == 4) {
				n.setState(3);
				ncrson2.setState(1);
				ncrSonDao.save(ncrson2);
				Product pro = productJPADao.findOne(ncrson.getProductId());
				pro.setState(20);
				productJPADao.save(pro);
				operRecord.setElementName("处置为返修");
//				UserMessage message = new UserMessage();
//				message.setMessageTime(DateUtil.getCurrentTime());
//				message.setMyId(userId);
//				message.setOperator(userChnName);
//				message.setState(1);
//				message.setType(1);
//				message.setSubject(pro.getSerialNum()+" ~ "+pro.getOrderNum()+"处置为返修");
//				message.setUserId(3L);
//				userMessageDao.save(message);
				type.append("申请").append("<br>");
			}else if (ncrson.getHandleType().intValue() == 5) {
				Product pro = productJPADao.findOne(ncrson.getProductId());
				pro.setState(21);
				productJPADao.save(pro);
				operRecord.setElementName("处置为退货");
				type.append("返修").append("<br>");
			}else if (ncrson.getHandleType().intValue() == 6) {
				Product pro = productJPADao.findOne(ncrson.getProductId());
				pro.setState(22);
				productJPADao.save(pro);
				operRecord.setElementName("处置为申请");
				type.append("退货").append("<br>");
			}
	    	operRecord.setOperateTime(DateUtil.getCurrentTime());
	    	operRecord.setProductId(ncrson.getProductId());
	    	operRecord.setUsername(userChnName);
			operateRecordDao.save(operRecord);
			
		}
		ncrDao.save(n);
		n.setHandleType(type.toString());
		 ncrDao.save(n);
		return "success";
	}
	
	
	@RequestMapping("/toSplit")
	public String toSplit(Long id,Model model,Long ncrId){
		Product product = productJPADao.findOne(id);
		model.addAttribute("bean", product);
		model.addAttribute("ncrId", ncrId);
		return "main/ncr/spilt";
	}
	
	@RequestMapping("/doSplit")
	@ResponseBody
	public String doSplit(Product p,String orderNumx,HttpSession session,Long ncrId){
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
		}
		for(Product pro : p.getListP()){
			if(x==0){
				product.setSerialNum(pro.getSerialNum());
				product.setOrderNum(pro.getOrderNum());
				productService.splitSerialNum(product);
				product.setQty(pro.getQty());
				productJPADao.save(product);
				entityManager.clear();
			}else{
				product.setId(null);
				product.setPoQty(null);
				product.setSerialNum(pro.getSerialNum());
				product.setOrderNum(pro.getOrderNum());
				productService.splitSerialNum(product);
				product.setQty(pro.getQty());
				productJPADao.save(product);
				NcrSon son = new NcrSon();
				son.setParentId(ncrId);
				son.setProductId(product.getId());
				ncrSonDao.save(son);
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
	    	operRecord.setElementName(product.getSerialNum()+" "+orderNumx+"-> 拆分为:锻件编号:"+product.getSerialNum()+" 序号:"+product.getOrderNum()+" 数量:"+product.getQty());
    		operateRecordDao.save(operRecord);
			x++;
		}
		return "success";
	}
	
	
	@RequestMapping("/toHandleb")
	public String toHandleb(Long id,Model model,Long ncrSonId){
		Product product = productJPADao.findOne(id);
		
		ProcessTempSon son = new ProcessTempSon();
		if(null != product.getNextTempSonId()){
			son = tempSonDao.findOne(product.getNextTempSonId());
		}else{
			son = tempSonDao.findOne(product.getTempSonId());
		}
		List<ProcessTempSon> list = tempSonDao.findByParentId(son.getParentId());
		for(ProcessTempSon s : list){
			SysProcess process = sysProcessDao.findOne(s.getProcessId());
			s.setProcName(process.getChnName());
		}
		model.addAttribute("id", id);
		model.addAttribute("list", list);
		model.addAttribute("bean", product);
		model.addAttribute("ncrSonId", ncrSonId);
		return "main/ncr/handleb";
	}
	
	@RequestMapping("/doHandleb")
	@ResponseBody
	public String doHandleb(Long id,HttpSession session,Long tempSonId,Long ncrSonId,
			Integer handleType,String serialNum,String orderNum,Integer state){
		if(null != ncrSonId){
			NcrSon ncrSon  = ncrSonDao.findOne(ncrSonId);
			ncrSon.setState(2);
			ncrSonDao.save(ncrSon);
			List<NcrSon> listss = ncrSonDao.findByParentIdAndState(ncrSon.getParentId(),1);
			if(listss==null||listss.size()==0){
				Ncr n = ncrDao.findOne(ncrSon.getParentId());
				n.setState(2);
				ncrDao.save(n);
			}
		}
		
		if(handleType.intValue()==1){
			Product pro = productJPADao.findOne(id);
			pro.setState(7);
			productJPADao.save(pro);
			entityManager.clear();
			pro.setId(null);
			pro.setState(state);
			pro.setSerialNum(serialNum);
			pro.setOrderNum(orderNum);
			pro.setTempSonId(tempSonId);
			productJPADao.save(pro);
		}else if(handleType.intValue()==2){
			Product pro = productJPADao.findOne(id);
			pro.setState(8);
			productJPADao.save(pro);
		}else if(handleType.intValue()==3){
			Product pro = productJPADao.findOne(id);
			pro.setState(4);
			productJPADao.save(pro);
		}else if(handleType.intValue()==4){
			Product pro = productJPADao.findOne(id);
			pro.setState(20);
			productJPADao.save(pro);
		}
		else if(handleType.intValue()==5){
			Product pro = productJPADao.findOne(id);
			pro.setState(21);
			productJPADao.save(pro);
			entityManager.clear();
			pro.setId(null);
			pro.setState(state);
			pro.setSerialNum(serialNum);
			pro.setOrderNum(orderNum);
			pro.setTempSonId(tempSonId);
			productJPADao.save(pro);
		}
		else if(handleType.intValue()==6){
			Product pro = productJPADao.findOne(id);
			pro.setState(22);
			productJPADao.save(pro);
		}
		return "success";
	}
	
	/**
	 * 
	 * 不合格报告下载
	 * @throws DocumentException 
	 * 
	 * */
	@RequestMapping("/downLoad")
	public void downLoad(HttpServletRequest request,HttpServletResponse response,Model model,Long id) throws IOException, DocumentException{
		Ncr ncr = ncrDao.findOne(id);
		List<NcrSon> listSon = ncrSonDao.findByParentId(id);
		List<String> list = new ArrayList<String>();
		List<Long> listid = new ArrayList<Long>();
		for (NcrSon ncrson : listSon) {
			ncrSonDao.save(ncrson);
			listid.add(ncrson.getProductId());
		}
		List<Product> listpp = (List<Product>) productJPADao.findAll(listid);
		String a = ReportService.ncrFirstPage(ncr,listSon,listpp);
		list.add(a);
		
		List<NcrFiles> listFile = ncrFilesDao.findByParentId(id);
        if(listFile!=null&&listFile.size()>0){
        	String b = ReportService.ncrSecondPage(ncr,listSon,listpp,listFile);
        	list.add(b);
		}
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    	String now = simple.format(new Date());
		OtherUtil.fileMkdir(OtherUtil.getAbsolutePath()+"ncrAllReport/"+now);
    	ItextUtil.morePdfTopdf(list, OtherUtil.getAbsolutePath()+"ncrAllReport/"+now+"/"+ncr.getNcrNum()+".pdf");
        //得到想客服端输出的输出流 
        OutputStream outputStream = response.getOutputStream();
        //输出文件用的字节数组，每次向输出流发送600个字节  
        byte b[] = new byte[600];  
        //要下载的文件  
        File fileload = new File(OtherUtil.getAbsolutePath()+"ncrAllReport/"+now+"/"+ncr.getNcrNum()+".pdf");       
        //客服端使用保存文件的对话框  
        String fileName = URLEncoder.encode(now+".pdf", "UTF-8");  
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);  
//        //通知客服文件的MIME类型  
        response.setContentType("application/octet-stream");  
        //通知客服文件的长度  
        long fileLength = fileload.length();  
        String length = String.valueOf(fileLength);  
        response.setHeader("Content_length", length);  
        //读取文件，并发送给客服端下载  
        FileInputStream inputStream = new FileInputStream(fileload);  
        int n = 0;  
        while((n=inputStream.read(b))!=-1){  
            outputStream.write(b,0,n);  
        }  
	} 
	
	
	/**
	 * 
	 * 探伤不合格报告下载
	 * @throws DocumentException 
	 * 
	 * */
	@RequestMapping("/downLoadb")
	public void downLoadb(HttpServletRequest request,HttpServletResponse response,Model model,Long id) throws IOException, DocumentException{
		Ncr ncr = ncrDao.findOne(id);
		List<NcrSon> listSon = ncrSonDao.findByParentId(id);
		List<String> list = new ArrayList<String>();
		List<Long> listid = new ArrayList<Long>();
		for (NcrSon ncrson : listSon) {
			ncrSonDao.save(ncrson);
			listid.add(ncrson.getProductId());
		}
		List<Product> listpp = (List<Product>) productJPADao.findAll(listid);
		String a = ReportService.ncrFirstPage(ncr,listSon,listpp);
		list.add(a);
		
		List<NcrFiles> listFile = ncrFilesDao.findByParentId(id);
        if(listFile!=null&&listFile.size()>0){
        	String c = ReportService.ncrThirdPage(ncr,listSon,listpp,listFile);
        	list.add(c);
		}
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    	String now = simple.format(new Date());
		OtherUtil.fileMkdir(OtherUtil.getAbsolutePath()+"ncrAllReport/"+now);
    	ItextUtil.morePdfTopdf(list, OtherUtil.getAbsolutePath()+"ncrAllReport/"+now+"/"+ncr.getNcrNum()+".pdf");
        //得到想客服端输出的输出流 
        OutputStream outputStream = response.getOutputStream();
        //输出文件用的字节数组，每次向输出流发送600个字节  
        byte b[] = new byte[600];  
        //要下载的文件  
        File fileload = new File(OtherUtil.getAbsolutePath()+"ncrAllReport/"+now+"/"+ncr.getNcrNum()+".pdf");       
        //客服端使用保存文件的对话框  
        String fileName = URLEncoder.encode(now+".pdf", "UTF-8");  
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);  
//        //通知客服文件的MIME类型  
        response.setContentType("application/octet-stream");  
        //通知客服文件的长度  
        long fileLength = fileload.length();  
        String length = String.valueOf(fileLength);  
        response.setHeader("Content_length", length);  
        //读取文件，并发送给客服端下载  
        FileInputStream inputStream = new FileInputStream(fileload);  
        int n = 0;  
        while((n=inputStream.read(b))!=-1){  
            outputStream.write(b,0,n);  
        }  
	} 
	
	@RequestMapping("/updateState")
	@ResponseBody
	public String updateState(@RequestParam(value = "ids", required = false) List<Long> ids,HttpSession session){
		List<Ncr> list = (List<Ncr>) ncrDao.findAll(ids);
		for(Ncr n : list){
			n.setState(2);
			n.setOperator((String)session.getAttribute("userChnName"));
			n.setOpTime(DateUtil.getCurrentymd());
			ncrDao.save(n);
		}
		return "success";
	}
	
	@RequestMapping(value = "download")
	public ModelAndView download(HttpServletRequest request,HttpServletResponse response,Long id) throws Exception {  
	  NcrFiles son = ncrFilesDao.findOne(id);
	  String realPath = request.getSession().getServletContext().getRealPath("/files"); 
	  String[] fs = son.getFile().split("/");
	  FileOperateUtil.download(request, response, realPath+son.getFile(),  
	  		son.getName());  
	  return null;  
	} 
}
