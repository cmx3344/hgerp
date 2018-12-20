package com.hg.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import com.itextpdf.text.pdf.PdfReader;
/**
 * 
 * <p>Title: ItextUtil</p>
 * <p>Description: PDF生成工具</p>
 * @author 陈明星
 * @date 下午4:22:56
 */
public class ItextUtil {
	
	/**
	 * 
	* @Title: getDocument   
	* @Description: 获取Document
	* @param marginLeft
	* @param marginRight
	* @param marginTop
	* @param marginBottom
	* @param @return 
	* @return Document
	* @throws
	 */
	public static Document getDocument(float marginLeft,float marginRight,float marginTop,float marginBottom){
		return new Document(PageSize.A4,marginLeft,marginRight,marginTop,marginBottom); 
	}
	
	/**
	 * 
	* @Title: getDocumentRotate   
	* @Description: 横向打印
	* @param @param marginLeft
	* @param @param marginRight
	* @param @param marginTop
	* @param @param marginBottom
	* @param @return 
	* @return Document
	* @throws
	 */
	public static Document getDocumentRotate(float marginLeft,float marginRight,float marginTop,float marginBottom){
		return new Document(PageSize.A4.rotate(),marginLeft,marginRight,marginTop,marginBottom); 
	}

	/**
	 * 
	* @Title: isMkdir   
	* @Description: 判断是否包含目录
	* @param  
	* @return void
	* @throws
	 */
	public static void isMkdir(File file){
		if(!file.exists()){
 			file.mkdir();
 		}
	}
	
	/**
	 * 
	* @Title: getBaseFont   
	* @Description: 获取字体Class
	* @param @return
	* @param @throws DocumentException
	* @param @throws IOException 
	* @return BaseFont
	* @throws
	 */
	public static BaseFont getBaseFont() throws DocumentException, IOException{
		return BaseFont.createFont("C:\\Windows\\Fonts\\simfang.ttf", BaseFont.IDENTITY_H, false);  
	}
	
	/**
	 * 
	* @Title: getFont   
	* @Description: 获取字体格式
	* @param bf
	* @param fontSize
	* @param fontStyle
	* @param color
	* @param @return 
	* @return Font
	* @throws
	 */
	public static Font getFont(BaseFont bf,float fontSize,int fontStyle){
		return new Font(bf,fontSize,fontStyle);
	}
	
	/**
	 * 
	* @Title: getPdfPTable   
	* @Description: 获取PdfPTable实例
	* @param relativeWidths
	* @param widthPercentage
	* @param @return 
	* @return PdfPTable
	* @throws
	 */
	public static PdfPTable getPdfPTable(float[] relativeWidths,float widthPercentage){
		PdfPTable table = new PdfPTable(relativeWidths);
		table.setWidthPercentage(widthPercentage);
		return table;
	}
	
