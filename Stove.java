public class Stove{
	
	Task t;				//task working on
	boolean cleaning;	//is cleaning?
	int ttc;			//time to clean
	Task next;			//next task
	Scheduler sched;	//scheduler
	int stoveUtil;
	
	public Stove(Scheduler s){
		t = null;
		cleaning = false;
		ttc = 1;
		next = null;
		sched = s;
		stoveUtil = 0;
	}

	public void update(){
		if(cleaning){
			ttc--;
			if(ttc == 0){
				cleaning = false;
				ttc = 1;
				if(next != null){
					this.t = next;
					next = null;
				}
			}
		}
		else{
			if(t != null){
				t.do_();
				stoveUtil++;
				
				if(t.isDone()){
					sched.alert(t);
				}
			}
		}
	}
	
	public void clean(){
		sched.interval = 0;
		t = null;
		cleaning = true;
	}
}