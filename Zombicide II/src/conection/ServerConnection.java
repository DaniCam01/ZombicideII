package conection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import main.Game;
import main.ServerGame;

/**
 * 
 * @author danicm
 *
 */
public class ServerConnection extends Thread {

	private ServerSocket serverSocket; Thread hilo;
	private int countConex=0;
	private Socket socket;
	private ArrayList<Socket> sockets;
	private PrintWriter pw;
	private BufferedReader brForNick;
	private String nick;
	private static final int PORTSERVER = 6000;
	private  int maxcli;
	private String jugadorestxt = "Jugadores:";
	private String esperandotxt = "";
	private ArrayList<String> nicks;
	private int dific;

	public ServerConnection(int dific, int nj) {
		// Iniciamos el server
		this.dific = dific;
		this.maxcli = nj;
		try {
			serverSocket = new ServerSocket(PORTSERVER);
			Game.lblIp.setText(serverSocket.getLocalSocketAddress().toString());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		sockets = new ArrayList<Socket>();
		nicks = new ArrayList<String>();
		setName("Server");
		start();
	}

	@Override
	public void run() {
		while (countConex < maxcli) {
			try {
				socket = serverSocket.accept();
				// Según el protocolo, sabemos que nada más conectar
				// el cliente nos envia su nick
				try {
					brForNick = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					nick = brForNick.readLine();
					nicks.add(nick);
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Avisemos al nuevo Jugador
				enviar("ACCEPT", socket);
				
				//actualizamos jugadores
				jugadorestxt = jugadorestxt + "<br/>" + nick;
	
				countConex++;
				esperandotxt= "Esperando jugadores "+countConex +"/"+maxcli;
				enviar(String.valueOf(countConex), socket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1); // Problemas => a la calle
			}
			sockets.add(socket);
			enviarall(jugadorestxt);
			enviarall(esperandotxt);
		}
		enviarall("PREPARADOS");
		
		try {
			Thread.sleep(2000);
			new ServerGame(sockets, nicks, dific);
			enviarall("START");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void enviar(String msg, Socket socket) {
		try {
			pw = new PrintWriter(socket.getOutputStream());
			pw.write(msg + "\n");
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void enviarall(String msg) {
		for (Socket socket:sockets) {
			try {
				pw = new PrintWriter(socket.getOutputStream());
				pw.write(msg + "\n");
				pw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
}
