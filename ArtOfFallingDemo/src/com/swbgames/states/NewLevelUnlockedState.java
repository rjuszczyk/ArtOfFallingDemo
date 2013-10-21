package com.swbgames.states;

import com.swbgames.artoffalling.main.GameModel;
import com.swbgames.artoffalling.main.ImageDrawer;

import com.swbgames.R;

public class NewLevelUnlockedState extends AppState {

	private ImageDrawer imgDrawer;
	private StateMachine stateMachine;
	public int getState() {
		return StateMachine.NEW_LEVEL_UNLOCKED;
	}
	public NewLevelUnlockedState(ImageDrawer imgDrawer, StateMachine stateMachine) {
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
		imgDrawer.drawIMG(R.raw.you_lose);
		imgDrawer.drawIMG(0.2f*width, 0.5f*height, 5.1f*40, 80.0f, R.raw.new_level_unlocked);
	}

	@Override
	public void onClick(float x, float y) {
		stateMachine.setState(StateMachine.CLASSIC_GAME_STATE);	
		//gameModel.setStop(false);
	}
}

