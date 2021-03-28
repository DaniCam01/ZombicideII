package sprites;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import util.Assets;

public class Botiquin {
	public int x;
	protected int y;
	public Image botiquin;
	private int rowFrame;
	protected int ANCHO;
	protected int ALTO;
	private int columnframe;
	private Player personaje;
	private boolean derecha;
	private int posicionjugador;

	public Botiquin(int x, int y) {
		this.x = x+40;
		this.y = y+40;
		columnframe = 0;
		ANCHO = Assets.iifa.getIconWidth();
		ALTO = Assets.iifa.getIconHeight();
	}
	

	public void draw(Graphics g) {
		int frameX = columnframe * ANCHO;
		int frameY = rowFrame * ALTO;
		g.drawImage(botiquin, x, y, x + ANCHO, y + ALTO, frameX, frameY, frameX + ANCHO, frameY + ALTO, null);
	}
	
	public boolean colision(Player player) {
		Rectangle recBotiquin = new Rectangle(x , y, ANCHO, ALTO);
		Rectangle recPlayer = new Rectangle(player.x, player.y, player.ANCHO, player.ALTO );
		return recBotiquin.intersects(recPlayer);
	}
}
