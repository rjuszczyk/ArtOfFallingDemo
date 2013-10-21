package com.swbgames.states;

public abstract class AppState {
	public abstract void draw();
	public abstract void onClick(float x, float y);
	public abstract int getState();
	public void onStart() {
		
	}
	public void onEnd() {
		
	}
}
