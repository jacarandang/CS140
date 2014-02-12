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
		for(int i = 0 ; i < 1; i++){
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
		}
		Collections.sort(pop);
		Unit un = null;
		while(!pop.isEmpty()) System.out.println((un=pop.poll()).toString());
		Main.program(un, true);
	}
	
	public static void normalize(LinkedList<Unit> u){
		double total = 0;
		for(int i = 0 ; i < u.size(); i++){
			total += u.get(i).s.fitness();
		}
		for(int i = 0 ; i < u.size(); i++){
			Unit un = u.get(i);
			un.norm = un.s.fitness()/total;
		}
	}
}