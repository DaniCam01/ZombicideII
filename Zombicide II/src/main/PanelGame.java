package main;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import engine.GameLoop;
import input.InputHandler;
import model.GameData;
import model.Movimiento;
import sprites.Arma;
import sprites.Botiquin;
import sprites.Disparo;
import sprites.Fondo;
import sprites.Player;
import sprites.Zombie;
import sprites.ZombieBig;
import util.Assets;
import util.Constant;

@SuppressWarnings("serial")
public class PanelGame extends JPanel {

	public boolean isRunning = true;
	public boolean isPause = false;
	private InputHandler input;

	public GameLoop gameLoop;
	//private Player player;
	private Fondo fondo;
	private int moverplayer;
	private final static int VECESMOVERPLAYER = 6;
	private int ronda;
	private int vidas;
	public GameData gameData;
	private ArrayList<Player> players;
	private ArrayList<Disparo> disparos;
	private ArrayList<Zombie> zombies;
	private ArrayList<ZombieBig> zombiesbig;
	private ArrayList<Botiquin> botiquines;
	private Botiquin botiquin;
	private int njugador;
	private int vecesdisparo = 20;
	private Disparo disparo;
	//private ArrayList<Disparo> disparos;
	private int nacedisparo;
	private int x, y, pjvivos;
	private AudioClip shotsound;
	private AudioClip bsosound;
	private AudioClip evillaught;
	private AudioClip endsound;
	private boolean endsound1 = false;
	private AudioClip healsound;
	private Player player;
	private AudioClip deadsound;
	private int nuevacompra;
	private AudioClip soundcash;
	private Arma arma;
	
	public PanelGame(int njugador) {
		super();
		this.njugador = njugador;
		input = new InputHandler(this);
		// create actores iniciales
		Assets.loadAssets();
		fondo = new Fondo();
		arma = new Arma();
		//player = new Player();
		//posicionjugador = player.getX();
		players = new ArrayList<Player>();
		disparos = new ArrayList<Disparo>();
		zombies = new ArrayList<Zombie>();
		botiquines = new ArrayList<Botiquin>();
		zombiesbig = new ArrayList<ZombieBig>();
		loadSound();
		evillaught.play();
		bsosound.loop();
	}
	
	void update(GameData gameData2) {
		this.gameData = gameData2;
		this.players = gameData.getPlayers();
		this.disparos = gameData.getDisparos();
		this.zombies = gameData.getZombies();
		this.vidas = gameData.getVidas();
		this.ronda = gameData.getRondas();
		this.botiquines = gameData.getBotis();
		this.zombiesbig = gameData.getZombiesbig();
		if(players.size()>0) {
		this.player = players.get(njugador-1);
		x = player.x;
		y = player.y;
		}
		this.pjvivos = gameData.getPjvivos();
		
		
		// handle inputs
		
		if (input.isKeyDown(KeyEvent.VK_UP) && input.isKeyDown(KeyEvent.VK_RIGHT)) {
			ClientGame.enviar(new Movimiento(njugador, 1));
		}else if (input.isKeyDown(KeyEvent.VK_DOWN) && input.isKeyDown(KeyEvent.VK_RIGHT)) {
			ClientGame.enviar(new Movimiento(njugador, 3));
		}else if (input.isKeyDown(KeyEvent.VK_DOWN) && input.isKeyDown(KeyEvent.VK_LEFT)) {
			ClientGame.enviar(new Movimiento(njugador, 5));
		}else if (input.isKeyDown(KeyEvent.VK_UP) && input.isKeyDown(KeyEvent.VK_LEFT)) {
			ClientGame.enviar(new Movimiento(njugador, 7));
		}else if (input.isKeyDown(KeyEvent.VK_RIGHT)) {
			ClientGame.enviar(new Movimiento(njugador, 2));
		}else if (input.isKeyDown(KeyEvent.VK_LEFT)) {
			ClientGame.enviar(new Movimiento(njugador, 6));
		}else if (input.isKeyDown(KeyEvent.VK_UP)) {
			ClientGame.enviar(new Movimiento(njugador, 0));
		}else if (input.isKeyDown(KeyEvent.VK_DOWN)) {
			ClientGame.enviar(new Movimiento(njugador, 4));
		}
		
		
		if(nacedisparo!=0) {
			nacedisparo = ++nacedisparo % players.get(njugador-1).vecesdisparo;
			}
		if(nacedisparo==0 && input.isKeyDown(KeyEvent.VK_SPACE)) {
			nacedisparo = ++nacedisparo % players.get(njugador-1).vecesdisparo;
			if(players.get(njugador-1).arma==0) {
				ClientGame.enviar(new Movimiento(njugador, 9));
			}else if(players.get(njugador-1).municion>0) {
				ClientGame.enviar(new Movimiento(njugador, 9));
			}
		}
		
		
		if (input.isKeyDown(KeyEvent.VK_1)) {
			ClientGame.enviar(new Movimiento(njugador, 11));
		}else if (input.isKeyDown(KeyEvent.VK_2)) {
			ClientGame.enviar(new Movimiento(njugador, 12));
		}
		
		
		
		if(nuevacompra!=0) {
			nuevacompra = ++nuevacompra % 20;
			}
		if(nuevacompra==0 && input.isKeyDown(KeyEvent.VK_E)) {
			nuevacompra = ++nuevacompra % 20;
			ClientGame.enviar(new Movimiento(njugador, 10));
		}
		
		
		if(!input.isKeyDown(KeyEvent.VK_LEFT) && !input.isKeyDown(KeyEvent.VK_RIGHT) && !input.isKeyDown(KeyEvent.VK_UP) && !input.isKeyDown(KeyEvent.VK_DOWN)) {
			ClientGame.enviar(new Movimiento(njugador, 8));
		}
	
		
		if(gameData.isSounddisparo()) {
			shotsound.stop();
			shotsound.play();
		}
		

		if(gameData.isSoundheal()) {
			healsound.stop();
			healsound.play();
		}
		if(gameData.isSoundcash()) {
			soundcash.stop();
			soundcash.play();
		}
		if(gameData.isSoundevil()) {
			evillaught.stop();
			evillaught.play();
		}
		
		if(gameData.isSounddead()) {
			if(pjvivos<1 && !endsound1) {
				endsound1 = true;
				endsound.play();
			}else {
				deadsound.stop();
				deadsound.play();
			}
		}
	}
	

	
	

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		fondo.draw(g2d);
		if(!players.isEmpty()) {
			for(Player player : players) {
				if(player.vivo) {
					player.mario = Assets.iimario.getImage();
					player.draw(g2d);
				}
				
			}
		}
		if(!disparos.isEmpty()) {
			for(Disparo disparo : disparos) {
				disparo.disparo = Assets.iidisparo.getImage();
				disparo.draw(g2d);
			}
		}
		if(!botiquines.isEmpty()) {
			for(Botiquin botiquin : botiquines) {
				botiquin.botiquin = Assets.iifa.getImage();
				botiquin.draw(g2d);
			}
		}
		if(!zombies.isEmpty()) {
			for(Zombie zombie : zombies) {
				zombie.mario = Assets.iizombie.getImage();
				zombie.draw(g2d);
			}
		}
		if(!zombiesbig.isEmpty()) {
			for(ZombieBig zombieb : zombiesbig) {
				zombieb.mario = Assets.iizombiebig.getImage();
				zombieb.draw(g2d);
			}
		}
		if(!players.isEmpty() && pjvivos>0) {
			if(players.get(njugador-1).arma==0) {
				arma.arma = Assets.iiarma0.getImage();
			}else if(players.get(njugador-1).arma==1) {
				arma.arma = Assets.iiarma1.getImage();
			}else if(players.get(njugador-1).arma==2) {
				arma.arma = Assets.iiarma2.getImage();
			}
			arma.draw(g2d);
		}
		
