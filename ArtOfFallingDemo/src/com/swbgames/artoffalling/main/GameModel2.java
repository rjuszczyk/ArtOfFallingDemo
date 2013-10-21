package com.swbgames.artoffalling.main;

import java.util.ArrayList;
import java.util.Random;

import com.swbgames.obstacles.BonusItem;
import com.swbgames.obstacles.Coin;
import com.swbgames.obstacles.Obstacle;
import com.swbgames.obstacles.Rura1;
import com.swbgames.obstacles.RuraPusta;
import com.swbgames.obstacles.RuraZDziura;
import com.swbgames.obstacles.SlowItem;
import com.swbgames.states.StateMachine;


import com.swbgames.R;

import android.content.res.Resources;

public class GameModel2 extends GameModel{
	public GameModel2(Resources resources, int bestScore_, StateMachine stateMachine) {
		super(resources, bestScore_, stateMachine);
		
	}
	public void onLose(){
		if(this.score > 99 && this.bestScore <= 99) {
			stateMachine.setState(StateMachine.NEW_LEVEL_UNLOCKED);
			stateMachine.classicGameSelectState.level3locked = false;
		}
		else
			stateMachine.setState(StateMachine.LOSE_STATE);
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
		Mesh meshRuraWylot = Mesh.getMeshSerialized(R.raw.rura_wylot, resources, R.raw.tex3);
		Mesh meshRuraWlot = Mesh.getMeshSerialized(R.raw.rura_wlot, resources, R.raw.tex3);
		Mesh meshNoMesh = Mesh.getMeshSerialized(R.raw.rura_no_mesh, resources, R.raw.tex3);
		Mesh meshMeteorytyPolowka = Mesh.getMeshSerialized(R.raw.meteoryty_polowka, resources, R.raw.tex4);
		Mesh meshMeteorytyPolowka2 = Mesh.getMeshSerialized(R.raw.meteor_polowka2, resources, R.raw.tex4);
		Mesh meshMeteorytyPolowka3 = Mesh.getMeshSerialized(R.raw.meteor_polowka3, resources, R.raw.tex4);
		
		Mesh meshMeteorytDziura = Mesh.getMeshSerialized(R.raw.ser_meteor_dziura, resources, R.raw.tex4);
		
		
		//Mesh meshMeteorytDziura = Mesh.getMeshSerialized(R.raw.meteor_dziura, resources, R.raw.tex4);
		Mesh meshResztkiDziura = Mesh.getMeshSerialized(R.raw.resztki_dziura, resources, R.raw.tex1);
		Mesh meshPanelPolowka = Mesh.getMeshSerialized(R.raw.panel_sloneczny_polowka, resources, R.raw.tex3);
		/*
		Mesh meshRura1 = Mesh.getMesh(R.raw.rura_high_tex, resources, new float[] {1.0f, 0.0f, 0.0f, 1.0f},R.raw.tex1);
		Mesh meshRuraZDziura = Mesh.getMesh(R.raw.rura_srodek_high_tex, resources, new float[] {1.0f, 0.0f, 0.0f, 1.0f},R.raw.tex1);
		Mesh meshRuraPusta = Mesh.getMesh(R.raw.rura_pusta_high_tex, resources, new float[] {1.0f, 0.0f, 0.0f, 1.0f},R.raw.tex1);
		Mesh meshRuraPusta2 = Mesh.getMesh(R.raw.rura_pusta2_high_tex, resources, new float[] {1.0f, 0.0f, 0.0f, 1.0f},R.raw.tex1);
		Mesh meshRuraPusta3 = Mesh.getMesh(R.raw.rura_pusta3_high_tex, resources, new float[] {1.0f, 0.0f, 0.0f, 1.0f},R.raw.tex2);
		Mesh meshRuraWylot = Mesh.getMesh(R.raw.rura_wylot, resources, new float[] {1.0f, 0.0f, 0.0f, 1.0f},R.raw.tex3);
		Mesh meshRuraWlot = Mesh.getMesh(R.raw.rura_wlot, resources, new float[] {1.0f, 0.0f, 0.0f, 1.0f},R.raw.tex3);
		Mesh meshNoMesh = Mesh.getMesh(R.raw.rura_no_mesh, resources, new float[] {1.0f, 0.0f, 0.0f, 1.0f},R.raw.tex3);
		Mesh meshMeteorytyPolowka = Mesh.getMesh(R.raw.meteoryty_polowka, resources, new float[] {1.0f, 0.0f, 0.0f, 1.0f},R.raw.tex4);
		Mesh meshMeteorytyPolowka2 = Mesh.getMesh(R.raw.meteor_polowka2, resources, new float[] {1.0f, 0.0f, 0.0f, 1.0f},R.raw.tex4);
		Mesh meshMeteorytyPolowka3 = Mesh.getMesh(R.raw.meteor_polowka3, resources, new float[] {1.0f, 0.0f, 0.0f, 1.0f},R.raw.tex4);
		Mesh meshRuraPogieta = Mesh.getMesh(R.raw.rara_pogieta, resources, new float[] {1.0f, 0.0f, 0.0f, 1.0f},R.raw.tex1);
		
		Mesh meshMeteorytDziura = Mesh.getMeshSerialized(R.raw.ser_meteor_dziura, resources, R.raw.tex4);
		
		Mesh meshMeteorytDziura22222222222 = Mesh.getMesh(R.raw.meteor_dziura, resources, new float[] {1.0f, 0.0f, 0.0f, 1.0f},R.raw.tex4);
		
		//Mesh meshMeteorytDziura = Mesh.getMesh(R.raw.meteor_dziura, resources, new float[] {1.0f, 0.0f, 0.0f, 1.0f},R.raw.tex4);
		Mesh meshResztkiDziura = Mesh.getMesh(R.raw.resztki_dziura, resources, new float[] {1.0f, 0.0f, 0.0f, 1.0f},R.raw.tex1);
		Mesh meshPanelPolowka = Mesh.getMesh(R.raw.panel_sloneczny_polowka, resources, new float[] {1.0f, 0.0f, 0.0f, 1.0f},R.raw.tex3);
		*/
		
		Random random = new Random();
		final float prawdopodobienstwoMonety = 0.8f;
		final float prawdopodobienstwoSpowolnienia= 0.002f;
		final float prawdopodobienstwoBonusu = 0.45f;
		
		float rotationCzlon = random.nextFloat()*360;
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
			
			int j=i-10;	
			
			
			if(j==1) {
				obstacles.add(new RuraPusta(meshRuraPusta2,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==2) {
				obstacles.add(new RuraPusta(meshRuraWylot,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==3) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==4) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==5) {
				obstacles.add(new RuraZDziura(meshResztkiDziura,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==6) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==7) {
				obstacles.add(new Rura1(meshMeteorytyPolowka3,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			
			if(j==8) {
				obstacles.add(new Rura1(meshMeteorytyPolowka,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==9) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==10) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==11) {
				obstacles.add(new Rura1(meshPanelPolowka,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==12) {
				obstacles.add(new Rura1(meshMeteorytyPolowka2,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==13) {
				obstacles.add(new Rura1(meshMeteorytyPolowka3,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==14) {
				obstacles.add(new Rura1(meshMeteorytyPolowka2,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==15) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==16) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==17) {
				obstacles.add(new RuraZDziura(meshMeteorytDziura,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==18) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==19) {
				obstacles.add(new Rura1(meshMeteorytyPolowka,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==20) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==21) {
				obstacles.add(new Rura1(meshMeteorytyPolowka,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==22) {
				obstacles.add(new Rura1(meshMeteorytyPolowka2,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==23) {
				obstacles.add(new Rura1(meshMeteorytyPolowka2,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==24) {
				obstacles.add(new Rura1(meshMeteorytyPolowka,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==25) {
				obstacles.add(new Rura1(meshMeteorytyPolowka2,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==26) {
				obstacles.add(new Rura1(meshMeteorytyPolowka3,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==27) {
				obstacles.add(new Rura1(meshMeteorytyPolowka3,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==28) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==29) {
				obstacles.add(new Rura1(meshMeteorytyPolowka2,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==28) {
				obstacles.add(new Rura1(meshMeteorytyPolowka,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==29) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==30) {
				obstacles.add(new RuraZDziura(meshResztkiDziura,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==31) {
				obstacles.add(new Rura1(meshMeteorytyPolowka2,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==32) {
				obstacles.add(new Rura1(meshMeteorytyPolowka3,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==33) {
				obstacles.add(new Rura1(meshMeteorytyPolowka3,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==34) {
				obstacles.add(new Rura1(meshMeteorytyPolowka2,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==35) {
				obstacles.add(new Rura1(meshMeteorytyPolowka,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==36) {
				obstacles.add(new Rura1(meshMeteorytyPolowka,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==37) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==38) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==39) {
				obstacles.add(new RuraZDziura(meshResztkiDziura,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==40) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==41) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==42) {
				obstacles.add(new Rura1(meshMeteorytyPolowka,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==43) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==44) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==45) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==46) {
				obstacles.add(new RuraZDziura(meshResztkiDziura,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==47) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==48) {
				obstacles.add(new RuraPusta(meshNoMesh,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j==49) {
				obstacles.add(new RuraPusta(meshRuraWlot,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			
			if(j==90) {
				obstacles.add(new RuraPusta(meshRuraWylot,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j>90 && j < 140) {
				if(rand<0.20)
					obstacles.add(new Rura1(meshMeteorytyPolowka,rotation));
				//else if(rand<0.66)
				else if(rand<0.30)
					obstacles.add(new Rura1(meshMeteorytyPolowka2,rotation));
				else if(rand<0.50)
					obstacles.add(new Rura1(meshMeteorytyPolowka3,rotation));
				else if(rand<0.61)
					obstacles.add(new RuraZDziura(meshResztkiDziura,rotation));
				else if(rand<0.76)
					obstacles.add(new RuraZDziura(meshMeteorytDziura,rotation));
				else if(rand<0.91)
					obstacles.add(new Rura1(meshPanelPolowka,rotation));
				else if(rand<1.0)
					obstacles.add(new RuraPusta(meshNoMesh,rotation));
				
				hitedObstacles[j] = false;
				continue;
			}
			if(j==140) {
				obstacles.add(new RuraPusta(meshRuraWlot,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			
			if(j==240) {
				obstacles.add(new RuraPusta(meshRuraWylot,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			if(j>240 && j < 340) {
				if(rand<0.20)
					obstacles.add(new Rura1(meshMeteorytyPolowka,rotation));
				//else if(rand<0.66)
				else if(rand<0.30)
					obstacles.add(new Rura1(meshMeteorytyPolowka2,rotation));
				else if(rand<0.50)
					obstacles.add(new Rura1(meshMeteorytyPolowka3,rotation));
				else if(rand<0.61)
					obstacles.add(new RuraZDziura(meshResztkiDziura,rotation));
				else if(rand<0.76)
					obstacles.add(new RuraZDziura(meshMeteorytDziura,rotation));
				else if(rand<0.91)
					obstacles.add(new Rura1(meshPanelPolowka,rotation));
				else if(rand<1.0)
					obstacles.add(new RuraPusta(meshNoMesh,rotation));
				
				hitedObstacles[j] = false;
				continue;
			}
			if(j==340) {
				obstacles.add(new RuraPusta(meshRuraWlot,rotation));
				hitedObstacles[j] = false;
				 continue;
			}
			
			
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
}
