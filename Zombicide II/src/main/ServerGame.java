package main;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;

import engine.GameLoop;
import model.GameData;
import model.Movimiento;
import sprites.Botiquin;
import sprites.Disparo;
import sprites.Maquina;
import sprites.Mesa;
import sprites.Player;
import sprites.Zombie;
import sprites.ZombieBig;
import util.Assets;

/**
 * 
 * @author danicm
 *
 */
public class ServerGame extends Thread{

	private static final int PORTSERVER = 6060;
	private static final int PORTCLIENT = 6969;
	private JPanel contentPane;
	private DatagramSocket socketUDP=null;
	private Movimiento movimiento;
	private byte[] enviados;	
	private byte[] recibidos;
	private DatagramPacket dprecibo;
	private DatagramPacket dpenvio;
	private ArrayList<Socket> sockets;
	private Player player;
	private Mesa mesa;
	private Botiquin boti;
	private Maquina maquina;
	private Disparo disparo;
	private ArrayList<Player> players;
	private ArrayList<Disparo> disparos;
	private ArrayList<Mesa> mesas;
	private ArrayList<Maquina> maquinas;
	private ArrayList<Zombie> zombies;
	private ArrayList<ZombieBig> zombiesbig;
	private ArrayList<Botiquin> botiquines;
	public boolean isRunning = true;
	public boolean isPause;
	private GameData gameData;
	private GameLoop gameLoop;
	private int moverplayer;
	private boolean zombiecolisiona=false;
	private boolean zombiecolisiona2=false;
	private final static int VECESMOVERPLAYER = 6;
	private int velocidadzombie=15;
	private int nuevaoleada;
	private int zombiesnacen=2;
	private int tiempooleada = 100;
	private int ronda = 0;
	private Zombie zombie;
	private int vidas = 0;
	private int x = 806;
	private int pjvivos, maxpaciencia=10;
	private ArrayList<String> nicks;
	private int dific;
	private boolean enablecompra;
	private int vidazombie=10;
	private int numerobigs=0;
	/**
	 * Create the frame.
	 * @param nicks 
	 * @param dific 
	 */
	public ServerGame(ArrayList<Socket> sockets, ArrayList<String> nicks, int dific) {
		this.sockets = sockets;
		this.nicks = nicks;
		this.dific=dific;
		Assets.loadAssets();
		gameData = new GameData();
		players = new ArrayList<Player>();
		disparos = new ArrayList<Disparo>();
		mesas = new ArrayList<Mesa>();
		maquinas = new ArrayList<Maquina>();
		zombies = new ArrayList<Zombie>();
		zombiesbig = new ArrayList<ZombieBig>();
		botiquines = new ArrayList<Botiquin>();
		mesa = new Mesa(540, 300 , 303, 207);
		mesas.add(mesa);
		mesa = new Mesa(1071, 530 , 250, 450);
		mesas.add(mesa);
		maquinas.add(new Maquina(90, 250, 151, 93, 500, 0));
		maquinas.add(new Maquina(550, 950, 93, 151, 2000, 1));
		maquinas.add(new Maquina(1272, 90, 93, 151, 3000, 2));
		for(int i = 0; i < sockets.size(); i++) {
			player = new Player();
			player.x = x;
			x+=100;
			player.y = 228;
			players.add(player);
			player.nick = nicks.get(i);
		}
		if(dific==1) vidas=2;
		if(dific==2) vidas=1;
		if(dific==3) vidas=0;
		pjvivos = players.size();
		gameData.setPlayers(players);
		gameData.setDisparos(disparos);
		gameData.setMesas(mesas);
		gameData.setMaquinas(maquinas);
		gameData.setVidas(vidas);
		gameData.setZombies(zombies);
		gameData.setRondas(ronda);
		gameData.setPjvivos(pjvivos);
		gameData.setSounddisparo(false);
		gameData.setSoundgameover(false);
		gameData.setSoundcash(false);
		gameData.setBoti(botiquines);
		
		gameLoop = new GameLoop(this);
		gameLoop.start();
		try {
			socketUDP = new DatagramSocket(PORTSERVER);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		// Lanzamos el hilo que acepta nuevas conexiones
		// Recordamos que el hilo es la propia clase runnable (this)
		setName("Server");
		start();
	}

	@Override
	public void run() {
		// Recibiendo teclas de los clientes
		while (true) {
			recibidos = new byte[32768];
			// Recibimos del Cliente
			dprecibo= new DatagramPacket(recibidos, recibidos.length);
			try {
				socketUDP.receive(dprecibo);
				ByteArrayInputStream byteStream = new ByteArrayInputStream(recibidos);
				ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));
				movimiento = (Movimiento) is.readObject();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (isJugador(dprecibo.getAddress().getHostAddress())) {
				if (!(movimiento.getJugador()>=0)) {break;}
				updatePlayer(movimiento);
			} else continue;
		}
		
		// Cerrando socket
		socketUDP.close();
	}

