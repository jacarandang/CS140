
import java.util.*;
public class Scheduler{

	Assistant asst;
	Stove stove;
	LinkedList<Recipe> rlist;
	LinkedList<Recipe> dlist;
	LinkedList<String> remarks;
	Unit u;
	
	int timequantum, interval;
	boolean roundrobin;
	boolean preemption;
	boolean shortest;
	boolean aging;
	boolean custom;
	
	public Scheduler(boolean shortest, boolean preemption, boolean aging, boolean roundrobin, int timequantum){
		asst = new Assistant(this);
		stove = new Stove(this);
		rlist = new LinkedList<Recipe>();
		dlist = new LinkedList<Recipe>();
		remarks = new LinkedList<String>();

		interval = 0;
		this.shortest = shortest;
		this.preemption = preemption;
		this.roundrobin = roundrobin;
		this.timequantum = timequantum;
		this.aging = aging;
		custom = false;
		u = null;
	}
	
	public Scheduler(Unit u){
		this(false, false, false, false, 0);
		custom = true;
		this.u = u;
	}
	
	public void arrive(Recipe r){
		remarks.addLast(r.name+" arrives");
		if(r.nextTask().step == 1) asst.tlist.addLast(r.nextTask());
		else rlist.addLast(r);
	}
	
	public void update(){
		if(preemption==true)
			Collections.sort(rlist, Recipe.sortByPrio());
		else if(shortest == true)
			Collections.sort(rlist, Recipe.sortByTask());
		
		if(custom){
			for(int i = 0; i < rlist.size(); i++) rlist.get(i).updatePoint(u);
			Collections.sort(rlist, Recipe.sortByPoint());
		}
		
		boolean flag = true;
		
		if(stove.t!=null && roundrobin==true){
			interval++;
			if(interval==timequantum){
				interval = 0;
				rlist.add(stove.t.owner);
				stove.clean();
				flag = false;
			}
		}
		for(int i = 0; i < rlist.size(); i++){
			Recipe r = rlist.get(i);
			if(r.nextTask().step == 1){
				rlist.remove(r);
				asst.tlist.add(r.nextTask());
			}
			else if(flag && r.nextTask().step == 0 && stove.t == null && !stove.cleaning){
				rlist.remove(r);
				stove.t = r.nextTask();
			}
			else if(r.nextTask().step == 0 && stove.t != null){
				if(preemption==true){
					if(r.priority > stove.t.owner.priority){
						rlist.add(stove.t.owner);
						remarks.add(stove.t.owner.name + " was pre-empted");
						stove.clean();
						stove.next = r.nextTask();
						flag = false;
					}
					if(aging)r.priority++;
					r.wait++;
				}
				else{
					if(aging)r.priority++;
					r.wait++;
				}
			}
			else if (flag==false || stove.cleaning){
				if(aging)r.priority++;
				r.wait++;
			}
		}
		
		if(preemption==true)
			Collections.sort(rlist, Recipe.sortByPrio());
		
	}
	
	public void do_(){
		stove.update();
		asst.update();
	}
	
	public void alert(Task t){
		if(t.step == 0){	//from stove
			Recipe r = t.owner;
			r.updateTask();
			if(r.isDone()){
				dlist.addLast(r);
			}
			else{
				rlist.addLast(r);
			}
			stove.clean();
			if(r.isDone()) remarks.addLast(r.name+" is done");
			else remarks.addLast(r.name+" is done cooking");
			remarks.addLast("stove is cleaning");
		}
		else{
			Recipe r = t.owner;
			r.updateTask();
			if(r.isDone()){
				dlist.addLast(r);
			}
			else{
				rlist.addLast(r);
			}
			asst.tlist.remove(t);
			if(r.isDone()) remarks.addLast(r.name + " is done");
			else remarks.addLast(r.name + " is prepared");
		}
	}
	
	public int getDone(){
		return dlist.size();
	}

	public String out(int minute){
		String s = "";
		s+= minute+" ,";
		if(stove.t!=null){
			s += stove.t.owner.name+ "(cook=" +stove.t.trem+")";
		}
		else if(stove.cleaning){
			s += "Cleaning";
		}
		else{
			s += "Empty";
		}
		
		s += ",";
		if(rlist.size()>0)
			for(int i = 0; i < rlist.size(); i++){
				Recipe temp = rlist.get(i);
				if(temp.nextTask().step == 0)s += " " + temp.name+ "(cook=" + temp.nextTask().trem+")";
				else s += " " + temp.name+ "(prep=" + temp.nextTask().trem+")";
			}		
		else
			s += "none";
		
		s += ",";
		if(asst.tlist.size()>0)
			for(int i = 0; i < asst.tlist.size(); i++){
				s += " " + asst.tlist.get(i).owner.name+ "(prep=" +asst.tlist.get(i).trem+")";
			}
		else 
			s += "none";
			
		s += ",";
		if(remarks.isEmpty())
			s += "none";
		while(!remarks.isEmpty()){
			s += remarks.pop()+"|";
		}
		return s;
	}
}