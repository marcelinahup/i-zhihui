package com.palmyou.fw.ui.flexgrid;

import java.io.Serializable;
import java.util.List;

public class RowData implements Serializable {
	
	private static final long serialVersionUID = 1349360381897104350L;
	
	String id=null;
     List<String> cell=null;
     
     public RowData(){
         
     }
     
     public RowData(String id,List<String> cell){
         this.id=id;
         this.cell=cell;
     }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getCell() {
		return cell;
	}

	public void setCell(List<String> cell) {
		this.cell = cell;
	}
     
    
}
