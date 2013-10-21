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

public class ClassicGameSelectState  extends AppState {
	private ImageDrawer imgDrawer;
	private StateMachine stateMachine;
	private GameSelector gameSelector;
	private MediaPlayer mediaPlayer;
	private MainActivity mainActivity;
	private SoundHelper soundHelper;
	public int getState() {
	
		return StateMachine.CLASSIC_GAME_STATE;
	}
	public ClassicGameSelectState(ImageDrawer imgDrawer, MainActivity mainActivity) {
		this.soundHelper = mainActivity.getSoundHelper();
		this.mainActivity = mainActivity;
		this.imgDrawer = imgDrawer;
		this.stateMachine = mainActivity.getStateMachine();
		this.gameSelector = mainActivity.getGameSelector();
		this.mediaPlayer = mainActivity.getMediaPlayer();
		if(gameSelector.getGameModel(0).bestScore >10)
			level2locked=false;
		else
			level2locked=true;
		if(gameSelector.getGameModel(1).bestScore >10)
			level3locked=false;
		else
			level3locked=true;
	}
	
	int width;
	int height;
	public void setScreenSize(int _width, int _height) {
		width = _width;
		height = _height;
	}
	@Override
	public void draw() {
		imgDrawer.drawIMG(R.raw.wide_menu);
		buttonLevel1.draw();
		if(level2locked)
			buttonLevel2Locked.draw();
		else
			buttonLevel2.draw();
		
		if(level3locked)
			buttonLevel3Locked.draw();
		else
			buttonLevel3.draw();
		 
		backButton.draw();
	}
	private Button buttonLevel1 = new       Button(0.73f, 0.15f, 0.2f, 0.2f, R.raw.level1);
	private Button buttonLevel2 = new       Button(0.77f, 0.40f, 0.2f, 0.2f, R.raw.level2);
	private Button buttonLevel2Locked = new Button(0.77f, 0.40f, 0.2f, 0.2f, R.raw.level2locked);
	private Button buttonLevel3 = new       Button(0.73f, 0.65f, 0.2f, 0.2f, R.raw.level3);
	private Button buttonLevel3Locked = new Button(0.73f, 0.65f, 0.2f, 0.2f, R.raw.level3locked);
	
	private Button backButton = new Button(0.05f, 0.4f, 0.2f,0.2f, R.raw.back_button);
	
	
	public boolean level2locked;
	public boolean level3locked;
	@Override
	public void onClick(float x, float y) {
		
		//MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.theme1);
		
		if(buttonLevel1.checkIfClicked(x, y)) {
			stateMachine.setState(StateMachine.GAME_STATE);
			soundHelper.play(R.raw.click);
			//stateMachine.gameState.reset(); //TODO: zmienic na level1 , napisac gameModel dla Level 1
			gameSelector.setGameModel(0);
			stateMachine.gameState.reset();
		}
		if(buttonLevel2.checkIfClicked(x, y) && !level2locked) {
			stateMachine.setState(StateMachine.GAME_STATE);
			soundHelper.play(R.raw.click);
			//stateMachine.gameState.reset(); //TODO: zmienic na level1 , napisac gameModel dla Level 1
			gameSelector.setGameModel(1);
			stateMachine.gameState.reset();
		}
		if(buttonLevel3.checkIfClicked(x, y) && !level3locked) {
			soundHelper.play(R.raw.click);
			stateMachine.setState(StateMachine.GAME_STATE);	
			//stateMachine.gameState.reset(); //TODO: zmienic na level1 , napisac gameModel dla Level 1
			gameSelector.setGameModel(2);
			stateMachine.gameState.reset();
		}
		if( backButton.checkIfClicked(x, y)) {
			soundHelper.play(R.raw.click);
			stateMachine.setState(StateMachine.NOT_STARTED_STATE);
		}
		/*if(x>0.9*width && y<0.8*height)
			MainActivity.activity.finish();
		if(x < width/2) {
			gameSelector.setGameModel(0);
		} else
			gameSelector.setGameModel(1);
		mediaPlayer.start();
		*/
//			MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.theme1);
//			mediaPlayer.start();
		
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

