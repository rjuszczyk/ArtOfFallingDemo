package com.swbgames.artoffalling.main;

import java.util.ArrayList;
import java.util.Random;

import com.swbgames.obstacles.BonusItem;
import com.swbgames.obstacles.Coin;
import com.swbgames.obstacles.Obstacle;
import com.swbgames.obstacles.Rura1;
import com.swbgames.obstacles.RuraBelka;
import com.swbgames.obstacles.RuraPusta;
import com.swbgames.obstacles.RuraZDziura;
import com.swbgames.obstacles.SlowItem;
import com.swbgames.states.StateMachine;


import com.swbgames.R;

import android.content.res.Resources;

public class GameModel3 extends GameModel{
	public GameModel3(Resources resources, int bestScore_, StateMachine stateMachine) {
		super(resources, bestScore_, stateMachine);
		
	}
	public void onLose(){
		if(this.score > 99 ) {
			stateMachine.setState(StateMachine.WINNER_STATE);
		}
		else
			stateMachine.setState(StateMachine.LOSE_STATE);
	}
	public void initGame() {

		obstacles = new ArrayList<Obstacle>();
		coins = new ArrayList<Coin>();
		slows = new ArrayList<SlowItem>();
		bonuses = new ArrayList<BonusItem>();
		
		Mesh rura_pusta1 = Mesh.getMeshSerialized(R.raw.rury2_rura_pusta1, resources,  R.raw.rury2_textura);
		Mesh rura_pusta2 = Mesh.getMeshSerialized(R.raw.rury2_rura_pusta2, resources,  R.raw.rury2_textura);
		Mesh rura_pusta3 = Mesh.getMeshSerialized(R.raw.rury2_rura_pusta3, resources,  R.raw.rury2_textura);
		Mesh rura_srodek = Mesh.getMeshSerialized(R.raw.rury2_rura_srodek, resources,  R.raw.rury2_textura);
		Mesh rura_srodek_wlot = Mesh.getMeshSerialized(R.raw.rury2_rura_srodek_wlot, resources,  R.raw.rury2_textura);
		Mesh rura_polowka = Mesh.getMeshSerialized(R.raw.rury2_rura_polowka, resources,  R.raw.rury2_textura);
		Mesh rura_belka = Mesh.getMeshSerialized(R.raw.rury2_rura_belka, resources,  R.raw.rury2_textura);
		
		
			
		Random random = new Random();
		final float prawdopodobienstwoMonety = 0.8f;
		final float prawdopodobienstwoSpowolnienia= 0.002f;
		final float prawdopodobienstwoBonusu = 0.45f;
		
		float lastRandRotation = -1;
		for(int i = 0; i < len; i++) {
			
			float rotation;
			do {
				rotation = (int)(Math.random()*5)*(360.0f/5.0f);
			} while(rotation == lastRandRotation);			
			lastRandRotation = rotation;
			
			double rand = Math.random();
			coins.add(new Coin(random.nextFloat()*2.5f, random.nextFloat()*2.5f, random.nextFloat() <prawdopodobienstwoMonety ? false : true));
			slows.add(new SlowItem(random.nextFloat()*2.0f, random.nextFloat()*2.0f, random.nextFloat() <prawdopodobienstwoSpowolnienia ? false : true));
			bonuses.add(new BonusItem(random.nextFloat()*2.0f, random.nextFloat()*2.0f, random.nextFloat() <prawdopodobienstwoBonusu ? false : true));
			
			if(rand<0.25)
				obstacles.add(new RuraBelka(rura_belka,rotation));
			//else if(rand<0.66)
			else if(rand<0.60)
				obstacles.add(new Rura1(rura_polowka,rotation));
			//else if(rand<0.66)
			else if(rand<0.85)
				obstacles.add(new RuraZDziura(rura_srodek,rotation));
			else if(rand<0.90)
				obstacles.add(new RuraPusta(rura_pusta1,rotation));
			else if(rand<0.95)
				obstacles.add(new RuraPusta(rura_pusta2,rotation));
			else if(rand<1.0)
				obstacles.add(new RuraPusta(rura_pusta3,rotation));
				//obstacles.add(new Rura2(meshRura2,rotation));
			
			hitedObstacles[i] = false;
		}	
	}
}
