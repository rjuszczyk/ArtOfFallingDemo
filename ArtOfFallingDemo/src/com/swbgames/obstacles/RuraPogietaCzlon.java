package com.swbgames.obstacles;

import com.swbgames.artoffalling.main.Mesh;

import android.opengl.Matrix;


public class RuraPogietaCzlon implements Obstacle{
	Mesh mesh;
	private float rotation;
	private float y_offset;
	private float special_x;
	public RuraPogietaCzlon(Mesh m, float rotation, float y_offset) {
		this.mesh = m;
		this.rotation = rotation;
		this.y_offset = y_offset;
		this.special_x = 666.0f;
	}
	public RuraPogietaCzlon(Mesh m, float rotation, float y_offset,float special_x) {
		this.mesh = m;
		this.rotation = rotation;
		this.y_offset = y_offset;
		this.special_x = special_x;
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
		if(this.special_x == 666.0f) {
			if(mXY[1]< y_offset)
				return true;
			else
				return false;
		}else {
			if(mXY[1]< y_offset && mXY[0]>this.special_x)
				return true;
			else
				return false;
		}
			
	}

	@Override
	public Mesh getMesh() {
		return mesh;
	}

	@Override
	public float getRotation() {
		return this.rotation-45;
	}

}
