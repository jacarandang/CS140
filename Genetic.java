import java.util.*;
import java.io.*;

public class Genetic{
	
	public static void main(String []args)throws Exception{
		LinkedList<Unit> pop = new LinkedList<Unit>();
		LinkedList<Unit> gen = new LinkedList<Unit>();
		for(int i = 0 ; i < 100; i++){
			gen.offer(new Unit());
		}
		
		double lavg = 0;
		double avg = 0;
		for(int i = 0 ; i < 25; i++){
			for(int j = 0 ; j < gen.size(); j++){
				Unit u = gen.poll();
				u.s = Main.program(u, false);
				gen.offer(u);
				avg += u.s.fitness();
			}
			avg/=(double)gen.size();
			if(lavg > avg) System.out.println("Warning: generation "+i+" has lower average fitness");
			lavg = avg;
			System.out.println("Generation "+i+" average fitness: "+avg);
			avg = 0;
			while(!gen.isEmpty()){
				pop.offer(gen.poll());
			}
			Collections.sort(pop);
			double tot = sum(pop);
			for(int j = 0 ; j < 100; j++){
				Unit pn = next(pop, tot).crossover(next(pop, tot));
				pn.mutate();
				gen.offer(pn);
			}
		}
		Collections.sort(pop);
		Main.program(pop.get(0), true);
		Unit un = null;
		while(!pop.isEmpty()) System.out.println((un=pop.poll()).toString());
		
	}
	
	public static double sum(LinkedList<Unit> u){
		double sum = 0;
		for(int i = 0 ; i < u.size(); i++) sum += u.get(i).s.fitness();
		return sum;
	}
	
	public static Unit next(LinkedList<Unit> u, double max){
		Random r = new Random();
		double d = r.nextDouble()*max;
		int csum = 0;
		for(int i = 0 ;  i < u.size(); i++){
			csum += u.get(i).s.fitness();
			if(csum > d){
				return u.get(i);
			}
		}
		return u.get(u.size() - 1);
	}
}