import java.io.*;
import java.util.*;

public class Main{
	
	public static void main(String []args)throws Exception{
		program(new Unit(-1.5099009021, 4.0653042855, 0.1807736872,2.7973578779,-3.1972732469) , true);
	}
	
	public static Stat program(Unit u, boolean out)throws Exception{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		String inp;
		
		BufferedReader in2 = new BufferedReader(new FileReader("tasklist.txt"));
		LinkedList<Recipe> rlist = new LinkedList<Recipe>();
		while(true){
			inp = in2.readLine();
			if(inp == null) break;
			StringTokenizer tok = new StringTokenizer(inp);
			rlist.addLast(new Recipe(tok.nextToken(), Integer.parseInt(tok.nextToken())));
		}
		Collections.sort(rlist, Recipe.sortByArrival());
		
		Scheduler sched;
		if(u!=null) sched = new Scheduler(u);
		else sched = new Scheduler(false, false, false, false, 0);
		
		if(out) System.out.println("Time, Stove, Ready, Assistant, Remarks");
		int idx = 0;
		int timer = 1;
		while(sched.getDone() != rlist.size()){
			if(idx < rlist.size())
			while(rlist.get(idx).tta == timer){
				Recipe r = rlist.get(idx);
				r.readRecipe();
				//System.out.println(r.name+"|");
				//for(int i = 0; i < r.tlist.size(); i++) System.out.println(r.tlist.get(i).toString());
				sched.arrive(r);
				idx++;
				if(idx >= rlist.size()) break;
			}
			sched.update();
			if(out) System.out.println(sched.out(timer));
			else sched.out(timer);
			sched.do_();
			timer++;
		}
		
		if(out){
			System.out.printf("\n\n,,Time Completed, %d\n", timer);
			System.out.printf(",,Stove Utilization, %d/%d = %.2f%%\n", sched.stove.stoveUtil, timer, (float)sched.stove.stoveUtil/(float)(timer)*100);
		}
		
		int sum = 0;
		int dishes = sched.dlist.size();
		int totalweight = 0;
		if(out) System.out.println(",,Job, Waiting time, Priority");
		for(int i=0; i<sched.dlist.size(); i++){
			Recipe temp = sched.dlist.get(i);
			if(out) System.out.printf(",,[%s], %d, %d\n", temp.name, temp.wait, temp.priority);
			sum += temp.wait * temp.priority;
			totalweight += temp.priority;
		}
		if(out) System.out.printf(",,Weighted average wait time, %.2f\n", (double)sum/(double)(totalweight * dishes));
		
		double avg = 0;
		for(int i = 1; i < sched.dlist.size(); i++){
			avg += sched.dlist.get(i).priority - sched.dlist.get(i-1).priority;
		}
		avg/=(double)sched.dlist.size()-1;
		if(out) System.out.printf(",,Weighted average priority difference, %.2f\n", avg);
		Stat stat = new Stat(timer, (float)sched.stove.stoveUtil/(float)(timer), (double)sum/(double)(totalweight * dishes), avg);
		return stat;
	}
	
}