package DiscreteEventSim.maven.eclipse;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import DiscreteEventSim.maven.eclipse.Process;


  
class Process{
	private int ID;
	private int burst;
	private int arrival;
	private int priority;
	private int timePassed = 0;
	
	private String algoUsed = "";
	
	private int completionTime;
	private int firstRun;
	
	public Process() {}
	public Process(char ID,int burst,int arrival) {
		this.ID = ID;
		this.burst = burst;
		this.arrival = arrival;
	}
	
	public String toString() { 
	      return "Process [ ID: "+ID+",\tBurst Time: "+ burst+",\t\tArrival Time: "+arrival+ " ]"; 
	   }
	
	public String timeAnalysis() {
		return "Process [ ID: "+ID+",\tBurst Time: "+ burst+",\tArrival Time: "+arrival+ " ]"
				+"\n\t Completion Time: "+getCompletionTime()+"\n\t Turnaround Time: "+getTurnaroundTime()
				+"\n\t Response Time: "+getResponseTime()+"\n\t Waiting Time: "+getWaitingTime()
				+"\n\t First Run: "+firstRun; 
	}
	public int getCompletionTime() {return completionTime;}
	public void setCompletionTime(int completionTime) {this.completionTime = completionTime;}
	public int getPriority() {return priority;}
	public void setPriority(int priority) {this.priority = priority;}
	public int getArrival() {return arrival;}
	public void setArrival(int arrival) {this.arrival = arrival;}
	public int getBurst() {return burst;}
	public void setBurst(int burst) {this.burst = burst;}
	public int getID() {return ID;}
	public void setID(int iD) {ID = iD;}
	public int getTimePassed() {return timePassed;}
	public void setTimePassed(int timePassed) {this.timePassed = timePassed;}
	public void timeStep(int time) {
		if(timePassed == 0) {firstRun = time;}
		this.timePassed++;
		if(getTimeRemaining() == 0) {completionTime = time+1;}
		}
	public boolean isDone() {return getTimeRemaining()<=0;}
	public int getTimeRemaining() {return this.burst-this.timePassed;}
	
	
	public int getTurnaroundTime() {return getCompletionTime()-this.arrival;}
	public int getResponseTime() {return this.firstRun-this.arrival;}
	public int getWaitingTime() {return getTurnaroundTime()-this.burst;}
	
	
	
	
	static Process[] readProcessesFromFile(String file) throws FileNotFoundException { 
	      GsonBuilder builder = new GsonBuilder(); 
	      Gson gsonReader = builder.create(); 
	      BufferedReader bufferedReader = new BufferedReader(new FileReader(file));   
	      
	      Process[] proc = gsonReader.fromJson(bufferedReader, Process[].class); 
	      return proc; 
	   }
	static void writeProcessesToFile(String file,Process[] procs){ 
		Gson gson = new GsonBuilder().create();
		try {
			Writer writer = Files.newBufferedWriter(Paths.get(file));
			gson.toJson(procs, writer);
			writer.close();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	   }
	static Process[] generateRandomProcesses(int size,int maxArrive, int maxBurst){ 
			Random rand = new Random();
	      Process[] procs = new Process[size];
	      for(int i=0;i<procs.length;i++) {
	    	  procs[i] = new Process();
	    	  procs[i].setID((i*10)+rand.nextInt(10));
	    	  procs[i].setArrival(1+rand.nextInt(maxArrive));
	    	  procs[i].setBurst(1+rand.nextInt(maxBurst));
	      }
	      
	      return procs; 
	   }

	

	
	
	
	
}


class SortByArrival implements Comparator<Process>{
	
	 public int compare(Process a, Process b)
	    {
	        return a.getArrival() - b.getArrival();
	    }
}

class SortByID implements Comparator<Process>{
	
	 public int compare(Process a, Process b)
	    {
	        return a.getID() - b.getID();
	    }
}

class SortByBurst implements Comparator<Process>{
	
	 public int compare(Process a, Process b)
	    {
	        return a.getBurst() - b.getBurst();
	    }
}


