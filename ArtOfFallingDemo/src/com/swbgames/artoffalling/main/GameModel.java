package com.swbgames.artoffalling.main;

import java.util.ArrayList;
import java.util.Random;

import com.swbgames.obstacles.BonusItem;
import com.swbgames.obstacles.Coin;
import com.swbgames.obstacles.Obstacle;
import com.swbgames.obstacles.Rura1;
import com.swbgames.obstacles.RuraPogietaCzlon;
import com.swbgames.obstacles.RuraPusta;
import com.swbgames.obstacles.RuraZDziura;
import com.swbgames.obstacles.SlowItem;
import com.swbgames.states.StateMachine;



import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.swbgames.R;


public class GameModel {
	private float x_pos;
	private float y_pos;
	private float z_pos;

	public int score = 0;
	public int bestScore;
	public static final float default_speed= 0.0035f;
	public float speed = 0.0035f;
	
	public int incPointsFactor = 1;
	
	public long nextSpeedUP = 1000;
	public int iloscZyc = 3;
	public boolean smierc() {
		if(--iloscZyc > 0)
			return true;
		else
			return false;
	}
	protected StateMachine stateMachine;
	protected Resources resources;
	protected static final int len = 5000;
	ArrayList<Obstacle> obstacles;
	ArrayList<Coin> coins;
	ArrayList<SlowItem> slows; 
	ArrayList<BonusItem> bonuses;
	
	float[] obstacleAngles = new float[len];
	boolean[] hitedObstacles = new boolean[len];
	boolean[] passedObstacles = new boolean[len];
	
	public void saveGame(Bundle savedInstanceState) {
		
	    // Save the user's current game state
	    savedInstanceState.putInt("score1", score);
	    savedInstanceState.putFloat("Z", z_pos);
	    savedInstanceState.putFloat("speed", speed);
	    savedInstanceState.putLong("nextSpeedUP", nextSpeedUP);
	    savedInstanceState.putInt("bestScore",this.bestScore);
	}
	
	public void restoreGame(Bundle savedInstanceState) {
		this.score = savedInstanceState.getInt("score1");
		this.z_pos = savedInstanceState.getFloat("Z");
		this.speed = savedInstanceState.getFloat("speed");
	    this.nextSpeedUP = savedInstanceState.getLong("nextSpeedUP");
	    this.bestScore = savedInstanceState.getInt("bestScore");
	}
	
	public int getBestScore() {
		return this.bestScore;
	}
	public boolean setBestScore(int newBestScore) {
		if(this.bestScore < newBestScore) {
			this.bestScore = newBestScore;
			
			return true;
		}
		return false;
	}
	public Bonusy bonusy;
	public GameModel(Resources resources, int bestScore_, StateMachine stateMachine) {
		this.resources = resources;
		this.bestScore = bestScore_;
		bonusy = new Bonusy(this);
		this.stateMachine = stateMachine;
		x_pos = 0;
		y_pos = 0;
		z_pos = 0;
		
	}
	
	
	
	public void onLose(){
		
		if(this.score > 99 && this.bestScore <= 99) {
			stateMachine.setState(StateMachine.NEW_LEVEL_UNLOCKED);
			stateMachine.classicGameSelectState.level2locked = false;
		}
		else
			stateMachine.setState(StateMachine.LOSE_STATE);
	}
	
	public float getXPos(){return x_pos;}
	public float getYPos(){return y_pos;}
	public float getZPos(){return z_pos;}
	public void setXPos(float x){ if(x>1.0)x=1; if(x<-1.0)x=-1; x_pos=x*2.5f;}
	public void setYPos(float y){ if(y>1.0)y=1; if(y<-1.0)y=-1; y_pos=y*2.5f;}
	public void setZPos(float z){ z_pos=z;}
	public int getObstaclesCount() { return obstacles.size(); }
	public Obstacle getObstacleAt(int x) { return obstacles.get(x); }
	public float[] getObstacleAngles() { return obstacleAngles;}
	public Coin getCoinAt(int x) { return coins.get(x); }
	public BonusItem getBonusAt(int x) { return bonuses.get(x); }
	public SlowItem getSlowItemAt(int x) { return slows.get(x); }

	
	public void incScore(int pts) {
		this.score += incPointsFactor*pts;
	}
	long lastUpdate = 0;
	public void changeIncPointsFactor(int times) {
		incPointsFactor*=times;
		lastUpdate = System.currentTimeMillis();
	}
	public void checkResetIncPointsFactor() {
		if(lastUpdate !=0 && System.currentTimeMillis() - lastUpdate > 30000)
			incPointsFactor = 1;
	}
	
	public boolean testPickCoin() {
		int first = (int) ( getZPos() / 5.988f);
        first = first - 2;
        if(first<0)first=0;
        int last = first+20;
        if(last > getObstaclesCount())
        	last =  getObstaclesCount();
        for(int i = first; i < last; i++) 
	
	//	for(int i = 0; i < coins.size(); i++)
			if(-0.5f+z_pos+0.2 > 3.0f+5.988f*i  && -0.5f+z_pos-0.2 < 3.0f+5.988f*i) {
				if(i==0)return false;//zeby nie walic w pierwsze
					if(!coins.get(i).picked) {
						if(coins.get(i).checkHited(x_pos,y_pos)){
							coins.get(i).pick();
							return true;
						}
					}
				
			}
		return false;
	}
	public int lastBonusPicked=0;
	public boolean testPickBonusItem() {
		int first = (int) ( getZPos() / 5.988f);
        first = first - 2;
        if(first<0)first=0;
        int last = first+20;
        if(last > getObstaclesCount())
        	last =  getObstaclesCount();
        for(int i = first; i < last; i++) 
	
	//	for(int i = 0; i < bonuses.size(); i++)
			if(-0.5f+z_pos+0.2 > 3.0f+5.988f*i  && -0.5f+z_pos-0.2 < 3.0f+5.988f*i) {
				if(i==0)return false;//zeby nie walic w pierwsze
					if(!bonuses.get(i).picked) {
						if(bonuses.get(i).checkHited(x_pos,y_pos)){
							bonuses.get(i).pick();
							lastBonusPicked = bonuses.get(i).type; 
							return true;
						}
					}
				
			}
		return false;
	}
	
