package org.author.game;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

public class ModelPart {
	
	private ArrayList<Point> points = new ArrayList<Point>();
	private String name;
	private Polygon polygon;
	private Color color = Color.BLUE;
	
	public ModelPart(String name, Polygon p){
		this.polygon = p;
		for(int i = 0; i < p.npoints; ++i){
			points.add(new Point(p.xpoints[i], p.ypoints[i]));
		}
	}

	public void scalePart(int x) {
		for(int i = 0; i < polygon.npoints; ++i){
			polygon.xpoints[i] *= x;
			polygon.ypoints[i] *= x;
		}
		updatePoints();
	}
	
	public void updatePoints(){
		points.clear();
		for(int i = 0; i < polygon.npoints; ++i){
			points.add(new Point(polygon.xpoints[i], polygon.ypoints[i]));
		}
	}
	
	public Color getColor(){
		return color;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}
}
