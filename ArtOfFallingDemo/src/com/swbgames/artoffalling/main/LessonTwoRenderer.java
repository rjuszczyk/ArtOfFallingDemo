package com.swbgames.artoffalling.main;


import java.nio.FloatBuffer;
import java.util.LinkedList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.swbgames.sounds.SoundHelper;
import com.swbgames.states.AppState;
import com.swbgames.states.ClassicGameSelectState;
import com.swbgames.states.GameState;
import com.swbgames.states.LoadingState;
import com.swbgames.states.LoseState;
import com.swbgames.states.NewLevelUnlockedState;
import com.swbgames.states.NotStartedState;
import com.swbgames.states.PauseState;
import com.swbgames.states.StateMachine;
import com.swbgames.states.WinnerState;


import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.swbgames.R;

/**
 * This class implements our custom renderer. Note that the GL10 parameter passed in is unused for OpenGL ES 2.0
 * renderers -- the static class GLES20 is used instead.
 */
public class LessonTwoRenderer implements GLSurfaceView.Renderer 
{
	GameState gameState;
	NotStartedState notStartedState;
	LoseState loseState;
	PauseState pauseState;
	LoadingState loadingMeshesState;
	ClassicGameSelectState classicGameSelectState;
	NewLevelUnlockedState newLevelUnlockedState;
	WinnerState winnerState;
	
	MainActivity mainActivity;
	
	StateMachine stateMachine;
	public StateMachine getStateMachine() {
		return stateMachine;
	}
	long startZTime;         //init in constructor and changin in draw method
	MediaPlayer mediaPlayer;
	GameSelector gameSelector;    //init in constructor
	Resources resources;     //init in constructor
	ImageDrawer imgDrawer;   //init in constructor
	SoundHelper soundHelper; //init in constructor
	/**
	 * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
	 * of being located at the center of the universe) to world space.
	 */
	private float[] mModelMatrix = new float[16];
	private float[] mViewMatrix = new float[16];
	private float[] mProjectionMatrix = new float[16];
	private float[] mMVPMatrix = new float[16];
	private float[] mLightModelMatrix = new float[16];	
	
	private int mMVPMatrixHandle;
	private int mMVMatrixHandle;
	private int mLightPosHandle;
	private int mPositionHandle;
	private int mColorHandle;
	private int mNormalHandle;

	private int mTextureUniformHandle;
	private int mTextureCoordinateHandle;	

	private final int mPositionDataSize = 3;		
	private final int mNormalDataSize = 3;
	private final int mUVDataSize = 2;
	
	private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
	private final float[] mLightPosInWorldSpace = new float[4];
	private final float[] mLightPosInEyeSpace = new float[4];
	private int mPerVertexProgramHandle;  // init in onSurfaceCreated() method

	Mesh coinMesh; //init in constructor
	Mesh slowMesh; //init in constructor

	Mesh bonusBlueMesh;
	Mesh bonusRedMesh;
	Mesh bonusGreenMesh;
	Mesh bonusSpecialMesh;
	
