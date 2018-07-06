package org.author.game;

import java.util.ArrayList;

public class PersistentValue {
	
	static ArrayList<PersistentValue> vars = new ArrayList<PersistentValue>();
	
	String tag, sVal;
	int nVal;
	boolean eternal = false;
	
	public PersistentValue(String tag, int nVal, String sVal){
		this.tag = tag;
		this.nVal = nVal;
		this.sVal = sVal;
	}
	
	public static void modNumbervalByTag(String tag, int mod){
		for(PersistentValue v : vars){
			if(v.tag.equalsIgnoreCase(tag)){
				v.nVal += mod;
			}
		}
	}
	
	public PersistentValue setLifetime(int l){
		this.eternal = (l == 1);
		return this;
	}
	
	public static void addPersistentValue(String t, int nVal){
		PersistentValue p = new PersistentValue(t, nVal, null);
		boolean canAdd = true;
		for(PersistentValue v : vars){
			if(v.tag.equalsIgnoreCase(p.tag)){
				canAdd = false;
			}
		}
		if(canAdd){
			vars.add(p);
		}else{
			modNumbervalByTag(p.tag, p.nVal);
		}
	}
	
	public static void addPersistentValue(String t, String sVal){
		PersistentValue p = new PersistentValue(t, 0, sVal);
		boolean canAdd = true;
		for(PersistentValue v : vars){
			if(v.tag.equalsIgnoreCase(p.tag)){
				canAdd = false;
			}
		}
		if(canAdd){
			vars.add(p);
		}else{
			modNumbervalByTag(p.tag, p.nVal);
		}
	}
	
	public static PersistentValue getPersistentValueByTag(String tag){
		for(PersistentValue v : vars){
			if(v.tag.equalsIgnoreCase(tag)){
				return v;
			}
		}
		return null;
	}
}
