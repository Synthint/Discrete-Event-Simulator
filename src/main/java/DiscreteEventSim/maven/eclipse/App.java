package DiscreteEventSim.maven.eclipse;

import java.io.FileNotFoundException; 
import java.util.*;
import java.util.Scanner; 
  
public class App { 
	public static void main(String[] args) {   
		
		
		
		int numbOfSets = getInputInt("Number of process sets to simulate between 1 and 25?"
				+ " \n (note: each process set is simulated 4 times, once for each CPU scheduling algorithm)", 1, 25);
		int processesPerSet = getInputInt("Number of processes per set between 1 and 50?",1,50);
		int poissonMean = getInputInt("This simulator gives processes different arrival times based on the Poisson distribution,"
				+ "\n what is the mean arrival time between 3 and 100",3,100);
		int maxBurst = getInputInt("max burst time per process between 5 and 50?",5,50);
		int roundRobinQuantum = getInputInt("round robin requires a time quantum for simulation, what is the time quantum 1 and 25?",1,25);
		
		String excelFile = getInputString("Excel output file names (alphnumeric characters only)");
		
		
		
		
		
		for(int i=0;i<numbOfSets;i++) {
			ProcessGenerator.generatePoissonProcessSet("processSet"+i+".json", (double)poissonMean, maxBurst,processesPerSet);
		}
		
		
		Process[][] procs = new Process[numbOfSets*4][processesPerSet];
		try {
				for(int i=0;i<procs.length;i++) {
					procs[i] = Process.readProcessesFromFile("processSet"+(i%numbOfSets)+".json");
					Arrays.sort(procs[i],new SortByArrival());
				}
			
			}
		catch (FileNotFoundException e) {e.printStackTrace();}
		
		
		Process[][] setAnalysis = batchVariedSimulation(procs,roundRobinQuantum,numbOfSets);
		
		
		String[] labels = new String[numbOfSets*4];
		for(int x=0;x<labels.length;x++) {
			if(x<numbOfSets) {
				labels[x] = "FCFS";
			}else if(x<numbOfSets*2) {
				labels[x] = "SJF";
			}else if(x<numbOfSets*3) {
				labels[x] = "SJN";
			}else if(x<numbOfSets*4) {
				labels[x] = "RR";
			}
			
		}
		
		ExcelOutputHandler myBook = new ExcelOutputHandler();
		myBook.recordBatchOutputs(setAnalysis,labels,numbOfSets);
		myBook.saveToFile(excelFile);
		
		System.out.println("\n\n PROGRAM OUTPUT IN: "+excelFile+".xslx");
	}
	
	public static Process[][] batchSimulation(Process[][] multiQueue, int algorithm,int timeQuantum){
		Process[][] finalizedProcesses = new Process[multiQueue.length][multiQueue[0].length];
		for(int i=0;i<multiQueue.length;i++) {
				finalizedProcesses[i] = simulation(multiQueue[i],algorithm,timeQuantum);
		}
		return finalizedProcesses;
	}
	
	public static Process[][] batchVariedSimulation(Process[][] multiQueue,int timeQuantum,int batchCount){
		int count = 0;
		int algorithm = 0;
		Process[][] finalizedProcesses = new Process[multiQueue.length][multiQueue[0].length];
		for(int i=0;i<multiQueue.length;i++) {
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
	   
	   
		   
	public static int getInputInt(String prompt,int min,int max) {
		System.out.print("\n\n"+prompt+" >>> ");
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		int input = scan.nextInt();
		if(input < min || input > max) {
			input = getInputInt(prompt,min,max);
		}
		return input;
	}   
	
	public static String getInputString(String prompt) {
		System.out.print("\n\n"+prompt+" >>> ");
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();
		if(!input.matches("[A-Za-z0-9]*")) {
			input = getInputString(prompt);
		}
		return input;
	}  
	


	
	
	
	
	
	
	
	
	
	
	
	
	
} 





	
	

	
	
	