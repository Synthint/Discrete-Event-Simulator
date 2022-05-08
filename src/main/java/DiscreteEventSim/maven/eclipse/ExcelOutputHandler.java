package DiscreteEventSim.maven.eclipse;

import java.io.File;
import java.io.FileNotFoundException;



import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ExcelOutputHandler {
	Workbook timeRecords = new XSSFWorkbook();
	Sheet completion = timeRecords.createSheet("Completion Time Analysis");
	Sheet turnaround = timeRecords.createSheet("Turnaround Time Analysis");
	Sheet  response = timeRecords.createSheet("Response Time Analysis");
	Sheet  waiting = timeRecords.createSheet("Waiting Time Analysis");
	Sheet arrival = timeRecords.createSheet("Arrrival Time");
	
	public ExcelOutputHandler() {}
	


public void recordBatchOutputs(Process[][] processInfo) {
		
		Process[][] transposedInfo = new Process[processInfo[0].length][processInfo.length];
		for(int a=0;a<transposedInfo.length;a++) {
			for(int b=0;b<transposedInfo[a].length;b++) {
				transposedInfo[a][b] = processInfo[b][a];
			}
		}
		
		
		
		for(int x=0;x<transposedInfo.length;x++) {
			Row rowComp =  completion.createRow(x);
			Row rowTurn =  turnaround.createRow(x);
			Row rowResp =  response  .createRow(x);
			Row rowWait =  waiting   .createRow(x);
			for(int i =0; i<transposedInfo[x].length;i++) {
				rowComp.createCell(i).setCellValue(transposedInfo[x][i].getCompletionTime());
				rowTurn.createCell(i).setCellValue(transposedInfo[x][i].getTurnaroundTime());
				rowResp.createCell(i).setCellValue(transposedInfo[x][i].getResponseTime());
				rowWait.createCell(i).setCellValue(transposedInfo[x][i].getWaitingTime());
			
			}
		}
		
		
	}

	public void recordBatchOutputs(Process[][] processInfo, String[] labels) {
		
		Process[][] transposedInfo = new Process[processInfo[0].length][processInfo.length];
		for(int a=0;a<transposedInfo.length;a++) {
			for(int b=0;b<transposedInfo[a].length;b++) {
				transposedInfo[a][b] = processInfo[b][a];
			}
		}
	
		Row labelComp =  completion.createRow(0);
		Row labelTurn =  turnaround.createRow(0);
		Row labelResp =  response  .createRow(0);
		Row labelWait =  waiting   .createRow(0);
		Row labelAriv =  arrival   .createRow(0);
		
		for(int i=0;i<labels.length;i++) {
			String label = labels[i]+" - Process "+(i%processInfo.length);
			labelComp.createCell(i).setCellValue(label);
			labelTurn.createCell(i).setCellValue(label);
			labelResp.createCell(i).setCellValue(label);
			labelWait.createCell(i).setCellValue(label);
			labelAriv.createCell(i).setCellValue(label);
		}
		
		
		for(int x=1;x<=transposedInfo.length;x++) {
			Row rowComp =  completion.createRow(x);
			Row rowTurn =  turnaround.createRow(x);
			Row rowResp =  response  .createRow(x);
			Row rowWait =  waiting   .createRow(x);
			Row rowAriv =  waiting   .createRow(x);
			for(int i =0; i<transposedInfo[x-1].length;i++) {
				rowComp.createCell(i).setCellValue(transposedInfo[x-1][i].getCompletionTime());
				rowTurn.createCell(i).setCellValue(transposedInfo[x-1][i].getTurnaroundTime());
				rowResp.createCell(i).setCellValue(transposedInfo[x-1][i].getResponseTime());
				rowWait.createCell(i).setCellValue(transposedInfo[x-1][i].getWaitingTime());
				rowAriv.createCell(i).setCellValue(transposedInfo[x-1][i].getArrival());
			
			}
		}
	}

	public void recordBatchOutputs(Process[][] processInfo, String[] labels, int setSize) {
		
		Process[][] transposedInfo = new Process[processInfo[0].length][processInfo.length];
		for(int a=0;a<transposedInfo.length;a++) {
			for(int b=0;b<transposedInfo[a].length;b++) {
				transposedInfo[a][b] = processInfo[b][a];
			}
		}
		
		int offset = 0;
	
		Row labelComp =  completion.createRow(0);
		Row labelTurn =  turnaround.createRow(0);
		Row labelResp =  response  .createRow(0);
		Row labelWait =  waiting   .createRow(0);
		Row labelAriv =  arrival   .createRow(0);
		
		labelComp.createCell(0).setCellValue("Process ID");
		labelTurn.createCell(0).setCellValue("Process ID");
		labelResp.createCell(0).setCellValue("Process ID");
		labelWait.createCell(0).setCellValue("Process ID");
		labelAriv.createCell(0).setCellValue("Process ID");
		
		for(int i=0;i<labels.length;i++) {
			offset = (int)(i/setSize)+1;
			String label = labels[i]+" - Process Set"+(i%setSize);
			labelComp.createCell(i+offset+1).setCellValue(label);
			labelTurn.createCell(i+offset+1).setCellValue(label);
			labelResp.createCell(i+offset+1).setCellValue(label);
			labelWait.createCell(i+offset+1).setCellValue(label);
			labelAriv.createCell(i+offset+1).setCellValue(label);
		}
		
		int rowNum = 0;
		
		for(int x=1;x<=transposedInfo.length;x++) {
			Row rowComp =  completion.createRow(x);
			Row rowTurn =  turnaround.createRow(x);
			Row rowResp =  response  .createRow(x);
			Row rowWait =  waiting   .createRow(x);
			Row rowAriv =  arrival   .createRow(x);
			
			rowComp.createCell(0).setCellValue(x-1);
			rowTurn.createCell(0).setCellValue(x-1);
			rowResp.createCell(0).setCellValue(x-1);
			rowWait.createCell(0).setCellValue(x-1);
			rowAriv.createCell(0).setCellValue(x-1);
			
			for(int i =0; i<transposedInfo[x-1].length;i++) {
				offset = (int)(i/setSize)+1;
				rowComp.createCell(i+offset+1).setCellValue(transposedInfo[x-1][i].getCompletionTime());
				rowTurn.createCell(i+offset+1).setCellValue(transposedInfo[x-1][i].getTurnaroundTime());
				rowResp.createCell(i+offset+1).setCellValue(transposedInfo[x-1][i].getResponseTime());
				rowWait.createCell(i+offset+1).setCellValue(transposedInfo[x-1][i].getWaitingTime());
				rowAriv.createCell(i+offset+1).setCellValue(transposedInfo[x-1][i].getArrival());
			
			}
			rowNum = x;
		}
		
		
		rowNum+=2;

		Row rowComp =  completion.createRow(rowNum);
		Row rowTurn =  turnaround.createRow(rowNum);
		Row rowResp =  response  .createRow(rowNum);
		Row rowWait =  waiting   .createRow(rowNum);
		
		Row rowComp2 =  completion.createRow(rowNum+3);
		Row rowTurn2 =  turnaround.createRow(rowNum+3);
		Row rowResp2 =  response  .createRow(rowNum+3);
		Row rowWait2 =  waiting   .createRow(rowNum+3);
		
		rowComp.createCell(0).setCellValue("Set AVG");
		rowTurn.createCell(0).setCellValue("Set AVG");
		rowResp.createCell(0).setCellValue("Set AVG");
		rowWait.createCell(0).setCellValue("Set AVG");
		
		
		
		for(int x=0;x<setSize;x++) {
			int set0Loc = 2+x;
			int set1Loc = 3+x+(setSize*1);
			int set2Loc = 4+x+(setSize*2);
			int set3Loc = 5+x+(setSize*3);
			rowComp.createCell(set0Loc).setCellFormula("AVERAGE("+getExcelColumn(set0Loc)+"2:"+getExcelColumn(set0Loc)+""+(rowNum-1)+")");
			rowComp.createCell(set1Loc).setCellFormula("AVERAGE("+getExcelColumn(set1Loc)+"2:"+getExcelColumn(set1Loc)+""+(rowNum-1)+")");
			rowComp.createCell(set2Loc).setCellFormula("AVERAGE("+getExcelColumn(set2Loc)+"2:"+getExcelColumn(set2Loc)+""+(rowNum-1)+")");
			rowComp.createCell(set3Loc).setCellFormula("AVERAGE("+getExcelColumn(set3Loc)+"2:"+getExcelColumn(set3Loc)+""+(rowNum-1)+")");
			
			rowTurn.createCell(set0Loc).setCellFormula("AVERAGE("+getExcelColumn(set0Loc)+"2:"+getExcelColumn(set0Loc)+""+(rowNum-1)+")");
			rowTurn.createCell(set1Loc).setCellFormula("AVERAGE("+getExcelColumn(set1Loc)+"2:"+getExcelColumn(set1Loc)+""+(rowNum-1)+")");
			rowTurn.createCell(set2Loc).setCellFormula("AVERAGE("+getExcelColumn(set2Loc)+"2:"+getExcelColumn(set2Loc)+""+(rowNum-1)+")");
			rowTurn.createCell(set3Loc).setCellFormula("AVERAGE("+getExcelColumn(set3Loc)+"2:"+getExcelColumn(set3Loc)+""+(rowNum-1)+")");
			
			rowResp.createCell(set0Loc).setCellFormula("AVERAGE("+getExcelColumn(set0Loc)+"2:"+getExcelColumn(set0Loc)+""+(rowNum-1)+")");
			rowResp.createCell(set1Loc).setCellFormula("AVERAGE("+getExcelColumn(set1Loc)+"2:"+getExcelColumn(set1Loc)+""+(rowNum-1)+")");
			rowResp.createCell(set2Loc).setCellFormula("AVERAGE("+getExcelColumn(set2Loc)+"2:"+getExcelColumn(set2Loc)+""+(rowNum-1)+")");
			rowResp.createCell(set3Loc).setCellFormula("AVERAGE("+getExcelColumn(set3Loc)+"2:"+getExcelColumn(set3Loc)+""+(rowNum-1)+")");
			
			rowWait.createCell(set0Loc).setCellFormula("AVERAGE("+getExcelColumn(set0Loc)+"2:"+getExcelColumn(set0Loc)+""+(rowNum-1)+")");
			rowWait.createCell(set1Loc).setCellFormula("AVERAGE("+getExcelColumn(set1Loc)+"2:"+getExcelColumn(set1Loc)+""+(rowNum-1)+")");
			rowWait.createCell(set2Loc).setCellFormula("AVERAGE("+getExcelColumn(set2Loc)+"2:"+getExcelColumn(set2Loc)+""+(rowNum-1)+")");
			rowWait.createCell(set3Loc).setCellFormula("AVERAGE("+getExcelColumn(set3Loc)+"2:"+getExcelColumn(set3Loc)+""+(rowNum-1)+")");
			
			if(x==setSize-1) {
				rowNum++;
				rowComp2.createCell(0).setCellValue("Algo AVG");
				rowTurn2.createCell(0).setCellValue("Algo AVG");
				rowResp2.createCell(0).setCellValue("Algo AVG");
				rowWait2.createCell(0).setCellValue("Algo AVG");
				
				rowComp2.createCell(1).setCellFormula("AVERAGE("+getExcelColumn(set0Loc-(setSize-1))+""+rowNum+":"+getExcelColumn(set0Loc)+""+rowNum+")");
				rowComp2.createCell(2).setCellFormula("AVERAGE("+getExcelColumn(set1Loc-(setSize-1))+""+rowNum+":"+getExcelColumn(set1Loc)+""+rowNum+")");
				rowComp2.createCell(3).setCellFormula("AVERAGE("+getExcelColumn(set2Loc-(setSize-1))+""+rowNum+":"+getExcelColumn(set2Loc)+""+rowNum+")");
				rowComp2.createCell(4).setCellFormula("AVERAGE("+getExcelColumn(set3Loc-(setSize-1))+""+rowNum+":"+getExcelColumn(set3Loc)+""+rowNum+")");
				
				rowTurn2.createCell(1).setCellFormula("AVERAGE("+getExcelColumn(set0Loc-(setSize-1))+""+rowNum+":"+getExcelColumn(set0Loc)+""+rowNum+")");
				rowTurn2.createCell(2).setCellFormula("AVERAGE("+getExcelColumn(set1Loc-(setSize-1))+""+rowNum+":"+getExcelColumn(set1Loc)+""+rowNum+")");
				rowTurn2.createCell(3).setCellFormula("AVERAGE("+getExcelColumn(set2Loc-(setSize-1))+""+rowNum+":"+getExcelColumn(set2Loc)+""+rowNum+")");
				rowTurn2.createCell(4).setCellFormula("AVERAGE("+getExcelColumn(set3Loc-(setSize-1))+""+rowNum+":"+getExcelColumn(set3Loc)+""+rowNum+")");
				
				rowResp2.createCell(1).setCellFormula("AVERAGE("+getExcelColumn(set0Loc-(setSize-1))+""+rowNum+":"+getExcelColumn(set0Loc)+""+rowNum+")");
				rowResp2.createCell(2).setCellFormula("AVERAGE("+getExcelColumn(set1Loc-(setSize-1))+""+rowNum+":"+getExcelColumn(set1Loc)+""+rowNum+")");
				rowResp2.createCell(3).setCellFormula("AVERAGE("+getExcelColumn(set2Loc-(setSize-1))+""+rowNum+":"+getExcelColumn(set2Loc)+""+rowNum+")");
				rowResp2.createCell(4).setCellFormula("AVERAGE("+getExcelColumn(set3Loc-(setSize-1))+""+rowNum+":"+getExcelColumn(set3Loc)+""+rowNum+")");
				
				rowWait2.createCell(1).setCellFormula("AVERAGE("+getExcelColumn(set0Loc-(setSize-1))+""+rowNum+":"+getExcelColumn(set0Loc)+""+rowNum+")");
				rowWait2.createCell(2).setCellFormula("AVERAGE("+getExcelColumn(set1Loc-(setSize-1))+""+rowNum+":"+getExcelColumn(set1Loc)+""+rowNum+")");
				rowWait2.createCell(3).setCellFormula("AVERAGE("+getExcelColumn(set2Loc-(setSize-1))+""+rowNum+":"+getExcelColumn(set2Loc)+""+rowNum+")");
				rowWait2.createCell(4).setCellFormula("AVERAGE("+getExcelColumn(set3Loc-(setSize-1))+""+rowNum+":"+getExcelColumn(set3Loc)+""+rowNum+")");
			}
			
		}
		
		
		
		
	}
	
	
	
	
	
	public void saveToFile(String file) {
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		String fileLocation = path.substring(0, path.length() - 1) + file+".xslx";
		
		
		try {
			FileOutputStream outputStream = new FileOutputStream(fileLocation);
			timeRecords.write(outputStream);
			timeRecords.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private String getExcelColumn(int columnNumber){
		columnNumber++;
	    String columnName = "";

	    while (columnNumber > 0)
	    {
	        int modulo = (columnNumber - 1) % 26;
	        columnName = (char)('A' + modulo) + columnName;
	        columnNumber = (columnNumber - modulo) / 26;
	    } 

	    return columnName;
	}
	
		
}
