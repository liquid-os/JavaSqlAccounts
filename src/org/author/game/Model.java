package org.author.game;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

public class Model {
	
	private ArrayList<ModelPart> parts = new ArrayList<ModelPart>();

	public void scaleModel(int x){
		for(ModelPart part : parts){
			part.scalePart(x);
		}
	}
	
	public Polygon generateDetailedHitbox(){
		Polygon poly = new Polygon();
		for(ModelPart part : parts){
			for(Point p : part.getPoints()){
				poly.addPoint(p.x, p.y);
			}
		}
		return poly;
	}
}
