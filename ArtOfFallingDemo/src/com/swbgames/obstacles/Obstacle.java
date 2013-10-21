package com.swbgames.obstacles;

import com.swbgames.artoffalling.main.Mesh;

public interface Obstacle {
	public boolean checkHited(float x, float y);
	public Mesh getMesh();
	public float getRotation();
}
