package com.swbgames.others.ui;

import java.util.Random;

import com.swbgames.artoffalling.main.ImageDrawer;


import com.swbgames.R;

public class NotYet {
	public float x;
	public float y;
	public int resource_id;
	public NotYet(float x2, float y2,int resource_id_) { this.x=x2; this.y=y2; this.resource_id =resource_id_;}
	
	public void draw(ImageDrawer imgDrawer) {
		float width = 100;
		float height = 30;
		imgDrawer.drawIMG(x, y,  width,height, resource_id);
	}
	public static Random random = new Random();
	public static NotYet getNotYet(float x2, float y2) {
		int randInt = random.nextInt(4);
		switch(randInt) {
		case 0: return new NotYet(x2,y2,R.raw.notyet1);
		case 1: return new NotYet(x2,y2,R.raw.notyet2);
		case 2: return new NotYet(x2,y2,R.raw.notyet3);
		case 3: return new NotYet(x2,y2,R.raw.notyet4);
		default: return new NotYet(x2,y2,R.raw.notyet1);
		}
		
	}
}
