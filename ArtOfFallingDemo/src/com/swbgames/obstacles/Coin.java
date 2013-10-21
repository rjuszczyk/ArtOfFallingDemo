package com.swbgames.obstacles;

import com.swbgames.artoffalling.main.Mesh;

import android.util.Log;


public class Coin implements Obstacle{
	public float x;
	public float y;
	public boolean noCoin;
	public boolean picked = false;
	public static float r = 0.43f;
	public Coin(float x, float y) {
		this(x, y, false);
	}
	public Coin(float x, float y, boolean noCoin) {
		this.x=x;
		this.y=y;
		this.noCoin = noCoin;
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