	private void updatePlayer(Movimiento movi) {
		if(gameData.getPlayers().size()>0) {
		Player player= gameData.getPlayers().get(movi.getJugador()-1);
		switch (movi.getMove()) {
		case 0:
			player.up();
			break;
		case 2:
			player.right();
			break;
		case 4:
			player.down();
			break;
		case 6:
			player.left();
			break;
		case 1:
			player.right();
			player.up();
			player.diagonal();
			break;
		case 3:
			player.right();
			player.down();
			player.diagonal();
			break;
		case 5:
			player.left();
			player.down();
			player.diagonal();
			break;
		case 7:
			player.left();
			player.up();
			player.diagonal();
			break;
		case 8:
			player.estatico();
			break;
		case 9:
			if(player.vivo) {
				disparo = new Disparo(player);
				disparos.add(disparo);
				gameData.setDisparos(disparos);
				if(player.arma==1) {
					disparo = new Disparo(player);
					disparo.retrasar();
					disparos.add(disparo);
					gameData.setDisparos(disparos);
				}
				gameData.setSounddisparo(true);
				if(player.arma!=0) {
					player.municion--;
				}
			}
			break;
		case 10:
			enablecompra=true;
			break;
		case 11:
			player.arma=0;
			if(player.mejora0!=-1) {
				player.vecesdisparo=player.mejora0;
			}else
				player.vecesdisparo = 20;
			break;
		case 12:
			if(player.dosarmas) {
				player.arma = player.arma2;
				if(player.arma==1) {
					player.vecesdisparo=player.mejora1;
				}else if(player.arma==1){
					player.vecesdisparo=player.mejora2;
				}
			}else {
				player.arma = 0;
				if(player.mejora0!=-1) {
					player.vecesdisparo=player.mejora0;
				}else
					player.vecesdisparo = 20;
			}
		default:
			player.estatico();
		}
		
		for(Mesa mesa : gameData.getMesas())
		if(player.colisionMesas(mesa)) {
			player.block();
		}
		for(Maquina maquina : gameData.getMaquinas()) {
			if(player.colisionMaquinas(maquina)) {
				player.block();
				if(player.puntos >= maquina.precio && enablecompra) {
					enablecompra=false;
					gameData.setSoundcash(true);
					player.puntos -= maquina.precio;
					if(maquina.arma==0) {
						player.arma=0;
						player.vecesdisparo=19;
						if(player.mejora0!=-1) {
							if(player.mejora0>15) {
								player.mejora0--;
							}
							player.vecesdisparo=player.mejora0;
						}else {
							player.mejora0=player.vecesdisparo;
						}
					}else if(maquina.arma==1) {
						if(player.arma!=1) {
							player.arma=1;
							player.arma2=1;
							player.dosarmas=true;
							if(player.mejora1!=-1) {
								player.mejora1--;
								player.vecesdisparo=player.mejora1;
							}else {
								player.vecesdisparo=15;
								player.mejora1=player.vecesdisparo;
							}
							player.municion=30;
						}else {
							if(player.vecesdisparo>12) {
								player.vecesdisparo--;
								player.mejora1=player.vecesdisparo;
							}
							player.municion+=30;
						}
					}else if(maquina.arma==2) {
						if(player.arma!=2) {
							player.arma=2;
							player.dosarmas=true;
							player.arma2=2;
							if(player.mejora2!=-1) {
								player.vecesdisparo=player.mejora2;
							}else
								player.vecesdisparo=10;
							player.municion=30;
						}else {
							if(player.vecesdisparo>6) {
								player.vecesdisparo--;
								player.mejora2=player.vecesdisparo;
							}
							player.municion+=40;
							}
						}
					}
				
				}
			}
		}
	}

