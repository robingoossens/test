package game;

import exceptions.IllegalDirectionException;
import exceptions.IllegalSizeException;
import grid.Direction;
import grid.EffectGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.naming.directory.InvalidAttributesException;

import square.Effect;
import square.EffectType;
import square.OnSquare;
import square.OnSquareType;
import square.Wall;

public class WallCreationConstraint extends CreationConstraint{
	private int maxX;
	private int maxY;
	private double percentage;
	public WallCreationConstraint(int x, int y, double percentage){
		maxX = x;
		maxY = y;
		this.percentage = percentage;
		setType(OnSquareType.WALL);
	}
	
	@Override
	public OnSquare getOnSquare(HashSet<EffectType> effectTypes) {
		Random rand = new Random();
		Direction d;
		int maxsize;
		if(rand.nextInt(2)==1){
			d = Direction.NORTH;
			maxsize = (int) Math.ceil(maxY*percentage);
		}
		else{
			d = Direction.SOUTH;
			maxsize = (int) Math.ceil(maxX*percentage);
		}
		if(maxsize>2)
			maxsize = rand.nextInt(maxsize-2)+2;
		else
			maxsize=2;
		Wall wall = null;
		try {
			ArrayList<Effect> f = new ArrayList<Effect>();
			for(EffectType ef:effectTypes)
				f.add(EffectGenerator.getInstance().makeEffect(ef));
			wall = new Wall(d,maxsize,f);
		} catch (IllegalSizeException | IllegalDirectionException e) {
			e.printStackTrace();
		} catch (InvalidAttributesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wall;
	}

}
