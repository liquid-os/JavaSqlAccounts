package org.author.game;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class ClipShell {
	
    Clip clip;
    AudioInputStream inputStream;
    boolean playing = false;
    String name = null;
    float vol, hasVol = 0;
	
	public ClipShell(String path){
        try {
        	try {
				name = "cardsounds/"+path;
				clip = AudioSystem.getClip();
				if(path.contains("/")||path.contains("\\")){
					inputStream = AudioSystem.getAudioInputStream(new File(name));
				}else
					inputStream = AudioSystem.getAudioInputStream(Filestream.class.getResource(name));
        	}
			catch (Exception e) {
				e.printStackTrace();
			}
		}finally{}
	}
	
	public ClipShell(String subfolder, String path){
        try {
        	try {
				name = (subfolder!=null ? (subfolder+"/"+path) : "path");
				clip = AudioSystem.getClip();
				inputStream = AudioSystem.getAudioInputStream(Filestream.class.getResource(name));
        	}
			catch (Exception e) {
				e.printStackTrace();
			}
		}finally{}
	}
	
	public ClipShell(String subfolder, String path, float f){
        try {
        	try {
				clip = AudioSystem.getClip();
				name = (subfolder!=null ? (subfolder+"/"+path) : "path");
				inputStream = AudioSystem.getAudioInputStream(Filestream.class.getResource(name));
				hasVol = 1;
				vol = f+Properties.masterVol;
        	}
			catch (Exception e) {
				e.printStackTrace();
			}
		}finally{}
	}
	
	public void stop(){
	    try {
		    clip.stop(); 
			clip.close();
			playing = false;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loop(int i){
	    try {
			clip.open(inputStream);
			if(hasVol==1){
				FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				volume.setValue(vol);
			}
		    clip.loop(i);
		    playing = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start(){
		if(inputStream!=null){
		    try {
				clip.open(inputStream);
				if(hasVol==1){
					FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
					volume.setValue(vol);
				}
			    clip.start(); 
			    playing = true;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
