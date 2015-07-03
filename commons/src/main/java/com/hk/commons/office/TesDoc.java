package com.hk.commons.office;

import java.awt.Color;
import java.io.FileOutputStream;
import java.util.Vector;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.RtfWriter2;

/*
 * author:yulu
 * date:2011/11/25
 * 
 * usage:this class is used to write text and jpg into doc file(word 2003 format)
 */

public class TesDoc {
	
	//默认图片缩放的百分比，因为itext插入图片会比原始的大，所以需要缩小
	//public static float IMAGESCALEPERCENT=50;
	public static float IMAGESCALEPERCENT=25;
	
	public static void setPicPercent(int per){
		IMAGESCALEPERCENT=per;
	}
	
	//run
//	public static String FONT_USED=PropertyUtils.getString("font_path")+PropertyUtils.getString("font_name");
	public static String FONT_USED="font_path" + "font_name";
	
	//test
	//public static String FONT_USED="C:/Windows/Fonts/SIMYOU.TTF";
	
	//html特殊字符转换函数
	
	//temp_content=temp_content.replaceAll("&lt;","<").replaceAll("&gt;",">").replaceAll("&amp;","&").replaceAll("&nbsp;"," ");
	public static String toPdfStr(String str){
		return str.replaceAll("&lt;","<").replaceAll("&gt;",">").replaceAll("&amp;","&").replaceAll("&nbsp;"," ");
	}
	
