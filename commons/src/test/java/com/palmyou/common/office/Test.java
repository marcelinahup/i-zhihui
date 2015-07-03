package com.palmyou.common.office;

import com.hk.commons.office.Excel_03;

public class Test {

	public static void main(String[] args) throws Exception {

		Excel_03 excel_03 = new Excel_03("d:/Excel导出模版.xls", "作业分析");
		excel_03.useSheet(1);

		// 插入10-1行
		int size = 10;
//		excel_03.insertRows(3, size - 1);
		
		// 下一个表格的起始位置13
		int nextStart = 1 + 2 + size + 3;
		
		for (int i = 0; i < 5; i++) {
			
			// 下一个表格的起始位置13
			if (i != 0) {
				nextStart += size + 2 + 1;
			}

			if (i == 0) {
				excel_03.setValue("A15", "自定义组课后作业作业分析表");
			}
			
			excel_03.copyRowsWithStyle(1, size + 2, nextStart);
			
			excel_03.setValue("A" + nextStart, "组1");

			excel_03.setValue("C" + (nextStart + 1), "选择人数");
			excel_03.setValue("A" + (nextStart + 2), "题目");
			excel_03.setValue("B" + (nextStart + 2), "正确率");
			excel_03.setValue("C" + (nextStart + 2), "A");
			excel_03.setValue("D" + (nextStart + 2), "B");
			excel_03.setValue("E" + (nextStart + 2), "C");
			excel_03.setValue("F" + (nextStart + 2), "D");

		}
		// excel_03.copyColumnsWithStyle(2, 13,17);

		// ----------------------------------------

		// excel_03.insertOneRow(6);
		// excel_03.insertRows(6, 12);
		// for (int i = 0; i < 10; i++) {
		//
		// excel_03.insertOneRow(6);
		// }

		excel_03.writeTo("d:/3.xls");
	}

}
