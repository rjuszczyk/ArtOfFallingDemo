package com.swbgames.states;

import com.swbgames.artoffalling.main.ImageDrawer;

import com.swbgames.R;

public class WinnerState extends AppState {

	private ImageDrawer imgDrawer;
	private StateMachine stateMachine;
	public int getState() {
		return StateMachine.WINNER_STATE;
	}
	public WinnerState(ImageDrawer imgDrawer, StateMachine stateMachine) {
		this.imgDrawer = imgDrawer;
		this.stateMachine = stateMachine;
	}
	
	int width;
	int height;
	public void setScreenSize(int _width, int _height) {
		width = _width;
		height = _height;
	}
	@Override
	public void draw() {
		imgDrawer.drawIMG(0, 0, width, height, R.raw.theme_bg);
		imgDrawer.drawIMG(0.2f*width, 0.4f*height, 5.5f*40, 40.0f, R.raw.you_win_text);
	}

	@Override
	public void onClick(float x, float y) {
		stateMachine.currentAppState = stateMachine.notStartedState;	
		//gameModel.setStop(false);
	}
}
