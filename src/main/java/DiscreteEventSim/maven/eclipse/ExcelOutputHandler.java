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
		
		for(int i=0;i<labels.length;i++) {
			labelComp.createCell(i).setCellValue(labels[i]);
			labelTurn.createCell(i).setCellValue(labels[i]);
			labelResp.createCell(i).setCellValue(labels[i]);
			labelWait.createCell(i).setCellValue(labels[i]);
		}
		
		
		for(int x=1;x<=transposedInfo.length;x++) {
			Row rowComp =  completion.createRow(x);
			Row rowTurn =  turnaround.createRow(x);
			Row rowResp =  response  .createRow(x);
			Row rowWait =  waiting   .createRow(x);
			for(int i =0; i<transposedInfo[x-1].length;i++) {
				rowComp.createCell(i).setCellValue(transposedInfo[x-1][i].getCompletionTime());
				rowTurn.createCell(i).setCellValue(transposedInfo[x-1][i].getTurnaroundTime());
				rowResp.createCell(i).setCellValue(transposedInfo[x-1][i].getResponseTime());
				rowWait.createCell(i).setCellValue(transposedInfo[x-1][i].getWaitingTime());
			
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
		
		labelComp.createCell(0).setCellValue("Process ID");
		labelTurn.createCell(0).setCellValue("Process ID");
		labelResp.createCell(0).setCellValue("Process ID");
		labelWait.createCell(0).setCellValue("Process ID");
		
		for(int i=0;i<labels.length;i++) {
			offset = (int)(i/setSize)+1;
			labelComp.createCell(i+offset+1).setCellValue(labels[i]);
			labelTurn.createCell(i+offset+1).setCellValue(labels[i]);
			labelResp.createCell(i+offset+1).setCellValue(labels[i]);
			labelWait.createCell(i+offset+1).setCellValue(labels[i]);
		}
		
		
		for(int x=1;x<=transposedInfo.length;x++) {
			Row rowComp =  completion.createRow(x);
			Row rowTurn =  turnaround.createRow(x);
			Row rowResp =  response  .createRow(x);
			Row rowWait =  waiting   .createRow(x);
			
			rowComp.createCell(0).setCellValue(x-1);
			rowTurn.createCell(0).setCellValue(x-1);
			rowResp.createCell(0).setCellValue(x-1);
			rowWait.createCell(0).setCellValue(x-1);
			for(int i =0; i<transposedInfo[x-1].length;i++) {
				offset = (int)(i/setSize)+1;
				rowComp.createCell(i+offset+1).setCellValue(transposedInfo[x-1][i].getCompletionTime());
				rowTurn.createCell(i+offset+1).setCellValue(transposedInfo[x-1][i].getTurnaroundTime());
				rowResp.createCell(i+offset+1).setCellValue(transposedInfo[x-1][i].getResponseTime());
				rowWait.createCell(i+offset+1).setCellValue(transposedInfo[x-1][i].getWaitingTime());
			
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
	
	
	
		
}
