package com.swbgames.states;


import com.swbgames.artoffalling.main.GameModel;
import com.swbgames.artoffalling.main.ImageDrawer;
import com.swbgames.artoffalling.main.MainActivity;
import com.swbgames.sounds.SoundHelper;


import com.swbgames.R;

public class PauseState extends AppState {

	private ImageDrawer imgDrawer;
	private StateMachine stateMachine;
	private SoundHelper soundHelper;
	public int getState() {
		return StateMachine.PAUSE_STATE;
	}
	public PauseState(ImageDrawer imgDrawer , MainActivity mainActivity) {
		this.imgDrawer = imgDrawer;
		this.stateMachine = mainActivity.getStateMachine();
		this.soundHelper = mainActivity.getSoundHelper();
	}
	
	int width;
	int height;
	public void setScreenSize(int _width, int _height) {
		width = _width;
		height = _height;
	}
	@Override
	public void draw() {
		imgDrawer.drawIMG(0, 0, width, height, R.raw.wide_pause);
		backButton.draw();
		resumeButton.draw();
	}
	private Button backButton = new Button(0.05f, 0.4f, 0.2f,0.2f, R.raw.back_button);
	private Button resumeButton = new Button(0.75f, 0.4f, 0.2f,0.2f, R.raw.resume_button);
	@Override
	public void onClick(float x, float y) {
		if( backButton.checkIfClicked(x, y)) {
			soundHelper.play(R.raw.click);
			stateMachine.gameState.reset();
			stateMachine.setState(StateMachine.CLASSIC_GAME_STATE);
			return;
		}
		if(resumeButton.checkIfClicked(x, y)) {
			stateMachine.setState(StateMachine.GAME_STATE);
			soundHelper.play(R.raw.click);
			stateMachine.gameState.loadGameData();
		}
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
