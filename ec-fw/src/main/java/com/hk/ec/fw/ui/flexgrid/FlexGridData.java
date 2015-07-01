package com.hk.ec.fw.ui.flexgrid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * author: yulu
 * flexgrid 希望返回的json格式，用于分页数据显示
 */

public class  FlexGridData implements Serializable {

	private static final long serialVersionUID = 6034770662188825751L;

	    private int page=1;
	    private int total=0;
	    private List<RowData> rows=new ArrayList<RowData>();
	    
	    private String id=null;
	    private List<String> cells=null;
	    
	    /**
	     * @return
	     */
	    public int getPage() {
	        return page;
	    }
	    /**
	     * 设置页码。
	     * @param page
	     */
	    public void setPage(int page) {
	        this.page = page;
	    }
	    
	    
	    public int getTotal() {
	        return this.total;
	    }
	    
	    /**
	     * 设置总记录数
	     * @param total
	     */
	    public void setTotal(int total) {
	        this.total = total;
	    }
	    
	    
	    public List<RowData> getRows() {
	        return rows;
	    }



	    public void setRows(List<RowData> rows) {
	        this.rows = rows;
	    }
	    
	    
	    public void addRow(String rowid,List<String> coldatas){
	        RowData rd=new RowData(rowid,coldatas);
	        this.rows.add(rd);
	    }
	    
	    /**
	     * 设置每一行的id。
	     * 配合addColdata(),commitData()方法是用。 
	     * 例：setRowId("row1");
	     *     addColdata("1");
	     *     addColdata("2");
	     *     
	     *     setRowId("row2");
	     *     addColdata("a");
	     *     addColdata("b");
	     *     
	     *     commitData();
	     *   表示 1，2两个数据都为行row1中第一列，第二列的数据。
	     *  a,b 两个数据都为row2中第一列，第二列的数据。
	     *  commitData()的调用表示，row2行的数据已经组织完成。
	     *  
	     *  在设置row2行的数据时，会自动提交row1行的数据。
	     *  
	     * @param rowid
	     */
	    public void setRowId(String rowid){
	        commitData();
	        this.id=rowid;
	        this.cells=null;
	    }
	    
	    /**
	     * 该方法配合setRowId和commitData()使用。该方法必须在调用setRowId()后调用，否则会抛出NullPointerException
	     * 请参考setRowId的说明
	     * @param coldata 每一列数据
	     */
	    public void addColdata(String coldata){
	        if(id==null) throw new NullPointerException("please set rowid");
	        if(cells==null) cells=new ArrayList<String>();
	        
	        cells.add(coldata);
	    }
	    
	    public void commitData(){
	        if(this.id!=null && this.cells!=null){
	            addRow(this.id, this.cells);
	            this.id=null;
	            this.cells=null;
	        }
	    }
	    
	    //按照顺序设置rowdata
	    @SuppressWarnings("rawtypes")
	    public void setDataByListMap(List<Map<String, Object>> list,List<String> col_list){
	    	if(list==null  || list.size()==0)  return;
	    	int size=list.size();
	    	
	    	int col_list_size=col_list.size();
	    	
	    	for(int i=0;i<size;i++){
	    		this.setRowId(i+"");
	    		
	    		//遍历list.get(i)中的map的值，依次放入row中
	    		Map<String, Object> rowMap=list.get(i);
	    		
	    		boolean colValueIsNull=true;  //标志此次值是否找到
	    		int col_list_i=0;  //当前需要的字段名
	    		for(col_list_i=0;col_list_i<col_list_size;col_list_i++) {
	    			colValueIsNull=true;//默认未找到
	    			Iterator<?> iter = rowMap.entrySet().iterator(); 
	    			while(iter.hasNext()){
					Map.Entry entry = (Map.Entry) iter.next(); 
	    			 String key = entry.getKey()+"";
	    			 if(key.equals(col_list.get(col_list_i))){
			    			 Object val = entry.getValue(); 
			    			 this.addColdata(val+"");
			    			 colValueIsNull=false;
			    			 break;
	    			 }
	    			}
	    			//如果未找到，就加入空值
	    			if(colValueIsNull){
	    				this.addColdata("");
	    			}
	    		}
	    		
	    	}
	    	
	    	
	    	this.commitData();
	    }
	    
	    
	    /* 
	     * 这里生成的是符合flexgrid for jquery 的json格式字符串
	     * 其格式如下：
	     * {
	        page:1,
	        total:0,
	        rows:[
	          {id:'row2',cell:['col','col','col','col','col','col']},
	          {id:'row3',cell:['col','col','col','col','col','col']},
	          {id:'row1',cell:['col','col','col','col','col','col']}
	           ]
	        }
	     */
	    /*
	    public String toString(){
	        StringBuffer sb=new StringBuffer();
	        sb.append("{\r\n");
	        sb.append("page:").append(page).append(",\r\n");
	        sb.append("total:").append(total).append(",\r\n");
	        sb.append("rows:[\r\n");
	        
	        int keynum=1;
	        List<RowData> rowdatalist=this.rows;
	        for(RowData rowdata:rowdatalist){
	            sb.append("  {id:'").append(rowdata.getRowid()).append("',").append("cell:[");
	            int i=1;
	            List<String> coldatalist=rowdata.getColdata();
	            for(String data:coldatalist){
	                sb.append("'").append(data).append("'");
	                if(i<coldatalist.size()){
	                    sb.append(",");
	                }
	                i++;
	            }
	            
	            if(keynum<rowdatalist.size()){
	                sb.append("]},\r\n");
	            }else{
	                sb.append("]}\r\n");
	            }
	            
	            keynum++;
	        }
	        sb.append("  ]\r\n}");
	        return sb.toString();
	    }*/
	    
	    //getFlxGridJSONData by ListMap
	    /*
	     * request: flexgrid request 请求
	     * count: 数量
	     * dataList： 查询出来的list
	     * colList: 显示的列list
	     */
	    
}
	    