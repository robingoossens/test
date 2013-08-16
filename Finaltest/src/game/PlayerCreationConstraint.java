package game;

import grid.EffectGenerator;

import java.util.ArrayList;
import java.util.HashSet;

import javax.naming.directory.InvalidAttributesException;

import square.Effect;
import square.EffectType;
import square.OnSquare;
import square.OnSquareType;
import square.Player;

public class PlayerCreationConstraint extends CreationConstraint {
	String[] names;
	int cap;
	int index;
	public PlayerCreationConstraint(String[] name, int cap){
		this.names= new String[name.length];
		for(int i=0;i<name.length;i++)
			this.names[i] = name[i];
		this.cap = cap;
		this.index = 1;
		setType(OnSquareType.PLAYER);
	}
	
	@Override
	public OnSquare getOnSquare(HashSet<EffectType> effectTypes) {
		ArrayList<Effect> out = new  ArrayList<Effect>();
		for(EffectType et:effectTypes)
			out.add(EffectGenerator.getInstance().makeEffect(et));
		Player p = null;
		try {
			String temp = names[index-1];
			p = new Player(temp,cap,index++,out);
		} catch (InvalidAttributesException e) {
			e.printStackTrace();
		}
		return p;
	}

}
