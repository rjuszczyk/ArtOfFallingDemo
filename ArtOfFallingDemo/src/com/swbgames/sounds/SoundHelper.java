package com.swbgames.sounds;

import java.util.HashMap;

import com.swbgames.states.StateMachine;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.Log;
import android.util.SparseArray;

public class SoundHelper {
	private SoundPool soundPool;
	private int soundID;
	private Context context;
	boolean loaded = false;
	private SparseArray<Integer> resources_map = new SparseArray<Integer>();
	
	
	public SoundHelper(Context context) {
		this.context=context; 
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	    soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
	      @Override
	      public void onLoadComplete(SoundPool soundPool, int sampleId,
	          int status) {
	        loaded = true;
	      }
	    });
	}
	public void load(int rID) {
		loaded = false;
		
		
		int id = soundPool.load(context, rID, 1);
		
		resources_map.put(rID, id);
	}
	public void play(final int id) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if(resources_map.get(id)==null) {
					load(id);
					Log.i("sounds", "loading");
				}
				
				int r_id = resources_map.get(id);
				
				AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			    float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			    float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			    
			    float volume = actualVolume / maxVolume;
			   
			    if(loaded)
			    	soundPool.play(r_id, volume, volume, 1, 0, 1f);
			}
			
		});
		t.start();
		
	}
	public void stop(int id) {
		if(resources_map.get(id) == null )return;
		int r_id = resources_map.get(id);
		soundPool.stop(r_id);
	}
}
