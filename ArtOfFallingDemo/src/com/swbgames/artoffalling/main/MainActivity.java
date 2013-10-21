package com.swbgames.artoffalling.main;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import com.swbgames.sounds.SoundHelper;
import com.swbgames.states.StateMachine;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ConfigurationInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.swbgames.R;

public class MainActivity extends Activity implements SensorEventListener {
	public Resources resources;
	//private GLSurfaceView mGLSurfaceView;
	private LessonSixGLSurfaceView mGLSurfaceView;
	private GameModel gameModel;
	private GameModel gameModel2;
	private GameModel gameModel3;
	private LessonTwoRenderer renderer;
	private SoundHelper soundHelper;
	private StateMachine stateMachine;
	private MediaPlayer mediaPlayer;
	private SharedPreferences mPrefs;
	private GameSelector gameSelector;
	
	public static Activity activity;
	
	public static final String PREFS_NAME = "__freefall__";

	public void test() throws IOException, ClassNotFoundException {
		AssetManager assets = getAssets();
        InputStream is = assets.open("fff.ser");
        ObjectInputStream ois = new ObjectInputStream(is);
        int[] array = (int[]) ois.readObject();
        ois.close();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activity = this;
		
		
		super.onCreate(savedInstanceState);
		Log.i("debug2", "start onCreate ");// + savedInstanceState == null ? "null" : "recovery");
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		soundHelper = new SoundHelper(this);
		soundHelper.load(R.raw.ding);
		soundHelper.load(R.raw.click);
		soundHelper.load(R.raw.broken_glass);
		
		mPrefs = getSharedPreferences(PREFS_NAME, 0);
		
		
		
		mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.theme1);
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mediaPlayer.seekTo(0);
				mediaPlayer.start();
			}
			
		});
		//mediaPlayer.start();
		stateMachine = new StateMachine();
		mGLSurfaceView = new LessonSixGLSurfaceView(this);//new GLSurfaceView(this);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
		
		resources = getResources();
		int bestScore1 = mPrefs.getInt("score1", 0);
		int bestScore2 = mPrefs.getInt("score2", 0);
		int bestScore3 = mPrefs.getInt("score3", 0);
		
		
		gameModel = new GameModel(resources, bestScore1, stateMachine);
		gameModel2 = new GameModel2(resources, bestScore2, stateMachine);
		gameModel3 = new GameModel3(resources, bestScore3, stateMachine);
		
		
		if(savedInstanceState!=null) {
			Log.i("debug", "onCreate savedInstanceState!=null");
			int state = savedInstanceState.getInt("my_state");
			if(state == StateMachine.GAME_STATE)
				state = StateMachine.PAUSE_STATE;
			stateMachine.firstState = state;
			Log.e("RESTORE", "restoring in oncreate");
			gameSelector.getGameModel().restoreGame(savedInstanceState);
		} 
		
		this.gameSelector = new GameSelector(new GameModel[]{gameModel, gameModel2, gameModel3});
		//renderer = new LessonTwoRenderer(gameSelector,resources,soundHelper,stateMachine, mediaPlayer );
		renderer = new LessonTwoRenderer(this);
		if(supportsEs2) {
			mGLSurfaceView.setEGLContextClientVersion(2);
			
			mGLSurfaceView.setRenderer(renderer);
		} else {
			//niekompatybliny z OpenGL ES 2.0, moze z ES 1?
			return;
		}
		
		SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		// add listener. The listener will be HelloAndroid (this) class
		sensorManager.registerListener(this, 
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_FASTEST);

		Log.i("debug2", "koniec onCreate "); 
		setContentView(mGLSurfaceView);
		
		
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				
				
				stateMachine.setState(StateMachine.LOADING_MESHES_STATE);
				gameModel.initGame();
				gameModel2.initGame();
				gameModel3.initGame();	
				stateMachine.setState(StateMachine.NOT_STARTED_STATE);
			}
			
		});
		t.start();
		//stateMachine.setState(StateMachine.NOT_STARTED_STATE);
	}

	@Override
	public void onResume() {
		Log.i("debug2", "start onResume "); 
		this.stateMachine.gameState.loadGameData();
		super.onResume();
		mGLSurfaceView.onResume();
		Log.i("debug2", "koniec onREsume ");
	}
	
	@Override
	protected void onPause() {
		Log.i("debug2", "start onPause ");
		this.stateMachine.gameState.saveGameData();
		int state = this.stateMachine.getCurrentStateID();
		if(state == StateMachine.GAME_STATE)
			state = StateMachine.PAUSE_STATE;
		
		this.stateMachine.setState(state);
	    super.onPause();
	    mGLSurfaceView.onPause();
	    Log.i("debug2", "koniec onPause ");
	}
	float[] mRotateMatrix;
	
	final int numberOfLastVals = 5;
	float factor = 0.4f;
	float lastX = 0;
	float lastY = 0;
	
	public boolean switchXY=false;
	
	private static final boolean ADAPTIVE_ACCEL_FILTER = true;
	float lastAccel[] = new float[3];
	float accelFilter[] = new float[3];
	
	

	/*
	public void onAccelerometerChanged(float accelX, float accelY, float accelZ) {
	    // high pass filter
	    float updateFreq = 30; // match this to your update speed
	    float cutOffFreq = 0.9f;
	    float RC = 1.0f / cutOffFreq;
	    float dt = 1.0f / updateFreq;
	    float filterConstant = RC / (dt + RC);
	    float alpha = filterConstant; 
	    float kAccelerometerMinStep = 0.033f;
	    float kAccelerometerNoiseAttenuation = 3.0f;

	    if(ADAPTIVE_ACCEL_FILTER)
	    {
	    	double[] w1 = {accelFilter[0],accelFilter[1],accelFilter[2]};
	    	float norm1 =  (float)(new Vector(w1)).magnitude();
	    	double[] w2 = {accelX,accelY,accelZ};
	    	float norm2 =  (float)(new Vector(w2)).magnitude();
	    	
	        float d = Math.max(0, Math.min(1, Math.abs(norm1 - norm2) / kAccelerometerMinStep - 1.0f));
	        alpha = d * filterConstant / kAccelerometerNoiseAttenuation + (1.0f - d) * filterConstant;
	    }

	    accelFilter[0] = (float) (alpha * (accelFilter[0] + accelX - lastAccel[0]));
	    accelFilter[1] = (float) (alpha * (accelFilter[1] + accelY - lastAccel[1]));
	    accelFilter[2] = (float) (alpha * (accelFilter[2] + accelZ - lastAccel[2]));

	    lastAccel[0] = accelX;
	    lastAccel[1] = accelY;
	    lastAccel[2] = accelZ;
	    
	    sterowanie2(accelFilter[0], accelFilter[1], accelFilter[2]);
	    //manageAccel(accelFilter[0], accelFilter[1], accelFilter[2]);
	}
	*/
	
	private long lastSensorUpdateTime = System.currentTimeMillis();
	float x_pos = 500;
	float y_pos = 500;
	public void sterowanie2(float xa, float ya, float z) {
		
		if(renderer.getStateMachine().getCurrentStateID() != StateMachine.GAME_STATE) {
			defaultX= xa;
			defaultY= ya;
		}
		xa-=defaultX;
		ya-=defaultY;
		int maxWidth = 1000;
		int maxHeight = 1000;
		
		long deltaTime = System.currentTimeMillis() - lastSensorUpdateTime;
		lastSensorUpdateTime = System.currentTimeMillis();
		
		deltaTime *= 9000;
		
		if(xa>0.2 || xa<-0.2) {
			x_pos+=(deltaTime/(((float)maxWidth)*3.f)*( ((Math.sin(xa/10)))))* (1+Math.exp((Math.abs(0.5*xa)-1))/2);
		}
		if(x_pos<0)
			x_pos=0.f;
		if(x_pos>maxWidth)
			x_pos=maxWidth;
		
		Log.i("xy","xa ya= " + xa + "   x pos= "+ x_pos);
		
		if(ya>0.2 || ya<-0.2) {
			y_pos+=(deltaTime/(((float)maxHeight)*3.f)*( ((Math.sin(ya/10)))))*  (1+Math.exp((Math.abs(xa)-1)/2));
		}
		if(y_pos<0)
			y_pos=0.f;
		if(y_pos>maxHeight)
			y_pos=maxHeight;
		
		
		float x=x_pos/500 - 1;
		float y=y_pos/500 - 1;	
		gameSelector.getGameModel().setXPos(y);
		gameSelector.getGameModel().setYPos(-x);
	}
	float defaultX=0.f;
	float defaultY=0.f;
	
	/*
	public void manageAccel(float x , float y, float z){
		
		
		if(renderer.getStateMachine().getCurrentStateID() != StateMachine.GAME_STATE) {
			
					//z=-z;
					y=-y;
					x=-x;
											
					float[] firstRotate = new float[16];
					
					Matrix.setIdentityM(firstRotate, 0);
					Matrix.rotateM(firstRotate, 0, 90, 0.0f, 1.0f, 0.0f);
					float[] vectorX = new float[4];
					vectorX[0]=x;
					vectorX[1]=y;
					vectorX[2]=z;
					vectorX[3]=1;
					Matrix.multiplyMV(vectorX, 0, firstRotate, 0, vectorX, 0);
					
					float[] vectorY = new float[4];
					vectorY[0]=x;
					vectorY[1]=y;
					vectorY[2]=z;
					vectorY[3]=1;
					Matrix.setIdentityM(firstRotate, 0);
					Matrix.rotateM(firstRotate, 0,  90, -1.0f, 0.0f, 0.0f);
					//Matrix.rotateM(firstRotate, 0,  180, 0.0f, 0.0f, 1.0f);
					Matrix.multiplyMV(vectorY, 0, firstRotate, 0, vectorY, 0);
					
					
					mRotateMatrix = new float[] {
							vectorX[0]/10,    vectorY[0]/10,  x/10,    0,
							vectorX[1]/10,    vectorY[1]/10,  y/10,    0,
							vectorX[2]/10,    vectorY[2]/10,  z/10,    0,
							0,    0,0,1 };
					Matrix.invertM(mRotateMatrix,0,mRotateMatrix,0);
			
		}
		float[] vector = new float[4];
		vector[0]=x;
		vector[1]=y;
		vector[2]=z;
		vector[3]=1;
		
		
		Matrix.multiplyMV(vector, 0, mRotateMatrix, 0, vector, 0);
		x=vector[0];
		y=vector[1];
		
		x = x/3;
		if(x>1.0)x=1; if(x<-1.0)x=-1;
		
		y = y/3;
		if(y>1.0)y=1; if(y<-1.0)y=-1;
		
		lastX = x*factor + lastX*(1.0f-factor);
		lastY = y*factor + lastY*(1.0f-factor);
		
		if(lastX>1.0)lastX=1; if(lastX<-1.0)lastX=-1;
		if(lastY>1.0)lastY=1; if(lastY<-1.0)lastY=-1;
		x=-lastX;
		y=lastY;
		
		gameSelector.getGameModel().setXPos(y);
		gameSelector.getGameModel().setYPos(x);
		
	
	}
	*/
	public void onSensorChanged(SensorEvent event){
		
		// check sensor type
		if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
				float x,y,z;
				// assign directions
			
				x=event.values[0];
				y=event.values[1];	
				z=event.values[2];
				sterowanie2(x,y,z);
			
				
			
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		Log.i("debug2", "start onSaveInstanceState ");
		
	    renderer.gameState.saveGameData(savedInstanceState);
	    
	    savedInstanceState.putInt("my_state", renderer.stateMachine.getCurrentStateID());
	    Log.e("RESTORE", "SAVING BUNDLE, state="+renderer.stateMachine.getCurrentStateID());
	    super.onSaveInstanceState(savedInstanceState);
	}
	/*
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.e("RESTORE", "LOADING BUNDLE");
		Log.w("RESTORE", "score="+ savedInstanceState.getInt("score"));
	    // Always call the superclass so it can restore the view hierarchy
		renderer.gameState.loadGameData(savedInstanceState);
		int state = savedInstanceState.getInt("my_state");
		if(state == StateMachine.GAME_STATE)
			state = StateMachine.PAUSE_STATE;
		renderer.stateMachine.setState (state);
	    super.onRestoreInstanceState(savedInstanceState);
	}*/
	@Override
	protected void onDestroy() {
		Log.i("debug2", "start onDestroy ");
		mediaPlayer.stop();
		mediaPlayer.release();
		
		Editor editor = this.mPrefs.edit();
		editor.putInt("score1", gameModel.getBestScore());
		editor.putInt("score2", gameModel2.getBestScore());
		editor.putInt("score3", gameModel3.getBestScore());
		editor.commit();
		editor.apply();
		
		//Log.i("debug", "start onDestroy ");
		super.onDestroy();
		Log.i("debug2", "koniec onDestroy ");
		System.exit(0);
	}
	@Override
    public void onConfigurationChanged(Configuration newConfig) 
    {
        super.onConfigurationChanged(newConfig);
    }
	
	
	public LessonSixGLSurfaceView getLessonSixGLSurfaceView() { return this.mGLSurfaceView; }
	public GameModel getGameModel1() { return this.gameModel; }
	public GameModel getGameModel2() { return this.gameModel2; }
	public LessonTwoRenderer getLessonTwoRenderer() { return this.renderer; }
	public SoundHelper getSoundHelper() { return this.soundHelper; }
	public StateMachine getStateMachine() { return this.stateMachine; }
	public MediaPlayer getMediaPlayer() { return this.mediaPlayer; }
	public SharedPreferences getSharedPreferences() { return this.mPrefs; }
	public GameSelector getGameSelector() {return this.gameSelector; }
}