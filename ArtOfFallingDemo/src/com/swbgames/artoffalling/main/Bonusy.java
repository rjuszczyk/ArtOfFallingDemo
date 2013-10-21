package com.swbgames.artoffalling.main;

public class Bonusy {
	public static final int BLUE = 0;
	public static final int RED = 1;
	public static final int GREEN = 2;
	public static final int SPECIAL = 3;
	
	
	int blue = 0;
	int red = 0;
	int green = 0;
	int special = 0;
	
	GameModel gameModel;
	
	public Bonusy(GameModel gameModel) {
		this.gameModel = gameModel;
	}
	public void reset() {
		blue = 0;
		red = 0;
		green = 0;
		special = 0;
	}
	public int getCount() {
		if(blue>0)
			return blue;
		if(green>0)
			return green;
		if(red>0)
			return red;
		
			return special;
	}
	public void collectBonus(int bonus) {
		switch(bonus){
		case BLUE: 
			blue++;
			if(blue==3) {
				bonusBlue();
				blue=0;
			}
			red=0;
			green=0;
			special=0;
			break;
			
		case RED:
			red++;
			if(red==3) {
				bonusRed();
				red=0;
			}
			blue=0;
			green=0;
			special=0;
			break;
			
		case GREEN:
			green++;
			if(green==3) {
				bonusGreen();
				green=0;
			}
			red=0;
			blue=0;
			special=0;
			break;
			
		case SPECIAL:
			special++;
			if(special==3) {
				bonusSpecial();
				special=0;
			}
			red=0;
			green=0;
			blue=0;
			break;
		}
	
	}
	
	void bonusBlue(){
		gameModel.incScore(25);
		//TODO: dzwiek i ?napis?
	}
	void bonusRed() {
		gameModel.speed = 0.0035f;
	}
	void bonusGreen() {
		gameModel.changeIncPointsFactor(2);
	}
	void bonusSpecial() {
		gameModel.iloscZyc++;
	}
	
}
