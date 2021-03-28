package conection;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import main.ClientGame;
import main.Game;

public class ClientConnection extends Thread {

	private Socket socket;
	private BufferedReader br;
	private boolean fin=false;
	private PrintWriter pw;
	private String ip;
	private int njugador;
	private static final int PORTSERVER = 6000;

	public ClientConnection(String ip, String nick) {
		this.ip = ip;
		setName(nick);
		try {
			socket = new Socket(ip, PORTSERVER);
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}
        // Creamos los streams de entrada/salida
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("No se pudieron crear los canales");
			System.exit(0);
		}
		// Nada mas conectarnos, enviamos nuestro nombre
		pw.write(nick+ "\n");
		pw.flush();
		start();		
	}

	@Override
	public void run() {
		while (!fin) {
			try {
				String cadena = br.readLine();
				if (cadena!=null) {
					//Game.taInfo.append(cadena+"\n");
					if (cadena.contains("START")) {
						fin = true;
					}else if(cadena.contains("Jugadores:")) {
						Game.lblJugadores.setVisible(true);
						Game.lblJugadores.setText("<html>"+cadena+"</html>");
					}else if(cadena.contains("ACCEPT")) {
						Game.lblConectado.setVisible(true);
						String nj = br.readLine();
						Game.lblNotify.setVisible(true);;
						if(nj!=null) {
							njugador = Integer.valueOf(nj);
						}
					}else if(cadena.contains("PREPARADOS")) {
						Game.lblNotify.setText("Jugadores listos La partida va a comenzar");
					}else if(cadena.contains("Esperando")) {
						Game.lblNotify.setText(cadena);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Comienza el juego //////////////////
		Game.bsoinisound.stop();
		new ClientGame(getName(),njugador, ip);
	}

}