	public static Font chooseFont(String font_style,int font_size)throws Exception{	
		
		//设置字体为中文
		//BaseFont bfc = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED)
		//BaseFont bfc = BaseFont.createFont("C:/WINDOWS/Fonts/SIMYOU.TTF", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED); 
		BaseFont bfc = BaseFont.createFont(FONT_USED, BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
		Font font=new Font(bfc);
		font.setSize(font_size);
		
		font.setStyle(Font.NORMAL);  // 默认的字体为normal
		
		if(font_style.equals("BOLD")){
			font.setStyle(Font.BOLD);
		}
		
		if(font_style.equals("ITALIC")){
			font.setStyle(Font.ITALIC);
		}
		return font;
	}
	
public static Font chooseFont(int font_size)throws Exception{	
		
		//设置字体为中文
		//BaseFont bfc = BaseFont.createFont("STSongStd-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
	    //BaseFont bfc = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
	    BaseFont bfc = BaseFont.createFont(FONT_USED, BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);    
		Font font=new Font(bfc);
		font.setSize(font_size);  //默认的字体大小为10	
		font.setStyle(Font.NORMAL);  // 默认的字体为normal
		return font;
	}
	/*
	 * 向文档中写入文字,换行
	 * 参数：
	 * document： 写入的文档
	 * text  文字内容 string
	 * font_style    String :支持：normal，bold,italic，默认为 normal
	 * font_size    
	 * 
	 * 
	 */
    
	public static void  writeTextLine(Document doc,
			                          String text,
			                          String font_style,
			                          int font_size)throws Exception{
		Font font=chooseFont(font_style,font_size);
		Paragraph p=new Paragraph(toPdfStr(text),font);  //新建一个段落
		//p.setFont(font);
		doc.add(p); //加入段落	
	}
	
	/*writeTextLine 的重载版本，增加一个是否居中的参数，用于标题等需要居中段落的显示*/
	public static void  writeTextLine(Document doc,
            String text,
            String font_style,
            int font_size,
            boolean iscenter)throws Exception{
       Font font=chooseFont(font_style,font_size);
       Paragraph p=new Paragraph(toPdfStr(text),font);  //新建一个段落
       //p.setFont(font);
       if(iscenter){
    	   p.setAlignment(Element.ALIGN_CENTER);
       }
       doc.add(p); //加入段落	
}
	
	/*支持2个参数的 writeTextLine 版本，默认字体为小，normal*/
	public static void  writeTextLine(Document doc,String text,int fontsize)throws Exception{
		Font font=chooseFont(fontsize);
		Paragraph p=new Paragraph(toPdfStr(text),font);  //新建一个段落
		//p.setFont(font);
		doc.add(p); //加入段落
    }

	/*
	 * 参数同writeText 
	 */
	public static void  writeText(Document doc,
                                String text,
                                String font_style,
                                int font_size)throws Exception{
		Font font=chooseFont(font_style,font_size);
		Chunk chunk = new Chunk(toPdfStr(text),font);
		doc.add(chunk);
	}
	
	public static void  writeText(Document doc,String text,int fontsize)throws Exception{
       Font font=chooseFont(fontsize);
       Chunk chunk = new Chunk(toPdfStr(text),font);
       doc.add(chunk);
    }
	
	/*添加一个图片,换行*/
	public static void writePicLine(Document doc,String pic)throws Exception{
		Image png = Image.getInstance(pic);
		//Jpeg jpg=new Jpeg(,200,100);
		// 缩放图片，百分比
		png.scalePercent(IMAGESCALEPERCENT);	
		doc.add(png);
	}
	
	/*换行1行*/
	public static void writeLine(Document doc)throws Exception{
		Paragraph p=new Paragraph("");  //新建一个段落
		doc.add(p); //加入段落
	}
	
	/*换行，lines代表插入的空行数*/
    public static void writeLine(Document doc,int lines)throws Exception{
    	if(lines<0) return;
		for(int i=0;i<lines;i++){
			writeLine(doc);
		}
	}
	
	
	public static void testTable(Document doc)throws Exception{
		Table table = new Table(3);
		 table.setBorderWidth(0);
		 table.setBorderColor(new Color(0, 0, 255));
		 table.setPadding(5);
		 Cell cell = new Cell("header");
		 cell.setHeader(true);
		 cell.setColspan(3);
		 table.addCell(cell);
		 cell = new Cell("example cell with colspan 1 and rowspan 2");
		 cell.setRowspan(2);
		 cell.setBorderColor(new Color(255, 0, 0));
		 table.addCell(cell);
		 table.addCell("1.1");
		 table.addCell("2.1444");
		 table.addCell("1.2");
		 table.addCell("2.2");
		 doc.add(table);

	}
	
	public static void testPicAndText(Document doc,String text,String pic,int fontsize)throws Exception{
		Font font=chooseFont(fontsize);
		Paragraph p=new Paragraph(toPdfStr(text));  //新建一个段落
		p.setFont(font);
		Image png = Image.getInstance(pic);
		png.scalePercent(IMAGESCALEPERCENT);	
		p.add(png);
		p.add(toPdfStr(text));
		doc.add(p); //加入段落
	}
	
	
	//添加复杂的题目，即包含图片，有包含文字，并且图片不换行
	
	/*
	 * 参数：Document doc: 需要操作的文档
	 * String[] texts 需要添加的文字 数组，
	 * String[] pics  需要添加的图片 数组，保存图片路径的数组
	 * boolean textFirst  是否先开始显示文字，true为开始显示文字，false为开始显示图片
	 */
	public static void addParagraph(Document doc,Vector<String> texts,Vector<String>pics,boolean textFirst,int fontsize)throws Exception{
		Font font=chooseFont(fontsize);
		Paragraph p=new Paragraph("",font);  //新建一个段落
		//p.setFont(font);
		int txt_size=texts.size();
		int pic_size=pics.size();
		int j=0;
		if(textFirst){
			for(int i=0;i<txt_size;i++){
				p.add(toPdfStr(texts.get(i)));
				if(j<pic_size){
					Image png = Image.getInstance(pics.get(j++));
					png.scalePercent(IMAGESCALEPERCENT);
					p.add(png);
				}
			}
			//如果仍然有图片，继续显示
			while(j<pic_size){
				Image png = Image.getInstance(pics.get(j++));
				png.scalePercent(IMAGESCALEPERCENT);
				p.add(png);
			}
		}else{
			for(int i=0;i<pic_size;i++){
				Image png = Image.getInstance(pics.get(i));
				png.scalePercent(IMAGESCALEPERCENT);
				p.add(png);
				if(j<txt_size){
					p.add(toPdfStr(texts.get(j++)));
				}
			}
			//如果仍然有文字，继续显示
			while(j<txt_size){
				p.add(toPdfStr(texts.get(j++)));
			}
		}
		
		doc.add(p);	
	}
	
	/*
	 * 显示一道复杂的数学题目，包含3段文字和两个公式图片
	 * 设[img1], 则[img2]=(),
	 */
	public static void testaddParagraph(Document doc)throws Exception{
		String[] texts=new String[3];
		texts[0]="设";
		texts[1]=", 则";
		texts[2]="=()";
		String[] pics=new String[2];
		pics[0]="d:/temp/testimg/f1.gif";
		pics[1]="d:/temp/testimg/f2.gif";
		//addParagraph(doc,texts,pics,true);
		
	}
	
	public static void main(String[] args){
		String FILENAME="d:/temp/testDoc.doc";
		
		try{
            Document doc = new Document(PageSize.A4);
			RtfWriter2.getInstance(doc, new FileOutputStream(FILENAME));
			doc.open();
			
			for(int i=0;i<10;i++){
				TesDoc.writeTextLine(doc,"yulu是一个好人","bold",15);
			}
			
			for(int i=0;i<10;i++){
				TesDoc.writeText(doc,"我是一个好人",10);
			}
			
			/*add a pic*/
			for(int i=0;i<3;i++){
				TesDoc.writePicLine(doc, "d:/temp/hagen.gif");
			}
			
			//testTable(doc);
		//	testPicAndText(doc,"we are the best","d:/temp/testimg/1.gif");
			
			testaddParagraph(doc);
			
			doc.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//清除数据中的HTML标签,返回字符串
	public static String cleanHTMLSpan( String HTMLstr ){
			Vector<String> v = HtmlElementsGetter.getElementsByHtmlStr( HTMLstr );
			String ret_str = "";
			for( String str : v ){
				ret_str += str; 
			}
			return ret_str;
		}
	
}