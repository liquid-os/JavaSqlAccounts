package org.author.game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Filestream {
	
	public static Image getImageFromPath(String s)
	{
		if(Bank.isServer() || Util.autoServer){
			return null;
		}
		try {
			return ImageIO.read(Filestream.class.getResource(s));
		} catch (IOException e) {
			System.out.println("Image file not found @ "+s);
		}
		return null;
	}
	
    public static BufferedImage getScaledImage(String s, int w, int h)
    {
    	if(Bank.isServer() || Util.autoServer){
			return null;
		}
            try
            {
                    Image ii = Filestream.getImageFromPath(s);
                    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = (Graphics2D)bi.createGraphics();
                    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_SPEED));
                    boolean b = g2d.drawImage(ii, 0, 0, w, h, null);
                    return bi;
            }
            catch (Exception e)
            {
                    e.printStackTrace();
            }
            return null;
    }
    
    public static BufferedImage getScaledImage(Image ii, int w, int h)
    {
    	if(Bank.isServer() || Util.autoServer){
				return null;
		}
            try
            {
                    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = (Graphics2D)bi.createGraphics();
                    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_SPEED));
                    boolean b = g2d.drawImage(ii, 0, 0, w, h, null);
                    return bi;
            }
            catch (Exception e)
            {
                    e.printStackTrace();
            }
            return null;
    }
	
	public static BufferedImage getCardImage(String str){
		if(Bank.isServer() || Util.autoServer){
				return null;
		}
		if(str.contains("/")){
			BufferedImage img = null;
			try 
			{
			    img = ImageIO.read(new File(str));
			} 
			catch (IOException e) 
			{
			    e.printStackTrace();
			}
			return getScaledImage(img, 80, 80);
		}
		return getScaledImage(str, 80, 80);
		/*
		int cw = Properties.width/6;
		int ch = (int) (cw * 1.5);
		BufferedImage img = null;
		try {
			img =  ImageIO.read(Filestream.class.getResource(s));
		} catch (IOException e) {
			System.out.println("Image file not found @ "+s);
		}
		if(cw > img.getWidth())cw = img.getWidth();
		if(ch > img.getHeight())ch = img.getHeight();
		int x = img.getWidth()/2-cw/2;
		int y = img.getHeight()/2-ch/2;
		while(x < 0)++x;
		while(y < 0)++x;
		return img.getSubimage(img.getWidth()/2-cw/2, img.getHeight()/2-ch/2, cw, ch);
		String s = str;
		int cw = 256;
		int ch = cw;
		BufferedImage img = null;
		try {
			img =  ImageIO.read(Filestream.class.getResource(s));
		} catch (IOException e) {
			System.out.println("Image file not found @ "+s);
			return Bank.ball;
		}
		BufferedImage newImage = new BufferedImage(cw, ch, BufferedImage.TYPE_INT_RGB);

		Graphics g = newImage.createGraphics();
		g.drawImage(img, 0, 0, cw, ch, null);
		g.dispose();
		return newImage;*/
	}
	
	public static Image loadGif(final String url) {
		if(Bank.isServer() || Util.autoServer){
			return null;
		}
	    try {
	        final Toolkit tk = Toolkit.getDefaultToolkit();
	        final URL path = Filestream.class.getResource(url);
	        final Image img = tk.createImage(path);
	        tk.prepareImage(img, -1, -1, null);
	        return img;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	} 
	
	public boolean resExists(String file){
		boolean ret = false;
		if(Properties.resPack!=null){
			File f = new File(Bank.path+"resourcepacks/"+Properties.resPack+"/"+file);
			if(f.exists())ret = true;
		}
		return ret;
	}
	
	public String getResPath(){
		return Bank.path+"resourcepacks/"+Properties.resPack+"/";
	}
	
	public static BufferedImage toBufferedImage(Image img)
	{
		if(Bank.isServer() || Util.autoServer){
			return null;
		}
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
}
