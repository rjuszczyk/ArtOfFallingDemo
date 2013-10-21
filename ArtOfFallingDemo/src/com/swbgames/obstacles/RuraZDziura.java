package com.swbgames.obstacles;

import com.swbgames.artoffalling.main.Mesh;

import android.opengl.Matrix;



public class RuraZDziura implements Obstacle{
	Mesh mesh;
	private float rotation;
	
	public RuraZDziura(Mesh m, float rotation) {
		this.mesh = m;
		this.rotation = rotation;
	}
	
	@Override
	public boolean checkHited(float x, float y) {
		float r = (float) Math.sqrt(x*x+y*y);
		
		if(r<1.77f)
			return false;
		else 
			return true;
	}

	@Override
	public Mesh getMesh() {
		return mesh;
	}

	@Override
	public float getRotation() {
		return rotation;
	}

}

