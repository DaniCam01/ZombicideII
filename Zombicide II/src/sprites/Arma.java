package sprites;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import util.Assets;

public class Arma {
	public int x;
	protected int y;
	public Image arma;
	private int rowFrame;
	protected int ANCHO;
	protected int ALTO;
	private int columnframe;

	public Arma() {
		x= -200;
		y= 855;
		columnframe = 0;
		ANCHO = Assets.iiarma0.getIconWidth();
		ALTO = Assets.iiarma0.getIconHeight();
	}
	

	public void draw(Graphics g) {
		int frameX = columnframe * ANCHO;
		int frameY = rowFrame * ALTO;
		g.drawImage(arma, x, y, x + ANCHO, y + ALTO, frameX, frameY, frameX + ANCHO, frameY + ALTO, null);
	}
	
	
}