	public LessonTwoRenderer(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		initLessonTwoRenderer(mainActivity.getGameSelector(), mainActivity.getResources(), mainActivity.getSoundHelper(), mainActivity.getStateMachine(), mainActivity.getMediaPlayer());
	}
	/**
	 * Initialize the model data.
	 */
	private void initLessonTwoRenderer(GameSelector gameSelector, Resources _resources, SoundHelper _soundHelper, StateMachine _stateMachine, MediaPlayer mediaPlayer)	
	{	
		
		Log.i("debug", "start LessonTwoRenderer()");
		soundHelper = _soundHelper;
		imgDrawer = new ImageDrawer(_resources);
		resources = _resources;
		this.gameSelector = gameSelector;
		this.mediaPlayer = mediaPlayer;
		
		
		coinMesh =  Mesh.getMeshSerialized(R.raw.coin, resources, R.raw.tex2);
		slowMesh =  Mesh.getMeshSerialized(R.raw.slow, resources, R.raw.tex1);
		
		bonusBlueMesh =  Mesh.getMeshSerialized(R.raw.coin, resources, R.raw.blue);
		bonusRedMesh =  Mesh.getMeshSerialized(R.raw.coin, resources, R.raw.red);
		bonusGreenMesh =  Mesh.getMeshSerialized(R.raw.coin, resources, R.raw.green);
		bonusSpecialMesh =  Mesh.getMeshSerialized(R.raw.coin, resources, R.raw.special);
		
		stateMachine = _stateMachine;
		gameState = new GameState(
				0,
				soundHelper,
				gameSelector,
				imgDrawer,
				mediaPlayer,
				coinMesh,
				slowMesh,
				bonusBlueMesh,
				bonusRedMesh,
				bonusGreenMesh,
				bonusSpecialMesh,
				stateMachine);
		notStartedState = new NotStartedState(imgDrawer,mainActivity);
		loseState = new LoseState(imgDrawer,stateMachine, mainActivity);
		winnerState = new WinnerState(imgDrawer, stateMachine);
		loadingMeshesState = new LoadingState(imgDrawer,stateMachine, soundHelper);
		pauseState = new PauseState(imgDrawer,mainActivity);
		classicGameSelectState = new ClassicGameSelectState(imgDrawer,mainActivity);
		newLevelUnlockedState = new NewLevelUnlockedState(imgDrawer,stateMachine);
		
		stateMachine.init(gameState, notStartedState, loseState, pauseState,loadingMeshesState,classicGameSelectState,newLevelUnlockedState, winnerState);
		startZTime = SystemClock.uptimeMillis();
		Log.i("debug", "koniec LessonTwoRenderer()");
	}
	public void loadTextures() {
		//imgDrawer.loadTextures();
	}
	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) 
	{
		Log.i("debug2", "start onSurfaceCreated");
		// Set the background clear color to black.
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		// Use culling to remove back faces.
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		
		// Enable depth testing
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glEnable(GLES20.GL_BLEND);
		
		
		//GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA );
		
		imgDrawer.initImageDrawing();
		imgDrawer.loadTextures();
		//imgDrawer.loadTextures();
		
		
		final String vertexShader = RawResourceReader.readTextFileFromRawResource(resources, R.raw.walls_vertexshader);   		
 		final String fragmentShader = RawResourceReader.readTextFileFromRawResource(resources, R.raw.walls_fragmentshader);
 		
		final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);		
		final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);		
		
		mPerVertexProgramHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, 
				new String[] {"a_Position",  "a_Color", "a_Normal", "a_TexCoordinate"});
		
		gameState.setProgramHandle(mPerVertexProgramHandle);
		
		
		
		Log.i("debug2", "koniec onSurfaceCreated");
	}	
		
	
	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) 
	{
		Log.i("debug2", "start onSurfaceCHANGED");
		// Set the OpenGL viewport to the same size as the surface.
		GLES20.glViewport(0, 0, width, height);
		imgDrawer.setScreenSize(height, width);
		// Create a new perspective projection matrix. The height will stay the same
		// while the width will vary as per aspect ratio.
		final float ratio = (float) width / height;
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -1.0f;
		final float top = 1.0f;
		final float near = 1.0f;
		final float far = 10.0f;
		
		
		/*float eyeX = 0;
		float eyeY = 0;
		float eyeZ = 10;
		
		float centerX = 0;
		float centerY = 0;
		float centerZ = -10;
		
		float upX = 0;
		float upY = 1;
		float upZ = 0;*/
		//Matrix.setLookAtM(mProjectionMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
		//Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, 0.1f, 100.0f);
		//Matrix.perspectiveM(mProjectionMatrix, 0, 45.0f, ratio, 0.1f, 100.0f);
		//Matrix.orthoM(m, mOffset, left, right, bottom, top, near, far)
		PerspectiveMatrix.perspectiveM(mProjectionMatrix, 0, 45.0f, ratio, 0.25f, 100.0f);
		gameState.setProjectionMatrix(mProjectionMatrix);
		notStartedState.setScreenSize(width, height);
		loseState.setScreenSize(width, height);
		pauseState.setScreenSize(width, height);
		classicGameSelectState.setScreenSize(width, height);
		newLevelUnlockedState.setScreenSize(width, height);
		winnerState.setScreenSize(width, height);
		gameState.setScreenSize(width, height);
		
		Log.i("debug2", "koniec onSurfaceCHANGED");
	}	
	
	long lastTime =0;
	float speed = 0.0035f;
	long nextSpeedUP = 500;
//	public void restartGame(){
//		gameState.reset();
//		przegrana = false;
//		startZTime = SystemClock.uptimeMillis();
//	}

	@Override
	public void onDrawFrame(GL10 glUnused) 
	{
		//GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
		
		stateMachine.draw();
		
	}	
	
	
	public void drawMesh(Mesh mesh) {
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
	
	
	
}
