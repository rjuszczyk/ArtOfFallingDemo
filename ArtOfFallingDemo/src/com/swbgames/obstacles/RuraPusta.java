package com.swbgames.obstacles;

import com.swbgames.artoffalling.main.Mesh;

import android.opengl.Matrix;



public class RuraPusta implements Obstacle{
	Mesh mesh;
	private float rotation;
	
	public RuraPusta(Mesh m, float rotation) {
		this.mesh = m;
		this.rotation = rotation;
	}
	
	@Override
	public boolean checkHited(float x, float y) {
		return false;
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

