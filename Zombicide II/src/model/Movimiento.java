package model;

import java.io.Serializable;

public class Movimiento implements Serializable{
	private static final long serialVersionUID = 1L;
	private int jugador;
	private int move;
	
	public Movimiento() {
	}
	public Movimiento(int jugador, int move) {
		this.jugador = jugador;
		this.move = move;
	}
	public int getJugador() {
		return jugador;
	}
	public void setJugador(int jugador) {
		this.jugador = jugador;
	}
	public int getMove() {
		return move;
	}
	public void setMove(int move) {
		this.move = move;
	}
	@Override
	public String toString() {
		return "Movimiento [jugador=" + jugador + ", move=" + move + "]";
	}
}
