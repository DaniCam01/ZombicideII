package sprites;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.Serializable;

import util.Assets;
import util.Constant;

public class ZombieBig implements Serializable  {
	private static final long serialVersionUID = 1L;
	public int x;
	public int y;
	public Image mario;
	private int rowFrame;
	public int ANCHO;
	public int ALTO;
	private final static int COLUMNAS = 2;
	private final static int FILAS = 8;
	private int columnframe;
	public boolean limite = false;
	private boolean derecha = true, arriba = false;
	public int direccion = 2;
	private int xb , yb;
	private Player player;
	private int velocidad;
	public int contestaticoy=0, contestaticox=0;
	private boolean inmap=false;
	private int puerta, distancia, alturarandom;
	public int maxpaciencia=10;
	public int vida;
	//private int speed;

	public ZombieBig(Player player, int velocidad, int vida) {  
		this.player = player;
		this.velocidad = velocidad;
		this.vida = vida;
		columnframe = 1;
		ANCHO = Assets.iizombiebig.getIconWidth() / COLUMNAS;
		ALTO = Assets.iizombiebig.getIconHeight() / FILAS;
		//mario = Assets.iimario.getImage();
		puerta = (int) (Math.random()*4);
		distancia= (int) (Math.random()*1000);
		if(puerta==0) {
			x = -200-distancia;
			y = 700;
		}else if(puerta==1){
			x = 400+alturarandom;
			y = -200;
		}else if(puerta==2){
			x = 2032+distancia;
			y = 227;
		}else if(puerta==3){
			y = 1293+distancia;
			x = 1336;
			
		}
		xb = x;
		yb=y;
	}
	public void left(){
		rowFrame = 6;
		derecha=false;
		xb = x;
		x-=velocidad;
		if(x < player.x && contestaticox<10) {
			x = player.x;
		}
		contestaticoy = 0;
		comprobarLimite();
	}
	
	public void right(){
		rowFrame = 4;
		derecha = true;
		xb = x;
		x+=velocidad;
		if(x > player.x && contestaticox<10) {
			x = player.x;
		}
		contestaticoy = 0;
		comprobarLimite();
	}
	public void up(){
		arriba = true;
		rowFrame = 0;
		yb = y;
		y-=velocidad;
		if(y < player.y && contestaticoy<10) {
			y = player.y;
		}
		contestaticox = 0;
		comprobarLimite();
	}
	public void down(){
		arriba = false;
		rowFrame = 3;
		yb = y;
		y+=velocidad;
		if(y > player.y && contestaticoy<10) {
			y = player.y;
		}
		contestaticox = 0;
		comprobarLimite();
	}
	
	
	
