import java.util.*;

public class Unit implements Comparable<Unit>{
	
	double trm;		//time remaining multiplier
	double prm;		//priority multiplier
	double wtm;		//waiting time multiplier
	double tkm;		//task remaining multiplier
	double ped;		//preemption difference
	Stat s;
	double norm;
	
	public Unit(double a, double b, double c, double d, double e){
		trm = a;
		prm = b;
		wtm = c;
		tkm = d;
		ped = e;
		s = null;
		norm = -1;
	}
	
	public Unit(){
		trm = (Math.random()*10) - 5;
		prm = (Math.random()*10) - 5;
		wtm = (Math.random()*10) - 5;
		tkm = (Math.random()*10) - 5;
		ped = (Math.random()*5) + 5; 
	}
	
	public Unit crossover(Unit other){
		Random r = new Random();
		double a, b, c, d, e, f;
		if(r.nextBoolean()) a = trm;
		else a = other.trm;
		if(r.nextBoolean())b = prm;
		else b = other.prm;
		if(r.nextBoolean())c = wtm;
		else c = other.wtm;
		if(r.nextBoolean())d = tkm;
		else d = other.tkm;
		if(r.nextBoolean())e = ped;
		else e = other.ped;
		return new Unit(a, b, c, d, e);
	}
	
	public void mutate(){
		Random r = new Random();
		if(r.nextFloat() <= 0.005){
			int idx = r.nextInt(5);
			if(idx == 0) trm += (r.nextFloat()*2)-1;
			else if(idx == 1) prm += (r.nextFloat()*2)-1;
			else if(idx == 2) wtm += (r.nextFloat()*2)-1;
			else if(idx == 3) tkm += (r.nextFloat()*2)-1;
			else if(idx == 4) ped += (r.nextFloat()*2)-1;
		}
	}
	
	public String toString(){
		return trm+","+prm+","+wtm+","+tkm+","+ped+","+s.fitness();
	}
	
	public String toCfg(){
		return trm+" "+prm+" "+wtm+" "+tkm+" "+ped;
	}
	
	public int compareTo(Unit u){
		return (int)(u.s.fitness()*100 - s.fitness()*100);
	}
	
}
