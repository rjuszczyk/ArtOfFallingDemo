package com.swbgames.obstacles;

import com.swbgames.artoffalling.main.Mesh;

import android.opengl.Matrix;
import android.util.Log;

import com.swbgames.R;

public class Rura1 implements Obstacle{
	Mesh mesh;
	private float rotation;
	
	public Rura1(Mesh m, float rotation) {
		this.mesh = m;
		this.rotation = rotation;
	}
	
	@Override
	public boolean checkHited(float x, float y) {
		float[] mModelMatrix = new float[16];
		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.rotateM(mModelMatrix, 0, -rotation, 0.0f, 0.0f, -1.0f);
		float[] mXY = new float[4];
		mXY[0]=x;
		mXY[1]=y;
		mXY[2]=1;
		mXY[3]=1;
		Matrix.multiplyMV(mXY, 0, mModelMatrix, 0, mXY, 0);
		
		if(mXY[1]<0)
			return true;
		else
			return false;
	}

	@Override
	public Mesh getMesh() {
		return mesh;
	}

	@Override
	public float getRotation() {
		return rotation;
	}

}
