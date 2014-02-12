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
		for(int i = 0 ; i < 10; i++){
			for(int j = 0 ; j < gen.size(); j++){
				Unit u = gen.poll();
				u.s = Main.program(u, false);
				gen.offer(u);
				avg += u.s.fitness();
			}
			Collections.sort(gen);
			avg/=(double)gen.size();
			if(lavg > avg) System.out.println("Warning: generation "+i+" has lower average fitness");
			lavg = avg;
			System.out.println(avg);
			while(!gen.isEmpty()){
				pop.offer(gen.poll());
			}
			
			for(int j = 0; j < 100; j++){
				double a = Math.random();
				Unit au, bu;
				au = new Unit();
				bu = new Unit();
				for(int k = 0; k < pop.size(); k++){
					if(pop.get(k).s.fitness() > a){
						au = pop.get(k); 
					}
				}
				a = Math.random();
				for(int k = 0; k < pop.size(); k++){
					if(pop.get(k).s.fitness() > a){
						bu = pop.get(k); 
					}
				}
				gen.offer(au.crossover(bu));
			}
		}
		Collections.sort(pop);
		for(int i = 0 ; i < pop.size(); i++){
			System.out.println(pop.get(i).toString());
		}
	}
}