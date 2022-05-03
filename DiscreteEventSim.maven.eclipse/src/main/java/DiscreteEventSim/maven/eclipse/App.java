package DiscreteEventSim.maven.eclipse;

import java.io.FileNotFoundException; 
import java.util.*;
  
public class App { 
	public static void main(String[] args) {   
		
		
		ProcessGenerator.generatePoissonProcessSet("processSet0.json", 15, 15, 25);
		ProcessGenerator.generatePoissonProcessSet("processSet1.json", 15, 15, 25);
		ProcessGenerator.generatePoissonProcessSet("processSet2.json", 15, 15, 25);
		ProcessGenerator.generatePoissonProcessSet("processSet3.json", 15, 15, 25);
		ProcessGenerator.generatePoissonProcessSet("processSet4.json", 15, 15, 25);
		
		
		Process[][] procs = new Process[20][25];
		try {
				for(int i=0;i<procs.length;i++) {
					procs[i] = Process.readProcessesFromFile("processSet"+(i%5)+".json");
					Arrays.sort(procs[i],new SortByArrival());
				}
			
			}
		catch (FileNotFoundException e) {e.printStackTrace();}
		
		
		Process[][] setAnalysis = batchVariedSimulation(procs,5,5);
		String[] labels = {
				"FCFS","FCFS","FCFS","FCFS","FCFS",
				"SJF","SJF","SJF","SJF","SJF",
				"SJN","SJN","SJN","SJN","SJN",
				"RR","RR","RR","RR","RR"};
		
		ExcelOutputHandler myBook = new ExcelOutputHandler();
		myBook.recordBatchOutputs(setAnalysis,labels,5);
		myBook.saveToFile("book");
		
		
	}
	
	public static Process[][] batchSimulation(Process[][] multiQueue, int algorithm,int timeQuantum){
		@SuppressWarnings("unchecked")
		Process[][] finalizedProcesses = new Process[multiQueue.length][multiQueue[0].length];
		for(int i=0;i<multiQueue.length;i++) {
				finalizedProcesses[i] = simulation(multiQueue[i],algorithm,timeQuantum);
		}
		return finalizedProcesses;
	}
	
	public static Process[][] batchVariedSimulation(Process[][] multiQueue,int timeQuantum,int batchCount){
		int count = 0;
		int algorithm = 0;
		@SuppressWarnings("unchecked")
		Process[][] finalizedProcesses = new Process[multiQueue.length][multiQueue[0].length];
		for(int i=0;i<multiQueue.length;i++) {
				System.out.println(algorithm+" -- "+i);
				finalizedProcesses[i] = simulation(multiQueue[i],algorithm,timeQuantum);
				count++;
				if(count==batchCount) {
					count = 0;
					algorithm = (algorithm+1)%4;
					
				}
				
		}
		return finalizedProcesses;
	}
	
	
	
	public static Process[] simulation(Process[] arrivalQueue,int algorithm, int timeQuantum) {
		// algorithms:
		// 0 - first come first serve, 1 - shortest job first, 2 - shortest job next, 3 - round robin
		
		Arrays.sort(arrivalQueue,new SortByArrival());
		ArrayList<Process> processingQueue = new ArrayList<Process>();
		ArrayList<Process> finishQueue = new ArrayList<Process>();
		int time = 0;
		int timeQuantumStep = 0;
		int arrivalCounter = 0;
		
		while(!(processingQueue.isEmpty() && arrivalCounter >= arrivalQueue.length)) {
			while(arrivalCounter < arrivalQueue.length && arrivalQueue[arrivalCounter].getArrival() == time) {
				processingQueue.add(arrivalQueue[arrivalCounter]);
				//System.out.println("Added: "+arrivalQueue[arrivalCounter].getID());
				arrivalCounter++;
			}
			
			
			if(!processingQueue.isEmpty()) {
				
				
				
				if(algorithm == 0) { 
					processingQueue.get(0).timeStep(time);
					//System.out.println("Time: "+time+",\tProcess: "+processingQueue.get(0).getID()+",\tRemaining Time: "+processingQueue.get(0).getTimeRemaining());
					if(processingQueue.get(0).isDone()) {finishQueue.add(processingQueue.remove(0));}
				}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				else if(algorithm == 1) { 
					if(processingQueue.size()>0){
						Collections.sort(processingQueue.subList(1,processingQueue.size()), new SortByBurst());
					}
					processingQueue.get(0).timeStep(time);
					//System.out.println("Time: "+time+",\tProcess: "+processingQueue.get(0).getID()+",\tRemaining Time: "+processingQueue.get(0).getTimeRemaining());
					
					if(processingQueue.get(0).isDone()) {finishQueue.add(processingQueue.remove(0));}
				}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				else if(algorithm == 2) { 
					if(processingQueue.size()>0){
						Collections.sort(processingQueue, new SortByBurst());
					}
					processingQueue.get(0).timeStep(time);
					//System.out.println("Time: "+time+",\tProcess: "+processingQueue.get(0).getID()+",\tRemaining Time: "+processingQueue.get(0).getTimeRemaining());
					
					if(processingQueue.get(0).isDone()) {finishQueue.add(processingQueue.remove(0));}
				}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				else if(algorithm == 3) { 
					processingQueue.get(0).timeStep(time);
					//System.out.println("Time: "+time+",\tProcess: "+processingQueue.get(0).getID()+",\tRemaining Time: "+processingQueue.get(0).getTimeRemaining());
					timeQuantumStep++;
					if(processingQueue.get(0).isDone()) {
						timeQuantumStep=0;
						finishQueue.add(processingQueue.remove(0));
					}
					else if(timeQuantumStep == timeQuantum) {
						timeQuantumStep=0;
						processingQueue.add(processingQueue.remove(0));
					}
				}
			}
			time++;
			
			
		}
		
		
		//System.out.println("\n\n");
		for(int i=0;i<finishQueue.size();i++) {
			//System.out.println(finishQueue.get(i).timeAnalysis()+"\n");
		}
		Process[] ret = new Process[finishQueue.size()];
		for(int i=0;i<ret.length;i++) {
			ret[i] = finishQueue.get(i);
		}
		
		Arrays.sort(ret,new SortByID());
		return ret;
	}
	   
	   
		   
		   
	


	
	
	
	
	
	
	
	
	
	
	
	
	
} 





	
	

	
	
	