	/**
	 * 
	* @Title: setPdfHeader   
	* @Description: 设置居中标题
	* @param table
	* @param percent
	* @param widthPercentage
	* @param font
	* @param minimumHeight
	* @param borderWidth
	* @param title 
	* @return void
	* @throws
	 */
	public static void setPdfHeader(PdfPTable table,int colspan,Font font,
			float minimumHeight,float borderWidth,String title){
		PdfPCell cell = new PdfPCell(new Paragraph(title,font));
		cell.setColspan(colspan);
        cell.setMinimumHeight(minimumHeight);
        cell.setBorderWidth(borderWidth);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);
	}
	
	
	/**
	 * 
	* @Title: setPdfHeader   
	* @Description: TODO
	* @param @param table
	* @param @param colspan
	* @param @param font
	* @param @param minimumHeight
	* @param @param borderWidth
	* @param @param title
	* @param @param paddingRight 
	* @return void
	* @throws
	 */
	public static void setPdfHeader(PdfPTable table,int colspan,Font font,
			float minimumHeight,float borderWidth,String title,float paddingRight){
		PdfPCell cell = new PdfPCell(new Paragraph(title,font));
		cell.setColspan(colspan);
        cell.setMinimumHeight(minimumHeight);
        cell.setBorderWidth(borderWidth);
        cell.setPaddingRight(paddingRight);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);
	}
	
	
	/**
	 * 
	* @Title: setPdfHeaderInRight   
	* @Description: 设置靠右标题
	* @param @param table
	* @param @param colspan
	* @param @param font
	* @param @param minimumHeight
	* @param @param borderWidth
	* @param @param title 
	* @return void
	* @throws
	 */
	public static void setPdfHeaderInRight(PdfPTable table,int colspan,Font font,
			float minimumHeight,float borderWidth,String title){
		PdfPCell cell = new PdfPCell(new Paragraph(title,font));
		cell.setColspan(colspan);
        cell.setMinimumHeight(minimumHeight);
        cell.setBorderWidth(borderWidth);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table.addCell(cell);
	}
	
	/**
	 * 
	* @Title: setPdfBody   
	* @Description: TODO
	* @param table
	* @param contents
	* @param font 
	* @return void
	* @throws
	 */
	public static void setPdfBody(PdfPTable table,PdfPCell[] cells){
		if (null != cells&&cells.length>0)
		for(int i = 0;i<cells.length;i++){
			table.addCell(cells[i]);
		}
	}
	
	/**
	 * 
	* @Title: getTextCell   
	* @Description: 设置并获取普通文本单元格
	* @param @param text 文本
	* @param @param font 字体格式
	* @param @return 
	* @return PdfPCell
	* @throws
	 */
	public static PdfPCell getTextCell(String text,Font font,float minimumHeight){
		PdfPCell cell = new PdfPCell(new Paragraph(text,font));
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setMinimumHeight(minimumHeight);
        return cell;
	}
	
	/**
	 * 
	* @Title: getTextCellByLeft   
	* @Description: 设置靠左单元格
	* @param @param text
	* @param @param font
	* @param @param minimumHeight
	* @param @return 
	* @return PdfPCell
	* @throws
	 */
	public static PdfPCell getTextCellByLeft(String text,Font font,float minimumHeight){
		PdfPCell cell = new PdfPCell(new Paragraph(text,font));
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setMinimumHeight(minimumHeight);
        return cell;
	}
	
	/**
	 * 
	* @Title: getTextCellByLeft   
	* @Description: 设置靠右单元格
	* @param @param text
	* @param @param font
	* @param @param minimumHeight
	* @param @return 
	* @return PdfPCell
	* @throws
	 */
	public static PdfPCell getTextCellByRight(String text,Font font,float minimumHeight){
		PdfPCell cell = new PdfPCell(new Paragraph(text,font));
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        cell.setMinimumHeight(minimumHeight);
        return cell;
	}
	
	/**
	 * 
	* @Title: getDefaultTextCell   
	* @Description: 获取默认单元格
	* @param @param text
	* @param @param font
	* @param @param minimumHeight
	* @param @return 
	* @return PdfPCell
	* @throws
	 */
	public static PdfPCell getDefaultTextCell(String text,Font font,float minimumHeight){
		PdfPCell cell = new PdfPCell(new Paragraph(text,font));
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setMinimumHeight(minimumHeight);
        return cell;
	}
	
	/**
	 * 
	* @Title: getTextCell   
	* @Description: 设置并获取普通文本单元格
	* @param text 文本
	* @param font 字体格式
	* @param verticalAlignment 垂直对齐 ——》 取值实例: PdfPCell.ALIGN_MIDDLE
	* @param horizontalAlignment 水平对齐方式 ——》 取值实例: PdfPCell.ALIGN_CENTER
	* @param @return 
	* @return PdfPCell
	* @throws
	 */
	public static PdfPCell getTextCell(String text,Font font,float minimumHeight,int horizontalAlignment,int verticalAlignment){
		PdfPCell cell = new PdfPCell(new Paragraph(text,font));
		cell.setMinimumHeight(minimumHeight);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(verticalAlignment);
        return cell;
	}
	
	/**
	 * 
	* @Title: getImageCell   
	* @Description: 设置并获取图片单元格
	* @param @param imgAbsoluteUrl
	* @param @param fitWidth
	* @param @param fitHeight
	* @param @return 
	* @return PdfPCell
	* @throws
	 */
	public static PdfPCell getImageCell(String imgAbsoluteUrl,float fitWidth,float fitHeight){
		PdfPCell cell ;
		try {
			Image image = Image.getInstance(imgAbsoluteUrl);
			// 设置图片的显示大小
			image.scaleAbsolute(fitWidth, fitHeight);
			cell = new PdfPCell(image);
		} catch (BadElementException | IOException e) {
			cell = new PdfPCell();
			e.printStackTrace();
		} 
		return cell;
	}
	
	/**
	 * 
	* @Title: getImageCell   
	* @Description: 设置并获取图片单元格
	* @param @param imgAbsoluteUrl
	* @param @return 
	* @return PdfPCell
	* @throws
	 */
	public static PdfPCell getFitImageCell(String imgAbsoluteUrl,float fitWidth,float fitHeight){
		PdfPCell cell ;
		try {
			Image image = Image.getInstance(imgAbsoluteUrl);
			// 设置图片的显示大小
			image.scaleToFit(fitWidth, fitHeight);
			cell = new PdfPCell(image);
		} catch (BadElementException | IOException e) {
			cell = new PdfPCell();
			e.printStackTrace();
		} 
		return cell;
	}
	
	/**
	 * 
	* @Title: getImageCell   
	* @Description: 设置并获取图片单元格
	* @param @param imgAbsoluteUrl
	* @param @param fitWidth
	* @param @param fitHeight
	* @param @param x
	* @param @param y
	* @param @return 
	* @return PdfPCell
	* @throws
	 */
	public static PdfPCell getImageCell(String imgAbsoluteUrl,float fitWidth,float fitHeight,float x,float y){
		PdfPCell cell ;
		try {
			Image image = Image.getInstance(imgAbsoluteUrl);
			// 设置图片的显示大小
			image.scaleToFit(fitWidth, fitHeight);
			image.setAbsolutePosition(x, y);
			cell = new PdfPCell(image);
		} catch (BadElementException | IOException e) {
			cell = new PdfPCell();
			e.printStackTrace();
		} 
		return cell;
	}
	
	/**
	 * 
	* @Title: getTableCell   
	* @Description: 获取table格式的单元格
	* @param @param table
	* @param @return 
	* @return PdfPCell
	* @throws
	 */
	public static PdfPCell getTableCell(PdfPTable table){
		PdfPCell cell = new PdfPCell(table);
		return cell;
	}
	
	/**
	 * 
	* @Title: morePdfTopdf   
	* @Description: 合并pdf
	* @param @param fileList
	* @param @param savepath 
	* @return void
	* @throws
	 */
	public static void morePdfTopdf(List<String> list, String savepath) {
		Document document = null;
        try {
            document = new Document(new PdfReader(list.get(0)).getPageSize(1));
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(savepath));
            document.open();
            for (int i = 0; i < list.size(); i++) {
            	if(list.get(i)==null||("").equals(list.get(i))){
            		continue;
            	}
            	try {
            		PdfReader reader = new PdfReader(list.get(i));
                    int n = reader.getNumberOfPages();// 获得总页码
                    for (int j = 1; j <= n; j++) {
                        document.newPage();
                        PdfImportedPage page = copy.getImportedPage(reader, j);// 从当前Pdf,获取第j页
                        copy.addPage(page);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
}
