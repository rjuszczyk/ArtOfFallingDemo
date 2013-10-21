package com.swbgames.artoffalling.main;

public class GameSelector {
	private GameModel gameModel;
	private GameModel[] gameModels;
	public GameSelector(GameModel[] gms) {
		this.gameModel = gms[0];
		this.gameModels = gms;
	}
	public void setGameModel(GameModel gm) {
		this.gameModel = gm;
	}
	public void setGameModel(int i){
		setGameModel(gameModels[i]);
		this.gameModel.reset();
	}
	public GameModel getGameModel() {
		return this.gameModel;
	}
	public GameModel getGameModel(int i) {
		return this.gameModels[i];
	}
}