	public void comprobarLimite() {
		if(inmap) {
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
	
	public boolean colisionDisparo(Disparo disparo) {
		Rectangle recZombie = new Rectangle(x , y, ANCHO, ALTO);
		Rectangle recBola = new Rectangle(disparo.x, disparo.y, disparo.ANCHO, disparo.ALTO );
		return recZombie.intersects(recBola);
	}
	
	public boolean entraMapa() {
		Rectangle recZombie = new Rectangle(x , y, ANCHO, ALTO);
		Rectangle recBola = new Rectangle(400, 400, Constant.WIDTHSCREEN-1000, Constant.HEIGHTSCREEN-1000);
		return recZombie.intersects(recBola);
	}
	
	
	

	public void update() {
			columnframe = ++columnframe % 2;
			//System.out.println("x = "+x+" y = "+y);
	}
	public void moove() {
		if(inmap) {
			if(x < player.x && y > player.y) {
				x-=velocidad/3; y+=velocidad/3;
				right(); up();
				rowFrame = 1;
			}else if(x < player.x && y < player.y) {
				x-=velocidad/3; y-=velocidad/3;
				right(); down();
				rowFrame = 5;
			}else if(x > player.x && y > player.y) {
				x+=velocidad/3; y+=velocidad/3;
				up(); left();
				rowFrame = 7;
			}else if(x > player.x && y < player.y) {
				x+=velocidad/3; y-=velocidad/3;
				down(); left();
				rowFrame = 2;
			}else if(x < player.x) {
				right();
			}else if(x > player.x) {
				left();
			}else if(y < player.y) {
				down();
			}else if(y > player.y) {
				up();
			}
			contestaticox = 0;
			contestaticoy = 0;
		} 
		if(!inmap){
			if(puerta==0) {
				right();
			}else if(puerta==1) {
				down();
			}else if(puerta==2) {
				left();
			}else if(puerta==3) {
				up();
			}
			
			if(entraMapa()) {
				inmap=true;
			}
		}
	}
	
	
	
	
	
	public void chocar(Mesa mesa) {
		boolean top = false,  bot = false, der = false, izq=false;
		x-=velocidad+5;
		if (!colisionMesas(mesa)) {
			izq = true;
		}
		x+=velocidad+5;
		y-=velocidad+5;
		 if (!colisionMesas(mesa)) {
			 top = true;
		}
		y+=velocidad+5;
		x+=velocidad+5;
		if (!colisionMesas(mesa)) {
			der = true;
		}
		x-=velocidad+5;
		y+=velocidad+5;
		 if (!colisionMesas(mesa)) {
			bot = true;
		}
		y-=velocidad+5;
		
		if(izq  || der) {
			if(y==player.y) {
				contestaticoy++;
				contestaticox=0;
			}
			if(contestaticoy== maxpaciencia) {
				if(y+(ALTO/2)>mesa.y+(mesa.ALTO/2)) {
					down();
				}else {
					up();
				}
			}else {
				if(y < player.y) {
					down();
					
				}else
					up();
				
				
				if(izq && player.x < x) {
					left();
				}
				if(der && player.x > x) {
					right();
				}
			}
		}else if(top  || bot) {
			if(x==player.x) {
				contestaticox++;
				contestaticoy = 0;
			}
			if(contestaticox == 10) {
				if(x+(ANCHO/2)>mesa.x+(mesa.ANCHO/2)) {
					right();
				}else {
					left();
				}
			}else { 
				if(x < player.x) {
					right();
				}else
					left();
			
				
				if(top && player.y < y) {
					up();
				}
				if(bot && player.y > y) {
					down();
				}
			}
		}	
	}
	
	public void chocar(Maquina maquina) {
		boolean top = false,  bot = false, der = false, izq=false;
		x-=velocidad+5;
		if (!colisionMaquinas(maquina)) {
			izq = true;
		}
		x+=velocidad+5;
		y-=velocidad+5;
		 if (!colisionMaquinas(maquina)) {
			 top = true;
		}
		y+=velocidad+5;
		x+=velocidad+5;
		if (!colisionMaquinas(maquina)) {
			der = true;
		}
		x-=velocidad+5;
		y+=velocidad+5;
		 if (!colisionMaquinas(maquina)) {
			bot = true;
		}
		y-=velocidad+5;
		
		if(izq  || der) {
			if(y==player.y) {
				contestaticoy++;
				contestaticox=0;
			}
			if(contestaticoy== 10) {
				if(y<Constant.HEIGHTSCREEN/2) {
					down();
				}else {
					up();
				}
			}else {
				if(y < player.y) {
					down();
					
				}else
					up();
				
				
				if(izq && player.x < x) {
					left();
				}
				if(der && player.x > x) {
					right();
				}
			}
		}else if(top  || bot) {
			if(x==player.x) {
				contestaticox++;
				contestaticoy = 0;
			}
			if(contestaticox == maxpaciencia) {
				if(x < Constant.WIDTHSCREEN) {
					right();
				}else {
					left();
				}
			}else { 
				if(x < player.x) {
					right();
				}else
					left();
			
				
				if(top && player.y < y) {
					up();
				}
				if(bot && player.y > y) {
					down();
				}
			}
		}	
	}
	
	
	
	

	public void draw(Graphics g) {
		int frameX = columnframe * ANCHO;
		int frameY = rowFrame * ALTO;
		g.drawImage(mario, x, y, x + ANCHO, y + ALTO, frameX, frameY, frameX + ANCHO, frameY + ALTO, null);
	}
	
	public int getX() {
		return x;
	}
	

}
