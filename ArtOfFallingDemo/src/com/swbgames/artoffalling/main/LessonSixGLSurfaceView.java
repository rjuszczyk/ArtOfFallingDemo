package com.swbgames.artoffalling.main;

import com.swbgames.states.AppState;
import com.swbgames.states.StateMachine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;

public class LessonSixGLSurfaceView extends GLSurfaceView 
{	
	private LessonTwoRenderer mRenderer;
	
	
	public float acc_x;
	public float acc_y;
	public float acc_z;
	

    
    private float mDensity;
    MainActivity mainActivity;
	public LessonSixGLSurfaceView(Context context) 
	{
		
		super(context);
		mainActivity = (MainActivity)context;
		
	}
	
	public LessonSixGLSurfaceView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);		
	}
	float mPreviousX = 0;
	float mPreviousY = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		Log.i("event","event");
		if (event != null)
		{			
			float x = event.getX();
			float y = event.getY();
			
			if (event.getAction() == MotionEvent.ACTION_DOWN)//MotionEvent.ACTION_MOVE)
			{
				StateMachine stateMachine = mRenderer.getStateMachine();
				stateMachine.onClick(x, y);
				//if (mRenderer != null) {
				//	mRenderer.restartGame();								
				//}
			}
			
			return true;
		}
		else
		{
			return super.onTouchEvent(event);
		}		
	}

	// Hides superclass method.
	public void setRenderer(LessonTwoRenderer renderer, float density) 
	{
		mRenderer = renderer;
		mDensity = density;
		super.setRenderer(renderer);
	}
	// Hides superclass method.
	public void setRenderer(LessonTwoRenderer renderer) 
	{
		mRenderer = renderer;
		super.setRenderer(renderer);
		Bundle b;
		
	}
	
	
}
