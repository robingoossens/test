package grid;

import java.util.ArrayList;

import square.OnSquare;

public abstract class PuttingPolicy {
	private int number;
	
	public PuttingPolicy(int number){
		this.number = number;
	}
	
	public int getNumber(){
		return number;
	}
	
	protected void setNumber(int n){
		number = n;
	}
	
	public abstract void put(Grid grid);
}
