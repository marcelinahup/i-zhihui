package com.hk.commons.office;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.hk.commons.util.BeanUtils;

/**
 * Excel 操作类，不可序列化网络传递！
 * @author tongc
 */
public class Excel_03 {

	// 当前workbook
    private HSSFWorkbook myWorkBook = null;
    
	// 当前sheet
    private HSSFSheet mySheet = null;

    // 文件输入流
    private FileInputStream fileInStream = null;

    /**
     * 构建空白 Excel
     */
    public Excel_03(){
    	myWorkBook = new HSSFWorkbook();
    	mySheet = myWorkBook.createSheet();
    }
    
    /**
     * 指定sheetName,构建空白 Excel
     */
    public Excel_03(String sheetName){
        myWorkBook = new HSSFWorkbook();
        mySheet = myWorkBook.createSheet(sheetName);
    }
    
    /**
     * 根据输入流和sheet名构建excel对象
     * @param input 数据输入流
     * @param sheetName sheet名
     */
    public Excel_03(InputStream input, String sheetName) {

        try {
            myWorkBook = new HSSFWorkbook(input);
            mySheet = myWorkBook.getSheet(sheetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据模板路径和sheet名构建excel对象
     * @param templatePath 
     * @param sheetName
     */
    public Excel_03(String templatePath, String sheetName) {

        try {
            fileInStream = new FileInputStream(templatePath);
            myWorkBook = new HSSFWorkbook(fileInStream);
            mySheet = myWorkBook.getSheet(sheetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 根据输入流和sheet索引构建excel对象
     * @param input 数据输入流
     * @param sheetName sheet名
     */
    public Excel_03(InputStream input, int sheetIndex) {

        try {
            myWorkBook = new HSSFWorkbook(input);
            mySheet = myWorkBook.getSheetAt(sheetIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 根据模板和sheet索引构建excel对象
     * @param templatePath 
     * @param sheetName
     */
    public Excel_03(String templatePath, int sheetIndex) {
        
        try {
            fileInStream = new FileInputStream(templatePath);
            myWorkBook = new HSSFWorkbook(fileInStream);
            mySheet = myWorkBook.getSheetAt(sheetIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 新创建一个名为sheetName 的 sheet
     * @param sheetName
     */
    public void createSheet(String sheetName) {
        
        mySheet = myWorkBook.getSheet(sheetName);
        if(mySheet == null){
            mySheet = this.myWorkBook.createSheet(sheetName);
        }
    }
    
    /**
     * 重命名sheet
     * @param sheetIndex
     * @param sheetName
     */
    public void renameSheet(int sheetIndex,String sheetName) {
    	
    	myWorkBook.setSheetName(sheetIndex, sheetName);
    }

    /**
     * 根据索引copy sheet并重命名
     * @param sheetIndex
     * @param sheetName
     */
    public void copySheet(int sheetIndex, String sheetName){
        mySheet = this.myWorkBook.cloneSheet(sheetIndex);
        
        // 取得全部sheet个数
        int sheetIx = this.myWorkBook.getNumberOfSheets();
        this.myWorkBook.setSheetName(sheetIx-1, sheetName);
    }
    
    /**
     * 根据sheet索引设定当前使用的sheet
     * 
     * @param sheetName
     */
    public void useSheet(int sheetIndex) {

        int count = myWorkBook.getNumberOfSheets();
        int len = sheetIndex - count + 1;

        for (int i = 0; i < len; i++) {
                myWorkBook.createSheet();
        }
        this.mySheet = myWorkBook.getSheetAt(sheetIndex);
    }
    
    /**
     * 根据sheet名设定当前使用的sheet
     * 【如果不存在就创建】
     * @param sheetName
     */
    public void useSheet(String sheetName){
        this.mySheet = myWorkBook.getSheet(sheetName);
        if (this.mySheet == null) {
            this.mySheet = myWorkBook.createSheet(sheetName);
        }
    }
    
    /**
     * 根据sheet索引设定当前活动sheet
     * @param sheetIndex
     */
    public void setActiveSheet(int sheetIndex){
        mySheet = myWorkBook.getSheetAt(sheetIndex);
        myWorkBook.setActiveSheet(sheetIndex);
        myWorkBook.setSelectedTab(sheetIndex);
    }
    
    /**
     * 根据sheet名设定当前活动sheet
     * @param sheetName
     */
    public void setActiveSheet(String sheetName){
        int sheetIndex = myWorkBook.getSheetIndex(sheetName);
        setActiveSheet(sheetIndex);
    }

    /**
     * 保存 Excel 到指定路径
     * @param targetPath 需要保存的位置全路径
     * @throws IOException
     */
    public void writeTo(String targetPath) throws IOException {

        FileOutputStream fileOutStream = new FileOutputStream(targetPath);
        writeTo(fileOutStream);
    }

    /**
     * 保存 Excel 到response（通过浏览器输出）
     * @param response
     * @throws IOException
     */
    public void writeTo(HttpServletResponse response,String fileName) throws IOException {
        
        // 判断文件名后缀因为导出的文件类型要靠后缀保护的
        // 97-03
        if(!fileName.endsWith(".xls")){
            fileName += ".xls";
        }
        
        //设置响应类型 
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/msexcel;charset=UTF-8");
        
        OutputStream out = response.getOutputStream();
        writeTo(out);
    }

    /**
     * 保存 Excel 到输出流
     * @param out OutputStream
     * @throws IOException
     */
    public void writeTo(OutputStream out) throws IOException {
        
        myWorkBook.write(out);
        if(fileInStream != null){
            fileInStream.close();
            fileInStream = null;
        }
        out.close();
        mySheet = null;
        myWorkBook = null;
    }
    
//    /**
//     * 插入一行 【后面的行可能会被覆盖】
//     * @param insertIndex
//     * @param insertNum
//     */
//    public void insertOneRow(int insertIndex){
//
//    	mySheet.shiftRows(insertIndex, insertIndex, 1);
//    	
//        HSSFRow sourceRow = mySheet.createRow(insertIndex);  
//        HSSFRow targetRow = mySheet.getRow(insertIndex + 1);
//        int targetFirstCellNum = targetRow.getFirstCellNum();
//        if (targetFirstCellNum >= 0) {
//			
//        	HSSFCell sourceCell = null;  
//            HSSFCell targetCell = null;
//            
//        	for (int m = targetFirstCellNum; m < targetRow.getPhysicalNumberOfCells(); m++) {  
//        		sourceCell = sourceRow.createCell(m);  
//        		targetCell = targetRow.getCell(m);  
//        		sourceCell.setCellStyle(targetCell.getCellStyle());  
//        		sourceCell.setCellType(targetCell.getCellType());  
//        	}  
//		}
//    }
//    
//    /**
//     * 插入多行 【后面的行可能会被覆盖】
//     * @param startRow
//     * @param rows
//     */
//    public void insertRows(int startRow, int rows) {  
//        for (int i = 0; i < rows; i++) {
//			insertOneRow(startRow + i);
//		}
//    }  
    
    /**
     * 复制单行样式
     * @param startRow 被复制的起始行
     * @param position 目标的起始行
     * @throws IOException
     */
    public void copyOneRowWithStyle(int startRow, int position) throws IOException {

        copyRowsWithStyle(startRow, startRow, position);
    }

    /**
     * 复制多行样式
     * @param startRow  被复制的起始行
     * @param endRow 被复制的结束行
     * @param position 目标的起始行
     */
    public void copyRowsWithStyle(int startRow, int endRow, int position) {

        HSSFRow sourceRow = null;
        HSSFRow targetRow = null;
        
        for (int rowIndex = startRow; rowIndex <= endRow; rowIndex++) {
            sourceRow = mySheet.getRow(rowIndex);
            targetRow = mySheet.createRow(position + rowIndex - startRow);
            targetRow.setHeight(sourceRow.getHeight());

            for (int cellIndex = sourceRow.getFirstCellNum(); cellIndex < sourceRow.getLastCellNum(); cellIndex++) {
            	
            	// 判断有无合并单元格【只支持单行】
            	int [] mergedNum = getMergedRegions(rowIndex, rowIndex, cellIndex, sourceRow.getLastCellNum());
            	if (mergedNum != null) {
					
//                    int firstRow = mergedNum[0];
//                    int lastRow = mergedNum[1];
                    int firstColumn = mergedNum[2];
                    int lastColumn = mergedNum[3];
                    for (int i = 0; i < firstColumn; i++) {
                    	HSSFCell srcCell = sourceRow.getCell(cellIndex);
    					HSSFCell targetCell = targetRow.createCell(cellIndex);
    					targetCell.setCellStyle(srcCell.getCellStyle());
                    }
                    // 合并单元格
                    CellRangeAddress caAddress = new CellRangeAddress(targetRow.getRowNum(), targetRow.getRowNum(), firstColumn, lastColumn);
                    mySheet.addMergedRegion(caAddress);
                    
                    for (int i = lastColumn; i < sourceRow.getLastCellNum(); i++) {
                    	HSSFCell srcCell = sourceRow.getCell(cellIndex);
    					HSSFCell targetCell = targetRow.createCell(cellIndex);
    					targetCell.setCellStyle(srcCell.getCellStyle());
                    }
            		
				}else {
					
					HSSFCell srcCell = sourceRow.getCell(cellIndex);
					HSSFCell targetCell = targetRow.createCell(cellIndex);
					targetCell.setCellStyle(srcCell.getCellStyle());
				}
            }
        }
    }
    
    /**
     * 获取合并单元格的坐标    
     * @param startRow
     * @param endRow
     * @param startColumn
     * @param endColumn
     * @return
     */
     public int [] getMergedRegions(int startRow, int endRow, int startColumn, int endColumn){     
         int sheetMergeCount = mySheet.getNumMergedRegions();     
              
         for(int i = 0 ; i < sheetMergeCount ; i++){
             CellRangeAddress ca = mySheet.getMergedRegion(i);
             
             int firstColumn = ca.getFirstColumn();
             int lastColumn = ca.getLastColumn();
             int firstRow = ca.getFirstRow();
             int lastRow = ca.getLastRow();
             
             if(startRow <= firstRow && endRow >= lastRow && startColumn <= firstColumn && endColumn >= lastColumn){     
                 return new int []{firstRow,lastRow,firstColumn,lastColumn};
             }
         }
         
         return null ;
     } 
     
    /**
     * 复制列带样式
     * @param startColumn 起始列索引
     * @param endColumn 结束列索引
     * @param position 粘贴位置
     * @throws IOException
     */
    public void copyColumnsWithStyle(int startColumn, int endColumn, int position) {    
        HSSFRow sourceRow = null;
        int firstNum = mySheet.getFirstRowNum();
        int lastNum = mySheet.getLastRowNum();
        for (int i = firstNum; i <= lastNum; i++) {
            sourceRow = mySheet.getRow(i);
            int positionIndex = position;
            if (sourceRow != null) {
				
            	for (int j = startColumn; j <= endColumn; j++) {
            		HSSFCell sourceCell = sourceRow.getCell(j);
            		HSSFCell targetCell = sourceRow.createCell(positionIndex++);
            		if (sourceCell != null) {
            			
            			HSSFCellStyle sourceCellStyle = sourceCell.getCellStyle();
            			targetCell.setCellStyle(sourceCellStyle);
            			
            			HSSFCellStyle targetCellStyle = myWorkBook.createCellStyle();
            			targetCellStyle = sourceCellStyle;
            			targetCell.setCellStyle(targetCellStyle);
            		}
            	}
            }
		}
        
        for (int i = startColumn; i <= endColumn; i++) {
            
            mySheet.setColumnWidth(position++, mySheet.getColumnWidth(i));
        }
    }

    /**
     * 设置合并单元格的样式
     * 
     * @param fromRow
     *            起始行
     * @param fromCol
     *            起始列
     * @param borderLeft
     *            左边边框宽度
     * @param borderRight
     *            右边边框宽度
     * @param borderTop
     *            上边边框宽度
     * @param borderBottom
     *            下边边框宽度
     *//*
    public void setRegionStyle(int fromRow, int fromCol, short borderLeft,
            short borderRight, short borderTop, short borderBottom) {

        for (int i = 0; i < mySheet.getNumMergedRegions(); i++) {

            Region region = mySheet.getMergedRegionAt(i);
            if (region.getRowFrom() == fromRow
                    && region.getColumnFrom() == fromCol) {

                HSSFCellStyle cellstyle = myWorkBook.createCellStyle();
                cellstyle = mySheet.getRow(fromRow).getCell(fromCol)
                        .getCellStyle();
                if (borderLeft != -1) {
                    cellstyle.setBorderLeft(borderLeft);
                }
                if (borderRight != -1) {
                    cellstyle.setBorderRight(borderRight);
                }
                if (borderTop != -1) {
                    cellstyle.setBorderTop(borderTop);
                }
                if (borderBottom != -1) {
                    cellstyle.setBorderBottom(borderBottom);
                }

                setRegionStyle(region, cellstyle);
            }
        }
    }

    *//**
     * 设置合并单元格的样式
     * 
     * @param region
     *            合并的单元格
     * @param cellstyle
     *            样式
     *//*
    public void setRegionStyle(Region region, HSSFCellStyle cellstyle) {

        int topRowNum = region.getRowFrom();

        for (int i = topRowNum; i <= region.getRowTo(); i++) {

            HSSFRow row = HSSFCellUtil.getRow(i, mySheet);

            for (int j = region.getColumnFrom(); j <= region.getColumnTo(); j++) {

                HSSFCell cell = HSSFCellUtil.getCell(row, (short) j);

                cell.setCellStyle(cellstyle);
            }
        }
    }*/

    public void cellComment(int startRow, int startCell, int endRow,
            int endCell, String strComment, String author) {

        HSSFPatriarch patr = mySheet.createDrawingPatriarch();

        HSSFComment comment = null;

        // 定义注释的大小和位置,详见文档
        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0,
                (short) startCell, startRow, (short) endCell, endRow);

        comment = patr.createCellComment(anchor);

        // 设置注释内容
        comment.setString(new HSSFRichTextString(strComment));

        comment.setAuthor(author);

        mySheet.getRow(startRow).getCell(startCell).setCellComment(comment);
    }

    public void setRowBorderStyle(int row, int startCell, int endCell,
            short borderBottom, short borderLeft, short borderRight,
            short borderTop) {

        int cellNum = mySheet.getRow(row).getLastCellNum();

        if (startCell < 0)
            startCell = 0;
        if (endCell < 0)
            endCell = 0;

        if (endCell >= startCell) {

            int forCount = 0;

            if (cellNum > 0) {

                if (startCell <= cellNum && endCell >= cellNum) {

                    forCount = cellNum - startCell;
                } else if (startCell <= cellNum && endCell <= cellNum) {

                    forCount = endCell - startCell;
                }
            } else {

                forCount = endCell - startCell;
            }

            for (int i = 0; i < forCount; i++) {

                borderBottom(row, startCell + i, borderBottom);
                borderLeft(row, startCell + i, borderLeft);
                borderRight(row, startCell + i, borderRight);
                borderTop(row, startCell + i, borderTop);
            }
        }
    }

    /**
     * 导入图片
     * 
     * @param pic
     *            图片的路径
     * @param startRow
     *            源的行号
     * @param startCell
     *            源的列号
     * @param endRow
     *            目标的行号
     * @param endCell
     *            目标的列号
     * @throws IOException
     */
    public void insertPic(String pic, int startRow, int startCell, int endRow,
            int endCell) throws IOException {

        // 先把读进来的图片放到�?��ByteArrayOutputStream中，以便产生ByteArray
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

        File file = new File(pic);

        BufferedImage bufferImg = ImageIO.read(file);

        String[] token = file.getName().split("\\.");

        String extendName = token[1];

        ImageIO.write(bufferImg, extendName, byteArrayOut);

        HSSFPatriarch patriarch = mySheet.createDrawingPatriarch();

        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255,
                (short) startCell, startRow, (short) endCell, endRow);

        patriarch.createPicture(anchor, myWorkBook.addPicture(byteArrayOut
                .toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
    }

    /**
     * 清空指定行的数据
     * @param int rowIndex 行号
     */
    public void clearRow(int rowIndex){
        mySheet.removeRow(mySheet.getRow(rowIndex));
    }
    
    /**
     * 单元格名to 行索引
     * @param cellName
     * @return int rowIndex
     */
    private int cellName2CellIndex(String cellName){
        char LETTER_A ='A';
        
        // 去除数字，并全部转成大写
        String  cell = cellName.replaceAll("\\d", "").toUpperCase();
        int len = cell.length();
        
        // 行数
        double cellCount = 0;
        
        for (int chIndex = 0; chIndex < cell.length() ; chIndex++) {
            
            char ch = cell.charAt(chIndex);

            // 积数
            int multi = ch - LETTER_A + 1;
            
            // 基数
            int base = 26;
            
            //幂次
            double power = len -1;
            
            cellCount += multi * Math.pow(base, power);
            len--;
        }
        
        return (int) cellCount - 1 ;
    }
    
    /**
     * 单元格名 to 列索引
     * @param cellName
     * @return int CellIndex
     */
    private int cellName2RowIndex(String cellName){
        
        // 去除非数字
        int row = Integer.parseInt(cellName.replaceAll("\\D", ""));
        return row - 1;
    }
    
    /**
     * 取单元格的String值
     * @param String cellName 单元格名称
     * @return String value
     */
    public String getString(String cellName){
        
        int rowIndex = cellName2RowIndex(cellName);
        int cellIndex = cellName2CellIndex(cellName);
        return getString(rowIndex, cellIndex);
    }
    
    /**
     * 取单元格的String值
     * @param int rowIndex 行索引
     * @param int cellIndex 列索引
     * @return String value
     */
    public String getString(int rowIndex, int cellIndex) {

        HSSFCell cell = mySheet.getRow(rowIndex).getCell(cellIndex);
        
        String cellvalue = null;  
        
        if (cell != null){
 
           // 判断当前Cell的Type  
           switch (cell.getCellType()){  
 
           // 如果当前Cell的Type为NUMERIC  
           case HSSFCell.CELL_TYPE_NUMERIC:{  
 
              // 判断当前的cell是否为Date  
              if (HSSFDateUtil.isCellDateFormatted(cell)){  
 
                 // 如果是Date类型则，取得该Cell的Date值  
                 Date date = cell.getDateCellValue();  
 
                 // 把Date转换成本地格式的字符串  
                 DateFormat format = new SimpleDateFormat();
                 cellvalue = format.format(date);
              }  
 
              // 如果是纯数字  
              else{
 
                 // 取得当前Cell的数值  

            	  double num  = cell.getNumericCellValue();
                 cellvalue = String.valueOf(num);  
              }  
 
              break;  
           }  
 
           // 如果当前Cell的Type为STRIN  
           case HSSFCell.CELL_TYPE_STRING:  
 
              // 取得当前的Cell字符串  
              cellvalue = cell.getStringCellValue().replaceAll("'", "''");  
              break;  
 
           // 默认的Cell值  
           default:  
 
              cellvalue = "";  
              break;
           }   
        }
        
        // 去除左右空格
        return cellvalue.trim();
    }
    
    /**
     * 取单元格的double值
     * @param String cellName 单元格名
     * @return double value
     */
    public double getDouble(String cellName){
        return Double.parseDouble(getString(cellName));
    }

    /**
     * 取单元格的double值
     * @param int rowIndex
     * @param int cellIndex
     * @return double value
     */
    public double getDouble(int rowIndex, int cellIndex){
        return Double.parseDouble(getString(rowIndex, cellIndex));
    }
    
    /**
     * 取单元格的boolean值
     * @param StringcellName 单元格名
     * @return boolean value
     */
    public boolean getBoolean(String cellName){
        return Boolean.parseBoolean(getString(cellName));
    }
    
    /**
     * 取单元格的boolean值
     * @param int rowIndex
     * @param int cellIndex
     * @return boolean value
     */
    public boolean getBoolean(int rowIndex, int cellIndex){
        return Boolean.parseBoolean(getString(rowIndex, cellIndex));
    }
    
    /**
     * 取单元格的Date值
     * @param String cellName 单元格名
     * @return Date value
     */
    public Date getDate(String cellName){
        
        int rowIndex = cellName2RowIndex(cellName);
        int cellIndex = cellName2CellIndex(cellName);
        return getDate(rowIndex, cellIndex);
    }

    /**
     * 取单元格的Date值
     * @param rowIndex
     * @param cellIndex
     * @return Date value
     */
    public Date getDate(int rowIndex, int cellIndex){
        return mySheet.getRow(rowIndex).getCell(cellIndex).getDateCellValue();
    }
    
    /**
     * 取单元格的 Long 值(截掉小数)
     * @param String cellName 单元格名
     * @return Integer value
     */
    public long getLong(String cellName){
    	
    	int rowIndex = cellName2RowIndex(cellName);
    	int cellIndex = cellName2CellIndex(cellName);
    	return getLong(rowIndex, cellIndex);
    }
    
    /**
     * 取单元格的 Long 值(截掉小数)
     * @param int rowIndex
     * @param int cellIndex
     * @return Date value
     */
    public long getLong(int rowIndex, int cellIndex){
    	
    	String value = getString(rowIndex, cellIndex);
    	if(value == null ){
    		return 0L;
    	}
    	
    	// 防止科学计数
        DecimalFormat df = new DecimalFormat("0");
        
        double dblVal = Double.parseDouble(value);
        value = df.format(dblVal);
    	return Long.parseLong(value);
    }
    
    /**
     * 取单元格的 Integer 值(截掉小数)
     * @param String cellName 单元格名
     * @return Integer value
     */
    public int getInteger(String cellName){
        
        int rowIndex = cellName2RowIndex(cellName);
        int cellIndex = cellName2CellIndex(cellName);
        return getInteger(rowIndex, cellIndex);
    }

    /**
     * 取单元格的 Integer 值(截掉小数)
     * @param int rowIndex
     * @param int cellIndex
     * @return Date value
     */
    public int getInteger(int rowIndex, int cellIndex){
        
        String value = getString(rowIndex, cellIndex);
        if(value == null){
            return 0;
        }
        // 防止科学计数
        DecimalFormat df = new DecimalFormat("0");
        
        double dblVal = Double.parseDouble(value);
        value = df.format(dblVal);
        return Integer.parseInt(value);
    }

    /**
     * 给单元格赋值 String 类型
     * @param String cellName 单元格名
     * @param String value 值
     */
    public void setValue(String cellName, String value){
        
        int rowIndex = cellName2RowIndex(cellName);
        int cellIndex = cellName2CellIndex(cellName);
        setValue(rowIndex, cellIndex, value);
    }
    
    /**
     * 给单元格赋值 String 类型
     * @param rowIndex 行索引
     * @param cellIndex 列索引
     * @param value string类型的值
     */
    public void setValue(int rowIndex, int cellIndex, String value) {

        // 根据索引取得行
        HSSFRow tmpRow = mySheet.getRow(rowIndex);
        
        // 未取到，创建新行
        if(tmpRow == null){
            tmpRow = mySheet.createRow(rowIndex);
        }
        
        // 根据索引取得单元格
        HSSFCell tmpCell = tmpRow.getCell(cellIndex);
        
        // 未取到，创建新单元格
        if(tmpCell == null){
            tmpCell = tmpRow.createCell(cellIndex);
        }
        
        // 给单元格赋值
        tmpCell.setCellValue(value);
        
    }

    /**
     * 给单元格赋值 double 类型
     * @param String cellName 单元格名
     * @param double value �?
     */
    public void setValue(String cellName, double value){
        
        int rowIndex = cellName2RowIndex(cellName);
        int cellIndex = cellName2CellIndex(cellName);
        setValue(rowIndex, cellIndex, value);
    }

    /**
     * 给单元格赋值 double 类型
     * @param rowIndex
     * @param cellIndex
     * @return double value
     */
    public void setValue(int rowIndex, int cellIndex, double value){
        
        // 根据索引取得行
        HSSFRow tmpRow = mySheet.getRow(rowIndex);
        
        // 未取到，创建新行
        if(tmpRow == null){
            tmpRow = mySheet.createRow(rowIndex);
        }
        
        // 根据索引取得单元格
        HSSFCell tmpCell = tmpRow.getCell(cellIndex);
        
        // 未取到，创建新单元格
        if(tmpCell == null){
            tmpCell = tmpRow.createCell(cellIndex);
        }
        
        // 给单元格赋值
        tmpCell.setCellValue(value);
    }
    
    /**
     * 给单元格赋值 boolean 类型
     * @param String cellName 单元格名
     * @param boolean value �?
     */
    public void setValue(String cellName, boolean value){
        
        int rowIndex = cellName2RowIndex(cellName);
        int cellIndex = cellName2CellIndex(cellName);
        setValue(rowIndex, cellIndex, value);
    }

    /**
     * 给单元格赋值 boolean 类型
     * @param rowIndex
     * @param cellIndex
     * @return boolean value
     */
    public void setValue(int rowIndex, int cellIndex, boolean value){
        
        // 根据索引取得行
        HSSFRow tmpRow = mySheet.getRow(rowIndex);
        
        // 未取到，创建新行
        if(tmpRow == null){
            tmpRow = mySheet.createRow(rowIndex);
        }
        
        // 根据索引取得单元格
        HSSFCell tmpCell = tmpRow.getCell(cellIndex);
        
        // 未取到，创建新单元格
        if(tmpCell == null){
            tmpCell = tmpRow.createCell(cellIndex);
        }
        
        // 给单元格赋值
        tmpCell.setCellValue(value);
    }
    
    /**
     * 给单元格赋值 Date 类型
     * @param String cellName 单元格名
     * @param Date value �?
     */
    public void setValue(String cellName, Date value){
        
        int rowIndex = cellName2RowIndex(cellName);
        int cellIndex = cellName2CellIndex(cellName);
        setValue(rowIndex, cellIndex, value);
    }

    /**
     * 给单元格赋值 Date 类型
     * @param rowIndex
     * @param cellIndex
     * @return Date value
     */
    public void setValue(int rowIndex, int cellIndex, Date value){
        
        // 根据索引取得行
        HSSFRow tmpRow = mySheet.getRow(rowIndex);
        
        // 未取到，创建新行
        if(tmpRow == null){
            tmpRow = mySheet.createRow(rowIndex);
        }
        
        // 根据索引取得单元格
        HSSFCell tmpCell = tmpRow.getCell(cellIndex);
        
        // 未取到，创建新单元格
        if(tmpCell == null){
            tmpCell = tmpRow.createCell(cellIndex);
        }
        
        // 给单元格赋值
        tmpCell.setCellValue(value);
    }

    /**
     * 给单元格赋值 Integer 类型
     * @param String cellName 单元格名
     * @param Integer value 值
     */
    public void setValue(String cellName, Integer value){
        
        int rowIndex = cellName2RowIndex(cellName);
        int cellIndex = cellName2CellIndex(cellName);
        setValue(rowIndex, cellIndex, value);
    }

    /**
     * 给单元格赋值 Integer 类型
     * @param int rowIndex
     * @param int cellIndex
     * @param Integer value
     */
    public void setValue(int rowIndex, int cellIndex, Integer value) {
    
        // 根据索引取得行
        HSSFRow tmpRow = mySheet.getRow(rowIndex);
        
        // 未取到，创建新行
        if(tmpRow == null){
            tmpRow = mySheet.createRow(rowIndex);
        }
        
        // 根据索引取得单元格
        HSSFCell tmpCell = tmpRow.getCell(cellIndex);
        
        // 未取到，创建新单元格
        if(tmpCell == null){
            tmpCell = tmpRow.createCell(cellIndex);
        }
        
        // 给单元格赋值
        tmpCell.setCellValue(value);
    }

    /**
     * 给单元格赋值 HSSFRichTextString 类型
     * @param String cellName
     * @param HSSFRichTextString value �?
     */
    public void setValue(String cellName, HSSFRichTextString value){
        
        int rowIndex = cellName2RowIndex(cellName);
        int cellIndex = cellName2CellIndex(cellName);
        setValue(rowIndex, cellIndex, value);
    }

    /**
     * 给单元格赋值 HSSFRichTextString 类型
     * @param rowIndex 行索引
     * @param cellIndex 列索引
     * @param HSSFRichTextString value
     */
    public void setValue(int rowIndex, int cellIndex, HSSFRichTextString value){
        
        // 根据索引取得行
        HSSFRow tmpRow = mySheet.getRow(rowIndex);
        
        // 未取到，创建新行
        if(tmpRow == null){
            tmpRow = mySheet.createRow(rowIndex);
        }
        
        // 根据索引取得单元格
        HSSFCell tmpCell = tmpRow.getCell(cellIndex);
        
        // 未取到，创建新单元格
        if(tmpCell == null){
            tmpCell = tmpRow.createCell(cellIndex);
        }
        
        // 给单元格赋值
        tmpCell.setCellValue(value);
    }
    
    /**
     * 给单元格设定 公式
     * @param String cellName 单元格名
     * @param String value 公式
     */
    public void setFormula(String cellName, String formula){
        
        int rowIndex = cellName2RowIndex(cellName);
        int cellIndex = cellName2CellIndex(cellName);
        setFormula(rowIndex, cellIndex, formula);
    }

    /**
     * 通过列表横向填充数据
     * @param cellName
     * @param list
     * @param order
     */
    public void setValue(String cellName,List<Object> list,String ... order){
        int rowIndex = cellName2RowIndex(cellName);
        int cellIndex = cellName2CellIndex(cellName);
        setValue(rowIndex, cellIndex, list,order);
    }
    
    /**
     * 通过列表横向填充数据
     * @param stratRowIndex
     * @param stratCellIndex
     * @param list
     * @param order
     */
    public void setValue(int stratRowIndex,int stratCellIndex,List<Object> list,String ... order){
        if (list != null) {
            int len = list.size();
            for (int i = 0; i < len; i++) {
                Object object = list.get(i);
                Object[] values = BeanUtils.getValues(object,order);
                int rowIndex = stratRowIndex + i;
                int cellIndex = 0;
                int valueLen = values.length;
                for (int j = 0;j < valueLen; j++) {
                    Object value = values[j];
                    cellIndex = stratCellIndex + j;
                    if(value instanceof Integer){
                        setValue(rowIndex, cellIndex, Integer.parseInt(value.toString()));
                    }
                    else if(value instanceof Double){
                        setValue(rowIndex, cellIndex, Double.parseDouble(value.toString()));
                    }
                    else if (value instanceof Boolean) {
                        setValue(rowIndex, cellIndex, Boolean.parseBoolean(value.toString()));
                    }
                    else if (value instanceof Date) {
                        setValue(rowIndex, cellIndex, value.toString());
                    }
                    else {
                        setValue(rowIndex, cellIndex, value.toString());
                    }
                    
                }
            }
        }
    }
    
    /**
     * 给单元格设定 公式
     * @param int row 行索引
     * @param int cell 列索引
     * @param String formula 公式
     */
    public void setFormula(int row, int cell, String formula) {
        mySheet.getRow(row).getCell(cell).setCellFormula(formula);
    }
    
    /**
     * copyStyle 复制模板style
     * 
     * @param setRowIndex
     *            所需设置行索引
     * @param setCellIndex
     *            所需设置列索引
     * @param modelRowIndex
     *            模板行索引
     * @param modelCellIndex
     *               模板列索引
     */
    public void copyStyle(int setRowIndex, int setCellIndex, int modelRowIndex,int modelCellIndex ) {

        HSSFRow row = mySheet.getRow(setRowIndex);
        if (row == null) {
            row = mySheet.createRow(setRowIndex);
        }
        HSSFCell cell = row.getCell(setCellIndex);
        if (cell == null) {
            cell = row.createCell(setCellIndex);
        }
        
        HSSFRow modelRow = mySheet.getRow(modelRowIndex);
        if (modelRow == null) {
            modelRow = mySheet.createRow(modelRowIndex);
        }
        HSSFCell modelCell = modelRow.getCell(modelCellIndex);
        if (modelCell == null) {
            modelCell = modelRow.createCell(modelCellIndex);
        }
        cell.setCellStyle(modelCell.getCellStyle());
        
    }
    
    /**
     * 复制单元格的数据
     * @param String srcCellName 源单元格
     * @param String targetCellName 目标单元格
     */
    public void copyValue(String srcCellName, String targetCellName){
        
        int srcRowIndex = cellName2RowIndex(srcCellName);
        int srcCellIndex = cellName2CellIndex(srcCellName);
        
        int targetRowIndex = cellName2RowIndex(targetCellName);
        int targetCellIndex = cellName2RowIndex(targetCellName);
        
        copyValue(srcRowIndex, srcCellIndex, targetRowIndex, targetCellIndex);
    }

    /**
     * 复制单元格的数据
     * @param srcRowIndex 源的行号
     * @param srcCellIndex 源的列号
     * @param targetRowIndex 目标的行索引
     * @param targetCellIndex 目标的列索引
     */
    public void copyValue(int srcRowIndex, int srcCellIndex, int targetRowIndex, int targetCellIndex) {

        HSSFCell srcCell = mySheet.getRow(srcRowIndex).getCell(srcCellIndex);
        HSSFCell targetCell = mySheet.getRow(targetRowIndex).getCell(targetCellIndex);
        int cType = srcCell.getCellType();
        targetCell.setCellType(srcCell.getCellType());

        switch (cType) {

        case HSSFCell.CELL_TYPE_BOOLEAN:
            targetCell.setCellValue(srcCell.getBooleanCellValue());
            break;
        case HSSFCell.CELL_TYPE_ERROR:
            targetCell.setCellErrorValue(srcCell.getErrorCellValue());
            break;
        case HSSFCell.CELL_TYPE_FORMULA:
            targetCell.setCellFormula(srcCell.getCellFormula());
            break;
        case HSSFCell.CELL_TYPE_NUMERIC:
            targetCell.setCellValue(srcCell.getNumericCellValue());
            break;
        case HSSFCell.CELL_TYPE_STRING:
            targetCell.setCellValue(srcCell.getStringCellValue());
            break;
        default:
            targetCell.setCellValue(srcCell.getStringCellValue());
            break;
        }
    }

    /**
     * 删除指定行
     * @param int rowIndex  行索引
     */
    public void removeRow(int rowIndex) {

        int lastRowNum = mySheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum) {

            // 移动-1 行
            mySheet.shiftRows(rowIndex + 1, lastRowNum, -1);
        }

        if (rowIndex == lastRowNum) {

            HSSFRow removingRow = mySheet.getRow(rowIndex);

            if (removingRow != null) {

                mySheet.removeRow(removingRow);
            }
        }
    }

    public void mergedRegion(String beginCellName, String endCellName){
        
        int beginRowIndex = cellName2RowIndex(beginCellName);
        int beginCellIndex = cellName2CellIndex(beginCellName);
        
        int endRowIndex = cellName2RowIndex(endCellName);
        int endCellIndex = cellName2RowIndex(endCellName);
        
        mergedRegion(beginRowIndex, beginCellIndex, endRowIndex, endCellIndex);
    }

    /**
     * 合并单元格
     * @param benginRowIndex 起始行的行号
     * @param beginCellIndex 起始行的列号
     * @param endRowIndex 目标行的行号
     * @param endCellIndex 目标行的列号
     */
    public void mergedRegion(int benginRowIndex, int beginCellIndex, int endRowIndex,
            int endCellIndex) {

        CellRangeAddress rangeAddress = new CellRangeAddress(benginRowIndex, endRowIndex,
                beginCellIndex, endCellIndex);

        mySheet.addMergedRegion(rangeAddress);
    }

    /**
     * 设定单元格背景色
     * @param String cellName 单元格名
     * @param color
     */
    public void setCellBackground(String cellName, short color){
        
        int rowIndex = cellName2RowIndex(cellName);
        int cellIndex = cellName2CellIndex(cellName);
        setCellBackground(rowIndex, cellIndex, color);
    }

    /**
     * 设定单元格背景色
     * @param rowIndex 行索引
     * @param cellIndex 列索引
     * @param color HSSFColor
     */
    /*public void setCellBackground(int rowIndex, int cellIndex, short color) {

        HSSFCellStyle style = myWorkBook.createCellStyle();
        style.setFillBackgroundColor(color);
        mySheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(style);
    }*/
    
    public void setCellBackground(int rowIndex, int cellIndex, short color) {
        
        //背景颜色修改
        HSSFCellStyle style = myWorkBook.createCellStyle();
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(color);
        mySheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(style);
    }

    /**
     * 设定单元格前景色
     * @param String cellName 单元格名
     * @param color 颜色值
     */
    public void setCellForeground(String cellName, short color){
        
        int rowIndex = cellName2RowIndex(cellName);
        int cellIndex = cellName2CellIndex(cellName);
        setCellForeground(rowIndex, cellIndex, color);
    }

    /**
     * 设定单元格前景色
     * @param int row 行索引
     * @param int column 列索引
     * @param foregroundColor HSSFColor 颜色
     */
    public void setCellForeground(int row, int column, short foregroundColor) {

        HSSFCellStyle style = myWorkBook.createCellStyle();
        style.setFillForegroundColor(foregroundColor);
        mySheet.getRow(row).getCell(column).setCellStyle(style);
    }

    /**
     * <p>
     * �?HSSFCellStyle.NO_FILL,说明:No background
     * </p>
     * <p>
     * �?HSSFCellStyle.SOLID_FOREGROUND,说明:Solidly filled
     * </p>
     * <p>
     * �?HSSFCellStyle.FINE_DOTS,说明:Small fine dots
     * </p>
     * <p>
     * �?HSSFCellStyle.ALT_BARS,说明:Wide dots
     * </p>
     * <p>
     * �?HSSFCellStyle.SPARSE_DOTS,说明:Sparse dots
     * </p>
     * <p>
     * �?HSSFCellStyle.THICK_HORZ_BANDS,说明:Thick horizontal bands
     * </p>
     * <p>
     * �?HSSFCellStyle.THICK_VERT_BANDS,说明:Thick vertical bands
     * <p>
     * �?HSSFCellStyle.THICK_BACKWARD_DIAG,说明:Thick backward facing diagonals
     * </p>
     * <p>
     * �?HSSFCellStyle.THICK_FORWARD_DIAG,说明:Thick forward facing diagonals
     * </p>
     * <p>
     * �?HSSFCellStyle.BIG_SPOTS,说明:Large spots
     * </p>
     * <p>
     * �?HSSFCellStyle.BRICKS,说明:Brick-like layout
     * </p>
     * <p>
     * �?HSSFCellStyle.THIN_HORZ_BANDS,说明:Thin horizontal bands
     * </p>
     * <p>
     * �?HSSFCellStyle.THIN_VERT_BANDS,说明:Thin vertical bands
     * </p>
     * <p>
     * �?HSSFCellStyle.THIN_BACKWARD_DIAG,说明:Thin backward diagonal
     * </p>
     * <p>
     * �?HSSFCellStyle.THIN_FORWARD_DIAG,说明:Thin forward diagonal
     * </p>
     * <p>
     * �?HSSFCellStyle.SQUARES,说明:Squares
     * </p>
     * <p>
     * �?HSSFCellStyle.DIAMONDS,说明:Diamonds
     * </p>
     * 
     * @param row
     *            行号
     * @param column
     *            列号
     * @param pattern
     *            HSSFCellStyle
     */
    public void fillPattern(int row, int column, short pattern) {

        HSSFCellStyle style = myWorkBook.createCellStyle();

        style.setFillPattern(pattern);

        mySheet.getRow(row).getCell(column).setCellStyle(style);
    }

    /**
     * 水平位置的设置
     * <p>
     * �?CellStyle.ALIGN_CENTER
     * </p>
     * <p>
     * �?CellStyle.ALIGN_CENTER_SELECTION
     * </p>
     * <p>
     * �?CellStyle.ALIGN_FILL
     * </p>
     * <p>
     * �?CellStyle.ALIGN_GENERAL
     * </p>
     * <p>
     * �?CellStyle.ALIGN_JUSTIFY
     * </p>
     * <p>
     * �?CellStyle.ALIGN_LEFT
     * </p>
     * <p>
     * �?CellStyle.ALIGN_RIGHT
     * </p>
     * 
     * @param row
     * @param column
     * @param alignment
     */
    public void alignment(int row, int column, short alignment) {

        HSSFCellStyle style = myWorkBook.createCellStyle();

        style.setAlignment(alignment);

        mySheet.getRow(row).getCell(column).setCellStyle(style);
    }

    /**
     * 垂直位置的设置
     * <p>
     * �?CellStyle.VERTICAL_BOTTOM
     * </p>
     * <p>
     * �?CellStyle.VERTICAL_CENTER
     * </p>
     * <p>
     * �?CellStyle.VERTICAL_JUSTIFY
     * </p>
     * <p>
     * �?CellStyle.VERTICAL_TOP
     * </p>
     * 
     * @param row
     * @param column
     * @param cellstyle
     */
    public void verticalAlignment(int row, int column, short verticalAlignment) {

        HSSFCellStyle style = myWorkBook.createCellStyle();

        style.setVerticalAlignment(verticalAlignment);

        mySheet.getRow(row).getCell(column).setCellStyle(style);
    }

    /**
     * 设置单元格的上边宽度(不可用于已经合并了的单元格)
     * 
     * @param row
     *            行索引
     * @param column
     *            列索引
     * @param borderTop
     *            宽度
     */
    public void borderTop(int row, int column, short borderTop) {

        HSSFCellStyle style = myWorkBook.createCellStyle();

        style.setBorderTop(borderTop);

        mySheet.getRow(row).getCell(column).setCellStyle(style);
    }

    /**
     * 设置单元格的上边宽度(不能用于已经合并了的单元格)
     * 
     * @param row
     *            行索引
     * @param startColumn
     *            起始列
     * @param endColumn
     *            目标列
     * @param borderTop
     *            宽度
     */
    public void borderTop(int row, int startColumn, int endColumn,
            short borderTop) {

        for (int i = startColumn; i <= endColumn; i++) {

            borderTop(row, i, borderTop);
        }
    }

    /**
     * 设置单元格的下边宽度(不能用于已经合并了的单元格)
     * 
     * @param row
     *            行索引
     * @param column
     *            列索引
     * @param borderBottom
     *            宽度
     */
    public void borderBottom(int row, int column, short borderBottom) {

        HSSFCellStyle style = myWorkBook.createCellStyle();

        style.setBorderBottom(borderBottom);

        mySheet.getRow(row).getCell(column).setCellStyle(style);
    }

    /**
     * 设置单元格的下边宽度(不能用于已经合并了的单元格)
     * 
     * @param row
     *            行索引
     * @param startColumn
     *            起始列
     * @param endColumn
     *            目标列
     * @param borderBottom
     *            宽度
     */
    public void borderBottom(int row, int startColumn, int endColumn,
            short borderBottom) {

        for (int i = startColumn; i <= endColumn; i++) {

            borderBottom(row, i, borderBottom);
        }
    }

    /**
     * 设置单元格的左边宽度(不能用于已经合并了的单元格)
     * 
     * @param row
     *            行索引
     * @param column
     *            列索引
     * @param borderLeft
     *            宽度
     */
    public void borderLeft(int row, int column, short borderLeft) {

        HSSFCellStyle style = myWorkBook.createCellStyle();

        style.setBorderLeft(borderLeft);

        mySheet.getRow(row).getCell(column).setCellStyle(style);
    }

    /**
     * 设置单元格的左边宽度(不能用于已经合并了的单元格)
     * 
     * @param row
     *            行索引
     * @param startColumn
     *            起始列
     * @param endColumn
     *            目标
     * @param borderLeft
     *            宽度
     */
    public void borderLeft(int row, int startColumn, int endColumn,
            short borderLeft) {

        for (int i = startColumn; i <= endColumn; i++) {

            borderBottom(row, i, borderLeft);
        }
    }

    /**
     * 设置单元格的右边宽度(不能用于已经合并了的单元格)
     * 
     * @param row
     *            行索引
     * @param column
     *            列索引
     * @param borderRight
     *            宽度
     */
    public void borderRight(int row, int column, short borderRight) {

        HSSFCellStyle style = myWorkBook.createCellStyle();

        style.setBorderRight(borderRight);
        mySheet.getRow(row).getCell(column).setCellStyle(style);
        
    }
    
    /**
     * 设置单元格的属性
     * @param row 单元格所在的行
     * @param column 单元格所在的列
     * @param border 边框的格式
     * @param color 背景颜色
     */
    public void setCellStyle(int row,int column,short border,short color){
        
        HSSFCellStyle style = myWorkBook.createCellStyle();
        
        //设置右边框格式
        style.setBorderRight(border);
        mySheet.getRow(row).getCell(column).setCellStyle(style);
        
        //设置上边框格式
        style.setBorderTop(border);
        mySheet.getRow(row).getCell(column).setCellStyle(style);
        
        //设置背景颜色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(color);
        mySheet.getRow(row).getCell(column).setCellStyle(style);
        
        //单元格大小自动填充大小
        mySheet.autoSizeColumn(column);
    }

    /**
     * 设置单元格的右边宽度(不能用于已经合并了的单元格)
     * 
     * @param row
     *            行索引
     * @param startColumn
     *            起始列
     * @param endColumn
     *            目标列
     * @param borderRight
     *            宽度
     */
    public void borderRight(int row, int startColumn, int endColumn,
            short borderRight) {

        for (int i = startColumn; i <= endColumn; i++) {

            borderBottom(row, i, borderRight);
        }
    }
    
    public HSSFWorkbook getMyWorkBook() {
		return myWorkBook;
	}

	public void setMyWorkBook(HSSFWorkbook myWorkBook) {
		this.myWorkBook = myWorkBook;
	}

    /**
     * @return the mySheet
     */
    public HSSFSheet getMySheet() {
        return mySheet;
    }

    /**
     * @param mySheet the mySheet to set
     */
    public void setMySheet(HSSFSheet mySheet) {
        this.mySheet = mySheet;
    }

}
