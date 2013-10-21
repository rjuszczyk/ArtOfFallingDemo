package com.swbgames.obstacles;

import com.swbgames.artoffalling.main.Mesh;


public class BonusItem implements Obstacle{
	public float x;
	public float y;
	public boolean noBonusItem;
	public boolean picked = false;
	public static float r = 0.43f;
	
	public int type;
	
	public BonusItem(float x, float y) {
		this(x, y, false);
	}
	public BonusItem(float x, float y, boolean noBonusItem) {
		this.x=x;
		this.y=y;
		this.noBonusItem = noBonusItem;
		this.type = (int)(Math.random()*4);
	}
	public void pick() {
		picked = true;
	}
	@Override
	public boolean checkHited(float x2, float y2) {
	
		float d = (float) Math.sqrt(Math.pow(x2-x, 2.0)+Math.pow(y2-y,2.0));
		
		if(d>r)
			return false;
		else
			return true;
	}
	@Override
	public Mesh getMesh() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public float getRotation() {
		// TODO Auto-generated method stub
		return 0;
	}
}
