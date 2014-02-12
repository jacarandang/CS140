import java.util.*;

public class Stat{
	
	int time;
	float cpuutil;	//cpu utilization
	double avgwait;	//average waiting time
	double avgprio;	//avarage priority difference
	double fitness;
	
	public Stat(int time, float a, double b, double c){
		this.time = time;
		cpuutil = a;
		avgwait = b;
		avgprio = c;
		fitness = -1;
	}
	
	
	public double fitness(){
		if(fitness == -1)fitness = ((time*cpuutil-avgwait+avgprio)/(double)time*100);
		return fitness;
	}
	
	public String toString(){
		return time+","+cpuutil+","+avgwait+","+avgprio+","+fitness();
	}
}