package sprites;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.Serializable;

import util.Assets;
import util.Constant;

public class Mesa implements Serializable  {
	private static final long serialVersionUID = 1L;
	public int x;
	public int y;
	public int ANCHO = 207;
	public int ALTO = 303;
	//private int speed;

	public Mesa(int x, int y, int alto, int ancho) {  
		this.ALTO = alto;
		this.ANCHO = ancho;
		this.x = x;
		this.y = y;
	}
	
	
	public boolean colisionMesas(Mesa bola) {
		Rectangle recZombie = new Rectangle(x , y, ANCHO, ALTO);
		Rectangle recBola = new Rectangle(bola.x, bola.y, bola.ANCHO, bola.ALTO );
		return recZombie.intersects(recBola);
	}

}
