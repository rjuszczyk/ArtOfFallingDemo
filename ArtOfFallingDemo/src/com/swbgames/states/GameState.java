package com.swbgames.states;

import java.nio.FloatBuffer;
import java.util.LinkedList;

import com.swbgames.artoffalling.main.Bonusy;
import com.swbgames.artoffalling.main.GameModel;
import com.swbgames.artoffalling.main.GameSelector;
import com.swbgames.artoffalling.main.ImageDrawer;
import com.swbgames.artoffalling.main.MainActivity;
import com.swbgames.artoffalling.main.Mesh;
import com.swbgames.obstacles.BonusItem;
import com.swbgames.obstacles.Coin;
import com.swbgames.obstacles.Obstacle;
import com.swbgames.obstacles.SlowItem;
import com.swbgames.sounds.SoundHelper;



import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.swbgames.R;

public class GameState extends AppState {
	SoundHelper soundHelper;
	GameSelector gameSelector;
	ImageDrawer imgDrawer;
	Mesh coinMesh;
	Mesh slowMesh;
	
	Mesh bonusBlueMesh;
	Mesh bonusRedMesh;
	Mesh bonusGreenMesh;
	Mesh bonusSpecialMesh;
	
	
	StateMachine stateMachine;
	MediaPlayer mediaPlayer;
	
	int mPerVertexProgramHandle; //in constructor!
	public int getState() {
		return StateMachine.GAME_STATE;
	}
	long startZTime; //in constructor!
	
	public void setProgramHandle(int mPerVertexProgramHandle_) {
		this.mPerVertexProgramHandle = mPerVertexProgramHandle_;
	}
	@Override
	public void onStart() {
		if(!mediaPlayer.isPlaying())
		mediaPlayer.start();
	}
	@Override
	public void onEnd() {
		if(mediaPlayer.isPlaying())
			mediaPlayer.pause();
	}
	public GameState(
			int mPerVertexProgramHandle_,
			SoundHelper soundHelper_,
			GameSelector gameSelector_,
			ImageDrawer imgDrawer_,
			MediaPlayer mediaPlayer_,
			Mesh coinMesh_,
			Mesh slowMesh_,
			Mesh _bonusBlueMesh,
			Mesh _bonusRedMesh,
			Mesh _bonusGreenMesh,
			Mesh _bonusSpecialMesh,
			
			StateMachine stateMachine_
			) {
		this.mediaPlayer = mediaPlayer_;
		this.stateMachine = stateMachine_; 
		this.mPerVertexProgramHandle = mPerVertexProgramHandle_;
		this.soundHelper = soundHelper_;
		this.gameSelector = gameSelector_;
		this.imgDrawer = imgDrawer_;
		this.coinMesh = coinMesh_;
		this.slowMesh = slowMesh_;
		this.bonusBlueMesh = _bonusBlueMesh;
		this.bonusRedMesh  = _bonusRedMesh;
		this.bonusGreenMesh = _bonusGreenMesh;
		this.bonusSpecialMesh =_bonusSpecialMesh;
		
		startZTime = SystemClock.uptimeMillis();
	}
	
	private final int mPositionDataSize = 3;		
	private final int mNormalDataSize = 3;
	private final int mUVDataSize = 2;
	
	private int mMVPMatrixHandle;
	private int mMVMatrixHandle;
	private int mLightPosHandle;
	private int mPositionHandle;
	private int mColorHandle;
	private int mNormalHandle;

	private int mTextureUniformHandle;
	private int mTextureCoordinateHandle;
	
	
	
	public long lastTime = 0;
	
	private float[] mModelMatrix = new float[16];
	private float[] mViewMatrix = new float[16];
	//private float[] mProjectionMatrix = new float[16];
	private float[] mMVPMatrix = new float[16];
	private float[] mLightModelMatrix = new float[16];	
	
	private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
	private final float[] mLightPosInWorldSpace = new float[4];
	private final float[] mLightPosInEyeSpace = new float[4];
	