	private boolean isJugador(String host) {
		for (Socket socket:sockets) {
			String hostAdddress = socket.getInetAddress().getHostAddress();
			if ( hostAdddress.equals(host)) { 
				return true;
			}
		}
		return false;
	}

	private void enviarall(GameData movimiento) {
		// Enviamos a todos los Clientes
		for (Socket socket:sockets) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(movimiento);
				enviados = baos.toByteArray();
				dpenvio= new DatagramPacket(enviados, enviados.length, socket.getInetAddress(), PORTCLIENT);
				socketUDP.send(dpenvio);
				oos.close();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
	}

	public void update() {
		moverplayer = ++moverplayer % VECESMOVERPLAYER;
		if (moverplayer==0) {
			for (int i=0; i<players.size(); i++) {
				player=players.get(i);
				if(!player.vivo)
					continue;
				player.update();
				
				for(Zombie zombie : zombies) {
					if(player.colisionZombie(zombie)) {
						gameData.setSounddead(true);
						if(vidas>0) {
							vidas--;
							zombies.removeAll(zombies);
							zombiesbig.removeAll(zombiesbig);
							break;
						}else {
							player.vivo=false;
							pjvivos -- ;
							break;
						}
					}
				}
				for(ZombieBig zombieb : zombiesbig) {
					if(player.colisionZombieb(zombieb)) {
						gameData.setSounddead(true);
						if(vidas>0) {
							vidas--;
							zombies.removeAll(zombies);
							zombiesbig.removeAll(zombiesbig);
							break;
						}else {
							player.vivo=false;
							pjvivos -- ;
							break;
						}
					}
				}
				
				for(Botiquin botiquin : botiquines) {
					if(botiquin.colision(player)) {
						vidas++;
						gameData.setSoundheal(true);
						botiquines.remove(botiquin);
						break;
					}
				}
			}
			for(Zombie zombie : zombies) {
				zombie.update();
				for(Mesa mesa : gameData.getMesas()) {
					if(zombie.colisionMesas(mesa)) {
						zombie.chocar(mesa);
						zombiecolisiona = true;
						break;
					}else {
						zombiecolisiona = false;
					}
				}
				for(Maquina maquina : gameData.getMaquinas()) {
					if(zombie.colisionMaquinas(maquina)) {
						zombie.chocar(maquina);
						zombiecolisiona2 = true;
						break;
					}else {
						zombiecolisiona2 = false;
					}
				}
				if(!zombiecolisiona && !zombiecolisiona2) {
					zombie.moove();
				}
			}
			for(ZombieBig zombieb : zombiesbig) {
				zombieb.update();
				for(Mesa mesa : gameData.getMesas()) {
					if(zombieb.colisionMesas(mesa)) {
						zombieb.chocar(mesa);
						zombiecolisiona = true;
						break;
					}else {
						zombiecolisiona = false;
					}
				}
				for(Maquina maquina : gameData.getMaquinas()) {
					if(zombieb.colisionMaquinas(maquina)) {
						zombieb.chocar(maquina);
						zombiecolisiona2 = true;
						break;
					}else {
						zombiecolisiona2 = false;
					}
				}
				if(!zombiecolisiona && !zombiecolisiona2) {
					zombieb.moove();
				}
			}
			
		}
		for (int i = 0 ; i<disparos.size(); i++) {
			disparo = disparos.get(i);
			if(disparo.x > 2000 || disparo.y > 1500 || disparo.x <-50 || disparo.y <-50 ) {
				disparos.remove(disparo);
				i--;
				continue;
			}else
				disparo.update();
			for(Zombie zombie : zombies) {
				if(zombie.colisionDisparo(disparo)) {
					disparos.remove(disparo);
					i--;
					player = disparo.player;
					player.puntos+=100;
					int n =(int) (Math.random()*300);
					if(n==1 && (dific==3 || dific==1 || dific==2)) {
						boti = new Botiquin(zombie.x, zombie.y);
						botiquines.add(boti);
					}else if(n==69 && (dific==1 || dific==2)) {
						boti = new Botiquin(zombie.x, zombie.y);
						botiquines.add(boti);
					}else if(n==169 && dific==1) {
						boti = new Botiquin(zombie.x, zombie.y);
						botiquines.add(boti);
					}
					
					zombies.remove(zombie);
					break;
				}
			}
		}
		for (int i = 0 ; i<disparos.size(); i++) {
			disparo = disparos.get(i);
			for(ZombieBig zombieb : zombiesbig) {
				if(zombieb.colisionDisparo(disparo)) {
					disparos.remove(disparo);
					i--;
					if(zombieb.vida<1) {
						zombiesbig.remove(zombieb);
						break;
					}else {
						player = disparo.player;
						player.puntos+=50;
						zombieb.vida--;
					}
					player = disparo.player;
					player.puntos+=50;
					break;
				}
			}
		}
		
		
		
		if (zombies.isEmpty() && zombiesbig.isEmpty()) {
			nuevaoleada = ++nuevaoleada % tiempooleada;
			if(nuevaoleada==0) {
				ronda+=1;
				
				for(Player player : players) {
					player.puntos+=200;
				}
				for(int i = 0 ; i< zombiesnacen ; i++) {
					player = players.get((int) (Math.random()*players.size()));
					while(!player.vivo) {
						player = players.get((int) (Math.random()*players.size()));
					}
					
					zombie = new Zombie(player,velocidadzombie);
					zombie.maxpaciencia = maxpaciencia;
					if(maxpaciencia>2) {
						maxpaciencia--;
					}
					zombies.add(zombie);
				}
				zombiesnacen+=dific;
				velocidadzombie+=dific;
				if(ronda%5==0) {
					numerobigs++;
					gameData.setSoundevil(true);
					for(int i = 0; i<numerobigs;i++) {
						player = players.get((int) (Math.random()*players.size()));
						while(!player.vivo) {
							player = players.get((int) (Math.random()*players.size()));
						}
						ZombieBig zombibig = new ZombieBig(player, velocidadzombie, vidazombie+dific);
						zombiesbig.add(zombibig);
					}
				}
				if(tiempooleada<250) {
					tiempooleada+=50;
				}
			}
		}
		
		gameData.setZombiesbig(zombiesbig);
		gameData.setVidas(vidas);
		gameData.setPlayers(players);
		gameData.setZombies(zombies);
		gameData.setDisparos(disparos);
		gameData.setRondas(ronda);
		gameData.setPjvivos(pjvivos);
		gameData.setBoti(botiquines);
	}

	public void sendWorld() {
		String json = new Gson().toJson(gameData);
		enviados = json.getBytes();
		
		for (Socket socket : sockets) {
			dpenvio= new DatagramPacket(enviados, enviados.length, socket.getInetAddress(), PORTCLIENT);
			try {
				socketUDP.send(dpenvio);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		gameData.setSounddisparo(false);
		gameData.setSoundheal(false);
		gameData.setSounddead(false);
		gameData.setSoundcash(false);
		gameData.setSoundevil(false);
	}
}
