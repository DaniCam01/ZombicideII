package main;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import conection.ClientConnection;
import conection.ServerConnection;
import util.Assets;

import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.awt.event.ActionEvent;

public class Game extends JFrame {

	private JPanel contentPane;

	private Image img;
	private JTextField tfNick;
	private String ipServer;

	private String dificultad;

	private int dific;

	private String jugadores;

	public static AudioClip bsoinisound;


	public static JLabel lblNotify;
	public static JLabel lblIp;
	public static JLabel lblJugadores;

	public static JLabel lblConectado;
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Game frame = new Game();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Game() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 485, 795);
		Assets.loadAssets();
		img = Assets.iifondoini.getImage();
		contentPane = new JPanel() {
	         @Override
	         public void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            g.drawImage(img, 0, 0, null);
	         }
	      };
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tfNick = new JTextField();
		tfNick.setHorizontalAlignment(SwingConstants.CENTER);
		tfNick.setFont(new Font("Verdana", Font.PLAIN, 20));
		tfNick.setBounds(129, 536, 204, 55);
		contentPane.add(tfNick);
		tfNick.setColumns(10);
		
		JLabel lblNick = new JLabel("NICK");
		lblNick.setForeground(UIManager.getColor("Button.highlight"));
		lblNick.setHorizontalAlignment(SwingConstants.CENTER);
		lblNick.setFont(new Font("Verdana", Font.PLAIN, 20));
		lblNick.setBounds(160, 488, 135, 49);
		contentPane.add(lblNick);
		
		JButton btnCrearPartida = new JButton("Crear Partida");
		btnCrearPartida.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createGame();
			}
		});
		btnCrearPartida.setForeground(Color.WHITE);
		btnCrearPartida.setBackground(Color.DARK_GRAY);
		btnCrearPartida.setFont(new Font("Verdana", Font.PLAIN, 18));
		btnCrearPartida.setBounds(10, 648, 161, 74);
		contentPane.add(btnCrearPartida);
		JButton btnUnirse = new JButton("Unirse");
		btnUnirse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				joinGame();
			}
		});
		btnUnirse.setForeground(Color.WHITE);
		btnUnirse.setBackground(Color.DARK_GRAY);
		btnUnirse.setFont(new Font("Verdana", Font.PLAIN, 18));
		btnUnirse.setBounds(298, 648, 161, 74);
		contentPane.add(btnUnirse);
		
		lblJugadores = new JLabel("Jugadores:");
		lblJugadores.setForeground(Color.WHITE);
		lblJugadores.setVerticalAlignment(SwingConstants.TOP);
		lblJugadores.setBounds(379, 138, 80, 188);
		lblJugadores.setVisible(false);
		contentPane.add(lblJugadores);
		
		lblNotify = new JLabel("CONECTANDO");
		lblNotify.setEnabled(false);
		lblNotify.setHorizontalAlignment(SwingConstants.CENTER);
		lblNotify.setForeground(Color.GREEN);
		lblNotify.setBounds(103, 438, 261, 14);
		contentPane.add(lblNotify);
		
		lblIp = new JLabel("");
		lblIp.setEnabled(false);
		lblIp.setForeground(Color.WHITE);
		lblIp.setBounds(10, 11, 109, 14);
		contentPane.add(lblIp);
		
		lblConectado = new JLabel("Conectado");
		lblConectado.setForeground(Color.GREEN);
		lblConectado.setHorizontalAlignment(SwingConstants.RIGHT);
		lblConectado.setBounds(379, 11, 80, 14);
		contentPane.add(lblConectado);
		lblNotify.setVisible(false);
		lblConectado.setVisible(false);
		repaint();
		loadSound();
		bsoinisound.loop();
	}
	
	protected void createGame() {
		if (tfNick.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Debes introducir un Nick");
			return;
		}
		do {
		dificultad = JOptionPane.showInputDialog(this, "Nivel de dificultad (1-3)");
		
		if(dificultad.equals("1"))
			dific = 1;
		else if(dificultad.equals("2"))
			dific = 2;
		else if(dificultad.equals("3"))
			dific = 3;
		}while(dific!=1 && dific!=2 && dific!=3);
		
		int nj=0;
		do {
			jugadores = JOptionPane.showInputDialog(this, "Numero de jugadres (Max 4)");
			
			if(jugadores.equals("1"))
				nj = 1;
			else if(jugadores.equals("2"))
				nj = 2;
			else if(jugadores.equals("3"))
				nj = 3;
			else if(jugadores.equals("4"))
				nj = 4;
			}while(nj!=1 && nj!=2 && nj!=3 && nj!=4);
		
		
		new ServerConnection(dific, nj);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new ClientConnection("127.0.0.1", tfNick.getText());
	}
	protected void joinGame() {
		if (tfNick.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Debes introducir un Nick");
			return;
		}else {
			ipServer = JOptionPane.showInputDialog(this, "Introduce la ip del host");
		}
		//ipServer="localhost";
		new ClientConnection(ipServer, tfNick.getText());
	}
	
	private void loadSound() {
		try {
			bsoinisound = Applet.newAudioClip(new File("assets/bsoinisound.wav").toURI().toURL());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
