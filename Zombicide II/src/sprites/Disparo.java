package sprites;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.Serializable;

import util.Assets;

public class Disparo implements Serializable{
	public int x;
	public int y;
	public Image disparo;
	private int rowFrame;
	protected int ANCHO;
	protected int ALTO;
	private final static int COLUMNAS = 1;
	private final static int FILAS = 8;
	private int columnframe;
	private Player personaje;
	private int direccion;
	private int posicionjugador;
	public Player player;
	//private int speed;

	public Disparo(Player player) {
		this.player = player;
		this.direccion = player.direccion;
		this.y = player.y;
		this.x = player.x;
		
		switch (direccion) {
		case 0:
			rowFrame = 0;
			x +=player.ANCHO/2-15;
			break;
		case 1:
			x+=player.ANCHO/2+35;
			y+=25;
			rowFrame = 1;
			break;
		case 2:
			y+=player.ALTO/2-15;
			x+=player.ANCHO/2+55;
			rowFrame = 2;
			break;
		case 3:
			y+=player.ALTO-45;
			x+=player.ANCHO-45;
			rowFrame = 3;
			break;
		case 4:
			rowFrame = 7;
			x +=player.ANCHO/2-15;
			y+=player.ALTO-10;
			break;
		case 5:
			rowFrame = 6;
			y+=player.ALTO-25;
			break;
		case 6:
			rowFrame = 5;
			y+=player.ALTO/2-15;
			x-=15;
			break;
		case 7:
			x+=10;
			y+=25;
			rowFrame = 4;
			break;
		}
		
		columnframe = 0;
		ANCHO = Assets.iidisparo.getIconWidth()/COLUMNAS;
		ALTO = Assets.iidisparo.getIconHeight()/FILAS;
		//disparo = Assets.iidisparo.getImage();
	}

	

	public void update() {
		switch (direccion) {
		case 0:
			y-=30;
			break;
		case 1:
			y-=15;
			x+=15;
			break;
		case 2:
			x+=30;
			break;
		case 3:
			x+=15;
			y+=15;
			break;
		case 4:
			y+=30;
			break;
		case 5:
			y+=15;
			x-=15;
			break;
		case 6:
			x-=30;
			break;
		case 7:
			y-=15;
			x-=15;
			break;
		}
	}

	public void draw(Graphics g) {
		int frameX = columnframe * ANCHO;
		int frameY = rowFrame * ALTO;
		g.drawImage(disparo, x, y, x + ANCHO, y + ALTO, frameX, frameY, frameX + ANCHO, frameY + ALTO, null);
	}
	
	public boolean colision(Player bola) {
		Rectangle recZombie = new Rectangle(x , y, ANCHO, ALTO);
		Rectangle recBola = new Rectangle(bola.x, bola.y, bola.ANCHO, bola.ALTO );
		return recZombie.intersects(recBola);
	}



	public void retrasar() {
		switch (direccion) {
		case 0:
			y-=35;
			break;
		case 1:
			y-=20;
			x+=20;
			break;
		case 2:
			x+=35;
			break;
		case 3:
			x+=20;
			y+=20;
			break;
		case 4:
			y+=35;
			break;
		case 5:
			y+=20;
			x-=20;
			break;
		case 6:
			x-=35;
			break;
		case 7:
			y-=20;
			x-=20;
			break;
		}
	}
}
