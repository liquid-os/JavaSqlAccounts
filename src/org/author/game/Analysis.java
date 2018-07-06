package org.author.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Analysis {
	
	/*public static String getKey(File f, String name){
	    try{
	    	BufferedReader br = new BufferedReader(new FileReader(f));
	        String line = br.readLine();
	        while (line != null) {
	        	if(line.split("=")[0].equalsIgnoreCase(name)){
	        		return line.split("=")[1];
	        	}
	            line = br.readLine();
	        }
	    }catch(IOException e){
	    	e.printStackTrace();
	    }
	    return "null";
	}*/
	
	public static String getKey(File f, String name){
	    try{
	    	if(f.createNewFile()){return "-1";}else{
		    	BufferedReader br = new BufferedReader(new FileReader(f));
		        String line = br.readLine();
		    	if(line!=null){
			        String[] split = line.split(";");
			        for(int i = 0; i < split.length; ++i){
			        	if(split[i].split("=")[0].equalsIgnoreCase(name)){
			        		String s = split[i].split("=")[1];
			        		System.out.println("Found key, string is "+s+";");
			        		return s;
			        	}
			        }
		    	}
	    	}
	    }catch(IOException e){
	    	e.printStackTrace();
	    }
        setKey(f,name,"0");
	    return "-1";
	}
	
	public static int increment(File f, String key, int amt){
		int last = Integer.parseInt(getKey(f, key));
		if(last<0)last=0;
		last+=amt;
		setKey(f, key, last+"");
		return last;
	}
	
	public static void setKey(File f, String key, String val){
	    try{
	    	System.out.println("Looking for key "+key);
	    	BufferedReader br = new BufferedReader(new FileReader(f));
	        String line = br.readLine();
	    	BufferedWriter bw = new BufferedWriter(new FileWriter(f));
	        StringBuilder sb = new StringBuilder();
	        boolean newKey = true;
	        if(line!=null){
		        String[] spl = line.split(";");
		        for(int i = 0; i < spl.length; ++i){
		        	String s = spl[i];
		        	if(s.contains(key+"=")){
		        		s = key+"="+val;
		        		System.out.println("Found key, string is "+s+";");
		        		newKey = false;
		        	}
		        	sb.append(s+";");
		        }
	        }
	        if(newKey){
	        	sb.append(key+"="+val+";");
	        }
	        bw.write(sb.toString());
	        bw.close();
	    }catch(IOException e){
	    	e.printStackTrace();
	    }
	}
	
	public static void addKeyNoReplace(File f, String key, String val){
	    try{
	    	System.out.println("Looking for key "+key);
	    	BufferedReader br = new BufferedReader(new FileReader(f));
	        String line = br.readLine();
	    	BufferedWriter bw = new BufferedWriter(new FileWriter(f));
	        StringBuilder sb = new StringBuilder();
	        boolean newKey = true;
	        if(line!=null){
		        String[] spl = line.split(";");
		        for(int i = 0; i < spl.length; ++i){
		        	String s = spl[i];
		        	if(s.contains(key+"=")){
		        		System.out.println("Found key, string is "+s+";");
		        		newKey = false;
		        	}
		        }
	        }
	        if(newKey){
	        	sb.append(key+"="+val+";");
	        }
	        bw.write(line+sb.toString());
	        bw.close();
	    }catch(IOException e){
	    	e.printStackTrace();
	    }
	}
}
