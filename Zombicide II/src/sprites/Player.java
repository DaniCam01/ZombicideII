package sprites;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.Serializable;

import util.Assets;
import util.Constant;

public class Player implements Serializable  {
	private static final long serialVersionUID = 1L;
	public int x;
	public int y;
	public Image mario;
	public int rowFrame;
	public int ANCHO;
	public int ALTO;
	private final static int COLUMNAS = 2;
	private final static int FILAS = 16;
	protected int limitd = util.Constant.WIDTHSCREEN/2+100;
	protected int limiti = util.Constant.WIDTHSCREEN/2-100;
	private int columnframe;
	public boolean limite = false;
	private boolean derecha = true, arriba = false;
	private boolean estatico = true;
	public int direccion = 2;
	private int xb=806, yb=228;
	private int velocidad = 10; //5
	public boolean vivo = true;
	public int puntos=0;
	public String nick;
	public boolean dosarmas;
	public int vecesdisparo=20, arma=0, municion=0, arma2=0, mejora0=-1, mejora1=-1, mejora2=-1;
	//private int speed;

	public Player() {  
		columnframe = 1;
		ANCHO = Assets.iimario.getIconWidth() / COLUMNAS;
		ALTO = Assets.iimario.getIconHeight() / FILAS;
		//mario = Assets.iimario.getImage();
		estatico();
	}
	public void left(){
		if(vivo) {
		estatico = false;
		rowFrame = 1;
		derecha=false;
		direccion = 6;
			xb = x;
			x-=velocidad;
		comprobarLimite();
		}
		
	}
	
	public void right(){
		if(vivo) {
		estatico = false;
		rowFrame = 0;
		derecha = true;
		direccion = 2;
		xb = x;
			x+=velocidad;
		comprobarLimite();
		}
	}
	public void up(){
		if(vivo) {
		estatico = false;
		arriba = true;
		direccion = 0;
		rowFrame = 2;
		yb = y;
			y-=velocidad;
		comprobarLimite();
		}
	}
	public void down(){
		if(vivo) {
		estatico = false;
		arriba = false;
		rowFrame = 3;
		direccion = 4;
		yb = y;
			y+=velocidad;
			comprobarLimite();
		}
	}
	
	public void estatico(){
		if(!estatico) {
			rowFrame+=4;
			estatico = true;
		}
	}
	
	public void diagonal() {
		if(vivo) {
			if(derecha && arriba) {
				rowFrame = 10;
				direccion = 1;
				x-=velocidad/3; y+=velocidad/3;
			}else if(derecha && !arriba) {
				rowFrame = 8;
				direccion = 3;
				x-=velocidad/3; y-=velocidad/3;
			}else if(!derecha && arriba) {
				rowFrame = 9;
				direccion = 7;
				x+=velocidad/3; y+=velocidad/3;
			}else if(!derecha && !arriba) {
				rowFrame = 11;
				direccion = 5;
				x+=velocidad/3; y-=velocidad/3;
			}
		}
	}
	
	public void comprobarLimite() {
		if(x<0) {
			x=0;
		}
		if(x > Constant.WIDTHSCREEN-ANCHO) {
			x= Constant.WIDTHSCREEN-ANCHO;
		}
		if(y<0) {
			y=0;
		}
		if(y > Constant.HEIGHTSCREEN-ALTO) {
			y=Constant.HEIGHTSCREEN-ALTO;
		}
	}
	
	public void block() {
		x = xb; y = yb;
	}
	

	public boolean colisionMesas(Mesa mesa) {
		Rectangle recPlayer = new Rectangle(x+50 , y+50, ANCHO-50, ALTO-50);
		Rectangle recMesa = new Rectangle(mesa.x, mesa.y, mesa.ANCHO, mesa.ALTO );
		return recPlayer.intersects(recMesa);
	}
	
	public boolean colisionMaquinas(Maquina maquina) {
		Rectangle recPlayer = new Rectangle(x+50 , y+50, ANCHO-50, ALTO-50);
		Rectangle recMesa = new Rectangle(maquina.x, maquina.y, maquina.ANCHO, maquina.ALTO );
		return recPlayer.intersects(recMesa);
	}
	
	public boolean colisionZombie(Zombie zombie) {
		Rectangle recPlayer = new Rectangle(x+50 , y+50, ANCHO-50, ALTO-50);
		Rectangle recZombie = new Rectangle(zombie.x, zombie.y, zombie.ANCHO, zombie.ALTO );
		return recPlayer.intersects(recZombie);
	}
	
	public boolean colisionZombieb(ZombieBig zombie) {
		Rectangle recPlayer = new Rectangle(x+50 , y+50, ANCHO-50, ALTO-50);
		Rectangle recZombie = new Rectangle(zombie.x, zombie.y, zombie.ANCHO, zombie.ALTO );
		return recPlayer.intersects(recZombie);
	}
	
	
	

	public void update() {
			columnframe = ++columnframe % 2;
	}

	public void draw(Graphics g) {
		int frameX = columnframe * ANCHO;
		int frameY = rowFrame * ALTO;
		g.drawImage(mario, x, y, x + ANCHO, y + ALTO, frameX, frameY, frameX + ANCHO, frameY + ALTO, null);
	}
	
	public int getX() {
		return x;
	}
	
	public boolean getDerecha() {
		return derecha;
	}
	
	public int getAncho() {
		return ANCHO;
	}
	
	

}
