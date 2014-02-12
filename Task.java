
public class Task{
	
	int step, time, trem;
	Recipe owner;
	
	public Task(String step, int time, Recipe owner){
		if(step.toLowerCase().equals("cook")) this.step = 0;
		else this.step = 1;
		this.time = time;
		this.trem = time;
		this.owner = owner;
	}
	
	public void do_(){
		if(trem>0){
			trem--;
		}
	}
	
	public boolean isDone(){
		return trem == 0;
	}
	
	public String toString(){
		return step+" "+time+" "+trem;
	}
}