	public boolean testPickSlowItem() {
		
		int first = (int) ( getZPos() / 5.988f);
        first = first - 2;
        if(first<0)first=0;
        int last = first+20;
        if(last > getObstaclesCount())
        	last =  getObstaclesCount();
        for(int i = first; i < last; i++) 
		
//		for(int i = 0; i < slows.size(); i++)
			if(-0.5f+z_pos+0.2 > 3.0f+5.988f*i  && -0.5f+z_pos-0.2 < 3.0f+5.988f*i) {
				if(i==0)return false;//zeby nie walic w pierwsze
					if(!slows.get(i).picked) {
						if(slows.get(i).checkHited(x_pos,y_pos)){
							slows.get(i).pick();
							return true;
						}
					}
				
			}
		return false;
	}
	
	public void reset() {
		for(int i = 0; i < obstacles.size(); ++i) {
			hitedObstacles[i] = false;
			passedObstacles[i] = false;
			coins.get(i).picked = false;
			slows.get(i).picked = false;
			bonuses.get(i).picked = false;
		}
		setZPos(0);
		iloscZyc=3;
		score = 0;
		speed = 0.0035f;
		nextSpeedUP = 550;
	}
	public void initGame() {

		obstacles = new ArrayList<Obstacle>();
		coins = new ArrayList<Coin>();
		slows = new ArrayList<SlowItem>();
		bonuses = new ArrayList<BonusItem>();
		
		Mesh meshRura1 = Mesh.getMeshSerialized(R.raw.rura_high_tex, resources, R.raw.tex1);
		Mesh meshRuraZDziura = Mesh.getMeshSerialized(R.raw.rura_srodek_high_tex, resources, R.raw.tex1);
		Mesh meshRuraPusta = Mesh.getMeshSerialized(R.raw.rura_pusta_high_tex, resources, R.raw.tex1);
		Mesh meshRuraPusta2 = Mesh.getMeshSerialized(R.raw.rura_pusta2_high_tex, resources, R.raw.tex1);
		Mesh meshRuraPusta3 = Mesh.getMeshSerialized(R.raw.rura_pusta3_high_tex, resources, R.raw.tex2);
		
		
		
		Random random = new Random();
		final float prawdopodobienstwoMonety = 0.8f;
		final float prawdopodobienstwoSpowolnienia= 0.002f;
		final float prawdopodobienstwoBonusu = 0.45f;
		
		float lastRandRotation = -1;
		for(int i = 0; i < len; i++) {
			
			float rotation;
			do {
				rotation = (int)(Math.random()*10)*(360.0f/10.0f);
			} while(rotation == lastRandRotation);			
			lastRandRotation = rotation;
			
			double rand = Math.random();
			coins.add(new Coin(random.nextFloat()*2.5f, random.nextFloat()*2.5f, random.nextFloat() <prawdopodobienstwoMonety ? false : true));
			slows.add(new SlowItem(random.nextFloat()*2.0f, random.nextFloat()*2.0f, random.nextFloat() <prawdopodobienstwoSpowolnienia ? false : true));
			bonuses.add(new BonusItem(random.nextFloat()*2.0f, random.nextFloat()*2.0f, random.nextFloat() <prawdopodobienstwoBonusu ? false : true));
						
			if(rand<0.60)
				obstacles.add(new Rura1(meshRura1,rotation));
			//else if(rand<0.66)
			else if(rand<0.85)
				obstacles.add(new RuraZDziura(meshRuraZDziura,rotation));
			else if(rand<0.90)
				obstacles.add(new RuraPusta(meshRuraPusta2,rotation));
			else if(rand<0.95)
				obstacles.add(new RuraPusta(meshRuraPusta,rotation));
			else if(rand<1.0)
				obstacles.add(new RuraPusta(meshRuraPusta3,rotation));
				//obstacles.add(new Rura2(meshRura2,rotation));
			
			hitedObstacles[i] = false;
		}	
	}
	

	
	public boolean test() {
		int first = (int) ( getZPos() / 5.988f);
        first = first - 2;
        if(first<0)first=0;
        int last = first+20;
        if(last > getObstaclesCount())
        	last =  getObstaclesCount();
        for(int i = first; i < last; i++) 
	
	//	for(int i = 0; i <obstacles.size(); ++i)
			if(-0.5f + z_pos-0.1 > 7.0f+5.988f*i && passedObstacles[i]==false) {
				passedObstacles[i]=true;
				
				if(hitedObstacles[i] == false) {
					if(obstacles.get(i).checkHited(x_pos,y_pos)){
						hitedObstacles[i]=true;
						return true;
					}
				}
			}
		return false;
	}

}