		if(!players.isEmpty()) {
			drawScore(g2d);
			drawCursor(g2d);
		}
	}
	
	private void drawCursor(Graphics2D g) {
		if(players.size()>0 && players.get(njugador-1).vivo) {
			if (players.get(njugador-1).rowFrame == 1) {
				g.drawString("▼", x+100, y);
			}else if (input.isKeyDown(KeyEvent.VK_UP) || input.isKeyDown(KeyEvent.VK_DOWN)) {
				g.drawString("▼", x+80, y);
			}else{
				g.drawString("▼", x+50, y);
			}
		}
		
	}

	private void drawScore(Graphics2D g) {
		
		g.setColor(Color.decode("#3D0000"));
		if(players.size()>0 && pjvivos<1) {
			g.setFont(new Font("Impact", Font.BOLD, 200)); 
			g.drawString("GAME OVER", 470, 600);
			g.setFont(new Font("Impact", Font.BOLD, 50));
			g.drawString("RONDA: "+ronda, 850, 680);
			g.setFont(new Font("Impact", Font.BOLD, 20));
			g.drawString("DANICM", 30, 1050);
			
		}else {
			g.setFont(new Font("Impact", Font.BOLD, 30)); 
			g.drawString("VIDAS: "+vidas, 200, 55);
			g.drawString("RONDA: "+ronda, 30, 55);
			if(players.get(njugador-1).arma==0) {
				g.drawString("MUNICION: ", 30, 1050);
				g.setFont(new Font("Impact", Font.BOLD, 50));
				g.drawString("∞", 185, 1055);
			}else {
				g.drawString("MUNICION: "+players.get(njugador-1).municion, 30, 1050);
			}
		}
		
		int margen = 0;
		for(Player player : players) {
			g.setFont(new Font("Impact", Font.BOLD, 20)); 
			g.drawString(player.nick.toUpperCase()+":   "+player.puntos, 1760,55+margen);
			margen += 30;
		}
		
	}
	
	private void loadSound() {
		try {
			shotsound = Applet.newAudioClip(new File("assets/disparosound.wav").toURI().toURL());
			bsosound = Applet.newAudioClip(new File("assets/bsosound.wav").toURI().toURL());
			evillaught = Applet.newAudioClip(new File("assets/evillaught.wav").toURI().toURL());
			endsound = Applet.newAudioClip(new File("assets/endsound.wav").toURI().toURL());
			healsound = Applet.newAudioClip(new File("assets/healsound.wav").toURI().toURL());
			deadsound = Applet.newAudioClip(new File("assets/kaboomsound.wav").toURI().toURL());
			soundcash = Applet.newAudioClip(new File("assets/soundcash.wav").toURI().toURL());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
