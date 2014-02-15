import java.io.*;
import java.util.*;

public class Main{
	
	public static void main(String []args)throws Exception{
		double[] mul = new double[5];
		File file = new File("config.txt");
		String inp;
		
		if(file.isFile()) {
			try {
				BufferedReader cfin = new BufferedReader(new FileReader(file));
				inp = cfin.readLine();
				StringTokenizer tok = new StringTokenizer(inp);
				for(int i = 0; i < 5; i++) {
					mul[i] = Double.parseDouble(tok.nextToken());
					//System.out.println("!!!"+mul[i]);
				}					
				cfin.close();
			} catch(IOException e){}
			program(new Unit(mul[0], mul[1], mul[2], mul[3], mul[4]), true);
		}
		
		else {
			program(null, true);
		}
		//program(new Unit(), true);
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
		else sched = new Scheduler(false, false, false, true, 10);
		
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
		
		int sum = 0, sum1 = 0, sum2 = 0;
		int dishes = sched.dlist.size();
		int totalweight = 0;
		if(out) System.out.println(",,Job, Waiting time, Priority");
		for(int i=0; i<sched.dlist.size(); i++){
			Recipe temp = sched.dlist.get(i);
			if(out) System.out.printf(",,[%s], %d, %d\n", temp.name, temp.wait, temp.priority);
			sum += temp.wait * temp.priority;
			sum1 += temp.wait;
			sum2 += temp.priority;
			totalweight += temp.priority;
		}
		if(out) System.out.printf(",,Total:, %d,Avg. Waiting Time %.2f, Weighted average wait time, %.2f\n", sum1, (double)sum1/(double)sched.dlist.size(), (double)sum/(double)(totalweight * dishes));
		
		double avg = 0;
		for(int i = 1; i < sched.dlist.size(); i++){
			avg += sched.dlist.get(i).priority - sched.dlist.get(i-1).priority;
		}
		avg/=(double)sched.dlist.size()-1;
		if(out) System.out.printf(",,Weighted average priority difference, %.2f\n", avg);
		Stat stat = new Stat(timer, (float)sched.stove.stoveUtil/(float)(timer), (double)sum1/(double)sched.dlist.size(), avg);
		if(out) System.out.println(",,Fitness,"+String.format("%.2f", stat.fitness()));
		return stat;
	}
	
}
