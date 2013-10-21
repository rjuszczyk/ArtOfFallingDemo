package com.swbgames.states;

import com.swbgames.artoffalling.main.GameModel;
import com.swbgames.artoffalling.main.ImageDrawer;
import com.swbgames.artoffalling.main.MainActivity;
import com.swbgames.sounds.SoundHelper;


import com.swbgames.R;

public class LoseState extends AppState {

	private ImageDrawer imgDrawer;
	private StateMachine stateMachine;
	private MainActivity mainActivity;
	private SoundHelper soundHelper;
	public int getState() {
		return StateMachine.LOSE_STATE;
	}
	public LoseState(ImageDrawer imgDrawer, StateMachine stateMachine, MainActivity mainActivity_) {
		this.soundHelper = mainActivity_.getSoundHelper();
		this.mainActivity = mainActivity_;
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
		int score = this.mainActivity.getGameSelector().getGameModel().score;
		int bestScore = this.mainActivity.getGameSelector().getGameModel().getBestScore();
		
		imgDrawer.drawIMG(R.raw.you_lose);
		
		//if(bestScore == score)
		imgDrawer.drawIMG(0.2f*width, 0.2f*height, 5.72f*40, 40.0f, R.raw.you_lose_text);
		if(bestScore==score) {
			imgDrawer.drawIMG(0.07f*width, 0.4f*height, 8.f*40, 40.0f, R.raw.new_record);
			imgDrawer.drawNumber((int)(0.07f*width+8.f*40+10), (int)(0.4*height), 40, score);
		}
	}

	@Override
	public void onClick(float x, float y) {
		soundHelper.play(R.raw.click);
		stateMachine.currentAppState = stateMachine.notStartedState;	
		//gameModel.setStop(false);
	}
}
