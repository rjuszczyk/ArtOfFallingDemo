package com.swbgames.states;

import android.util.Log;


public class StateMachine {
	public AppState currentAppState = null;
	
	public GameState gameState;
	public NotStartedState notStartedState;
	public LoseState loseState;
	public PauseState pauseState;
	public LoadingState loadingMeshesState;
	public ClassicGameSelectState classicGameSelectState;
	public NewLevelUnlockedState newLevelUnlockedState;
	public WinnerState winnerState;
	
	public static final int GAME_STATE = 1;
	public static final int NOT_STARTED_STATE = 2;
	public static final int PAUSE_STATE = 3;
	public static final int LOSE_STATE = 4;
	public static final int LOADING_MESHES_STATE = 5;
	public static final int CLASSIC_GAME_STATE = 6;
	public static final int NEW_LEVEL_UNLOCKED = 7;
	public static final int WINNER_STATE = 8;
	public boolean ready = false;
	public int getCurrentStateID() {
		if(this.currentAppState == null) return -1;
		return this.currentAppState.getState();
	}
	public void setState(int id) {
		if(this.currentAppState != null)
		this.currentAppState.onEnd();
		
		switch(id) {
		case GAME_STATE: 		this.currentAppState = gameState; 		break;
		case NOT_STARTED_STATE: this.currentAppState = notStartedState;	break;
		case PAUSE_STATE:		this.currentAppState = pauseState;		break;
		case LOSE_STATE: 		this.currentAppState = loseState;		break;
		case LOADING_MESHES_STATE: 		this.currentAppState = loadingMeshesState;		break;
		case CLASSIC_GAME_STATE: 		this.currentAppState = this.classicGameSelectState; break;
		case NEW_LEVEL_UNLOCKED: this.currentAppState = this.newLevelUnlockedState; break;
		case WINNER_STATE: this.currentAppState = this.winnerState; break;
		default: 				this.currentAppState = notStartedState;	break;
		}
		this.currentAppState.onStart();
	}
	
	public StateMachine() {}
	public StateMachine(int state) {
		this();
		setState(state);
	}
	public int firstState = LOADING_MESHES_STATE;
	public void init(GameState gameState_, NotStartedState notStartedState_, LoseState loseState_, PauseState pauseState_, LoadingState loadingMeshesState_, ClassicGameSelectState _classicGameSelectState,
			NewLevelUnlockedState newLevelUnlockedState_, WinnerState winnerState_) {
		this.gameState = gameState_;
		this.notStartedState = notStartedState_;
		this.loseState = loseState_;
		this.pauseState = pauseState_;
		this.loadingMeshesState = loadingMeshesState_;
		this.classicGameSelectState = _classicGameSelectState;
		this.newLevelUnlockedState = newLevelUnlockedState_;
		this.winnerState = winnerState_;
		
		setState(firstState);
		ready = true;
		//this.setState(PAUSE_STATE);
	}
	
	
	public void draw() {
		//Log.e("debug2", "drawing state="+this.getCurrentStateID());
		currentAppState.draw();
	}
	public void onClick(float x, float y) {
		currentAppState.onClick(x, y);
	}
}
