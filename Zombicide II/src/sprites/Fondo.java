package sprites;

import java.awt.Graphics;
import java.awt.Image;
import util.Assets;
import util.Constant;

public class Fondo {
	private int x = 0;
	private int y= 0;
	
	
	private Image fondo = Assets.iifondo.getImage();

	public Fondo() {
	}

	public void draw(Graphics g) {
		g.drawImage(fondo, 0, 0, Constant.WIDTHSCREEN, Constant.HEIGHTSCREEN, 
				x, y, x + Constant.WIDTHSCREEN, y+Constant.HEIGHTSCREEN, null);
	}

	public void update() {
		
	}

}
