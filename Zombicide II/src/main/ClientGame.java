package main;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;

import model.GameData;
import model.Movimiento;
import util.Assets;

public class ClientGame extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private static final int PORTSERVER = 6060;
	private static final int PORTCLIENT = 6969;
	private static DatagramSocket socketUDP=null;
	private static byte[] enviados;	
	private byte[] recibidos;
	private DatagramPacket dprecibo;
	private static DatagramPacket dpenvio;
	private Container container;
	private JPanel contentPane;
	private String jugador;
	private static InetAddress direccion;
	private Thread hilo;
	private PanelGame panelGame;	
	public GameData gameData;

	public ClientGame(String jugador, int njugador, String ip) {
		this.jugador = jugador;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1920, 1080);
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		
		container = getContentPane();
		container.setLayout(new BorderLayout(0, 0));
		
		panelGame = new PanelGame(njugador);
		
		container.add(panelGame, BorderLayout.CENTER);
		

		Assets.loadAssets();
		
		try {
			socketUDP = new DatagramSocket(PORTCLIENT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		try {
			direccion = InetAddress.getByName(ip);
			//direccion = InetAddress.getByName("192.168.1.37");
			//direccion = InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		setVisible(true);
		hilo = new Thread(this);
		hilo.start();
	}

	

	protected static void enviar(Movimiento movimiento) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(movimiento);
			enviados = baos.toByteArray();
			dpenvio= new DatagramPacket(enviados, enviados.length, direccion, PORTSERVER);
			socketUDP.send(dpenvio);
			baos.close();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		while (true) {
			recibidos = new byte[32768];
			dprecibo= new DatagramPacket(recibidos, recibidos.length);
			try {
				socketUDP.receive(dprecibo);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String json = new String(dprecibo.getData()).trim();
			gameData = new Gson().fromJson(json, GameData.class);
			panelGame.update(gameData);
			panelGame.repaint();
		}
	}
}
