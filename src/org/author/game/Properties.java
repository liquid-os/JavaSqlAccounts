package org.author.game;

import java.awt.Toolkit;
import java.util.ArrayList;

public class Properties {
	static byte PACK_TYPE_GAME = 0, PACK_TYPE_MAPEDITOR = 1, PACK_TYPE_DEMO = 2;
	public static final int defaultCamOffset = -64;
	static int width = 1200, baseWidth = 1200, height = 900, baseHeight = 900;
	static float gravity = 9.8F;
	static int framerate = 80, port = 8553, selHeroID = 0;
	static String gameName = ".terroth";
	static double ver = 0.1;
	static String gameTitle = "Pre-Alpha";
	public static float masterVol = 0F;
	public static String map = null;
	public static boolean legal = true, beta = false;
	public static String username = "username";
	static PanelBase startPanel = new PanelLogin();
	static byte packingType = PACK_TYPE_GAME;
	static String resPack = "";
	static String fileExt = ".PNG";
	public static int getWidth() {
		return width;
	}
	
	public static int getHeight() {
		return height;
	}
}