	private float[] mProjectionMatrix;
	public void setProjectionMatrix(float[] _mProjectionMatrix) {
		mProjectionMatrix = _mProjectionMatrix;
	}
	private void onLose() {
		gameSelector.getGameModel().onLose();
		gameSelector.getGameModel().setBestScore(gameSelector.getGameModel().score);
		
		//stateMachine.setState(StateMachine.LOSE_STATE);
    	//zmiana stanu
		 //stateMachine.currentAppState = stateMachine.loseState;
	}
	public void reset() {
		startZTime = SystemClock.uptimeMillis();
		gameSelector.getGameModel().reset();
		lastTime = 0;
	}
	boolean saved = false;
	Bundle savedInstanceState = new Bundle();
	public void saveGameData() {
		saved=true;
		gameSelector.getGameModel().saveGame(this.savedInstanceState);
	}
	public void saveGameData(Bundle _savedInstanceState) {
		
		gameSelector.getGameModel().saveGame(_savedInstanceState);
	}
	long zTime;
	public void loadGameData() {
		if(!saved)return;
		gameSelector.getGameModel().restoreGame(this.savedInstanceState);
		lastTime = SystemClock.uptimeMillis();
		startZTime = SystemClock.uptimeMillis() - zTime;		
	}
	public void loadGameData(Bundle _savedInstanceState) {
		gameSelector.getGameModel().restoreGame(_savedInstanceState);
		lastTime = SystemClock.uptimeMillis();
		startZTime = SystemClock.uptimeMillis() - zTime;		
	}
	
	@Override
	/**
	 * pamiêtaj o: setProjectionMatrix(...)
	 */
	public void draw() {
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
		
		
		if(lastTime==0){lastTime = SystemClock.uptimeMillis(); return;}
			
		long deltaTime = SystemClock.uptimeMillis() - lastTime;
		lastTime +=deltaTime;
		
		long currMillis = SystemClock.uptimeMillis();
		zTime = currMillis - startZTime;
		//float speed = 0.005f;
		if(zTime>gameSelector.getGameModel().nextSpeedUP) {
			startZTime = currMillis;
			gameSelector.getGameModel().nextSpeedUP*=1.1;
			gameSelector.getGameModel().speed+=0.0005f;
		}
		float zpos = gameSelector.getGameModel().getZPos();
		gameSelector.getGameModel().setZPos(zpos+deltaTime*gameSelector.getGameModel().speed);
		
		// Position the eye in front of the origin.
		final float eyeX = gameSelector.getGameModel().getXPos();
		final float eyeY = gameSelector.getGameModel().getYPos();
		final float eyeZ = 1.5f - gameSelector.getGameModel().getZPos();

		// We are looking toward the distance
		final float lookX = gameSelector.getGameModel().getXPos();
		final float lookY = gameSelector.getGameModel().getYPos();
		final float lookZ = -5.0f - gameSelector.getGameModel().getZPos();

		// Set our up vector. This is where our head would be pointing were we holding the camera.
		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;

		// Set the view matrix. This matrix can be said to represent the camera position.
		// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
		// view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);	
		
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);			        
                
         
        // Set our per-vertex lighting program.
        GLES20.glUseProgram(mPerVertexProgramHandle);
        
        // Set program handles for cube drawing.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVMatrix"); 
        mLightPosHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_LightPos");
        mPositionHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Position");
        mColorHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_Color");
        mNormalHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Normal"); 
        
        mTextureUniformHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_Texture");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_TexCoordinate");
     
       
        
        // Calculate position of the light. Rotate and then push into the distance.
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f,  1.0f-gameSelector.getGameModel().getZPos());      
        //Matrix.rotateM(mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
       // Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);
             
        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);                        
        
          
        int first = (int) (gameSelector.getGameModel().getZPos() / 5.988f);
        first = first - 2;
        if(first<0)first=0;
        int last = first+20;
        if(last > gameSelector.getGameModel().getObstaclesCount())
        	last = gameSelector.getGameModel().getObstaclesCount();
        for(int i = first; i < last; i++) {
        	Matrix.setIdentityM(mModelMatrix, 0);
	        //Matrix.rotateM(mModelMatrix, 0, -90, 1.0f, 0.0f, 0.0f); 
        	Obstacle obstacle = gameSelector.getGameModel().getObstacleAt(i);
	        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -7.0f-5.988f*(i));
	        Matrix.rotateM(mModelMatrix, 0, obstacle.getRotation(), 0.0f, 0.0f, -1.0f);
	        drawMesh(obstacle.getMesh(),mProjectionMatrix); 
	        
	        
	        Coin coin = gameSelector.getGameModel().getCoinAt(i);
	        SlowItem slowItem = gameSelector.getGameModel().getSlowItemAt(i);
	        BonusItem bonusItem = gameSelector.getGameModel().getBonusAt(i);
	        
	        if(!coin.noCoin && !coin.picked){
	        	Matrix.setIdentityM(mModelMatrix, 0);
		        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -3f-5.988f*(i));
		        
		        Matrix.translateM(mModelMatrix, 0, coin.x, coin.y, 0.0f);
		        Matrix.rotateM(mModelMatrix, 0, SystemClock.uptimeMillis()/4, 1, 0, 0);
		        drawMesh(coinMesh,mProjectionMatrix);
	        }
	        if(!slowItem.noCoin && !slowItem.picked){
	        	Matrix.setIdentityM(mModelMatrix, 0);
		        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -3f-5.988f*(i));
		        
		        Matrix.translateM(mModelMatrix, 0, slowItem.x, slowItem.y, 0.0f);
		        Matrix.rotateM(mModelMatrix, 0, SystemClock.uptimeMillis()/4, 1, 1, 0);
		        drawMesh(slowMesh,mProjectionMatrix);
	        }
	        if(!bonusItem.noBonusItem && !bonusItem.picked){
	        	Matrix.setIdentityM(mModelMatrix, 0);
		        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -3f-5.988f*(i));
		        
		        Matrix.translateM(mModelMatrix, 0, bonusItem.x, bonusItem.y, 0.0f);
		        Matrix.rotateM(mModelMatrix, 0, SystemClock.uptimeMillis()/4, 1, 1, 0);
		        
		        switch(bonusItem.type){
		        case Bonusy.BLUE:
		        	drawMesh(bonusBlueMesh,mProjectionMatrix);
		        	break;
		        case Bonusy.RED:
		        	drawMesh(bonusRedMesh,mProjectionMatrix);
		        	break;
		        case Bonusy.GREEN:
		        	drawMesh(bonusGreenMesh,mProjectionMatrix);
		        	break;
		        case Bonusy.SPECIAL:
		        	drawMesh(bonusSpecialMesh,mProjectionMatrix);
		        	break;
		        default:
		        	drawMesh(bonusBlueMesh,mProjectionMatrix);
		        	break;
		        }
	        }
        }
        if(gameSelector.getGameModel().test()) {
        	soundHelper.play(R.raw.broken_glass);
        	gameSelector.getGameModel().speed = GameModel.default_speed;
        	gameSelector.getGameModel().nextSpeedUP = 1000;
        	
        	if(!gameSelector.getGameModel().smierc())
        		this.onLose();
        }
        	
        if(gameSelector.getGameModel().testPickCoin()) {
        	soundHelper.play(R.raw.ding);
        	gameSelector.getGameModel().incScore(1);
        	if(gameSelector.getGameModel().score % 100 == 0)
        		gameSelector.getGameModel().iloscZyc++;
        }
        
        if(gameSelector.getGameModel().testPickSlowItem()) {
        	soundHelper.play(R.raw.bonus_collect);
        	gameSelector.getGameModel().speed = gameSelector.getGameModel().speed*0.75f;
        	gameSelector.getGameModel().nextSpeedUP = 550;
        }
        if(gameSelector.getGameModel().testPickBonusItem()) {
        	soundHelper.play(R.raw.ding);
        	this.gameSelector.getGameModel().bonusy.collectBonus(this.gameSelector.getGameModel().lastBonusPicked);
        }
        
        
        
        
        gameSelector.getGameModel().checkResetIncPointsFactor();
        
    	Log.i("score","x="+gameSelector.getGameModel().getBestScore());
        drawScore();
        imgDrawer.drawBestScore(gameSelector.getGameModel().getBestScore());
        pauseButton.draw();
        drawLives(gameSelector.getGameModel().iloscZyc);
        drawBonuses(this.gameSelector.getGameModel().lastBonusPicked,this.gameSelector.getGameModel().bonusy.getCount());
	}
	private Button pauseButton;// = new Button(0.9f, 0.9f, 0.1f, 0.1f*(height/width), R.raw._0);
	@Override
	public void onClick(float x, float y) {
		if(pauseButton.checkIfClicked(x, y)) {
			soundHelper.play(R.raw.click);
			stateMachine.setState(StateMachine.PAUSE_STATE);
			stateMachine.gameState.saveGameData();
		}
	}
	private void drawLives(int lives) {
		for(int i =0; i< lives; i++) {
			imgDrawer.drawIMG(100+i*42, 5, 40, 40, R.raw.serce);
		}
	}
	
	private void drawBonuses(int type, int count) {
		
		int score = gameSelector.getGameModel().score;
		
		
		
		int total_pos_x = 300;
		
		
		int offset = 0;
		while(count>0) {
			
			--count;
			int off=offset*60;
			offset++;
			switch(type) {
			case 	Bonusy.RED: imgDrawer.drawIMG(total_pos_x + off, 5, 40, 40, R.raw.red); break;
			case 	Bonusy.BLUE: imgDrawer.drawIMG(total_pos_x + off, 5, 40, 40, R.raw.blue); break;
			case 	Bonusy.GREEN: imgDrawer.drawIMG(total_pos_x + off, 5, 40, 40, R.raw.green); break;
			case 	Bonusy.SPECIAL: imgDrawer.drawIMG(total_pos_x + off, 5, 40, 40, R.raw.special); break;
		
				default: break;
			}	
		}
	}
	
	private void drawScore() {
		LinkedList<Integer> heap = new LinkedList<Integer>();
		int score = gameSelector.getGameModel().score;
		
		int numWidth=29;
		int numHeight=40;
		
		
		do{
			int cyfra = score%10;
			score = score/10;
			heap.push(cyfra);
		}while(score!=0);
		
		int offset = 0;
		while(!heap.isEmpty()) {
			int cyfra = heap.pop();
			int off=offset*numWidth;
			offset++;
			switch(cyfra) {
				case 0 : imgDrawer.drawIMG(off, 0, numWidth, numHeight, R.raw._0); break;
				case 1 : imgDrawer.drawIMG(off, 0, numWidth, numHeight, R.raw._1); break;
				case 2 : imgDrawer.drawIMG(off, 0, numWidth, numHeight, R.raw._2); break;
				case 3 : imgDrawer.drawIMG(off, 0, numWidth, numHeight, R.raw._3); break;
				case 4 : imgDrawer.drawIMG(off, 0, numWidth, numHeight, R.raw._4); break;
				case 5 : imgDrawer.drawIMG(off, 0, numWidth, numHeight, R.raw._5); break;
				case 6 : imgDrawer.drawIMG(off, 0, numWidth, numHeight, R.raw._6); break;
				case 7 : imgDrawer.drawIMG(off, 0, numWidth, numHeight, R.raw._7); break;
				case 8 : imgDrawer.drawIMG(off, 0, numWidth, numHeight, R.raw._8); break;
				case 9 : imgDrawer.drawIMG(off, 0, numWidth, numHeight, R.raw._9); break;
				default: break;
			}	
		}
	}
	public void drawMesh(Mesh mesh, float[] mProjectionMatrix) {
		FloatBuffer positionsBuffer = mesh.getPositionsBuffer();
		FloatBuffer normalsBuffer = mesh.getNormalsBuffer();
		FloatBuffer uvsBuffer = mesh.getUVsBuffer();
		
		float[] color = mesh.getColor();
		
		 // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
     
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mesh.getTextureDataHandle(imgDrawer));
     
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
		
		
		positionsBuffer.position(0);		
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
        		0, positionsBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);  
        
        normalsBuffer.position(0);
        GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES20.GL_FLOAT, false, 
        		0, normalsBuffer);
        GLES20.glEnableVertexAttribArray(mNormalHandle);
        
        uvsBuffer.position(0);
        GLES20.glVertexAttribPointer( mTextureCoordinateHandle, mUVDataSize, GLES20.GL_FLOAT, false, 
        		0, uvsBuffer);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
         
        
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0); 
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0); 
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
               
        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
        GLES20.glUniform4f(mColorHandle, color[0], color[1], color[2], color[3]);
        
        // Draw the mesh.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mesh.getSize()); 
	}
	int width;
	int height;
	public void setScreenSize(int _width, int _height) {
		width = _width;
		height = _height;
		pauseButton = new Button(0.9f, 1.0f-0.1f*((float)width/(float)height), 0.1f, 0.1f*((float)width/(float)height), R.raw.pause);
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
