package DiscreteEventSim.maven.eclipse;

import org.apache.commons.math3.distribution.*;

import java.io.FileNotFoundException;
import java.util.Random;
public class ProcessGenerator {
	
	public static void generatePoissonProcessSet(String file, double mean, int maxBurst, int numbOfProcesses) {
		PoissonDistribution pd = new PoissonDistribution(mean);
		Process procs[] = new Process[numbOfProcesses];
		Random rnd = new Random();
		
		for(int i=0;i<procs.length;i++) {
			int arrival = pd.sample();
			int burst = (int)((maxBurst-1)*rnd.nextDouble())+1;
			procs[i] = new Process((char)(i),burst,arrival);
			//System.out.println(procs[i]);
		}
		
		Process.writeProcessesToFile(file, procs);
		
	}
	
	public static void generateNormalProcessSet(String file, double mean, int maxBurst, int numbOfProcesses) {
		NormalDistribution pd = new NormalDistribution();
		Process procs[] = new Process[numbOfProcesses];
		Random rnd = new Random();
		
		for(int i=0;i<procs.length;i++) {
			int arrival = (int)(pd.sample()*10);
			int burst = (int)((maxBurst-1)*rnd.nextDouble())+1;
			procs[i] = new Process((char)(i),burst,arrival);
			//System.out.println(procs[i]);
		}
		
		Process.writeProcessesToFile(file, procs);
		
	}
	
	
	
	
}
