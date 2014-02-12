import java.util.*;

public class Assistant{

	LinkedList<Task> tlist;
	Scheduler sched;
	
	public Assistant(Scheduler s){
		tlist = new LinkedList<Task>();
		sched = s;
	}
	
	public void update(){
		for(int i = 0 ; i < tlist.size(); i++){
			Task t = tlist.get(i);
			t.do_();
			if(t.isDone()){
				sched.alert(t);
			}
		}
	}
	

}