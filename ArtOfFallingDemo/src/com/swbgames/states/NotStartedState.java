package com.swbgames.states;

import com.swbgames.artoffalling.main.GameModel;
import com.swbgames.artoffalling.main.GameSelector;
import com.swbgames.artoffalling.main.ImageDrawer;
import com.swbgames.artoffalling.main.MainActivity;
import com.swbgames.sounds.SoundHelper;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.swbgames.R;

public class NotStartedState extends AppState {
	private ImageDrawer imgDrawer;
	private StateMachine stateMachine;
	private GameSelector gameSelector;
	private MediaPlayer mediaPlayer;
	private MainActivity mainActivity;
	private SoundHelper soundHelper;
	
	public int getState() {
		return StateMachine.NOT_STARTED_STATE;
	}
	public NotStartedState(ImageDrawer imgDrawer, MainActivity mainActivity) {
		this.soundHelper = mainActivity.getSoundHelper();
		this.mainActivity = mainActivity;
		this.imgDrawer = imgDrawer;
		this.stateMachine = mainActivity.getStateMachine();
		this.gameSelector = mainActivity.getGameSelector();
		this.mediaPlayer = mainActivity.getMediaPlayer();
		
	}
	
	int width;
	int height;
	public void setScreenSize(int _width, int _height) {
		width = _width;
		height = _height;
	}
	@Override
	public void draw() {
		
		
		imgDrawer.drawIMG(R.raw.start_game);
		
		buttonStart.draw();
		buttonExit.draw();
		
	}
	private Button buttonStart = new Button(0.75f, 0.25f, 0.2f, 0.2f, R.raw.start_button);
	private Button buttonExit = new Button(0.75f, 0.55f, 0.2f, 0.2f, R.raw.exit_button);
	
	@Override
	public void onClick(float x, float y) {
		
		//MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.theme1);
		
		if(buttonStart.checkIfClicked(x, y)) {
			soundHelper.play(R.raw.click);
			//stateMachine.setState(StateMachine.CLASSIC_GAME_STATE);
			stateMachine.currentAppState = stateMachine.classicGameSelectState;
			//stateMachine.currentAppState = stateMachine.gameState;	
			//stateMachine.gameState.reset();
			//gameSelector.setGameModel(0);
			//mediaPlayer.start();
		}
		if(buttonExit.checkIfClicked(x, y)) {
			soundHelper.play(R.raw.click);
			mainActivity.finish();
		}
		/*if(x>0.9*width && y<0.8*height)
			MainActivity.activity.finish();
		if(x < width/2) {
			gameSelector.setGameModel(0);
		} else
			gameSelector.setGameModel(1);
		mediaPlayer.start();
		*/
//		MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.theme1);
//		mediaPlayer.start();
		
		//music/05_-_Two_Shoes
		//soundHelper.play(R.raw.theme1);
		//gameModel.setStop(false);
	}
	private class Button {
		float xs;
		float ys;
		float w;
		float h;
		int resImg;
		public Button(float xs, float ys, float w, float h, int resImg) {
			this.xs=xs;
			this.ys=ys;
			this.w=w;
			this.h=h;
			this.resImg=resImg; 
		}
		public void draw() {
			imgDrawer.drawIMG((xs*width), (ys*height), w*width, h*height, resImg);
		}
		public boolean checkIfClicked(float x, float y) {
			
			return x>=xs*width && x<=(xs+w)*width && y>=ys*height && y<=(ys+h)*height;		
		}
	}
}
