import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Recipe{
	
	String name;			//name
	int priority;			//priority
	int tta;				//time to arrive
	LinkedList<Task> tlist;//tasklist
	int task;				//current task index
	int tlen;				//number of task
	int wait;				//total time waited
	double points;
	
	public Recipe(String name, int tta){
		this.name = name;
		this.priority = -1;				//consider
		this.tta = tta;
		tlist = new LinkedList<Task>();
		task = 0;						//consider
		tlen = 0;
		wait = 0;						//consider
		points = 0;
	}
	
	public void readRecipe()throws Exception{
		String fname = name+".txt";
		BufferedReader in = new BufferedReader(new FileReader(fname));
		StringTokenizer tok = new StringTokenizer(in.readLine());
		this.name = tok.nextToken();
		this.priority = Integer.parseInt(tok.nextToken());
		while(true){
			String tmp = in.readLine();
			if(tmp == null) break;
			tok = new StringTokenizer(tmp);
			tlist.addLast(new Task(tok.nextToken(), Integer.parseInt(tok.nextToken()), this));
			tlen++;
		}
		//file
	}
	
	public boolean isDone(){
		for(int i = 0; i <  tlen; i++){
			if(!this.tlist.get(i).isDone()) return false;
		}
		return true;
	}
	
	public Task nextTask(){
		if(isDone()) return null;
		updateTask();
		return tlist.get(task);
	}
	
	public void updateTask(){
		if(!isDone()){
			if(tlist.get(task).isDone() && task < tlen){
				task++;
			}
		}
	}
	
	public void updatePoint(Unit m){
		points = priority*m.prm + (tlen-task)*m.tkm + wait*m.wtm + nextTask().trem*m.trm;
	}
	
	public static Comparator <Recipe> sortByArrival(){
		return new Comparator<Recipe>(){
			public int compare(Recipe other1, Recipe other2){
				return other1.tta - other2.tta;
			}
		};
	}
	
	public static Comparator <Recipe> sortByPoint(){
		return new Comparator <Recipe>(){
			public int compare(Recipe other1, Recipe other2){
				return (int)(other2.points*100 - other1.points*100);
			}
		};
	}
	
	public static Comparator <Recipe> sortByPrio(){
		return new Comparator<Recipe>(){
			public int compare(Recipe other1, Recipe other2){
				return other2.priority - other1.priority;
			}
		};
	}
	
	public static Comparator <Recipe> sortByTask(){
		return new Comparator<Recipe>(){
			public int compare(Recipe other1, Recipe other2){
				return other1.nextTask().trem - other2.nextTask().trem;
			}
		};
	}
}