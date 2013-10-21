package com.swbgames.states;

import java.util.ArrayList;

import com.swbgames.artoffalling.main.GameModel;
import com.swbgames.artoffalling.main.ImageDrawer;
import com.swbgames.others.ui.NotYet;
import com.swbgames.sounds.SoundHelper;

import android.util.Log;

import com.swbgames.R;

public class LoadingState extends AppState {


	private ImageDrawer imgDrawer;
	private StateMachine stateMachine;
	
	private SoundHelper soundHelper;
	
	public LoadingState(ImageDrawer imgDrawer, StateMachine stateMachine, SoundHelper soundHelper) {
		this.imgDrawer = imgDrawer;
		this.stateMachine = stateMachine;
		this.soundHelper = soundHelper;
	}
	private int first = 0;
	private static final int SIZE_NOT_YETS = 30;
	private ArrayList<NotYet> notYets = new ArrayList<NotYet>(SIZE_NOT_YETS);
	private int it_yet =0;
	private int minSize=0;
	private void notYetsAdd(NotYet ny) {
		if(it_yet==SIZE_NOT_YETS) {
			it_yet=0;
		}
		if(minSize < SIZE_NOT_YETS) {
			notYets.add(ny);
			it_yet++;
			minSize++;
			return;
		} else {
			minSize=SIZE_NOT_YETS;
		}
		notYets.set(it_yet, ny);
		it_yet++;
	}
	private void drawNotYets() {
		for(int i = 0; i < minSize;i++) {
			notYets.get(i).draw(imgDrawer);
		}
	}
	@Override
	public void draw() {
		
		if(first==0) {
			imgDrawer.drawIMG(R.raw.loading_textures);
			first++;
			return;
		}
		if(first==1) {
			imgDrawer.loadTextures();
			first++;
			return;
		}
		
		imgDrawer.drawIMG(R.raw.loading_meshes);	
		drawNotYets();
	}

	@Override
	public void onClick(float x, float y) {
		notYetsAdd(NotYet.getNotYet(x, y)); 
		soundHelper.play(R.raw.ding);
		// TODO Auto-generated method stub
		//stateMachine.setState(StateMachine.NOT_STARTED_STATE);
	}
	public int getState() {
		return StateMachine.LOADING_MESHES_STATE;
	}

}
